package com.suntray.chinapost.map.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.suntray.chinapost.map.ui.adapter.proxy.TaskListViewAdapter
import kotlinx.android.synthetic.main.fragment_task.*

/**
 *  待审核的fragment
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 16:17
 */
class WillExamineFragment:Fragment(),TaskView{

    //任务的适配器
    var pageNumber=1;
    var pagePreNumber=-1
    var tagInfo=""
    var firstType=1
    var supplyID:String=""
    //任务的适配器
    var taskAdapter: TaskListViewAdapter?=null
    var taskPresenter: TaskPresenter?=null
    var contentView:View?=null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        SystemUtil.printlnStr("TaskListity WillExamineFragment  setArguments ....")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.printlnStr("TaskListity WillExamineFragment  onCreate ....hashcode:"+hashCode())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        SystemUtil.printlnStr("TaskListity WillExamineFragment  onCreateView ....")
        if(contentView==null){
            contentView=inflater!!.inflate(R.layout.fragment_task,null)
        }
        return contentView
    }

    fun getNormalData(){
        //设置
        taskPresenter!!.getTaskListApi(TaskListRequest(UserDao.getLocalUser().id,"",pageNumber,10,firstType,2,supplyID), RefreshAction.NormalAction);
    }

    override fun onResume() {
        super.onResume()
        SystemUtil.printlnStr("TaskListity WillExamineFragment  onResume ....")
        /**
         * 点击 查询的按钮
         */
        recyclerview.setOnRefreshListener(object: PullToRefreshBase.OnRefreshListener<ListView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber=1;
                taskPresenter!!.getTaskListApi(
                        TaskListRequest(UserDao.getLocalUser().id,""
                                , pageNumber,10,firstType,2,supplyID), RefreshAction.PullDownRefresh);
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber+=1
                taskPresenter!!.getTaskListApi(
                        TaskListRequest(UserDao.getLocalUser().id,"",
                                pageNumber,10,firstType,2,supplyID), RefreshAction.UpMore);
            }
        })
    }

    override fun onError(content: String, action: RefreshAction) {
        if(action==RefreshAction.NormalAction){
            ToastUtil.makeText(context!!,"暂无待审核任务")
        }else if(action==RefreshAction.PullDownRefresh){
            recyclerview.onPullDownRefreshComplete()
            ToastUtil.makeText(context!!,content)
        }else if(action==RefreshAction.UpMore){
            pageNumber-=1
            ToastUtil.makeText(context!!,content)
            recyclerview.onPullUpRefreshComplete()
        }else if(action==RefreshAction.SearchAction){
            ToastUtil.makeText(context!!,content)
        }
    }

    override fun onGetWillExamineList(taskList: ArrayList<TaskEntity>, action: RefreshAction,count:Int) {
        if(action==RefreshAction.NormalAction){
            SystemUtil.printlnStr("mineClientlist onGetWillExamineList NormalAction size:"+taskList.size+"..taskAdapter:"+taskAdapter.toString())
            recyclerview.setRefreshTitle("我的客户列表,")
            if(taskAdapter==null){
                taskAdapter = TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity!!,1,firstType)
                SystemUtil.printlnStr("mineClientlist onGetWillExamineList 111111:"+"..taskAdapter:"+taskAdapter.toString())
                recyclerview.getmListView().setAdapter(taskAdapter)
            }else {
                taskAdapter!!.datas = taskList;
                taskAdapter!!.notifyDataSetChanged()
                SystemUtil.printlnStr("mineClientlist onGetWillExamineList 111111:"+"..taskAdapter:"+taskAdapter.toString())
            }
        }else if(action==RefreshAction.PullDownRefresh){
            //下拉刷新
            ToastUtil.makeText(context!!,"刷新完成...")
            SystemUtil.printlnStr("mineClientlist onGetWillExamineList size:"+taskList.size+"..taskAdapter:"+taskAdapter.toString())
            if(taskAdapter==null){
                taskAdapter = TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity!!,1,firstType)
                SystemUtil.printlnStr("mineClientlist onGetWillExamineList 111111:"+"..taskAdapter:"+taskAdapter.toString())
                recyclerview.getmListView().setAdapter(taskAdapter)
            }else{
                taskAdapter!!.datas=taskList;
                taskAdapter!!.notifyDataSetChanged()
                SystemUtil.printlnStr("mineClientlist onGetWillExamineList 2222222:"+"..taskAdapter:"+taskAdapter.toString())
            }
            recyclerview.onPullDownRefreshComplete()
        }else if(action==RefreshAction.UpMore){
            //上拉加载更多
            if(taskList.size>0){
                if(taskAdapter==null){
                    taskAdapter =TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity!!,1,firstType)
                    recyclerview.getmListView().setAdapter(taskAdapter)
                }else{
                    taskAdapter!!.datas.addAll(taskList);
                }
                taskAdapter!!.notifyDataSetChanged()
                ToastUtil.makeText(context!!,"加载更多完成...")
            }else{
                pageNumber-=1
                ToastUtil.makeText(context!!,"没有更多数据了...")
            }
            recyclerview.onPullUpRefreshComplete()
        }
    }
}