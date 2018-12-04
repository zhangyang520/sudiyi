package com.suntray.chinapost.user.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.lidroid.xutils.ViewUtils
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.rx.hasTxt
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.DateUtils
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.data.request.MineXudingDotRequest
import com.suntray.chinapost.user.presenter.MineDotPresenter
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.dialog_dot_renew.*
import java.util.*

/**
 *   点位预定的弹框
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/16 10:05
 */
class DotRenewDialog:Dialog{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)
    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)


    var myReservedDot:MineReservedDot?=null
    var tv_company_name:TextView?=null
    var tv_industry:TextView?=null
    var tv_renew_position:TextView?=null
    var ed_end_time:EditText?=null
    var iv_dot_renew_cancel:ImageView?=null
    var basePresenter: MineDotPresenter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var view=View.inflate(context, R.layout.dialog_dot_renew,null);
        AutoUtils.autoSize(view);
        setContentView(view)
        tv_company_name=view.findViewById(R.id.tv_company_name) as TextView
        tv_industry=view.findViewById(R.id.tv_industry) as TextView
        tv_renew_position=view.findViewById(R.id.tv_renew_position) as TextView
        ed_end_time=view.findViewById(R.id.ed_end_time) as EditText
        var calendar=Calendar.getInstance()
        println("yuanshi DateUtil.dateFormat():"+DateUtil.dateFormat(DateUtil.parse2DateTime(myReservedDot!!.enddate)))
        calendar.time=DateUtil.parse2DateTime(myReservedDot!!.enddate)
        calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)+5)
        var endDate= DateUtil.dateFormat(calendar.time)
        ed_end_time!!.setText(endDate)
        iv_dot_renew_cancel=view.findViewById(R.id.iv_dot_renew_cancel) as ImageView
        iv_dot_renew_cancel!!.setOnClickListener({
            dismiss()
        })

        ed_end_time!!.setOnClickListener({
            //
            var calendar=Calendar.getInstance()
            calendar.time=DateUtil.parse2Date(endDate)
            var picker= DatePickerDialog(context,object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var calendar= Calendar.getInstance()
                    calendar.set(year,month,dayOfMonth)
                    var content= DateUtil.dateFormat(calendar.time)
                    ed_end_time!!.setText(content)
                }
            }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        })

        btn_ok.setOnClickListener({
            //点击确定:
            if(ed_end_time!!.hasTxt()){
                if(DateUtil.parse2Date(ed_end_time!!.getTxt()).time>DateUtil.parse2Date(myReservedDot!!.enddate).time){
                    basePresenter!!.dotXuding(MineXudingDotRequest(myReservedDot!!.id,ed_end_time!!.getTxt(),UserDao.getLocalUser().id))
                }else{
                    ToastUtil.makeText(context,"选择的日期小于结束日期")
                }
            }else{
                ToastUtil.makeText(context,"请选择结束日期")
            }
        })
        setCancelable(true)
        view.layoutParams.height=AutoUtils.getPercentHeightSize(898)
        view.layoutParams.width=AutoUtils.getPercentWidthSize(605)
        view.requestLayout()
    }

    /**
     * 设置内容
     */
    fun setContent(){
        if(myReservedDot!=null){
            tv_company_name!!.setText(myReservedDot!!.clientname)
            if(myReservedDot!!.adtypename==null || myReservedDot!!.adtypename.equals("")){
                tv_industry!!.setText("暂无行业")
            }else{
                tv_industry!!.setText(myReservedDot!!.adtypename)
            }
            tv_renew_position!!.setText("预定资源位:"+myReservedDot!!.resourcetypename)
        }
    }
}