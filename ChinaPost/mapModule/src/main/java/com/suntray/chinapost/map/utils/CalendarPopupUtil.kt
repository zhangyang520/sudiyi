package com.suntray.chinapost.map.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import com.haibin.calendarview.CalendarView
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.enum.CalendarAction
import com.suntray.chinapost.map.data.request.ResourceDateRequest
import com.suntray.chinapost.map.presenter.ResourcePresenter
import com.suntray.chinapost.map.ui.calendar.CalendarClickListener
import com.suntray.chinapost.map.ui.calendar.SingleMonthView
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.popup_dot_info.view.*
import java.util.*

/**
 *  日历的工具类
 *  @Author 张扬 @version 1.0
 *  @Date \ 18:55
 */
object CalendarPopupUtil {


    //状态的数组
    var stateArray:Array<Int>?=null
    var window:PopupWindow?=null
    var mCalendarView:CalendarView?=null
    var calendar:Calendar?=null

    fun dismissWindow(){
        if(window!=null && window!!.isShowing){
            window!!.dismiss()
        }
    }

    fun isPopupWindowShowing():Boolean{
        if(window!=null && window!!.isShowing){
            return true
        }
        return false
    }
    /**
     * 展现 日历 popupWindow
     */
    fun showCalendar(context:Context,rootView:View,basePresenter: ResourcePresenter,id:Int){
        if(window==null || !window!!.isShowing){
            window=PopupWindow(context,null,R.style.Transparent_Dialog)
            var contentView= View.inflate(context, R.layout.activity_map_calendar,null)
            var mTextMonthDay = contentView.findViewById(R.id.tv_month_day) as TextView
            var mRelativeTool = contentView.findViewById(R.id.rl_tool) as RelativeLayout
            mCalendarView = contentView.findViewById(R.id.calendarView) as CalendarView
            calendar= Calendar.getInstance()
            calendar!!.set(Calendar.DAY_OF_MONTH,1)
            //设置日期拦截事件，仅适用单选模式，当前有效 mCalendarView.getCurDay()
            mTextMonthDay.setText(mCalendarView!!.getCurYear().toString()+"年" + mCalendarView!!.getCurMonth() + "月")

            var  btn_last_month = contentView.findViewById(R.id.btn_last_month) as Button
            //上个月点击事件
            btn_last_month.setOnClickListener(View.OnClickListener {
                basePresenter.getResourceDateSchedule(ResourceDateRequest(id,DateUtil.dateFormat(getLastMonthCalendar(calendar!!).time)),CalendarAction.CalendarPre)
            })
            mCalendarView!!.setOnMonthChangeListener(CalendarView.OnMonthChangeListener { year, month ->
                if (year != 0 && month != 0) {
                    mTextMonthDay.setText(year.toString() + "年" + month + "月")
                    calendar!!.set(Calendar.YEAR,year)
                    calendar!!.set(Calendar.MONTH,month-1)
                }
            })
            mCalendarView!!.setMonthViewScrollable(false)
            SingleMonthView.isCanSelected=false
            mCalendarView!!.setOnYearChangeListener(CalendarView.OnYearChangeListener { })
            var  btn_next_month = contentView.findViewById(R.id.btn_next_month) as Button
            //下个月的点击事件
            btn_next_month.setOnClickListener(View.OnClickListener {
                //点击事件处理
                basePresenter.getResourceDateSchedule(ResourceDateRequest(id,DateUtil.dateFormat(getNextMonthCalendar(calendar!!).time)),CalendarAction.CalendarNext)
            })
            window!!.contentView=contentView
            window!!.width=AutoUtils.getPercentWidthSize(750)
            window!!.height=AutoUtils.getPercentHeightSize(1000)
            window!!.isOutsideTouchable=true
            window!!.showAtLocation(rootView,Gravity.BOTTOM,0,0)
            window!!.setOnDismissListener {
                window=null
                mCalendarView=null
                SingleMonthView.isCanSelected=true
                SingleMonthView.calendarClickListener=null
            }
        }
    }


