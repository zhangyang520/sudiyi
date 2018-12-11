package com.suntray.chinapost.map.ui.adapter.proxy

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import com.zhy.autolayout.utils.AutoUtils

/**
 *  任务的列表的 适配器
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/24 9:32
 */
class TaskAdapter:RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    var context:Context?=null
    var taskList= arrayListOf<TaskEntity>()
    var currentType:Int=0
    constructor(context: Context?, taskList: ArrayList<TaskEntity>) : super() {
        this.context = context
        this.taskList = taskList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(View.inflate(context, R.layout.recylerview_tasklist,null));
    }

    override fun getItemCount(): Int {
        if(taskList!=null){
            return taskList.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder!!.tv_client_name_value!!.setText(taskList.get(position).supplyName)
        holder!!.tv_position!!.setText(taskList.get(position).zoneaddress)
        holder!!.tv_task_area_value!!.setText(taskList.get(position).taskArea)
        holder!!.tv_finish_time_value!!.setText(taskList.get(position).taskTime)
        holder!!.tv_dot_name_value!!.setText(taskList.get(position).pointName)

        /**
         * 条目设置点击事件
         */
        holder.itemView.setOnClickListener({
            ARouter.getInstance().build(RouterPath.MapModule.POST_TASK_DETAIL)
                        .withBoolean("editAble",false)
                        .withInt("currentType",currentType).navigation()
        })

        holder.btn_upload_pic!!.setOnClickListener({
            ARouter.getInstance().build(RouterPath.MapModule.POST_TASK_DETAIL)
                    .withBoolean("editAble",true)
                    .withInt("currentType",currentType).navigation()
        })

        /**
         * 查看地图
         */
        holder.btn_look_map!!.setOnClickListener({
            //跳转到对应的界面
            ToastUtil.show(context,"地图界面!")
            ARouter.getInstance().build(RouterPath.MapModule.POST_TASK_ROUTE).navigation()
        })
    }

    inner class TaskViewHolder: RecyclerView.ViewHolder{

        var tv_position:TextView?=null
        var tv_client_name_value:TextView?=null
        var tv_task_area_value:TextView?=null
        var tv_finish_time_value:TextView?=null
        var tv_dot_name_value:TextView?=null
        var btn_look_map:Button?=null
        var btn_upload_pic:Button?=null

        constructor(itemView: View?) : super(itemView!!){
            AutoUtils.autoSize(itemView)
            tv_position=itemView!!.findViewById(R.id.tv_position) as TextView
            tv_client_name_value=itemView!!.findViewById(R.id.tv_client_name_value) as TextView
            tv_task_area_value=itemView!!.findViewById(R.id.tv_task_area_value) as TextView
            tv_finish_time_value=itemView!!.findViewById(R.id.tv_finish_time_value) as TextView
            tv_dot_name_value=itemView!!.findViewById(R.id.tv_dot_name_value) as TextView

            btn_look_map=itemView!!.findViewById(R.id.btn_look_map) as Button
            btn_upload_pic=itemView!!.findViewById(R.id.btn_upload_pic) as Button
        }
    }
}