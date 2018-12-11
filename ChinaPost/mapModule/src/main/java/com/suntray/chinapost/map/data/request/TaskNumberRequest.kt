package com.suntray.chinapost.map.data.request

/**
 *  任务数量 的请求封装体
 *
 *  type	    是	int	上下刊任务类型，1上刊，2下刊
    supplyID	是	int	供应商id（登录用户所属机构id，即orgId）

 *  @Title ${name}
 *  @ProjectName ChinaPost
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/1020:10
 *
 */
data class TaskNumberRequest(var type:Int=0,var supplyID:Int=-1)