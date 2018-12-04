package com.suntray.chinapost.user.service

import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.user.data.api.MineApi
import com.suntray.chinapost.user.data.bean.EditPwdRequest
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.request.MineMessageRequest
import com.suntray.chinapost.user.data.request.MyClientRequest
import com.suntray.chinapost.user.data.request.ViewMessageRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observable

/**
 *   我的模块相关的 服务接口
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:45
 */
interface MineService {

    /**
     * 修改密码
     */
    fun editPwd(editPwdRequest: EditPwdRequest):Observable<Object>

    /**
     * 上传头像
     */
    fun uploadPortrait(descritpion: RequestBody, id:Int, file:MultipartBody.Part,type:Int):Observable<String>

    /**
     * 我的客户查询
     */
    fun myClient(myClientRequest: MyClientRequest):Observable<ArrayList<MineClient>>

    /**
     * 我的消息
     */
    fun myMessage(mineMessageRequest: MineMessageRequest):Observable<ArrayList<MineMessage>>


    /**
     * 查看消息
     */
    fun viewMessage(mineMessageRequest: ViewMessageRequest):Observable<ArrayList<Object>>
}