package com.suntray.chinapost.map.ui.adapter.proxy

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.ui.activity.proxy.TaskDetailUnfinishActivity
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.AptitudeInfo
import com.suntray.chinapost.user.data.bean.TaskUpload
import com.suntray.chinapost.user.data.enum.UploadAptitudeEnum
import com.suntray.chinapost.user.data.enum.UploadTaskEnum
import com.suntray.chinapost.user.ui.dialog.ImageInfoDialog
import com.zhy.autolayout.AutoRelativeLayout
import com.zhy.autolayout.utils.AutoUtils
import java.io.File
import java.util.*


/**
 * 多图上传,动态添加图片适配器
 */
class TaskUploadUnfinishImageAdapter(private val context: Context, var imagePathList: ArrayList<TaskUpload?>?) : BaseAdapter() {
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
    /**
     * 新增的信息  进行更新
     */
    fun  newAddUpdate(imagePath: TaskUpload??){
        SystemUtil.printlnStr(" this.imagePathList hs"+ this.imagePathList!!.hashCode()+"..enum:"+uploadAptitudeEnum!!.imageList.hashCode())
        this.imagePathList!!.add(0,imagePath!!)
        SystemUtil.printlnStr("update list size: this.imagePathList:"+imagePathList!!.size)
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
        SystemUtil.printlnStr("update list size: this.imagePathList:"+imagePathList!!.size)
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
            viewHolder = ViewHolder(View.inflate(context, R.layout.item_upload_aptitude, null))
            convertView = viewHolder.itemView
            val width = AutoUtils.getPercentWidthSize(184)
            imageHeight = width
            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }
        if (getItem(position)!!.imgPath == null || getItem(position)!!.imgPath.equals("")) {//图片地址为空时设置默认图片
            viewHolder.iv_1.setImageResource(R.drawable.mine_ic_default3)
            Glide.with(context).load(R.drawable.mine_ic_default3).into(viewHolder.iv_1)
            viewHolder.iv_cancel1.visibility = View.GONE

            viewHolder.iv_1.setOnClickListener({
                (context as TaskDetailUnfinishActivity).setPortraitDialog()
            })
        } else {
            if(isCancelable){
                viewHolder.iv_cancel1.visibility = View.VISIBLE
            }else{
                viewHolder.iv_cancel1.visibility = View.GONE
            }

            viewHolder.iv_1!!.setOnClickListener({
                if(getItem(position)!!.imgPath!=null && !getItem(position)!!.imgPath.equals("")){
                    var imageDialog= com.suntray.chinapost.map.ui.dialog.ImageInfoDialog(context)
                    imageDialog.show()
                    imageDialog.setContent(getItem(position)!!.imgPath)
                }else{
                    com.suntray.chinapost.map.utils.ToastUtil.show(context,"暂无图片信息")
                }
            })

            viewHolder.iv_cancel1.setOnClickListener {
                //删除 对应的图片
                //更新结构
               var aptitudeInfo= imagePathList!!.get(position)
                if(uploadAptitudeEnum!!.imageList.contains(aptitudeInfo)&&
                        !(!aptitudeInfo!!.imgPath.startsWith("http") || aptitudeInfo.id==0)){
                    //如果删除的对象 包含在 原有的集合中
                    if(imagePathList!!.size==3 && !(!imagePathList!!.get(2)!!.imgPath.startsWith("http") || imagePathList!!.get(2)!!.id==0)) {
                        //如果为3 且最后一个 不是空的
                        imagePathList!!.removeAt(position)
                        imagePathList!!.add(TaskUpload())
                        uploadAptitudeEnum!!.deleteList.add(aptitudeInfo.id!!)
                    }else{
                        imagePathList!!.removeAt(position)
                        uploadAptitudeEnum!!.deleteList.add(aptitudeInfo.id!!)
                    }

                    SystemUtil.printlnStr(uploadAptitudeEnum!!.yingyePathId+"..delete size:"+uploadAptitudeEnum!!.deleteList.size)
                }else{
                    if(imagePathList!!.size==3 && (!imagePathList!!.get(2)!!.imgPath.startsWith("http") || imagePathList!!.get(2)!!.id==0) && position==2) {
                        uploadAptitudeEnum!!.newAddList.remove(aptitudeInfo)
                        imagePathList!!.removeAt(position)
                        imagePathList!!.add(TaskUpload())
                    }else{
                        uploadAptitudeEnum!!.newAddList.remove(aptitudeInfo)
                        imagePathList!!.removeAt(position)
                    }
                }
                update(imagePathList)
            }
            Glide.with(context).load(getItem(position)!!.imgPath).error(R.drawable.mine_ic_default3).into(viewHolder.iv_1)
        }
        return convertView
    }

    internal inner class ViewHolder(var itemView: View) {
        var iv_1: ImageView
        var iv_cancel1: View

        init {
            AutoUtils.auto(itemView)
            iv_1 = itemView.findViewById(R.id.iv_1) as ImageView
            iv_cancel1 = itemView.findViewById(R.id.iv_cancel1)
        }
    }
}
