package com.suntray.chinapost.map.ui.activity.sale

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import cn.qqtheme.framework.picker.OptionPicker
import cn.qqtheme.framework.widget.WheelView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.rx.hasTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.baselibrary.data.dao.AdStyleDao
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.map.data.response.OneKeyReservedResponse
import com.suntray.chinapost.map.injection.component.DaggerMapComponent
import com.suntray.chinapost.map.presenter.ResourcePresenter
import com.suntray.chinapost.map.presenter.view.ResourceView
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.ui.adapter.inner.ClickClientNameAction
import com.suntray.chinapost.user.utils.PopupUtils
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_ad_onekey_reserved.*
import kotlinx.android.synthetic.main.item_client_ad_layout.*
import kotlinx.android.synthetic.main.item_start_end_time_layout.*
import java.util.*
import kotlin.collections.ArrayList

/**
 *   点位一键预定
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/14 15:53
 */
@Route(path = RouterPath.MapModule.POST_AD_ONE_KEY_RESERVED)
class PostAdOneKeyReservedActivity:BaseMvpActivity<ResourcePresenter>(),ResourceView{

    var button: Button?=null
    var selectedClient: MineClient?= null
    var isSetContent=false

    override fun injectCompontent() {
        DaggerMapComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }



    var pageNumer=1;//从1开始
    var rightClientName=""
    /**
     * 获取到数据
     */
    override fun onMineClient(myClientList: ArrayList<MineClient>, action: RefreshAction) {
        super.onMineClient(myClientList, action)
        if(action== RefreshAction.PullDownRefresh){
            //刷新数据
            PopupUtils.showPopupWindow(this@PostAdOneKeyReservedActivity,
                    tv_select_client_content,myClientList,object: ClickClientNameAction {
                override fun onNeedLoadMore() {
                    //进行加载更多
                    pageNumer++
                    basePresenter.myClient(UserDao.getLocalUser().id,tv_select_client_content.getTxt(),pageNumer,10, RefreshAction.UpMore)
                }

                override fun onClickClientName(mineClient: MineClient) {
                    PopupUtils.dismissWindow()
                    isSetContent=true
                    if(mineClient.name.length>13){
                        var name=mineClient.name.subSequence(0,13).toString()+"..."
                        tv_select_client_content.setText(name)
                        rightClientName=name
                    }else{
                        tv_select_client_content.setText(mineClient.name)
                        rightClientName=mineClient.name
                    }
                    selectedClient=mineClient
                }
            })
        }else if(action== RefreshAction.UpMore){
            PopupUtils.addClientNameDatas(myClientList)
        }

    }

