package com.suntray.chinapost.map.ui.activity.sale

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.ResourceAd
import com.suntray.chinapost.map.data.bean.ResourceDotLocation
import com.suntray.chinapost.map.data.enum.CalendarAction
import com.suntray.chinapost.map.data.request.ResourceDateRequest
import com.suntray.chinapost.map.data.response.ResourceDateResponse
import com.suntray.chinapost.map.injection.component.DaggerResourceComponent
import com.suntray.chinapost.map.presenter.ResourcePresenter
import com.suntray.chinapost.map.presenter.view.ResourceView
import com.suntray.chinapost.map.ui.calendar.SingleMonthView
import com.suntray.chinapost.map.ui.dialog.ImageInfoDialog
import com.suntray.chinapost.map.utils.CalendarPopupUtil
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.activity_check_date.*
import kotlinx.android.synthetic.main.recylerview_dot_info.*
import java.util.*

/**
 *  @Title ${name}
 *  @ProjectName ChinaPost
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/813:52
 */
@Route(path = RouterPath.MapModule.POST_AD_CHECK_DATE)
class PostAdCheckDateActivity:BaseMvpActivity<ResourcePresenter>(),ResourceView {

    override fun injectCompontent() {
        DaggerResourceComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }


    var dotId:Int=-1 //点位的id
    var resourceId:Int=-1 //资源位id
    var dateResourceId=-1
    var resourceDotLocation: ResourceDotLocation?=null
    /**
     * 初始化 对应的view
     */
    override fun initView() {
        var resourceAd=intent.getSerializableExtra("resorceAd")
        var resourceDotLocation=intent.getSerializableExtra("resourceDotLocation") as  ResourceDotLocation

        isTitleShow=true
        isBlackShow=true
        isRightShow=false
        viewtitle="查看排期"
        SingleMonthView.stateArray=null

        if(resourceAd!=null && resourceAd is ResourceAd) {
            //是该类型
            dotId = resourceAd.pointid
            resourceId = resourceAd.category
            dateResourceId = resourceAd.id

            calendar = Calendar.getInstance()
            calendar!!.set(Calendar.DAY_OF_MONTH,1)

            //设置图片
            Glide.with(applicationContext).load(resourceDotLocation!!.infoImage).into(iv_info)
                    .onLoadStarted(applicationContext!!.resources.getDrawable(R.drawable.icon_default))
            tv_info_devive_specification!!.setText(resourceDotLocation!!.deviceSpecification)

            //事件点击
            iv_info!!.setOnClickListener({
                if(resourceDotLocation!!.infoImage!=null && !resourceDotLocation!!.infoImage.equals("")){
                    var imageDialog= ImageInfoDialog(this@PostAdCheckDateActivity)
                    imageDialog.show()
                    imageDialog.setContent(resourceDotLocation!!.infoImage)
                }else{
                    com.suntray.chinapost.map.utils.ToastUtil.show(applicationContext,"暂无图片信息")
                }
            })
            //网点名称
            tv_wangdain_value!!.setText(resourceDotLocation!!.wangdianName)

            tv_device_position_value!!.setText(resourceDotLocation!!.cityname
                    +resourceDotLocation!!.districtname
                    +resourceDotLocation!!.zoneaddress
                    +resourceDotLocation!!.equlocation)

            tv_info_district_type!!.setText(resourceDotLocation!!.zonename)

            tv_info_limit_type!!.setText(resourceDotLocation!!.publishtypename)

            tv_dot_location!!.setText(resourceDotLocation!!.resourceLocation)
            tv_info_location!!.setText(resourceDotLocation!!.deviceId)

            //获取日期
            basePresenter.getResourceDateSchedule(ResourceDateRequest(dateResourceId,DateUtil.dateFormat(Calendar.getInstance().time)),CalendarAction.CalendarNormal)
        }
    }


    override fun getView(): View {
        return View.inflate(this@PostAdCheckDateActivity, R.layout.activity_check_date,null)
    }

    /**
     * 资源位的获取
     */
    override fun onGetResourceDate(resourceDateResponse: ResourceDateResponse, calendarAction: CalendarAction) {
        SystemUtil.printlnStr("SingleMonthView onGetResourceDate .....")
        if(calendarAction== CalendarAction.CalendarNext){
            //stateArray
            //下个月
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            SingleMonthView.isFirstShow=true
            nextMonth()
        }else if(calendarAction== CalendarAction.CalendarPre){
            //上个月
            SingleMonthView.isFirstShow=true
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            preMonth()
        }else if(calendarAction== CalendarAction.CalendarNormal){
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            showCalendar()
        }else if(calendarAction== CalendarAction.CalendarStartClick){
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            showCalendar()
        }else if(calendarAction== CalendarAction.CalendarEndClick){
            SingleMonthView.stateArray=resourceDateResponse!!.stateArr
            showCalendar()
        }
    }


    /**
     * 展现出 前一个月
     */
    fun  preMonth(){
        //点击事件处理
        calendarView!!.scrollToPre(true)
        calendarView.invalidate()
    }

    var calendar:Calendar?=null
    /**
     * 展现出下一个月
     */
    fun nextMonth(){
        calendarView!!.scrollToNext(true)
        calendarView.invalidate()
    }

    fun showCalendar(){
        //设置日期拦截事件，仅适用单选模式，当前有效 mCalendarView.getCurDay()
        tv_month_day.setText(calendarView!!.getCurYear().toString()+"年" + calendarView!!.getCurMonth() + "月")
        SingleMonthView.isFirstShow=true
        //上个月点击事件
        btn_last_month.setOnClickListener( {
            basePresenter.getResourceDateSchedule(ResourceDateRequest(dateResourceId,DateUtil.dateFormat(CalendarPopupUtil.getLastMonthCalendar(calendar!!).time)),CalendarAction.CalendarPre)
        })

        //下个月的点击事件
        btn_next_month.setOnClickListener( {
            //点击事件处理
            basePresenter.getResourceDateSchedule(ResourceDateRequest(dateResourceId,DateUtil.dateFormat(CalendarPopupUtil.getNextMonthCalendar(calendar!!).time)),CalendarAction.CalendarNext)
        })
        calendarView!!.setOnMonthChangeListener( { year, month ->
            if (year != 0 && month != 0) {
                tv_month_day.setText(year.toString() + "年" + month + "月")
                calendar!!.set(Calendar.YEAR,year)
                calendar!!.set(Calendar.MONTH,month-1)
            }
        })
        calendarView!!.setMonthViewScrollable(false)
        calendarView.update()
    }
}