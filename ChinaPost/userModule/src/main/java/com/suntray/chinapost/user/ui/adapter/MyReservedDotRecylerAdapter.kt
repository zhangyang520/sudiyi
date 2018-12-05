package com.suntray.chinapost.user.ui.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.DateUtils
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.presenter.MineDotPresenter
import com.suntray.chinapost.user.ui.dialog.DotRenewDialog

/**
 *  我预定的点位 列表
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 17:14
 */
class MyReservedDotRecylerAdapter: RecyclerView.Adapter<MyReservedDotRecylerAdapter.MyClientViewHolder>{

    var clientList:ArrayList<MineReservedDot>?=null
    var context:Context?=null
    var basePresenter: MineDotPresenter?=null

    constructor(clientList:ArrayList<MineReservedDot>,context:Context){
        this.clientList=clientList;
        this.context=context
    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyClientViewHolder {
        return MyClientViewHolder(View.inflate(context,R.layout.recylerview_mine_dot_state,null))
    }

    override fun getItemCount(): Int {
        if(clientList!=null && clientList!!.size>0){
            return clientList!!.size
        }
        return 0;
    }

    override fun onBindViewHolder(holder: MyClientViewHolder?, position: Int) {

        /**
         * 待审核 #FDA100
           已审核 00754b
         */
        if(clientList!!.get(position).clientname==null ||
                    clientList!!.get(position).clientname.equals("")){
            holder!!.tv_ad_name!!.text="暂无名称"
        }else{
            holder!!.tv_ad_name!!.text=clientList!!.get(position).clientname
        }
        if(clientList!!.get(position).adtypename==null || clientList!!.get(position).adtypename.equals("")){
            holder!!.tv_mark!!.text="暂无行业"
        }else{
            holder!!.tv_mark!!.text=clientList!!.get(position).adtypename
        }

//        未到期:00754b
//        已到期: #AEAEB0
//        即将到期:ff3823
        if(clientList!!.get(position).statename.equals("未到期")){
            //绿色
            holder!!.tv_ad_state!!.setTextColor(Color.parseColor("#00754b"))
        }else if(clientList!!.get(position).statename.equals("已到期")){
            //灰色
            holder!!.tv_ad_state!!.setTextColor(Color.parseColor("#AEAEB0"))
        }else if(clientList!!.get(position).statename.equals("即将到期")){
           //红色
            holder!!.tv_ad_state!!.setTextColor(Color.parseColor("#ff3823"))
        }
        holder!!.tv_ad_state!!.text=clientList!!.get(position).statename
        var startDate= DateUtil.dateFormat(DateUtil.parse2DateTime(clientList!!.get(position).startdate))
        var endDate= DateUtil.dateFormat(DateUtil.parse2DateTime(clientList!!.get(position).enddate))
        holder!!.tv_yuding_time_value!!.text=startDate+"至"+endDate
        holder!!.tv_ad_resource_value!!.text=clientList!!.get(position).resourcetypename


        holder.btn_chakan!!.setOnClickListener({
            //上传资质！
            dialog= DotRenewDialog(context)
            dialog!!.basePresenter=basePresenter
            dialog!!.myReservedDot=clientList!!.get(position)
            dialog!!.show()
            dialog!!.setContent()
        })
    }


    var dialog:DotRenewDialog?=null
    fun dismissDilog(){
        if(dialog!=null && dialog!!.isShowing){
            dialog!!.dismiss()
        }
    }
    /**
     * 内部类
     */
    inner class MyClientViewHolder: RecyclerView.ViewHolder {

        var tv_ad_name:TextView?=null
        var tv_mark:TextView?=null
        var tv_ad_state:TextView?=null
        var tv_yuding_time_value:TextView?=null
        var tv_ad_resource_value:TextView?=null

        var btn_chakan:Button?=null

        constructor(itemView: View?) : super(itemView){
            tv_ad_name=itemView!!.findViewById(R.id.tv_ad_name) as TextView;
            tv_mark=itemView!!.findViewById(R.id.tv_mark) as TextView;
            tv_ad_state=itemView!!.findViewById(R.id.tv_ad_state) as TextView;
            tv_yuding_time_value=itemView!!.findViewById(R.id.tv_yuding_time_value) as TextView;
            tv_ad_resource_value=itemView!!.findViewById(R.id.tv_ad_resource_value) as TextView;


            btn_chakan=itemView!!.findViewById(R.id.btn_chakan) as Button;
        }
    }
}