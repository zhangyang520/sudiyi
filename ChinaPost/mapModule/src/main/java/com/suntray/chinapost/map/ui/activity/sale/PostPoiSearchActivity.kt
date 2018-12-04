package com.suntray.chinapost.map.ui.activity.sale

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.Marker
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.data.dao.AdStyleDao
import com.suntray.chinapost.baselibrary.data.dao.ResourceTypeDao
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.rx.hasTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.ut.base.utils.AppPrefsUtils
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.map.data.constants.MapContstants
import com.suntray.chinapost.map.data.request.ProvinceDotRequest
import com.suntray.chinapost.map.data.request.RadiusDotRequest
import com.suntray.chinapost.map.data.request.UpdateRequest
import com.suntray.chinapost.map.data.response.UpdateResponse
import com.suntray.chinapost.map.injection.component.DaggerMapComponent
import com.suntray.chinapost.map.presenter.MapPresenter
import com.suntray.chinapost.map.presenter.view.MapView
import com.suntray.chinapost.map.ui.dialog.IOkClickListener
import com.suntray.chinapost.map.ui.dialog.SettingDialog
import com.suntray.chinapost.map.utils.AMapUI
import com.suntray.chinapost.map.utils.AMapUI.detailMarker
import com.suntray.chinapost.map.utils.AMapUI.mlastMarker
import com.suntray.chinapost.map.utils.AMapUI.resetlastmarker
import com.suntray.chinapost.map.utils.ASettingUtils
import com.suntray.chinapost.map.utils.UpdateUtils
import com.suntray.chinapost.provider.RouterPath
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_map_search.*
import java.util.*

/**
 *  搜索的主界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/13 14:14
 */
@Route(path =RouterPath.MapModule.POST_POI_SEARCH)
class PostPoiSearchActivity:BaseMvpActivity<MapPresenter>(),MapView, AMap.OnMarkerClickListener {

