package com.suntray.chinapost.map.ui.adapter.proxy

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.map.ui.activity.proxy.TaskDetailActivity
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.TaskUpload
import com.suntray.chinapost.user.data.enum.UploadTaskEnum
import com.zhy.autolayout.AutoRelativeLayout
import com.zhy.autolayout.utils.AutoUtils
import java.io.File
import java.util.*


/**
 * 多图上传,动态添加图片适配器
 */
class TaskUploadImageAdapter(private val context: Context, var imagePathList: ArrayList<TaskUpload?>?) : BaseAdapter() {
    private val isAddData = false
    /**
     * 控制最多上传的图片数量
     */
    var imageNumber = 3
    var gridView: GridView? = null
    private var imageHeight: Int = 0//图片的高度

    //所属的对应的 枚举类
    var uploadAptitudeEnum: UploadTaskEnum?=null
    var  isCancelable=true
    var editPosition=-1
    /**
     * 新增的信息  进行更新
     */
    fun  newAddUpdate(imagePath: TaskUpload??){
        this.imagePathList!!.add(imagePathList!!.size-1,imagePath!!)
        //这里控制选择的图片放到前面,默认的图片放到最后面,
        //集合中的总数量等于上传图片的数量加上默认的图片不能大于imageNumber + 1
        if (imagePathList!!.size == imageNumber + 1) {
            //移除默认的图片
            imagePathList!!.removeAt(imagePathList!!.size - 1)
        }
        //这里判断图片的个数是不是3的倍数值需要<=3
        if (gridView != null && imageNumber / 3 > 1) {
            updateGridViewLayout(imagePathList!!)
        }
        notifyDataSetChanged()
    }


    fun update(imagePathList: ArrayList<TaskUpload?>?) {
        this.imagePathList = imagePathList
        //这里控制选择的图片放到前面,默认的图片放到最后面,
        //集合中的总数量等于上传图片的数量加上默认的图片不能大于imageNumber + 1
        if (imagePathList!!.size == imageNumber + 1) {
            //移除默认的图片
            imagePathList.removeAt(imagePathList.size - 1)
        }
        //这里判断图片的个数是不是3的倍数值需要<=3
        if (gridView != null && imageNumber / 3 > 1) {
            updateGridViewLayout(imagePathList)
        }
        notifyDataSetChanged()
    }

    /**
     * 进行更新gridView的布局
     * @param imagePathList
     */
    private fun updateGridViewLayout(imagePathList: ArrayList<TaskUpload?>) {
        val lineCount = imageNumber / 3
        var index = 1//角标
        for (i in 1..lineCount) {
            if (imagePathList.size <= i * 3) {
                index = i
                break
            } else if (i == lineCount) {
                index = lineCount
            }
        }
        gridView!!.layoutParams.height = index * imageHeight + (index - 1) * AutoUtils.getPercentWidthSize(15)
        gridView!!.requestLayout()
    }

    /**
     * 进行更新"长按"删除
     * @param imagePathList
     */
    fun updateRemove(imagePathList: ArrayList<TaskUpload?>) {
        this.imagePathList = imagePathList
        if (getListContentSize(imagePathList) == imageNumber - 1) {
            imagePathList.add(null)
        }
        if (gridView != null && imageNumber / 3 > 1) {
            updateGridViewLayout(imagePathList)
        }
        notifyDataSetChanged()
    }

    /**
     * 进行获取list集合的长度
     * @param imagePathList
     * @return
     */
    private fun getListContentSize(imagePathList: ArrayList<TaskUpload?>): Int {
        var i = 0
        for (data in imagePathList) {
            if (data != null) {
                i++
            }
        }
        return i
    }

    override fun getCount(): Int {
        println("proImageShow 3333333 size:" + imagePathList!!.size)
        return if (imagePathList == null) 0 else imagePathList!!.size
    }

