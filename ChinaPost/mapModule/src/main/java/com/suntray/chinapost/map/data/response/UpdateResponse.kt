package com.suntray.chinapost.map.data.response

import com.suntray.chinapost.baselibrary.exception.ContentException

/**
 *  版本更新的响应实体的封装
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/8 16:24
 */
class UpdateResponse {
    var version:String=""
    var path:String=""

    @Throws(ContentException::class)
    fun getFloatVersion():Float{
        try {
            return version.toFloat();
        } catch (e: Exception) {
            throw ContentException()
        }
    }
}