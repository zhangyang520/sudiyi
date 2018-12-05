package com.suntray.chinapost.user.data.bean

/**
 *  营业执照的实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 21:12
 */
class LicenseAcc {
    var id:Int=0// id
    var address:String="" //上传照片地址
    var accessoryid:String="" //标准的id
    var name:String=""//图片的名称

    override fun toString(): String {
        return "LicenseAcc(id=$id, address='$address', accessoryid='$accessoryid', name='$name')"
    }
}