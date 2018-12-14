package com.suntray.chinapost.user.ui.holder

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.baselibrary.utils.UiUtils
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.R.id.btn_check
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.presenter.MineDotPresenter
import com.suntray.chinapost.user.ui.activity.MineReservedDotActivity
import com.suntray.chinapost.user.ui.dialog.DotRenewDialog

class MyReservedDotHolder: BaseHolder<MineReservedDot>{

    var tv_ad_name: TextView?=null
    var tv_mark: TextView?=null
    var tv_ad_state: TextView?=null
    var tv_yuding_time_value: TextView?=null
    var tv_ad_resource_value: TextView?=null

    var btn_chakan: Button?=null
    var btn_check: ImageView?=null
    var basePresenter: MineDotPresenter?=null
    var context:MineReservedDotActivity?=null
    var selectPositionList:ArrayList<Int>?=null

    companion  object MyReservedDotHolder{
        var isNeedShowSelected:Boolean=false
        var renewDays=-1
        var dialog:DotRenewDialog?=null

        fun dismissDilog(){
            if(dialog!=null && dialog!!.isShowing){
                dialog!!.dismiss()
            }
        }
    }

    constructor(basePresenter: MineDotPresenter?,context:MineReservedDotActivity,selectPositionList:ArrayList<Int>):super(){
        this.basePresenter = basePresenter
        this.context=context
        this.selectPositionList=selectPositionList
    }


    override fun initView(): View {
        var itemView=View.inflate(UiUtils.instance.getContext(),R.layout.recylerview_mine_dot_state,null)
        tv_ad_name=itemView!!.findViewById(R.id.tv_ad_name) as TextView;
        tv_mark=itemView!!.findViewById(R.id.tv_mark) as TextView;
        tv_ad_state=itemView!!.findViewById(R.id.tv_ad_state) as TextView;
        tv_yuding_time_value=itemView!!.findViewById(R.id.tv_yuding_time_value) as TextView;
        tv_ad_resource_value=itemView!!.findViewById(R.id.tv_ad_resource_value) as TextView;


        btn_chakan=itemView!!.findViewById(R.id.btn_chakan) as Button;
        btn_check=itemView!!.findViewById(R.id.btn_check) as ImageView;

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
            //红色 申请解除预约
            tv_ad_state!!.setTextColor(Color.parseColor("#ff3823"))
        }else if( data!!.statename.equals("申请解除预约")){
            tv_ad_state!!.setTextColor(Color.parseColor("#AEAEB0"))
        }
        tv_ad_state!!.text=data!!.statename
        var startDate= DateUtil.dateFormat(DateUtil.parse2DateTime(data!!.startdate))
        var endDate= DateUtil.dateFormat(DateUtil.parse2DateTime(data!!.enddate))
        tv_yuding_time_value!!.text=startDate+"至"+endDate
        tv_ad_resource_value!!.text=data!!.resourcetypename


        if(data!!.statename.equals("已到期")){
            btn_chakan!!.visibility=View.INVISIBLE
        }else{
            btn_chakan!!.visibility=View.VISIBLE
        }

        //是否需要显示
        if(isNeedShowSelected){
            if(data!!.statename.equals("已到期") || data!!.statename.equals("申请解除预约")){
                //如果对应的状态 为已到期 申请解除预约 不显示
                btn_check!!.visibility=View.INVISIBLE
            }else{
                btn_check!!.visibility=View.VISIBLE
                if(selectPositionList!!.contains(position)){
                    SystemUtil.printlnStr("selectPositionList.contains(position):"+position)
                    btn_check!!.isActivated=true
                }else{
                    SystemUtil.printlnStr("selectPositionList.not contains(position):"+position)
                    btn_check!!.isActivated=false
                }
            }
        }else{
            btn_check!!.visibility=View.INVISIBLE
        }



        btn_check!!.setOnClickListener({
            if(data!!.statename.equals("未到期") || data!!.statename.equals("即将到期")){
                if(btn_check!!.isActivated){
                    selectPositionList!!.remove(position)
                }else{
                    selectPositionList!!.add(position)
                }
                btn_check!!.isActivated=!btn_check!!.isActivated
                context!!.setSelectNumer(selectPositionList!!.size)
            }else{
                ToastUtil.makeText(UiUtils.instance.getContext(),"不可以选择")
            }
        })
        btn_chakan!!.setOnClickListener({
            //上传资质！
            dialog= DotRenewDialog(activity)
            dialog!!.basePresenter=basePresenter
            dialog!!.myReservedDot=data!!
            dialog!!.show()
            dialog!!.setContent()
        })

        if(data!!.statename.equals("申请解除预约")){
            btn_chakan!!.visibility=View.INVISIBLE
        }else{
            btn_chakan!!.visibility=View.VISIBLE
        }
    }
}