package com.suntray.chinapost.map.ui.activity.proxy

import android.graphics.Color
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.RatingBar
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpFragment
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.injection.component.DaggerTaskComponent
import com.suntray.chinapost.map.presenter.TaskPresenter
import com.suntray.chinapost.map.presenter.view.TaskView
import com.suntray.chinapost.map.ui.fragment.ExamineFragment
import com.suntray.chinapost.map.ui.fragment.NotExamineFragment
import com.suntray.chinapost.map.ui.fragment.UnFinishFragment
import com.suntray.chinapost.map.ui.fragment.WillExamineFragment
import com.suntray.chinapost.map.ui.view.inner.OnRatioChanageListener
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.activity_task_list.*

/**
 *  任务列表的界
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 9:30
 */
@Route(path =RouterPath.MapModule.POST_TASK_LIST)
class TaskListActivity: BaseMvpFragment<TaskPresenter>(),TaskView{

    var currentIndex=0
    var fragmentList= mutableListOf<Fragment>()
    var unFinishFragment:UnFinishFragment?=null
    var willExamineFragment:WillExamineFragment?=null
    var notExamineFragment:NotExamineFragment?=null
    var examineFragment:ExamineFragment?=null
    var supplyID:String=""

    override fun initView() {
        isTitleShow=false
        isRightShow=false
        isBlackShow=false

        supplyID=intent.getStringExtra("supplyID")
        swipeBtnRelativeLayout.onRatingBarChangeListener=object: OnRatioChanageListener {
            override fun onChangeToLeft() {
                if(currentIndex!=0){
                    currentIndex=0
                    unFinishFragment!!.firstType=currentIndex+1
                    willExamineFragment!!.firstType=currentIndex+1
                    notExamineFragment!!.firstType=currentIndex+1
                    examineFragment!!.firstType=currentIndex+1

                    tv_down.background=null
                    tv_down.setTextColor(Color.parseColor("#909092"))
                    tv_up.setTextColor(Color.parseColor("#01764A"))
                }
            }

            override fun onChangeToRight() {
                if(currentIndex!=1){
                    currentIndex=1
                    unFinishFragment!!.firstType=currentIndex+1
                    willExamineFragment!!.firstType=currentIndex+1
                    notExamineFragment!!.firstType=currentIndex+1
                    examineFragment!!.firstType=currentIndex+1
                    tv_up.background=null
                    tv_up.setTextColor(Color.parseColor("#909092"))
                    tv_down.setTextColor(Color.parseColor("#01764A"))
                }
            }
        }

        //增加 对应的fragment
        unFinishFragment=UnFinishFragment()
        unFinishFragment!!.supplyID=supplyID
        unFinishFragment!!.taskPresenter=basePresenter
        fragmentList.add(unFinishFragment!!)
        willExamineFragment=WillExamineFragment()
        willExamineFragment!!.supplyID=supplyID
        willExamineFragment!!.taskPresenter=basePresenter
        fragmentList.add(willExamineFragment!!)
        notExamineFragment=NotExamineFragment()
        notExamineFragment!!.supplyID=supplyID
        notExamineFragment!!.taskPresenter=basePresenter
        fragmentList.add(notExamineFragment!!)
        examineFragment=ExamineFragment()
        examineFragment!!.supplyID=supplyID
        examineFragment!!.taskPresenter=basePresenter
        fragmentList.add(examineFragment!!)

        unFinishFragment!!.firstType=currentIndex+1
        willExamineFragment!!.firstType=currentIndex+1
        notExamineFragment!!.firstType=currentIndex+1
        examineFragment!!.firstType=currentIndex+1

        var fragmentAdapter=TaskFragmentAdapter(supportFragmentManager)
        viewpager_list.adapter=fragmentAdapter
        viewpager_list.setOnPageChangeListener(object:ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                //页面的选择
                if(position==0){
                    unFinishFragment!!.getNormalData()
                }else if(position==1){
                    willExamineFragment!!.getNormalData()
                }else if(position==2){
                    notExamineFragment!!.getNormalData()
                }else if(position==3){
                    examineFragment!!.getNormalData()
                }
                viewpager_list.setCurrentItem(position)
                setTextView(position)
            }
        })

        viewpager_list.setCurrentItem(0)
        unFinishFragment!!.getNormalData()

        //未完成 点击事件 0
        ll_unfinished.setOnClickListener({
            viewpager_list.setCurrentItem(0)
        })

        //审核不通过的点击事件 2
        ll_notexamine.setOnClickListener({
            viewpager_list.setCurrentItem(2)
        })

        //审核通过的点击事件 3
        ll_examine.setOnClickListener({
            viewpager_list.setCurrentItem(3)
        })

        //待审核的点击事件 1
        ll_willexamine.setOnClickListener({
            viewpager_list.setCurrentItem(1)
        })

        //我的模块的跳转
        iv_user_head.setOnClickListener {
            ARouter.getInstance().build(RouterPath.MineModule.MINE_ACTIVITY).navigation(this@TaskListActivity)
        }

        iv_chat.setOnClickListener({
            ARouter.getInstance().build(RouterPath.MineModule.MINE_MESSAGE).navigation(this@TaskListActivity)
        })
    }

    override fun onError(content: String, action: RefreshAction,position:Int) {
        SystemUtil.printlnStr("onError position:"+position)
        if(position==0){
            unFinishFragment!!.onError(content,action)
        }else if(position==1){
            willExamineFragment!!.onError(content,action)
        }else if(position==2){
            notExamineFragment!!.onError(content,action)
        }else if(position==3){
            examineFragment!!.onError(content,action)
        }
    }

    override fun onGetExamineList(taskList: ArrayList<TaskEntity>, action: RefreshAction) {
        super.onGetExamineList(taskList, action)
        examineFragment!!.onGetExamineList(taskList,action)
    }

    override fun onGetNotExamineList(taskList: ArrayList<TaskEntity>, action: RefreshAction) {
        super.onGetNotExamineList(taskList, action)
        notExamineFragment!!.onGetNotExamineList(taskList,action)
    }

    override fun onGetUnfinishedList(taskList: ArrayList<TaskEntity>, action: RefreshAction) {
        super.onGetUnfinishedList(taskList, action)
        unFinishFragment!!.onGetUnfinishedList(taskList,action)
    }

    override fun onGetWillExamineList(taskList: ArrayList<TaskEntity>, action: RefreshAction) {
        super.onGetWillExamineList(taskList, action)
        willExamineFragment!!.onGetWillExamineList(taskList,action)
    }


    /**
     * 设置对应位置的Ui
     */
    private fun setTextView(position: Int) {
       if(position==0){
           //未完成
           tv_unfinished.setTextColor(Color.parseColor("#01764A"))
           view_unfinished.setBackgroundColor(Color.parseColor("#01764A"))
           view_unfinished.visibility=View.VISIBLE

           tv_will_examine.setTextColor(Color.parseColor("#909092"))
           view_will_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_will_examine.visibility=View.INVISIBLE

           tv_not_examine.setTextColor(Color.parseColor("#909092"))
           view_not_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_not_examine.visibility=View.INVISIBLE

           tv_examine.setTextColor(Color.parseColor("#909092"))
           view_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_examine.visibility=View.INVISIBLE

       }else if(position==1){
           //待审核
           tv_unfinished.setTextColor(Color.parseColor("#909092"))
           view_unfinished.setBackgroundColor(Color.parseColor("#909092"))
           view_unfinished.visibility=View.INVISIBLE

           tv_will_examine.setTextColor(Color.parseColor("#01764A"))
           view_will_examine.setBackgroundColor(Color.parseColor("#01764A"))
           view_will_examine.visibility=View.VISIBLE

           tv_not_examine.setTextColor(Color.parseColor("#909092"))
           view_not_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_not_examine.visibility=View.INVISIBLE

           tv_examine.setTextColor(Color.parseColor("#909092"))
           view_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_examine.visibility=View.INVISIBLE
       }else if(position==2){
           //审核不通过
           tv_unfinished.setTextColor(Color.parseColor("#909092"))
           view_unfinished.setBackgroundColor(Color.parseColor("#909092"))
           view_unfinished.visibility=View.INVISIBLE

           tv_will_examine.setTextColor(Color.parseColor("#909092"))
           view_will_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_will_examine.visibility=View.INVISIBLE

           tv_not_examine.setTextColor(Color.parseColor("#01764A"))
           view_not_examine.setBackgroundColor(Color.parseColor("#01764A"))
           view_not_examine.visibility=View.VISIBLE

           tv_examine.setTextColor(Color.parseColor("#909092"))
           view_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_examine.visibility=View.INVISIBLE
       }else if(position==3){
           //审核通过
           tv_unfinished.setTextColor(Color.parseColor("#909092"))
           view_unfinished.setBackgroundColor(Color.parseColor("#909092"))
           view_unfinished.visibility=View.INVISIBLE

           tv_will_examine.setTextColor(Color.parseColor("#909092"))
           view_will_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_will_examine.visibility=View.INVISIBLE

           tv_not_examine.setTextColor(Color.parseColor("#909092"))
           view_not_examine.setBackgroundColor(Color.parseColor("#909092"))
           view_not_examine.visibility=View.INVISIBLE

           tv_examine.setTextColor(Color.parseColor("#01764A"))
           view_examine.setBackgroundColor(Color.parseColor("#01764A"))
           view_examine.visibility=View.VISIBLE
       }
    }


    override fun injectCompontent() {
        DaggerTaskComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun getView(): View {
        return View.inflate(this@TaskListActivity, R.layout.activity_task_list,null)
    }

    /**
     * fragment的适配器
     */
    inner class TaskFragmentAdapter:FragmentPagerAdapter{

        constructor(fragmentManager:FragmentManager):super(fragmentManager)

        override fun getItem(position: Int): Fragment {
            println("TaskFragmentAdapter getItem position:"+position)
            return fragmentList.get(position)
        }

        override fun getCount(): Int {
            if(fragmentList!=null){
                return fragmentList.size
            }
            return 0
        }
    }
}
