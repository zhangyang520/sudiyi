package com.suntray.chinapost.map.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.suntray.chinapost.map.R
import com.zhy.autolayout.utils.AutoUtils

/**
 *   点位预定的弹框
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/16 10:05
 */
class ImageInfoDialog:Dialog{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, themeResId: Int) : super(context, themeResId)
    constructor(context: Context?, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener)

    var imageView:ImageView?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var view=View.inflate(context, R.layout.dialog_img_info,null);
        imageView=view.findViewById(R.id.iv_info) as ImageView
        AutoUtils.autoSize(view);
        setContentView(view)

        setCancelable(true)
        view.layoutParams.height=AutoUtils.getPercentHeightSize(800)
        view.layoutParams.width=AutoUtils.getPercentWidthSize(750)
        view.requestLayout()
    }

    /**
     * 设置 图片的url
     */
    fun setContent(imgUrl:String){
        Glide.with(context).load(imgUrl).into(imageView)
    }
}