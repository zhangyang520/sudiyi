package com.suntray.chinapost.user.data.request


/**
 *  添加 客户的请求实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 20:47
 */
data class AddClientRequest(var id:Int, // 客户主键id 修改时必填，新增时为空
                              var name:String, //客户名称
                              var profession:Int, //客户行业
                              var origin:Int, //客户来源
                               var linkmanname:String, //联系人姓名
                               var linkmanphone:String,//联系人手机
                               var stage:Int, //客户阶段
                               var salesman:Int,//归属销售id(当前登录用户id)
                                var province:Int,//客户所在省编码
                                var city:Int,//客户所在市编码
                                var address:String,//客户详细地址
                                var nature:Int,//客户性质
                                var district:Int,//区域编码
                                var state:Int,//账号状态
                                var  brand:String//客户品牌
)