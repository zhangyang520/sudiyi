package com.suntray.chinapost.map.presenter.view

import com.suntray.chinapost.baselibrary.presenter.view.BaseView
import com.suntray.chinapost.baselibrary.data.bean.AdStyle
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.map.data.enum.CalendarAction
import com.suntray.chinapost.map.data.response.AdDownResponse
import com.suntray.chinapost.map.data.response.ClientDictResponse
import com.suntray.chinapost.map.data.response.OneKeyReservedResponse
import com.suntray.chinapost.map.data.response.ResourceDateResponse
import com.suntray.chinapost.user.data.bean.MineClient

/**
 *  字典相关的view
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 16:26
 */
interface ResourceView:BaseView{

    /**
     * 获取 资源位列表信息
     */
    fun onGetResourceList(resourceList:ArrayList<ResourceType>){

    }

    fun onGetAdDownReportList(adDownResponse: AdDownResponse){

    }
    /**
     * 获取 广告类型列表数据回调
     */
    fun onGetAdStyleList(adStyleList:ArrayList<AdStyle>){

    }

    /**
     * 获取 客户的列表
     */
    fun onGetClientList(clientList: ClientDictResponse){

    }

    /**
     * 提交一键预定的请求
     */
    fun onOneKeyReservedSubmit(oneKeyReservedResponse: OneKeyReservedResponse,resourceTypeIdArray:Array<Int?>){


    }

    /**
     * 提交保存
     */
    fun onSaveReservedSubmit(){

    }


    /**
     * 获取 预定的结果
     */
    fun onGetReservedResult(oneKeyReservedResponse: OneKeyReservedResponse,resourceTypeIdArray:Array<Int?>){

    }

    /**
     * 我的客户数据的回调
     */
    fun onMineClient(myClientList:ArrayList<MineClient>, action: RefreshAction){


    }

    /**
     * 错误的请求
     */
    fun  onError(content:String,action:RefreshAction){

    }

    /**
     * 获取资源的周期
     */
    fun  onGetResourceDate(resourceDateResponse: ResourceDateResponse,calendarAction: CalendarAction){

    }
}