package com.suntray.chinapost.baselibrary.data.bean

import com.lidroid.xutils.db.annotation.Id
import com.lidroid.xutils.db.annotation.Table

/**
 *  小区类型实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 10:33
 */
@Table
class DistrictType {
    @Id
    var districtId:Int=0

    var id:Int=0 // 唯一的标识
    var name:String="" //名称
    var pid:Int=0//父类的 id

    constructor(districtId: Int, id: Int, name: String, pid: Int) {
        this.districtId = districtId
        this.id = id
        this.name = name
        this.pid = pid
    }
}