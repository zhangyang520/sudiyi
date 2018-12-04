package com.suntray.chinapost.user.data.respository

import com.google.gson.Gson
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.BaseResp
import com.suntray.chinapost.baselibrary.data.net.RetrofitFactory
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.user.data.api.MineApi
import com.suntray.chinapost.user.data.bean.EditPwdRequest
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.request.MineMessageRequest
import com.suntray.chinapost.user.data.request.MyClientRequest
import com.suntray.chinapost.user.data.request.ViewMessageRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import rx.Observable
import javax.inject.Inject

/**
 *  我的模块中 网络请求仓库
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:42
 */
class MineRespository @Inject constructor(){

    /**
     * 修改密码
     */
    public fun  editPwd(editPwdRequest: EditPwdRequest):Observable<BaseResp<Object>>{
        SystemUtil.printlnStr("editPwd json:"+Gson().toJson(editPwdRequest))
        return RetrofitFactory.instance.create(MineApi::class.java).editPwd(editPwdRequest)
    }

    /**
     * 上传头像
     * @param descritpion 上传的文件表单
     * @param file        文件
     * @return 返回值
     */
    fun upload(descritpion: RequestBody, id:Int, file: MultipartBody.Part,type: Int): Observable<BaseResp<String>>{
        return RetrofitFactory.instance.create(MineApi::class.java).uploadPortrait(descritpion,id,file,type)
    }


    /**
     * 我的客户
     */
    fun  myClient(myClientRequest: MyClientRequest):Observable<BaseResp<ArrayList<MineClient>>>{
        return RetrofitFactory.instance.create(MineApi::class.java).myClient(myClientRequest)
    }

    /**
     * 我的消息的
     */
    fun myMessage(mineMessageRequest: MineMessageRequest):Observable<BaseResp<ArrayList<MineMessage>>>{
        return RetrofitFactory.instance.create(MineApi::class.java).myMessage(mineMessageRequest)
    }


    fun viewMessage(mineMessageRequest: ViewMessageRequest):Observable<BaseResp<ArrayList<Object>>>{
        return RetrofitFactory.instance.create(MineApi::class.java).viewMessage(mineMessageRequest)
    }
}