package com.suntray.chinapost.map.presenter

import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.data.request.TaskListRequest
import com.suntray.chinapost.map.presenter.view.TaskView
import com.suntray.chinapost.map.service.impl.TaskServiceImpl
import com.suntray.chinapost.user.presenter.view.MineEditView
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 *  任务界面 的业务类
 *  @Author 张扬 @version 1.0
 *  @Date  11:36
 */
class TaskPresenter @Inject constructor():BasePresenter<TaskView>(){

    @Inject
    lateinit var taskServiceImpl: TaskServiceImpl
    /***
     * 请求任务列表
     */
    fun getTaskListApi(taskListRequest: TaskListRequest,action: RefreshAction){
        taskServiceImpl.getTaskListApi(taskListRequest).
                execute(object :BaseSucriber<ArrayList<TaskEntity>>(baseView,TaskPresenter::class.java.simpleName!!){

                    override fun onNext(t: ArrayList<TaskEntity>) {
                        assertMethod(baseView,{
                            if(taskListRequest.taskSecondeType==0){
                                //未完成
                                (baseView as TaskView).onGetUnfinishedList(t,action)
                            }else if(taskListRequest.taskSecondeType==1){
                                //待审核
                                (baseView as TaskView).onGetWillExamineList(t,action)
                            }else if(taskListRequest.taskSecondeType==2){
                                //审核不通过
                                (baseView as TaskView).onGetNotExamineList(t,action)
                            }else if(taskListRequest.taskSecondeType==3){
                                //审核通过
                                (baseView as TaskView).onGetExamineList(t,action)
                            }

                        })
                    }

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as TaskView).onError(e.errorContent,action,taskListRequest.taskSecondeType);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as TaskView).onError("请求超时!",action,taskListRequest.taskSecondeType)
                            }else{
                                (baseView as TaskView).onError("请求失败",action,taskListRequest.taskSecondeType)
                            }
                        }
                    }

        },lifecylerProvider)
    }
}