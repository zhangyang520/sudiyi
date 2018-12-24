package com.suntray.chinapost.user.ui.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
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
import com.github.zhangyang.camera_picker.CropperActivity
import com.github.zhangyang.camera_picker.utils.Constants
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
import com.suntray.chinapost.user.ui.dialog.ImageInfoDialog
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_upload_aptitude.*
import kotlinx.android.synthetic.main.upload_aptitude_view_new.*
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
        setResult(101)
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

    var uploadaptitudeenum:UploadAptitudeEnum?=null
    var yinyeAdapter:UploadImageAdapter?=null
    var tradeAdapter:UploadImageAdapter?=null



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
                                getMultiPartBody(UploadAptitudeEnum.JiBenXinxi),clientId
                                ,getMultiPartBody(UploadAptitudeEnum.HangyeTeshu),description,id,getDeleteIds()
                        ))
            }else{
                ToastUtil.makeText(this@UploadAptitudeActivity,"请至少选择基本类型的图片")
            }
        })

        //点击 取消按钮
        btn_cancel.setOnClickListener({
            finish()
        })
        //初始化数据信息
        UploadAptitudeEnum.JiBenXinxi.yingyePathId=""
        UploadAptitudeEnum.JiBenXinxi.currentNumber=0
        UploadAptitudeEnum.JiBenXinxi.getPathList().clear()
        UploadAptitudeEnum.JiBenXinxi.deleteList.clear()
        UploadAptitudeEnum.JiBenXinxi.newAddList.clear()
        UploadAptitudeEnum.JiBenXinxi.addPath(AptitudeInfo())

        UploadAptitudeEnum.HangyeTeshu.yingyePathId=""
        UploadAptitudeEnum.HangyeTeshu.currentNumber=0
        UploadAptitudeEnum.HangyeTeshu.deleteList.clear()
        UploadAptitudeEnum.HangyeTeshu.newAddList.clear()
        UploadAptitudeEnum.HangyeTeshu.getPathList().clear()
        UploadAptitudeEnum.HangyeTeshu.addPath(AptitudeInfo())


        var yinyelist=ArrayList<AptitudeInfo?>()
        yinyelist.addAll(UploadAptitudeEnum.JiBenXinxi.getPathList())
        SystemUtil.printlnStr("yinyelist hs:"+yinyelist.hashCode()+
                "...UploadAptitudeEnum.Yingye.getPathList() hs:"+UploadAptitudeEnum.JiBenXinxi.getPathList().hashCode())
        yinyeAdapter = UploadImageAdapter(this@UploadAptitudeActivity,yinyelist)
        yinyeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.JiBenXinxi
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
                            uploadaptitudeenum=UploadAptitudeEnum.JiBenXinxi
                            setPortraitDialog()
                        } else {
                            var imageDialog= ImageInfoDialog(this@UploadAptitudeActivity)
                            imageDialog.show()
                            imageDialog.setContent((parent!!.getItemAtPosition(position) as AptitudeInfo).address)
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })

            }
        })



        var tradelist=arrayListOf<AptitudeInfo?>()
        tradelist.addAll(UploadAptitudeEnum.HangyeTeshu.getPathList())
        //商标
        tradeAdapter = UploadImageAdapter(this@UploadAptitudeActivity, tradelist)
        tradeAdapter!!.gridView=food_gridvew
        tradeAdapter!!.uploadAptitudeEnum=UploadAptitudeEnum.HangyeTeshu
        food_gridvew.setAdapter(tradeAdapter)
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
                            uploadaptitudeenum=UploadAptitudeEnum.HangyeTeshu
                            setPortraitDialog()
                        } else {
                            var imageDialog= ImageInfoDialog(this@UploadAptitudeActivity)
                            imageDialog.show()
                            imageDialog.setContent((parent!!.getItemAtPosition(position) as AptitudeInfo).address)
                        }
                    }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@UploadAptitudeActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })

        /***
         *  提示图展示
         */
        tv_base_tips.setOnClickListener({
            AlertDialog.Builder(this@UploadAptitudeActivity)
                    .setTitle("基本资质说明").setMessage("基本资质信息包括营业执照、工商信息、商标注册证、代言人协议和肖像免责声明。")
                    .setNegativeButton("确定",object:DialogInterface.OnClickListener{
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            dialog!!.dismiss()
                        }
                    }).create().show()
        })
}

    /**
     * 获取得到  数组
     */
    private fun getDeleteIds(): Array<Int?> {
        var deleteIdArrays=ArrayList<Int>()
        for(data in UploadAptitudeEnum.JiBenXinxi.deleteList){
            deleteIdArrays.add(data.id)
        }

        for(data in UploadAptitudeEnum.HangyeTeshu.deleteList){
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
        if( UploadAptitudeEnum.JiBenXinxi.getPathList().size>1){
               return true
        }else if(UploadAptitudeEnum.JiBenXinxi.newAddList.size>=1){
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
        return View.inflate(this@UploadAptitudeActivity,R.layout.activity_upload_aptitude_new,null);
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
    private var PHOTO_FILE_NAME = "temp_photo.jpg"
    /**
     * 打开摄像机按钮功能
     */
    fun btnCamera() {
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            PHOTO_FILE_NAME=System.currentTimeMillis().toString()+"_temp_photo.jpg"
            tempFile = File(Environment.getExternalStorageDirectory(), PHOTO_FILE_NAME)
            // 从文件中创建uri
            val uri: Uri
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(this, packageName+".FileProvider",
                        tempFile!!)
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
                startPhotoZoom(uri)
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (hasSdcard()) {
                try {
                    startPhotoZoom(tempFile!!.path)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(this@UploadAptitudeActivity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show()
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data == null)
                return
            // 从剪切图片返回的数据
            if (data != null) {
                var fileName = data.extras!!.getString(Constants.FILE_PATH)
                if(photoWindow!=null && photoWindow!!.isShowing){
                    photoWindow!!.dismiss()
                }
                proImageShow(fileName)
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
        if(uploadaptitudeenum==UploadAptitudeEnum.JiBenXinxi){
            yinyeAdapter!!.newAddUpdate(aptitudeInfo)
        }else if(uploadaptitudeenum==UploadAptitudeEnum.HangyeTeshu){
            tradeAdapter!!.newAddUpdate(aptitudeInfo)
        }
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

    /**
     * 进入到 图片缩放
     */
    fun startPhotoZoom(uri: Uri?) {
        val intent = Intent(this@UploadAptitudeActivity, CropperActivity::class.java)
        intent.data = uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT)
    }

    /**
     * 进入到 图片缩放
     */
    fun startPhotoZoom(path:String) {
        val intent = Intent(this@UploadAptitudeActivity, CropperActivity::class.java)
        intent.putExtra("path",path)
        intent.putExtra("PORTRAIT_IMAGE",false)
        startActivityForResult(intent, PHOTO_REQUEST_CUT)
    }
}