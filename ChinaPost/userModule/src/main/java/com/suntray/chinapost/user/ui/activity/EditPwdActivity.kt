package com.suntray.chinapost.user.ui.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.rx.hasTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.MinePresenter
import com.suntray.chinapost.user.presenter.view.MineEditView
import kotlinx.android.synthetic.main.activity_edit_pwd.*

/**
 *  编辑密码界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:02
 */
@Route(path = RouterPath.MineModule.MINE_EDIT_PWD)
class EditPwdActivity:BaseMvpActivity<MinePresenter>(),MineEditView{
    override fun initView() {
        isTitleShow=true
        isRightShow=false
        isBlackShow=true
        viewtitle="修改密码"

        hud2= KProgressHUD(this@EditPwdActivity).
                        setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("密码修改中...");

        tv_phone.setText("账号名称: "+UserDao.getLocalUser().email)
        btn_edit_pwd.setOnClickListener({
            if(ed_old_pwd.hasTxt() && ed_new_pwd.hasTxt() && ed_new_pwd_again.hasTxt()){
                if(ed_new_pwd.getTxt().equals(ed_new_pwd_again.getTxt())){
                    basePresenter.editPwd(ed_old_pwd.getTxt(),ed_new_pwd.getTxt(),
                                        UserDao.getLocalUser().id,UserDao.getLocalUser().email,BaseConstants.SELECTEDROLEINDEX);
                }else{
                    ToastUtil.makeText(this@EditPwdActivity,"新密码两次输入不一致");
                }
            }else{
                ToastUtil.makeText(this@EditPwdActivity,"密码不能为空")
            }
        })
    }

    override fun getView(): View {
        return View.inflate(this@EditPwdActivity,R.layout.activity_edit_pwd,null)
    }

    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun onEditPwdRequest() {
        ToastUtil.makeText(this@EditPwdActivity,"修改密码成功")
        finish();
    }
}