    /**
     * 展现 日历 popupWindow
     */
    fun showCalendarDate(context:Context,rootView:View,basePresenter: ResourcePresenter,id:Int
                                                        ,calendarClickListener: CalendarClickListener){
        if(window==null || !window!!.isShowing){
            window=PopupWindow(context,null,R.style.Transparent_Dialog)
            var contentView= View.inflate(context, R.layout.activity_map_calendar,null)
            var mTextMonthDay = contentView.findViewById(R.id.tv_month_day) as TextView
            var mRelativeTool = contentView.findViewById(R.id.rl_tool) as RelativeLayout
            mCalendarView = contentView.findViewById(R.id.calendarView) as CalendarView
            calendar= Calendar.getInstance()
            calendar!!.set(Calendar.DAY_OF_MONTH,1)
            //设置日期拦截事件，仅适用单选模式，当前有效 mCalendarView.getCurDay()
            mTextMonthDay.setText(mCalendarView!!.getCurYear().toString()+"年" + mCalendarView!!.getCurMonth() + "月")
            SingleMonthView.calendarClickListener=calendarClickListener
            SingleMonthView.isFirstShow=true
            var  btn_last_month = contentView.findViewById(R.id.btn_last_month) as Button
            //上个月点击事件
            btn_last_month.setOnClickListener(View.OnClickListener {
                basePresenter.getResourceDateSchedule(ResourceDateRequest(id,DateUtil.dateFormat(getLastMonthCalendar(calendar!!).time)),CalendarAction.CalendarPre)
            })
            mCalendarView!!.setOnMonthChangeListener(CalendarView.OnMonthChangeListener { year, month ->
                if (year != 0 && month != 0) {
                    mTextMonthDay.setText(year.toString() + "年" + month + "月")
                    calendar!!.set(Calendar.YEAR,year)
                    calendar!!.set(Calendar.MONTH,month-1)
                }
            })
            mCalendarView!!.setMonthViewScrollable(false)
            mCalendarView!!.setOnYearChangeListener(CalendarView.OnYearChangeListener { })
            var  btn_next_month = contentView.findViewById(R.id.btn_next_month) as Button
            //下个月的点击事件
            btn_next_month.setOnClickListener(View.OnClickListener {
                //点击事件处理
                basePresenter.getResourceDateSchedule(ResourceDateRequest(id,DateUtil.dateFormat(getNextMonthCalendar(calendar!!).time)),CalendarAction.CalendarNext)
            })
            window!!.contentView=contentView
            window!!.width=AutoUtils.getPercentWidthSize(750)
            window!!.height=AutoUtils.getPercentHeightSize(1000)
            window!!.isOutsideTouchable=true
            window!!.showAtLocation(rootView,Gravity.BOTTOM,0,0)
            window!!.setOnDismissListener {
                window=null
                mCalendarView=null
                SingleMonthView.isCanSelected=true
                SingleMonthView.calendarClickListener=null
            }
        }
    }

    /**
     * 获取上个月的数据
     */
    fun getLastMonthCalendar(calendar: Calendar):Calendar{
        var calendarAction=Calendar.getInstance()
        calendarAction.time=calendar.time;
        var yearNumber=calendar.get(Calendar.YEAR);
        var monthNumber=calendar.get(Calendar.MONTH);
        SystemUtil.printlnStr("getLastMonthCalendar yearNumber:"+yearNumber+"...monthNumber:"+monthNumber)
        if(monthNumber==0){
            yearNumber=yearNumber-1
            monthNumber=11
        }else{
            monthNumber-=1
        }
        calendarAction.set(Calendar.YEAR,yearNumber)
        calendarAction.set(Calendar.MONTH,monthNumber)
        calendarAction.set(Calendar.DAY_OF_MONTH,1)
        SystemUtil.printlnStr("getLastMonthCalendar 11 yearNumber:"+calendarAction.get(Calendar.YEAR)+"...monthNumber:"
                +calendarAction.get(Calendar.MONTH)+"..date:"+DateUtil.dateFormat(calendarAction.time))
        return calendarAction
    }

    /**
     * 获取下个月的日期数据
     */
    fun getNextMonthCalendar(calendar: Calendar):Calendar{
        var calendarAction=Calendar.getInstance()
        calendarAction.time=calendar.time;
        var yearNumber=calendar.get(Calendar.YEAR);
        var monthNumber=calendar.get(Calendar.MONTH);
        SystemUtil.printlnStr("getNextMonthCalendar yearNumber:"+yearNumber+"...monthNumber:"+monthNumber)
        if(monthNumber==11){
            yearNumber=yearNumber+1
            monthNumber=0
        }else{
            monthNumber+=1
        }
        calendarAction.set(Calendar.YEAR,yearNumber)
        calendarAction.set(Calendar.MONTH,monthNumber)
        calendarAction.set(Calendar.DAY_OF_MONTH,1)
        SystemUtil.printlnStr("getNextMonthCalendar 11 yearNumber:"+calendarAction.get(Calendar.YEAR)+"...monthNumber:"
                                                                +calendarAction.get(Calendar.MONTH)+"..date:"+DateUtil.dateFormat(calendarAction.time))
        return calendarAction
    }
    /**
     * 展现出 前一个月
     */
    fun  preMonth(){
        if(window!=null && window!!.isShowing){
            //点击事件处理
            mCalendarView!!.scrollToPre(true)
        }
    }

    /**
     * 展现出下一个月
     */
    fun nextMonth(){
        if(window!=null && window!!.isShowing){
            //点击事件处理
            mCalendarView!!.scrollToNext(true)
        }
    }
}