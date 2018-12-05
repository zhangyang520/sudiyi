package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.services.route.RidePath
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.adapter.proxy.RideSegmentListAdapter
import com.suntray.chinapost.map.utils.AMapUtil
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.activity_route_detail.*

/**
 * 骑行路线详情
 */
class RideRouteDetailActivity : Activity() {
    private var mRidePath: RidePath? = null
    private var mTitle: TextView? = null
    private var mTitleWalkRoute: TextView? = null
    private var mRideSegmentList: ListView? = null
    private var mRideSegmentListAdapter: RideSegmentListAdapter? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_detail)
        getIntentData()
        mTitle = findViewById(R.id.title_center) as TextView
        mTitle!!.text = "骑行路线详情"
        mTitleWalkRoute = findViewById(R.id.firstline) as TextView
        val dur = AMapUtil.getFriendlyTime(mRidePath!!.duration.toInt())
        val dis = AMapUtil.getFriendlyLength(mRidePath!!.distance.toInt())
        mTitleWalkRoute!!.setText(dur + "(" + dis + ")")
        mRideSegmentList = findViewById(R.id.bus_segment_list) as ListView
        mRideSegmentListAdapter = RideSegmentListAdapter(
                this.applicationContext, mRidePath!!.steps)
        mRideSegmentList!!.adapter = mRideSegmentListAdapter

        //地图的map
        title_map.visibility=View.VISIBLE
        navi_map.setText("骑行导航")
    }

    /**
     * 点击海事局爱你
     */
    fun onMapClick(v: View) {
        ARouter.getInstance().build(RouterPath.MapModule.POST_RIDE_ROUTE_CALCULATE_ACTIVITY).navigation(this)
    }

    private fun getIntentData() {
        val intent = intent ?: return
        mRidePath = intent.getParcelableExtra("ride_path")
    }

    fun onBackClick(view: View) {
        this.finish()
    }

}
