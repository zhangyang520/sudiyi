package com.suntray.chinapost.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.suntray.chinapost.LoginActivity
import com.suntray.chinapost.R
import com.suntray.chinapost.data.bean.UserRole
import com.suntray.chinapost.utils.RolePopup
import com.zhy.autolayout.utils.AutoUtils

/**
 *   角色的适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/22 16:41
 */
class RoleAdapter: RecyclerView.Adapter<RoleAdapter.RoleViewHolder>{

    var roleList:ArrayList<UserRole>?=null
    var context:LoginActivity?=null
    constructor(roleList: ArrayList<UserRole>?, context: LoginActivity?) : super() {
        this.roleList = roleList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(View.inflate(context, R.layout.item_role,null))
    }

    override fun getItemCount(): Int {
        if(roleList!=null && roleList!!.size>0){
            return roleList!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder!!.tv_role!!.setText(roleList!!.get(position).roleName )
        holder.itemView.setOnClickListener({
            if(RolePopup.window!=null){
                RolePopup.currentRole=roleList!!.get(position)
                RolePopup.window!!.dismiss()
            }
        })
    }

    /**
     * 对应的 viewHolder
     */
   class RoleViewHolder: RecyclerView.ViewHolder{
       var tv_role:TextView?=null

       constructor(itemView: View?):super(itemView!!){
           AutoUtils.autoSize(itemView)
           var layoutParams=
                   ViewGroup.LayoutParams(AutoUtils.getPercentWidthSize(710),AutoUtils.getPercentHeightSize(88))
           itemView!!.layoutParams=layoutParams
           itemView!!.requestLayout()
           tv_role=itemView!!.findViewById(R.id.tv_role) as TextView
       }
   }
}