package com.suntray.chinapost.user.injection.component

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.baselibrary.injection.component.ActivityComponent
import com.suntray.chinapost.user.injection.module.MineModule
import com.suntray.chinapost.user.ui.activity.*
import dagger.Component


/**
 *  我的模块的component
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:50
 */
@PerComponentScope
@Component(modules = arrayOf(MineModule::class),dependencies = arrayOf(ActivityComponent::class))
interface MineComponent {

    /**
     * 绑定 修改密码的界面
     */
    fun bind(editPwdActivity: EditPwdActivity)

    /**
     * 绑定我的界面
     */
    fun bind(mineActivity: MineActivity)

    /**
     * 与我的客户界面绑定
     */
    fun bind(mineClientActivity: MineClientActivity)

    /**
     * 绑定我的消息界面
     */
    fun bind(mineMessageActivity: MineMessageActivity)

    /**
     * 绑定增加 客户界面
     */
    fun bind(mineAddClientActivity: MineAddClientActivity)


    /**
     * 我预定的界面
     */
    fun bind(mineReservedDotActivity: MineReservedDotActivity)

    /**
     * 绑定上传资质的界面
     */
    fun bind(uploadAptitudeActivity: UploadAptitudeActivity)

    /**
     * 绑定消息查看界面
     */
    fun bind(mineExamineEndorseActivity:MineExamineEndorseActivity)

    /**
     * 绑定 客户的详情页
     */
    fun bind(mineClientDetail: MineClientDetail)
}