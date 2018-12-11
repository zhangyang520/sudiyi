package com.suntray.chinapost.map.ui.calendar

import android.content.Context
import android.graphics.*
import android.view.View
import com.haibin.calendarview.Calendar
import com.haibin.calendarview.MonthView
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import java.util.*

/**
 * 高仿魅族日历布局
 * Created by huanghaibin on 2017/11/15.
 */

class SingleMonthView(context: Context) : MonthView(context) {

    private var mRadius: Int = 0
    private val mRingPaint = Paint()
    private var mRingRadius: Int = 0

    private val unusePaint = Paint()  //不可用的状态 红色
    private val hasLockedPaint = Paint() //已锁定的状态 灰色
    private val hasReservedPaint = Paint()//已预订的状态 绿色
    private val idlePaint = Paint()// 空闲的状态 橙色


    /**
     * 不可用画笔
     */
    private val mDisablePaint = Paint()

    private val mH: Int

    init {

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint)
        //4.0以上硬件加速会导致无效
        mSelectedPaint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.SOLID)

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemePaint)
        mSchemePaint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.SOLID)

        mRingPaint.isAntiAlias = true
        mRingPaint.color = mSchemePaint.color
        mRingPaint.style = Paint.Style.STROKE
        mRingPaint.strokeWidth = dipToPx(context, 1f).toFloat()
        setLayerType(View.LAYER_TYPE_SOFTWARE, mRingPaint)
        mRingPaint.maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.SOLID)

        //不可用的状态
        unusePaint.isAntiAlias = true
        unusePaint.color = Color.RED
        unusePaint.strokeWidth = dipToPx(context, 1f).toFloat()
        unusePaint.style = Paint.Style.FILL
        unusePaint.textAlign = Paint.Align.CENTER
        unusePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        unusePaint.textSize = dipToPx(context, 14f).toFloat()


        //已锁定
        hasLockedPaint.isAntiAlias = true
        hasLockedPaint.color = Color.GRAY
        hasLockedPaint.strokeWidth = dipToPx(context, 1f).toFloat()
        hasLockedPaint.style = Paint.Style.FILL
        hasLockedPaint.textAlign = Paint.Align.CENTER
        hasLockedPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        hasLockedPaint.textSize = dipToPx(context, 14f).toFloat()

        //已预订
        hasReservedPaint.isAntiAlias = true
        hasReservedPaint.color =Color.parseColor("#FF9123")
        //        hasReservedPaint.setTextSize(dipToPx(context,25));
        hasReservedPaint.strokeWidth = dipToPx(context, 1f).toFloat()
        hasReservedPaint.style = Paint.Style.FILL
        hasReservedPaint.textAlign = Paint.Align.CENTER
        hasReservedPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        hasReservedPaint.textSize = dipToPx(context, 14f).toFloat()


        //空闲的状态
        idlePaint.isAntiAlias = true
        idlePaint.color = Color.parseColor("#006D46")
        idlePaint.strokeWidth = dipToPx(context, 1f).toFloat()
        idlePaint.style = Paint.Style.FILL
        idlePaint.textAlign = Paint.Align.CENTER
        idlePaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        idlePaint.textSize = dipToPx(context, 14f).toFloat()


        mH = dipToPx(context, 18f)

    }

    override fun onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 2-10
        mRingRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2
        mSelectTextPaint.textSize = dipToPx(context, 17f).toFloat()
    }

    /**
     * 如果需要点击Scheme没有效果，则return true
     *
     * @param canvas    canvas
     * @param calendar  日历日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme hasScheme 非标记的日期
     * @return false 则不绘制onDrawScheme，因为这里背景色是互斥的
     */
    override fun onDrawSelected(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean): Boolean {
        SystemUtil.printlnStr("SingleMonthView onDrawSelected .....")
        if(isCanSelected){
            //是否能选择
            if(stateArray!=null && !isFirstShow){
                val state = stateArray!![calendar.day-1]
                if ((state == 0 || state == 4)) {
                    //绘制 今天的日期
                    var  cx = (x + mItemWidth / 2).toFloat() ;
                    var  cy = (y + mItemHeight / 2 +25).toFloat();

                    canvas.drawCircle(cx, cy, mRadius.toFloat(), mSelectedPaint);
                    //日期的回调
                    if(calendarClickListener!=null){
                        calendarClickListener!!.onClickCalendar(calendar)
                    }
                }else{
                    ToastUtil.makeText(context,"不能选择非空闲状态")
                }
            }else{
                isFirstShow=false
            }
        }
        return true
    }

    override fun onDrawScheme(canvas: Canvas, calendar: Calendar, x: Int, y: Int) {
        SystemUtil.printlnStr("onDrawScheme .....")
    }

    override fun onDrawText(canvas: Canvas, calendar: Calendar, x: Int, y: Int, hasScheme: Boolean, isSelected: Boolean) {

        if(stateArray!=null){
          SystemUtil.printlnStr("SingleMonthView onDrawText .....stateArray:"+(stateArray==null)+"..stateArray size:"+stateArray!!.size+"..calendar:"+calendar.day)
        }else{
            SystemUtil.printlnStr("SingleMonthView onDrawText .....stateArray:"+(stateArray==null))
        }
        val baselineY = mTextBaseLine + y - dipToPx(context, 1f)
        val cx = x + mItemWidth / 2

        if (stateArray != null && stateArray!!.size >= calendar.day) {
            val state = stateArray!![calendar.day-1]
            if (state == 0 || state == 4) {
                canvas.drawText(calendar.day.toString() + "",
                        cx.toFloat(),
                        baselineY,
                        idlePaint)

                canvas.drawText("空闲",
                        cx.toFloat(),
                        baselineY + 50,
                        idlePaint)

            } else if (state == 1) {
                //不可用的状态
                canvas.drawText(calendar.day.toString() + "",
                        cx.toFloat(),
                        baselineY,
                        unusePaint)

                canvas.drawText("不可用",
                        cx.toFloat(),
                        baselineY + 50,
                        unusePaint)
            } else if (state == 2) {
                //不可用的状态
                canvas.drawText(calendar.day.toString() + "",
                        cx.toFloat(),
                        baselineY,
                        hasLockedPaint)

                canvas.drawText("已锁定",
                        cx.toFloat(),
                        baselineY + 50,
                        hasLockedPaint)

            } else if (state == 3) {
                canvas.drawText(calendar.day.toString() + "",
                        cx.toFloat(),
                        baselineY,
                        hasReservedPaint)

                canvas.drawText("已预订",
                        cx.toFloat(),
                        baselineY + 50,
                        hasReservedPaint)
            }
        }
    }

    companion object {

        var stateArray:Array<Int>? = null
        var isFirstShow=false
        //是否能够 选择
        var isCanSelected=true

        /**
         * dp转px
         *
         * @param context context
         * @param dpValue dp
         * @return px
         */
        private fun dipToPx(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        var calendarClickListener:CalendarClickListener?=null
    }
}
