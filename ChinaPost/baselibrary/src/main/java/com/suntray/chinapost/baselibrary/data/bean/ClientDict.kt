package com.suntray.chinapost.baselibrary.data.bean

import com.lidroid.xutils.db.annotation.Id
import com.lidroid.xutils.db.annotation.Table
import java.io.Serializable

/**
 *   客户实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 16:15
 */
@Table
class ClientDict:Serializable{
    @Id
    var clientId=0
    var id:Int=0 //客户的id
    var name:String=""//客户名称
    var pid:Int=0//父id
    override fun toString(): String {
        return "ClientDict(id=$id, name='$name', pid=$pid)"
    }
}