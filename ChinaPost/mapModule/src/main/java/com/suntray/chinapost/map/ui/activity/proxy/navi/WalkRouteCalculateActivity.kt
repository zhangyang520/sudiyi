package com.suntray.chinapost.map.ui.activity.proxy.navi

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route

import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.AMapNaviCross
import com.amap.api.navi.model.NaviLatLng
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.activity.proxy.navi.inner.BaseActivity
import com.suntray.chinapost.provider.RouterPath

/**
 *  步行的导航
 */
@Route(path = RouterPath.MapModule.POST_WALK_ROUTE_CALCULATE_ACTIVITY)
class WalkRouteCalculateActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_navi)
        mAMapNaviView = findViewById(R.id.navi_view) as AMapNaviView
        mAMapNaviView!!.onCreate(savedInstanceState)
        mAMapNaviView!!.setAMapNaviViewListener(this)
        mAMapNaviView!!.naviMode = AMapNaviView.NORTH_UP_MODE
    }

    override fun onInitNaviSuccess() {
        super.onInitNaviSuccess()
        mAMapNavi!!.calculateWalkRoute(NaviLatLng(39.942295, 116.335891), NaviLatLng(39.995576, 116.481288))

    }

    override fun onCalculateRouteSuccess(ids: IntArray) {
        super.onCalculateRouteSuccess(ids)
        mAMapNavi!!.startNavi(NaviType.GPS)
    }
}
