package com.suntray.chinapost.map.ui.activity.proxy

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.utils.SDCardUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.baselibrary.utils.UiUtils
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.injection.component.DaggerTaskComponent
import com.suntray.chinapost.map.presenter.TaskPresenter
import com.suntray.chinapost.map.presenter.view.TaskView
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.data.bean.TaskUpload
import com.suntray.chinapost.user.data.enum.UploadTaskEnum
import com.suntray.chinapost.user.ui.adapter.TaskUploadImageAdapter
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_task_detail.*
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 *   任务的详情页
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/24 11:38
 */
@Route(path = RouterPath.MapModule.POST_TASK_DETAIL)
class TaskDetailActivity:BaseMvpActivity<TaskPresenter>(),TaskView{

    var currentType=0// 0:上刊 1:下刊
    var isCanEditable=false
    override fun injectCompontent() {
        DaggerTaskComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    var  kanAdapter:TaskUploadImageAdapter?=null
    override fun initView() {
        isBlackShow=true
        isRightShow=false
        isTitleShow=true
        viewtitle="任务详情"

        isCanEditable=intent.getBooleanExtra("editAble",false)
        currentType=intent.getIntExtra("currentType",0)
        if(isCanEditable){
            ll_bottom.visibility=View.VISIBLE
        }else{
            ll_bottom.visibility=View.GONE
        }
        var landlist=arrayListOf<TaskUpload?>()
        if(currentType==0){
            //初始化数据信息
            UploadTaskEnum.UpKan.yingyePathId=""
            UploadTaskEnum.UpKan.currentNumber=0
            UploadTaskEnum.UpKan.getPathList().clear()
            UploadTaskEnum.UpKan.deleteList.clear()
            UploadTaskEnum.UpKan.newAddList.clear()
            UploadTaskEnum.UpKan.addPath(TaskUpload())
            landlist.addAll(UploadTaskEnum.UpKan.getPathList())
        }else{
            //初始化数据信息
            UploadTaskEnum.DownKan.yingyePathId=""
            UploadTaskEnum.DownKan.currentNumber=0
            UploadTaskEnum.DownKan.getPathList().clear()
            UploadTaskEnum.DownKan.deleteList.clear()
            UploadTaskEnum.DownKan.newAddList.clear()
            UploadTaskEnum.DownKan.addPath(TaskUpload())
            landlist.addAll(UploadTaskEnum.DownKan.getPathList())
        }

        kanAdapter = TaskUploadImageAdapter(this@TaskDetailActivity, landlist)
        kanAdapter!!.isCancelable=isCanEditable
        kanAdapter!!.gridView=gridvew
        if(currentType==0){
            kanAdapter!!.uploadAptitudeEnum= UploadTaskEnum.UpKan
        }else{
            kanAdapter!!.uploadAptitudeEnum= UploadTaskEnum.DownKan
        }
        gridvew.setAdapter(kanAdapter)
        gridvew.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                //先请求拍照权限
                requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
                    //允许拍照权限
                    //请求SD卡读写权限
                    requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                        //允许SD卡读写权限
                        if ((parent!!.getItemAtPosition(position) as TaskUpload).address== null ||
                                (parent!!.getItemAtPosition(position) as TaskUpload).address.equals("")) { // 添加图片
                            setPortraitDialog()
                        } else {
                        }
                    }, Runnable { ToastUtil.makeText(this@TaskDetailActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
                }, Runnable { ToastUtil.makeText(this@TaskDetailActivity, "相机权限未打开，请到手机权限中心设置打开...") })
            }
        })

        btn_cancel.setOnClickListener({
            finish()
        })

        /**
         * 点击
         */
        btn_submit.setOnClickListener({
            //todo 点击提交.
        })
    }

    override fun getView(): View {
        return View.inflate(this@TaskDetailActivity, R.layout.activity_task_detail,null)
    }


    var photoWindow: PopupWindow?=null
    /**
     * 点击头像，显示弹窗，设置新头像
     */
    private fun setPortraitDialog() {
        //设置长按弹窗-提醒是否删除本条目
        photoWindow= PopupWindow(this@TaskDetailActivity)
        var view=View.inflate(this@TaskDetailActivity, com.suntray.chinapost.user.R.layout.item_choose_photo,null)
        AutoUtils.autoSize(view)
        photoWindow!!.contentView=view;
        photoWindow!!.isOutsideTouchable=true
        photoWindow!!.height= AutoUtils.getPercentHeightSize(1334);
        photoWindow!!.width= AutoUtils.getPercentWidthSize(750)
        photoWindow!!.showAtLocation(root, Gravity.BOTTOM,0,0)

        //拍照
        (view.findViewById(com.suntray.chinapost.user.R.id.btn_photo) as Button).setOnClickListener({
            btnCamera()
        })

        //从相册选择
        (view.findViewById(com.suntray.chinapost.user.R.id.btn_choose) as Button).setOnClickListener({
            btnXiangCe()
        })

        (view.findViewById(com.suntray.chinapost.user.R.id.btn_cancel) as Button).setOnClickListener({
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
                Toast.makeText(this@TaskDetailActivity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show()
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

    private fun proImageShow(fileName: String) {
        var aptitudeInfo=TaskUpload(fileName)
        if(currentType==0){
            UploadTaskEnum.UpKan!!.newAddList.add(0, aptitudeInfo)
        }else{
            UploadTaskEnum.DownKan!!.newAddList.add(0, aptitudeInfo)
        }
        kanAdapter!!.newAddUpdate(aptitudeInfo)
    }

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
}