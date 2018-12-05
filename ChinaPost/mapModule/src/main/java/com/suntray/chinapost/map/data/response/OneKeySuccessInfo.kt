package com.suntray.chinapost.map.data.response

import com.suntray.chinapost.map.data.bean.MapDot
import java.io.Serializable

/**
 *   一键预定 成功或者失败的信息
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 19:04
 */
class OneKeySuccessInfo:Serializable{
    var point:MapDot?=null
    var success:Boolean=false
    var error:String=""
    override fun toString(): String {
        return "OneKeySuccessInfo(point=$point, success=$success, error='$error')"
    }
}