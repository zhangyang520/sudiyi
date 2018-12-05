package com.suntray.chinapost.user.service

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
import rx.Observable

/**
 *   客户仓库的相关业务
 *  @Author 张扬 @version 1.0
 *  @Date ] 22:09
 */
interface ClientService {
    /**
     * 增加 客户
     */
    fun addClient(addClientRequest: AddClientRequest): Observable<MutableList<Object>>

    /**
     * 查找对应的客户
     */
    fun findClient(@Body findClientRequest: FindClientRequest): Observable<MineClient>


    /**
     *  通过 客户id查找资质
     */
    @POST(value = BaseConstants.FIND_APTITUDE)
    fun findAptitudeByClientId(@Body findClientAptitudeRequest: FindClientAptitudeRequest):Observable<FindAptitudeByClientIdResponse>


    /**
     * 省市的请求
     */
    fun province(provinceRequest: ProvinceRequest): Observable<ArrayList<ProvinceCity>>;



    /**
     * 保存 资质图片
     */
    fun saveAptitudeImage(saveAptitudeImgRequest: SaveAptitudeImgRequest):Observable<SaveAptitudeImageResponse>

    /**
     *  保存客户信息
     */
    fun saveClientInfo(saveClientInfoRequest: SaveClientInfoRequest):Observable<ArrayList<Object>>


    /**
     * 上传所有的资质图片接口
     */
    fun uploadAllAptitudeImage(uploadAllAptitudeImageRequest: UploadAllAptitudeImageRequest):Observable<ArrayList<Object>>


    /**
     * 获取所有的 资质信息
     */
    fun getAllUploadAptitude(getAllUploadAptitudeRequest:GetAllUploadAptitudeRequest):Observable<GetAllAptitudeInfoResponse>
}