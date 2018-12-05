package com.suntray.chinapost.baselibrary.data.bean

import com.lidroid.xutils.db.annotation.Id
import com.lidroid.xutils.db.annotation.Table

/**
 *   资源位
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 15:54
 */
@Table
class ResourceType{
    @Id
    var resourceId:Int=0
    var id:Int=0
    var name:String=""
    override fun toString(): String {
        return "ResourceType(id=$id, name='$name')"
    }
}