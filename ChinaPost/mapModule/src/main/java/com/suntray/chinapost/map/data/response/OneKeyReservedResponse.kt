package com.suntray.chinapost.map.data.response

import java.io.Serializable

/**
 *  一键预定返回结果 实体封装
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 19:01
 */
class OneKeyReservedResponse:Serializable{
    var countFailure:Int=0
    var countSuccess:Int=0
    var success:Boolean=false
    var data:MutableList<OneKeySuccessInfo>?=null


    override fun toString(): String {
        return "OneKeyReservedResponse(countFailure=$countFailure, countSuccess=$countSuccess, success=$success, dataList=$data)"
    }
}