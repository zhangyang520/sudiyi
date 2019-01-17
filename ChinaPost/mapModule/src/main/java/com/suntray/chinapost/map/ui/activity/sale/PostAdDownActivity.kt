package com.suntray.chinapost.map.ui.activity.sale

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.suntray.chinapost.baselibrary.ui.activity.BaseAcvitiy
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.ResourceAd
import com.suntray.chinapost.map.data.request.FindResourceReportRequest
import com.suntray.chinapost.map.data.response.AdDownResponse
import com.suntray.chinapost.map.injection.component.DaggerResourceComponent
import com.suntray.chinapost.map.presenter.ResourcePresenter
import com.suntray.chinapost.map.presenter.view.ResourceView
import com.suntray.chinapost.provider.RouterPath
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_ad_down.*
import kotlinx.android.synthetic.main.item_bus_result.*
import kotlinx.android.synthetic.main.recylerview_ad_down.*

/**
 *  下刊报告界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/14 11:03
 */
@Route(path = RouterPath.MapModule.POST_AD_DOWN)
class PostAdDownActivity:BaseMvpActivity<ResourcePresenter>(),ResourceView{


    var resourceAd:ResourceAd?=null
    override fun injectCompontent() {
        DaggerResourceComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun onGetAdDownReportList(adDownResponse: AdDownResponse) {
        super.onGetAdDownReportList(adDownResponse)

        //获取到数据 进行展示
        tv_city.text=adDownResponse.cityName
        tv_dot_location.text=adDownResponse.pointName
        tv_location.text=adDownResponse.equLocation
        tv_type.text=adDownResponse.equSpecify
        tv_up_date.text=resourceAd!!.uploadDate
        tv_ad_type.text="柜体广告"
        //进行循环 增加图片
        if(adDownResponse.imageList!=null){
            for (data in adDownResponse.imageList!!) {
                var imageView=ImageView(this@PostAdDownActivity)
                var layoutParams= ViewGroup.MarginLayoutParams(AutoUtils.getPercentWidthSize(750), AutoUtils.getPercentHeightSize(460))
                layoutParams.setMargins(0,AutoUtils.getPercentHeightSize(50),0,0)
                imageView.layoutParams=layoutParams
                imageView.scaleType=ImageView.ScaleType.FIT_CENTER
                Glide.with(this@PostAdDownActivity).load(data.imgPath).into(imageView)
                //.error(R.drawable.map_iv_default)
                ll_content.addView(imageView)
            }
        }
    }
    override fun initView() {
        isRightShow=false
        isTitleShow=true
        isBlackShow=true

        var title=intent.getStringExtra("title")
        viewtitle=title

        resourceAd=intent.getSerializableExtra("ad") as ResourceAd
        if(resourceAd==null){
            finish()
            return
        }
        if(resourceAd!!.taskType==1){
            tv_date.text="上刊日期"
        }else{
            tv_date.text="下刊日期"
        }

        basePresenter.getResourceReportRequest(FindResourceReportRequest(resourceAd!!.id))
    }

    override fun getView(): View {
        return View.inflate(this@PostAdDownActivity, R.layout.activity_ad_down,null);
    }
}