package com.suntray.chinapost.map.presenter

import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.baselibrary.data.bean.AdStyle
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.baselibrary.data.dao.AdStyleDao
import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.data.enum.CalendarAction
import com.suntray.chinapost.map.data.request.OneKeySubmitRequest
import com.suntray.chinapost.map.data.request.ResourceDateRequest
import com.suntray.chinapost.map.data.response.ClientDictResponse
import com.suntray.chinapost.map.data.response.OneKeyReservedResponse
import com.suntray.chinapost.map.data.response.ResourceDateResponse
import com.suntray.chinapost.map.presenter.view.ResourceView
import com.suntray.chinapost.map.service.impl.DictServiceImpl
import com.suntray.chinapost.map.service.impl.ResourceServiceImpl
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.request.MyClientRequest
import com.suntray.chinapost.user.presenter.MinePresenter
import com.suntray.chinapost.user.presenter.view.MineEditView
import com.suntray.chinapost.user.service.impl.MineServiceImpl
import rx.Observable
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 16:26
 */
class ResourcePresenter @Inject constructor():BasePresenter<ResourceView>(){

    @Inject
    lateinit var dictService: DictServiceImpl

    @Inject
    lateinit var resourceServiceImpl: ResourceServiceImpl

    fun getResourceList(){
        dictService.getResourceList().execute(object:BaseSucriber<ArrayList<ResourceType>>(baseView,ResourcePresenter::javaClass.name){

            override fun onError(e: Throwable?) {
                if(e is ContentException){
                    assertMethod(baseView,{
                        (baseView as ResourceView).onError(e.errorContent)
                    })
                }else{
                    super.onError(e)
                }
            }

            override fun onNext(t: ArrayList<ResourceType>) {
                super.onNext(t)
                (baseView as ResourceView).onGetResourceList(t)
            }
        },lifecylerProvider)
    }

    /**
     *  获取 广告类型列表
     */
     fun getAdTypeList() {
         dictService.getAdTypeList().execute(object : BaseSucriber<ArrayList<AdStyle>>(baseView, ResourcePresenter::javaClass.name) {

             override fun onError(e: Throwable?) {
                 if (e is ContentException) {
                     assertMethod(baseView, {
                         (baseView as ResourceView).onError(e.errorContent)
                     })
                 } else {
                     super.onError(e)
                 }
             }

             override fun onNext(t: ArrayList<AdStyle>) {
                 super.onNext(t)
                 AdStyleDao.deleteAll()
                 AdStyleDao.saveUpDate(t)
                 (baseView as ResourceView).onGetAdStyleList(t)
             }
         }, lifecylerProvider)
     }

    /**
     * 获取 客户名称列表
     */
    fun getClientNameList(){
        dictService.getClientNameList().execute(object : BaseSucriber<ClientDictResponse>(baseView, ResourcePresenter::javaClass.name) {

            override fun onError(e: Throwable?) {
                if (e is ContentException) {
                    assertMethod(baseView, {
                        (baseView as ResourceView).onError(e.errorContent)
                    })
                } else {
                    super.onError(e)
                }
            }

            override fun onNext(t: ClientDictResponse) {
                super.onNext(t)
                (baseView as ResourceView).onGetClientList(t)
            }
        }, lifecylerProvider)
    }


    /***
     *  一键预定的提交  单个 一键预定
     */
    fun oneKeySubmit( id: Array<Int?>? ,//点位id数组
                      resourceid: Array<Int?>?, //资源位类别字典id数组（包含柜屏和柜贴）
                      clientid:Int ,//客户ID
                      adtype:Int=0 ,//广告类型字典id
                      userId:Int=0 ,//当前登录用户ID
                      startdate:String="" ,//预约开始时间（2018/09/09）
                      enddate:String="" //预约开始时间（2018/09/09）'
                      ){
        resourceServiceImpl.oneKeySubmit(OneKeySubmitRequest(id,resourceid,clientid,adtype,userId,startdate,enddate))
                .execute(object : BaseSucriber<OneKeyReservedResponse>(baseView, ResourcePresenter::javaClass.name) {
                        override fun onError(e: Throwable?) {
                            if (e is ContentException) {
                                assertMethod(baseView, {
                                    (baseView as ResourceView).onError(e.errorContent)
                                    (baseView as ResourceView).hideLoading()
                                })
                            } else {
                                super.onError(e)
                            }
                        }

                        override fun onNext(t: OneKeyReservedResponse) {
                            super.onNext(t)
                            (baseView as ResourceView).onOneKeyReservedSubmit(t,resourceid!!)
                            (baseView as ResourceView).hideLoading()
                        }
        }, lifecylerProvider)
    }


