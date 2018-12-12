package com.suntray.chinapost.user.ui.activity

import android.graphics.Color
import android.view.View
import cn.qqtheme.framework.picker.OptionPicker
import cn.qqtheme.framework.widget.WheelView
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.data.dao.ClientDictDao
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.rx.hasTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.data.enum.ClientAction
import com.suntray.chinapost.user.data.request.AddClientRequest
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.ClientPresenter
import com.suntray.chinapost.user.presenter.view.ClientView
import com.suntray.chinapost.user.utils.ClientBizUtils
import com.zhy.autolayout.utils.AutoUtils
import kotlinx.android.synthetic.main.activity_add_client.*
import kotlinx.android.synthetic.main.item_client_hangye_layout.*
import kotlinx.android.synthetic.main.item_client_info_layout.*

/**
 *  增加客户的界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/16 13:39
 */
@Route(path=RouterPath.MineModule.MINE_ADD_CLIENT)
class MineAddClientActivity : BaseMvpActivity<ClientPresenter>(),ClientView{

    //当前客户的实体
    var currentClient:MineClient?=null

    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun beforeFinish() {
        if(ClientBizUtils.window!=null &&  ClientBizUtils.window!!.isShowing){
            ClientBizUtils.window!!.dismiss()
        }else{
            finish()
        }
    }
    override fun onProvinceCityRequest(provinceCity: ArrayList<ProvinceCity>, action: CityListAction) {
        super.onProvinceCityRequest(provinceCity, action)
        SystemUtil.printlnStr("provinceCity:"+provinceCity.toString())
        if(provinceCity.size>0){
            if(ClientBizUtils.window==null ||  !ClientBizUtils.window!!.isShowing){
                //没有展示的时候
                ClientBizUtils.showCityList(this@MineAddClientActivity,root,action,provinceCity,basePresenter,currentClient,tv_client_dizhi)
            }
        }else{
            ToastUtil.makeText(this@MineAddClientActivity,action.errorMsg)
        }
    }

