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
import com.suntray.chinapost.baselibrary.rx.assertMethod
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
 *  审核通过的 fragment
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/23 16:17
 */
class ExamineFragment:Fragment(),TaskView {

    //任务的适配器
    var pageNumber=1;
    var pagePreNumber=-1
    var tagInfo=""
    var firstType=0
    var supplyID:String=""
    //任务的适配器
    var taskAdapter: TaskListViewAdapter?=null
    var taskPresenter: TaskPresenter?=null
    var contentView:View?=null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        SystemUtil.printlnStr("TaskListity ExamineFragment  setArguments ....")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.printlnStr("TaskListity ExamineFragment  onCreate ....hashcode:"+hashCode())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        SystemUtil.printlnStr("TaskListity ExamineFragment  onCreateView ....")
        if(contentView==null){
            contentView=inflater!!.inflate(R.layout.fragment_task,null)
        }
        return contentView
    }

    fun getNormalData(){
        //设置内容
        taskPresenter!!.getTaskListApi(TaskListRequest(UserDao.getLocalUser().id,"",pageNumber,10,firstType,3,supplyID), RefreshAction.NormalAction);
    }

    fun  clearData(){
        if(taskAdapter!=null &&  taskAdapter!!.datas!=null){
            taskAdapter!!.datas.clear()
            taskAdapter!!.notifyDataSetChanged()
        }
    }

    fun updateFirstType(firstType:Int){
        this.firstType=firstType
        if(taskAdapter!=null){
            taskAdapter!!.firstType=firstType
        }
    }
    override fun onResume() {
        super.onResume()
        SystemUtil.printlnStr("TaskListity ExamineFragment  onResume ....")
        /**
         * 点击 查询的按钮
         */
        recyclerview.setOnRefreshListener(object: PullToRefreshBase.OnRefreshListener<ListView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber=1;
                taskPresenter!!.getTaskListApi(
                        TaskListRequest(UserDao.getLocalUser().id,""
                                , pageNumber,10,firstType,3,supplyID), RefreshAction.PullDownRefresh);
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber+=1
                taskPresenter!!.getTaskListApi(
                        TaskListRequest(UserDao.getLocalUser().id,"",
                                pageNumber,10,firstType,3,supplyID), RefreshAction.UpMore);
            }
        })
    }


    override fun onError(content: String, action: RefreshAction) {
        if(action==RefreshAction.NormalAction){
            assertMethod(context,{
                ToastUtil.makeText(context!!,"暂无审核通过任务")
            })
        }else if(action==RefreshAction.PullDownRefresh){
            recyclerview.onPullDownRefreshComplete()
            assertMethod(context,{
                ToastUtil.makeText(context!!,content)
            })
        }else if(action==RefreshAction.UpMore){
            pageNumber-=1
            assertMethod(context,{
                ToastUtil.makeText(context!!,content)
            })
            recyclerview.onPullUpRefreshComplete()
        }else if(action==RefreshAction.SearchAction){
            assertMethod(context,{
                ToastUtil.makeText(context!!,content)
            })
        }
    }

    override fun onGetExamineList(taskList: ArrayList<TaskEntity>, action: RefreshAction,count:Int) {
        if(action==RefreshAction.NormalAction){
            SystemUtil.printlnStr("mineClientlist onGetExamineList NormalAction size:"+taskList.size)
            recyclerview.setRefreshTitle("我的客户列表,")
            if(taskAdapter==null){
                taskAdapter = TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity!!,3,firstType)
                recyclerview.getmListView().setAdapter(taskAdapter)
            }else {
                taskAdapter!!.datas = taskList;
                taskAdapter!!.notifyDataSetChanged()
            }
        }else if(action==RefreshAction.PullDownRefresh){
            //下拉刷新
            ToastUtil.makeText(context!!,"刷新完成...")
            SystemUtil.printlnStr("mineClientlist onGetExamineList size:"+taskList.size)
            if(taskAdapter==null){
                SystemUtil.printlnStr("mineClientlist onGetExamineList 11111:")
                taskAdapter = TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity!!,3,firstType)
                recyclerview.getmListView().setAdapter(taskAdapter)
            }else{
                SystemUtil.printlnStr("mineClientlist onGetExamineList 222222:")
                taskAdapter!!.datas=taskList;
                taskAdapter!!.notifyDataSetChanged()
            }
            recyclerview.onPullDownRefreshComplete()
        }else if(action==RefreshAction.UpMore){
            //上拉加载更多
            if(taskList.size>0){
                if(taskAdapter==null){
                    taskAdapter =TaskListViewAdapter(taskList, recyclerview.getRefreshableView(),activity!!,3,firstType)
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