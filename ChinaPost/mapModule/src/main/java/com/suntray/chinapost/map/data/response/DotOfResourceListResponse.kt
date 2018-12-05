package com.suntray.chinapost.map.data.response

import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.map.data.bean.ResourceAd

/**
 *  查看 对应点位的 广告资源位
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 12:33
 */
class DotOfResourceListResponse {
    var point:MapDot?=null
    var resourceList:ArrayList<ResourceAd>?=null
}