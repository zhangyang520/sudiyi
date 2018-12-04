package com.suntray.chinapost.baselibrary.data.bean

import com.lidroid.xutils.db.annotation.Id

/**
 *  广告类型实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 13:23
 */
class AdStyle {
    @Id
    var  adId:Int=0 //广告类型的主键
    var  id:Int=0 //类型的id
    var pid:Int=0 //父id
    var  name:String="" //类型的名称
    var  adValue:Int=-1  //广告类型对应的值
    override fun toString(): String {
        return "AdStyle(id=$id, pid=$pid, name='$name', adValue=$adValue)"
    }
}