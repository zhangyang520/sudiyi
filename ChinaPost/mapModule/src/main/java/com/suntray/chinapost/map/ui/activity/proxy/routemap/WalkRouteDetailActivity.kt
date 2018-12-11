package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.WalkPath
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.adapter.proxy.WalkSegmentListAdapter
import com.suntray.chinapost.map.utils.AMapUtil
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.activity_route_detail.*

class WalkRouteDetailActivity : Activity() {
    private var mWalkPath: WalkPath? = null
    private var mTitle: TextView? = null
    private var mTitleWalkRoute: TextView? = null
    private var mWalkSegmentList: ListView? = null
    private var mWalkSegmentListAdapter: WalkSegmentListAdapter? = null
    private var mStartPoint = LatLonPoint(39.942295, 116.335891)//起点，116.335891,39.942295
    private var mEndPoint = LatLonPoint(39.995576, 116.481288)//终点，116.481288,39.995576

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_detail)
        getIntentData()
        mTitle = findViewById(R.id.title_center) as TextView
        mTitle!!.text = "步行路线详情"
        mTitleWalkRoute = findViewById(R.id.firstline) as TextView
        val dur = AMapUtil.getFriendlyTime(mWalkPath!!.duration.toInt())
        val dis = AMapUtil
                .getFriendlyLength(mWalkPath!!.distance.toInt())
        mTitleWalkRoute!!.setText(dur + "(" + dis + ")")
        mWalkSegmentList = findViewById(R.id.bus_segment_list) as ListView
        mWalkSegmentListAdapter = WalkSegmentListAdapter(
                this.applicationContext, mWalkPath!!.steps)
        mWalkSegmentList!!.adapter = mWalkSegmentListAdapter

        title_map.visibility=View.VISIBLE
        navi_map.setText("开始导航")
        title_map.setOnClickListener({
            //导航点击事件
            ARouter.getInstance().build(RouterPath.MapModule.POST_WALK_ROUTE_CALCULATE_ACTIVITY)
                    .withParcelable("startPoint",mStartPoint).withParcelable("endPoint",mEndPoint).navigation(this)
        })
    }

    /**
     * 点击事件
     */
    fun onMapClick(v: View) {
        ARouter.getInstance().build(RouterPath.MapModule.POST_WALK_ROUTE_CALCULATE_ACTIVITY)
                .withParcelable("startPoint",mStartPoint)
                .withParcelable("endPoint",mEndPoint).navigation()
    }


    private fun getIntentData() {
        val intent = intent ?: return
        mStartPoint=intent.getParcelableExtra("startPoint")
        mEndPoint=intent.getParcelableExtra("endPoint")
        mWalkPath = intent.getParcelableExtra("walk_path")
    }

    fun onBackClick(view: View) {
        this.finish()
    }

}
