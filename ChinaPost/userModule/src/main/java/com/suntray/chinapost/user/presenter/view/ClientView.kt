package com.suntray.chinapost.user.presenter.view

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.enum.ClientAction
import com.suntray.chinapost.user.data.enum.UploadAptitudeEnum
import com.suntray.chinapost.user.data.request.AddClientRequest
import com.suntray.chinapost.user.data.request.FindClientAptitudeRequest
import com.suntray.chinapost.user.data.request.FindClientRequest
import com.suntray.chinapost.user.data.response.FindAptitudeByClientIdResponse
import com.suntray.chinapost.user.data.response.GetAllAptitudeInfoResponse
import com.suntray.chinapost.user.data.response.SaveAptitudeImageResponse
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

/**
 *   客户相关的view
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 22:14
 */
interface ClientView:BaseView{

    /**
     * 增加客户的回调
     */
    fun onAddClientRequest(action:ClientAction){

    }


    /**
     * 查找 客户的回调
     */
    fun onFindClientRequest(mineClient: MineClient){

    }

    /**
     *  通过客户id 查找资质的回调
     */
    fun onFindAptitudeByClientIdRequest(findAptitudeByClientIdResponse:FindAptitudeByClientIdResponse){


    }

    /**
     * 请求省市的数据回调
     */
    fun onProvinceCityRequest(provinceCity: ArrayList<ProvinceCity>, action: CityListAction){

    }

    /**
     * 保存资质图片上传
     */
    fun  onSaveAptitudeImage(saveAptitudeImageResponse: SaveAptitudeImageResponse,uploadAptitudeEnum: UploadAptitudeEnum){

    }

    /**
     * 保存 客户客户的资质的信息
     */
    fun onSaveClientInfo(){

    }

    /**
     *  获取 资质信息的回调
     */
    fun onGetAllApatutdInfos(getAllAptitudeInfoResponse: GetAllAptitudeInfoResponse){

    }
}