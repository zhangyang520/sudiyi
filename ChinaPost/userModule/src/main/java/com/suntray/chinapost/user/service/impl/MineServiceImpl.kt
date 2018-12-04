package com.suntray.chinapost.user.service.impl

import com.suntray.chinapost.baselibrary.rx.convertData
import com.suntray.chinapost.user.data.bean.EditPwdRequest
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.request.MineMessageRequest
import com.suntray.chinapost.user.data.request.MyClientRequest
import com.suntray.chinapost.user.data.request.ViewMessageRequest
import com.suntray.chinapost.user.data.respository.MineRespository
import com.suntray.chinapost.user.service.MineService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import rx.Observable
import javax.inject.Inject

/**
 *  我的模块 服务实现类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:47
 */
class MineServiceImpl @Inject constructor():MineService{

    @Inject
    lateinit var mineRespository: MineRespository


    /*
     * 修改密码界面
     */
    override fun editPwd(editPwdRequest: EditPwdRequest): Observable<Object> {
        return mineRespository.editPwd(editPwdRequest).convertData();
    }

    /**
     * 上传头像
     */
    override fun uploadPortrait(descritpion: RequestBody, id: Int, file: MultipartBody.Part,type:Int): Observable<String> {
        return mineRespository.upload(descritpion,id,file,type).convertData()
    }

    /**
     * 我的客户查询
     */
    override fun myClient(myClientRequest: MyClientRequest): Observable<ArrayList<MineClient>> {
        return mineRespository.myClient(myClientRequest).convertData()
    }

    /**
     * 我的客户
     */
    override fun myMessage(mineMessageRequest: MineMessageRequest): Observable<ArrayList<MineMessage>> {
        return mineRespository.myMessage(mineMessageRequest).convertData()
    }

    override fun viewMessage(mineMessageRequest: ViewMessageRequest): Observable<ArrayList<Object>> {
        return mineRespository.viewMessage(mineMessageRequest).convertData()
    }
}