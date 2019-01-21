package com.suntray.chinapost.map.ui.activity.proxy

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.github.zhangyang.camera_picker.CropperActivity
import com.github.zhangyang.camera_picker.exception.ContentException
import com.github.zhangyang.camera_picker.utils.Constants
import com.github.zhangyang.camera_picker.utils.DisplayUtil
import com.github.zhangyang.camera_picker.utils.FileUtils
import com.github.zhangyang.camera_picker.utils.ImageUtils
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.SDCardUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.baselibrary.utils.UiUtils
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.injection.component.DaggerTaskComponent
import com.suntray.chinapost.map.presenter.TaskPresenter
import com.suntray.chinapost.map.presenter.view.TaskView
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.data.bean.TaskUpload
import com.suntray.chinapost.user.data.enum.UploadTaskEnum
import com.suntray.chinapost.map.ui.adapter.proxy.TaskUploadImageAdapter
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.android.synthetic.main.item_task.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    var firstType=0// 0:上刊 1:下刊
    var isCanEditable=false
    var taskEntity:TaskEntity?=null
    override fun injectCompontent() {
        DaggerTaskComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    var  kanAdapter: TaskUploadImageAdapter?=null
    override fun initView() {
        isBlackShow=true
        isRightShow=false
        isTitleShow=true


        isCanEditable=intent.getBooleanExtra("editAble",false)
        firstType=intent.getIntExtra("firstType",1)
        taskEntity= intent.getSerializableExtra("taskEntity") as TaskEntity?

        if(taskEntity==null){
            finish()
            return
        }

        ll_top.post(object:Runnable{
            override fun run() {
                println("ll_top post run scrollView.measuredHeight:"+scrollView.measuredHeight+"..ll_top height:"+ll_top.measuredHeight)
                ll_top.layoutParams.height=scrollView.measuredHeight
                ll_top.requestLayout()
                gridvew.setAdapter(kanAdapter)
            }
        })

        tv_task_type_value.text=taskEntity!!.taskType
        tv_dot_name_value.text=taskEntity!!.pointName
        tv_district_type_value.text=taskEntity!!.zoneType
        tv_device_id_value.text=taskEntity!!.equId
        tv_limit_up.text=taskEntity!!.publishType
        tv_device_guige_value.text=taskEntity!!.equSpecify
        tv_task_area_value.text=taskEntity!!.taskArea
        tv_district_value.text=taskEntity!!.zoneAddress
        tv_ad_type_value.text=taskEntity!!.adverType
        tv_task_time_value.text=taskEntity!!.taskTime
        tv_equation_value.text=taskEntity!!.equLocation
//        tv_up_product_value=taskEntity!!.taskTime  上刊产品
        if(taskEntity!!.state.equals("1")){
            tv_task_state_value.text="未完成"
        }else if(taskEntity!!.state.equals("2")){
            tv_task_state_value.text="待审核"
        }else if(taskEntity!!.state.equals("3")){
            tv_task_state_value.text="审核通过"
        }else if(taskEntity!!.state.equals("4")){
            tv_task_state_value.text="审核不通过"
        }

        if(isCanEditable){
            hud2= KProgressHUD(this@TaskDetailActivity).setLabel("图片上传中....").setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            ll_bottom.visibility=View.VISIBLE
            viewtitle="上传图片"

            tv_base_tips.visibility=View.VISIBLE;

            /***
             *  提示图展示
             */
            tv_base_tips.setOnClickListener({
                AlertDialog.Builder(this@TaskDetailActivity)
                        .setTitle("照片说明").setMessage("1、带报头的地址贴；\r\n2、带报纸的全景；\n3、不带报纸的全景\r\n")
                        .setNegativeButton("确定",object: DialogInterface.OnClickListener{
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog!!.dismiss()
                            }
                        }).create().show()
            })

        }else{

            tv_base_tips.visibility=View.GONE;
            ll_bottom.visibility=View.GONE
            viewtitle="任务详情"
        }
        var landlist=arrayListOf<TaskUpload?>()
        if(firstType==1){
            //初始化数据信息
            UploadTaskEnum.UpKan.yingyePathId=""
            UploadTaskEnum.UpKan.currentNumber=0
            UploadTaskEnum.UpKan.getPathList().clear()
            UploadTaskEnum.UpKan.imageList.clear()
            UploadTaskEnum.UpKan.deleteList.clear()
            UploadTaskEnum.UpKan.newAddList.clear()
            UploadTaskEnum.UpKan.newIdsList.clear()
            if(taskEntity!!.imgs!=null && taskEntity!!.imgs!!.size<3){
                UploadTaskEnum.UpKan.addPath(taskEntity!!.imgs!!)
                if(isCanEditable){
                    UploadTaskEnum.UpKan.getPathList().add(TaskUpload())
                }
            }else{
                UploadTaskEnum.UpKan.addPath(taskEntity!!.imgs!!)
            }
            landlist.addAll(UploadTaskEnum.UpKan.getPathList())
        }else{
            //初始化数据信息
            UploadTaskEnum.DownKan.yingyePathId=""
            UploadTaskEnum.DownKan.currentNumber=0
            UploadTaskEnum.DownKan.getPathList().clear()
            UploadTaskEnum.DownKan.imageList.clear()
            UploadTaskEnum.DownKan.deleteList.clear()
            UploadTaskEnum.DownKan.newAddList.clear()
            UploadTaskEnum.DownKan.newIdsList.clear()
            if(taskEntity!!.imgs!=null && taskEntity!!.imgs!!.size<3){
                UploadTaskEnum.DownKan.addPath(taskEntity!!.imgs!!)
                if(isCanEditable){
                    UploadTaskEnum.DownKan.getPathList().add(TaskUpload())
                }
            }else{
                UploadTaskEnum.DownKan.addPath(taskEntity!!.imgs!!)
            }
            landlist.addAll(UploadTaskEnum.DownKan.getPathList())
        }

        kanAdapter = TaskUploadImageAdapter(this@TaskDetailActivity, landlist)
        kanAdapter!!.isCancelable=isCanEditable
        kanAdapter!!.gridView=gridvew
        if(firstType==1){
            kanAdapter!!.uploadAptitudeEnum= UploadTaskEnum.UpKan
        }else{
            kanAdapter!!.uploadAptitudeEnum= UploadTaskEnum.DownKan
        }

        btn_cancel.setOnClickListener({
            finish()
        })

        /**
         * 点击
         */
        btn_submit.setOnClickListener({
            if(canUpload()){
                val description = RequestBody.create(MediaType.parse("multipart/form-data"), "this is a description")
                if(firstType==1){
                    //上刊
                    basePresenter.uploadTaskImg(taskEntity!!.pointTaskId,taskEntity!!.taskId,firstType,
                            UserDao.getLocalUser().id,getMultiPartBody(UploadTaskEnum.UpKan),
                            getNewAddIds(UploadTaskEnum.UpKan.newAddList)!!,getDeleteIds(UploadTaskEnum.UpKan.deleteList),description)
                }else{
                    //下刊
                    basePresenter.uploadTaskImg(taskEntity!!.pointTaskId,taskEntity!!.taskId,firstType,
                            UserDao.getLocalUser().id,getMultiPartBody(UploadTaskEnum.DownKan),
                            getNewAddIds(UploadTaskEnum.DownKan.newAddList),getDeleteIds(UploadTaskEnum.DownKan.deleteList),description)
                }
            }else{
                ToastUtil.makeText(this@TaskDetailActivity,"暂时不能上传")
            }
        })
    }

    override fun onUploadTaskImg() {
        ToastUtil.makeText(this@TaskDetailActivity,"上传成功")
        setResult(101)
        finish()
    }
    /**
     * 是否 能够上传
     */
    private fun canUpload(): Boolean {
        if(firstType==1){
            if(UploadTaskEnum.UpKan.getPathList().size >0 && UploadTaskEnum.UpKan.newAddList.size>0){
                return true
            }else if(UploadTaskEnum.UpKan.deleteList.size>0){
                //只是减少了图片
                return true
            }
            return false
        }else{
            if(UploadTaskEnum.DownKan.getPathList().size >0 && UploadTaskEnum.DownKan.newAddList.size>0){
                return true
            }else if(UploadTaskEnum.DownKan.deleteList.size>0){
                //只是减少了图片
                return true
            }
            return false
        }
    }

    override fun getView(): View {
        return View.inflate(this@TaskDetailActivity, R.layout.activity_task_detail,null)
    }


    var photoWindow: PopupWindow?=null
    /**
     * 点击头像，显示弹窗，设置新头像
     */
    public fun setPortraitDialog() {
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
            kanAdapter!!.editPosition=-1
            photoWindow!!.dismiss()
        })
    }


    /**
     * 带有权限的 对话框
     */
    public fun setPermissinPortraitDialog(){
        //先请求拍照权限
        requestPermission(BaseConstants.CAMERA, "android.permission.CAMERA", Runnable {
            //允许拍照权限
            //请求SD卡读写权限
            requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
                //允许SD卡读写权限
                setPortraitDialog()
            }, Runnable { ToastUtil.makeText(this@TaskDetailActivity, "读写存储卡权限未打开，请到手机权限中心设置打开...") })
        }, Runnable { ToastUtil.makeText(this@TaskDetailActivity, "相机权限未打开，请到手机权限中心设置打开...") })
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
                val point = DisplayUtil.getScreenPoint(this@TaskDetailActivity)
                val bitmap = ImageUtils.decodeBitmapWithOrientationMax(this@TaskDetailActivity, ImageUtils.getFilePathByFileUri(this@TaskDetailActivity, uri), point.x, point.y, true)
                var filePash = FileUtils.saveBitmap(this@TaskDetailActivity, bitmap, System.currentTimeMillis(), 1)
                if(photoWindow!=null && photoWindow!!.isShowing){
                    photoWindow!!.dismiss()
                }
                proImageShow(filePash)
