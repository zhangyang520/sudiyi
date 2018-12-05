package com.suntray.chinapost.map.data.response

import com.suntray.chinapost.baselibrary.data.bean.ClientDict
import java.io.Serializable

/**
 *   客户字典 请求结果封装类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 16:55
 */
class ClientDictResponse:Serializable{
    var professionList:ArrayList<ClientDict>?=null
    var stageList:ArrayList<ClientDict>?=null
    var originList:ArrayList<ClientDict>?=null
    var natureList:ArrayList<ClientDict>?=null


    override fun toString(): String {
        return "ClientDictResponse(professionList=$professionList, stageList=$stageList, originList=$originList, natureList=$natureList)"
    }
}