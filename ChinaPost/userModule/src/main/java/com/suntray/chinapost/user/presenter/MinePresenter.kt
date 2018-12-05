package com.suntray.chinapost.user.presenter

import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.user.data.bean.EditPwdRequest
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.request.MineMessageRequest
import com.suntray.chinapost.user.data.request.MyClientRequest
import com.suntray.chinapost.user.data.request.ViewMessageRequest
import com.suntray.chinapost.user.presenter.view.MineEditView
import com.suntray.chinapost.user.service.impl.MineServiceImpl
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observable
import java.io.File
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 *  我的模块的业务处理
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:54
 */
class MinePresenter @Inject constructor():BasePresenter<MineEditView> (){


    @Inject
    lateinit var mineService: MineServiceImpl
    /**
     * 修改密码
     */
    fun  editPwd(oldPwd:String,newPwd:String,userId:Int,email:String,type:Int){
        mineService.editPwd(EditPwdRequest(email,oldPwd,newPwd,userId,type)).
                execute(object:BaseSucriber<Object>(baseView,MinePresenter::javaClass.name){
                        override fun onError(e: Throwable?) {
                            if(e is ContentException){
                                assertMethod(baseView,{
                                    baseView.onError(e.errorContent);
                                    baseView.hideLoading()
                                })
                            }else{
                                super.onError(e)
                            }
                        }

                        override fun onNext(t: Object) {
                            super.onNext(t)
                            assertMethod(baseView,{
                                (baseView as MineEditView).onEditPwdRequest()
                            })
                        }
        },lifecylerProvider);
    }


    /**
     * 上传头像成功
     */
    fun onUploadPortrait(id:Int,file:File,type:Int){
        val requestBody = RequestBody.create(MediaType.parse("imgage/png"), file)
        val body = MultipartBody.Part.createFormData("imgFile", file.name, requestBody)
        //进行构建描述类型的RequestBody的description
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), "this is a description")
        mineService.uploadPortrait(description,id,body,type).execute(
                object:BaseSucriber<String>(baseView,MinePresenter::javaClass.name!!){
                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                baseView.onError(e.errorContent);
                                baseView.hideLoading()
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onNext(t: String) {
                        super.onNext(t)
                        SystemUtil.printlnStr("onUploadPortrait url:"+t)
                        try {
                            var user=UserDao.getLocalUser()
                            user.headImgPath=t;
                            UserDao.saveUpDate(user)
                            assertMethod(baseView,{
                                (baseView as MineEditView).onUploadPortrait("上传头像成功")
                            })
                        } catch (e: Exception) {
                            assertMethod(baseView,{
                                (baseView as MineEditView).onUploadPortrait("用户尚未登录")
                            })
                        }

                    }
        },lifecylerProvider)
    }


    /**
     * 我的客户查询..
     */
    fun myClient(salesman:Int,name:String,page:Int,rows:Int,action: RefreshAction){
        mineService.myClient(MyClientRequest(salesman, name, page, rows,-1,1)).
                execute(object:BaseSucriber<ArrayList<MineClient>>(baseView,MinePresenter::javaClass.name){
                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MineEditView).onError(e.errorContent,action);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as MineEditView).onError("请求超时!",action)
                            }else{
                                (baseView as MineEditView).onError("请求失败",action)
                            }
                        }
                    }

                    override fun onNext(t: ArrayList<MineClient>) {
                        super.onNext(t)
                        assertMethod(baseView,{
                            (baseView as MineEditView).onMineClient(t,action)
                        })
                    }
                },lifecylerProvider);
    }


    /**
     * 我的消息
     */
    fun myMessage(userId:Int,page:Int,rows:Int,action:RefreshAction){
        mineService.myMessage(MineMessageRequest(userId,page,rows)).
                execute(object:BaseSucriber<ArrayList<MineMessage>>(baseView,MinePresenter::javaClass.name){
                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MineEditView).onError(e.errorContent,action);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as MineEditView).onError("请求超时!",action)
                            }else{
                                (baseView as MineEditView).onError("请求失败",action)
                            }
                        }
                    }

                    override fun onNext(t: ArrayList<MineMessage>) {
                        super.onNext(t)
                        assertMethod(baseView,{
                            (baseView as MineEditView).onMineMessage(t,action)
                        })
                    }
                },lifecylerProvider);
    }


    /**
     * 查看消息
     */
    fun viewMessage(mineMessageRequest: ViewMessageRequest){
        mineService.viewMessage(mineMessageRequest).
                execute(object:BaseSucriber<ArrayList<Object>>(baseView,MinePresenter::javaClass.name){
                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MineEditView).onError(e.errorContent);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as MineEditView).onError("请求超时!")
                            }else{
                                (baseView as MineEditView).onError("请求失败")
                            }
                        }
                    }

                    override fun onNext(t: ArrayList<Object>) {
                        super.onNext(t)
                    }
                },lifecylerProvider);
    }
}