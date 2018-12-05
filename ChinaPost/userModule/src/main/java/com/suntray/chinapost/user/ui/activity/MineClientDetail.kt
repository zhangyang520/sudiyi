package com.suntray.chinapost.user.ui.activity

import android.view.View
import android.widget.AdapterView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.AptitudeInfo
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.enum.UploadAptitudeEnum
import com.suntray.chinapost.user.data.request.GetAllUploadAptitudeRequest
import com.suntray.chinapost.user.data.response.GetAllAptitudeInfoResponse
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.ClientPresenter
import com.suntray.chinapost.user.presenter.view.ClientView
import com.suntray.chinapost.user.ui.adapter.UploadImageAdapter
import kotlinx.android.synthetic.main.activity_client_detail.*
import kotlinx.android.synthetic.main.item_client_hangye_layout_show.*
import kotlinx.android.synthetic.main.item_client_info_layout_show.*
import kotlinx.android.synthetic.main.upload_aptitude_view.*

/**
 *   客户的详情页
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/30 13:47
 */
@Route(path = RouterPath.MineModule.MINE_CLIENT_DETAIL)
class MineClientDetail :BaseMvpActivity<ClientPresenter>(),ClientView{


    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }


    var uploadaptitudeenum: UploadAptitudeEnum?=null
    var yinyeAdapter: UploadImageAdapter?=null
    var tradeAdapter: UploadImageAdapter?=null
    var foodAdapter: UploadImageAdapter?=null
    var portraitAdpater: UploadImageAdapter?=null

    //域名
    var hostAdpater: UploadImageAdapter?=null
    var zCodeAdpater: UploadImageAdapter?=null
    var clienMianzeAdapter: UploadImageAdapter?=null
    var expressAdapter: UploadImageAdapter?=null
    var dichanxiaoshouAdapter: UploadImageAdapter?=null
    var otherAdapter: UploadImageAdapter?= null


    /**
     * 获取 所有的资质信息
     */
    var id=-1
    override fun onGetAllApatutdInfos(getAllAptitudeInfoResponse: GetAllAptitudeInfoResponse) {
        SystemUtil.printlnStr("getAllAptitudeInfoResponse:"+getAllAptitudeInfoResponse.toString())
        id=getAllAptitudeInfoResponse.id
        if(getAllAptitudeInfoResponse.clientdisclaimereAccList!=null &&
                getAllAptitudeInfoResponse!!.clientdisclaimereAccList!!.size>0){
            UploadAptitudeEnum.ClientMianze.imageList.addAll(0,getAllAptitudeInfoResponse.clientdisclaimereAccList!!)
            //获取到信息时
            getApptitudeInfoUpadate(UploadAptitudeEnum.ClientMianze);
        }

        if(getAllAptitudeInfoResponse.expressdisclaimerAccList!=null &&
                getAllAptitudeInfoResponse!!.expressdisclaimerAccList!!.size>0){
            UploadAptitudeEnum.PostMianze.imageList.addAll(0,getAllAptitudeInfoResponse.expressdisclaimerAccList!!)
            getApptitudeInfoUpadate(UploadAptitudeEnum.PostMianze);
        }

        if(getAllAptitudeInfoResponse.healthAccList!=null &&
                getAllAptitudeInfoResponse!!.healthAccList!!.size>0){
            UploadAptitudeEnum.Food.imageList.addAll(0,getAllAptitudeInfoResponse.healthAccList!!)
            getApptitudeInfoUpadate(UploadAptitudeEnum.Food);

        }

        if(getAllAptitudeInfoResponse.landsaleAccList!=null &&
                getAllAptitudeInfoResponse!!.landsaleAccList!!.size>0){
            UploadAptitudeEnum.DiChanXiaoShouLicense.imageList.addAll(0,getAllAptitudeInfoResponse.landsaleAccList!!)
            getApptitudeInfoUpadate(UploadAptitudeEnum.DiChanXiaoShouLicense);

        }

        if(getAllAptitudeInfoResponse.licenseAccList!=null &&
                getAllAptitudeInfoResponse!!.licenseAccList!!.size>0){
            UploadAptitudeEnum.Yingye.imageList.addAll(0,getAllAptitudeInfoResponse.licenseAccList!!)

            getApptitudeInfoUpadate(UploadAptitudeEnum.Yingye);
        }

        if(getAllAptitudeInfoResponse.otherAccList!=null &&
                getAllAptitudeInfoResponse!!.otherAccList!!.size>0){
            UploadAptitudeEnum.Other.imageList.addAll(0,getAllAptitudeInfoResponse.otherAccList!!)
            getApptitudeInfoUpadate(UploadAptitudeEnum.Other);

        }

        if(getAllAptitudeInfoResponse.portraitAccList!=null &&
                getAllAptitudeInfoResponse!!.portraitAccList!!.size>0){
            UploadAptitudeEnum.Portrait.imageList.addAll(0,getAllAptitudeInfoResponse.portraitAccList!!)
            getApptitudeInfoUpadate(UploadAptitudeEnum.Portrait);

        }

        if(getAllAptitudeInfoResponse.qrcodeAccList!=null &&
                getAllAptitudeInfoResponse!!.qrcodeAccList!!.size>0){
            UploadAptitudeEnum.ZCode.imageList.addAll(0,getAllAptitudeInfoResponse.qrcodeAccList!!)

            getApptitudeInfoUpadate(UploadAptitudeEnum.ZCode);
        }

        if(getAllAptitudeInfoResponse.urlAccList!=null &&
                getAllAptitudeInfoResponse!!.urlAccList!!.size>0){
            UploadAptitudeEnum.Host.imageList.addAll(0,getAllAptitudeInfoResponse.urlAccList!!)
            getApptitudeInfoUpadate(UploadAptitudeEnum.Host);
        }

        if(getAllAptitudeInfoResponse.trademarkAccList!=null &&
                getAllAptitudeInfoResponse!!.trademarkAccList!!.size>0){
            UploadAptitudeEnum.Trade.imageList.addAll(0,getAllAptitudeInfoResponse.trademarkAccList!!)

            getApptitudeInfoUpadate(UploadAptitudeEnum.Trade);
        }
    }


    override fun initView() {
        isRightShow=false;
        isTitleShow=true
        isBlackShow=true
        viewtitle="客户详情信息"

        hud2= KProgressHUD(this@MineClientDetail).setLabel("获取资质信息....").setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        var currentClient=intent.getSerializableExtra("client") as MineClient
        if(currentClient==null){
            finish()
            return  //直接放回
        }
        tv_client_name.setText(currentClient!!.name.toString())
        tv_client_xingming.setText(currentClient!!.linkmanname)
        tv_client_phone.setText(currentClient!!.linkmanphone)
        if(currentClient!!.stage==8){
            tv_yixiang_client.visibility=View.VISIBLE
            iv_yixiang.visibility=View.GONE

            btn_hasxiadan.visibility=View.GONE
            tv_has_client.visibility=View.GONE
        }else if(currentClient!!.stage==9){
            tv_yixiang_client.visibility=View.GONE
            iv_yixiang.visibility=View.GONE

            btn_hasxiadan.visibility=View.GONE
            tv_has_client.visibility=View.VISIBLE
        }

        tv_client_dizhi.setText(currentClient!!.provincename+currentClient!!.cityname+currentClient!!.districtname)
        tv_select_content.setText(currentClient!!.professionname)
        tv_choose_type.setText(currentClient!!.naturename)
        tv_client_laiyuan.setText(currentClient!!.originname)
        tv_address.setText(currentClient!!.address)
        ed_belong.setText(currentClient!!.salesmanname)

        if(currentClient!!.stage==8){
            scrollView.visibility=View.GONE
        }else{
            //初始化数据信息
            UploadAptitudeEnum.Food.yingyePathId=""
            UploadAptitudeEnum.Food.currentNumber=0
            UploadAptitudeEnum.Food.getPathList().clear()
            UploadAptitudeEnum.Food.getPathList().add(AptitudeInfo())
            UploadAptitudeEnum.Food.deleteList.clear()
            UploadAptitudeEnum.Food.newAddList.clear()

            UploadAptitudeEnum.Portrait.yingyePathId=""
            UploadAptitudeEnum.Portrait.currentNumber=0
            UploadAptitudeEnum.Portrait.deleteList.clear()
            UploadAptitudeEnum.Portrait.newAddList.clear()
            UploadAptitudeEnum.Portrait.getPathList().clear()
            UploadAptitudeEnum.Portrait.getPathList().add(AptitudeInfo())


            UploadAptitudeEnum.Trade.yingyePathId=""
            UploadAptitudeEnum.Trade.currentNumber=0
            UploadAptitudeEnum.Trade.getPathList().clear()
            UploadAptitudeEnum.Trade.getPathList().add(AptitudeInfo())
            UploadAptitudeEnum.Trade.deleteList.clear()
            UploadAptitudeEnum.Trade.newAddList.clear()

            UploadAptitudeEnum.Yingye.yingyePathId=""
            UploadAptitudeEnum.Yingye.currentNumber=0
            UploadAptitudeEnum.Yingye.deleteList.clear()
            UploadAptitudeEnum.Yingye.newAddList.clear()
            UploadAptitudeEnum.Yingye.getPathList().clear()
            UploadAptitudeEnum.Yingye.getPathList().add(AptitudeInfo())


            UploadAptitudeEnum.ClientMianze.yingyePathId=""
            UploadAptitudeEnum.ClientMianze.currentNumber=0
            UploadAptitudeEnum.ClientMianze.deleteList.clear()
            UploadAptitudeEnum.ClientMianze.newAddList.clear()
            UploadAptitudeEnum.ClientMianze.getPathList().clear()
            UploadAptitudeEnum.ClientMianze.getPathList().add(AptitudeInfo())

            UploadAptitudeEnum.DiChanXiaoShouLicense.yingyePathId=""
            UploadAptitudeEnum.DiChanXiaoShouLicense.currentNumber=0
            UploadAptitudeEnum.DiChanXiaoShouLicense.deleteList.clear()
            UploadAptitudeEnum.DiChanXiaoShouLicense.newAddList.clear()
            UploadAptitudeEnum.DiChanXiaoShouLicense.getPathList().clear()
            UploadAptitudeEnum.DiChanXiaoShouLicense.getPathList().add(AptitudeInfo())


            UploadAptitudeEnum.Host.yingyePathId=""
            UploadAptitudeEnum.Host.currentNumber=0
            UploadAptitudeEnum.Host.deleteList.clear()
            UploadAptitudeEnum.Host.newAddList.clear()
            UploadAptitudeEnum.Host.getPathList().clear()
            UploadAptitudeEnum.Host.getPathList().add(AptitudeInfo())

            UploadAptitudeEnum.Other.yingyePathId=""
            UploadAptitudeEnum.Other.currentNumber=0
            UploadAptitudeEnum.Other.deleteList.clear()
            UploadAptitudeEnum.Other.newAddList.clear()
            UploadAptitudeEnum.Other.getPathList().clear()
            UploadAptitudeEnum.Other.getPathList().add(AptitudeInfo())

            UploadAptitudeEnum.PostMianze.yingyePathId=""
            UploadAptitudeEnum.PostMianze.currentNumber=0
            UploadAptitudeEnum.PostMianze.deleteList.clear()
            UploadAptitudeEnum.PostMianze.newAddList.clear()
            UploadAptitudeEnum.PostMianze.getPathList().clear()
            UploadAptitudeEnum.PostMianze.getPathList().add(AptitudeInfo())


            UploadAptitudeEnum.ZCode.yingyePathId=""
            UploadAptitudeEnum.ZCode.currentNumber=0
            UploadAptitudeEnum.ZCode.deleteList.clear()
            UploadAptitudeEnum.ZCode.newAddList.clear()
            UploadAptitudeEnum.ZCode.getPathList().clear()
            UploadAptitudeEnum.ZCode.getPathList().add(AptitudeInfo())

            var yinyelist=ArrayList<AptitudeInfo?>()
            yinyelist.addAll(UploadAptitudeEnum.Yingye.getPathList())
            SystemUtil.printlnStr("yinyelist hs:"+yinyelist.hashCode()+
                    "...UploadAptitudeEnum.Yingye.getPathList() hs:"+UploadAptitudeEnum.Yingye.getPathList().hashCode())
            yinyeAdapter = UploadImageAdapter(this@MineClientDetail,yinyelist)
            yinyeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.Yingye
            yinyeAdapter!!.gridView=yingye_gridvew
            yinyeAdapter!!.isCancelable=false
            yingye_gridvew.setAdapter(yinyeAdapter)

            basePresenter.getAllUploadAptitude(GetAllUploadAptitudeRequest(currentClient.id,true))

            var tradelist=arrayListOf<AptitudeInfo?>()
            tradelist.addAll(UploadAptitudeEnum.Trade.getPathList())
            //商标
            tradeAdapter = UploadImageAdapter(this@MineClientDetail, tradelist)
            tradeAdapter!!.gridView=trade_gridvew
            tradeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.Trade
            tradeAdapter!!.isCancelable=false
            trade_gridvew.setAdapter(tradeAdapter)

            var foodlist=arrayListOf<AptitudeInfo?>()
            foodlist.addAll(UploadAptitudeEnum.Food.getPathList())
            foodAdapter = UploadImageAdapter(this@MineClientDetail, foodlist)
            foodAdapter!!.gridView=food_gridvew
            foodAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.Food
            foodAdapter!!.isCancelable=false
            food_gridvew.setAdapter(foodAdapter)

            var portraitlist=arrayListOf<AptitudeInfo?>()
            SystemUtil.printlnStr("UploadAptitudeEnum.Portrait.getPathList():"+UploadAptitudeEnum.Portrait.getPathList().size)
            portraitlist.addAll(UploadAptitudeEnum.Portrait.getPathList())
            portraitAdpater = UploadImageAdapter(this@MineClientDetail,portraitlist)
            portraitAdpater!!.gridView=portrait_gridvew
            portraitAdpater!!.uploadAptitudeEnum=UploadAptitudeEnum.Portrait
            portraitAdpater!!.isCancelable=false
            portrait_gridvew.setAdapter(portraitAdpater)


            var clientlist=arrayListOf<AptitudeInfo?>()
            clientlist.addAll(UploadAptitudeEnum.ClientMianze.getPathList())

            clienMianzeAdapter = UploadImageAdapter(this@MineClientDetail, clientlist)
            clienMianzeAdapter!!.gridView=mianze_gridvew
            clienMianzeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.ClientMianze
            clienMianzeAdapter!!.isCancelable=false
            mianze_gridvew.setAdapter(clienMianzeAdapter)

            var zcodelist=arrayListOf<AptitudeInfo?>()
            zcodelist.addAll(UploadAptitudeEnum.ZCode.getPathList())
            zCodeAdpater = UploadImageAdapter(this@MineClientDetail,zcodelist)
            zCodeAdpater!!.gridView=zcode_gridvew
            zCodeAdpater!!.uploadAptitudeEnum=UploadAptitudeEnum.ZCode
            zCodeAdpater!!.isCancelable=false
            zcode_gridvew.setAdapter(zCodeAdpater)


            var expresslist=arrayListOf<AptitudeInfo?>()
            expresslist.addAll(UploadAptitudeEnum.PostMianze.getPathList())
            expressAdapter = UploadImageAdapter(this@MineClientDetail, expresslist)
            expressAdapter!!.gridView=sudimianze_gridvew
            expressAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.PostMianze
            expressAdapter!!.isCancelable=false
            sudimianze_gridvew.setAdapter(expressAdapter)


            var otherlist=arrayListOf<AptitudeInfo?>()
            otherlist.addAll(UploadAptitudeEnum.Other.getPathList())
            otherAdapter = UploadImageAdapter(this@MineClientDetail,otherlist)
            otherAdapter!!.gridView=other_gridvew
            otherAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.Other
            otherAdapter!!.isCancelable=false
            other_gridvew.setAdapter(otherAdapter)


            var hostlist=arrayListOf<AptitudeInfo?>()
            hostlist.addAll(UploadAptitudeEnum.Host.getPathList())
            hostAdpater = UploadImageAdapter(this@MineClientDetail, hostlist)
            hostAdpater!!.gridView=hostname_gridvew
            hostAdpater!!.uploadAptitudeEnum=UploadAptitudeEnum.Host
            hostAdpater!!.isCancelable=false
            hostname_gridvew.setAdapter(hostAdpater)


            var landlist=arrayListOf<AptitudeInfo?>()
            landlist.addAll(UploadAptitudeEnum.DiChanXiaoShouLicense.getPathList())
            dichanxiaoshouAdapter = UploadImageAdapter(this@MineClientDetail, landlist)
            dichanxiaoshouAdapter!!.gridView=dichanxiaoshou_gridvew
            dichanxiaoshouAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.DiChanXiaoShouLicense
            dichanxiaoshouAdapter!!.isCancelable=false
            dichanxiaoshou_gridvew.setAdapter(dichanxiaoshouAdapter)
        }
    }

    override fun getView(): View {
        return View.inflate(this@MineClientDetail, R.layout.activity_client_detail,null)
    }

    private fun getApptitudeInfoUpadate(uploadAptitudeEnum1: UploadAptitudeEnum) {
        //将 图片放入集合 显示界面
        var list=uploadAptitudeEnum1!!.getPathList()
        SystemUtil.printlnStr("list size:"+list.size)
        if(uploadAptitudeEnum1==UploadAptitudeEnum.DiChanXiaoShouLicense){
            dichanxiaoshouAdapter!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.Host){
            hostAdpater!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.Other){
            otherAdapter!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.PostMianze){
            expressAdapter!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.ZCode){
            zCodeAdpater!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.ClientMianze){
            clienMianzeAdapter!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.Yingye){
            yinyeAdapter!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.Trade){
            tradeAdapter!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.Portrait){
            portraitAdpater!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.Food){
            foodAdapter!!.update(list)
        }
    }
}