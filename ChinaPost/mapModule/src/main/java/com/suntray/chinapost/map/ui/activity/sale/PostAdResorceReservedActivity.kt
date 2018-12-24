package com.suntray.chinapost.map.ui.activity.sale

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import cn.qqtheme.framework.picker.OptionPicker
import cn.qqtheme.framework.widget.WheelView
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.dao.AdStyleDao
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.rx.hasTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.ResourceAd
import com.suntray.chinapost.map.data.enum.CalendarAction
import com.suntray.chinapost.map.data.request.ResourceDateRequest
import com.suntray.chinapost.map.data.response.OneKeyReservedResponse
import com.suntray.chinapost.map.data.response.ResourceDateResponse
import com.suntray.chinapost.map.injection.component.DaggerResourceComponent
import com.suntray.chinapost.map.presenter.ResourcePresenter
import com.suntray.chinapost.map.presenter.view.ResourceView
import com.suntray.chinapost.map.ui.calendar.CalendarClickListener
import com.suntray.chinapost.map.ui.calendar.SingleMonthView
import com.suntray.chinapost.map.utils.CalendarPopupUtil
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.ui.adapter.inner.ClickClientNameAction
import com.suntray.chinapost.user.utils.PopupUtils
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_guanggao_reserverd.*
import kotlinx.android.synthetic.main.item_client_ad_layout.*
import kotlinx.android.synthetic.main.item_start_end_time_layout.*
import java.util.*

/**
 *  广告资源位 预定 界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/14 9:33
 */
@Route(path = RouterPath.MapModule.POST_AD_RESOURCE_RESERVED)
class PostAdResorceReservedActivity:BaseMvpActivity<ResourcePresenter>(),ResourceView{

    var selectedClient:MineClient?= null
    var isSetContent=false
    override fun injectCompontent() {
        DaggerResourceComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }


    override fun onSaveReservedSubmit() {
        super.onSaveReservedSubmit()
        //预定成功
        ToastUtil.makeText(this@PostAdResorceReservedActivity,"预定成功")
        finish()
    }

    /**
     * 资源位的获取
     */
    override fun onGetResourceDate(resourceDateResponse: ResourceDateResponse,calendarAction: CalendarAction) {
        SystemUtil.printlnStr("SingleMonthView onGetResourceDate .....")
        if(calendarAction==CalendarAction.CalendarNext){
         //stateArray
            //下个月
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            SingleMonthView.isFirstShow=true
            CalendarPopupUtil.nextMonth()
        }else if(calendarAction==CalendarAction.CalendarPre){
            //上个月
            SingleMonthView.isFirstShow=true
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            CalendarPopupUtil.preMonth()
        }else if(calendarAction==CalendarAction.CalendarNormal){
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            CalendarPopupUtil.showCalendar(this@PostAdResorceReservedActivity,root,basePresenter!!,dateResourceId)
        }else if(calendarAction==CalendarAction.CalendarStartClick){
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            CalendarPopupUtil.showCalendarDate(this@PostAdResorceReservedActivity,
                    root,basePresenter!!,dateResourceId,object:CalendarClickListener{
                override fun onClickCalendar(calendar: com.haibin.calendarview.Calendar) {
                    CalendarPopupUtil.dismissWindow()
                    var date=calendar.timeInMillis
                    var content= DateUtil.dateFormat(Date(date))
                    tv_choose_start_time!!.setText(content)
                }
            })
        }else if(calendarAction==CalendarAction.CalendarEndClick){
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            CalendarPopupUtil.showCalendarDate(this@PostAdResorceReservedActivity,
                    root,basePresenter!!,dateResourceId,object:CalendarClickListener{
                override fun onClickCalendar(calendar: com.haibin.calendarview.Calendar) {
                    CalendarPopupUtil.dismissWindow()
                    var date=calendar.timeInMillis
                    var content= DateUtil.dateFormat(Date(date))
                    tv_choose_end_time!!.setText(content)
                }
            })
        }
    }