//              startPhotoZoom(uri)
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (hasSdcard()) {
                val point = DisplayUtil.getScreenPoint(this@TaskDetailActivity)
                val bitmap = ImageUtils.decodeBitmapWithOrientationMax(this@TaskDetailActivity, tempFile!!.path, point.x, point.y, true)
                var filePash = FileUtils.saveBitmap(this@TaskDetailActivity, bitmap, System.currentTimeMillis(), 1)
                if(photoWindow!=null && photoWindow!!.isShowing){
                    photoWindow!!.dismiss()
                }
                proImageShow(filePash)
            } else {
                Toast.makeText(this@TaskDetailActivity, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show()
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

    private fun proImageShow(fileName: String) {
        if(kanAdapter!!.editPosition==-1){
            //新增
            var aptitudeInfo=TaskUpload(fileName)
            if(firstType==1){
                UploadTaskEnum.UpKan!!.newAddList.add(0, null)
            }else{
                UploadTaskEnum.DownKan!!.newAddList.add(0, null)
            }
            kanAdapter!!.newAddUpdate(aptitudeInfo)
        }else{
            //修改
            if(firstType==1){
                if(UploadTaskEnum.UpKan!!.newAddList.contains(kanAdapter!!.getItem(kanAdapter!!.editPosition))){
                     //如果包含
                    var index=UploadTaskEnum.UpKan!!.newAddList.indexOf( kanAdapter!!.getItem(kanAdapter!!.editPosition))
                    UploadTaskEnum.UpKan!!.newAddList.set(index,kanAdapter!!.getItem(kanAdapter!!.editPosition))
                }else{
                    UploadTaskEnum.UpKan!!.newAddList.add(0, kanAdapter!!.getItem(kanAdapter!!.editPosition))
                }
            }else{
                if(UploadTaskEnum.DownKan!!.newAddList.contains(kanAdapter!!.getItem(kanAdapter!!.editPosition))){
                    //如果包含
                    var index=UploadTaskEnum.DownKan!!.newAddList.indexOf( kanAdapter!!.getItem(kanAdapter!!.editPosition))
                    UploadTaskEnum.DownKan!!.newAddList.set(index,kanAdapter!!.getItem(kanAdapter!!.editPosition))
                }else{
                    UploadTaskEnum.DownKan!!.newAddList.add(0, kanAdapter!!.getItem(kanAdapter!!.editPosition))
                }
            }
            kanAdapter!!.imagePathList!!.get(kanAdapter!!.editPosition)!!.imgPath=fileName

            if(kanAdapter!!.imagePathList!!.size<3 &&
                      kanAdapter!!.editPosition==(kanAdapter!!.imagePathList!!.size-1)){
                //如果对应的长度 小于 3
                kanAdapter!!.imagePathList!!.add(TaskUpload())
            }
            kanAdapter!!.editPosition=-1
            kanAdapter!!.update( kanAdapter!!.imagePathList)
        }
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


    /**
     * 根据 上传类型 返回 boduy
     */
    private  fun  getMultiPartBody(uploadAptitudeEnum: UploadTaskEnum):List<MultipartBody.Part>{
        SystemUtil.printlnStr("uploadAptitudeEnum.getPathId():"+uploadAptitudeEnum.newAddList.size)
        var  parts= ArrayList<MultipartBody.Part>();
        for (data in uploadAptitudeEnum.newAddList) {
            SystemUtil.printlnStr("uploadAptitudeEnum data.address:"+data)
            if(data!=null && data.imgPath!=null && !data.imgPath.equals("")){
                var file=File(data.imgPath)
                var requestBody= RequestBody.create(MediaType.parse("imgage/png"),file);
                var  part= MultipartBody.Part.createFormData(uploadAptitudeEnum.getPathId(),file.getName(),requestBody);
                parts.add(part);
            }
        }
        return parts;
    }

    private fun getNewAddIds(arrayList:ArrayList<TaskUpload?>):Array<Int?>{
        var deleteIdInters= arrayOfNulls<Int>(arrayList.size)
        for(index in arrayList.indices){
            if(arrayList.get(index)==null){
                deleteIdInters.set(index,-1)
            }else{
                deleteIdInters.set(index,arrayList.get(index)!!.id)
            }
        }
        return deleteIdInters
    }
    /**
     * 获取得到  数组
     */
    private fun getDeleteIds(arrayList:ArrayList<Int>): Array<Int?> {
        var deleteIdArrays=ArrayList<Int>()
        for(data in arrayList){
            deleteIdArrays.add(data)
        }

        var deleteIdInters= arrayOfNulls<Int>(deleteIdArrays.size)
        for(index in deleteIdArrays.indices){
            deleteIdInters.set(index,deleteIdArrays.get(index))
        }
        return deleteIdInters
    }


    /**
     * 进入到 图片缩放
     */
    fun startPhotoZoom(uri: Uri?) {
        val intent = Intent(this@TaskDetailActivity, CropperActivity::class.java)
        intent.data = uri
        startActivityForResult(intent, PHOTO_REQUEST_CUT)
    }

    /**
     * 进入到 图片缩放
     */
    fun startPhotoZoom(path:String) {
        val intent = Intent(this@TaskDetailActivity, CropperActivity::class.java)
        intent.putExtra("path",path)
        startActivityForResult(intent, PHOTO_REQUEST_CUT)
    }
}