    override fun onGetResourceList(resourceList: ArrayList<ResourceType>) {
        super.onGetResourceList(resourceList)
        SystemUtil.printlnStr("onGetResourceList resourceList:"+resourceList.size)
        if(resourceList.size>0){
            initResourceView(resourceList)
        }else{
            ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"暂无广告位数据")
        }
    }


    override fun onGetReservedResult(oneKeyReservedResponse: OneKeyReservedResponse, resourceTypeIdArray: Array<Int?>) {
        var integerArrayList=ArrayList<Int?>()
        integerArrayList.addAll(resourceTypeIdArray)
        if(UserDao.getLocalUser().userRole==4){
            //销售人员
            ARouter.getInstance().build(RouterPath.MapModule.POST_AD_RESERVED_RESULT)
                    .withSerializable("reservedAdResult",oneKeyReservedResponse)
                    .withIntegerArrayList("resourceIdList",integerArrayList)
                    .withInt("adType",adType)
                    .withInt("clientId",selectedClient!!.id)
                    .withString("startDate",startDate)
                    .withString("endDate",endDate)
                    .withString("clientName",tv_select_client_content!!.text.toString())
                    .navigation(this@PostAdOneKeyReservedActivity,110)
        }else if(UserDao.getLocalUser().userRole==2){
            //代理商
            ARouter.getInstance().build(RouterPath.MapModule.POST_AD_RESERVED_RESULT)
                    .withSerializable("reservedAdResult",oneKeyReservedResponse)
                    .withIntegerArrayList("resourceIdList",integerArrayList)
                    .withInt("adType",adType)
                    .withInt("clientId",-1)
                    .withString("startDate",startDate)
                    .withString("endDate",endDate)
                    .withString("clientName",tv_select_client_content!!.text.toString())
                    .navigation(this@PostAdOneKeyReservedActivity,110)
        }else{
           ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"其他类型")
        }
    }

    val columnNum=2 //每行
    var resourceIdList= arrayListOf<ResourceType>()  //选择的对应的资源位的id


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //结束 请求 resultCode
        if(requestCode==110 && resultCode==103){
            setResult(104)
            finish()
        }
    }
    /**
     * 初始化 点位列表
     */
    private fun initResourceView(resourceList: ArrayList<ResourceType>) {
        resourceIdList.clear()
        //首先 判断 有几行
        var resourceIndex=-1;
        var total=0
        if(resourceList.size%columnNum==0){
            total=resourceList.size/columnNum-1
        }else{
            total=resourceList.size/columnNum
        }
        for(index in 0..total){
            if(index!=total){
                var contentView=View.inflate(this@PostAdOneKeyReservedActivity,R.layout.item_onekey_reserved,null)
                var btn1=contentView.findViewById(R.id.btn_1) as Button;
                var btn2=contentView.findViewById(R.id.btn_2) as Button;
//                var btn3=contentView.findViewById(R.id.btn_3) as Button;
                for(i in 0..1){
                    resourceIndex++
                    if(i==0){
                        btn1.setText(resourceList.get(resourceIndex).name)
                        btn1.setOnClickListener({
                            btn1.isActivated=!btn1.isActivated
                            //保存 对应的位置
                            if(btn1.isActivated){
                                resourceIdList.add(resourceList.get(index*columnNum+i))
                            }else{
                                resourceIdList.remove(resourceList.get(index*columnNum+i))
                            }
                        })
                    }else if(i==1){
                        btn2.setText(resourceList.get(resourceIndex).name)
                        btn2.setOnClickListener({
                            btn2.isActivated=!btn2.isActivated
                            //保存 对应的位置
                            if(btn2.isActivated){
                                resourceIdList.add(resourceList.get(index*columnNum+i))
                            }else{
                                resourceIdList.remove(resourceList.get(index*columnNum+i))
                            }
                        })
                    }

//                    else if(i==2){
//                        btn3.setText(resourceList.get(resourceIndex).name)
//                        btn3.setOnClickListener({
//                            btn3.isActivated=!btn3.isActivated
//                            //保存 对应的位置
//                            if(btn3.isActivated){
//                                resourceIdList.add(resourceList.get(index*columnNum+i))
//                            }else{
//                                resourceIdList.remove(resourceList.get(index*columnNum+i))
//                            }
//                        })
//                    }
                }
                rl_yuding.addView(contentView)
            }else{
                SystemUtil.printlnStr("resourceList.size-columnNum*(index+1)"+(resourceList.size-columnNum*(index))+"..resourceIndex:"+resourceIndex)
                var contentView=View.inflate(this@PostAdOneKeyReservedActivity,R.layout.item_onekey_reserved,null)
                for(i in 1..(resourceList.size-columnNum*(index))){
                    resourceIndex++
                    var btn1=contentView.findViewById(R.id.btn_1) as Button;
                    var btn2=contentView.findViewById(R.id.btn_2) as Button;
//                    var btn3=contentView.findViewById(R.id.btn_3) as Button;
                        if(i==1){
                            btn2.visibility=View.INVISIBLE
//                            btn3.visibility=View.INVISIBLE
                            btn1.setText(resourceList.get(resourceIndex).name)
                            btn1.setOnClickListener({
                                btn1.isActivated=!btn1.isActivated
                                //保存 对应的位置
                                if(btn1.isActivated){
                                    resourceIdList.add(resourceList.get(index*columnNum+(i-1)))
                                }else{
                                    resourceIdList.remove(resourceList.get(index*columnNum+(i-1)))
                                }
                            })
                        }else if(i==2){
                            btn2.visibility=View.VISIBLE
//                            btn3.visibility=View.INVISIBLE
                            btn2.setText(resourceList.get(resourceIndex).name)
                            btn2.setOnClickListener({
                                btn2.isActivated=!btn2.isActivated
                                //保存 对应的位置
                                if(btn2.isActivated){
                                    resourceIdList.add(resourceList.get(index*columnNum+(i-1)))
                                }else{
                                    resourceIdList.remove(resourceList.get(index*columnNum+(i-1)))
                                }
                            })
                        }

//                        else if(i==3){
//                            btn3.visibility=View.VISIBLE
//                            btn3.setText(resourceList.get(resourceIndex).name)
//                            btn3.setOnClickListener({
//                                btn3.isActivated=!btn3.isActivated
//                                //保存 对应的位置
//                                if(btn3.isActivated){
//                                    resourceIdList.add(resourceList.get(resourceIndex))
//                                }else{
//                                    resourceIdList.remove(resourceList.get(resourceIndex))
//                                }
//                            })
//                    }
                }
                rl_yuding.addView(contentView)
            }
        }

        //创建 linearout,将 buttton放入 到 linear中
    }

    var adType=-1
    var startDate=""
    var endDate=""
    override fun initView() {
        isRightShow=false
        isTitleShow=true
        isBlackShow=true
        viewtitle="点位一键预定"

        //首先获取广告为资源列表
        basePresenter.getResourceList()
        basePresenter.getAdTypeList()

        hud2= KProgressHUD(this@PostAdOneKeyReservedActivity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("提交预定中...")
        //获取数值 selectedList
        //将集合 放入到布局中
        addSelectedList(PostAdReservedListActivity.selectedList)


        if(UserDao.getLocalUser().userRole==2){
            tv_select_client_content.setHint("请输入客户名称")
        }

        /**
         * 内容的监听器
         */
        tv_select_client_content.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {
                //获取内容:
                //刷新该界面:
                if(UserDao.getLocalUser().userRole==4){
                    if(!isSetContent && !s.toString().equals("")){
                        pageNumer=1
                        basePresenter.myClient(UserDao.getLocalUser().id,s.toString(),pageNumer,10,RefreshAction.PullDownRefresh)
                    }else{
                        PopupUtils.dismissWindow()
                        isSetContent=false
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        tv_select_client_content.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //进行的刷新业务
            }
        });
        ll_choose_type.setOnClickListener({
            try {
                var clientList= AdStyleDao.getall()
                if(clientList.size>0){
                    var array= arrayOfNulls<String>(clientList.size)
                    var index=0
                    for (data in clientList){
                        array.set(index,data.name)
                        index++
                    }
                    val picker = OptionPicker(this, array)
                    picker.setCanceledOnTouchOutside(false)
                    picker.setDividerRatio(WheelView.DividerConfig.FILL)
                    picker.setShadowColor(Color.RED, 40)
                    picker.selectedIndex = 1
                    picker.setCycleDisable(true)
                    picker.setTextSize(20)
                    picker.setHeight(AutoUtils.getPercentHeightSize(350))
                    picker.setOnOptionPickListener(object : OptionPicker.OnOptionPickListener() {
                        override fun onOptionPicked(index: Int, item: String) {
                            tv_choose_ad_type.setText(clientList.get(index).name)
                            adType=clientList.get(index).id
                        }
                    })
                    picker.show()
                }
            } catch (e: Exception) {
                ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"暂无广告类型数据")
            }
        })
        btn_cancel.setOnClickListener({
            finish()
        })

        btn_submit_reserved.setOnClickListener({
            if(hasInput()){
                //资源位的id
                var resourceArray= arrayOfNulls<Int>(resourceIdList.size)
                var index=0
                for(data in resourceIdList){
                    resourceArray.set(index,data.id)
                    index++
                }

                //点位的id
                var dotArray= arrayOfNulls<Int>(PostAdReservedListActivity.selectedList.size)
                index=0
                for(data in PostAdReservedListActivity.selectedList){
                    dotArray.set(index,data.id)
                    index++
                }

                //客户的id  是你选择的客户的id
                //广告类型 也是你选择的
                startDate=tv_choose_start_time.getTxt()
                endDate=tv_choose_end_time.getTxt()

                if(UserDao.getLocalUser().userRole==2){
                    basePresenter.oneKeySubmitResult(dotArray,resourceArray,-1,adType,
                            UserDao.getLocalUser().id,tv_choose_start_time.getTxt(),tv_choose_end_time.getTxt(),tv_select_client_content.getTxt(),UserDao.getLocalUser().userRole)
                }else{
                    basePresenter.oneKeySubmitResult(dotArray,resourceArray,selectedClient!!.id,adType,
                            UserDao.getLocalUser().id,tv_choose_start_time.getTxt(),tv_choose_end_time.getTxt(),"",UserDao.getLocalUser().userRole)
                }

            }
        })

        /**
         * 开始时间
         */
        tv_choose_start_time.setOnClickListener({

            var picker= DatePickerDialog(this@PostAdOneKeyReservedActivity,object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var calendar=Calendar.getInstance()
                    calendar.set(year,month,dayOfMonth)
                    var content=DateUtil.dateFormat(calendar.time)
                    tv_choose_start_time.setText(content)
                }
            },Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                                     Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
        })

        /**
         * 结束时间
         */
        tv_choose_end_time.setOnClickListener({
            var picker= DatePickerDialog(this@PostAdOneKeyReservedActivity,object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var calendar=Calendar.getInstance()
                    calendar.set(year,month,dayOfMonth)
                    var content=DateUtil.dateFormat(calendar.time)
                    tv_choose_end_time.setText(content)
                }
            },Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()
        })
    }

    private fun addSelectedList(selectList: ArrayList<MapDot>?) {
        var index=0
        for (data in selectList!!){
            index++
            var contentView=View.inflate(this@PostAdOneKeyReservedActivity,R.layout.recylerview_dianwei_qingdan,null)
            var btn_xuhao:TextView=contentView.findViewById(R.id.btn_xuhao) as TextView
            btn_xuhao.setText(""+index)
            var tv_location:TextView=contentView.findViewById(R.id.tv_location)  as TextView
            tv_location.setText(data.zoneaddress)
            var tv_id_value:TextView=contentView.findViewById(R.id.tv_id_value) as TextView
            tv_id_value.setText(data.equid)
            var tv_position:TextView=contentView.findViewById(R.id.tv_position) as TextView
            tv_position.setText(data.equlocation)
            ll_dianwei_qingdan.addView(contentView)
        }
    }

    override fun getView(): View {
       return View.inflate(this@PostAdOneKeyReservedActivity, R.layout.activity_ad_onekey_reserved,null);
    }

    private  fun  hasInput():Boolean{
        if(tv_select_client_content.hasTxt() && tv_choose_ad_type.hasTxt()
                && resourceIdList.size>0 && tv_choose_start_time.hasTxt() &&  tv_choose_end_time.hasTxt()){

            if(!(UserDao.getLocalUser().userRole==2)){
               //如国
                    if(selectedClient==null){
                        ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"请选择客户名称")
                        tv_select_client_content.setText("")
                        return false
                    }
                    if(!tv_select_client_content.getTxt().equals(rightClientName)){
                        ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"客户名称与选择的客户名称不一致")
                        tv_select_client_content.setText("")
                        return false
                    }
            }

            if(DateUtil.parse2Date(tv_choose_start_time.getTxt()).time<=
                    DateUtil.parse2Date(tv_choose_end_time.getTxt()).time){
                return true
            }else{
                ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"结束时间需要大于开始时间")
                return false
            }

            return true
        }else{
            if(!tv_select_client_content.hasTxt()){
                ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"请选择客户名称")
                return false
            }

            if(tv_choose_ad_type.equals("请选择") ){
                ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"请选择广告类型")
                return false
            }

            if(!(resourceIdList!!.size>0)){
                ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"请选择预订资源位类型")
                return false
            }
            if(tv_choose_start_time.getTxt().trim().equals("")){
                ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"请选择开始时间")
                return false
            }

            if(tv_choose_end_time.getTxt().trim().equals("") ){
                ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"请选择结束时间")
                return false
            }
            ToastUtil.makeText(this@PostAdOneKeyReservedActivity,"请输入相关信息")
            return false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PopupUtils.dismissWindow()
        PopupUtils.window=null
    }

    /**
     * 错误的回调
     */
    override fun onError(content: String, action: RefreshAction) {
        if(action==RefreshAction.PullDownRefresh){
            //消除提示框
            PopupUtils.dismissWindow()
        }
        ToastUtil.makeText(this@PostAdOneKeyReservedActivity,content)
    }
}