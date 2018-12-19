package com.github.zhangyang.camera_picker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.github.zhangyang.camera_picker.adapter.UploadImageAdapter;
import com.github.zhangyang.camera_picker.exception.ContentException;
import com.github.zhangyang.camera_picker.inner.BaseActivity;
import com.github.zhangyang.camera_picker.utils.Constants;
import com.github.zhangyang.camera_picker.utils.ImageUtils;
import com.github.zhangyang.camera_picker.utils.UiUtils;
import com.github.zhangyang.camera_picker.view.SelectPicPopupWindow;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    //popupWindow的实例
    SelectPicPopupWindow menuWindow;//PopupWindow实体类
    String mImagePath;//图片的路径

    EditText edit_title, edit_content;
    GridView gridView;
    ImageButton btn_Ganmbit_Sub;
    ImageView btnBack;
    private RelativeLayout rl_back;
    private ArrayList<String> mpressImgPathList = new ArrayList<String>();
    private UploadImageAdapter adapter;

    public String mPressImagePath;//压缩之后的文件的路径
    public final static String IMAGE_UNSPECIFIED="image/*";//格式
    //上传位置获取到的信息
    List<String> uploadFilePath=new ArrayList<String>();
    int uploadImgCount;//上传总的照片数
    private String title;

    final int messageImgWhat=204;//消息的what码
    final int messageImgError=205;//消息的what码
    final int submitDataSuccess=206;//上传文明监督提报成功请求码
    final int submitDataFailure=207;//上传文明监督提报成功失败码

    LinearLayout ll_img_des,ll_title,ll_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        customLayout=R.layout.activity_main;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        gridView = (GridView) findViewById(R.id.gambit_sub_upload);
        mpressImgPathList.add(null);
        adapter = new UploadImageAdapter(this,mpressImgPathList);
        adapter.setGridView(gridView);
        adapter.setImageNumber(Constants.COMMENT_SUP_MAX_IMG);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(mItemClick);
        gridView.setOnItemLongClickListener(mItemLongClick);
    }

    /**
     * 上传图片GridView Item单击监听
     */
    private AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            if(parent.getItemAtPosition(position) == null){ // 添加图片
                //showPictureDailog();//Dialog形式
                showPicturePopupWindow();//PopupWindow形式
            }else{
            }
        }
    };


    /**
     * 上传图片GridView Item长按监听
     */
    private AdapterView.OnItemLongClickListener mItemLongClick = new AdapterView.OnItemLongClickListener(){

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            System.out.println("parent.getItemAtPosition(position) :" + parent.getItemAtPosition(position) + "..position:" + position);
            if(parent.getItemAtPosition(position) != null){ // 长按删除
                mpressImgPathList.remove(parent.getItemAtPosition(position));
                adapter.updateRemove(MainActivity.this,mpressImgPathList); // 刷新图片
            }
            return true;
        }
    };
    @Override
    protected void initInitevnts() {

    }

    /**
     * 拍照或从图库选择图片(PopupWindow形式)
     */
    public void showPicturePopupWindow(){
        menuWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 隐藏弹出窗口
                menuWindow.dismiss();
                if(v.getId()==R.id.takePhotoBtn){
                    takePhoto();
                }else if(v.getId()==R.id.pickPhotoBtn){
                    mImagePath=null;
                    pickPhoto();
                }
            }
        });
        menuWindow.showAtLocation(findViewById(R.id.sub_gambit), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /***
     * 从相册中取图片
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, Constants.PCIK_IMAGE_RESULT_CODE);
    }

    /**
     * 拍照获取图片
     */
    private void takePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        // 执行拍照前，应该先判断SD卡是否存在
        /**
         * 通过指定图片存储路径，解决部分机型onActivityResult回调 data返回为null的情况
         */
        try {
            requestPermission(Constants.CAMERA_REQUEST_CODE, new String[]{"android.permission.CAMERA"}, new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, TakeCameraActivity.class);
                    intent.putStringArrayListExtra(Constants.IMAGE_LIST_KEY_NAME, adapter.getImagePathList());
                    intent.putExtra(Constants.MAX_TAKE_IMAGE_COUNT, Constants.COMMENT_SUP_MAX_IMG);
                    MainActivity.this.startActivityForResult(intent, Constants.TAKE_IMAGE_REQUEST_CODE);
                }
            }, new Runnable() {
                @Override
                public void run() {
                    UiUtils.getInstance().showToast(MainActivity.this,"请检测相机权限!");
                }
            });
        } catch (ContentException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this,e.getErrorContent(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.SELECT_IMAGE_RESULT_CODE && resultCode == RESULT_OK){//照相判断的业务逻辑
            String imagePath = "";
            if(data != null && data.getData() != null){//有数据返回直接使用返回的图片地址
                imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());
                startPhotoZoom(data.getData());
            }else{//无数据使用指定的图片路径s
                startPhotoZoom(Uri.fromFile(new File(mImagePath)));
            }
        }else if(requestCode == Constants.ZOOM_REQUEST_CODE){//缩减判断的逻辑
            //进行判断数据返回值
            if(data!=null && resultCode == Constants.ZOOM_RESULT_CODE){
                //进行图片压缩至比特流中
                mPressImagePath=data.getExtras().getString(Constants.FILE_PATH);
                //先进行去除
                mpressImgPathList.remove(null);
                mpressImgPathList.add(0, mPressImagePath);
                adapter.update(this,mpressImgPathList); // 刷新图片
            }else{
                System.out.println("requestCode == ZOOM_REQUEST_CODE else.....");
            }
        }else if(Constants.PCIK_IMAGE_RESULT_CODE==requestCode && resultCode == RESULT_OK ){
            if(data != null && data.getData() != null){//有数据返回直接使用返回的图片地址
                startPhotoZoom(data.getData());
            }
        }else if(requestCode == Constants.TAKE_IMAGE_REQUEST_CODE && resultCode == Constants.TAKE_IMAGE_RESULT_CODE){
            ArrayList<String> name=data.getExtras().getStringArrayList(Constants.IMAGE_LIST_KEY_NAME);
            Log.d(TAG, "TAKE_IMAGE_RESULT_CODE name is null:" + (name != null));
            if(name!=null && name.size()>=0){
                Log.d(TAG,"TAKE_IMAGE_RESULT_CODE name size:"+(name.size()));
                adapter.update(this, name);
                mpressImgPathList=adapter.getImagePathList();
            }
        }
    }

    public void startPhotoZoom(Uri uri){
        Intent intent=new Intent(this,CropperActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, Constants.ZOOM_REQUEST_CODE);
    }
}