    override fun onOneKeyReservedSubmit(oneKeyReservedResponse: OneKeyReservedResponse, resourceTypeIdArray: Array<Int?>) {
        super.onOneKeyReservedSubmit(oneKeyReservedResponse, resourceTypeIdArray)
        if(oneKeyReservedResponse.countFailure>0){
            if(oneKeyReservedResponse.data!!.size>0){
                ToastUtil.makeText(this@PostAdResorceReservedActivity,oneKeyReservedResponse!!.data!!.get(0).error)
            }
        }else{
            ToastUtil.makeText(this@PostAdResorceReservedActivity,"操作成功")
            finish()
        }
    }
    /**
     * 错误的回调
     */
    override fun onError(content: String, action: RefreshAction) {
        if(action==RefreshAction.PullDownRefresh){
            //消除提示框
            PopupUtils.dismissWindow()
        }
        ToastUtil.makeText(this@PostAdResorceReservedActivity,content)
    }


    var rightClientName=""
    /**
     * 获取到数据
     */
    override fun onMineClient(myClientList: ArrayList<MineClient>, action: RefreshAction) {
        super.onMineClient(myClientList, action)
        if(action==RefreshAction.PullDownRefresh){
            //刷新数据
            PopupUtils.showPopupWindow(this@PostAdResorceReservedActivity,
                    rl_client_name,myClientList,object:ClickClientNameAction{
                override fun onNeedLoadMore() {
                    //进行加载更多
                    pageNumer++
                    basePresenter.myClient(UserDao.getLocalUser().id,tv_select_client_content.getTxt(),pageNumer,10,RefreshAction.UpMore)
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
        }else if(action==RefreshAction.UpMore){
            PopupUtils.addClientNameDatas(myClientList)
        }

    }


    override fun rightTitleClick() {
        super.rightTitleClick()
        if(!CalendarPopupUtil.isPopupWindowShowing()){
            basePresenter.getResourceDateSchedule(ResourceDateRequest(dateResourceId,DateUtil.dateFormat(Calendar.getInstance().time)),CalendarAction.CalendarNormal)
        }
    }
    var chooseAdStyle:Int=-1
    var dotId:Int=-1 //点位的id
    var resourceId:Int=-1 //资源位id
    var dateResourceId=-1
    var pageNumer=1;//从1开始
    override fun initView() {
        isBlackShow=true;
        isTitleShow=true
        isRightShow=false
        viewtitle="广告资源位预定"
        rightTitle="查看预定"

        var resourceAd=intent.getSerializableExtra("resorceAd")
        if(resourceAd!=null && resourceAd is ResourceAd){
            //是该类型
            dotId=resourceAd.pointid
            resourceId=resourceAd.category
            dateResourceId=resourceAd.id
            hud2= KProgressHUD(this@PostAdResorceReservedActivity).setLabel("提交预定中...").setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)

            if(UserDao.getLocalUser().userRole==2){
                tv_select_client_content.setHint("请输入客户名称")
            }
            /**
             * 一键提交
             */
            btn_submmit_reserved.setOnClickListener({
                if (hasInput()) {
                    if(UserDao.getLocalUser().userRole==4){
                        //销售人员
                        basePresenter.oneKeySubmit(arrayOf(dotId), arrayOf(resourceId),
                                selectedClient!!.id, chooseAdStyle, UserDao.getLocalUser().id,
                                tv_choose_start_time.getTxt(), tv_choose_end_time.getTxt(),"",4)

                    }else if(UserDao.getLocalUser().userRole==2){
                        //代理商
                        basePresenter.oneKeySubmit(arrayOf(dotId), arrayOf(resourceId),
                                -1, chooseAdStyle, UserDao.getLocalUser().id,
                                tv_choose_start_time.getTxt(), tv_choose_end_time.getTxt(),tv_select_client_content.getTxt(),2)
                    }else{
                        ToastUtil.makeText(this@PostAdResorceReservedActivity,"其他类型")
                    }
                }
            })

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
                    }else{
                        PopupUtils.dismissWindow()
                        isSetContent=false
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            //广告类型
            ll_choose_type.setOnClickListener({
                try {
                    var clientList= AdStyleDao.getall()
                    SystemUtil.printlnStr("profession al_hangye:"+clientList.toString())
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
                                SystemUtil.printlnStr("profession:"+clientList.get(index).id)
                                chooseAdStyle=clientList.get(index).id
                                tv_choose_ad_type.setText(clientList.get(index).name)
                            }
                        })
                        picker.show()
                    }
                } catch (e: Exception) {
                    ToastUtil.makeText(this@PostAdResorceReservedActivity,"暂无行业数据")
                }
            })