    override fun getItem(position: Int): TaskUpload? {
        return if (imagePathList == null) null else imagePathList!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val autoRelativeLayout: AutoRelativeLayout
        var viewHolder: ViewHolder? = null
        if (convertView == null) {//创建ImageView
            viewHolder = ViewHolder(View.inflate(context, R.layout.item_upload_task, null))
            convertView = viewHolder.itemView
            val width = AutoUtils.getPercentWidthSize(200)
            imageHeight = width
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }


        if (getItem(position)!!.state==-1) {//没有数据的时候
            Glide.with(context).load(getItem(position)!!.imgPath).error(R.drawable.mine_ic_default3).into(viewHolder.iv_1)
            viewHolder.iv_cancel1.visibility = View.GONE

            //其他的情况 隐藏其他的信息
            viewHolder.tv_approval_content!!.visibility=View.GONE
            viewHolder.tv_approval_reason!!.visibility=View.GONE

            if(!isCancelable){
                //不能编辑
                viewHolder.tv_edit!!.visibility=View.GONE
                viewHolder.iv_1.setOnClickListener(null)
            }else{
                viewHolder.tv_edit!!.visibility=View.VISIBLE
                viewHolder.iv_1!!.setOnClickListener({
                    //编辑 事件
                    editPosition=position
                    (context as TaskDetailActivity)!!.setPermissinPortraitDialog()
                })
            }
        } else {
            viewHolder.itemView.visibility=View.VISIBLE

            println("getItem(position)!!.imgPath:"+getItem(position)!!.imgPath)
            Glide.with(context).load(getItem(position)!!.imgPath).error(R.drawable.mine_ic_default3).into(viewHolder.iv_1)

            //根据 状态值 设置 对应意见的颜色 2待审批、4审批不通过、3,5,6,7审批通过
            if(getItem(position)!!.state==2){
                //待审批
                viewHolder.tv_approval_content!!.setTextColor(Color.parseColor("#277E63"))
                viewHolder.tv_approval_content!!.setText("待审批")
                if(getItem(position)!!.opinion==null || getItem(position)!!.opinion.trim().equals("")){
                    viewHolder.tv_approval_reason!!.setText("审批意见:暂无")
                }else{
                    viewHolder.tv_approval_reason!!.setText("审批意见:"+getItem(position)!!.opinion)
                }
                viewHolder.tv_edit!!.visibility=View.INVISIBLE
                viewHolder.iv_1!!.setOnClickListener(null)
            }else if(getItem(position)!!.state==4){
                //审批不通过
                viewHolder.tv_approval_content!!.setTextColor(Color.RED)
                viewHolder.tv_approval_content!!.setText("审批不通过")
                if(getItem(position)!!.opinion==null || getItem(position)!!.opinion.trim().equals("")){
                    viewHolder.tv_approval_reason!!.setText("审批意见:暂无")
                }else{
                    viewHolder.tv_approval_reason!!.setText("审批意见:"+getItem(position)!!.opinion)
                }
                println("/审批不通过 viewHolder.tv_edit!!.setOnClickListener")

                if(!isCancelable){
                    //不能编辑
                    viewHolder.tv_edit!!.visibility=View.GONE
                    viewHolder.iv_1!!.setOnClickListener(null)
                }else{
                    viewHolder.tv_edit!!.visibility=View.VISIBLE
                    viewHolder.iv_1!!.setOnClickListener({
                        //编辑 事件
                        editPosition=position
                        (context as TaskDetailActivity)!!.setPermissinPortraitDialog()
                    })
                }
            }else if(getItem(position)!!.state==3 || getItem(position)!!.state==5
                           || getItem(position)!!.state==6|| getItem(position)!!.state==7){
                //审批通过
                viewHolder.tv_approval_content!!.setTextColor(Color.GRAY)
                viewHolder.tv_approval_content!!.setText("审批通过")
                viewHolder.tv_edit!!.visibility=View.INVISIBLE
                if(getItem(position)!!.opinion==null || getItem(position)!!.opinion.trim().equals("")){
                    viewHolder.tv_approval_reason!!.setText("审批意见:暂无")
                }else{
                    viewHolder.tv_approval_reason!!.setText("审批意见:"+getItem(position)!!.opinion)
                }
                viewHolder.iv_1!!.setOnClickListener(null)
            }else{
                //其他的情况 隐藏其他的信息
                viewHolder.tv_approval_content!!.visibility=View.GONE
                viewHolder.tv_approval_reason!!.visibility=View.GONE

                if(!isCancelable){
                    //不能编辑
                    viewHolder.tv_edit!!.visibility=View.GONE
                    viewHolder.iv_1!!.setOnClickListener(null)
                }else{
                    viewHolder.tv_edit!!.visibility=View.VISIBLE
                    viewHolder.iv_1!!.setOnClickListener({
                        //编辑 事件
                        editPosition=position
                        (context as TaskDetailActivity)!!.setPermissinPortraitDialog()
                    })
                }
            }
        }
        return convertView
    }

    internal inner class ViewHolder(var itemView: View) {
        var iv_1: ImageView
        var iv_cancel1: View
        var tv_approval_content:TextView?=null //审批状态
        var tv_approval_reason:TextView?=null //审批意见
        var tv_edit:Button?=null //编辑按钮

        init {
            AutoUtils.auto(itemView)
            iv_1 = itemView.findViewById(R.id.iv_1) as ImageView
            iv_cancel1 = itemView.findViewById(R.id.iv_cancel1)
            tv_approval_content = itemView.findViewById(R.id.tv_approval_content) as TextView
            tv_approval_reason = itemView.findViewById(R.id.tv_approval_reason) as TextView
            tv_edit = itemView.findViewById(R.id.tv_edit) as Button
        }
    }
}
