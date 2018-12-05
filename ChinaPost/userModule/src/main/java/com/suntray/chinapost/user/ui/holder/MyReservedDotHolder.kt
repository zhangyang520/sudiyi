package com.suntray.chinapost.user.ui.holder

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.UiUtils
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.presenter.MineDotPresenter
import com.suntray.chinapost.user.ui.dialog.DotRenewDialog

class MyReservedDotHolder: BaseHolder<MineReservedDot>{

    var tv_ad_name: TextView?=null
    var tv_mark: TextView?=null
    var tv_ad_state: TextView?=null
    var tv_yuding_time_value: TextView?=null
    var tv_ad_resource_value: TextView?=null

    var btn_chakan: Button?=null

    var basePresenter: MineDotPresenter?=null

    constructor(basePresenter: MineDotPresenter?):super(){
        this.basePresenter = basePresenter
    }


    override fun initView(): View {
        var itemView=View.inflate(UiUtils.instance.getContext(),R.layout.recylerview_mine_dot_state,null)
        tv_ad_name=itemView!!.findViewById(R.id.tv_ad_name) as TextView;
        tv_mark=itemView!!.findViewById(R.id.tv_mark) as TextView;
        tv_ad_state=itemView!!.findViewById(R.id.tv_ad_state) as TextView;
        tv_yuding_time_value=itemView!!.findViewById(R.id.tv_yuding_time_value) as TextView;
        tv_ad_resource_value=itemView!!.findViewById(R.id.tv_ad_resource_value) as TextView;


        btn_chakan=itemView!!.findViewById(R.id.btn_chakan) as Button;
        return itemView
    }

    override fun refreshView(data: MineReservedDot?, activity: Activity?) {
        /**
         * 待审核 #FDA100
        已审核 00754b
         */
        if(data!!.clientname==null ||
                data!!.clientname.equals("")){
            tv_ad_name!!.text="暂无名称"
        }else{
            tv_ad_name!!.text=data!!.clientname
        }
        if(data!!.adtypename==null || data!!.adtypename.equals("")){
            tv_mark!!.text="暂无行业"
        }else{
            tv_mark!!.text=data!!.adtypename
        }

//        未到期:00754b
//        已到期: #AEAEB0
//        即将到期:ff3823
        if(data!!.statename.equals("未到期")){
            //绿色
            tv_ad_state!!.setTextColor(Color.parseColor("#00754b"))
        }else if(data!!.statename.equals("已到期")){
            //灰色
            tv_ad_state!!.setTextColor(Color.parseColor("#AEAEB0"))
        }else if(data!!.statename.equals("即将到期")){
            //红色
            tv_ad_state!!.setTextColor(Color.parseColor("#ff3823"))
        }
        tv_ad_state!!.text=data!!.statename
        var startDate= DateUtil.dateFormat(DateUtil.parse2DateTime(data!!.startdate))
        var endDate= DateUtil.dateFormat(DateUtil.parse2DateTime(data!!.enddate))
        tv_yuding_time_value!!.text=startDate+"至"+endDate
        tv_ad_resource_value!!.text=data!!.resourcetypename


        if(data!!.statename.equals("已到期")){
            btn_chakan!!.visibility=View.GONE
        }else{
            btn_chakan!!.visibility=View.VISIBLE
        }

        btn_chakan!!.setOnClickListener({
            //上传资质！
            dialog= DotRenewDialog(activity)
            dialog!!.basePresenter=basePresenter
            dialog!!.myReservedDot=data!!
            dialog!!.show()
            dialog!!.setContent()
        })
    }

    companion object {
        var dialog:DotRenewDialog?=null

        fun dismissDilog(){
            if(dialog!=null && dialog!!.isShowing){
                dialog!!.dismiss()
            }
        }
    }
}