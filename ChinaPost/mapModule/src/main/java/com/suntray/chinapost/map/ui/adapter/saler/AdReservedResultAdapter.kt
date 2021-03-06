package com.suntray.chinapost.map.ui.adapter.saler

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.response.OneKeySuccessInfo

/**
 *  预定结果 的适配器
 *  @Author 张扬 @version 1.0
 *  @Date  15:03
 */
class AdReservedResultAdapter: RecyclerView.Adapter<AdReservedResultAdapter.AdReservedResultViewHolder>{

    var dataList:MutableList<OneKeySuccessInfo>?=null
    var context:Context?=null
    var selectedIndexList:ArrayList<Int>?=null

    constructor(data: MutableList<OneKeySuccessInfo>?, context: Context?,selectedIndexList:ArrayList<Int>) : super() {
        this.dataList = data
        this.context = context
        this.selectedIndexList=selectedIndexList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdReservedResultViewHolder {
        return AdReservedResultViewHolder(View.inflate(context,R.layout.recylerview_reserved_result,null))
    }

    override fun getItemCount(): Int {
        if(dataList!=null){
            return dataList!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: AdReservedResultViewHolder, position: Int) {
        holder!!.tv_resource_name!!.text=dataList!!.get(position).point!!.pointname
        if(dataList!!.get(position).success){
            //成功
            holder!!.btn_check!!.visibility=View.VISIBLE
            //设置点击事件
            holder!!.btn_check!!.setOnClickListener({
                if(selectedIndexList!!.contains(position)){
                    //如果包含
                    holder!!.btn_check!!.isActivated=false
                    selectedIndexList!!.remove(position)

                }else{
                    //如果不包含
                    holder!!.btn_check!!.isActivated=true
                    selectedIndexList!!.add(position)

                }
            })

            if(selectedIndexList!!.contains(position)){
                //如果包含
                holder!!.btn_check!!.isActivated=true
            }else{
                //如果不包含
                holder!!.btn_check!!.isActivated=false
            }

            holder!!.tv_state!!.text="成功"
            holder!!.tv_state!!.setTextColor(Color.parseColor("#00754b"))
            holder!!.tv_error_msg!!.visibility=View.INVISIBLE
        }else{
            holder!!.btn_check!!.visibility=View.INVISIBLE
            holder!!.tv_error_msg!!.text=dataList!!.get(position).error
            holder!!.tv_error_msg!!.visibility=View.VISIBLE
            holder!!.tv_state!!.setTextColor(Color.parseColor("#ff3826"))
            holder!!.tv_state!!.text="失败"
        }
    }


    inner class AdReservedResultViewHolder:RecyclerView.ViewHolder{
        var tv_resource_name: TextView? = null
        var tv_error_msg: TextView? = null
        var tv_state: TextView? = null
        var btn_check:ImageView?=null

        constructor(itemView: View?) : super(itemView!!){
            btn_check= itemView!!.findViewById(R.id.btn_check) as ImageView
            tv_resource_name= itemView!!.findViewById(R.id.tv_resource_name) as TextView
            tv_error_msg= itemView!!.findViewById(R.id.tv_error_msg) as TextView
            tv_state= itemView!!.findViewById(R.id.tv_state) as TextView
        }
    }

    /**
     * 处理全选
     */
    fun processSlectedAll() {
        //处理全选
        selectedIndexList!!.clear()
        for (index in dataList!!.indices) {
            if(dataList!!.get(index)!!.success){
                selectedIndexList!!.add(index)
            }
        }
        notifyDataSetChanged()
    }

    fun processAntiAll() {
        //处理全选
        selectedIndexList!!.clear()
        notifyDataSetChanged()
    }
}