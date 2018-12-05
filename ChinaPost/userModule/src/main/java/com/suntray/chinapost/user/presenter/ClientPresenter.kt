package com.suntray.chinapost.user.presenter

import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.enum.ClientAction
import com.suntray.chinapost.user.data.enum.UploadAptitudeEnum
import com.suntray.chinapost.user.data.request.*
import com.suntray.chinapost.user.data.response.FindAptitudeByClientIdResponse
import com.suntray.chinapost.user.data.response.GetAllAptitudeInfoResponse
import com.suntray.chinapost.user.data.response.SaveAptitudeImageResponse
import com.suntray.chinapost.user.presenter.view.ClientView
import com.suntray.chinapost.user.presenter.view.MineEditView
import com.suntray.chinapost.user.service.ClientService
import com.suntray.chinapost.user.service.impl.ClientServiceImpl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import rx.Observable
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 *   客户的相关的presenter
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 22:13
 */
class ClientPresenter @Inject constructor():BasePresenter<ClientView>(){

    @Inject
    lateinit var clientService: ClientServiceImpl


    /**
     * 增加 客户的请求
     */
    fun addClient(addClientRequest: AddClientRequest,action:ClientAction){
         clientService.addClient(addClientRequest).
                 execute(object: BaseSucriber<MutableList<Object>>(baseView,ClientPresenter::javaClass.name){
                     override fun onError(e: Throwable?) {
                         if(e is ContentException){
                             assertMethod(baseView,{
                                 (baseView as ClientView).onError(e.errorContent);
                                 baseView.hideLoading()
                             })
                         }else{
                             if(e is SocketTimeoutException){
                                 (baseView as ClientView).onError("请求超时!")
                                 baseView.hideLoading()
                             }else{
                                 (baseView as ClientView).onError("请求失败")
                                 baseView.hideLoading()
                             }
                         }
                     }

                     override fun onNext(t:MutableList<Object>) {
                         super.onNext(t)
                         assertMethod(baseView,{
                             baseView.hideLoading()
                             (baseView as ClientView).onAddClientRequest(action)
                         })
                     }
                 },lifecylerProvider);
    }

    /**
     *  查找客户的请求
     */
     fun findClient(findClientRequest: FindClientRequest){
         clientService.findClient(findClientRequest).
                 execute(object: BaseSucriber<MineClient>(baseView,ClientPresenter::javaClass.name){
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

                     override fun onNext(t:MineClient) {
                         super.onNext(t)
                         assertMethod(baseView,{
                             (baseView as ClientView).onFindClientRequest(t)
                         })
                     }
                 },lifecylerProvider);
    }

    /**
     * 查找 对应客户的资质
     */
    fun findAptitudeByClientId(findClientAptitudeRequest: FindClientAptitudeRequest){
        clientService.findAptitudeByClientId(findClientAptitudeRequest).
                execute(object: BaseSucriber<FindAptitudeByClientIdResponse>(baseView,ClientPresenter::javaClass.name){
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

                    override fun onNext(t:FindAptitudeByClientIdResponse) {
                        super.onNext(t)
                        assertMethod(baseView,{
                            (baseView as ClientView).onFindAptitudeByClientIdRequest(t)
                        })
                    }
                },lifecylerProvider);
    }

    /**
     *  省市区的请求
     */
    fun province(type:String,provinceId:Int,cityId:Int,action: CityListAction){
        clientService.province(ProvinceRequest(type,provinceId,cityId)).
                execute(object:BaseSucriber<ArrayList<ProvinceCity>>(baseView,ClientPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView).onError(e.errorContent)
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onStart() {

                    }
                    override fun onNext(t: ArrayList<ProvinceCity>) {
                        super.onNext(t)
                        (baseView as ClientView).onProvinceCityRequest(t,action)
                    }
                },lifecylerProvider)

    }


    /**
     * 保存 资质图片
     */
    fun saveAptitudeImage(saveAptitudeImgRequest: SaveAptitudeImgRequest,uploadAptitudeEnum: UploadAptitudeEnum,imgFile:MultipartBody.Part){
        clientService.saveAptitudeImage(saveAptitudeImgRequest).
                execute(object:BaseSucriber<SaveAptitudeImageResponse>(baseView,ClientPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView).onError(e.errorContent)
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onNext(t: SaveAptitudeImageResponse) {
                        super.onNext(t)
                        (baseView as ClientView).onSaveAptitudeImage(t,uploadAptitudeEnum)
                    }
                },lifecylerProvider)
    }

    /**
     *  保存客户信息
     */
    fun saveClientInfo(saveClientInfoRequest: SaveClientInfoRequest){
        clientService.saveClientInfo(saveClientInfoRequest).
                execute(object:BaseSucriber<ArrayList<Object>>(baseView,ClientPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView).onError(e.errorContent)
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onNext(t: ArrayList<Object>) {
                        super.onNext(t)
                        (baseView as ClientView).onSaveClientInfo()
                    }
                },lifecylerProvider)
    }


    /**
     * 上传 所有的信息
     */
    fun uploadAllAptitudeImage(uploadAllAptitudeImageRequest: UploadAllAptitudeImageRequest){
        clientService.uploadAllAptitudeImage(uploadAllAptitudeImageRequest).
                execute(object:BaseSucriber<ArrayList<Object>>(baseView,ClientPresenter::javaClass.name){

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

                    override fun onNext(t: ArrayList<Object>) {
                        super.onNext(t)
                        (baseView as ClientView).onSaveClientInfo()
                        (baseView).hideLoading()
                    }
                },lifecylerProvider)
    }


    fun getAllUploadAptitude(getAllUploadAptitudeRequest: GetAllUploadAptitudeRequest){
        clientService.getAllUploadAptitude(getAllUploadAptitudeRequest).
                execute(object:BaseSucriber<GetAllAptitudeInfoResponse>(baseView,ClientPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView).onError(e.errorContent,"获取资质信息失败")
                                baseView.hideLoading()
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onNext(t: GetAllAptitudeInfoResponse) {
                        super.onNext(t)
                        baseView.hideLoading()
                        (baseView as ClientView).onGetAllApatutdInfos(t)
                    }
                },lifecylerProvider)
    }
}