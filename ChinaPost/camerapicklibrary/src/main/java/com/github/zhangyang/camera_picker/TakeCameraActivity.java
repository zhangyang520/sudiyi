package com.github.zhangyang.camera_picker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.github.zhangyang.camera_picker.adapter.GridViewAdapter;
import com.github.zhangyang.camera_picker.exception.ContentException;
import com.github.zhangyang.camera_picker.utils.*;
import com.github.zhangyang.camera_picker.view.MaskView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 思路:
 *    1：先进行布局,将框在上方,中间gridView层,下方按钮层
 *    2: 执行拍照的程序:预览阶段,拍照阶段,图片保存阶段(同时将图片显示在预览框中:可以进行删除)
 *    可以借鉴其他程序,但是不可直接地粘贴！
 */
public class TakeCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,Camera.PictureCallback {
    private SurfaceView surfaceView;//承载照相的数据类
    private MaskView maskView;//面具类
    private SurfaceHolder surfaceHolder;//surfaceView的hold类
    private String TAG="MainActivity";
    boolean hasCreateHolder;//是否已经创建
    Rect rect;//拍照区域
    int cameraRectHeight;
    GridView gridView;//相册部分
    List<String> pathList=new ArrayList<String>();//路径集合
    GridViewAdapter adapter;//gridView适配器
    RecyclerViewAdapter recyclerViewAdapter;
//    final int FILE_NUMBERS=3;//文件个数
    float rectRate=0.5f;//显示拍照框的占全部拍照框的比例
    float btnTakeRate=0.75f;//拍照按钮与其外框的比例
    float changeCameraRate=0.8f;//相框与黑色的外景之间的比例:
    float cammeraRate=0.75f;//相机拍照的比例:屏幕分辨率宽和高之间的比例
    float yRate;//实际拍摄的框的高度和整个手机屏幕高度之间的比例
    float xRate;//实际拍摄的框的宽度和整个手机屏幕宽度之间的比例
    ImageView change_camera;
    private int cameraId;//照相机的Id
    private int degrees;//相机的角度
    private Button btn_take;
    private RelativeLayout ll;

    private int maxImageCount=3;//最大的照片个数！
    public static final String MAX_TAKE_IMAGE_COUNT="MAX_TAKE_IMAGE_COUNT";
    public static final String LOGO_PORTRAIT="LOGO_PORTRAIT";

    //表示当前的图片的角标
    private int imgIndex =1;
    private boolean isStartFrontCamera;
    boolean isMashPortraitEnable;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    public boolean isMashPortraitEnable() {
        return isMashPortraitEnable;
    }

    public void setIsMashPortraitEnable(boolean isMashPortraitEnable) {
        this.isMashPortraitEnable = isMashPortraitEnable;
    }

