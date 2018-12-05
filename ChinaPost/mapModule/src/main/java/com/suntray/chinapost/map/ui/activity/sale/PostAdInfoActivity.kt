package com.suntray.chinapost.map.ui.activity.sale

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.ResourceDotLocation
import com.suntray.chinapost.map.injection.component.DaggerResourceComponent
import com.suntray.chinapost.map.presenter.DotPresenter
import com.suntray.chinapost.map.presenter.view.DotView
import com.suntray.chinapost.map.ui.adapter.saler.AdResourceAdapter
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.activity_guanggao_info.*

/**
 *   广告资源位 的界面信息
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/13 17:09
 */
@Route(path =RouterPath.MapModule.POST_AD_INFO)
class PostAdInfoActivity:BaseMvpActivity<DotPresenter>(),DotView{

    var adResourceAdapter: AdResourceAdapter?=null
    var id=-1
    override fun initView() {
        isTitleShow=true
        isBlackShow=true
        viewtitle="广告资源位"
        id=intent.getIntExtra("id",-1);
        hud2= KProgressHUD(this@PostAdInfoActivity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("广告资源位数据获取中...")
        basePresenter.getDotOfResourceInfo(id)
    }

    override fun injectCompontent() {
      DaggerResourceComponent.builder().activityComponent(activityComponent).build().bind(this)
      basePresenter.baseView=this
    }

    override fun onGetDotOfResourceList(resourceDotLocation: ResourceDotLocation) {
        super.onGetDotOfResourceList(resourceDotLocation)
        if(resourceDotLocation!=null){
            adResourceAdapter= AdResourceAdapter(resourceDotLocation, this@PostAdInfoActivity)
            var layoutManager=LinearLayoutManager(this@PostAdInfoActivity)
            layoutManager.orientation=LinearLayoutManager.VERTICAL

            rc_guanggao.layoutManager=layoutManager
            rc_guanggao.adapter=adResourceAdapter;
        }

    }

    override fun getView(): View {
        return View.inflate(this@PostAdInfoActivity, R.layout.activity_guanggao_info,null)
    }


    override fun onRestart() {
        super.onRestart()
        basePresenter.getDotOfResourceInfo(id)
    }
    /**
     * 处理业务
     */
   override fun processBussiness(){
   }
}