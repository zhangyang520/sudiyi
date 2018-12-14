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
import kotlinx.android.synthetic.main.item_client_ad_layout.*
import kotlinx.android.synthetic.main.item_start_end_time_layout.*

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

    //所选择的 成功个数
    var selectedIndexList= arrayListOf<Int>()

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
       var clientName=intent.getStringExtra("clientName")

       if(reservedAdResult is OneKeyReservedResponse && reservedAdResult!=null && adType!=-1 && clientId!=-1){
           hud2= KProgressHUD(this@PostReservedAdResultActivity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("确定预定中...")

           tv_success_count.text= reservedAdResult.countSuccess.toString()
           tv_error_count.text=reservedAdResult.countFailure.toString()
           SystemUtil.printlnStr("reservedAdResult:"+reservedAdResult.toString())

           var adReservedResultAdapter=
                   AdReservedResultAdapter(reservedAdResult.data, this@PostReservedAdResultActivity,selectedIndexList)
           var linearLayoutManager=LinearLayoutManager(this@PostReservedAdResultActivity)
           linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
           recylerView.layoutManager=linearLayoutManager
           recylerView.adapter=adReservedResultAdapter

           btn_cancel.setOnClickListener({
               finish()
           })

           btn_submit_reserved.setOnClickListener({
               if(reservedAdResult.countSuccess>0){

//                   if(selectedIndexList.size>0){
                       //集合循环
                       var idArray= arrayOfNulls<Int>(selectedIndexList.size)
                       var resourceIdArray:Array<Int?> = arrayOfNulls<Int>(resourceIdList.size)
                       var index=0
                       for( i in selectedIndexList.indices){
                           //直接从 选择的集合中遍历
                           idArray.set(index,reservedAdResult.data!!.get(selectedIndexList.get(i)).point!!.id)
                           index++
                       }

                       for(j in resourceIdList.indices){
                           resourceIdArray.set(j,resourceIdList.get(j))
                       }


                       if(UserDao.getLocalUser().userRole==4){
                           //销售人员
                           //提交
                           basePresenter.submitReserve(idArray,resourceIdArray,
                                   clientId,adType,UserDao.getLocalUser().id,startDate,endDate,"",4)

                       }else if(UserDao.getLocalUser().userRole==2){
                           //代理商
                           //提交
                           basePresenter.submitReserve(idArray,resourceIdArray,
                                   clientId,adType,UserDao.getLocalUser().id,startDate,endDate,clientName,2)
                       }else{
                           ToastUtil.makeText(this@PostReservedAdResultActivity,"其他类型")
                       }
//                   }else{
//                       ToastUtil.makeText(this@PostReservedAdResultActivity,"请选择!")
//                   }
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