    /**
     * 进行处理相机的打开
     */
    boolean hasStart;
    private final int MESSAGE_START_CAMERA=101;//相机的启动码
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                /**
                 * 进行使用锁进行同步打开相机:
                 * onRestart 和 surfaceView的surfaceCreated的同时进行的冲突
                 */
                case MESSAGE_START_CAMERA:
                    if(!hasStart && surfaceHolder!=null){
                        synchronized (TakeCameraActivity.this) {
                            if(!hasStart && surfaceHolder!=null){
                                hasStart=true;
                                startCamera(surfaceHolder);
                            }
                        }
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_takecamera);
        initView();
    }


     int gridViewColumnWidth;
    /**
     * 进行初始化view
     */
    private void initView(){
        isMashPortraitEnable=getIntent().getBooleanExtra(LOGO_PORTRAIT,false);
        isStartFrontCamera=isMashPortraitEnable;
        surfaceView=(SurfaceView)findViewById(R.id.surfaceView);
        maskView=(MaskView)findViewById(R.id.maskView);
        gridView=(GridView)findViewById(R.id.gridView);
        btn_take=(Button)findViewById(R.id.btn_take);
        change_camera=(ImageView) findViewById(R.id.change_camera);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        if(isMashPortraitEnable){
            change_camera.setBackgroundResource(R.drawable.camera_black);
        }
        ll=(RelativeLayout) findViewById(R.id.ll);
        final Point point= DisplayUtil.getScreenPoint(this);
        System.out.println("point toString:"+point.toString());
        //比较进行获取相框的高度:
        int  blackHeight=(int)(change_camera.getLayoutParams().height/changeCameraRate);
        maskView.setBlackHeight(blackHeight);
        maskView.setIsCircleEnable(isMashPortraitEnable);
        maskView.invalidate();
        int  realCameraHeight=blackHeight*2+(int)(point.x*MaskView.heghtDividWidthRate);

        //进行与半个的相框的高度比较
        if(point.y*rectRate>realCameraHeight){
            cameraRectHeight=(int)(point.y*rectRate);
        }else{
            cameraRectHeight=realCameraHeight;
        }
        this.rect=new Rect(0,0,point.x,cameraRectHeight);
        maskView.setRect(this.rect);
        surfaceView.getHolder().addCallback(this);

        //进行获取文件名称列表
        List<String> datas=getIntent().getExtras().getStringArrayList(Constants.IMAGE_LIST_KEY_NAME);
        if(datas!=null){
            pathList=datas;
            if(pathList.contains(null))
                pathList.remove(null);
            //进行初始化图片的角标
            imgIndex=pathList.size()+1;
        }
        //进行获取上一ativitity传递的MAX_TAKE_IMAGE_COUNT的值
        maxImageCount=getIntent().getExtras().getInt(MAX_TAKE_IMAGE_COUNT,maxImageCount);
//        pathList= FileUtils.getFilePath(3);
        final ViewTreeObserver observer=surfaceView.getViewTreeObserver();
        /*
         * 等全部的布局完成之后,
         * 设置gridView的高以及每张图片的宽度:gridViewColumnWidth
         */
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //进行计算gridView的高度和宽度
                int gridColumnNums= Constants.LINE_IMG_COUNT;
                 gridViewColumnWidth = (point.x- (int) ((gridColumnNums-1) * //
                                                    getResources().getDimension(R.dimen.gridview_column_space)))/ gridColumnNums;
                //gridView.setNumColumns(maxImageCount);
                gridView.setColumnWidth(gridViewColumnWidth);

                int gridViewHeight=(int)(gridViewColumnWidth*MaskView.heghtDividWidthRate+gridView.getPaddingTop());
//                gridView.getLayoutParams().height = gridViewHeight;
//                gridView.requestLayout();
//                Log.d(TAG, "point.x:" + point.x + "..getNumColumns:" + gridColumnNums + "..gridview_column_space:" + getResources().getDimension(R.dimen.gridview_column_space));
//
//                adapter = new GridViewAdapter(pathList, TakeCameraActivity.this);
//                adapter.setImageWidth(gridViewColumnWidth);
//                gridView.setAdapter(adapter);

                //进行设置高度
                recyclerView.getLayoutParams().height=gridViewHeight;
                recyclerView.requestLayout();

                recyclerViewAdapter=new RecyclerViewAdapter();
                linearLayoutManager=new LinearLayoutManager(TakeCameraActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerViewAdapter);


                //进行设置btnTake的宽度和高度
                int llBtnTakeHeight=point.y-cameraRectHeight-gridViewHeight;
                btn_take.getLayoutParams().height=(int)(llBtnTakeHeight*changeCameraRate);
                btn_take.getLayoutParams().width=(int)(llBtnTakeHeight*changeCameraRate);
                btn_take.requestLayout();
                ll.getLayoutParams().height=llBtnTakeHeight;
                ll.requestLayout();

                surfaceView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    /**
     * 改变摄像头的方向
     * @param view
     */
    public void changeCamera(View view){
        if(cameraId== Camera.CameraInfo.CAMERA_FACING_FRONT){
            cameraId=getBackCameraId();
        }else{
            cameraId=getFrontCameraId();
        }

        if(cameraId== Camera.CameraInfo.CAMERA_FACING_BACK){
            isStartFrontCamera=false;
        }else{
            isStartFrontCamera=true;
        }
        System.out.println("changeCamera isStartFrontCammera :"+isStartFrontCamera);
        restartCamera();
    }
    /**
     * 取消
     * @param view
     */
   public void cacelBtn(View view){
       Intent intent=new Intent();
       intent.putStringArrayListExtra(Constants.IMAGE_LIST_KEY_NAME,null);
       setResult(Constants.TAKE_IMAGE_RESULT_CODE,intent);
       finish();
   }

    /**
     * 完成
     * @param view
     */
    public void completeBtn(View view){
        Intent intent=new Intent();
        intent.putStringArrayListExtra(Constants.IMAGE_LIST_KEY_NAME, (ArrayList) pathList);
        setResult(Constants.TAKE_IMAGE_RESULT_CODE, intent);
        finish();
    }

    boolean isPictureNow;
    /**
     * 拍照的点击按钮
     * @param view
     */
    public void takePicture(View view){
        if(!isPictureNow){
            synchronized (TakeCameraActivity.this){
                if(imgIndex >maxImageCount){
                    //进行提示用户不能超过最大数
                    UiUtils.getInstance().showToast(this,"只能拍摄"+maxImageCount+"照片!");
                }else{
                    if(camera!=null && !isPictureNow){
                        isPictureNow=true;
                        camera.takePicture(new Camera.ShutterCallback() {
                            @Override
                            public void onShutter() {
                            }
                        }, null, this);
                    }else{
                        Log.d(TAG, "takePicture false");
                    }
                }
            }
        }
    }

    /**
     * 响应surfacaHolder的回调函数
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder=holder;
        //进行启动照相机
        handler.sendEmptyMessage(MESSAGE_START_CAMERA);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceHolder=null;
        hasStart=false;
    }

    boolean isCameraError;
    Camera camera;
    Camera.CameraInfo cameraInfo;
    /**
     * 进行启动相机
     */
    private void startCamera(SurfaceHolder surfaceHolder){
        try {
            if(isMashPortraitEnable &&isStartFrontCamera){
                cameraId=(getFrontCameraId());
            }else{
                cameraId=(getBackCameraId());
            }
            camera= Camera.open(cameraId);
            //进行需要设置预览和拍照的参数
            initCameraParams();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "打开相机失败!", Toast.LENGTH_SHORT).show();
            isCameraError=true;
            finish();
        }catch(RuntimeException e){
                Toast.makeText(this, "打开相机失败!", Toast.LENGTH_SHORT).show();
                isCameraError=true;
                finish();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.sendEmptyMessage(MESSAGE_START_CAMERA);
    }

    /**
     * 进行启动相机
     */
    private void startFrontCamera(SurfaceHolder surfaceHolder){
        try {
            camera= Camera.open(cameraId);
            //进行需要设置预览和拍照的参数
            initFrontCameraParams();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }catch(RuntimeException e){
            if(camera==null){
                Toast.makeText(this, "打开相机失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    /**
     * 关闭照相
     */
    private void stopCamera(){
        camera.stopPreview();
        camera.release();
        camera=null;
    }

    /**
     * 重启相机
     */
    private void restartCamera(){
        if(camera!=null){
            camera.stopPreview();
            camera.release();
            camera=null;
        }
        //启动前置摄像头
        if(isStartFrontCamera){
            startFrontCamera(surfaceHolder);
        }else{
            startCamera(surfaceHolder);
        }

    }
    /**
     * 进行获取后置摄像头的Id
     * 但是在获取前置摄像头的时候,需要进行判断一下,有没有这个属性
     * @return
     */
    public int getBackCameraId(){
        return  Camera.CameraInfo.CAMERA_FACING_BACK;
    }

    /**
     * 进行返回前置的摄像头的id
     * @return
     */
    public int getFrontCameraId(){
        //首先进行判断是否有前置摄像头
        PackageManager pm=getPackageManager();
        if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
            return Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
        //如果没有进行默认返回后置摄像头
        return getBackCameraId();
    }

    /**
     * 进行初始化照相机：
     *   预览,拍照的参数
     *   展现的方向
     *   拍照后保存图片的格式
     *   展现
     */
    Camera.Size pictureSize;
    Camera.Size previewSize;
    public void initCameraParams(){
        Camera.Parameters parameters=camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        //进行初始化surfaceView的高度
        surfaceView.getLayoutParams().height=(int)(UiUtils.getInstance().getDefaultWidth(this)/cammeraRate);
        surfaceView.requestLayout();
        yRate=cammeraRate;
        xRate=surfaceView.getMeasuredWidth()/ UiUtils.getInstance().getDefaultWidth(this);
        System.out.println("yRate:"+yRate+"..xRate:"+xRate);
        previewSize= CamParaUtil.getInstance().getPropPreviewSize(parameters.getSupportedPreviewSizes(),cammeraRate);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        CamParaUtil.getInstance().printSupportPreviewSize(parameters);
        pictureSize=CamParaUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), cammeraRate);
        parameters.setPictureSize(pictureSize.width,pictureSize.height);
        CamParaUtil.getInstance().printSupportPictureSize(parameters);

        List<String> focusModes = parameters.getSupportedFocusModes();
        if(focusModes.contains("continuous-video")){
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        camera.setParameters(parameters);
        //以90度展现
        camera.setDisplayOrientation(90);
    }

    /**
     * 进行初始化照相机：
     *   预览,拍照的参数
     *   展现的方向
     *   拍照后保存图片的格式
     *   展现
     */
    public void initFrontCameraParams(){
        Camera.Parameters parameters=camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        //进行初始化surfaceView的高度
        surfaceView.getLayoutParams().height=(int)(UiUtils.getInstance().getDefaultWidth(this)/cammeraRate);
        surfaceView.requestLayout();
        yRate=cammeraRate;
        xRate=surfaceView.getMeasuredWidth()/UiUtils.getInstance().getDefaultWidth(this);
        System.out.println("yRate:" + yRate+"..xRate:"+xRate);
        previewSize= CamParaUtil.getInstance().getPropPreviewSize(parameters.getSupportedPreviewSizes(),cammeraRate);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
        CamParaUtil.getInstance().printSupportPreviewSize(parameters);
        pictureSize=CamParaUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), cammeraRate);
        parameters.setPictureSize(pictureSize.width,pictureSize.height);
        CamParaUtil.getInstance().printSupportPictureSize(parameters);

        List<String> focusModes = parameters.getSupportedFocusModes();
        if(focusModes.contains("continuous-video")){
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        camera.setParameters(parameters);
        //以90度展现
        camera.setDisplayOrientation(90);
    }
    /**
     * Determine the current display orientation and rotate the camera preview
     * accordingly
     */
    private void determineDisplayOrientation() {
        // Clockwise rotation needed to align the window display to the natural position
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: {
                degrees = 0;
                break;
            }
            case Surface.ROTATION_90: {
                degrees = 90;
                break;
            }
            case Surface.ROTATION_180: {
                degrees = 180;
                break;
            }
            case Surface.ROTATION_270: {
                degrees = 270;
                break;
            }
        }

        int displayOrientation;

        // CameraInfo.Orientation is the angle relative to the natural position of the device
        // in clockwise rotation (angle that is rotated clockwise from the natural position)
        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // Orientation is angle of rotation when facing the camera for
            // the camera image to match the natural orientation of the device
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(displayOrientation);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(camera!=null && !isCameraError){
            hasStart=false;
            stopCamera();
        }
//        mOrientationListener.disable();
    }

    /**
     * 响应拍照的回调函数
     * @param data
     * @param camera
     */
    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        //进行获取bitmap的字节数组
        long currentTime= System.currentTimeMillis();

        Bitmap b = null;
        if(null != data){
            //进行缩至原图的startScale 以便能够减小图片的大小
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);

            Log.d("MainActivity", "options width:" + options.outWidth + "...options height:" + options.outHeight);
            int targetDensity = getResources().getDisplayMetrics().densityDpi;
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);


            //接近目标比例
            int x = 600;
            int y =800;
            options.inSampleSize=calculateInSampleSize(options,x,y);
//            UiUtils.getInstance().showToast("inSampleSize:"+options.inSampleSize+"...requestX:"+x+"...requestY;"+y);
//            //进行将原始的图保存到文件中
//            try {
//                FileUtils.saveBitmapBytes(data,currentTime,-1);
//            } catch (ContentException e) {
//                e.printStackTrace();
//            }
            options.inScaled = true;
            //防止分辨率过小
            if(options.outHeight*options.outWidth<x*y){
                options.inDensity = (int) (targetDensity);//乘以一个固定值
            }else{
                options.inDensity = (int) (targetDensity*1.5);//乘以一个固定值
            }

            options.inTargetDensity = targetDensity;
            options.inJustDecodeBounds = false;
            b= BitmapFactory.decodeByteArray(data, 0, data.length, options);
            camera.stopPreview();
        }


        //保存图片到sdcard
        if(null != b)
        {
            //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。
            //图片竟然不能旋转了，故这里要旋转下
            int rotaion=0;
            if(cameraId==getBackCameraId()){
                System.out.println("isMashPortraitEnable 11111");
                rotaion=90;
            }else{
                if(cameraId==getBackCameraId()){
                    System.out.println("isMashPortraitEnable 222");
                    rotaion=90;
                }else{
                    if(isMashPortraitEnable){
                        System.out.println("isMashPortraitEnable "+isMashPortraitEnable);
                        rotaion=-90;
                    }else{
                        System.out.println("isMashPortraitEnable "+false);
                        rotaion=-90;
                    }
                }
            }
            Bitmap rotaBitmap = ImageUtils.getRotateBitmap(b, rotaion);
//            //保存原图
//            try {
//                FileUtils.saveBitmap(rotaBitmap,currentTime,1);
//            } catch (ContentException e) {
//                e.printStackTrace();
//            }
            Log.d(TAG, "rotaBitmap width:" + rotaBitmap.getWidth() + "..rotaBitmap height:" + rotaBitmap.getHeight() + "this.rect.top:" + this.rect.top + "..this.rect.left:" +
                    this.rect.left + "..,this.rect.width():" + this.rect.width() + "..this.rect.height():" + this.rect.height());
            Bitmap rectBitmap;
            if(isStartFrontCamera){//前置摄像头
                System.out.println("isStartFrontCamera :"+isStartFrontCamera);
                //进行在旋转图的基础上进行截取部分
                if(maskView.isCircleEnable()){
                    System.out.println("isStartFrontCamera :"+isStartFrontCamera+"....isCircleEnable true");
                    //如果带有圆形图片
                    float grayRate=(MaskView.leftLength*2f)/((float)cameraRectHeight/rectRate);//原始的蓝色的灰色部分占拍照的全部屏幕的比例
                    float scaleBlueLength=((float)rotaBitmap.getHeight())*grayRate;
                    int radius=(int) Math.min(rotaBitmap.getWidth() / 2, (int) (((float) rotaBitmap.getHeight() * rectRate - scaleBlueLength) / yRate) / 2);
                    int x=rotaBitmap.getWidth()/2-radius;
                    int y=(int)((scaleBlueLength/2f)/yRate);
//                    Bitmap roundBitmap=Bitmap.createBitmap(rotaBitmap,x,y,rotaBitmap.getWidth(),(int)(((float)rotaBitmap.getHeight()*rectRate-scaleBlueLength)/yRate));
                    Bitmap roundBitmap= Bitmap.createBitmap(rotaBitmap, x, y, radius * 2, radius * 2);
                    rectBitmap=getCircularBitmap(roundBitmap);
                    FileUtils.saveBitmapPng(this,rectBitmap,pathList,80);
                }else{
                    System.out.println("isStartFrontCamera :"+isStartFrontCamera+"....isCircleEnable false");
                    float grayRate=(MaskView.leftLength*2f)/((float)cameraRectHeight/rectRate);//原始的蓝色的灰色部分占拍照的全部屏幕的比例
                    float scaleBlueLength=((float)rotaBitmap.getHeight())*grayRate;
                    rectBitmap= Bitmap.createBitmap(rotaBitmap, 0, (int) ((scaleBlueLength / 2f) / yRate), rotaBitmap.getWidth(), (int) (((float) rotaBitmap.getHeight() * rectRate - scaleBlueLength) / yRate));
                    //保存jpg格式
                    try {
                        FileUtils.saveBitmapJPG(this,rectBitmap,pathList,70);
                    } catch (ContentException e) {
                        e.printStackTrace();
                    }
                }
            }else{
//                System.out.println("isStartFrontCamera :"+isStartFrontCamera);
//                //进行在旋转图的基础上进行截取部分
//                float grayRate=(MaskView.leftLength*2f)/((float)cameraRectHeight/rectRate);//原始的蓝色的灰色部分占拍照的全部屏幕的比例
//                float scaleBlueLength=((float)rotaBitmap.getHeight())*grayRate;
//                rectBitmap=Bitmap.createBitmap(rotaBitmap,0,(int)((scaleBlueLength/2f)/yRate),rotaBitmap.getWidth(),(int)(((float)rotaBitmap.getHeight()*rectRate-scaleBlueLength)/yRate));
                if(maskView.isCircleEnable()){
                    System.out.println("isStartFrontCamera :"+isStartFrontCamera+"....isCircleEnable true");
                    //如果带有圆形图片
                    float grayRate=(MaskView.leftLength*2f)/((float)cameraRectHeight/rectRate);//原始的蓝色的灰色部分占拍照的全部屏幕的比例
                    float scaleBlueLength=((float)rotaBitmap.getHeight())*grayRate;
//                    Bitmap roundBitmap=Bitmap.createBitmap(rotaBitmap,0,(int)((scaleBlueLength/2f)/yRate),rotaBitmap.getWidth(),(int)(((float)rotaBitmap.getHeight()*rectRate-scaleBlueLength)/yRate));
                    int radius=(int) Math.min(rotaBitmap.getWidth() / 2, (int) (((float) rotaBitmap.getHeight() * rectRate - scaleBlueLength) / yRate) / 2);
                    int x=rotaBitmap.getWidth()/2-radius;
                    int y=(int)((scaleBlueLength/2f)/yRate);
//                    Bitmap roundBitmap=Bitmap.createBitmap(rotaBitmap,x,y,rotaBitmap.getWidth(),(int)(((float)rotaBitmap.getHeight()*rectRate-scaleBlueLength)/yRate));
                    Bitmap roundBitmap= Bitmap.createBitmap(rotaBitmap, x, y, radius * 2, radius * 2);
                    rectBitmap=getCircularBitmap(roundBitmap);
                    FileUtils.saveBitmapPng(this,rectBitmap,pathList,80);
                }else{
                    System.out.println("isStartFrontCamera :"+isStartFrontCamera+"....isCircleEnable false");
                    float grayRate=(MaskView.leftLength*2f)/((float)cameraRectHeight/rectRate);//原始的蓝色的灰色部分占拍照的全部屏幕的比例
                    float scaleBlueLength=((float)rotaBitmap.getHeight())*grayRate;
                    rectBitmap= Bitmap.createBitmap(rotaBitmap, 0, (int) ((scaleBlueLength / 2f) / yRate), rotaBitmap.getWidth(), (int) (((float) rotaBitmap.getHeight() * rectRate - scaleBlueLength) / yRate));
                    //保存jpg格式
                    try {
                        FileUtils.saveBitmapJPG(this,rectBitmap, pathList, 70);
                    } catch (ContentException e) {
                        e.printStackTrace();
                    }
                }
            }

            //保存截取之后的图片 rectBitmap
//            try {
//                FileUtils.saveBitmap(rectBitmap, currentTime,2,pathList);
//            } catch (ContentException e) {
//                e.printStackTrace();
//            }

//            //对截取的图片进行压缩:
//            //保存截取之后的图片 rectBitmap
//            try {
//                FileUtils.saveBitmapJPG(rectBitmap, currentTime,3,70);
//            } catch (ContentException e) {
//                e.printStackTrace();
//            }
            recyclerViewAdapter.notifyDataSetChanged();
//            linearLayoutManager.scrollToPosition(0);
            if(rotaBitmap.isRecycled()){
                rotaBitmap.recycle();
                rotaBitmap = null;
            }
            if(rectBitmap.isRecycled()){
                rectBitmap.recycle();
                rectBitmap = null;
            }
        }
        //再次进入预览
        camera.startPreview();
        if(!b.isRecycled()){
            b.recycle();
            b = null;
        }
        //拍完照周进行重置！
        isPictureNow=false;
        //图片的角标++
        imgIndex++;
    }

    public Bitmap getCircularBitmap(Bitmap square) {

        int halfWidth = square.getWidth() / 2;
        int halfHeight = square.getHeight() / 2;

        if (square == null) return null;
//        Bitmap output = Bitmap.createBitmap(Math.min(halfWidth, halfHeight)*2, Math.min(halfWidth, halfHeight)*2,
//                Bitmap.Config.ARGB_8888);
//
//        final Rect rect = new Rect(0, 0, Math.min(halfWidth, halfHeight)*2, Math.min(halfWidth, halfHeight)*2);

        Bitmap output = Bitmap.createBitmap(square.getWidth(), square.getHeight(),
                Bitmap.Config.ARGB_8888);

        final Rect rect = new Rect(0, 0, square.getWidth(), square.getHeight());
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(halfWidth, halfHeight, Math.min(halfWidth, halfHeight), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(square, rect, rect, paint);
        return output;
    }
    /**
     * 计算采样率的大小
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        // Raw height and width of image
        int height;
        int width;
        if(options.outWidth>options.outHeight){
            height = options.outWidth;
            width = options.outHeight;
        }else{
            height = options.outHeight;
            width = options.outWidth;
        }
//        final int height = options.outHeight;
//        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

           /* if(inSampleSize==1){
                inSampleSize=2;
            }*/
        }

        Log.d("MainActivity", "inSampleSize:" + inSampleSize);
        return inSampleSize;
    }

    public int getImgIndex() {
        return imgIndex;
    }

    public void setImgIndex(int imgIndex) {
        this.imgIndex = imgIndex;
    }



   class RecyclerViewAdapter extends RecyclerView.Adapter<ImageViewHoder>{
       public RecyclerViewAdapter() {
           super();
       }

       @Override
       public void onBindViewHolder(ImageViewHoder holder, final int position) {


               if(isMashPortraitEnable()){
                   holder.imageView.getLayoutParams().width=(int)(gridViewColumnWidth* MaskView.heghtDividWidthRate);
               }else{
                   holder.imageView.getLayoutParams().width=gridViewColumnWidth;
               }
           holder.imageView.getLayoutParams().height=(int)(gridViewColumnWidth* MaskView.heghtDividWidthRate);
           Log.d("gridView getView", "imageWidth:" + gridViewColumnWidth);
           holder.imageView.requestLayout();


           holder.cancelImageView.setOnClickListener(new View.OnClickListener(){
               @Override
               public void onClick(View v) {
                   pathList.remove(position);
                   notifyDataSetChanged();
                   setImgIndex(getImgIndex() - 1);
               }
           });
           BitmapUtil.getInstance(TakeCameraActivity.this).displayImg(holder.imageView,pathList.get(position),R.drawable.supervise_upload_img);
           //对图片进行解析
       }

       @Override
       public ImageViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
           return new ImageViewHoder(View.inflate(TakeCameraActivity.this,R.layout.item_take_image,null));
       }

       @Override
       public int getItemCount() {
           return pathList.size();
       }
   }

    class ImageViewHoder<String> extends RecyclerView.ViewHolder{
         ImageView imageView;
         ImageView cancelImageView;

        public ImageViewHoder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        /**
         * 进行初始化view
         * @param itemView
         */
        private void initView(View itemView){
            imageView=(ImageView)itemView.findViewById(R.id.iv);
            cancelImageView=(ImageView)itemView.findViewById(R.id.iv_cancel);
        }
    }
}