    var action:ClientAction ?=null
    override fun initView() {
        isRightShow=false
        isTitleShow=true
        isBlackShow=true

        //修改客户信息
        //初始化View
        if(intent!=null && intent.getSerializableExtra("client")!=null){
            viewtitle="修改客户信息"
            action=ClientAction.EditClient
            currentClient=intent.getSerializableExtra("client") as MineClient
            ed_client_name.setText(currentClient!!.name.toString())
            ed_client_xingming.setText(currentClient!!.linkmanname)
            ed_client_phone.setText(currentClient!!.linkmanphone)

            btn_hasxiadan.visibility=View.VISIBLE
            tv_has_client.visibility=View.VISIBLE
            btn_hasxiadan.isActivated=true


            iv_yixiang.visibility=View.GONE
            tv_yixiang_client.visibility=View.GONE

            if(currentClient!!.stage==8){
                tv_has_client.setText("意向客户")
            }else if(currentClient!!.stage==9){
                tv_has_client.setText("已下单客户")
            }else{
                tv_has_client.setText("资质客户")
            }

            //65

            tv_client_dizhi.setText(currentClient!!.provincename+currentClient!!.cityname+currentClient!!.districtname)
            tv_select_content.setText(currentClient!!.professionname)
            tv_choose_type.setText(currentClient!!.naturename)
            tv_client_laiyuan.setText(currentClient!!.originname)
            ed_address.setText(currentClient!!.address)
            ed_belong.setText(currentClient!!.salesmanname)
            hud2= KProgressHUD(this@MineAddClientActivity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("修改客户信息中")

        }else{
            viewtitle="添加客户信息"
            currentClient=MineClient()
            action=ClientAction.AddClient

            hud2= KProgressHUD(this@MineAddClientActivity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("添加客户信息中")
            //增加客户：
            btn_hasxiadan.visibility=View.INVISIBLE
            tv_has_client.visibility=View.VISIBLE
            btn_hasxiadan.isActivated=true

            tv_has_client.setText("意向客户")
            iv_yixiang.visibility=View.GONE
            tv_yixiang_client.visibility=View.GONE
        }

        /**
         * 不允许修改
         */

        /**
         * 取消的按钮 的点击
         */
        btn_cancel.setOnClickListener({
            finish()
        })

        al_hangye.setOnClickListener({
            try {
               var clientList= ClientDictDao.getClients(1)
                SystemUtil.printlnStr("profession al_hangye:"+clientList.toString())
                if(clientList.size>0){
                    var array= arrayOfNulls<String>(clientList.size)
                    var index=0
                    for (data in clientList){
                        array.set(index,data.name)
                        index++
                    }
                    val picker = OptionPicker(this, array)
                    picker.setCanceledOnTouchOutside(false)
                    picker.setDividerRatio(WheelView.DividerConfig.FILL)
                    picker.setShadowColor(Color.RED, 40)
                    picker.selectedIndex = 1
                    picker.setCycleDisable(true)
                    picker.setTextSize(20)
                    picker.setHeight(AutoUtils.getPercentHeightSize(350))
                    picker.setOnOptionPickListener(object : OptionPicker.OnOptionPickListener() {
                        override fun onOptionPicked(index: Int, item: String) {
                            SystemUtil.printlnStr("profession:"+clientList.get(index).id)
                            currentClient!!.profession=clientList.get(index).id
                            tv_select_content.setText(clientList.get(index).name)
                        }
                    })
                    picker.show()
                }
            } catch (e: Exception) {
                ToastUtil.makeText(this@MineAddClientActivity,"暂无行业数据")
            }

        })

        ll_client_laiyuan.setOnClickListener({
            try {
                var clientList= ClientDictDao.getClients(2)
                if(clientList.size>0){
                    var array= arrayOfNulls<String>(clientList.size)
                    var index=0
                    for (data in clientList){
                        array.set(index,data.name)
                        index++
                    }
                    val picker = OptionPicker(this, array)
                    picker.setCanceledOnTouchOutside(false)
                    picker.setDividerRatio(WheelView.DividerConfig.FILL)
                    picker.setShadowColor(Color.RED, 40)
                    picker.selectedIndex = 1
                    picker.setCycleDisable(true)
                    picker.setTextSize(20)
                    picker.setHeight(AutoUtils.getPercentHeightSize(350))
                    picker.setOnOptionPickListener(object : OptionPicker.OnOptionPickListener() {
                        override fun onOptionPicked(index: Int, item: String) {
                            currentClient!!.origin=clientList.get(index).id
                            tv_client_laiyuan.setText(clientList.get(index).name)
                        }
                    })
                    picker.show()
                }
            } catch (e: Exception) {
                ToastUtil.makeText(this@MineAddClientActivity,"暂无来源数据")
            }
        })

        ll_choose_type.setOnClickListener({
            try {
                var clientList= ClientDictDao.getClients(20)
                if(clientList.size>0){
                    var array= arrayOfNulls<String>(clientList.size)
                    var index=0
                    for (data in clientList){
                        array.set(index,data.name)
                        index++
                    }
                    val picker = OptionPicker(this, array)
                    picker.setCanceledOnTouchOutside(false)
                    picker.setDividerRatio(WheelView.DividerConfig.FILL)
                    picker.setShadowColor(Color.RED, 40)
                    picker.selectedIndex = 1
                    picker.setCycleDisable(true)
                    picker.setTextSize(20)
                    picker.setHeight(AutoUtils.getPercentHeightSize(350))
                    picker.setOnOptionPickListener(object : OptionPicker.OnOptionPickListener() {
                        override fun onOptionPicked(index: Int, item: String) {
                            currentClient!!.nature=clientList.get(index).id
                            tv_choose_type.setText(clientList.get(index).name)
                        }
                    })
                    picker.show()
                }
            } catch (e: Exception) {
                ToastUtil.makeText(this@MineAddClientActivity,"暂无客户性质数据")
            }
        })
        btn_ok.setOnClickListener({

            SystemUtil.printlnStr("currentClient:"+currentClient.toString())
            if(examineInput()){
                if(!ed_client_phone.getTxt().matches(Regex("1\\d{10}"))){
                    ToastUtil.makeText(this@MineAddClientActivity,"号码必须以1开头的11位数字")
                }else{
                    //判断条件
                    //前置条件判断: 客户来源是否存在
                    var currentStart:Int=0
                    if(iv_yixiang.isActivated){
                        currentStart=8
                    }else{
                        currentStart=9
                    }
                    //这个是 修改的客户
                    basePresenter.addClient(
                            AddClientRequest(currentClient!!.id,ed_client_name.getTxt(),
                                    currentClient!!.profession,currentClient!!.origin,
                                    ed_client_xingming.getTxt(),ed_client_phone.getTxt(),currentStart,currentClient!!.salesman,
                                    currentClient!!.province,currentClient!!.city,ed_address.getTxt(),
                                    currentClient!!.nature,currentClient!!.district,currentClient!!.state,currentClient!!.brand?:""),action!!)
                }
            }else{
                ToastUtil.makeText(this@MineAddClientActivity,"输入的信息不全")
            }
        })

        //客户地址点击 弹出
        ll_client_dizhi.setOnClickListener({
            basePresenter.province(CityListAction.ProvinceAtion.getCityValue1().toString(),-1,-1, CityListAction.ProvinceAtion)
        })
        currentClient!!.salesman=UserDao.getLocalUser().id
        ed_belong.setText(UserDao.getLocalUser().nickName)
    }

    /**
     * 提示成功
     */
    override fun onAddClientRequest(action: ClientAction) {
        if(action==ClientAction.AddClient){
            setResult(102)
            finish()
        }else{
            setResult(101)
            finish()
        }
       ToastUtil.makeText(this@MineAddClientActivity,action.successMsg)
    }


    /**
     * 验证输入的信息
     */
    fun examineInput():Boolean{
        if(ed_client_name.hasTxt() && ed_client_xingming.hasTxt() && ed_client_phone.hasTxt() &&
                (iv_yixiang.isActivated || btn_hasxiadan.isActivated) &&
                                            tv_select_content.hasTxt() && tv_choose_type.hasTxt() &&
                                                    tv_client_laiyuan.hasTxt() && ed_address.hasTxt() && ed_belong.hasTxt() && tv_client_dizhi.hasTxt()){
                return true
        }
        return false
    }
    override fun getView(): View {
        return View.inflate(this@MineAddClientActivity, R.layout.activity_add_client,null)
    }
}