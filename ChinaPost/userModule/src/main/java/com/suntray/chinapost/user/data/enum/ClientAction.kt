package com.suntray.chinapost.user.data.enum

/**
 *  客户操作的 名称
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 16:21
 */
enum class ClientAction(var successMsg:String,var errorMsg:String){
    AddClient("添加客户成功","添加客户失败"),EditClient("修改客户成功","修改客户失败")
}