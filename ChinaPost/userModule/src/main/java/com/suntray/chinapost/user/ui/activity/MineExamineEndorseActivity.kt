package com.suntray.chinapost.user.ui.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.ui.activity.BaseAcvitiy
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.data.request.MineMessageRequest
import com.suntray.chinapost.user.data.request.ViewMessageRequest
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.MinePresenter
import com.suntray.chinapost.user.presenter.view.MineEditView
import com.suntray.chinapost.user.presenter.view.MineView
import kotlinx.android.synthetic.main.recylerview_message_examine.*

/**
 *  审批通知界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/16 17:47
 */
@Route(path = RouterPath.MineModule.MINE_EXAMINE_ENDORSE)
class MineExamineEndorseActivity:BaseMvpActivity<MinePresenter>(), MineEditView {

    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun initView() {
        isRightShow=false
        isTitleShow=true
        isBlackShow=true
        viewtitle="审批通知"

        //获取数据 进行展示
        var message=intent.getSerializableExtra("message") as MineMessage
        if(message!=null){
            tv_date.text=message.senddate
            tv_content.text=message.content
            basePresenter.viewMessage(ViewMessageRequest(message.id))
        }

    }

    override fun getView(): View {
        return View.inflate(this@MineExamineEndorseActivity,R.layout.activity_mine_examine_endorse,null);
    }
}