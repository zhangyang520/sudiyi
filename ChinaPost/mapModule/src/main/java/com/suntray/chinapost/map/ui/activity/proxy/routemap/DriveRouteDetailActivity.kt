package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.services.route.DrivePath
import com.amap.api.services.route.DriveRouteResult
import com.amap.api.services.route.DriveStep
import com.amap.api.services.route.TMC
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.adapter.proxy.DriveSegmentListAdapter
import com.suntray.chinapost.map.utils.AMapUtil
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.activity_route_detail.*

/**
 * 驾车路线详情
 */
class DriveRouteDetailActivity : Activity() {
    private var mDrivePath: DrivePath? = null
    private var mDriveRouteResult: DriveRouteResult? = null
    private var mTitle: TextView? = null
    private var mTitleDriveRoute: TextView? = null
    private var mDesDriveRoute: TextView? = null
    private var mDriveSegmentList: ListView? = null
    private var mDriveSegmentListAdapter: DriveSegmentListAdapter? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_detail)

        getIntentData()
        init()
    }

    private fun init() {
        mTitle = findViewById(R.id.title_center) as TextView
        mTitleDriveRoute = findViewById(R.id.firstline) as TextView
        mDesDriveRoute = findViewById(R.id.secondline) as TextView
        mTitle!!.text = "驾车路线详情"
        val dur = AMapUtil.getFriendlyTime(mDrivePath!!.duration.toInt())
        val dis = AMapUtil.getFriendlyLength(mDrivePath!!
                .distance.toInt())
        mTitleDriveRoute!!.setText(dur + "(" + dis + ")")
        val taxiCost = mDriveRouteResult!!.taxiCost.toInt()
        mDesDriveRoute!!.text = "打车约" + taxiCost + "元"
        mDesDriveRoute!!.visibility = View.VISIBLE
        configureListView()
        title_map.visibility=View.VISIBLE
        navi_map.setText("开始导航")
        title_map.setOnClickListener({
            //导航点击事件
            ARouter.getInstance().build(RouterPath.MapModule.POST_SINGLE_ROUTE_CALCULATE_ACTIVITY).navigation(this)
        })
    }

    private fun configureListView() {
        mDriveSegmentList = findViewById(R.id.bus_segment_list) as ListView
        mDriveSegmentListAdapter = DriveSegmentListAdapter(
                this.applicationContext, mDrivePath!!.steps)
        mDriveSegmentList!!.adapter = mDriveSegmentListAdapter
    }

    private fun getIntentData() {
        val intent = intent ?: return
        mDrivePath = intent.getParcelableExtra("drive_path")
        mDriveRouteResult = intent.getParcelableExtra("drive_result")
        for (i in 0 until mDrivePath!!.steps.size) {
            val step = mDrivePath!!.steps[i]
            val tmclist = step.tmCs
            for (j in tmclist.indices) {
                val s = "" + tmclist[j].polyline.size
                Log.i("MY", s + tmclist[j].status
                        + tmclist[j].distance
                        + tmclist[j].polyline.toString())
            }
        }
    }

    fun onBackClick(view: View) {
        this.finish()
    }
}
