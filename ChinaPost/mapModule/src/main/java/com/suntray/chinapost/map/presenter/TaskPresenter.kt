package com.suntray.chinapost.map.presenter

import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.data.bean.TaskItem
import com.suntray.chinapost.map.data.request.TaskListRequest
import com.suntray.chinapost.map.data.request.TaskNumberRequest
import com.suntray.chinapost.map.data.request.TaskUploadRequest
import com.suntray.chinapost.map.data.request.UpdateRequest
import com.suntray.chinapost.map.data.response.TaskNumberResponse
import com.suntray.chinapost.map.data.response.UpdateResponse
import com.suntray.chinapost.map.presenter.view.MapView
import com.suntray.chinapost.map.presenter.view.TaskView
import com.suntray.chinapost.map.service.impl.OtherServiceImpl
import com.suntray.chinapost.map.service.impl.TaskServiceImpl
import com.suntray.chinapost.user.presenter.view.ClientView
import com.suntray.chinapost.user.presenter.view.MineEditView
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observable
import rx.Subscriber
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

    @Inject
    lateinit var otherService: OtherServiceImpl

    /**
     * 更新版本
     */
    fun updateAppVersion(updateRequest: UpdateRequest){
        SystemUtil.printlnStr("updateAppVersion .......:")
        otherService.update(updateRequest).execute(object : BaseSucriber<UpdateResponse>(baseView, MapPresenter::javaClass.name) {

            override fun onError(e: Throwable?) {
                if (e is ContentException) {
                    assertMethod(baseView, {
                        //                        (baseView as MapView).onError(e.errorContent)
                    })
                } else {
//                    super.onError(e)
                }
            }

            override fun onStart() {

            }

            override fun onNext(t: UpdateResponse) {
                super.onNext(t)
                (baseView as TaskView).onGetAppVersion(t)
            }
        }, lifecylerProvider)
    }

    /**
     * 获取 任务的数量
     */
    fun  getTaskNumber(taskNumberRequest: TaskNumberRequest){
        taskServiceImpl.getTaskNumber(taskNumberRequest)
                .execute(object: BaseSucriber<TaskNumberResponse>(baseView,TaskPresenter::class.java.simpleName!!) {
                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView).onError(e.errorContent)
                                (baseView).hideLoading()
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onStart() {

                    }
                    override fun onNext(t: TaskNumberResponse) {
                        (baseView as TaskView).onGetTaskNumber(t)
                        (baseView).hideLoading()
                    }
                },lifecylerProvider)
    }


    /**
     *  获取任务列表 第二个的方法
     */
    fun getTaskListApi2(taskListRequest: TaskListRequest,action: RefreshAction) {
        taskServiceImpl.getTaskListApi2(taskListRequest).
                execute(object :BaseSucriber<TaskItem>(baseView,TaskPresenter::class.java.simpleName!!){

                    override fun onNext(t: TaskItem) {
                        assertMethod(baseView,{
                            if(taskListRequest.state==1){
                                //未完成
                                (baseView as TaskView).onGetUnfinishedList(t.list!!,action,t.count)
                            }else if(taskListRequest.state==2){
                                //待审核
                                (baseView as TaskView).onGetWillExamineList(t.list!!,action)
                            }else if(taskListRequest.state==3){
                                //审核不通过
                                (baseView as TaskView).onGetExamineList(t.list!!,action)
                            }else if(taskListRequest.state==4){
                                //审核通过 onGetNotExamineList
                                (baseView as TaskView).onGetNotExamineList(t.list!!,action,t.count)
                            }

                        })
                    }

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as TaskView).onError(e.errorContent,action,taskListRequest.state);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as TaskView).onError("请求超时!",action,taskListRequest.state)
                            }else{
                                (baseView as TaskView).onError("请求失败",action,taskListRequest.state)
                            }
                        }
                    }

                },lifecylerProvider)
    }
    /***
     * 请求任务列表
     */
    fun getTaskListApi(taskListRequest: TaskListRequest,action: RefreshAction){
        taskServiceImpl.getTaskListApi(taskListRequest).
                execute(object :BaseSucriber<ArrayList<TaskEntity>>(baseView,TaskPresenter::class.java.simpleName!!){

                    override fun onNext(t: ArrayList<TaskEntity>) {
                        println("getTaskListApi t size:"+t.size+"..baseView:"+baseView.toString())
                        assertMethod(baseView,{
                            if(taskListRequest.state==1){
                                //未完成
                                (baseView as TaskView).onGetUnfinishedList(t,action)
                            }else if(taskListRequest.state==2){
                                //待审核
                                (baseView as TaskView).onGetWillExamineList(t,action)
                            }else if(taskListRequest.state==3){
                                //审核不通过
                                (baseView as TaskView).onGetExamineList(t,action)
                            }else if(taskListRequest.state==4){
                                //审核通过 onGetNotExamineList
                                (baseView as TaskView).onGetNotExamineList(t,action)
                            }
                        })
                    }

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as TaskView).onError(e.errorContent,action,taskListRequest.state);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as TaskView).onError("请求超时!",action,taskListRequest.state)
                            }else{
                                (baseView as TaskView).onError("请求失败",action,taskListRequest.state)
                            }
                        }
                    }

        },lifecylerProvider)
    }

    /**
     * 上传 task的img
     */
     fun uploadTaskImg( pointTaskId:String="",
                        taskId:String="",
                        type:Int,
                        userId:Int,
                        imgFiles:List<MultipartBody.Part> ?=null ,
                        imgIds:Array<Int?> ? =null ,
                        deleteIds:Array<Int?> ? =null,
                        descritpion: RequestBody? = null){
        taskServiceImpl.uploadTaskImg(TaskUploadRequest(pointTaskId,taskId,type,userId,imgFiles,imgIds,deleteIds,descritpion))
                    .execute(object: BaseSucriber<Object>(baseView,TaskPresenter::class.java.simpleName!!) {
                        override fun onError(e: Throwable?) {
                            if(e is ContentException){
                                assertMethod(baseView,{
                                    (baseView).onError(e.errorContent)
                                    (baseView).hideLoading()
                                })
                            }else{
                                super.onError(e)
                            }
                        }

                        override fun onNext(t: Object) {
                            (baseView as TaskView).onUploadTaskImg()
                            (baseView).hideLoading()
                        }
                    },lifecylerProvider)
     }
}
