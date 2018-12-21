package com.suntray.chinapost.map.ui.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.TextView
import com.nineoldandroids.animation.Animator
import com.nineoldandroids.animation.ValueAnimator
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.view.inner.OnRatioChanageListener
import com.zhy.autolayout.AutoRelativeLayout

/**
 *  手指滑动 切换的relativeLayout
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/30 14:15
 */
class SwipeBtnRelativeLayout: AutoRelativeLayout{
    constructor(context: Context?):this(context,null)
    constructor(context: Context?,attrs: AttributeSet?):this(context,attrs,-1)
    constructor(context: Context?,attrs: AttributeSet?,defStyle:Int):super(context,attrs,defStyle)

   var onRatingBarChangeListener: OnRatioChanageListener?=null
    var leftValue:Int=0
    var topValue:Int=0
    var rightValue:Int=0
    var bottomValue:Int=0
    var paddingLeftValue=0
    var paddingRightValue=0
    var paddingTopValue=0
    var paddingBottomValue=0

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        leftValue=paddingLeft
        bottomValue=bottomValue-paddingBottom
        rightValue=rightValue-paddingRight
        topValue=paddingTop


        SystemUtil.printlnStr("SwipeBtnRelativeLayout onLayout  leftValue:"+leftValue+"..topValue:"+topValue+"..rightValue:"+rightValue
                +"..bottomValue:"+bottomValue+"..middleRight:"+tv_middle!!.right+"..middleLeft:"+tv_middle!!.left+"..middleTop:"+tv_middle!!.top+"..middleBottom:"+tv_middle!!.bottom)
        SystemUtil.printlnStr("SwipeBtnRelativeLayout onLayout paddingLeft:"+paddingLeft+"..paddingRight:"+paddingRight+
                                                                                "...paddingTop:"+paddingTop+"...paddingBottom:"+paddingBottom)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    var tv_middle:TextView?=null

    override fun onFinishInflate() {
        super.onFinishInflate()
        tv_middle=findViewById(R.id.tv_middle) as TextView
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        leftValue=0
        rightValue=w
        topValue=0
        bottomValue=h
        SystemUtil.printlnStr("SwipeBtnRelativeLayout onSizeChanged  w:"+w+"..h:"+h+"..oldw:"+oldw+"..oldh:"+oldh)
    }

    var lastX:Float=0f
    var lastY:Float=0f
    var dxAll=0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN->{
                lastX=event.x
                lastY=event.y
                dxAll=0f
                SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_DOWN x:"+event!!.x+"..y:"+event!!.y)
                return true
            }
            MotionEvent.ACTION_MOVE->{

                SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_MOVE tv_middle!!.left:"+tv_middle!!.left+
                                "..tv_middle!!.right:"+tv_middle!!.right+"..tv_middle!!.top:"+tv_middle!!.top+
                        "...tv_middle!!.bottom:"+tv_middle!!.bottom+"..tv_middle.x:"+tv_middle!!.x+"..tv_middle.y:"+tv_middle!!.y)

