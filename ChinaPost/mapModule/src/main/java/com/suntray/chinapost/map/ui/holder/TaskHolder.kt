package com.suntray.chinapost.map.ui.holder

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.common.BaseApplication.Companion.context
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.utils.UiUtils
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import com.zhy.autolayout.utils.AutoUtils

/**
 *  任务的相关的holder
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/25 9:51
 */
class TaskHolder: BaseHolder<TaskEntity>{
    var currentType:Int=0
    var tv_client_name_value: TextView?=null
    var tv_task_area_value: TextView?=null
    var tv_finish_time_value: TextView?=null
    var tv_dot_name_value: TextView?=null
    var btn_look_map: Button?=null
    var btn_upload_pic: Button?=null
    var firstType:Int=0
    constructor(currentType: Int,firstType:Int) : super() {
        this.currentType = currentType
        this.firstType=firstType
    }


    override fun initView(): View {
        var itemView=View.inflate(context, R.layout.recylerview_tasklist,null)
        AutoUtils.autoSize(itemView)
        tv_client_name_value=itemView!!.findViewById(R.id.tv_client_name_value) as TextView
        tv_task_area_value=itemView!!.findViewById(R.id.tv_task_area_value) as TextView
        tv_finish_time_value=itemView!!.findViewById(R.id.tv_finish_time_value) as TextView
        tv_dot_name_value=itemView!!.findViewById(R.id.tv_dot_name_value) as TextView

        btn_look_map=itemView!!.findViewById(R.id.btn_look_map) as Button
        btn_upload_pic=itemView!!.findViewById(R.id.btn_upload_pic) as Button
        return itemView;
    }

    override fun refreshView(data: TaskEntity?, activity: Activity?) {
        tv_client_name_value!!.setText(data!!.supplyName)
        tv_task_area_value!!.setText(data!!.taskArea)
        tv_finish_time_value!!.setText(data!!.taskTime)
        tv_dot_name_value!!.setText(data!!.pointName)

        /**
         * 条目设置点击事件
         */
       contentView.setOnClickListener({
            ARouter.getInstance().build(RouterPath.MapModule.POST_TASK_DETAIL)
                    .withBoolean("editAble",false)
                    .withInt("currentType",currentType)
                    .withInt("firstType",firstType).navigation()
        })

        btn_upload_pic!!.setOnClickListener({
            ARouter.getInstance().build(RouterPath.MapModule.POST_TASK_DETAIL)
                    .withBoolean("editAble",true)
                    .withInt("currentType",currentType)
                    .withInt("firstType",firstType).navigation()
        })

        /**
         * 查看地图
         */
        btn_look_map!!.setOnClickListener({
            //跳转到对应的界面
            ToastUtil.show(context,"地图界面!")
            ARouter.getInstance().build(RouterPath.MapModule.POST_TASK_ROUTE).navigation()
        })

    }
}