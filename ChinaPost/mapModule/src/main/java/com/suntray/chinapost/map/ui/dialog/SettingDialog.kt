package com.suntray.chinapost.map.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import com.suntray.chinapost.baselibrary.data.bean.AdStyle
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.ut.base.utils.AppPrefsUtils
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.constants.MapContstants
import com.suntray.chinapost.map.ui.activity.sale.PostPoiSearchActivity
import com.suntray.chinapost.map.utils.ASettingUtils
import com.zhy.autolayout.AutoLinearLayout
import com.zhy.autolayout.AutoRelativeLayout
import com.zhy.autolayout.utils.AutoUtils
import java.util.*

/**
 *   设置的对话弹框
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/16 10:05
 */
class SettingDialog:Dialog{

    var context: PostPoiSearchActivity?=null
    constructor(context: PostPoiSearchActivity?) : super(context){
        this.context=context
    }

    constructor(context: PostPoiSearchActivity?, themeResId: Int) : super(context, themeResId){
        this.context=context
    }

    constructor(context: Context?, cancelable: Boolean,
                    cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    var btn_ok:Button?=null
    var btn_cance:Button?=null
    var ed_end_time:EditText?=null
    var ed_start_time:EditText?=null
    var rl_yuding:AutoLinearLayout?=null
    var rl_ad:AutoLinearLayout?=null
    var iv_dot_name:ImageView?=null
    var iv_position:ImageView?= null
    var currentAdType:Int=-1 //当前的广告类型
    var currentKeyIndex=-1
    var currentStartTime=""
    var currentEndTime=""
    var initResourceIdList:MutableList<Int>?= null

    var tv_search_type_title:TextView?=null
    var rl_search_type:AutoRelativeLayout?=null
    var view_line1:View?=null

    var listenr:IOkClickListener?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var view=View.inflate(context, R.layout.popup_setting,null);
        AutoUtils.autoSize(view);
        setContentView(view)

        btn_ok=findViewById(R.id.btn_ok) as Button
        btn_cance=findViewById(R.id.btn_cance) as Button
        iv_dot_name=findViewById(R.id.iv_dot_name) as ImageView
        iv_position=findViewById(R.id.iv_position) as ImageView
        ed_end_time=findViewById(R.id.ed_end_time) as EditText
        tv_search_type_title=findViewById(R.id.tv_search_type_title) as TextView
        rl_search_type=findViewById(R.id.rl_search_type) as AutoRelativeLayout
        view_line1=findViewById(R.id.view_line1)

        ed_start_time=findViewById(R.id.ed_start_time) as EditText
        rl_yuding=findViewById(R.id.rl_yuding) as AutoLinearLayout
        rl_ad=findViewById(R.id.rl_ad) as AutoLinearLayout

        var dotIndex=AppPrefsUtils.getInt(MapContstants.SETTING_KEYWORDINDEX,1)
        currentKeyIndex=dotIndex
        //设置对应的标签
        iv_dot_name!!.isActivated= (dotIndex==0)
        iv_dot_name!!.setOnClickListener({
            currentKeyIndex=0
            iv_dot_name!!.isActivated=(currentKeyIndex==0)
            iv_position!!.isActivated=(currentKeyIndex==1)
        })
        iv_position!!.isActivated= (dotIndex==1)
        iv_position!!.setOnClickListener({
            currentKeyIndex=1
            iv_dot_name!!.isActivated=(currentKeyIndex==0)
            iv_position!!.isActivated=(currentKeyIndex==1)
        })
        var startTime=AppPrefsUtils.getString(MapContstants.SETTING_STARTTIME,
                                        DateUtil.dateFormat(Calendar.getInstance().time))
        currentStartTime=startTime
        ed_start_time!!.setText(startTime)

        var endTime=AppPrefsUtils.getString(MapContstants.SETTING_ENDTIME,
                                        DateUtil.dateFormat(Calendar.getInstance().time))
        currentEndTime=endTime
        ed_end_time!!.setText(endTime)

        //根据id 去查询
        currentAdType=AppPrefsUtils.getInt(MapContstants.SETTING_ADTYPEID,-1)

        //去设置 resourceIds
        var resourceIds=AppPrefsUtils.getString(MapContstants.SETTING_RESOURCEIDS,"[]")
        if(!resourceIds.equals("") && !resourceIds.equals("[]")){
            initResourceIdList=ASettingUtils.getDecoderString(resourceIds)
            SystemUtil.printlnStr("resourceIds:"+resourceIds+"..initResourceIdList:"+initResourceIdList.toString())
        }


        /**
         * 开始时间的点击
         */
        ed_start_time!!.setOnClickListener({
            var calendar=Calendar.getInstance()
            calendar.time=DateUtil.parse2Date(ed_start_time!!.getTxt())
            var picker= DatePickerDialog(context,object: DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    var calendar= Calendar.getInstance()
                    calendar.set(year,month,dayOfMonth)
                    var content= DateUtil.dateFormat(calendar.time)
                    ed_start_time!!.setText(content)
                }
            }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
        })

