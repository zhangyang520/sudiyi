package com.suntray.chinapost.user.service.impl

import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.request.*
import com.suntray.chinapost.user.data.response.FindAptitudeByClientIdResponse
import com.suntray.chinapost.user.data.response.GetAllAptitudeInfoResponse
import com.suntray.chinapost.user.data.response.SaveAptitudeImageResponse
import com.suntray.chinapost.user.data.respository.ClientRespository
import com.suntray.chinapost.user.service.ClientService
import okhttp3.MultipartBody
import rx.Observable
import javax.inject.Inject

/**
 *   客户相关的服务
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 22:12
 */
class ClientServiceImpl @Inject constructor():ClientService{

    @Inject
    lateinit var clientRespository: ClientRespository

    override fun addClient(addClientRequest: AddClientRequest): Observable<MutableList<Object>> {
       return  clientRespository.addClient(addClientRequest).convertData()
    }

    override fun findClient(findClientRequest: FindClientRequest): Observable<MineClient> {
        return  clientRespository.findClient(findClientRequest).convertData()
    }

    override fun findAptitudeByClientId(findClientAptitudeRequest: FindClientAptitudeRequest): Observable<FindAptitudeByClientIdResponse> {
        return  clientRespository.findAptitudeByClientId(findClientAptitudeRequest).convertData()
    }

    override fun province(provinceRequest: ProvinceRequest): Observable<ArrayList<ProvinceCity>> {
        return clientRespository.province(provinceRequest).convertData()
    }

    override fun saveAptitudeImage(saveAptitudeImgRequest: SaveAptitudeImgRequest): Observable<SaveAptitudeImageResponse> {
        return clientRespository.saveAptitudeImage(saveAptitudeImgRequest).convertData()
    }

    override fun saveClientInfo(saveClientInfoRequest: SaveClientInfoRequest): Observable<ArrayList<Object>> {
        return clientRespository.saveClientInfo(saveClientInfoRequest).convertData()
    }

    override fun uploadAllAptitudeImage(uploadAllAptitudeImageRequest: UploadAllAptitudeImageRequest): Observable<ArrayList<Object>> {
        return clientRespository.uploadAllAptitudeImage(uploadAllAptitudeImageRequest).convertData()
    }


    override fun getAllUploadAptitude(getAllUploadAptitudeRequest: GetAllUploadAptitudeRequest): Observable<GetAllAptitudeInfoResponse> {
        return clientRespository.getAllUploadAptitude(getAllUploadAptitudeRequest).convertData()
    }
}