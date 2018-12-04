package com.suntray.chinapost.map.ui.activity.sale

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.ui.activity.BaseAcvitiy
import com.suntray.chinapost.map.R
import com.suntray.chinapost.provider.RouterPath

/**
 *  下刊报告界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/14 11:03
 */
@Route(path = RouterPath.MapModule.POST_AD_DOWN)
class PostAdDownActivity:BaseAcvitiy(){

    override fun initView() {
        isRightShow=false
        isTitleShow=true
        isBlackShow=true
        viewtitle="下刊报告"
    }

    override fun getView(): View {
        return View.inflate(this@PostAdDownActivity, R.layout.activity_ad_down,null);
    }
}