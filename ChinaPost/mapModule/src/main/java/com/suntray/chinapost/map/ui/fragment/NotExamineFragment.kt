package com.suntray.chinapost.map.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.ui.refreshView.PullToRefreshBase
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.data.request.TaskListRequest
import com.suntray.chinapost.map.presenter.TaskPresenter
import com.suntray.chinapost.map.presenter.view.TaskView
import com.suntray.chinapost.map.ui.adapter.proxy.TaskAdapter
import com.suntray.chinapost.map.ui.adapter.proxy.TaskListViewAdapter
import kotlinx.android.synthetic.main.fragment_task.*

/**
 *  审核不通过 fragment
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 16:17
 */
class NotExamineFragment:Fragment(),TaskView {

    //任务的适配器
    var pageNumber=1;
    var pagePreNumber=-1
    var tagInfo=""
    var firstType=0
    var supplyID:String=""
    //任务的适配器
    var taskAdapter: TaskListViewAdapter?=null
    var taskPresenter: TaskPresenter?=null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        SystemUtil.printlnStr("TaskListity NotExamineFragment  setArguments ....")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.printlnStr("TaskListity NotExamineFragment  onCreate ....hashcode:"+hashCode())
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        SystemUtil.printlnStr("TaskListity NotExamineFragment  onCreateView ....")
        return inflater!!.inflate(R.layout.fragment_task,null)
    }

    fun getNormalData(){
        //设置内容
        taskPresenter!!.getTaskListApi(TaskListRequest(UserDao.getLocalUser().id,"",pageNumber,10,firstType,3,supplyID), RefreshAction.NormalAction);
    }

    override fun onResume() {
        super.onResume()
        SystemUtil.printlnStr("TaskListity NotExamineFragment  onResume ....")
        /**
         * 点击 查询的按钮
         */
        recyclerview.setOnRefreshListener(object: PullToRefreshBase.OnRefreshListener<ListView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber=1;
                taskPresenter!!.getTaskListApi(
                        TaskListRequest(UserDao.getLocalUser().id,""
                                , pageNumber,10,firstType,2), RefreshAction.PullDownRefresh);
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber+=1
                taskPresenter!!.getTaskListApi(
                        TaskListRequest(UserDao.getLocalUser().id,"",
                                pageNumber,10,firstType,2), RefreshAction.UpMore);
            }
        })
     }

    override fun onError(content: String, action: RefreshAction) {
        if(action==RefreshAction.NormalAction){
            ToastUtil.makeText(context,"暂无审核不通过任务")
        }else if(action==RefreshAction.PullDownRefresh){
            recyclerview.onPullDownRefreshComplete()
            ToastUtil.makeText(context,content)
        }else if(action==RefreshAction.UpMore){
            pageNumber-=1
            ToastUtil.makeText(context,content)
            recyclerview.onPullUpRefreshComplete()
        }else if(action==RefreshAction.SearchAction){
            ToastUtil.makeText(context,content)
        }
    }

    override fun onGetUnfinishedList(taskList: ArrayList<TaskEntity>, action: RefreshAction) {
        super.onGetUnfinishedList(taskList, action)
        if(action==RefreshAction.NormalAction){
            recyclerview.setRefreshTitle("我的客户列表,")
            if(taskAdapter==null){
                taskAdapter = TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity,2,firstType)
                recyclerview.getmListView().setAdapter(taskAdapter)
            }else {
                taskAdapter!!.datas = taskList;
                taskAdapter!!.notifyDataSetChanged()
            }
        }else if(action==RefreshAction.PullDownRefresh){
            //下拉刷新
            ToastUtil.makeText(context,"刷新完成...")
            SystemUtil.printlnStr("mineClientlist size:"+taskList.size)
            if(taskAdapter==null){
                taskAdapter = TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity,2,firstType)
                recyclerview.getmListView().setAdapter(taskAdapter)
            }else{
                taskAdapter!!.datas=taskList;
            }
            taskAdapter!!.notifyDataSetChanged()
            recyclerview.onPullDownRefreshComplete()
        }else if(action==RefreshAction.UpMore){
            //上拉加载更多
            if(taskList.size>0){
                if(taskAdapter==null){
                    taskAdapter =TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity,2,firstType)
                    recyclerview.getmListView().setAdapter(taskAdapter)
                }else{
                    taskAdapter!!.datas.addAll(taskList);
                }
                taskAdapter!!.notifyDataSetChanged()
                ToastUtil.makeText(context,"加载更多完成...")
            }else{
                pageNumber-=1
                ToastUtil.makeText(context,"没有更多数据了...")
            }
            recyclerview.onPullUpRefreshComplete()
        }
    }

    private fun getDatas(): ArrayList<TaskEntity> {
        var list= arrayListOf<TaskEntity>()
        list.add(TaskEntity("北京大学东门","中国邮政","北京大学","2018-10-30","定位名称"))
        list.add(TaskEntity("北京大学东门","中国邮政","北京大学","2018-10-30","定位名称"))
        list.add(TaskEntity("北京大学东门","中国邮政","北京大学","2018-10-30","定位名称"))
        list.add(TaskEntity("北京大学东门","中国邮政","北京大学","2018-10-30","定位名称"))
        list.add(TaskEntity("北京大学东门","中国邮政","北京大学","2018-10-30","定位名称"))
        list.add(TaskEntity("北京大学东门","中国邮政","北京大学","2018-10-30","定位名称"))
        list.add(TaskEntity("北京大学东门","中国邮政","北京大学","2018-10-30","定位名称"))
        return list
    }
}