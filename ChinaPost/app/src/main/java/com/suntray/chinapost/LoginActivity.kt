package com.suntray.chinapost

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.User
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.rx.hasTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.data.bean.UserRole
import com.suntray.chinapost.inject.component.DaggerLoginComponent
import com.suntray.chinapost.map.utils.ASettingUtils
import com.suntray.chinapost.map.utils.ToastUtil
import com.suntray.chinapost.presenter.LoginPresenter
import com.suntray.chinapost.presenter.view.LoginView
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.utils.RolePopup
import com.suntray.chinapost.utils.inner.RoleItemClickListener
import kotlinx.android.synthetic.main.activity_login.*

/**
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/12 9:09
 */
@Route(path = RouterPath.AppModule.LOGIN_ACTIVITY)
class LoginActivity:BaseMvpActivity<LoginPresenter>(),LoginView{


    override fun initView() {
        isBlackShow=false
        isTitleShow=false
        isRightShow=false
        //清空设置条件
        ASettingUtils.clearSetting()
        try {
            var user=UserDao.getLocalUser()
            if(user.userRole==3){
                //供应商 ....
                ARouter.getInstance().build(RouterPath.MapModule.POST_TASK_LIST).withString("supplyID",user.orgId).navigation();
            }else{
                ARouter.getInstance().build(RouterPath.MapModule.POST_POI_SEARCH).navigation();
            }
            finish()
            //如果 有该用户
        } catch (e: ContentException) {
            hud2= KProgressHUD.create(this@LoginActivity).setLabel("登录中...").setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            //点击登录按钮
            btn_login.setOnClickListener({
                if(ed_name.hasTxt() && ed_pwd.hasTxt()){
                    if(BaseConstants.SELECTEDROLEINDEX==-1){
                        ToastUtil.show(this@LoginActivity,"请选择角色")
                    }else{
                        basePresenter.loginBiz(ed_name.getTxt(),ed_pwd.getTxt(),BaseConstants.SELECTEDROLEINDEX)
                    }
                }else{
                    ToastUtil.show(this@LoginActivity,"请输入账号或者密码")
                }
            })
            //角色的点击事件 供应商
            ed_role.setOnClickListener({
                //弹出 角色选择框
                iv_role_index.background=resources.getDrawable(R.drawable.iv_up)
                RolePopup.showRolePoopup(this@LoginActivity,rl_role,object:RoleItemClickListener{
                    override fun onItemClick(userRole: UserRole) {
                        ed_role.setText(userRole.roleName)
                        BaseConstants.SELECTEDROLEINDEX=userRole.roleId
                        iv_role_index.background=resources.getDrawable(R.drawable.iv_down)
                    }
                })
            })
        }
    }

    override fun getView(): View {
        return View.inflate(this@LoginActivity,R.layout.activity_login,null);
    }

    override fun injectCompontent() {
        DaggerLoginComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    /**
     * 登录的反馈界面
     */
    override fun onLogignRequest(t: User) {
        ToastUtil.show(this@LoginActivity,"登录成功")
        finish()
       if(BaseConstants.SELECTEDROLEINDEX==3){
            //供应商 ....
            ARouter.getInstance().build(RouterPath.MapModule.POST_TASK_LIST).withString("supplyID",t.orgId).navigation();
        }else{
            ARouter.getInstance().build(RouterPath.MapModule.POST_POI_SEARCH).navigation();
       }
    }
}