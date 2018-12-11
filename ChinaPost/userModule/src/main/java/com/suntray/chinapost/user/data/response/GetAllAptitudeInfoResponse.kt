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

    var base="" //基本复印件—附件UUID
    var baseAccList:ArrayList<AptitudeInfo?>?=null //营业执照复印件—附件列表
    var special="" //行业特殊—附件UUID
    var specialAccList:ArrayList<AptitudeInfo?>?=null  //行业特殊证明—附件列表

    constructor()

    override fun toString(): String {
        return "GetAllAptitudeInfoResponse(id=$id, cid=$cid, base='$base', baseAccList=$baseAccList, special='$special', specialAccList=$specialAccList)"
    }

}