package com.suntray.chinapost.user.utils

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.PopupWindow
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.ui.adapter.ClientNameAdapter
import com.suntray.chinapost.user.ui.adapter.inner.ClickClientNameAction
import com.zhy.autolayout.utils.AutoUtils

/**
 *  弹出框的工具类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/26 13:15
 */
object PopupUtils {

    var window:PopupWindow?=null
    var recyler_clientName:RecyclerView?=null
    var clientNameAdadapter:ClientNameAdapter?=null
    /**
     * 展示客户名称列表
     */
    fun  showPopupWindow(context: Context,rootView:View,clientNameDatas:ArrayList<MineClient>,clickClientNameAction: ClickClientNameAction){
        if(window==null || !window!!.isShowing()){
            var contentView= View.inflate(context, R.layout.popup_client_name,null);
            AutoUtils.autoSize(contentView)
            window=PopupWindow(context,null,R.style.Transparent_Dialog)
            window!!.width= AutoUtils.getPercentWidthSize(400);
            window!!.height= AutoUtils.getPercentHeightSize(500);
            window!!.contentView=contentView;
            window!!.isOutsideTouchable=false
            //-AutoUtils.getPercentWidthSize(250) AutoUtils.getPercentHeightSize(50)
            window!!.showAsDropDown(rootView,AutoUtils.getPercentWidthSize(350),AutoUtils.getPercentHeightSize(30))
            recyler_clientName=contentView.findViewById(R.id.recyler_clientName) as RecyclerView
        }

        window!!.setOnDismissListener {
            window=null
        }
        //进行刷新界面
        var linearLayoutManager=LinearLayoutManager(context)
        linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
        recyler_clientName!!.layoutManager=linearLayoutManager

        clientNameAdadapter=ClientNameAdapter(clientNameDatas,context,clickClientNameAction)
        recyler_clientName!!.adapter=clientNameAdadapter
    }

    /*
     * 消失 对话框
     */
    fun  dismissWindow(){
        if(window!=null && window!!.isShowing()){
            window!!.dismiss()
            window=null
        }
    }
    fun addClientNameDatas(clientNameDatas:ArrayList<MineClient>){
        if(window!=null && window!!.isShowing){
            var index=clientNameAdadapter!!.provinceList!!.size-1
            clientNameAdadapter!!.provinceList!!.addAll(clientNameDatas)
            clientNameAdadapter!!.notifyItemChanged(index)
        }
    }
}