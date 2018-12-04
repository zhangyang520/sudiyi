package com.suntray.chinapost.user.data.request

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

/**
 * 上传所有的图片形式
 */
data class UploadAllAptitudeImageRequest(var licenseFiles: List<MultipartBody.Part>,
                                          var cid:Int=-1,
                                         var healthFiles: List<MultipartBody.Part>,
                                         var portraitFiles: List<MultipartBody.Part>,
                                         var trademarkFiles: List<MultipartBody.Part>,
                                         var urlFiles: List<MultipartBody.Part>,
                                         var qrcodeFiles: List<MultipartBody.Part>,
                                         var clientdisclaimerFiles: List<MultipartBody.Part>,
                                         var expressdisclaimerFiles: List<MultipartBody.Part>,
                                         var landsaleFiles: List<MultipartBody.Part>,
                                         var otherFiles: List<MultipartBody.Part>,
                                         var descritpion: RequestBody,
                                         var id:Int,
                                         var deleteFileIds:Array<Int?>
                                         )