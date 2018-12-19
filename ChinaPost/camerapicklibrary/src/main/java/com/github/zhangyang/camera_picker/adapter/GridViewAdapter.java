package com.github.zhangyang.camera_picker.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.github.zhangyang.camera_picker.R;
import com.github.zhangyang.camera_picker.TakeCameraActivity;
import com.github.zhangyang.camera_picker.utils.BitmapUtil;
import com.github.zhangyang.camera_picker.view.MaskView;

import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class GridViewAdapter extends BaseAdapter {
    List<String> imgPath;
    int imageWidth;
    Context takeCameraActivity;
    public GridViewAdapter(List<String> imgPath, Context takeCameraActivity) {
        this.imgPath=imgPath;
        this.takeCameraActivity=takeCameraActivity;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    @Override
    public int getCount() {
        return imgPath.size();
    }

    @Override
    public Object getItem(int position) {
        return imgPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            convertView= View.inflate(takeCameraActivity, R.layout.item_take_image, null);
            viewHolder=new ViewHolder();
            viewHolder.imageView=(ImageView)convertView.findViewById(R.id.iv);

            if(((TakeCameraActivity)takeCameraActivity).isMashPortraitEnable()){
                viewHolder.imageView.getLayoutParams().width=(int)(imageWidth* MaskView.heghtDividWidthRate);
            }else{
                viewHolder.imageView.getLayoutParams().width=imageWidth;
            }
            viewHolder.imageView.getLayoutParams().height=(int)(imageWidth* MaskView.heghtDividWidthRate);
            Log.d("gridView getView", "imageWidth:" + imageWidth);
            viewHolder.imageView.requestLayout();
            viewHolder.cancelImageView=(ImageView)convertView.findViewById(R.id.iv_cancel);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.cancelImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imgPath.remove(position);
                notifyDataSetChanged();
                ((TakeCameraActivity)takeCameraActivity).setImgIndex(((TakeCameraActivity)takeCameraActivity).getImgIndex()-1);
            }
        });
        BitmapUtil.getInstance(takeCameraActivity).displayImg(viewHolder.imageView,imgPath.get(position),R.drawable.supervise_upload_img);
        //对图片进行解析
        return convertView;
    }

    class ViewHolder{
        ImageView imageView,cancelImageView;
    }
}
