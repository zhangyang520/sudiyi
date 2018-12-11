package com.suntray.chinapost.user.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.request.*
import com.suntray.chinapost.user.data.response.FindAptitudeByClientIdResponse
import com.suntray.chinapost.user.data.response.GetAllAptitudeInfoResponse
import com.suntray.chinapost.user.data.response.SaveAptitudeImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

/**
 *   客户相关接口的api
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 20:47
 *
 */
interface ClientApi {

    /**
     * 增加 客户
     */
    @POST(value = BaseConstants.ADD_CLIENT)
    fun addClient(@Body addClientRequest: AddClientRequest):Observable<BaseResp<MutableList<Object>>>


    /**
     * 查找对应的客户
     */
    @POST(value = BaseConstants.FIND_CLIENT)
    fun findClient(@Body findClientRequest: FindClientRequest):Observable<BaseResp<MineClient>>


    /**
     *  通过 客户id查找资质
     */
    @POST(value = BaseConstants.FIND_APTITUDE)
    fun findAptitudeByClientId(@Body findClientAptitudeRequest: FindClientAptitudeRequest)
                                                :Observable<BaseResp<FindAptitudeByClientIdResponse>>

    /**
     * 省市区 接口
     */
    @POST(value = BaseConstants.PROVINCE)
    fun province(@Body province: ProvinceRequest):Observable<BaseResp<ArrayList<ProvinceCity>>>


    /**
     * 保存 资质图片
     */
    @Multipart
    @POST(value = BaseConstants.SAVE_APTITUDE_IMG)
    fun saveAptitudeImage(@Query(value = "userId") userId:Int, @Query(value = "pathId") pathId:String,
                          @Part imgFile: MultipartBody.Part,@Part("description") descritpion: RequestBody):Observable<BaseResp<SaveAptitudeImageResponse>>

    /**
     *  保存客户信息
     */
    @POST(value = BaseConstants.SAVE_CLINET_INFO)
    fun saveClientInfo(@Body saveClientInfoRequest: SaveClientInfoRequest):Observable<BaseResp<ArrayList<Object>>>


    /**
     * 上传 所有的资质图片的接口
     *
     */
    @Multipart
    @POST(value = BaseConstants.UPLOAD_ALL_APTITUDE_IMAGE)
    fun uploadAllAptitudeImage(@Part baseFiles: List<MultipartBody.Part>,@Query(value = "cid")cid:Int=-1,
                               @Part specialFiles: List<MultipartBody.Part>,
                               @Part("description") descritpion: RequestBody,
                               @Query(value = "id")id:Int=-1,
                               @Query(value = "deleteFileIds")deleteFileIds:Array<Int?>):Observable<BaseResp<ArrayList<Object>>>


    /**
     *  获取 该用户的上传资质信息
     */
    @POST(value = BaseConstants.GET_ALL_UPLOAD_APTITUDE)
    fun getAllUploadAptitude(@Body getAllUploadAptitudeRequest:GetAllUploadAptitudeRequest):Observable<BaseResp<GetAllAptitudeInfoResponse>>
}