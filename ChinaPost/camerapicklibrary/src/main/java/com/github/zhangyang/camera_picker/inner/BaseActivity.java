package com.github.zhangyang.camera_picker.inner;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import com.github.zhangyang.camera_picker.exception.ContentException;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseActivity extends AppCompatActivity {

    protected int customLayout = 0;
    protected View contentView;
    public String TAG ="BaseActivtiy.class";
    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(customLayout);
        initViews();
        initInitevnts();
    }


    /**
     * 组件初始化
     */
    protected abstract void initViews();

    /**
     * 事件初始化
     */
    protected abstract void initInitevnts();


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public interface BeforeLogout {
        public void before();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 请求权限
     * @param id 请求授权的id 唯一标识即可
     * @param allowableRunnable 同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    protected void requestPermission(int id,String[] permissions, Runnable allowableRunnable, Runnable disallowableRunnable) throws ContentException {
        if(allowableRunnable!=null){
            allowablePermissionRunnables.put(id, allowableRunnable);
        }

        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }

        if(permissions==null || permissions.length<=0){
            throw new ContentException("输入的权限为空!");
        }
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限checkCallPhonePermission != PackageManager.PERMISSION_GRANTED
            int resultPermission=0;
            for(String permission:permissions){
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
                resultPermission=resultPermission|(checkCallPhonePermission|PackageManager.PERMISSION_GRANTED);
                System.out.println("requestPermission checkCallPhonePermission:"+checkCallPhonePermission+"...resultPermission:"+resultPermission);
            }
            if (resultPermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                ActivityCompat.requestPermissions(BaseActivity.this, permissions, id);
                return;
            } else {
                if(allowableRunnable!=null){
                    allowableRunnable.run();
                }
            }
        } else {
            int resultPermission=-1;
            for(String permission:permissions){
                int checkCallPhonePermission = PermissionChecker.checkSelfPermission(getApplicationContext(), permission);
                resultPermission=resultPermission&(checkCallPhonePermission&PackageManager.PERMISSION_GRANTED);
            }

            boolean result = resultPermission
                    == PermissionChecker.PERMISSION_GRANTED;
            if(!result){
                //如果未授权
                ActivityCompat.requestPermissions(BaseActivity.this, permissions, id);
            }else{
                if(allowableRunnable!=null){
                    allowableRunnable.run();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //防止有多个权限的请求 要分别得出 每个权限的结果值
        int resultGrant=PackageManager.PERMISSION_GRANTED;
        for(int i=0;i<permissions.length;i++){
            resultGrant=resultGrant+grantResults[i];
        }

        //如果结果值 相加 都为 PERMISSION_GRANTED
        if (resultGrant== PackageManager.PERMISSION_GRANTED) {
            Runnable allowRun = allowablePermissionRunnables.get(requestCode);
            if(allowRun!=null){
                allowRun.run();
            }
        } else {
            //否则 不允许的执行!
            Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
            if(disallowRun!=null){
                disallowRun.run();
            }
        }
    }
}