        /**
         * 结束时间的点击
         */
        ed_end_time!!.setOnClickListener({
            var calendar=Calendar.getInstance()
            calendar.time=DateUtil.parse2Date(ed_end_time!!.getTxt())
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

        btn_ok!!.setOnClickListener({
            //进行保存信息
            if(exameInfo()){
                AppPrefsUtils.putInt(MapContstants.SETTING_KEYWORDINDEX,currentKeyIndex)
                SystemUtil.printlnStr("currentAdType:"+currentKeyIndex)
                AppPrefsUtils.putInt(MapContstants.SETTING_ADTYPEID,currentAdType)
                AppPrefsUtils.putString(MapContstants.SETTING_STARTTIME,ed_start_time!!.getTxt())
                AppPrefsUtils.putString(MapContstants.SETTING_ENDTIME,ed_end_time!!.getTxt())
                if(resourceIdList.size>0){
                   var resourceIdString=ASettingUtils.getIntList(resourceIdList!!)
                    AppPrefsUtils.putString(MapContstants.SETTING_RESOURCEIDS,resourceIdString)
                }else{
                    AppPrefsUtils.putString(MapContstants.SETTING_RESOURCEIDS,"[]")
                }

                if(rl_search_type!!.visibility==View.VISIBLE){
                     //显示的时候
                     if(currentKeyIndex==0){
                         //点位名称
                         if(listenr!=null){
                             listenr!!.onDotNameClickListener()
                         }
                     }else{
                         //地理位置
                         if(listenr!=null){
                             listenr!!.onPositionClickListener()
                         }
                     }
                }
                dismiss()
            }
        })

        btn_cance!!.setOnClickListener({
            dismiss()
        })
        setCancelable(true)
    }

    /**
     * 检测 开始 和 结束 时间
     */
    private fun exameInfo(): Boolean {
        if(!ed_end_time!!.getTxt().equals("") &&
                    !ed_start_time!!.getTxt().equals("")){
            //两个都不为空
            var endTime=DateUtil.parse2Date(ed_end_time!!.getTxt())
            var startTime=DateUtil.parse2Date(ed_start_time!!.getTxt())
            if(endTime.time<startTime.time){
                ToastUtil.makeText(context!!,"开始时间 不能小于 结束时间")
                return false
            }
        }else{
            ToastUtil.makeText(context!!,"开始时间和结束时间不能为空")
            return false
        }
        return true
    }

    var resourceIdList= mutableListOf<ResourceType>()
    var columnNum=2
    /**
     * 设置 资源位的信息
     */
    fun setResourceDotContent(resourceList:List<ResourceType>){
        //首先 判断 有几行
        var resourceIndex=-1;
        var total=0
        if(resourceList.size%columnNum==0){
            total=resourceList.size/columnNum-1
        }else{
            total=resourceList.size/columnNum
        }
        for(index in 0..total){
            if(index!=total){
                var contentView=View.inflate(context,R.layout.item_setting,null)
                var btn1=contentView.findViewById(R.id.btn_1) as Button;
                var btn2=contentView.findViewById(R.id.btn_2) as Button;
                for(i in 0..1){
                    resourceIndex++
                    if(i==0){
                        btn1.setText(resourceList.get(resourceIndex).name)
                        if(initResourceIdList!=null && initResourceIdList!!.contains(resourceList.get(resourceIndex).id)){
                            btn1.isActivated=true
                            resourceIdList.add(resourceList.get(resourceIndex))
                        }
                        btn1.setOnClickListener({
                            btn1.isActivated=!btn1.isActivated
                            //保存 对应的位置
                            if(btn1.isActivated){
                                resourceIdList.add(resourceList.get(index*columnNum+i))
                            }else{
                                resourceIdList.remove(resourceList.get(index*columnNum+i))
                            }
                        })
                    }else if(i==1){
                        btn2.setText(resourceList.get(resourceIndex).name)
                        if(initResourceIdList!=null && initResourceIdList!!.contains(resourceList.get(resourceIndex).id)){
                            btn2.isActivated=true
                            resourceIdList.add(resourceList.get(resourceIndex))
                        }
                        btn2.setOnClickListener({
                            btn2.isActivated=!btn2.isActivated
                            //保存 对应的位置
                            if(btn2.isActivated){
                                resourceIdList.add(resourceList.get(index*columnNum+i))
                            }else{
                                resourceIdList.remove(resourceList.get(index*columnNum+i))
                            }
                        })
                    }
                }
                rl_yuding!!.addView(contentView)
            }else{
                SystemUtil.printlnStr("resourceList.size-columnNum*(index+1)"+(resourceList.size-columnNum*(index))+"..resourceIndex:"+resourceIndex)
                var contentView=View.inflate(context,R.layout.item_setting,null)
                for(i in 1..(resourceList.size-columnNum*(index))){
                    resourceIndex++
                    var btn1=contentView.findViewById(R.id.btn_1) as Button;
                    var btn2=contentView.findViewById(R.id.btn_2) as Button;
                    if(i==1){
                        btn2.visibility=View.INVISIBLE
                        btn1.setText(resourceList.get(resourceIndex).name)
                        if(initResourceIdList!=null && initResourceIdList!!.contains(resourceList.get(resourceIndex).id)){
                            btn1.isActivated=true
                            resourceIdList.add(resourceList.get(resourceIndex))
                        }
                        btn1.setOnClickListener({
                            btn1.isActivated=!btn1.isActivated
                            //保存 对应的位置
                            if(btn1.isActivated){
                                resourceIdList.add(resourceList.get(index*columnNum+(i-1)))
                            }else{
                                resourceIdList.remove(resourceList.get(index*columnNum+(i-1)))
                            }
                        })
                    }else if(i==2){
                        btn2.visibility=View.VISIBLE
                        if(initResourceIdList!=null && initResourceIdList!!.contains(resourceList.get(resourceIndex).id)){
                            btn2.isActivated=true
                            resourceIdList.add(resourceList.get(resourceIndex))
                        }
                        btn2.setText(resourceList.get(resourceIndex).name)
                        btn2.setOnClickListener({
                            btn2.isActivated=!btn2.isActivated
                            //保存 对应的位置
                            if(btn2.isActivated){
                                resourceIdList.add(resourceList.get(index*columnNum+(i-1)))
                            }else{
                                resourceIdList.remove(resourceList.get(index*columnNum+(i-1)))
                            }
                        })
                    }
                }
                rl_yuding!!.addView(contentView)
            }
        }
    }


    var btnAdCurent:Button?=null
    /**
     * 设置 资源位的信息
     */
    fun setAdStyleContent(resourceList:List<AdStyle>){
        //首先 判断 有几行
        var resourceIndex=-1;
        var total=0
        if(resourceList.size%columnNum==0){
            total=resourceList.size/columnNum-1
        }else{
            total=resourceList.size/columnNum
        }
        for(index in 0..total){
            if(index!=total){
                var contentView=View.inflate(context,R.layout.item_setting,null)
                var btn1=contentView.findViewById(R.id.btn_1) as Button;
                var btn2=contentView.findViewById(R.id.btn_2) as Button;
                for(i in 0..1){
                    resourceIndex++
                    if(i==0){
                        btn1.setText(resourceList.get(resourceIndex).name)
                        if(currentAdType==(resourceList.get(resourceIndex).id)){
                            btn1.isActivated=true
                            btnAdCurent=btn1;
                        }
                        btn1.setOnClickListener({
                            btn1.isActivated=!btn1.isActivated
                            //保存 对应的位置
                            if(btn1.isActivated){
                                if(btnAdCurent!=null){
                                    btnAdCurent!!.isActivated=!btnAdCurent!!.isActivated
                                }
                                btnAdCurent=btn1;
                                currentAdType=resourceList.get(index*columnNum+i).id
                            }else{
                                btnAdCurent=null
                                currentAdType=-1
                            }
                        })
                    }else if(i==1){
                        btn2.setText(resourceList.get(resourceIndex).name)
                        if(currentAdType==(resourceList.get(resourceIndex).id)){
                            btn2.isActivated=true
                            btnAdCurent=btn2;
                        }
                        btn2.setOnClickListener({
                            btn2.isActivated=!btn2.isActivated
                            //保存 对应的位置
                            if(btn2.isActivated){
                                if(btnAdCurent!=null){
                                    btnAdCurent!!.isActivated=!btnAdCurent!!.isActivated
                                }
                                btnAdCurent=btn2;
                                currentAdType=resourceList.get(index*columnNum+i).id
                            }else{
                                btnAdCurent=null
                                currentAdType=-1
                            }
                        })
                    }
                }
                rl_ad!!.addView(contentView)
            }else{
                SystemUtil.printlnStr("resourceList.size-columnNum*(index+1)"+(resourceList.size-columnNum*(index))+"..resourceIndex:"+resourceIndex)
                var contentView=View.inflate(context,R.layout.item_setting,null)
                for(i in 1..(resourceList.size-columnNum*(index))){
                    resourceIndex++
                    var btn1=contentView.findViewById(R.id.btn_1) as Button;
                    var btn2=contentView.findViewById(R.id.btn_2) as Button;
                    if(i==1){
                        btn2.visibility=View.INVISIBLE
                        btn1.setText(resourceList.get(resourceIndex).name)
                        if(currentAdType==(resourceList.get(resourceIndex).id)){
                            btn1.isActivated=true
                            btnAdCurent=btn1;
                        }
                        btn1.setOnClickListener({
                            btn1.isActivated=!btn1.isActivated
                            //保存 对应的位置
                            if(btn1.isActivated){
                                if(btnAdCurent!=null){
                                    btnAdCurent!!.isActivated=!btnAdCurent!!.isActivated
                                }
                                currentAdType=resourceList.get(index*columnNum+(i-1)).id
                            }else{
                                btnAdCurent=null
                                currentAdType=-1
                            }
                        })
                    }else if(i==2){
                        btn2.visibility=View.VISIBLE

                        if(currentAdType==(resourceList.get(resourceIndex).id)){
                            btn2.isActivated=true
                            btnAdCurent=btn2;
                        }
                        btn2.setText(resourceList.get(resourceIndex).name)
                        btn2.setOnClickListener({
                            btn2.isActivated=!btn2.isActivated
                            //保存 对应的位置
                            if(btn2.isActivated){
                                if(btnAdCurent!=null){
                                    btnAdCurent!!.isActivated=!btnAdCurent!!.isActivated
                                }
                                currentAdType=resourceList.get(index*columnNum+(i-1)).id
                            }else{
                                btnAdCurent=null
                                currentAdType=-1
                            }
                        })
                    }
                }
                rl_ad!!.addView(contentView)
            }
        }
    }

    /**
     * 设置关键字
     */
    fun setKeyword(isShow: Boolean) {
        if(isShow){
             tv_search_type_title!!.visibility=View.VISIBLE
             rl_search_type!!.visibility=View.VISIBLE
             view_line1!!.visibility=View.VISIBLE
        }else{
            tv_search_type_title!!.visibility=View.GONE
            rl_search_type!!.visibility=View.GONE
            view_line1!!.visibility=View.GONE
        }
    }
}