package com.suntray.chinapost.user.ui.activity

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.Gravity
import android.view.View
import android.widget.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.SDCardUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.baselibrary.utils.UiUtils
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.AptitudeInfo
import com.suntray.chinapost.user.data.enum.UploadAptitudeEnum
import com.suntray.chinapost.user.data.request.GetAllUploadAptitudeRequest
import com.suntray.chinapost.user.data.request.SaveAptitudeImgRequest
import com.suntray.chinapost.user.data.request.UploadAllAptitudeImageRequest
import com.suntray.chinapost.user.data.response.GetAllAptitudeInfoResponse
import com.suntray.chinapost.user.data.response.SaveAptitudeImageResponse
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.ClientPresenter
import com.suntray.chinapost.user.presenter.view.ClientView
import com.suntray.chinapost.user.ui.adapter.UploadImageAdapter
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_upload_aptitude.*
import kotlinx.android.synthetic.main.upload_aptitude_view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 *  上传资质的信息的界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/16 15:25
 */
@Route(path = RouterPath.MineModule.MINE_UPLOAD_APTITUDE)
class UploadAptitudeActivity:BaseMvpActivity<ClientPresenter>(),ClientView{


    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }


    /**
     * 保存 资质信息接口
     */
    override fun onSaveClientInfo() {
        super.onSaveClientInfo()
        ToastUtil.makeText(this@UploadAptitudeActivity,"上传成功")
        finish()
    }

    override fun onError(error: String, vararg args: String) {
        if(args!=null && args.size>=1){
            if(args[0].equals("获取资质信息失败")){
                //提示不能上传
                ToastUtil.makeText(this@UploadAptitudeActivity,error)
                finish()
            }
        }
    }
    /**
     * 获取 所有的资质信息
     */
     var id=-1
    override fun onGetAllApatutdInfos(getAllAptitudeInfoResponse: GetAllAptitudeInfoResponse) {
        hud2!!.setLabel("上传资质信息中...")
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


    var uploadaptitudeenum:UploadAptitudeEnum?=null
    var yinyeAdapter:UploadImageAdapter?=null
    var tradeAdapter:UploadImageAdapter?=null
    var foodAdapter:UploadImageAdapter?=null
    var portraitAdpater:UploadImageAdapter?=null

    //域名
    var hostAdpater:UploadImageAdapter?=null
    var zCodeAdpater:UploadImageAdapter?=null
    var clienMianzeAdapter:UploadImageAdapter?=null
    var expressAdapter:UploadImageAdapter?=null
    var dichanxiaoshouAdapter:UploadImageAdapter?=null
    var otherAdapter:UploadImageAdapter?= null



    override fun initView() {
        isRightShow=false;
        isTitleShow=true
        isBlackShow=true
        viewtitle="上传资质信息"

        hud2= KProgressHUD(this@UploadAptitudeActivity).setLabel("获取资质信息....").setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        var clientId=intent.getIntExtra("clientId",-1)

        //点击确定 按钮
        btn_ok.setOnClickListener({
            if(canUpload()){

                //进行构建描述类型的RequestBody的description
                val description = RequestBody.create(MediaType.parse("multipart/form-data"), "this is a description")

                SystemUtil.printlnStr("getDeleteIds:"+getDeleteIds().size+"..id:"+id)
                basePresenter.uploadAllAptitudeImage(
                        UploadAllAptitudeImageRequest(
                                getMultiPartBody(UploadAptitudeEnum.Yingye),clientId
                                ,getMultiPartBody(UploadAptitudeEnum.Food)
                                ,getMultiPartBody(UploadAptitudeEnum.Portrait)
                                ,getMultiPartBody(UploadAptitudeEnum.Trade)
                                ,getMultiPartBody(UploadAptitudeEnum.Host)
                                ,getMultiPartBody(UploadAptitudeEnum.ZCode)
                                ,getMultiPartBody(UploadAptitudeEnum.ClientMianze)
                                ,getMultiPartBody(UploadAptitudeEnum.PostMianze)
                                ,getMultiPartBody(UploadAptitudeEnum.DiChanXiaoShouLicense)
                                ,getMultiPartBody(UploadAptitudeEnum.Other),
                                description,id,getDeleteIds()

                        ))
            }else{
                ToastUtil.makeText(this@UploadAptitudeActivity,"至少选择一种类型的图片")
            }
        })

        //点击 取消按钮
        btn_cancel.setOnClickListener({
            finish()
        })
        //初始化数据信息
        UploadAptitudeEnum.Food.yingyePathId=""
        UploadAptitudeEnum.Food.currentNumber=0
        UploadAptitudeEnum.Food.getPathList().clear()
        UploadAptitudeEnum.Food.deleteList.clear()
        UploadAptitudeEnum.Food.newAddList.clear()
        UploadAptitudeEnum.Food.addPath(AptitudeInfo())

        UploadAptitudeEnum.Portrait.yingyePathId=""
        UploadAptitudeEnum.Portrait.currentNumber=0
        UploadAptitudeEnum.Portrait.deleteList.clear()
        UploadAptitudeEnum.Portrait.newAddList.clear()
        UploadAptitudeEnum.Portrait.getPathList().clear()
        UploadAptitudeEnum.Portrait.addPath(AptitudeInfo())

        UploadAptitudeEnum.Trade.yingyePathId=""
        UploadAptitudeEnum.Trade.currentNumber=0
        UploadAptitudeEnum.Trade.getPathList().clear()
        UploadAptitudeEnum.Trade.deleteList.clear()
        UploadAptitudeEnum.Trade.newAddList.clear()
        UploadAptitudeEnum.Trade.addPath(AptitudeInfo())

        UploadAptitudeEnum.Yingye.yingyePathId=""
        UploadAptitudeEnum.Yingye.currentNumber=0
        UploadAptitudeEnum.Yingye.deleteList.clear()
        UploadAptitudeEnum.Yingye.newAddList.clear()
        UploadAptitudeEnum.Yingye.getPathList().clear()
        UploadAptitudeEnum.Yingye.addPath(AptitudeInfo())


        UploadAptitudeEnum.ClientMianze.yingyePathId=""
        UploadAptitudeEnum.ClientMianze.currentNumber=0
        UploadAptitudeEnum.ClientMianze.deleteList.clear()
        UploadAptitudeEnum.ClientMianze.newAddList.clear()
        UploadAptitudeEnum.ClientMianze.getPathList().clear()
        UploadAptitudeEnum.ClientMianze.addPath(AptitudeInfo())

        UploadAptitudeEnum.DiChanXiaoShouLicense.yingyePathId=""
        UploadAptitudeEnum.DiChanXiaoShouLicense.currentNumber=0
        UploadAptitudeEnum.DiChanXiaoShouLicense.deleteList.clear()
        UploadAptitudeEnum.DiChanXiaoShouLicense.newAddList.clear()
        UploadAptitudeEnum.DiChanXiaoShouLicense.getPathList().clear()
        UploadAptitudeEnum.DiChanXiaoShouLicense.addPath(AptitudeInfo())


        UploadAptitudeEnum.Host.yingyePathId=""
        UploadAptitudeEnum.Host.currentNumber=0
        UploadAptitudeEnum.Host.deleteList.clear()
        UploadAptitudeEnum.Host.newAddList.clear()
        UploadAptitudeEnum.Host.getPathList().clear()
        UploadAptitudeEnum.Host.addPath(AptitudeInfo())


        UploadAptitudeEnum.Other.yingyePathId=""
        UploadAptitudeEnum.Other.currentNumber=0
        UploadAptitudeEnum.Other.deleteList.clear()
        UploadAptitudeEnum.Other.newAddList.clear()
        UploadAptitudeEnum.Other.getPathList().clear()
        UploadAptitudeEnum.Other.addPath(AptitudeInfo())

        UploadAptitudeEnum.PostMianze.yingyePathId=""
        UploadAptitudeEnum.PostMianze.currentNumber=0
        UploadAptitudeEnum.PostMianze.deleteList.clear()
        UploadAptitudeEnum.PostMianze.newAddList.clear()
        UploadAptitudeEnum.PostMianze.getPathList().clear()
        UploadAptitudeEnum.PostMianze.addPath(AptitudeInfo())


        UploadAptitudeEnum.ZCode.yingyePathId=""
        UploadAptitudeEnum.ZCode.currentNumber=0
        UploadAptitudeEnum.ZCode.deleteList.clear()
        UploadAptitudeEnum.ZCode.newAddList.clear()
        UploadAptitudeEnum.ZCode.getPathList().clear()
        UploadAptitudeEnum.ZCode.addPath(AptitudeInfo())

        var yinyelist=ArrayList<AptitudeInfo?>()
        yinyelist.addAll(UploadAptitudeEnum.Yingye.getPathList())
        SystemUtil.printlnStr("yinyelist hs:"+yinyelist.hashCode()+
                "...UploadAptitudeEnum.Yingye.getPathList() hs:"+UploadAptitudeEnum.Yingye.getPathList().hashCode())
        yinyeAdapter = UploadImageAdapter(this@UploadAptitudeActivity,yinyelist)
        yinyeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.Yingye
        yinyeAdapter!!.gridView=yingye_gridvew
        yingye_gridvew.setAdapter(yinyeAdapter)

        basePresenter.getAllUploadAptitude(GetAllUploadAptitudeRequest(clientId))
        yingye_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                        (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) { // 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.Yingye
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })

            }
        })



        var tradelist=arrayListOf<AptitudeInfo?>()
        tradelist.addAll(UploadAptitudeEnum.Trade.getPathList())
        //商标
        tradeAdapter = UploadImageAdapter(this@UploadAptitudeActivity, tradelist)
        tradeAdapter!!.gridView=trade_gridvew
        tradeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.Trade
        trade_gridvew.setAdapter(tradeAdapter)
        trade_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) {// 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.Trade
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })

            }
        })

        var foodlist=arrayListOf<AptitudeInfo?>()
        foodlist.addAll(UploadAptitudeEnum.Food.getPathList())
        foodAdapter = UploadImageAdapter(this@UploadAptitudeActivity, foodlist)
        foodAdapter!!.gridView=food_gridvew
        foodAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.Food
        food_gridvew.setAdapter(foodAdapter)
        food_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) {// 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.Food
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })

            }
        })

        var portraitlist=arrayListOf<AptitudeInfo?>()
        portraitlist.addAll(UploadAptitudeEnum.Portrait.getPathList())
        portraitAdpater = UploadImageAdapter(this@UploadAptitudeActivity,portraitlist)
        portraitAdpater!!.gridView=portrait_gridvew
        portraitAdpater!!.uploadAptitudeEnum=UploadAptitudeEnum.Portrait
        portrait_gridvew.setAdapter(portraitAdpater)
        portrait_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) {// 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.Portrait
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })

            }
        })


        var clientlist=arrayListOf<AptitudeInfo?>()
        clientlist.addAll(UploadAptitudeEnum.ClientMianze.getPathList())

        clienMianzeAdapter = UploadImageAdapter(this@UploadAptitudeActivity, clientlist)
        clienMianzeAdapter!!.gridView=mianze_gridvew
        clienMianzeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.ClientMianze
        mianze_gridvew.setAdapter(clienMianzeAdapter)
        mianze_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) { // 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.ClientMianze
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })

        var zcodelist=arrayListOf<AptitudeInfo?>()
        zcodelist.addAll(UploadAptitudeEnum.ZCode.getPathList())
        zCodeAdpater = UploadImageAdapter(this@UploadAptitudeActivity,zcodelist)
        zCodeAdpater!!.gridView=zcode_gridvew
        zCodeAdpater!!.uploadAptitudeEnum=UploadAptitudeEnum.ZCode
        zcode_gridvew.setAdapter(zCodeAdpater)
        zcode_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) { // 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.ZCode
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })


        var expresslist=arrayListOf<AptitudeInfo?>()
        expresslist.addAll(UploadAptitudeEnum.PostMianze.getPathList())
        expressAdapter = UploadImageAdapter(this@UploadAptitudeActivity, expresslist)
        expressAdapter!!.gridView=sudimianze_gridvew
        expressAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.PostMianze
        sudimianze_gridvew.setAdapter(expressAdapter)
        sudimianze_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) {// 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.PostMianze
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })


        var otherlist=arrayListOf<AptitudeInfo?>()
        otherlist.addAll(UploadAptitudeEnum.Other.getPathList())
        otherAdapter = UploadImageAdapter(this@UploadAptitudeActivity,otherlist)
        otherAdapter!!.gridView=other_gridvew
        otherAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.Other
        other_gridvew.setAdapter(otherAdapter)
        other_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) {// 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.Other
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })


        var hostlist=arrayListOf<AptitudeInfo?>()
        hostlist.addAll(UploadAptitudeEnum.Host.getPathList())
        hostAdpater = UploadImageAdapter(this@UploadAptitudeActivity, hostlist)
        hostAdpater!!.gridView=hostname_gridvew
        hostAdpater!!.uploadAptitudeEnum=UploadAptitudeEnum.Host
        hostname_gridvew.setAdapter(hostAdpater)
        hostname_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) { // 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.Host
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })


        var landlist=arrayListOf<AptitudeInfo?>()
        landlist.addAll(UploadAptitudeEnum.DiChanXiaoShouLicense.getPathList())
        dichanxiaoshouAdapter = UploadImageAdapter(this@UploadAptitudeActivity, landlist)
        dichanxiaoshouAdapter!!.gridView=dichanxiaoshou_gridvew
        dichanxiaoshouAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.DiChanXiaoShouLicense
        dichanxiaoshou_gridvew.setAdapter(dichanxiaoshouAdapter)
        dichanxiaoshou_gridvew.setOnItemClickListener(object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as AptitudeInfo).address== null ||
                                (parent!!.getItemAtPosition(position) as AptitudeInfo).address.equals("")) { // 添加图片
                            uploadaptitudeenum=UploadAptitudeEnum.DiChanXiaoShouLicense
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })
}

    /**
     * 获取得到  数组
     */
    private fun getDeleteIds(): Array<Int?> {
        var deleteIdArrays=ArrayList<Int>()
        for(data in UploadAptitudeEnum.DiChanXiaoShouLicense.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.Host.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.Other.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.PostMianze.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.ZCode.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.ClientMianze.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.Portrait.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.Food.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.Trade.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.Yingye.deleteList){
            deleteIdArrays.add(data.id)
        }

        var deleteIdInters= arrayOfNulls<Int>(deleteIdArrays.size)
        for(index in deleteIdArrays.indices){
            deleteIdInters.set(index,deleteIdArrays.get(index))
        }
        return deleteIdInters
    }

    /**
     * 是否 能够上传
     */
    private fun canUpload(): Boolean {
        if( UploadAptitudeEnum.Food.getPathList().size>1 || UploadAptitudeEnum.Portrait.getPathList().size>1
               || UploadAptitudeEnum.Trade.getPathList().size>1 || UploadAptitudeEnum.Yingye.getPathList().size>1
                || UploadAptitudeEnum.ClientMianze.getPathList().size>1 || UploadAptitudeEnum.ZCode.getPathList().size>1 ||
                       UploadAptitudeEnum.PostMianze.getPathList().size>1 || UploadAptitudeEnum.Other.getPathList().size>1 ||
                            UploadAptitudeEnum.Host.getPathList().size>1 || UploadAptitudeEnum.DiChanXiaoShouLicense.getPathList().size>1){
               return true
        }else if( UploadAptitudeEnum.Food.newAddList.size>=1 || UploadAptitudeEnum.Portrait.newAddList.size>=1
                || UploadAptitudeEnum.Trade.newAddList.size>=1 || UploadAptitudeEnum.Yingye.newAddList.size>=1
                || UploadAptitudeEnum.ClientMianze.newAddList.size>=1 || UploadAptitudeEnum.ZCode.newAddList.size>=1 ||
                UploadAptitudeEnum.PostMianze.newAddList.size>=1 || UploadAptitudeEnum.Other.newAddList.size>=1 ||
                UploadAptitudeEnum.Host.newAddList.size>=1 || UploadAptitudeEnum.DiChanXiaoShouLicense.newAddList.size>=1){
            return true
        }else if(getDeleteIds().size>0){
            //只是减少了图片
            return true
        }
        return false
    }


    /**
     * 根据 上传类型 返回 boduy
     */
    private  fun  getMultiPartBody(uploadAptitudeEnum: UploadAptitudeEnum):List<MultipartBody.Part>{

        SystemUtil.printlnStr("uploadAptitudeEnum.getPathId():"+uploadAptitudeEnum.newAddList.size)
       var  parts= ArrayList<MultipartBody.Part>();
        for (data in uploadAptitudeEnum.newAddList) {
            SystemUtil.printlnStr("uploadAptitudeEnum data.address:"+data.address)
            if(data!=null && data.address!=null && !data.address.equals("")){
                    var file=File(data.address)
                    var requestBody=RequestBody.create(MediaType.parse("imgage/png"),file);
                    var  part=MultipartBody.Part.createFormData(uploadAptitudeEnum.getPathId(),file.getName(),requestBody);
                    parts.add(part);
            }
        }
        return parts;
    }
    var photoWindow:PopupWindow?=null
    /**
     * 点击头像，显示弹窗，设置新头像
     */
    private fun setPortraitDialog() {
        //设置长按弹窗-提醒是否删除本条目
        photoWindow= PopupWindow(this@UploadAptitudeActivity)
        var view=View.inflate(this@UploadAptitudeActivity,R.layout.item_choose_photo,null)
        AutoUtils.autoSize(view)
        photoWindow!!.contentView=view;
        photoWindow!!.isOutsideTouchable=true
        photoWindow!!.height= AutoUtils.getPercentHeightSize(1334);
        photoWindow!!.width= AutoUtils.getPercentWidthSize(750)
        photoWindow!!.showAtLocation(root, Gravity.BOTTOM,0,0)

        //拍照
        (view.findViewById(R.id.btn_photo) as Button).setOnClickListener({
            btnCamera()
        })

        //从相册选择
        (view.findViewById(R.id.btn_choose) as Button).setOnClickListener({
            btnXiangCe()
        })

        (view.findViewById(R.id.btn_cancel) as Button).setOnClickListener({
            photoWindow!!.dismiss()
        })
    }

    override fun getView(): View {
        return View.inflate(this@UploadAptitudeActivity,R.layout.activity_upload_aptitude,null);
    }


    /**
     * 打开相册按钮功能
     */
    fun btnXiangCe() {
        // 激活系统图库，选择一张图片
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY)
    }


    var tempFile: File?=null
    /* 头像名称 */
    private val PHOTO_FILE_NAME = "temp_photo.jpg"
    /**
     * 打开摄像机按钮功能
     */
    fun btnCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            tempFile = File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)
            // 从文件中创建uri
            val uri: Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, packageName+".FileProvider",
                        tempFile)
            } else {
                uri = Uri.fromFile(tempFile)
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA)
    }


    private val PHOTO_REQUEST_CAREMA = 1// 拍照
    private val PHOTO_REQUEST_GALLERY = 2// 从相册中选择
    private val PHOTO_REQUEST_CUT = 3// 结果

    /**
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        SystemUtil.printlnStr("PHOTO_REQUEST_CUT requestCode:"+requestCode)
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data == null)
                return
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                val uri = data.data
                crop(uri)
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            SystemUtil.printlnStr("PHOTO_REQUEST_CUT crop 22222:"+requestCode)
            if (hasSdcard()) {
                SystemUtil.printlnStr("PHOTO_REQUEST_CUT crop:"+requestCode)
                tempFile = File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)
                val uri: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(this, "$packageName.FileProvider",
                            tempFile)
                } else {
                    uri = Uri.fromFile(tempFile)
                }

                crop(uri)
            } else {
                Toast.makeText(this@UploadAptitudeActivity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show()
            }

            SystemUtil.printlnStr("PHOTO_REQUEST_CUT crop 1111:"+requestCode)
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            SystemUtil.printlnStr("PHOTO_REQUEST_CUT 1111111111111:"+(data==null))
            if (data == null)
                return
            // 从剪切图片返回的数据
            if (data != null) {
                SystemUtil.printlnStr("PHOTO_REQUEST_CUT 222222222222222222:"+(data==null))
                val bitmap = data.getParcelableExtra<Parcelable>("data") as Bitmap;
                /**
                 * 获得图片
                 */
                SystemUtil.printlnStr("PHOTO_REQUEST_CUT 33333333333333333333 bitmap:"+(bitmap==null))
                var fileName=saveBitmapPng(bitmap, 95)
                SystemUtil.printlnStr("PHOTO_REQUEST_CUT 33333333333333333333 fileName:"+fileName)
                proImageShow(fileName)
                if(photoWindow!=null && photoWindow!!.isShowing){
                    photoWindow!!.dismiss()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    /**
     * 处理页面的展示 业务逻辑
     */
    private fun proImageShow(fileName: String) {
        //将 图片放入集合 显示界面
        var aptitudeInfo=AptitudeInfo(fileName)
        uploadaptitudeenum!!.newAddList.add(0, aptitudeInfo)
        if(uploadaptitudeenum==UploadAptitudeEnum.DiChanXiaoShouLicense){
            dichanxiaoshouAdapter!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.Host){
            hostAdpater!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.Other){
            otherAdapter!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.PostMianze){
            expressAdapter!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.ZCode){
            zCodeAdpater!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.ClientMianze){
            clienMianzeAdapter!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.Yingye){
            yinyeAdapter!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.Trade){
            tradeAdapter!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.Portrait){
            portraitAdpater!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.Food){
            foodAdapter!!.newAddUpdate(aptitudeInfo)
        }
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

    /**
     * 照相部分的保存Bitmap到sdcard
     *
     * @param b
     */
    fun saveBitmapPng(b: Bitmap, qualtity: Int):String{
        val path = SDCardUtil.instance.uploadPic!!
        val dataTake = System.currentTimeMillis()
        val jpegName = "$path/$dataTake.png"
        //本地的图片Url转换成图片文件流
        try {
            val fout = FileOutputStream(jpegName)
            val bos = BufferedOutputStream(fout)
            b.compress(Bitmap.CompressFormat.PNG, qualtity, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            UiUtils.instance.showToast("请查看SD卡权限或手机内存")
            e.printStackTrace()
        }
        return jpegName

    }

    /*
   * 判断sdcard是否被挂载
   */
    fun hasSdcard(): Boolean {
        //判断ＳＤ卡手否是安装好的media_mounted
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            true
        } else {
            false
        }
    }

    /*
 * 剪切图片
 */
    private fun crop(uri: Uri?) {
//         裁剪图片意图
        var intent = Intent("com.android.camera.action.CROP")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "image/*");
        } else {
            intent.setDataAndType(uri, "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0.1);
        intent.putExtra("aspectY", 0.1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
}