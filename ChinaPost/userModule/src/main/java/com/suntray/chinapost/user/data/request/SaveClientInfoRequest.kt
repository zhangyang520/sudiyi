package com.suntray.chinapost.user.data.request

/**
 *   保存客户信息的封装体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/23 17:26
 */
data class SaveClientInfoRequest(var cid:Int,//客户的id
                                 var license:String,//营业执照复印件(附件ID)
                                 var health:String ,//卫生许可证明(附件ID)
                                 var portrait:String,//肖像授权书(附件ID)
                                 var trademark:String,//商标注册证附件ID)
                                 var url:String,//网址域名注册证(附件ID)
                                 var qrcode:String,//二维码说明(附件ID)
                                 var clientdisclaimer:String,//客户免责说明(附件ID)
                                 var expressdisclaimer:String,//速递易免责说明(附件ID)
                                 var landsale:String,//地产销售许可证(附件ID)
                                 var other:String//其他(附件ID)
)