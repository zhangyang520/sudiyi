package com.suntray.chinapost.map.ui.activity.sale

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.ui.activity.BaseAcvitiy
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.map.ui.adapter.saler.AdReservedRecylerAdapter
import com.suntray.chinapost.map.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.activity_ad_reserved_list.*

/**
 *  预定点位 清单列表
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/14 13:37
 */
@Route(path = RouterPath.MapModule.POST_AD_RESERVED_LIST)
class PostAdReservedListActivity:BaseAcvitiy(){

    var mapDotList:ArrayList<MapDot>?=null
    var adReservedRecylerAdapter: AdReservedRecylerAdapter?=null

    companion object {
        var selectedList= ArrayList<MapDot>()
    }
    override fun initView() {
        mapDotList= PostPoiSearchActivity.currentMapDot
        isBlackShow=true
        isTitleShow=true
        isRightShow=true
        viewtitle="预定点位清单"
        rightTitle="全选"
        selectedList.clear()
        //跳转到 点位一键预定
        rl_bottom.setOnClickListener({

           SystemUtil.printlnStr("index:"+adReservedRecylerAdapter!!.selectPositionList.toString())
            if(adReservedRecylerAdapter!!.selectPositionList.size>0){
                selectedList.clear()
                for(a in adReservedRecylerAdapter!!.selectPositionList){
                    //获取数值
                    selectedList.add(adReservedRecylerAdapter!!.mapDotList!!.get(a))
                }
                ARouter.getInstance()
                         .build(RouterPath.MapModule.POST_AD_ONE_KEY_RESERVED)
                                             .navigation(this@PostAdReservedListActivity,100)
            }else{
                ToastUtil.show(this@PostAdReservedListActivity,"请选择点位")
            }
        })

        var linearLayoutManager=LinearLayoutManager(this@PostAdReservedListActivity)
        linearLayoutManager.orientation=LinearLayoutManager.VERTICAL
        recylerview.layoutManager=linearLayoutManager
        adReservedRecylerAdapter= AdReservedRecylerAdapter(mapDotList, this@PostAdReservedListActivity)
        recylerview.adapter=adReservedRecylerAdapter
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100 && resultCode==104){
            finish()
        }
    }


    override fun getView(): View {
        return View.inflate(this@PostAdReservedListActivity, R.layout.activity_ad_reserved_list,null);
    }


    override fun rightTitleClick() {
        super.rightTitleClick()
        if(viewtitle.equals("全选")){
            adReservedRecylerAdapter!!.processAllSelect()
            tv_reserved_number.setText(adReservedRecylerAdapter!!.selectPositionList.size.toString())
            setRight("取消")
        }else{
            adReservedRecylerAdapter!!.processAntiAllSelect()
            tv_reserved_number.setText(adReservedRecylerAdapter!!.selectPositionList.size.toString())
            setRight("全选")
        }
    }

    fun setSelectNumer(number:Int){
        tv_reserved_number.setText(number.toString())
    }
}