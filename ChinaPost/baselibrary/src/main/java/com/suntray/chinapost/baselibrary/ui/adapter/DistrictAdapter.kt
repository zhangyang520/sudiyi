package com.suntray.chinapost.baselibrary.ui.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.suntray.chinapost.baselibrary.R
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.ui.adapter.inner.ClickCodeAction
import com.zhy.autolayout.utils.AutoUtils

/**
 *   省的列表数据 适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 18:55
 */
class DistrictAdapter:RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>{

    var provinceList:ArrayList<ProvinceCity>?=null
    var context:Context?=null
    var clickPosition=-1
    var clickCodeAction: ClickCodeAction?=null


    constructor(provinceList: ArrayList<ProvinceCity>?, context: Context?) : super() {
        this.provinceList = provinceList
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DistrictViewHolder {
        var contentView=View.inflate(context,R.layout.recylerview_district,null)
        var layoutParams=ViewGroup.LayoutParams(AutoUtils.getPercentWidthSize(750/3),AutoUtils.getPercentHeightSize(88))
        contentView.layoutParams=layoutParams
        contentView.requestLayout()
        AutoUtils.autoSize(contentView)
        return DistrictViewHolder(contentView)
    }

    override fun getItemCount(): Int {
        if(provinceList!=null){
            return provinceList!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: DistrictViewHolder?, position: Int) {
        holder!!.tv_province!!.setText(provinceList!!.get(position).district)

        holder.itemView.setOnClickListener({
            clickPosition=position

            //此时 点击 弹框消失
            clickCodeAction!!.clickDistrict(provinceList!!.get(position))
            notifyDataSetChanged()
        })

        if(clickPosition==position){
            //如果当前相等
            holder!!.tv_province!!.setTextColor(Color.BLACK)
        }else{
            holder!!.tv_province!!.setTextColor(Color.parseColor("#98979C"))
        }
    }

    /**
     * 省的 viewHolder
     */
    inner class DistrictViewHolder:RecyclerView.ViewHolder{

        var tv_province:TextView?=null

        constructor(itemView: View?) : super(itemView){
            tv_province= itemView!!.findViewById(R.id.tv_district) as TextView
        }
    }
}