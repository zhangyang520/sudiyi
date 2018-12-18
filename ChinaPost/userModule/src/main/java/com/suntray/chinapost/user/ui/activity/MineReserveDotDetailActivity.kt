package com.suntray.chinapost.user.ui.activity

import android.content.Context
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.progressbar.KProgressHUD
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.data.request.FindReservePointByIdRequest
import com.suntray.chinapost.user.data.response.FindReservePointByIdResponse
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.MineDotPresenter
import com.suntray.chinapost.user.presenter.view.MineDotView
import kotlinx.android.synthetic.main.activity_my_reserved_dot_detail.*
import kotlinx.android.synthetic.main.item_mine_reserve_dot_detail.*
import java.lang.StringBuilder

/**
 *  预订点位的详情页
 *  @Title ${name}
 *  @ProjectName ChinaPost
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/1412:07
 *
 */
@Route(path = RouterPath.MineModule.MINE_RESERVED_DOT_DETAIL)
class MineReserveDotDetailActivity:BaseMvpActivity<MineDotPresenter>(),MineDotView{

    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun initView() {
        isRightShow=false
        isBlackShow=true
        isTitleShow=true
        viewtitle="点位详情"
        //初始化数据
        var id=intent.getIntExtra("dotId",-1)
        hud2= KProgressHUD(this@MineReserveDotDetailActivity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("获取预订点位详情")
        basePresenter.findReservePointById(FindReservePointByIdRequest(id))
    }

    override fun getView(): View {
        return View.inflate(this@MineReserveDotDetailActivity, R.layout.activity_my_reserved_dot_detail,null)
    }

    /**
     * 获取回调
     */
    override fun onFindReservePointByIdResponse(findReservePointByIdResponse: FindReservePointByIdResponse) {
        super.onFindReservePointByIdResponse(findReservePointByIdResponse)
        //初始化信息
        if(findReservePointByIdResponse.reserve!!!=null){
            tv_client_name_value.setText(findReservePointByIdResponse.reserve!!.clientname)

            //进行循环 资源位类型
            if (findReservePointByIdResponse.resourceCategoryList != null) {
                var stringBuilder=StringBuilder()
                for (data in findReservePointByIdResponse!!.resourceCategoryList!!) {
                    stringBuilder.append(data+",")
                }
                stringBuilder.delete(stringBuilder.lastIndexOf(","),stringBuilder.length)
                tv_resource_type_value.setText(stringBuilder.toString())
            }else{
                tv_resource_type_value.setText("")
            }

            //广告类型
            tv_ad_type_value.setText(findReservePointByIdResponse.reserve!!.adtypename)
            tv_reserve_time_value.setText(DateUtil.dateFormat(DateUtil.parse2Date(findReservePointByIdResponse.reserve!!.startdate))+
                                                       "--"+DateUtil.dateFormat(DateUtil.parse2Date(findReservePointByIdResponse.reserve!!.enddate)))
        }

        //进行循环 显示 列表 pointList数据
        if(findReservePointByIdResponse.pointList!=null){
            addSelectedList(findReservePointByIdResponse.pointList)
        }
    }

    private fun addSelectedList(selectList: ArrayList<FindReservePointByIdResponse.Point>?) {
        var index=0
        for (data in selectList!!){
            index++
            var contentView=View.inflate(this@MineReserveDotDetailActivity,R.layout.recylerview_reserve_dot_qingdan,null)
            var btn_xuhao: TextView =contentView.findViewById(R.id.btn_xuhao) as TextView
            btn_xuhao.setText(""+index)
            var tv_location: TextView =contentView.findViewById(R.id.tv_location)  as TextView
            tv_location.setText(data.pointname)
            var tv_id_value: TextView =contentView.findViewById(R.id.tv_id_value) as TextView
            tv_id_value.setText(data.equid)
            var tv_area: TextView =contentView.findViewById(R.id.tv_area) as TextView
            tv_area.setText(data.areaname)

            var tv_position: TextView =contentView.findViewById(R.id.tv_position) as TextView
            tv_position.setText(data.equlocation)

            ll_dianwei_qingdan.addView(contentView)
        }
    }
}