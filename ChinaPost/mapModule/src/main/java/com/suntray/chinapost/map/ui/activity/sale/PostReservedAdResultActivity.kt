package com.suntray.chinapost.map.ui.activity.sale

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.response.OneKeyReservedResponse
import com.suntray.chinapost.map.injection.component.DaggerResourceComponent
import com.suntray.chinapost.map.presenter.ResourcePresenter
import com.suntray.chinapost.map.presenter.view.ResourceView
import com.suntray.chinapost.map.ui.adapter.saler.AdReservedResultAdapter
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.activity_reserved_result.*

/**
 *   预定结果展示!
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/23 11:40
 */
@Route(path = RouterPath.MapModule.POST_AD_RESERVED_RESULT)
class PostReservedAdResultActivity:BaseMvpActivity<ResourcePresenter>(),ResourceView{

    override fun injectCompontent() {
        DaggerResourceComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun initView() {
        isBlackShow=true
        isRightShow=false
        isTitleShow=true
        viewtitle="点位预定结果提示"

       var reservedAdResult= intent.getSerializableExtra("reservedAdResult")
       var resourceIdList=intent.getIntegerArrayListExtra("resourceIdList")
       var adType= intent.getIntExtra("adType",-1)
       var clientId=intent.getIntExtra("clientId",-1)
       var startDate= intent.getStringExtra("startDate")
       var endDate= intent.getStringExtra("endDate")

       if(reservedAdResult is OneKeyReservedResponse && reservedAdResult!=null && adType!=-1 && clientId!=-1){
           hud2= KProgressHUD(this@PostReservedAdResultActivity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("确定预定中...")

           tv_success_count.text= reservedAdResult.countSuccess.toString()
           tv_error_count.text=reservedAdResult.countFailure.toString()
           SystemUtil.printlnStr("reservedAdResult:"+reservedAdResult.toString())

           var adReservedResultAdapter=
                   AdReservedResultAdapter(reservedAdResult.data, this@PostReservedAdResultActivity)
           var linearLayoutManager=LinearLayoutManager(this@PostReservedAdResultActivity)
           linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
           recylerView.layoutManager=linearLayoutManager
           recylerView.adapter=adReservedResultAdapter

           btn_cancel.setOnClickListener({
               finish()
           })

           btn_submit_reserved.setOnClickListener({
               if(reservedAdResult.countSuccess>0){
                   //集合循环
                   var idArray= arrayOfNulls<Int>(reservedAdResult.countSuccess)
                   var index=0
                   for(data in reservedAdResult.data!!){
                       if(data.success){
                           //如果成功
                           idArray.set(index,data.point!!.id)
                           index++
                       }
                   }
                   var resourceIdArray:Array<Int?> = arrayOfNulls<Int>(resourceIdList.size)
                   for(index in resourceIdList.indices){
                       resourceIdArray.set(index,resourceIdList.get(index))
                   }
                   //提交
                   basePresenter.submitReserve(idArray,resourceIdArray,
                                                                clientId,adType,UserDao.getLocalUser().id,startDate,endDate)
               }else{
                   ToastUtil.makeText(this@PostReservedAdResultActivity,"成功个数为0")
               }
           })
       }
    }


    override fun onSaveReservedSubmit() {
        super.onSaveReservedSubmit()
        ToastUtil.makeText(this@PostReservedAdResultActivity,"预定成功")
        setResult(103)
        finish()
    }


    override fun getView(): View {
        return View.inflate(this@PostReservedAdResultActivity, R.layout.activity_reserved_result,null)
    }

}