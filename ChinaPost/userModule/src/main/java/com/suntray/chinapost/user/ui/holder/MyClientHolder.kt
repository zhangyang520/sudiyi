package com.suntray.chinapost.user.ui.holder

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.data.dao.ClientDictDao
import com.suntray.chinapost.baselibrary.ui.refreshView.inner.BaseHolder
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.baselibrary.utils.UiUtils
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.ui.activity.MineClientActivity

/**
 *  我的客户的holder
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 9:32
 */
class MyClientHolder:BaseHolder<MineClient>(){

    var mineClientActivity:MineClientActivity?=null
    var tv_ad_name: TextView?=null
    var tv_mark: TextView?=null
    var tv_ad_state: TextView?=null
    var tv_state: TextView?=null
    var btn_chakan: Button?=null
    var btn_edit:Button?=null
    override fun initView(): View {
        var itemView=View.inflate(UiUtils.instance.getContext(),R.layout.recylerview_mine_client,null)
        tv_ad_name=itemView!!.findViewById(R.id.tv_ad_name) as TextView;
        tv_mark=itemView!!.findViewById(R.id.tv_mark) as TextView;
        tv_ad_state=itemView!!.findViewById(R.id.tv_ad_state) as TextView;
        tv_state=itemView!!.findViewById(R.id.tv_state) as TextView;

        btn_chakan=itemView!!.findViewById(R.id.btn_chakan) as Button;
        btn_edit=itemView!!.findViewById(R.id.btn_edit) as Button;
        return itemView;
    }

    override fun refreshView(data: MineClient?,activity: Activity) {

        /**
         * 待审核 #FDA100
        已审核 00754b
         */
        tv_ad_name!!.text=data!!.name
        try {
            tv_mark!!.text=ClientDictDao.getClient(data.profession).name
        } catch (e: Exception) {
            tv_mark!!.text="暂无"
        }

        if(data.statename.equals("待审核")){
            tv_ad_state!!.setTextColor(activity.resources.getColor(R.color.orange))
        }else if(data.statename.equals("审核不通过")){
            tv_ad_state!!.setTextColor(activity.resources.getColor(R.color.red))
        }else{
            tv_ad_state!!.setTextColor(Color.parseColor("#00754b"))
        }
        tv_ad_state!!.text=data!!.statename
        tv_state!!.text=data!!.stagename

        btn_edit!!.setOnClickListener({
            //意向客户  待审批  能够
            //意向客户  审批不通过 能够
            if((data.stagename!=null && data.stagename.equals("意向客户")
                       && data.statename!=null && data.statename.equals("待审核"))
                    ||(data.stagename!=null && data.stagename.equals("意向客户")
                            && data.statename!=null && data.statename.equals("审核不通过"))){
                //如果状态已经变为 已下单客户
                ARouter.getInstance().build(RouterPath.MineModule.MINE_ADD_CLIENT).withSerializable("client",data).navigation(activity,100)
            }else{
                ToastUtil.makeText(activity,"不允许编辑")
            }
        })

        btn_chakan!!.setOnClickListener({
            //上传资质！
            if(!((data.stagename!=null && data.stagename.equals("意向客户") &&
                                            data.statename!=null && data.statename.equals("待审核"))||(
                            data.stagename!=null && data.stagename.equals("意向客户") &&
                                                data.statename!=null && data.statename.equals("审核不通过")))){
                ARouter.getInstance().build(RouterPath.MineModule.MINE_UPLOAD_APTITUDE).withInt("clientId",data.id).navigation(activity,100)
            }else{
                ToastUtil.makeText(activity,"不允许上传资质")
            }
        })

//        /**
//         * 设置点击事件
//         */
        contentView.setOnClickListener({
            //跳转到 详情页
            ARouter.getInstance().build(RouterPath.MineModule.MINE_CLIENT_DETAIL).withSerializable("client",data).navigation(activity,100)
        })
    }
}