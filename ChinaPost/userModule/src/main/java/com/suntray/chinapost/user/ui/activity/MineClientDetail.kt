package com.suntray.chinapost.user.ui.activity

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.suntray.chinapost.user.ui.dialog.ImageInfoDialog
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


    var yinyeAdapter: UploadImageAdapter?=null
    var tradeAdapter: UploadImageAdapter?=null



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

        scrollView.scrollBy(0,-400)
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
            iv_yixiang.visibility=View.INVISIBLE

            btn_hasxiadan.visibility=View.GONE
            tv_has_client.visibility=View.GONE
        }else if(currentClient!!.stage==64){
            tv_yixiang_client.visibility=View.GONE
            iv_yixiang.visibility=View.GONE

            btn_hasxiadan.visibility=View.INVISIBLE
            tv_has_client.visibility=View.VISIBLE
         }else if(currentClient!!.stage==9){
            tv_yixiang_client.visibility=View.GONE
            iv_yixiang.visibility=View.GONE

            btn_hasxiadan.visibility=View.GONE
            tv_has_client.visibility=View.GONE

            btn_zizhi.visibility=View.GONE
            tv_zizhi.visibility=View.VISIBLE
          }

        tv_client_dizhi.setText(currentClient!!.provincename+currentClient!!.cityname+currentClient!!.districtname)
        tv_select_content.setText(currentClient!!.professionname)
        tv_choose_type.setText(currentClient!!.naturename)
        tv_client_laiyuan.setText(currentClient!!.originname)
        tv_address.setText(currentClient!!.address)
        ed_belong.setText(currentClient!!.salesmanname)

//        if(currentClient!!.stage==8){
//            scrollView.visibility=View.GONE
//        }else{
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
            yinyelist.addAll(UploadAptitudeEnum.JiBenXinxi.getPathList())
            SystemUtil.printlnStr("yinyelist hs:"+yinyelist.hashCode()+
                    "...UploadAptitudeEnum.Yingye.getPathList() hs:"+UploadAptitudeEnum.JiBenXinxi.getPathList().hashCode())
            yinyeAdapter = UploadImageAdapter(this@MineClientDetail,yinyelist)
            yinyeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.JiBenXinxi
            yinyeAdapter!!.gridView=yingye_gridvew
            yinyeAdapter!!.isCancelable=false
            yingye_gridvew.setAdapter(yinyeAdapter)
           yingye_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    //允许SD卡读写权限
                    if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                            (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) {// 添加图片
                    } else {
                        var imageDialog= ImageInfoDialog(this@MineClientDetail)
                        imageDialog.show()
                        imageDialog.setContent((parent!!.getItemAtPosition(position) as AptitudeInfo).address)
                    }

                }
            })

            basePresenter.getAllUploadAptitude(GetAllUploadAptitudeRequest(currentClient.id,true))

            var tradelist=arrayListOf<AptitudeInfo?>()
            tradelist.addAll(UploadAptitudeEnum.HangyeTeshu.getPathList())
            //商标
            tradeAdapter = UploadImageAdapter(this@MineClientDetail, tradelist)
            tradeAdapter!!.gridView=food_gridvew
            tradeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.HangyeTeshu
            tradeAdapter!!.isCancelable=false
            food_gridvew.setAdapter(tradeAdapter)

            food_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            //允许SD卡读写权限
                            if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                    (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) {// 添加图片
                            } else {
                                var imageDialog= ImageInfoDialog(this@MineClientDetail)
                                imageDialog.show()
                                imageDialog.setContent((parent!!.getItemAtPosition(position) as AptitudeInfo).address)
                            }

                }
            })


        /***
             *  提示图展示
             */
            tv_base_tips.setOnClickListener({
                AlertDialog.Builder(this@MineClientDetail)
                        .setTitle("基本资质说明").setMessage("基本资质信息包括营业执照、工商信息、商标注册证、代言人协议和肖像免责声明。")
                        .setNegativeButton("确定",object: DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.dismiss()
                            }
                        }).create().show()
            })
//        }
    }

    override fun getView(): View {
        return View.inflate(this@MineClientDetail, R.layout.activity_client_detail,null)
    }

    private fun getApptitudeInfoUpadate(uploadAptitudeEnum1: UploadAptitudeEnum) {
        //将 图片放入集合 显示界面
        var list=uploadAptitudeEnum1!!.getPathList()
        SystemUtil.printlnStr("list size:"+list.size)
        if(uploadAptitudeEnum1==UploadAptitudeEnum.JiBenXinxi){
            yinyeAdapter!!.update(list)
        }else if(uploadAptitudeEnum1==UploadAptitudeEnum.HangyeTeshu){
            tradeAdapter!!.update(list)
        }


    }
}