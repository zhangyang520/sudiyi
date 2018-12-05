package com.suntray.chinapost.user.data.response

import com.suntray.chinapost.user.data.bean.LicenseAcc

/**
 *  通过客户的id 查找 资质的响应体的封装
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 21:17
 */
class FindAptitudeByClientIdResponse {
    var id:Int=0//
    var cid:Int=0//
    var license:String=""//
    var licenseAccList:MutableList<LicenseAcc>?=null
    var health:String=""//
    var healthAccList:MutableList<LicenseAcc>?=null
    var trademark:String=""//
    var trademarkAccList:MutableList<LicenseAcc>?=null
    var portrait:String=""
    var portraitAccList:MutableList<LicenseAcc>?=null
    var url:String=""
    var urlAccList:MutableList<LicenseAcc>?=null
    var qrcode:String=""
    var qrcodeAccList:MutableList<LicenseAcc>?=null
    var clientdisclaimer:String=""
    var clientdisclaimereAccList:MutableList<LicenseAcc>?=null
    var expressdisclaimer:String=""
    var expressdisclaimerAccList:MutableList<LicenseAcc>?=null
    var landsale:String=""
    var landsaleAccList:MutableList<LicenseAcc>?=null
    var other:String=""
    var otherAccList:MutableList<LicenseAcc>?=null

}