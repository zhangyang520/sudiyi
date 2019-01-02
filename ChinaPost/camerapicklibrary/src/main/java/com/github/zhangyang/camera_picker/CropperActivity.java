package com.github.zhangyang.camera_picker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.zhangyang.camera_picker.exception.ContentException;
import com.github.zhangyang.camera_picker.inner.BaseActivity;
import com.github.zhangyang.camera_picker.utils.Constants;
import com.github.zhangyang.camera_picker.utils.DisplayUtil;
import com.github.zhangyang.camera_picker.utils.FileUtils;
import com.github.zhangyang.camera_picker.utils.ImageUtils;
import com.github.zhangyang.camera_picker.view.CropImageView;
import com.github.zhangyang.camera_picker.view.MaskView;
import com.suntray.chinapost.provider.RouterPath;


/**
 * zhangyang 图片剪切的activity
 */
@Route(path = RouterPath.PickerModule.CROP_ACTIVITY)
public class CropperActivity extends BaseActivity implements View.OnClickListener {
    CropImageView imageView;//切图的显示
    Button btn_complete,btn_cancel;
    public static String PORTRAIT_IMAGE="PORTRAIT_IMAGE";//头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        customLayout=R.layout.activity_cropper;
        super.onCreate(savedInstanceState);
    }

    boolean isPortrait;
    @Override
    protected void initViews() {
        isPortrait=getIntent().getBooleanExtra(PORTRAIT_IMAGE,false);
        imageView=(CropImageView)findViewById(R.id.iv_crop);
        if(isPortrait){
            MaskView.heghtDividWidthRate=0.7f;
            imageView.setIsCircleEnable(true);
            imageView.invalidate();
        }else{
            MaskView.heghtDividWidthRate=1.0f;
            imageView.setIsCircleEnable(false);
            imageView.invalidate();
        }
        btn_complete=(Button)findViewById(R.id.btn_complete);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        String path=getIntent().getStringExtra("path");
        if(path!=null && !path.equals("")){
            Point point= DisplayUtil.getScreenPoint(this);
            Bitmap bitmap= ImageUtils.decodeBitmapWithOrientationMax(this,path, point.x, point.y, isPortrait);
            imageView.setImageBitmap(bitmap);
        }else{
            Uri uri=getIntent().getData();
            Point point= DisplayUtil.getScreenPoint(this);
            Bitmap bitmap= ImageUtils.decodeBitmapWithOrientationMax(this,ImageUtils.getFilePathByFileUri(this, uri), point.x, point.y, isPortrait);
            imageView.setImageBitmap(bitmap);
        }



        btn_complete.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    protected void initInitevnts() {

    }

    /**
     * 图片剪切完成按钮
     * @param view
     */
    public void completeBtn(Context context,View view){
        //图片的剪切完成
        try {
            Bitmap rectBitmap=imageView.getRectBitmap();
            String filePath= FileUtils.saveBitmapPng(context,rectBitmap, 100);
            //进行界面的消失
            Intent intent=new Intent();
            intent.getExtras().putString(Constants.FILE_PATH,filePath);
            setResult(Constants.ZOOM_RESULT_CODE, intent);
            finish();
        } catch (ContentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_complete){
            try {
                Bitmap rectBitmap;
                String filePath;
                if(isPortrait){
                    rectBitmap=imageView.getCroppedBitmap();
                    if(rectBitmap!=null){
                        filePath=FileUtils.saveBitmapPng(this,rectBitmap, 100);
                    }else{
                        return ;
                    }
                }else{
                    rectBitmap=imageView.getRectBitmap();
                    if(rectBitmap!=null){
                        filePath=FileUtils.saveBitmapJPG(this,rectBitmap,96);
                    }else{
                        return;
                    }
                }
                System.out.println("btn_complete onClick: rectBitmap.height:"+rectBitmap.getHeight()+"..rectBitmap.width:"+rectBitmap.getWidth());
                //进行界面的消失
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString(Constants.FILE_PATH, filePath);
                intent.putExtras(bundle);
                setResult(Constants.ZOOM_RESULT_CODE, intent);
                finish();
            } catch (ContentException e) {
                e.printStackTrace();
            }
        }else if(v.getId()==R.id.btn_cancel){
            finish();
        }
    }
}
