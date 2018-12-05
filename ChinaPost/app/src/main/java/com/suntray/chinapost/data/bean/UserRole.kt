package com.suntray.chinapost.data.bean

/**
 *  用户的角色
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/22 16:37
 */
class UserRole {


    var roleName=""
    var roleId=-1

    constructor(roleName: String, roleId: Int) {
        this.roleName = roleName
        this.roleId = roleId
    }

    override fun toString(): String {
        return "UserRole(roleName='$roleName', roleId=$roleId)"
    }
}