                //先进行获取 移动的范围 dx dy
                var dx=lastX-event.x
                var dy=lastY-event.y
                //更新 最新一次的 lastX 和 lastY
                lastX=event!!.x
                lastY=event!!.y
                //必须判断 坐标 tv_middle的x y坐标 没有超过边界 (注意 tv_middle的边界的 left top right bottom 不会变化)
                //对挺的 translateX 可以变化的
                if((tv_middle!!.x-dx)>=leftValue.toFloat() &&
                        (tv_middle!!.x+(tv_middle!!.right-tv_middle!!.left)-dx)<=rightValue.toFloat()){
                    tv_middle!!.translationX=tv_middle!!.translationX-dx
                    //累计移动 量
                    dxAll+=dx
                    SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_MOVE x:"+event!!.x+"..y:"+event!!.y+"..dx:"+dx+"..dxToInt:"+dx+"..dy:"+dy+"..dyToInt:"+dy+"..dxAll:"+dxAll)
                }
                return true
            }
            MotionEvent.ACTION_UP->{
                if(Math.abs(dxAll)>=(tv_middle!!.right-tv_middle!!.left)/2){
                    //如果 累计移动量 > 一半的长度
                    SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_UP 超过一半的长度")
                    //划向目标点
                    if(dxAll<0){
                        //向右滑
                        SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_UP 超过一半的长度 向右滑")
                        var translateXAll=(rightValue.toFloat()-leftValue.toFloat())-(tv_middle!!.right-tv_middle!!.left)
                        var value=ValueAnimator.ofFloat(dxAll,-translateXAll)
                        value.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
                            override fun onAnimationUpdate(p0: ValueAnimator?) {
                                tv_middle!!.translationX=-(p0!!.animatedValue as Float)
                            }
                        })
                        value.addListener(object:Animator.AnimatorListener{
                            override fun onAnimationCancel(p0: Animator?) {
                            }
                            override fun onAnimationStart(p0: Animator?) {
                            }
                            override fun onAnimationEnd(p0: Animator?) {
                                //结束 的事件
                                if(onRatingBarChangeListener!=null){
                                    SystemUtil.printlnStr("SwipeBtnRelativeLayout  onAnimationEnd onChangeToRight:")
                                    onRatingBarChangeListener!!.onChangeToRight()
                                }
                            }
                            override fun onAnimationRepeat(p0: Animator?) {
                            }
                        })
                        value.start()
                    }else{
                        //向左滑
                        SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_UP 超过一半的长度 向左滑")
                        var translateXAll=(rightValue.toFloat()-leftValue.toFloat())-(tv_middle!!.right-tv_middle!!.left)
                        var value=ValueAnimator.ofFloat(translateXAll-dxAll,0f)
                        value.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
                            override fun onAnimationUpdate(p0: ValueAnimator?) {
                                tv_middle!!.translationX=-(p0!!.animatedValue as Float)
                                SystemUtil.printlnStr("SwipeBtnRelativeLayout onAnimationUpdate value:"+p0!!.animatedValue)
                            }
                        })
                        value.addListener(object:Animator.AnimatorListener{
                            override fun onAnimationCancel(p0: Animator?) {
                            }
                            override fun onAnimationStart(p0: Animator?) {
                            }
                            override fun onAnimationEnd(p0: Animator?) {
                                //结束 的事件
                                SystemUtil.printlnStr("SwipeBtnRelativeLayout  onAnimationEnd onChangeToLeft:")
                                if(onRatingBarChangeListener!=null){
                                    onRatingBarChangeListener!!.onChangeToLeft()
                                }
                            }
                            override fun onAnimationRepeat(p0: Animator?) {
                            }
                        })
                        value.start()
                    }
                }else{
                    //回到原位
                    if(dxAll<0){
                        //向右滑 回到左边
                        SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_UP 小于一半的长度 向右滑 回到左边")
                        var value=ValueAnimator.ofFloat(dxAll,0f)
                        value.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
                            override fun onAnimationUpdate(p0: ValueAnimator?) {
                                tv_middle!!.translationX=p0!!.animatedValue as Float
                                SystemUtil.printlnStr("SwipeBtnRelativeLayout onAnimationUpdate value:"+p0!!.animatedValue)
                            }
                        })
                        value.addListener(object:Animator.AnimatorListener{
                            override fun onAnimationCancel(p0: Animator?) {
                            }
                            override fun onAnimationStart(p0: Animator?) {
                            }
                            override fun onAnimationEnd(p0: Animator?) {
                                //结束 的事件
                                SystemUtil.printlnStr("SwipeBtnRelativeLayout  onAnimationEnd onChangeToLeft:")
                                if(onRatingBarChangeListener!=null){
                                    onRatingBarChangeListener!!.onChangeToLeft()
                                }
                            }
                            override fun onAnimationRepeat(p0: Animator?) {
                            }
                        })
                        value.start()
                    }else{
                        //向左滑 回到右边
                        SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_UP 小于一半的长度 向左滑 回到右边")
                        var translateXAll=(rightValue.toFloat()-leftValue.toFloat())-(tv_middle!!.right-tv_middle!!.left)
                        var value=ValueAnimator.ofFloat(-translateXAll+dxAll,-translateXAll)
                        value.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
                            override fun onAnimationUpdate(p0: ValueAnimator?) {
                                //todo
                                tv_middle!!.translationX=-(p0!!.animatedValue as Float)
                                SystemUtil.printlnStr("SwipeBtnRelativeLayout onAnimationUpdate value:"+(p0!!.animatedValue as Float)+"..tv_middle.x:"+tv_middle!!.x)
                            }
                        })
                        value.addListener(object:Animator.AnimatorListener{
                            override fun onAnimationCancel(p0: Animator?) {
                            }
                            override fun onAnimationStart(p0: Animator?) {
                            }
                            override fun onAnimationEnd(p0: Animator?) {
                                //结束 的事件
                                SystemUtil.printlnStr("SwipeBtnRelativeLayout  onAnimationEnd onChangeToRight:")
                                if(onRatingBarChangeListener!=null){
                                    onRatingBarChangeListener!!.onChangeToRight()
                                }
                            }
                            override fun onAnimationRepeat(p0: Animator?) {
                            }
                        })
                        value.start()
                    }
                }
                SystemUtil.printlnStr("SwipeBtnRelativeLayout onTouchEvent  ACTION_UP x:"+event!!.x+"..y:"+event!!.y)
                return true
             }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 平滑到 左边
     */
    fun smoothToLeft(){
        var translateXAll=(rightValue.toFloat()-leftValue.toFloat())-(tv_middle!!.right-tv_middle!!.left)
        var value=ValueAnimator.ofFloat(-translateXAll,0f)
        value.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(p0: ValueAnimator?) {
                //todo
                tv_middle!!.translationX=-(p0!!.animatedValue as Float)
                SystemUtil.printlnStr("SwipeBtnRelativeLayout onAnimationUpdate value:"+(p0!!.animatedValue as Float)+"..tv_middle.x:"+tv_middle!!.x)
            }
        })
        value.start()
    }

    fun smoothToRight(){
        var translateXAll=(rightValue.toFloat()-leftValue.toFloat())-(tv_middle!!.right-tv_middle!!.left)
        var value=ValueAnimator.ofFloat(0f,translateXAll)
        value.addUpdateListener(object :ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(p0: ValueAnimator?) {
                //todo
                tv_middle!!.translationX=p0!!.animatedValue as Float
                SystemUtil.printlnStr("SwipeBtnRelativeLayout onAnimationUpdate value:"+(p0!!.animatedValue as Float)+"..tv_middle.x:"+tv_middle!!.x)
            }
        })
        value.start()
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        when(ev!!.action){
            MotionEvent.ACTION_DOWN->{
                   return false
            }
            MotionEvent.ACTION_MOVE->{

            }

            MotionEvent.ACTION_UP->{
                return false
            }
        }
        return true
    }
}