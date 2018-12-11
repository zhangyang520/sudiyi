package com.suntray.chinapost.map.ui.adapter.saler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.map.ui.activity.sale.PostAdReservedListActivity


/**
 *  预定点位 列表 清单适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 11:47
 */
class AdReservedRecylerAdapter:
        RecyclerView.Adapter<AdReservedRecylerAdapter.AdReservedViewHolder>{

    var mapDotList:ArrayList<MapDot>?=null
    var context: PostAdReservedListActivity?=null

    constructor(mapDotList: ArrayList<MapDot>?, context: PostAdReservedListActivity?) : super() {
        this.mapDotList = mapDotList
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdReservedViewHolder {
        return AdReservedViewHolder(View.inflate(context,R.layout.recylerview_ad_reserved_list,null))
    }

    override fun getItemCount(): Int {
        if(mapDotList!=null){
            return mapDotList!!.size
        }
        return 0;
    }

    var isClickAll:Boolean=false
    var selectPositionList:ArrayList<Int> = arrayListOf()
    override fun onBindViewHolder(holder: AdReservedViewHolder, position: Int) {
        holder!!.tv_location!!.setText(mapDotList!!.get(position).zoneaddress)
        holder!!.tv_id_value!!.setText(mapDotList!!.get(position).equid)
        holder!!.tv_position!!.setText(mapDotList!!.get(position).equlocation)
        if(isClickAll){
            holder!!.btn_check!!.isActivated=true
        }else{
            holder!!.btn_check!!.isActivated=false
        }
        holder!!.btn_check!!.setOnClickListener({
            holder!!.btn_check!!.isActivated=!holder!!.btn_check!!.isActivated
            if(holder!!.btn_check!!.isActivated){
                selectPositionList.add(position)
            }else{
                selectPositionList.remove(position)
            }
            context!!.setSelectNumer(selectPositionList.size)
        })

        if (selectPositionList.contains(position)) {
            //是否 包含 该位置
            holder!!.btn_check!!.isActivated=true
        }else{
            //不包含
            holder!!.btn_check!!.isActivated=false
        }
    }

    fun  processAllSelect(){
        isClickAll=true
        selectPositionList.clear()
        for (i in 0..mapDotList!!.size-1){
            selectPositionList.add(i)
        }
        notifyDataSetChanged()
    }

    /**
     * 处理取消全选
     */
    fun processAntiAllSelect(){
        isClickAll=false
        selectPositionList.clear()
        notifyDataSetChanged()
    }

    inner class AdReservedViewHolder:RecyclerView.ViewHolder{
        var tv_location:TextView?=null
        var tv_id_value:TextView?=null
        var tv_position:TextView?=null
        var btn_check:ImageView?=null


        constructor(itemView: View?) : super(itemView!!){
            tv_location=itemView!!.findViewById(R.id.tv_location) as TextView
            tv_id_value=itemView!!.findViewById(R.id.tv_id_value) as TextView
            tv_position=itemView!!.findViewById(R.id.tv_position) as TextView
            btn_check=itemView!!.findViewById(R.id.btn_check) as ImageView
        }
    }
}