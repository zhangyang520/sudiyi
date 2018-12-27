package com.suntray.chinapost.map.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.core.PoiItem
import com.amap.api.services.district.DistrictResult
import com.amap.api.services.district.DistrictSearch
import com.amap.api.services.district.DistrictSearchQuery
import com.amap.api.services.poisearch.PoiSearch
import com.bumptech.glide.Glide
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.map.presenter.MapPresenter
import com.suntray.chinapost.baselibrary.ui.adapter.CityAdapter
import com.suntray.chinapost.baselibrary.ui.adapter.DistrictAdapter
import com.suntray.chinapost.baselibrary.ui.adapter.ProvinceAdapter
import com.suntray.chinapost.baselibrary.ui.adapter.inner.ClickCodeAction
import com.suntray.chinapost.baselibrary.ut.base.utils.AppPrefsUtils
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.map.data.constants.MapContstants
import com.suntray.chinapost.map.data.request.ProvinceDotRequest
import com.suntray.chinapost.map.ui.dialog.ImageInfoDialog
import com.suntray.chinapost.provider.RouterPath
import com.zhy.autolayout.config.AutoLayoutConifg
import com.zhy.autolayout.utils.AutoUtils
import java.util.*
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


object AMapUI {

    fun setMapCustomStyleFile(context: Context, aMap: AMap) {
        val styleName = "style_json.json"
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        var filePath: String? = null
        try {
            inputStream = context.assets.open(styleName)
            val b = ByteArray(inputStream!!.available())
            inputStream.read(b)

            filePath = context.filesDir.absolutePath
            val file = File("$filePath/$styleName")
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
            outputStream = FileOutputStream(file)
            outputStream.write(b)

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close()

                if (outputStream != null)
                    outputStream.close()

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        aMap!!.setCustomMapStylePath("$filePath/$styleName")

        aMap!!.showMapText(false)

    }


    private var locationClient: AMapLocationClient? = null
    private var locationOption: AMapLocationClientOption? = null

    /**
     * 初始化定位
     *
     * @since 2.8.0
     * @author hongming.wang
     */
    fun initLocation(context: Context, locationListener: AMapLocationListener) {
        //初始化client
        locationClient = AMapLocationClient(context)
        locationOption = AMapUI.getDefaultOption()
        //设置定位参数
        locationClient!!.setLocationOption(locationOption)
        // 设置定位监听
        locationClient!!.setLocationListener(locationListener)
    }


    /**
     * 销毁定位
     *
     * @since 2.8.0
     * @author hongming.wang
     */
    fun destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient!!.onDestroy()
            locationClient = null
            locationOption = null
        }
    }

    /**
     * 停止定位
     *
     * @since 2.8.0
     * @author hongming.wang
     */
    fun stopLocation() {
        // 停止定位
        locationClient!!.stopLocation()
    }


    /**
     * 开始定位
     *
     * @since 2.8.0
     * @author hongming.wang
     */
    fun startLocation() {
        // 设置定位参数
        locationClient!!.setLocationOption(locationOption)
        // 启动定位
        locationClient!!.startLocation()
    }

    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     */
    fun getDefaultOption(): AMapLocationClientOption {
        val mOption = AMapLocationClientOption()
        mOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.isGpsFirst = false//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.httpTimeOut = 30000//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.interval = 2000//可选，设置定位间隔。默认为2秒
        mOption.isNeedAddress = true//可选，设置是否返回逆地理地址信息。默认是true
        mOption.isOnceLocation = false//可选，设置是否单次定位。默认是false
        mOption.isOnceLocationLatest = false//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP)//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.isSensorEnable = false//可选，设置是否使用传感器。默认是false
        mOption.isWifiScan = true //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.isLocationCacheEnable = true //可选，设置是否使用缓存定位，默认为true
        return mOption
    }

    /**
     * 根据位置定位
     */
    fun locationMap(context: Context, aMap: AMap, lp: LatLonPoint) {
        var locationMarker = aMap.addMarker(MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.point4)))
                .position(LatLng(lp.getLatitude(), lp.getLongitude())))
        locationMarker.showInfoWindow()
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lp.latitude, lp.longitude), 18f))
    }


    var query: PoiSearch.Query? = null// Poi查询条件类


    /**
     * 开始进行poi搜索
     */
    fun doSearchQuery(keyWord: String, context: Context, latLonPoint: LatLonPoint, km: Int,
                                                    poiSearchListener: PoiSearch.OnPoiSearchListener) {
        var currentPage = 0
        query = PoiSearch.Query(keyWord, "", "")// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query!!.setPageSize(20)// 设置每页最多返回多少条poiitem
        query!!.setPageNum(currentPage)// 设置查第一页

        if (latLonPoint != null) {
            var poiSearch = PoiSearch(context, query)
            poiSearch.setOnPoiSearchListener(poiSearchListener)
            poiSearch.setBound(PoiSearch.SearchBound(latLonPoint, km, true))//
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            poiSearch.searchPOIAsyn()// 异步搜索
        }
    }


    var locationMarker: Marker? = null // 选择的点
    var detailMarker: Marker? = null
    var mlastMarker: Marker? = null
    var poiSearch: PoiSearch? = null
    var poiOverlay: myPoiOverlay? = null// poi图层
    var poiItems: List<PoiItem>? = null// poi数据

    /**
     * 绘制的 圆圈的业务
     */
    fun poiCircle(context: Context, aMap: AMap,
                  apmLocationList:ArrayList<MapDot>,
                  currntLocation:AMapLocation,
                  radis:Int){

        SystemUtil.printlnStr("radis:"+radis)
        if (mlastMarker != null) {
            resetlastmarker(context)
        }
        //清理之前搜索结果的marker
        if (poiOverlay != null) {
            poiOverlay!!.removeFromMap()
        }
        aMap!!.clear()
        poiOverlay = myPoiOverlay(aMap, apmLocationList, LatLonPoint(currntLocation.latitude,currntLocation.longitude))
        poiOverlay!!.addToMap(context)
        poiOverlay!!.zoomToSpan()

        aMap!!.addMarker(MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                context.resources, R.drawable.point4)))
                .position(LatLng(currntLocation!!.getLatitude(), currntLocation!!.getLongitude())))

        aMap!!.addCircle(CircleOptions()
                .center(LatLng(currntLocation!!.getLatitude(),
                        currntLocation!!.getLongitude())).radius(radis.toDouble())
                .strokeColor(Color.BLUE)
                .fillColor(Color.argb(50, 1, 1, 1))
                .visible(true)
                .strokeWidth(2f))
        //todo 1km公里范围内
        if(radis==1000 && apmLocationList.size<=20){
            zoomLarge(aMap, 14.7f, LatLonPoint(currntLocation.latitude,currntLocation.longitude))
        }else if(radis==3000 &&apmLocationList.size<=20){
            zoomLarge(aMap, 13f, LatLonPoint(currntLocation.latitude,currntLocation.longitude))
        }else if(radis==5000 &&apmLocationList.size<=20){
            zoomLarge(aMap, 12.5f, LatLonPoint(currntLocation.latitude,currntLocation.longitude))
        }else if(radis==8000 &&apmLocationList.size<=20){
            zoomLarge(aMap, 11.8f, LatLonPoint(currntLocation.latitude,currntLocation.longitude))
        }else if(radis==10000 &&apmLocationList.size<=20){
            zoomLarge(aMap, 11.4f, LatLonPoint(currntLocation.latitude,currntLocation.longitude))
        }
    }

    /**
     * 绘制的 圆圈的业务
     */
    fun poiCircle(context: Context, aMap: AMap,
                  apmLocationList:ArrayList<MapDot>,
                  currntLocation:LatLonPoint,
                  radis:Int){

        SystemUtil.printlnStr("radis:"+radis)
        if (mlastMarker != null) {
            resetlastmarker(context)
        }
        //清理之前搜索结果的marker
        if (poiOverlay != null) {
            poiOverlay!!.removeFromMap()
        }
        aMap!!.clear()
        poiOverlay = myPoiOverlay(aMap, apmLocationList,currntLocation)
        poiOverlay!!.addToMap(context)
        poiOverlay!!.zoomToSpan()

        aMap!!.addMarker(MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                context.resources, R.drawable.point4)))
                .position(LatLng(currntLocation!!.getLatitude(), currntLocation!!.getLongitude())))

        aMap!!.addCircle(CircleOptions()
                .center(LatLng(currntLocation!!.getLatitude(),
                        currntLocation!!.getLongitude())).radius(radis.toDouble())
                .strokeColor(Color.BLUE)
                .fillColor(Color.argb(50, 1, 1, 1))
                .visible(true)
                .strokeWidth(2f))
        //todo 1km公里范围内
        if(radis==1000 && apmLocationList.size<=20){
            zoomLarge(aMap,14.7f,currntLocation)
        }else if(radis==3000 &&apmLocationList.size<=20){
            zoomLarge(aMap,13f,currntLocation)
        }else if(radis==5000 &&apmLocationList.size<=20){
            zoomLarge(aMap,12.5f,currntLocation)
        }else if(radis==8000 &&apmLocationList.size<=20){
            zoomLarge(aMap,11.8f,currntLocation)
        }else if(radis==10000 &&apmLocationList.size<=20){
            zoomLarge(aMap,11.4f,currntLocation)
        }
    }

    /**
     * 绘制的 圆圈的业务
     */
    fun poiDot(context: Context, aMap: AMap,
                  currntLocation:LatLonPoint,
                  radis:Int){

        SystemUtil.printlnStr("radis:"+radis)
        if (mlastMarker != null) {
            resetlastmarker(context)
        }
        //清理之前搜索结果的marker
        if (poiOverlay != null) {
            poiOverlay!!.removeFromMap()
        }
        aMap!!.clear()
        poiOverlay = myPoiOverlay(aMap, ArrayList<MapDot>(),currntLocation)
        poiOverlay!!.addToMap(context)
        poiOverlay!!.zoomToSpan()

        aMap!!.addMarker(MarkerOptions()
                .anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(BitmapFactory.decodeResource(
                                context.resources, R.drawable.point4)))
                .position(LatLng(currntLocation!!.getLatitude(), currntLocation!!.getLongitude())))
        //todo 1km公里范围内
        if(radis==1000){
            zoomLarge(aMap,14.7f,currntLocation)
        }else if(radis==3000){
            zoomLarge(aMap,13f,currntLocation)
        }else if(radis==5000){
            zoomLarge(aMap,12.5f,currntLocation)
        }else if(radis==8000){
            zoomLarge(aMap,11.8f,currntLocation)
        }else if(radis==10000){
            zoomLarge(aMap,11.4f,currntLocation)
        }
    }

    /**
     *  zoom的放大
     *  1000 14.7
     *  3000
     *  5000
     *  8000
     *  10000
     */
    private fun zoomLarge(aMap: AMap, scale: Float, currntLocation: LatLonPoint) {
        aMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currntLocation.latitude,currntLocation.longitude), scale));
    }

    /**
     * 根据2个坐标返回一个矩形Bounds
     * 以此来智能缩放地图显示
     */
    fun   createBounds(latA:Double, lngA:Double, latB:Double, lngB:Double):LatLngBounds{
        var northeastLatLng:LatLng?=null;
        var southwestLatLng:LatLng?=null

        var topLat:Double
        var topLng:Double;
        var  bottomLat:Double
        var bottomLng:Double

        if(latA>=latB){
            topLat=latA;
            bottomLat=latB;
        }else{
            topLat=latB;
            bottomLat=latA;
        }
        if(lngA>=lngB){
            topLng=lngA;
            bottomLng=lngB;
        }else{
            topLng=lngB;
            bottomLng=lngA;
        }
        northeastLatLng= LatLng(topLat,topLng);
        southwestLatLng= LatLng(bottomLat,bottomLng);
        return  LatLngBounds(southwestLatLng, northeastLatLng);
    }


    /**
     * 绘制的 圆圈的业务
     */
    fun poiDot(context: Context, aMap: AMap,
                  apmLocationList:ArrayList<MapDot>,
                  currntLocation:AMapLocation){
        if (mlastMarker != null) {
            resetlastmarker(context)
        }
        //清理之前搜索结果的marker
        if (poiOverlay != null) {
            poiOverlay!!.removeFromMap()
        }
        aMap!!.clear()
        poiOverlay = myPoiOverlay(aMap, apmLocationList, LatLonPoint(currntLocation.latitude,currntLocation.longitude))
        poiOverlay!!.addToMap(context)
        poiOverlay!!.zoomToSpan()

//        aMap!!.addMarker(MarkerOptions()
//                .anchor(0.5f, 0.5f)
//                .icon(BitmapDescriptorFactory
//                        .fromBitmap(BitmapFactory.decodeResource(
//                                context.resources, R.drawable.point4)))
//                .position(LatLng(currntLocation!!.getLatitude(), currntLocation!!.getLongitude())))
    }


    fun poiDot(context: Context, aMap: AMap,
               apmLocationList:ArrayList<MapDot>,
               currntLocation:LatLonPoint){
        if (mlastMarker != null) {
            resetlastmarker(context)
        }
        //清理之前搜索结果的marker
        if (poiOverlay != null) {
            poiOverlay!!.removeFromMap()
        }
        aMap!!.clear()
        poiOverlay = myPoiOverlay(aMap, apmLocationList, LatLonPoint(currntLocation.latitude,currntLocation.longitude))
        poiOverlay!!.addToMap(context)
        poiOverlay!!.zoomToSpan()

//        aMap!!.addMarker(MarkerOptions()
//                .anchor(0.5f, 0.5f)
//                .icon(BitmapDescriptorFactory
//                        .fromBitmap(BitmapFactory.decodeResource(
//                                context.resources, R.drawable.point4)))
//                .position(LatLng(currntLocation!!.getLatitude(), currntLocation!!.getLongitude())))
    }

    // 将之前被点击的marker置为原来的状态
     fun resetlastmarker(context: Context) {
        val index = poiOverlay!!.getPoiIndex(mlastMarker!!)
        if (index < 10) {
            mlastMarker!!.setIcon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(
                            context.resources,
                            myPoiOverlay.markers[index])))
        } else {
            mlastMarker!!.setIcon(BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(context.resources, R.drawable.dingwei_default)))
        }
        mlastMarker = null
    }


    /**
     * 弹出 对应的框
     */
    fun showPopupWindow(mCurrentPoi: MapDot,context: Context,rootView:View) {
        var contentView= View.inflate(context,R.layout.popup_dot_info,null);
        window=PopupWindow(context,null,R.style.Transparent_Dialog)
        window!!.width= AutoLayoutConifg.getInstance().screenWidth;
        window!!.height= AutoUtils.getPercentHeightSize(558);
        (contentView.findViewById(R.id.tv_location)as TextView).setText(mCurrentPoi.equid)

        //广告资源位
        (contentView.findViewById(R.id.btn_guanggao) as Button).setOnClickListener({
            ARouter.getInstance().
                    build(RouterPath.MapModule.POST_AD_INFO).withInt("id",mCurrentPoi.id).
                    navigation(context)
        })

        window!!.contentView=contentView;
        window!!.isOutsideTouchable=true
        window!!.showAtLocation(rootView, Gravity.BOTTOM,-1,-AutoUtils.getPercentHeightSize(915))

        if(mCurrentPoi.publishtypename==null || mCurrentPoi.publishtypename.equals("")){
            (contentView.findViewById(R.id.tv_limit_up) as TextView).setText("无")
        }else{
            (contentView.findViewById(R.id.tv_limit_up) as TextView).setText(mCurrentPoi.publishtypename)
        }

        //小区类型名称
        (contentView.findViewById(R.id.tv_district_type_value) as TextView).setText(mCurrentPoi.zonename)

        (contentView.findViewById(R.id.tv_device_spec) as TextView).setText(mCurrentPoi.equspecify)
        (contentView.findViewById(R.id.tv_dot_location)as TextView).setText(mCurrentPoi.zoneaddress)

        //网点名称
        (contentView.findViewById(R.id.tv_wangdain_value)as TextView).setText(mCurrentPoi.pointname)
        //tv_device_position_value
        (contentView.findViewById(R.id.tv_device_position_value)as TextView).
                setText(mCurrentPoi.cityname+mCurrentPoi.districtname+mCurrentPoi.zoneaddress+mCurrentPoi.equlocation)
        Glide.with(context).load(mCurrentPoi.imgpath).error((context!!.resources.getDrawable(R.drawable.icon_default)))
                .into(contentView.findViewById(R.id.map_iv_info) as ImageView)
        //.error(R.drawable.icon_default)

        //设置 点击事件
        (contentView.findViewById(R.id.map_iv_info) as ImageView).setOnClickListener({
                if(mCurrentPoi.imgpath!=null && !mCurrentPoi.imgpath.equals("")){
                    var imageDialog= ImageInfoDialog(context)
                    imageDialog.show()
                    imageDialog.setContent(mCurrentPoi.imgpath)
                }else{
                    com.suntray.chinapost.map.utils.ToastUtil.show(context,"暂无图片信息")
                }
        })
        //周边社区属性
        (contentView.findViewById(R.id.btn_shequshuxing) as Button).setOnClickListener({
            ToastUtil.makeText(context,"暂时不用")
        })
    }


    fun dismissPopup(){
        if(window!=null && window!!.isShowing){
            window!!.dismiss()
        }
    }
    /**
     * 展示城市 列表
     */
    var contentView:View?=null
    var window:PopupWindow?=null
    var provinceAdapter: ProvinceAdapter?=null
    var cityAdapter: CityAdapter?=null
    var districtAdapter: DistrictAdapter?=null
    var stringBuilder:StringBuilder?=StringBuilder()
    var clickProvinceId=-1
    var clickCityId=-1
    var clickDistrictId=-1

    var clickIndex=0// 1:省  2:市  3:区
    var recyler_province:RecyclerView?=null
    var recyler_city:RecyclerView?=null
    var recyler_district:RecyclerView?=null

    fun showCityList(context: Context, rootView: View, action: CityListAction,
                             provinceCity: ArrayList<ProvinceCity>, mapPresenter: MapPresenter, keyWord:String=""){

        if(action== CityListAction.ProvinceAtion){
            stringBuilder=StringBuilder()
            contentView= View.inflate(context,R.layout.popup_city_list,null);

            window=PopupWindow(context,null,R.style.Transparent_Dialog);
            window!!.width= AutoLayoutConifg.getInstance().screenWidth;
            window!!.height= AutoUtils.getPercentHeightSize(1242);
            AutoUtils.autoSize(contentView)
            window!!.contentView=contentView;
            window!!.isOutsideTouchable=false
            window!!.setOnDismissListener {
                window==null
                //初始化一些为零!
                recyler_province=null
                recyler_city=null
                recyler_district=null
                cityAdapter=null
                districtAdapter=null
            }
            window!!.showAtLocation(rootView, Gravity.NO_GRAVITY,0,AutoUtils.getPercentHeightSize(150))

            var buttonCancel=contentView!!.findViewById(R.id.btn_cancel) as Button
            var buttonOk=contentView!!.findViewById(R.id.btn_ok) as Button

            buttonCancel.setOnClickListener({
                if(window!=null && window!!.isShowing){
                    window!!.dismiss()
                }
            })

            buttonOk.setOnClickListener({
                //控制必须 点击到 区这个的级别
                if(clickIndex>=2){
                    if(window!=null && window!!.isShowing){
                        window!!.dismiss()
                    }
                    var startTime= AppPrefsUtils.getString(MapContstants.SETTING_STARTTIME,
                            DateUtil.dateFormat(Calendar.getInstance().time))
                    var endTime= AppPrefsUtils.getString(MapContstants.SETTING_ENDTIME,
                            DateUtil.dateFormat(Calendar.getInstance().time))
                    var adType= AppPrefsUtils.getInt(MapContstants.SETTING_ADTYPEID,-1)
                    var categoryIds= AppPrefsUtils.getString(MapContstants.SETTING_RESOURCEIDS,"[]")

                    mapPresenter.provinceDot(ProvinceDotRequest(clickProvinceId, clickCityId, clickDistrictId,
                            keyWord,"0",1,10, UserDao.getLocalUser().id,adType,categoryIds,startTime,endTime,UserDao.getLocalUser().userRole))
                }else{
                    com.suntray.chinapost.map.utils.ToastUtil.show(context,"必须选择到市这一级别")
                }
            })
            SystemUtil.printlnStr("showCityList 11111111111111 provinceCity:"+provinceCity.size)
            //显示 省
            recyler_province=contentView!!.findViewById(R.id.recyler_province) as RecyclerView
            recyler_city=contentView!!.findViewById(R.id.recyler_city) as RecyclerView
            recyler_district=contentView!!.findViewById(R.id.recyler_district) as RecyclerView

           var linearLayoutManager=LinearLayoutManager(context)
            linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
            recyler_province!!.layoutManager=linearLayoutManager
            provinceAdapter= ProvinceAdapter(provinceCity, context)
            provinceAdapter!!.clickCodeAction=object: ClickCodeAction {
                override fun clickItem(code: Int, action: CityListAction, provinceCity: ProvinceCity) {
                    clickIndex=0
                    mapPresenter.province(action.getCityValue1().toString(),code,-1,action)
//                    stringBuilder!!.append(provinceCity.province)
                }
            }
            recyler_province!!.adapter=provinceAdapter

            if(cityAdapter!=null){
                cityAdapter!!.provinceList?.clear()
                cityAdapter!!.notifyDataSetChanged()
            }

            if(districtAdapter!=null){
                districtAdapter!!.provinceList?.clear()
                districtAdapter!!.notifyDataSetChanged()
            }

        }else if(action== CityListAction.CityAction){
            SystemUtil.printlnStr("showCityList 2222222222222 provinceCity:"+provinceCity.size)
            //显示的 市
            var linearLayoutManager=LinearLayoutManager(context)
            linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
            recyler_city!!.layoutManager=linearLayoutManager
            cityAdapter= CityAdapter(provinceCity, context)
            cityAdapter!!.clickCodeAction=object: ClickCodeAction {
                override fun clickItem(code: Int, action: CityListAction, provinceCity: ProvinceCity) {
                    clickIndex=2
                    mapPresenter.province(action.getCityValue1().toString(),-1,code,action)
                    stringBuilder!!.delete(0, stringBuilder!!.length)
                    stringBuilder!!.append(provinceCity.id)
                    clickProvinceId=provinceCity.provinceId
                    clickCityId=code
                }
            }
            recyler_city!!.adapter=cityAdapter
            if(districtAdapter!=null){
                districtAdapter!!.provinceList?.clear()
                districtAdapter!!.notifyDataSetChanged()
            }
        }else if(action== CityListAction.DistrictAction){
            SystemUtil.printlnStr("showCityList 333333333333333   provinceCity:"+provinceCity.size)
            //显示的区
            var linearLayoutManager=LinearLayoutManager(context)
            linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
            recyler_district!!.layoutManager=linearLayoutManager
            districtAdapter= DistrictAdapter(provinceCity, context)
            districtAdapter!!.clickCodeAction=object: ClickCodeAction {

                override fun clickDistrict(provinceCity: ProvinceCity) {
                    super.clickDistrict(provinceCity)
                    clickIndex=2
                    clickDistrictId=provinceCity.id
                    stringBuilder!!.delete(0, stringBuilder!!.length)
                    stringBuilder!!.append(provinceCity.id)
                }
            }
            recyler_district!!.adapter=districtAdapter
        }
    }



    //绘制 地图的区域
    fun  doDistrictCanvas(context: Context,districtName:String,mAMap:AMap){
        var districtSearch =  DistrictSearch(context);
        SystemUtil.printlnStr(":districtName:"+districtName)
        var searchListener=object:DistrictSearch.OnDistrictSearchListener{
            override fun onDistrictSearched(districtResult: DistrictResult?) {
                if (districtResult == null || districtResult.district == null) {
                    SystemUtil.printlnStr("districtName 1111111111")
                    return
                }
                //通过ErrorCode判断是否成功
                if (districtResult.aMapException != null && districtResult.aMapException.errorCode == AMapException.CODE_AMAP_SUCCESS) {
                    SystemUtil.printlnStr("districtName 333333 size:"+districtResult.district.size)
                    if(districtResult.district.size<=0) return
                    val item = districtResult.district[0] ?: return
                    SystemUtil.printlnStr("districtName 444444")
                    val centerLatLng = item.center
                    if (centerLatLng != null) {
                        mAMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(LatLng(centerLatLng.latitude, centerLatLng.longitude), 8f))
                    }

                    Executors.newCachedThreadPool().submit((Runnable {
                        val polyStr = item.districtBoundary()
                        SystemUtil.printlnStr("districtName 555555555")
                        if (polyStr == null || polyStr.size == 0) {
                            SystemUtil.printlnStr("districtName 666666")
                            return@Runnable
                        }
                        for (str in polyStr) {
                            SystemUtil.printlnStr("districtName 7777777")
                            val lat = str.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            val polylineOption = PolylineOptions()
                            var isFirst = true
                            var firstLatLng: LatLng? = null
                            var list= mutableListOf<LatLng>()
                            for (latstr in lat) {
                                val lats = latstr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                                if (isFirst) {
                                    isFirst = false
                                    firstLatLng = LatLng(java.lang.Double
                                            .parseDouble(lats[1]), java.lang.Double
                                            .parseDouble(lats[0]))
                                }
                                list.add(LatLng(java.lang.Double
                                        .parseDouble(lats[1]), java.lang.Double
                                        .parseDouble(lats[0])))
                                polylineOption.add(LatLng(java.lang.Double
                                        .parseDouble(lats[1]), java.lang.Double
                                        .parseDouble(lats[0])))
                            }
                            if (firstLatLng != null) {
                                polylineOption.add(firstLatLng)
                                list.add(firstLatLng)
                            }
                            polylineOption.width(10f).color(Color.BLUE)
                            mAMap.addPolyline(polylineOption)

                            var latLngBuild=LatLngBounds.Builder()
                            for(data in list){
                                latLngBuild.include(data)
                            }

                            //使用该 区域范围 去构建 屏幕范围内的 区
                            mAMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBuild.build(), 12));

                            //放大 区域
//                            var cameraPosition = mAMap!!.getCameraPosition();
//                            var mapZoom = cameraPosition.zoom;
//                            var mapTarget = cameraPosition.target;
//                            mAMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(mapTarget, 12f));
                        }
                    }))
                } else {
                    SystemUtil.printlnStr("districtName 2222222")
                    if (districtResult.aMapException != null) {
                        ToastUtil.makeText(context, "绘图失败:"+districtResult.aMapException.errorCode)
                    }
                }
            }
        }
        districtSearch.setOnDistrictSearchListener(searchListener);//绑定监听器;

        var fromQuery = DistrictSearchQuery();
          //設置關鍵字
        fromQuery.setKeywords(districtName);
        //設置是否返回邊界值
        fromQuery.setShowBoundary(true);
        //不显示子区域边界
        fromQuery.setShowChild(false);
        districtSearch.setQuery(fromQuery);
        //开启异步搜索
        districtSearch.searchDistrictAsyn();
    }
}
