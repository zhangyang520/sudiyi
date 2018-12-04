package com.suntray.chinapost.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.PopupWindow
import com.suntray.chinapost.LoginActivity
import com.suntray.chinapost.R
import com.suntray.chinapost.adapter.RoleAdapter
import com.suntray.chinapost.baselibrary.data.bean.User
import com.suntray.chinapost.data.bean.UserRole
import com.suntray.chinapost.utils.inner.RoleItemClickListener
import com.zhy.autolayout.utils.AutoUtils

/**
 *  角色弹出框
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/22 16:12
 */
object RolePopup {

    var currentRole:UserRole?=null
    var window:PopupWindow?=null
    /**
     * 弹出 角色弹 出框
     */
    fun showRolePoopup(context:LoginActivity,rootView:View,roleItemClickListener: RoleItemClickListener){
        if(window!=null && window!!.isShowing){
            return
        }
        window = PopupWindow(context,null, R.style.Transparent_Dialog);
        window!!.width= AutoUtils.getPercentWidthSize(710);
        window!!.height= AutoUtils.getPercentHeightSize(270);
        var contentView=View.inflate(context,R.layout.recylerview_role,null)
        var recylerview_role=contentView.findViewById(R.id.recylerview) as RecyclerView
        AutoUtils.autoSize(contentView)
        window!!.contentView= contentView;
        window!!.isOutsideTouchable=false
        window!!.setOnDismissListener {
            window ==null
            if(roleItemClickListener!=null){
                roleItemClickListener.onItemClick(currentRole!!)
            }
        }
        window!!.showAsDropDown(rootView)
        var linearLayoutManager= LinearLayoutManager(context)
        linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
        recylerview_role.layoutManager=linearLayoutManager
        //初始化数据
        var roleArrayList= arrayListOf<UserRole>()
        roleArrayList.add(UserRole("销售人员",4))
        roleArrayList.add(UserRole("供应商",3))
        roleArrayList.add(UserRole("代理商",2))
        var roleAdapter=RoleAdapter(roleArrayList,context)
        recylerview_role!!.adapter= roleAdapter
    }
}