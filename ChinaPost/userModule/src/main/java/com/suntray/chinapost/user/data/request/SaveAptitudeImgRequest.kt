package com.suntray.chinapost.user.data.request

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

/**
 *  保存 资质图片的请求封装
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/23 17:23
 */
data class SaveAptitudeImgRequest(var userId:Int,var pathId:String=" ",var imgFile: MultipartBody.Part,var  descritpion:RequestBody)