package com.suntray.chinapost.user.data.api

import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.user.data.bean.EditPwdRequest
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.request.MineMessageRequest
import com.suntray.chinapost.user.data.request.MyClientRequest
import com.suntray.chinapost.user.data.request.ViewMessageRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

/**
 *  我的模块相关的业务接口
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:34
 */
interface MineApi {

    /**
     * 修改密码接口
     */
    @POST(BaseConstants.EDIT_PWD)
    fun  editPwd(@Body editPwdRequest: EditPwdRequest):Observable<BaseResp<Object>>

    /**
     * 头像上传成功
     */
    @Multipart
    @POST(BaseConstants.UPLOAD_IMG)
    fun uploadPortrait(@Part("description") descritpion: RequestBody,
                       @Query(value = "id") id:Int, @Part file: MultipartBody.Part,@Query(value  ="type") type:Int):Observable<BaseResp<String>>


    /**
     * 我的客户的查询
     */
    @POST(BaseConstants.MY_CLIENT)
    fun myClient(@Body myClientRequest: MyClientRequest):Observable<BaseResp<ArrayList<MineClient>>>


    /**
     * 我的客户的查询
     */
    @POST(BaseConstants.MY_MESSAGE)
    fun myMessage(@Body mineMessageRequest: MineMessageRequest):Observable<BaseResp<ArrayList<MineMessage>>>

    /**
     * 查看 消息
     */
    @POST(BaseConstants.VIEW_MESSAGE)
    fun viewMessage(@Body mineMessageRequest: ViewMessageRequest):Observable<BaseResp<ArrayList<Object>>>
}