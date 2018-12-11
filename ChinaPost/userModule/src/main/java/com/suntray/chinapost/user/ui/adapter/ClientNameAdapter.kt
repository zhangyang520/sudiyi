package com.suntray.chinapost.user.ui.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.suntray.chinapost.baselibrary.R
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.ui.adapter.inner.ClickClientNameAction
import com.zhy.autolayout.AutoRelativeLayout
import com.zhy.autolayout.utils.AutoUtils

/**
 *   省的列表数据 适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 18:55
 */
class ClientNameAdapter:RecyclerView.Adapter<ClientNameAdapter.ProvinceViewHoler>{

    var provinceList:ArrayList<MineClient>?=null
    var context:Context?=null
    var clickPosition=-1

    var clickClientNameAction:ClickClientNameAction?=null
    constructor(provinceList: ArrayList<MineClient>?, context: Context?, clickClientNameAction: ClickClientNameAction) : super() {
        this.provinceList = provinceList
        this.context = context
        this.clickClientNameAction=clickClientNameAction
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceViewHoler {
        var contentView=View.inflate(context,R.layout.recylerview_client_name,null)
//        var layoutParams=
//                AutoRelativeLayout.LayoutParams(AutoUtils.getPercentWidthSize(150),AutoUtils.getPercentHeightSize(80))
//        contentView.layoutParams=layoutParams
//        contentView.requestLayout()
        AutoUtils.autoSize(contentView)
        return ProvinceViewHoler(contentView)
    }

    override fun getItemCount(): Int {
        if(provinceList!=null){
            return provinceList!!.size
        }
        return 0
    }

    /**
     * <!--
    #98979C 灰色
    #000000 黑色
    -->
     */
    override fun onBindViewHolder(holder: ProvinceViewHoler, position: Int) {
        holder!!.tv_province!!.setText(provinceList!!.get(position).name)
        holder.itemView.setOnClickListener({
            clickPosition=position
            notifyDataSetChanged()
            //此时点击 请求接口
            clickClientNameAction!!.onClickClientName(provinceList!!.get(position))
        })

        if(clickPosition==position){
            //如果当前相等
            holder.rl_top!!.setBackgroundColor(Color.WHITE)
            holder!!.tv_province!!.setTextColor(Color.BLACK)
        }else{
            holder.rl_top!!.setBackgroundColor(Color.parseColor("#F0EFF4"))
            holder!!.tv_province!!.setTextColor(Color.parseColor("#98979C"))
        }

        if(position==(provinceList!!.size-1) && clickClientNameAction!=null && provinceList!!.size>=10){
            //加载更多的回调 .....
            clickClientNameAction!!.onNeedLoadMore()
        }
    }

    /**
     * 省的 viewHolder
     */
    inner class ProvinceViewHoler:RecyclerView.ViewHolder{

        var tv_province:TextView?=null
        var rl_top: AutoRelativeLayout?=null
        constructor(itemView: View?) : super(itemView!!){
            rl_top=itemView!!.findViewById(R.id.rl_top) as AutoRelativeLayout
            tv_province= itemView!!.findViewById(R.id.tv_province) as TextView
        }
    }
}