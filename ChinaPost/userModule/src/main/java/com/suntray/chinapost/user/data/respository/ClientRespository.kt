package com.suntray.chinapost.user.data.respository

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.user.data.api.ClientApi
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.request.*
import com.suntray.chinapost.user.data.response.FindAptitudeByClientIdResponse
import com.suntray.chinapost.user.data.response.GetAllAptitudeInfoResponse
import com.suntray.chinapost.user.data.response.SaveAptitudeImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part
import rx.Observable
import javax.inject.Inject


/**
 *   客户相关请求的网络仓库
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 22:07
 */
class ClientRespository @Inject constructor(){

    /**
     * 增加 客户
     */
    fun addClient(@Body addClientRequest: AddClientRequest): Observable<BaseResp<MutableList<Object>>>{
        return RetrofitFactory.instance.create(ClientApi::class.java).addClient(addClientRequest)
    }


    /**
     * 查找对应的客户
     */
    @POST(value = BaseConstants.FIND_CLIENT)
    fun findClient(@Body findClientRequest: FindClientRequest): Observable<BaseResp<MineClient>>{
        return RetrofitFactory.instance.create(ClientApi::class.java).findClient(findClientRequest)
    }


    /**
     *  通过 客户id查找资质
     */
    @POST(value = BaseConstants.FIND_APTITUDE)
    fun findAptitudeByClientId(@Body findClientAptitudeRequest: FindClientAptitudeRequest)
            : Observable<BaseResp<FindAptitudeByClientIdResponse>>{
        return RetrofitFactory.instance.create(ClientApi::class.java).findAptitudeByClientId(findClientAptitudeRequest)
    }

    /**
     * 省市的请求
     */
    fun province(provinceRequest: ProvinceRequest):Observable<BaseResp<ArrayList<ProvinceCity>>>{
        return RetrofitFactory.instance.create(ClientApi::class.java).province(provinceRequest)
    }


    /**
     * 保存 资质图片
     */
    fun saveAptitudeImage(saveAptitudeImgRequest: SaveAptitudeImgRequest):Observable<BaseResp<SaveAptitudeImageResponse>>{
        return RetrofitFactory.instance.create(ClientApi::class.java).
                saveAptitudeImage(saveAptitudeImgRequest.userId,saveAptitudeImgRequest.pathId,saveAptitudeImgRequest.imgFile,saveAptitudeImgRequest.descritpion)
    }

    /**
     *  保存客户信息
     */
    fun saveClientInfo(saveClientInfoRequest: SaveClientInfoRequest):Observable<BaseResp<ArrayList<Object>>>{
        return RetrofitFactory.instance.create(ClientApi::class.java).saveClientInfo(saveClientInfoRequest)
    }


    /**
     * 上传 所有的资质图片的信息
     */
    fun uploadAllAptitudeImage(uploadAllAptitudeImageRequest: UploadAllAptitudeImageRequest):Observable<BaseResp<ArrayList<Object>>>{
        return RetrofitFactory.instance.create(ClientApi::class.java)
                .uploadAllAptitudeImage(uploadAllAptitudeImageRequest.baseFiles,
                        uploadAllAptitudeImageRequest.cid,
                        uploadAllAptitudeImageRequest.specialFiles,
                        uploadAllAptitudeImageRequest.descritpion,
                        uploadAllAptitudeImageRequest.id,
                        uploadAllAptitudeImageRequest.deleteFileIds
                )
    }


    /**
     * 获取 对应客户的 资质信息
     */
    fun getAllUploadAptitude(getAllUploadAptitudeRequest:GetAllUploadAptitudeRequest):Observable<BaseResp<GetAllAptitudeInfoResponse>>{
        return RetrofitFactory.instance.create(ClientApi::class.java).getAllUploadAptitude(getAllUploadAptitudeRequest)
    }
}