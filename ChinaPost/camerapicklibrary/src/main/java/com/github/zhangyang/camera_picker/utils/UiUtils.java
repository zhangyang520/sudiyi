package com.github.zhangyang.camera_picker.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;


/**
 * 描 述 ： 进行创建UI的相关的工具方法
 **/
public class UiUtils {

    private static UiUtils instance;

    Handler handler=new Handler();
    private UiUtils() {

    }

    public static UiUtils getInstance() {
        if (instance == null) {
            synchronized (UiUtils.class) {
                if (instance == null) {
                    instance = new UiUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 进行获取资源类
     *
     * @return
     */
    private Resources getResources(Context context) {

        return context.getResources();
    }


    /**
     * 进行获取图片的Drawable
     *
     * @param
     * @return
     */
    public Drawable getDrawable(Context context,int drawableId) {

        return getResources(context).getDrawable(drawableId);
    }

    public static int getDimen(Context context,int id) {

        return (int) (UiUtils.getInstance().getResources(context).getDimension(id) + 0.5f);
    }


    /**
     * 进行实现runOnUiThread
     *
     * @param runnable
     */
    public void runOnUiThread(Runnable runnable) {

        // 首先进行判断是不是主线程
        if (android.os.Process.myTid() == //
                android.os.Process.myTid()) {
            // 如果是主线程,进行运行runnable
            runnable.run();
        } else {
            // 如果不是主线程,进行将方法进行提交都主线程
            handler.post(runnable);
        }
    }

    /**
     * 进行获取Handler
     */
    public Handler getHandler() {
        return handler;
    }

    /**
     * 进行显示信息
     *
     * @param msg
     */
    public void showToast(final  Context context,final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 进行获取屏幕的宽度
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public int getDefaultWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 进行获取屏幕的高度
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public int getDefaultHeight(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }
}
