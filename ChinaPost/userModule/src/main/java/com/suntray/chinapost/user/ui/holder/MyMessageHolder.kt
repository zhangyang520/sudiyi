package com.suntray.chinapost.user.ui.holder

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.baselibrary.utils.UiUtils
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineMessage

/**
 *  我的客户的holder
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 9:32
 */
class MyMessageHolder:BaseHolder<MineMessage>(){

    var iv_prewarning:ImageView?=null
    var tv_prewarning_title:TextView?=null
    var tv_prewarning_date:TextView?=null
    var tv_prewarning_content:TextView?=null

    override fun initView(): View {
        var itemView=View.inflate(UiUtils.instance.getContext(),R.layout.recylerview_message_prewarning,null)
        iv_prewarning=itemView!!.findViewById(R.id.iv_prewarning) as ImageView;
        tv_prewarning_title=itemView!!.findViewById(R.id.tv_prewarning_title) as TextView;
        tv_prewarning_date=itemView!!.findViewById(R.id.tv_prewarning_date) as TextView;
        tv_prewarning_content=itemView!!.findViewById(R.id.tv_prewarning_content) as TextView;
        return itemView;
    }

    override fun refreshView(data: MineMessage?,activity:Activity) {

        /**
         * 待审核 #FDA100
           已审核 00754b
         */
        if(data!!.type==1){
            iv_prewarning!!.setImageResource(R.drawable.mine_iv_examine)
        }else{
            iv_prewarning!!.setImageResource(R.drawable.mine_iv_prewarning)
        }

        if(data!!.isnew==0){
            //为查看 #A3A3A3
            tv_prewarning_title!!.setTextColor(Color.BLACK)
        }else{
            //以查看 #A3A3A3
            tv_prewarning_title!!.setTextColor(Color.parseColor("#A3A3A3"))
        }
        tv_prewarning_title!!.text=data!!.title
        tv_prewarning_date!!.text=data!!.senddate
        tv_prewarning_content!!.text=data!!.content

        contentView.setOnClickListener({
            //点击事件的跳转
            ARouter.getInstance().build(RouterPath.MineModule.MINE_EXAMINE_ENDORSE).withSerializable("message",data).navigation()
        })
    }
}