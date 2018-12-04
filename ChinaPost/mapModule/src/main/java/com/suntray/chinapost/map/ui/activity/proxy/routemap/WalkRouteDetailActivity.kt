package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
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
    }

    /**
     * 点击事件
     */
    fun onMapClick(v: View) {
        ARouter.getInstance().build(RouterPath.MapModule.POST_WALK_ROUTE_CALCULATE_ACTIVITY).navigation()
    }
    private fun getIntentData() {
        val intent = intent ?: return
        mWalkPath = intent.getParcelableExtra("walk_path")
    }

    fun onBackClick(view: View) {
        this.finish()
    }

}
