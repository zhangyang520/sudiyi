package com.suntray.chinapost.utils.inner

import com.suntray.chinapost.data.bean.UserRole

/**
 *   角色条目的点击事件
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/22 18:05
 */
interface RoleItemClickListener {
    fun onItemClick(userRole:UserRole)
}