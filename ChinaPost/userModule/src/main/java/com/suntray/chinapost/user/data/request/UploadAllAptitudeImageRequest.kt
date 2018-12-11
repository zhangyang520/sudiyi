package com.suntray.chinapost.user.data.request

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

/**
 * 上传所有的图片形式
 */
data class UploadAllAptitudeImageRequest(var baseFiles: List<MultipartBody.Part>,
                                          var cid:Int=-1,
                                         var specialFiles: List<MultipartBody.Part>,
                                         var descritpion: RequestBody,
                                         var id:Int,
                                         var deleteFileIds:Array<Int?>
                                         )