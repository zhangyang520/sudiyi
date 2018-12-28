package com.suntray.chinapost.baselibrary.ui.activity

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.suntray.chinapost.baselibrary.R
import java.util.*
import android.support.v4.app.ActivityCompat
import android.support.v4.content.PermissionChecker
import android.support.v4.content.ContextCompat
import com.suntray.chinapost.baselibrary.common.AppManager
import com.suntray.chinapost.baselibrary.ui.dialog.LuyinTipDialog
import com.trello.rxlifecycle.components.support.RxFragmentActivity
import kotlinx.android.synthetic.main.layout_title.*


/**
 * Created by ASUS on 2018/5/25.
 * 基础的activity
 */
open abstract class BaseFragmenAcvitiy: BaseAcvitiy() {

}