package com.suntray.chinapost.user.data.response

/**
 *  保存资质的响应 实体封装
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/23 17:36
 */
class SaveAptitudeImageResponse {
    var pathId=""  //图片保存的附件ID(可用于当前类别资质再次上传图片是作为参数带入)
    var path="" //图片保存的路径


    override fun toString(): String {
        return "SaveAptitudeImageResponse(pathId='$pathId', path='$path')"
    }
}