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
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.suntray.chinapost.baselibrary.common.AppManager
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.utils.*
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.AptitudeInfo
import com.suntray.chinapost.user.data.enum.UploadAptitudeEnum
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.MinePresenter
import com.suntray.chinapost.user.presenter.view.MineEditView
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_mine.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 *   我的界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/12 14:37
 */
@Route(path = RouterPath.MineModule.MINE_ACTIVITY)
class MineActivity :BaseMvpActivity<MinePresenter>(), MineEditView {

    private val PHOTO_REQUEST_CAREMA = 1// 拍照
    private val PHOTO_REQUEST_GALLERY = 2// 从相册中选择
    private val PHOTO_REQUEST_CUT = 3// 结果

    override fun getView(): View {
        return View.inflate(this,R.layout.activity_mine,null);
    }

    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this;
    }

    override fun onUploadPortrait(content: String) {
        ToastUtil.makeText(this@MineActivity,content)
        if(UserDao.getLocalUser().headImgPath!=null &&
                !UserDao.getLocalUser().headImgPath.equals("")){
            Glide.with(this@MineActivity)
                    .load(BaseConstants.BASE_UPLOAD_URL+UserDao.getLocalUser().headImgPath)
                    .error(R.drawable.iv_user_portrait).into(iv_portrait)

            if(UserDao.getLocalUser().headImgPath.startsWith("http")){
                BitmapUtil.myInstance!!.configDefaultLoadFailedImage(R.drawable.iv_user_portrait)
                        .display(iv_portrait, UserDao.getLocalUser().headImgPath)
            }else{
                BitmapUtil.myInstance!!.configDefaultLoadFailedImage(R.drawable.iv_user_portrait)
                        .display(iv_portrait,BaseConstants.BASE_UPLOAD_URL+UserDao.getLocalUser().headImgPath)
            }

        }
    }

    override fun initView() {
        isTitleShow = false
        isBlackShow = false
        tv_username.setText(UserDao.getLocalUser().nickName);
        tv_jigou.setText(UserDao.getLocalUser().orgName)
        if(UserDao.getLocalUser().headImgPath!=null &&
                    !UserDao.getLocalUser().headImgPath.equals("")) {
//            Glide.with(this@MineActivity).load(BaseConstants.BASE_UPLOAD_URL+UserDao.getLocalUser().headImgPath).error(R.drawable.iv_user_portrait).into(iv_portrait)

            if (UserDao.getLocalUser().headImgPath.startsWith("http")) {
                BitmapUtil.myInstance!!.configDefaultLoadFailedImage(R.drawable.iv_user_portrait)
                        .display(iv_portrait, UserDao.getLocalUser().headImgPath)

            } else {
                BitmapUtil.myInstance!!.configDefaultLoadFailedImage(R.drawable.iv_user_portrait)
                        .display(iv_portrait, BaseConstants.BASE_UPLOAD_URL + UserDao.getLocalUser().headImgPath)
            }
        }

        /**
         * 上传头像的点击事件
         */
        iv_portrait.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        //显示弹窗，选择相册或者拍照，选择头像
                        setPortraitDialog()
                    }, Runnable { ToastUtil.makeText(this@MineActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@MineActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })

        //返回键
        iv_back.setOnClickListener({
            finish()
        })

        //修改密码界面
        rl_edit_pass.setOnClickListener({
            ARouter.getInstance()
                    .build(RouterPath.MineModule.MINE_EDIT_PWD)
                    .navigation(this@MineActivity)
        })

        //我的客户 点击事件
        rl_mine_client.setOnClickListener({
            ARouter.getInstance()
                    .build(RouterPath.MineModule.MINE_CLIENT)
                         .navigation(this@MineActivity)
        })

        //我预约的点位 点击事件
        rl_mine_reserved.setOnClickListener({
            ARouter.getInstance()
                    .build(RouterPath.MineModule.MINE_RESERVERD_DOT)
                    .navigation(this@MineActivity)
        })


        rl_clear_cache.setOnClickListener({

            //先请求拍照权限
            requestPermission(BaseConstants.REDA_EXTERNAL_STORAGE, "android.permission.READ_EXTERNAL_STORAGE", Runnable {
                //允许拍照权限
                //请求SD卡读写权限
                requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                    SDCardUtil.instance.deleteDir(File(SDCardUtil.instance.rootPath))
                    ToastUtil.makeText(this@MineActivity,"清除缓存成功")
                }, Runnable { ToastUtil.makeText(this@MineActivity, "写存储卡权限未打开，请到手机权限中心设置打开...") })
            }, Runnable { ToastUtil.makeText(this@MineActivity, "读存储卡权限未打开,请到手机权限中心设置打开...") })
        })

        //退出登录
        rl_out_login.setOnClickListener({
            UserDao.updateAllUserLocalState(false)
            AppManager.instance.finishAll();
            ARouter.getInstance()
                    .build(RouterPath.AppModule.LOGIN_ACTIVITY)
                                .navigation(this@MineActivity)
        })
    }


    var photoWindow:PopupWindow?=null
    /**
     * 点击头像，显示弹窗，设置新头像
     */
    private fun setPortraitDialog() {
        //设置长按弹窗-提醒是否删除本条目
         photoWindow= PopupWindow(this@MineActivity)
        var view=View.inflate(this@MineActivity,R.layout.item_choose_photo,null)
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
                Toast.makeText(this@MineActivity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show()
            }


            SystemUtil.printlnStr("PHOTO_REQUEST_CUT crop 1111:"+requestCode)
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            SystemUtil.printlnStr("PHOTO_REQUEST_CUT 1111111111111:"+(data==null))
            if (data == null)
                return
            // 从剪切图片返回的数据
            if (data != null) {
                SystemUtil.printlnStr("PHOTO_REQUEST_CUT 222222222222222222:"+(data==null))
                val bitmap = getCircularBitmap(data.getParcelableExtra<Parcelable>("data") as Bitmap) ?: return
                /**
                 * 获得图片
                 */
                SystemUtil.printlnStr("PHOTO_REQUEST_CUT 33333333333333333333 bitmap:"+(bitmap==null))
                var fileName=saveBitmapPng(bitmap, 95)
                SystemUtil.printlnStr("PHOTO_REQUEST_CUT 33333333333333333333 fileName:"+fileName)
                basePresenter.onUploadPortrait(UserDao.getLocalUser().id,File(fileName),BaseConstants.SELECTEDROLEINDEX)
                if(photoWindow!=null && photoWindow!!.isShowing){
                    photoWindow!!.dismiss()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
  * 剪切图片
  */
    private fun crop(uri: Uri?) {
//         裁剪图片意图
        var intent = Intent("com.android.camera.action.CROP")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1)
        intent.putExtra("aspectY", 1)
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)

        intent.putExtra("outputFormat", "JPEG")// 图片格式
        intent.putExtra("noFaceDetection", true)// 取消人脸识别
        intent.putExtra("return-data", true)
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT)
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

    fun getCircularBitmap(square: Bitmap?): Bitmap? {
        if (square == null) return null
        val output = Bitmap.createBitmap(square.width, square.height,
                Bitmap.Config.ARGB_4444)

        val rect = Rect(0, 0, square.width, square.height)
        val canvas = Canvas(output)

        val halfWidth = square.width / 2
        val halfHeight = square.height / 2
        val radius = Math.min(halfWidth, halfHeight)

        val color = -0xbdbdbe
        val paint = Paint()

        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawCircle(halfWidth.toFloat(), halfHeight.toFloat(), radius.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(square, rect, rect, paint)
        return output
    }



}