package com.suntray.chinapost.map.utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Looper
import android.support.v4.content.FileProvider
import android.view.View
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.ui.activity.BaseAcvitiy
import com.suntray.chinapost.baselibrary.utils.SDCardUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

/**
 *  更新的业务
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/8 15:57
 */
object UpdateUtils {

    /**
     * 更新 app版本的业务
     */
    fun  updateApp(version: Float, newVersion: Float,appUrl: String,fixBug: String,loginActivity: BaseAcvitiy){
        SystemUtil.printlnStr("appUrl:"+appUrl+"..newVersion:"+newVersion+"..version:"+version)
        if (version >= newVersion) {
            //如果版本相等 不做处理
            return
        }
        //更新状态 ( 0强制更新 1非强制更新 )
        AlertDialog.Builder(loginActivity)
                .setCancelable(false)
                .setMessage("为保证您享受到更完善的系统体验，请更新到最新版本")
                .setPositiveButton("立即更新", object:DialogInterface.OnClickListener{
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dialog!!.dismiss()
                        showDownloadProgressDialog(loginActivity, appUrl,newVersion,loginActivity)
                    }
                })
                .show()
    }


    //显示进度弹窗，不可取消，只能下载
    private fun showDownloadProgressDialog(context: Context, downloadUrl: String, newVersion: Float, loginActivity: BaseAcvitiy) {
        //测试数据，后期注释掉11111111111111
        val progressDialog = ProgressDialog(context)
        loginActivity.requestPermission(BaseConstants.WRITE_EXTERNAL_STORAGE, "android.permission.WRITE_EXTERNAL_STORAGE", Runnable {
            //允许SD卡读写权限
            progressDialog.setTitle("提示")
            progressDialog.setMessage("正在下载...")
            progressDialog.setIndeterminate(false)
            progressDialog.setMax(100)
            progressDialog.setCancelable(false)
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            progressDialog.show()
            DownloadAPK(progressDialog,newVersion,loginActivity).execute(downloadUrl)
        }, Runnable {
            progressDialog.dismiss()
            setPortraitDialog(loginActivity)
        })
    }

    /**
     * 点击条目按钮，提示是否开启权限
     */
    fun setPortraitDialog(loginActivity: BaseAcvitiy) {
        //设置长按弹窗-提醒是否删除本条目
        val builder = AlertDialog.Builder(loginActivity, AlertDialog.BUTTON_NEUTRAL)
        /**
         * 设置内容区域为简单列表项
         */
        builder.setMessage("存储权限未打开，请到设置中心开启权限！")
                .setNegativeButton("确定", null)
        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.show()
    }

    /**
     * 下载APK的异步任务，下载完成自动安装
     */
    private  class DownloadAPK(var progressDialog: ProgressDialog, var newVersion: Float,var acvitiy: BaseAcvitiy) : AsyncTask<String, Int, String>() {
        var file: File?=null

        override fun doInBackground(vararg params: String): String? {
            SystemUtil.printlnStr("download 999999")
            val url: URL
            val conn: HttpURLConnection
            var bis: BufferedInputStream? = null
            var fos: FileOutputStream? = null
            try {
                url = URL(params[0])
                conn = url.openConnection() as HttpURLConnection
                conn.setRequestMethod("GET")
                conn.setConnectTimeout(5000)
                SystemUtil.printlnStr("download 2222222")
                val fileLength = conn.getContentLength()
                SystemUtil.printlnStr("download 3333333333333")
                bis = BufferedInputStream(conn.getInputStream())
                SystemUtil.printlnStr("download 44444444444444")
                val fileName = SDCardUtil.instance.downloadDir + File.separator + newVersion +"_" +".apk"
                file = File(fileName)
                if (!file!!.exists()) {
                    if (!file!!.getParentFile()!!.exists()) {
                        file?.getParentFile()?.mkdirs()
                    }
                    file!!.createNewFile()
                }
                fos = FileOutputStream(file)
                val data = ByteArray(4 * 1024)
                var total: Long = 0
                var count: Int= bis!!.read(data)
                while (count != -1) {
                    total += count.toLong()
                    SystemUtil.printlnStr("download 55555555555555")
                    publishProgress((total * 100 / fileLength).toInt())
                    fos!!.write(data, 0, count)
                    fos!!.flush()
                    count=bis!!.read(data)
                }
                fos!!.flush()
                SystemUtil.printlnStr("download 666666666666")
            } catch (e: IOException) {
                e.printStackTrace()
                SystemUtil.printlnStr("download 11111111111111")
                if(e is FileNotFoundException){
                    return "文件不存在!"
                }else{
                    return "其他错误"
                }
            } finally {
                SystemUtil.printlnStr("download 7777777777777")
                try {
                    if (fos != null) {
                        fos!!.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {
                    if (bis != null) {
                        bis!!.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return ""
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            SystemUtil.printlnStr("download 10000000000")
            values[0]?.let { progressDialog.setProgress(it) }
        }


        override fun onPostExecute(s: String) {
            super.onPostExecute(s)
            SystemUtil.printlnStr("download 11000000000")
            if(s.equals("")){
                try {
                    progressDialog.dismiss()
                    openFile(file)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }else{
                progressDialog.dismiss()
                ToastUtil.show(acvitiy,s)
            }
        }

        private fun openFile(file: File?) {
            try {
                if (file != null) {
                    var fileUri: Uri? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        fileUri = FileProvider.getUriForFile(acvitiy, acvitiy.packageName + ".FileProvider",
                                file)
                    } else {
                        fileUri = Uri.fromFile(file)
                    }
                    val installIntent = Intent(Intent.ACTION_VIEW)
                    installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    installIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    installIntent.action = Intent.ACTION_VIEW
                    installIntent.setDataAndType(fileUri,
                            "application/vnd.android.package-archive")
                    acvitiy.startActivity(installIntent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}