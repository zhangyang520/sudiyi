package com.suntray.chinapost.user.data.response

import com.suntray.chinapost.user.data.bean.AptitudeInfo

/**
 *   获取所有的 资质信息 返回信息的封装体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/26 16:24
 */
class GetAllAptitudeInfoResponse {
    var id=-1 //主键的id
    var cid=0 //客户的id

    var license="" //营业执照复印件—附件UUID
    var licenseAccList:ArrayList<AptitudeInfo?>?=null //营业执照复印件—附件列表
    var health="" //卫生许可证明—附件UUID
    var healthAccList:ArrayList<AptitudeInfo?>?=null  //卫生许可证明—附件列表
    var trademark="" //商标注册证明—附件UUID
    var trademarkAccList:ArrayList<AptitudeInfo?>?=null //商标注册证明—附件列表
    var portrait="" //肖像授权书—附件UUID
    var portraitAccList:ArrayList<AptitudeInfo?>?=null //肖像授权书—附件列表
    var url="" //网址域名注册证—附件UUID
    var urlAccList:ArrayList<AptitudeInfo?>?=null  //网址域名注册证—附件列表
    var qrcode="" //二维码说明—附件UUID
    var qrcodeAccList:ArrayList<AptitudeInfo?>?=null  //二维码说明—附件列表
    var clientdisclaimer="" //客户免责说明—附件UUID
    var clientdisclaimereAccList:ArrayList<AptitudeInfo?>?=null //客户免责说明—附件列表
    var expressdisclaimer=""  //速递易免责说明—附件UUID
    var expressdisclaimerAccList:ArrayList<AptitudeInfo?>?=null //速递易免责说明—附件列表
    var landsale="" //地产销售许可证明—附件UUID
    var landsaleAccList:ArrayList<AptitudeInfo?>?=null //地产销售许可证明—附件列表
    var other="" //其他—附件UUID
    var otherAccList:ArrayList<AptitudeInfo?>?=null //其他—附件列表



    override fun toString(): String {
        return "GetAllAptitudeInfoResponse(id=$id, cid=$cid, license='$license', licenseAccList=$licenseAccList, health='$health', healthAccList=$healthAccList, trademark='$trademark', trademarkAccList=$trademarkAccList, portrait='$portrait', portraitAccList=$portraitAccList, url='$url', urlAccList=$urlAccList, qrcode='$qrcode', qrcodeAccList=$qrcodeAccList, clientdisclaimer='$clientdisclaimer', clientdisclaimereAccList=$clientdisclaimereAccList, expressdisclaimer='$expressdisclaimer', expressdisclaimerAccList=$expressdisclaimerAccList, landsale='$landsale', landsaleAccList=$landsaleAccList, other='$other', otherAccList=$otherAccList)"
    }
}