            //tv_choose_start_time
            ll_choose_start_time.setOnClickListener({
//                var picker= DatePickerDialog(this@PostAdResorceReservedActivity,object: DatePickerDialog.OnDateSetListener {
//                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                        var calendar= Calendar.getInstance()
//                        calendar.set(year,month,dayOfMonth)
//                        var content= DateUtil.dateFormat(calendar.time)
//                        tv_choose_start_time!!.setText(content)
//                    }
//                }, Calendar.getInstance().get(Calendar.YEAR),
//                        Calendar.getInstance().get(Calendar.MONTH),
//                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()

                if(!CalendarPopupUtil.isPopupWindowShowing()){
                    basePresenter.getResourceDateSchedule(ResourceDateRequest(dateResourceId,DateUtil.dateFormat(Calendar.getInstance().time)),CalendarAction.CalendarStartClick)
                }
            })


            //tv_choose_end_time
            ll_choose_end_time.setOnClickListener({
//                var picker= DatePickerDialog(this@PostAdResorceReservedActivity,object: DatePickerDialog.OnDateSetListener {
//                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//                        var calendar= Calendar.getInstance()
//                        calendar.set(year,month,dayOfMonth)
//                        var content= DateUtil.dateFormat(calendar.time)
//                        tv_choose_end_time!!.setText(content)
//                    }
//                }, Calendar.getInstance().get(Calendar.YEAR),
//                        Calendar.getInstance().get(Calendar.MONTH),
//                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show()

                if(!CalendarPopupUtil.isPopupWindowShowing()){
                    basePresenter.getResourceDateSchedule(
                            ResourceDateRequest(dateResourceId,DateUtil.dateFormat(Calendar.getInstance().time)),
                                CalendarAction.CalendarEndClick)
                }
            })

        }else{
            ToastUtil.makeText(this@PostAdResorceReservedActivity,"暂无数据")
            finish()
        }
    }

    /**
     * 是否有输入内容
     */
    private fun hasInput(): Boolean {
        if(tv_select_client_content.hasTxt() && tv_choose_ad_type.hasTxt()
                        && tv_choose_start_time.hasTxt() && tv_choose_end_time.hasTxt()  && chooseAdStyle>0){

            if(selectedClient==null && UserDao.getLocalUser().userRole==4){
                ToastUtil.makeText(this@PostAdResorceReservedActivity,"请选择客户名称")
                tv_select_client_content.setText("")
                return false
            }else if(selectedClient==null && UserDao.getLocalUser().userRole==2 && tv_select_client_content.text.isEmpty()){
                //代理商
                ToastUtil.makeText(this@PostAdResorceReservedActivity,"请输入客户名称")
                return false
            }
            if(!tv_select_client_content.getTxt().equals(rightClientName)){
                ToastUtil.makeText(this@PostAdResorceReservedActivity,"客户名称与选择的客户名称不一致")
                tv_select_client_content.setText("")
                return false
            }
            if(DateUtil.parse2Date(tv_choose_start_time.getTxt()).time<=
                            DateUtil.parse2Date(tv_choose_end_time.getTxt()).time){
                return true
            }else{
                ToastUtil.makeText(this@PostAdResorceReservedActivity,"结束时间需要大于开始时间")
                return false
            }
        }else{
            ToastUtil.makeText(this@PostAdResorceReservedActivity,"缺少相关信息")
        }
        return false
    }

    override fun getView(): View {
        return View.inflate(this@PostAdResorceReservedActivity, R.layout.activity_guanggao_reserverd,null);
    }

    override fun processBussiness() {
        super.processBussiness()
    }

    override fun onDestroy() {
        super.onDestroy()
        CalendarPopupUtil.dismissWindow()
        CalendarPopupUtil.window=null
        PopupUtils.dismissWindow()
        PopupUtils.window=null
    }
}