    var mode:Int=0 //0:为 自身定位周边的模式  1:区域的模式 + 模糊搜索的模式
    var currntLocation:AMapLocation?=null
    var currentRadius:Int=1000;//默认当前半径
    var currentTip:Tip?=null
    var isCanAccess=true //是否能够访问
    override fun initView() {
        isBlackShow = false
        isTitleShow = false
        isTitleShow = false

        hud2= KProgressHUD(this@PostPoiSearchActivity).setLabel("区域数据获取中").setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        rl_dingwei_area.setOnClickListener({

            if (currentMapDot != null) {
                currentMapDot!!.clear()
            }
            et_input_search.setText("")
            if (mode == 0) {
                mode = 1
                aMap!!.clear()
                //请求
            } else {
                mode = 0
                aMap!!.clear()
                //进行重新定位 并且画圈
                AMapUI.locationMap(this@PostPoiSearchActivity,aMap!!, LatLonPoint(currntLocation!!.latitude,currntLocation!!.longitude))
                //暂时不画
                doRequestRadius()
            }
            setModeDotAndArea()
        })
        /**
         * 跳转到 预定点位清单 列表界面
         */
        iv_finger.setOnClickListener(View.OnClickListener {
            //判断 当前获取到的数据有无?
            if (currentMapDot != null &&
                    currentMapDot!!.size > 0) {
                if (currentMapDot!!.size < 50) {
                    ARouter.getInstance()
                            .build(RouterPath.MapModule.POST_AD_RESERVED_LIST)
                            .navigation(this@PostPoiSearchActivity)
                } else {
                    ToastUtil.makeText(this@PostPoiSearchActivity, "点位的数据不能超过50个")
                }
            } else {
                ToastUtil.makeText(this@PostPoiSearchActivity, "暂无点位数据")
            }
        })

        iv_map_area.setOnClickListener({
            if(AMapUI.window!=null && AMapUI.window!!.isShowing){
                //当前正在展示
                ToastUtil.makeText(this@PostPoiSearchActivity,"不能重复点击展示")
            }else if(basePresenter.isProvinceRequesting){
                //如果正在请求
                ToastUtil.makeText(this@PostPoiSearchActivity,"正在请求中")
            }else{
                basePresenter.province(CityListAction.ProvinceAtion.getCityValue1().toString(), -1, -1, CityListAction.ProvinceAtion)
            }
        })

        //初始化 模式
        setModeDotAndArea()
        //我的模块的跳转
        iv_user_head.setOnClickListener {
            ARouter.getInstance().build(RouterPath.MineModule.MINE_ACTIVITY).navigation(this@PostPoiSearchActivity)
        }

        iv_chat.setOnClickListener({
            ARouter.getInstance().build(RouterPath.MineModule.MINE_MESSAGE).navigation(this@PostPoiSearchActivity)
        })

        btn_dis_1.setOnClickListener({
            currentRadius = 1000
            btn_dis_1.setTextColor(Color.parseColor("#0d7aff"))
            btn_dis_3.setTextColor(resources.getColor(R.color.gray))
            btn_dis_5.setTextColor(resources.getColor(R.color.gray))
            btn_dis_8.setTextColor(resources.getColor(R.color.gray))
            btn_dis_10.setTextColor(resources.getColor(R.color.gray))
            doRequestRadius()
        })
        btn_dis_3.setOnClickListener({
            //当前 半径点击
            currentRadius = 3000
            btn_dis_1.setTextColor(resources.getColor(R.color.gray))
            btn_dis_3.setTextColor(Color.parseColor("#0d7aff"))
            btn_dis_5.setTextColor(resources.getColor(R.color.gray))
            btn_dis_8.setTextColor(resources.getColor(R.color.gray))
            btn_dis_10.setTextColor(resources.getColor(R.color.gray))
            doRequestRadius()
        })

        btn_dis_5.setOnClickListener({
            btn_dis_1.setTextColor(resources.getColor(R.color.gray))
            btn_dis_5.setTextColor(Color.parseColor("#0d7aff"))
            btn_dis_3.setTextColor(resources.getColor(R.color.gray))
            btn_dis_8.setTextColor(resources.getColor(R.color.gray))
            btn_dis_10.setTextColor(resources.getColor(R.color.gray))
            currentRadius = 5000
            doRequestRadius()
        })

        btn_dis_8.setOnClickListener({
            btn_dis_1.setTextColor(resources.getColor(R.color.gray))
            btn_dis_8.setTextColor(Color.parseColor("#0d7aff"))
            btn_dis_3.setTextColor(resources.getColor(R.color.gray))
            btn_dis_5.setTextColor(resources.getColor(R.color.gray))
            btn_dis_10.setTextColor(resources.getColor(R.color.gray))
            currentRadius = 8000
            doRequestRadius()
        })

        btn_dis_10.setOnClickListener({
            btn_dis_1.setTextColor(resources.getColor(R.color.gray))
            btn_dis_10.setTextColor(Color.parseColor("#0d7aff"))
            btn_dis_3.setTextColor(resources.getColor(R.color.gray))
            btn_dis_5.setTextColor(resources.getColor(R.color.gray))
            btn_dis_8.setTextColor(resources.getColor(R.color.gray))
            currentRadius = 10000
            doRequestRadius()
        })

        et_input_search.addTextChangedListener(object:TextWatcher, Inputtips.InputtipsListener {


            override fun onGetInputtips(tipList: MutableList<Tip>?, rCode: Int) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if(tipList!=null && tipList.size>0){
                        inputlist.visibility=View.VISIBLE
                        val listString = java.util.ArrayList<HashMap<String, String>>()
                        var listPoint= arrayListOf<Tip>()
                        if (tipList != null) {
                            val size = tipList.size
                            for (i in 0 until size) {
                                val tip = tipList.get(i)
                                if (tip != null && tip!!.point!=null) {
                                    val map = HashMap<String, String>()
                                    map["name"] = tipList.get(i).getName()
                                    map["address"] = tipList.get(i).getDistrict()
                                    listString.add(map)
                                    listPoint.add(tip)
                                }
                            }
                            val aAdapter = SimpleAdapter(applicationContext, listString, R.layout.item_layout,
                                    arrayOf("name", "address"), intArrayOf(R.id.poi_field_id, R.id.poi_value_id))

                            inputlist.divider=applicationContext.resources.getDrawable(R.drawable.base_bg_line)
                            inputlist.dividerHeight=AutoUtils.getPercentHeightSize(2)
                            inputlist.setAdapter(aAdapter)
                            inputlist.setOnItemClickListener(object:AdapterView.OnItemClickListener{
                                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                    currentTip=listPoint.get(position)
                                    SystemUtil.printlnStr("currentTip point isnull:"+(currentTip!!.point==null))
                                    inputlist.visibility=View.GONE
                                    isCanAccess=false
                                    et_input_search.setText(currentTip!!.name)
                                    //访问 半径请求
                                    doRequestRadius()
                                }
                            })
                            aAdapter.notifyDataSetChanged()
                        }
                    }else{
                        inputlist.visibility=View.GONE
                    }
                } else {
                    inputlist.visibility=View.GONE
                    ToastUtil.makeText(this@PostPoiSearchActivity, "暂无搜索地理数据")
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //内容改变时
                if(isCanAccess){
                    if(AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)==1){
                        //地理位置 查询高德地图的api
                        val newText = s.toString().trim { it <= ' ' }
                        val inputquery = InputtipsQuery(newText, "北京")
                        inputquery.cityLimit = false
                        val inputTips = Inputtips(this@PostPoiSearchActivity, inputquery)
                        inputTips.setInputtipsListener(this)
                        inputTips.requestInputtipsAsyn()
                    }
                }else{
                    isCanAccess=true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        iv_setting.setOnClickListener({
            try {
                var dialog=SettingDialog(this@PostPoiSearchActivity,R.style.ActionSheetDialogStyle)
                //获取当前Activity所在的窗体
                var  dialogWindow = dialog.getWindow();
                //设置Dialog从窗体底部弹出
                dialogWindow.getDecorView().setPadding(AutoUtils.getPercentWidthSize(70), 0, 0, 0);
                var lp = dialogWindow.getAttributes();
                //设置宽
                lp.width = AutoUtils.getPercentWidthSize(750)
                //设置高
                lp.height = AutoUtils.getPercentHeightSize(1334)

                dialogWindow.setAttributes(lp);
                //显示对话框
                dialogWindow.setGravity(Gravity.RIGHT);
                //设置dialog的动画效果
                dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
                dialog.show()
                if(mode==0){
                    //自身定位
                    dialog.setKeyword(true)
                }else{
                    //区域模式
                    dialog.setKeyword(false)
                }
                dialog.listenr=object:IOkClickListener{
                    override fun onDotNameClickListener() {
                        //点位名称
                        //点位名称
                        //将 位置列表 隐藏:
                        inputlist.visibility=View.GONE
                        var startTime=AppPrefsUtils.getString(MapContstants.SETTING_STARTTIME,
                                DateUtil.dateFormat(Calendar.getInstance().time))
                        var endTime=AppPrefsUtils.getString(MapContstants.SETTING_ENDTIME,
                                DateUtil.dateFormat(Calendar.getInstance().time))
                        basePresenter.radiusDot(RadiusDotRequest(currntLocation!!.longitude,currntLocation!!.latitude,
                                currentRadius.toDouble(),et_input_search.getTxt(),
                                "0",1,30, UserDao.getLocalUser().id,
                                currntLocation!!.adCode.toInt(),-1,AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)+1,
                                AppPrefsUtils.getInt(MapContstants.SETTING_ADTYPEID,-1),
                                AppPrefsUtils.getString(MapContstants.SETTING_RESOURCEIDS,"[]"),startTime,endTime))
                    }

                    override fun onPositionClickListener() {
                        if(!et_input_search.getTxt().equals("")){
                            //不为空 进行搜索
                            //地理位置 查询高德地图的api
                            val inputquery = InputtipsQuery(et_input_search.getTxt(), "北京")
                            inputquery.cityLimit = false
                            val inputTips = Inputtips(this@PostPoiSearchActivity, inputquery)
                            inputTips.setInputtipsListener(object :Inputtips.InputtipsListener{
                                override fun onGetInputtips(tipList: MutableList<Tip>?, rCode: Int) {
                                    if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                                        if(tipList!=null && tipList.size>0){
                                            inputlist.visibility=View.VISIBLE
                                            val listString = java.util.ArrayList<HashMap<String, String>>()
                                            var listPoint= arrayListOf<Tip>()
                                            if (tipList != null) {
                                                val size = tipList.size
                                                for (i in 0 until size) {
                                                    val tip = tipList.get(i)
                                                    if (tip != null && tip!!.point!=null) {
                                                        val map = HashMap<String, String>()
                                                        map["name"] = tipList.get(i).getName()
                                                        map["address"] = tipList.get(i).getDistrict()
                                                        listString.add(map)
                                                        listPoint.add(tip)
                                                    }
                                                }
                                                val aAdapter = SimpleAdapter(applicationContext, listString, R.layout.item_layout,
                                                        arrayOf("name", "address"), intArrayOf(R.id.poi_field_id, R.id.poi_value_id))

                                                inputlist.divider=applicationContext.resources.getDrawable(R.drawable.base_bg_line)
                                                inputlist.dividerHeight=AutoUtils.getPercentHeightSize(2)
                                                inputlist.setAdapter(aAdapter)
                                                inputlist.setOnItemClickListener(object:AdapterView.OnItemClickListener{
                                                    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                                        currentTip=listPoint.get(position)
                                                        SystemUtil.printlnStr("currentTip point isnull:"+(currentTip!!.point==null))
                                                        inputlist.visibility=View.GONE
                                                        isCanAccess=false
                                                        et_input_search.setText(currentTip!!.name)
                                                        //访问 半径请求
                                                        doRequestRadius()
                                                    }
                                                })
                                                aAdapter.notifyDataSetChanged()
                                            }
                                        }else{
                                            inputlist.visibility=View.GONE
                                        }
                                    } else {
                                        inputlist.visibility=View.GONE
                                        ToastUtil.makeText(this@PostPoiSearchActivity, "暂无搜索地理数据")
                                    }
                                }
                            })
                            inputTips.requestInputtipsAsyn()
                        }else{
                         et_input_search.isFocusable=true
                         ToastUtil.makeText(this@PostPoiSearchActivity,"请输入地理位置名称")
                        }
                    }
                }
                var resources= ResourceTypeDao.getall()
                dialog.setResourceDotContent(resources)
                var adStyles=AdStyleDao.getall()
                dialog.setAdStyleContent(adStyles)
            } catch (e: ContentException) {
                ToastUtil.makeText(this@PostPoiSearchActivity,"暂无资源位")
            }
        })
        //获取 小区类型列表 数据
        basePresenter.getDistrictTypeList()

        //获取资源类型 数据
        basePresenter.getResourceTypeList()

        //获取广告类型的数据
        basePresenter.getAdTypeList()
        //获取客户字典的 数据
        basePresenter.getClientNameList()

        requestPermission(101, "android.permission.ACCESS_COARSE_LOCATION", object : Runnable {
            override fun run() {
                //定位中
                AMapUI.initLocation(this@PostPoiSearchActivity, locationListener);
                AMapUI.startLocation()
            }
        }, object : Runnable {
            override fun run() {
                ToastUtil.makeText(this@PostPoiSearchActivity, "请打开定位权限")
            }
        })
    }

    private fun setModeDotAndArea() {
        //清空设置
        ASettingUtils.clearSetting()
        if (mode == 0) {
            //自身定位的模式
            iv_map_search.visibility = View.VISIBLE
            rl_map_search.visibility= View.VISIBLE
            rl_map_search.setOnClickListener(null)
            iv_map_location_search.visibility = View.VISIBLE
            rl_map_location_search.visibility=View.VISIBLE
            iv_map_area.visibility = View.GONE
            iv_area.visibility=View.VISIBLE
            iv_dingwei.visibility=View.GONE
            //自身定位 的 同时加上 模糊搜索
            rl_map_location_search.setOnClickListener({
                if(et_input_search.hasTxt()){
                    //如果数据
                    inputlist.visibility=View.INVISIBLE
                    doRequestRadius()
                }else{
                    if(AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)==0){
                        //点位名称
                        ToastUtil.makeText(this@PostPoiSearchActivity,"请输入点位名称")
                    }else{
                        //地理位置
                        ToastUtil.makeText(this@PostPoiSearchActivity,"请输入地理位置名称")
                    }
                }
            })
            //显示 就是默认
            ll_distance.visibility=View.VISIBLE
        } else {
            //区域的模式 +  模糊搜索的模式
            iv_map_search.visibility = View.VISIBLE
            rl_map_search.visibility = View.VISIBLE
            rl_map_search.setOnClickListener(null)
            iv_map_location_search.visibility = View.VISIBLE
            rl_map_location_search.visibility=View.VISIBLE
            iv_map_area.visibility = View.VISIBLE

            rl_map_location_search.setOnClickListener({

                var startTime=AppPrefsUtils.getString(MapContstants.SETTING_STARTTIME,
                        DateUtil.dateFormat(Calendar.getInstance().time))
                var endTime=AppPrefsUtils.getString(MapContstants.SETTING_ENDTIME,
                        DateUtil.dateFormat(Calendar.getInstance().time))
                var adType=AppPrefsUtils.getInt(MapContstants.SETTING_ADTYPEID,-1)
                var categoryIds=AppPrefsUtils.getString(MapContstants.SETTING_RESOURCEIDS,"[]")
                basePresenter.provinceDot(ProvinceDotRequest(AMapUI.clickProvinceId, AMapUI.clickCityId,
                                        AMapUI.clickDistrictId,et_input_search.getTxt(),"0",
                                    1,30,UserDao.getLocalUser().id,adType,categoryIds,startTime,endTime))
            })
            iv_area.visibility=View.GONE
            iv_dingwei.visibility=View.VISIBLE
            ll_distance.visibility=View.GONE
        }
    }

    override fun getView(): View {
        return View.inflate(this, R.layout.activity_map_search,null);
    }


    override fun injectCompontent() {
        DaggerMapComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }


    override fun onProvinceCityRequest(provinceCity: ArrayList<ProvinceCity>, action: CityListAction) {
        SystemUtil.printlnStr("provinceCity:"+provinceCity.toString())
        if(provinceCity.size>0){
            if(action==CityListAction.ProvinceAtion){
                AMapUI.clickIndex=0
                AMapUI.clickCityId=-1
                AMapUI.clickDistrictId=-1
                AMapUI.clickProvinceId=-1
            }
            AMapUI.clickIndex++
            AMapUI.showCityList(this@PostPoiSearchActivity,rl_top,action,provinceCity,basePresenter,et_input_search.getTxt())
        }else{
            ToastUtil.makeText(this@PostPoiSearchActivity,action.errorMsg)
        }
    }

    companion object {
        var  currentMapDot:ArrayList<MapDot>?= arrayListOf<MapDot>()
    }

    /**
     * 失败了 区域位置 定位
     */
    override fun onProvinceDotRequestError() {
        aMap!!.clear()
        if(currentMapDot!=null){
            currentMapDot!!.clear()
        }
        AMapUI.doDistrictCanvas(this,AMapUI.stringBuilder.toString(),aMap!!);
    }

    override fun onRadiusDotRequestError() {
        aMap!!.clear()
        if(currentMapDot!=null){
            currentMapDot!!.clear()
        }
        if(AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)==1
                && !et_input_search.getTxt().equals("") && currentTip!=null &&
                et_input_search.getTxt().equals(currentTip!!.name) && currentTip!!.point!=null){
            //此时能够切换
            //成功获取数据之后
            AMapUI.poiCircle(this@PostPoiSearchActivity,aMap!!, arrayListOf<MapDot>(),currentTip!!.point,currentRadius)
        }else{
            AMapUI.poiCircle(this@PostPoiSearchActivity,aMap!!, arrayListOf(),currntLocation!!,currentRadius)
        }
    }

    /*
      根据城市 查找点位 成功
     */
    override fun onProvinceDotRequest(mapDot: ArrayList<MapDot>) {
        super.onProvinceDotRequest(mapDot)
        currentMapDot =mapDot
        SystemUtil.printlnStr("onProvinceDotRequest:"+mapDot.size+"...:"+AMapUI.stringBuilder.toString())
        if(mapDot.size>0){
            AMapUI.poiDot(this@PostPoiSearchActivity,aMap!!,mapDot,currntLocation!!)
            AMapUI.doDistrictCanvas(this,AMapUI.stringBuilder.toString(),aMap!!);
        }else{
            aMap!!.clear()
            AMapUI.doDistrictCanvas(this,AMapUI.stringBuilder.toString(),aMap!!);
            ToastUtil.makeText(this@PostPoiSearchActivity,"暂无城市点位数据")
        }
    }

    override fun onRadisDotRequest(mapDot: ArrayList<MapDot>) {
        currentMapDot =mapDot;
        SystemUtil.printlnStr("11111  currentTip ......SETTING_KEYWORDINDEX："+
                AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)+"..currentTip!!.name:"+(currentTip!=null))
        if(AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)==1
                    && !et_input_search.getTxt().equals("") && currentTip!=null &&
                et_input_search.getTxt().equals(currentTip!!.name) && currentTip!!.point!=null){
            //此时能够切换
            SystemUtil.printlnStr("11111  currentTip ......")
            //成功获取数据之后
            if(mapDot.size>0){
                AMapUI.poiCircle(this@PostPoiSearchActivity,aMap!!,mapDot,currentTip!!.point,currentRadius)
            }else{
                AMapUI.poiCircle(this@PostPoiSearchActivity,aMap!!, arrayListOf(),currentTip!!.point,currentRadius)
                ToastUtil.makeText(this@PostPoiSearchActivity,"暂无半径点位数据")
            }
        }else{
            if(mapDot.size>0){
                AMapUI.poiCircle(this@PostPoiSearchActivity,aMap!!,mapDot,currntLocation!!,currentRadius)
            }else{
                AMapUI.poiCircle(this@PostPoiSearchActivity,aMap!!, arrayListOf(),currntLocation!!,currentRadius)
                ToastUtil.makeText(this@PostPoiSearchActivity,"暂无半径点位数据")
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker!!.getObject() != null) {
            try {
                val mCurrentPoi = marker.getObject() as MapDot
                if (mlastMarker == null) {
                    mlastMarker = marker
                } else {
                    // 将之前被点击的marker置为原来的状态
                    AMapUI.resetlastmarker(this@PostPoiSearchActivity)
                    mlastMarker = marker
                }
                detailMarker = marker
                detailMarker!!.setIcon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                resources,
                                R.drawable.poi_marker_pressed)))

                AMapUI.showPopupWindow(mCurrentPoi,this@PostPoiSearchActivity,root)
            } catch (e: Exception) {
            }

        } else {
            resetlastmarker(this@PostPoiSearchActivity)
        }
        return true
    }

    /**
     * 定位监听
     */
    internal var locationListener: AMapLocationListener = AMapLocationListener { location ->
        if (null != location) {
            currntLocation=location
            val sb = StringBuffer()
            //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.errorCode == 0) {
                sb.append("定位成功" + "\n")
                sb.append("定位类型: " + location.locationType + "\n")
                sb.append("经    度    : " + location.longitude + "\n")
                sb.append("纬    度    : " + location.latitude + "\n")

                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : " + location.satellites + "\n")
                sb.append("国    家    : " + location.country + "\n")
                sb.append("省            : " + location.province + "\n")
                sb.append("市            : " + location.city + "\n")
                sb.append("城市编码 : " + location.cityCode + "\n")
                sb.append("区            : " + location.district + "\n")
                sb.append("区域 码   : " + location.adCode + "\n")
                sb.append("地    址    : " + location.address + "\n")
                sb.append("地    址    : " + location.description + "\n")
                AMapUI.stopLocation()
                AMapUI.locationMap(this@PostPoiSearchActivity,aMap!!, LatLonPoint(location.latitude,location.longitude))
                doRequestRadius()
            } else {
                //定位失败.
                if(location.errorCode==12){
                    var intent= Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent,887);
                }else{
                    ToastUtil.makeText(this@PostPoiSearchActivity,"定位失败....")
                }
            }
            //定位之后的回调时间
            //解析定位结果，
        } else {
            //定位失败
            ToastUtil.makeText(this@PostPoiSearchActivity,"定位失败....")
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            887 ->
                //开启GPS，重新添加地理监听
                AMapUI.startLocation()
            else -> {

            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapView.onCreate(savedInstanceState)// 此方法必须重写
        init()
        //进行更新版本
        basePresenter.updateAppVersion(UpdateRequest())
    }

    private var aMap: AMap? = null
    /**
     * 初始化AMap对象
     */
    private fun init() {
        if (aMap == null) {
            aMap = mapView.map
            aMap!!.setOnMarkerClickListener(this)
        }
    }

    /**
     *  zoom的放大
     */
    private fun zoomLarge() {
        var cameraPosition = aMap!!.getCameraPosition();
        var mapZoom = cameraPosition.zoom;
        var mapTarget = cameraPosition.target;
        aMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(mapTarget, 14f));
    }

    /**
     * 更新版本
     */
    override fun onGetAppVersion(appUpdateResponse: UpdateResponse) {
        var packageInfo=packageManager.getPackageInfo(packageName,0)
        var versoinName=packageInfo.versionName
        UpdateUtils.updateApp(versoinName.toFloat(),appUpdateResponse.getFloatVersion(),BaseConstants.BASE_DOWNLOAD_URL+appUpdateResponse.path,"",this@PostPoiSearchActivity)
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        AMapUI.stopLocation()
    }
    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        AMapUI.destroyLocation()
        AMapUI.dismissPopup()
    }

    /**
     * 请求 半径搜索
     */
    fun doRequestRadius(){
        if(AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)==0){
            //点位名称
            var startTime=AppPrefsUtils.getString(MapContstants.SETTING_STARTTIME,
                    DateUtil.dateFormat(Calendar.getInstance().time))
            var endTime=AppPrefsUtils.getString(MapContstants.SETTING_ENDTIME,
                    DateUtil.dateFormat(Calendar.getInstance().time))
            basePresenter.radiusDot(RadiusDotRequest(currntLocation!!.longitude,currntLocation!!.latitude,
                                         currentRadius.toDouble(),et_input_search.getTxt(),
                                      "0",1,30, UserDao.getLocalUser().id,
                    currntLocation!!.adCode.toInt(),-1,AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)+1,
                         AppPrefsUtils.getInt(MapContstants.SETTING_ADTYPEID,-1),
                             AppPrefsUtils.getString(MapContstants.SETTING_RESOURCEIDS,"[]"),startTime,endTime))
        }else{
            //地理位置
            //点位名称
            var startTime=AppPrefsUtils.getString(MapContstants.SETTING_STARTTIME,
                    DateUtil.dateFormat(Calendar.getInstance().time))
            var endTime=AppPrefsUtils.getString(MapContstants.SETTING_ENDTIME,
                    DateUtil.dateFormat(Calendar.getInstance().time))
            var city:Int=-1
            SystemUtil.printlnStr("adCode:"+currntLocation!!.adCode+"..city:"+currntLocation!!.cityCode)
            if(currentTip!=null && currentTip!!.name.equals(et_input_search.getTxt())){
                //如果输入不为空  且等于 当前的tip的 内容
                city=currentTip!!.adcode.toInt()
                basePresenter.radiusDot(RadiusDotRequest(currentTip!!.point.longitude,currentTip!!.point.latitude,
                        currentRadius.toDouble(),et_input_search.getTxt(), "0",1,30, UserDao.getLocalUser().id,
                        city,-1,AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)+1,
                        AppPrefsUtils.getInt(MapContstants.SETTING_ADTYPEID,-1),
                        AppPrefsUtils.getString(MapContstants.SETTING_RESOURCEIDS,"[]"),startTime,endTime))
            }else{
                //不等的情况
                city=currntLocation!!.adCode.toInt()
                basePresenter.radiusDot(RadiusDotRequest(currntLocation!!.longitude,currntLocation!!.latitude,
                        currentRadius.toDouble(),et_input_search.getTxt(), "0",1,30, UserDao.getLocalUser().id,
                        city,-1,AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX)+1,
                        AppPrefsUtils.getInt(MapContstants.SETTING_ADTYPEID,-1),
                        AppPrefsUtils.getString(MapContstants.SETTING_RESOURCEIDS,"[]"),startTime,endTime))
            }

        }
    }
}