    /**
     * 提交保存
     */
    fun submitReserve(id: Array<Int?>? ,//点位id数组
                      resourceid: Array<Int?>?, //资源位类别字典id数组（包含柜屏和柜贴）
                      clientid:Int ,//客户ID
                      adtype:Int=0 ,//广告类型字典id
                      userId:Int=0 ,//当前登录用户ID
                      startdate:String="" ,//预约开始时间（2018/09/09）
                      enddate:String="" //预约开始时间（2018/09/09)
    ){
                  resourceServiceImpl.submitReserve(OneKeySubmitRequest(id,resourceid,clientid,adtype,userId,startdate,enddate))
                  .execute(object : BaseSucriber<ArrayList<Object>>(baseView, ResourcePresenter::javaClass.name) {
                      override fun onError(e: Throwable?) {
                          if (e is ContentException) {
                              assertMethod(baseView, {
                                  (baseView as ResourceView).onError(e.errorContent)
                                  (baseView as ResourceView).hideLoading()
                              })
                          } else {
                              super.onError(e)
                          }
                      }

                      override fun onNext(t: ArrayList<Object>) {
                          super.onNext(t)
                          (baseView as ResourceView).onSaveReservedSubmit()
                          (baseView as ResourceView).hideLoading()
                      }
                  }, lifecylerProvider)
    }


    fun oneKeySubmitResult( id: Array<Int?>? ,//点位id数组
                            resourceid: Array<Int?>?, //资源位类别字典id数组（包含柜屏和柜贴）
                            clientid:Int ,//客户ID
                            adtype:Int=0 ,//广告类型字典id
                            userId:Int=0 ,//当前登录用户ID
                            startdate:String="" ,//预约开始时间（2018/09/09）
                            enddate:String="" //预约开始时间（2018/09/09）'
     ){
        resourceServiceImpl.oneKeySubmitResult(OneKeySubmitRequest(id,resourceid,clientid,adtype,userId,startdate,enddate))
                .execute(object : BaseSucriber<OneKeyReservedResponse>(baseView, ResourcePresenter::javaClass.name) {
                    override fun onError(e: Throwable?) {
                        if (e is ContentException) {
                            assertMethod(baseView, {
                                (baseView as ResourceView).onError(e.errorContent)
                                (baseView as ResourceView).hideLoading()
                            })
                        } else {
                            super.onError(e)
                        }
                    }

                    override fun onNext(t: OneKeyReservedResponse) {
                        super.onNext(t)
                        (baseView as ResourceView).onGetReservedResult(t,resourceid!!)
                        (baseView as ResourceView).hideLoading()
                    }
                }, lifecylerProvider)
    }


    @Inject
    lateinit var mineService: MineServiceImpl

    /**
     * 我的客户查询..
     */
    fun myClient(salesman:Int,name:String,page:Int,rows:Int,action: RefreshAction){
        mineService.myClient(MyClientRequest(salesman, name, page, rows,-1,2)).
                execute(object:BaseSucriber<ArrayList<MineClient>>(baseView, ResourcePresenter::javaClass.name){
                    override fun onError(e: Throwable?) {
                        SystemUtil.printlnStr(" myClient  onError:")
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as ResourceView).onError(e.errorContent,action);
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as ResourceView).onError("请求超时!",action)
                            }else{
                                (baseView as ResourceView).onError("请求失败",action)
                            }
                        }
                    }

                    override fun onStart() {
                        SystemUtil.printlnStr(" myClient  onStart:")
                    }

                    override fun onNext(t: ArrayList<MineClient>) {
                        SystemUtil.printlnStr(" myClient  onNext:")
                        assertMethod(baseView,{
                            (baseView as ResourceView).onMineClient(t,action)
                        })
                    }
                },lifecylerProvider);
    }


    /**
     * 获取 时间的周期
     */
    fun getResourceDateSchedule(resourceDateRequest: ResourceDateRequest,calendarAction: CalendarAction){
        resourceServiceImpl.getResourceDateSchedule(resourceDateRequest)
                .execute(object : BaseSucriber<ResourceDateResponse>(baseView, ResourcePresenter::javaClass.name) {
                    override fun onError(e: Throwable?) {
                        if (e is ContentException) {
                            assertMethod(baseView, {
                                (baseView as ResourceView).onError(e.errorContent)
                                (baseView as ResourceView).hideLoading()
                            })
                        } else {
                            super.onError(e)
                        }
                    }


                    override fun onStart() {
                    }
                    override fun onNext(t: ResourceDateResponse) {
                        super.onNext(t)
                        (baseView as ResourceView).onGetResourceDate(t,calendarAction)
                        (baseView as ResourceView).hideLoading()
                    }
                }, lifecylerProvider)
    }
}