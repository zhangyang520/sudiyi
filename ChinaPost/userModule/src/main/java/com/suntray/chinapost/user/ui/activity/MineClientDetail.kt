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
import kotlinx.android.synthetic.main.upload_aptitude_view_new.*

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

    //域名
    var hostAdpater: UploadImageAdapter?=null
    var dichanxiaoshouAdapter: UploadImageAdapter?=null


    /**
     * 获取 所有的资质信息
     */
    var id=-1
    override fun onGetAllApatutdInfos(getAllAptitudeInfoResponse: GetAllAptitudeInfoResponse) {
        SystemUtil.printlnStr("getAllAptitudeInfoResponse:"+getAllAptitudeInfoResponse.toString())
        id=getAllAptitudeInfoResponse.id
        if(getAllAptitudeInfoResponse.baseAccList!=null &&
                getAllAptitudeInfoResponse!!.baseAccList!!.size>0){
            UploadAptitudeEnum.JiBenXinxi.imageList.addAll(0,getAllAptitudeInfoResponse.baseAccList!!)
            //获取到信息时
            getApptitudeInfoUpadate(UploadAptitudeEnum.JiBenXinxi);
        }

        if(getAllAptitudeInfoResponse.specialAccList!=null &&
                getAllAptitudeInfoResponse!!.specialAccList!!.size>0){
            UploadAptitudeEnum.HangyeTeshu.imageList.addAll(0,getAllAptitudeInfoResponse.specialAccList!!)
            getApptitudeInfoUpadate(UploadAptitudeEnum.HangyeTeshu);
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
            UploadAptitudeEnum.HangyeTeshu.yingyePathId=""
            UploadAptitudeEnum.HangyeTeshu.currentNumber=0
            UploadAptitudeEnum.HangyeTeshu.getPathList().clear()
            UploadAptitudeEnum.HangyeTeshu.getPathList().add(AptitudeInfo())
            UploadAptitudeEnum.HangyeTeshu.deleteList.clear()
            UploadAptitudeEnum.HangyeTeshu.newAddList.clear()

            UploadAptitudeEnum.JiBenXinxi.yingyePathId=""
            UploadAptitudeEnum.JiBenXinxi.currentNumber=0
            UploadAptitudeEnum.JiBenXinxi.deleteList.clear()
            UploadAptitudeEnum.JiBenXinxi.newAddList.clear()
            UploadAptitudeEnum.JiBenXinxi.getPathList().clear()
            UploadAptitudeEnum.JiBenXinxi.getPathList().add(AptitudeInfo())


            var yinyelist=ArrayList<AptitudeInfo?>()
            yinyelist.addAll(UploadAptitudeEnum.HangyeTeshu.getPathList())
            SystemUtil.printlnStr("yinyelist hs:"+yinyelist.hashCode()+
                    "...UploadAptitudeEnum.Yingye.getPathList() hs:"+UploadAptitudeEnum.HangyeTeshu.getPathList().hashCode())
            yinyeAdapter = UploadImageAdapter(this@MineClientDetail,yinyelist)
            yinyeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.HangyeTeshu
            yinyeAdapter!!.gridView=yingye_gridvew
            yinyeAdapter!!.isCancelable=false
            yingye_gridvew.setAdapter(yinyeAdapter)

            basePresenter.getAllUploadAptitude(GetAllUploadAptitudeRequest(currentClient.id,true))

            var tradelist=arrayListOf<AptitudeInfo?>()
            tradelist.addAll(UploadAptitudeEnum.JiBenXinxi.getPathList())
            //商标
            tradeAdapter = UploadImageAdapter(this@MineClientDetail, tradelist)
            tradeAdapter!!.gridView=food_gridvew
            tradeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.JiBenXinxi
            tradeAdapter!!.isCancelable=false
            food_gridvew.setAdapter(tradeAdapter)
        }
    }

    override fun getView(): View {
        return View.inflate(this@MineClientDetail, R.layout.activity_client_detail,null)
    }

    private fun getApptitudeInfoUpadate(uploadAptitudeEnum1: UploadAptitudeEnum) {
        //将 图片放入集合 显示界面
        var list=uploadAptitudeEnum1!!.getPathList()
        SystemUtil.printlnStr("list size:"+list.size)
        if(uploadAptitudeEnum1==UploadAptitudeEnum.HangyeTeshu){
            yinyeAdapter!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.JiBenXinxi){
            tradeAdapter!!.update(list)
        }
    }
}