package com.suntray.chinapost.baselibrary.ui.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.R
import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.ui.adapter.inner.ClickCodeAction
import com.zhy.autolayout.AutoRelativeLayout
import com.zhy.autolayout.utils.AutoUtils

/**
 *   省的列表数据 适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 18:55
 */
class ProvinceAdapter:RecyclerView.Adapter<ProvinceAdapter.ProvinceViewHoler>{

    var provinceList:ArrayList<ProvinceCity>?=null
    var context:Context?=null
    var clickPosition=-1
    var clickCodeAction: ClickCodeAction?=null

    constructor(provinceList: ArrayList<ProvinceCity>?, context: Context?) : super() {
        this.provinceList = provinceList
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceViewHoler {
        var contentView=View.inflate(context,R.layout.recylerview_province,null)
        SystemUtil.printlnStr("ProvinceAdapter onCreateViewHolder height:"+AutoUtils.getPercentHeightSize(88))
        var layoutParams=ViewGroup.LayoutParams(AutoUtils.getPercentWidthSize(750/3),AutoUtils.getPercentHeightSize(70))
        contentView.layoutParams=layoutParams
        contentView.requestLayout()
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
        holder!!.tv_province!!.setText(provinceList!!.get(position).province)
        holder.itemView.setOnClickListener({
            clickPosition=position
            notifyDataSetChanged()
            //此时点击 请求接口
            clickCodeAction!!.clickItem(provinceList!!.get(position).id, CityListAction.CityAction,provinceList!!.get(position))
        })

        if(clickPosition==position){
            //如果当前相等
            holder.rl_top!!.setBackgroundColor(Color.WHITE)
            holder!!.tv_province!!.setTextColor(Color.BLACK)
        }else{
            holder.rl_top!!.setBackgroundColor(Color.parseColor("#F0EFF4"))
            holder!!.tv_province!!.setTextColor(Color.parseColor("#98979C"))
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