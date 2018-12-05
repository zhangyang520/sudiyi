package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.*
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.services.route.BusPath
import com.amap.api.services.route.BusRouteResult
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.adapter.proxy.BusSegmentListAdapter
import com.suntray.chinapost.map.utils.AMapUtil
import kotlinx.android.synthetic.main.activity_route_detail.*
import overlay.BusRouteOverlay

class BusRouteDetailActivity : Activity(), OnMapLoadedListener, OnMapClickListener, InfoWindowAdapter, OnInfoWindowClickListener, OnMarkerClickListener {
    private var aMap: AMap? = null
    private var mapView: MapView? = null
    private var mBuspath: BusPath? = null
    private var mBusRouteResult: BusRouteResult? = null
    private var mTitle: TextView? = null
    private var mTitleBusRoute: TextView? = null
    private var mDesBusRoute: TextView? = null
    private var mBusSegmentList: ListView? = null
    private var mBusSegmentListAdapter: BusSegmentListAdapter? = null
    private var mBusMap: LinearLayout? = null
    private var mBuspathview: LinearLayout? = null
    private var mBusrouteOverlay: BusRouteOverlay? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_detail)
        mapView = findViewById(R.id.route_map) as MapView
        mapView!!.onCreate(savedInstanceState)// 此方法必须重写
        getIntentData()
        init()
    }

    private fun getIntentData() {
        val intent = intent
        if (intent != null) {
            mBuspath = intent.getParcelableExtra("bus_path")
            mBusRouteResult = intent.getParcelableExtra("bus_result")
        }
    }

    private fun init() {
        if (aMap == null) {
            aMap = mapView!!.map
        }
        registerListener()
        navi_map.setText("地图")
        mTitle = findViewById(R.id.title_center) as TextView
        mTitle!!.text = "公交路线详情"
        mTitleBusRoute = findViewById(R.id.firstline) as TextView
        mDesBusRoute = findViewById(R.id.secondline) as TextView
        val dur = AMapUtil.getFriendlyTime(mBuspath!!.duration.toInt())
        val dis = AMapUtil.getFriendlyLength(mBuspath!!.distance.toInt())
        mTitleBusRoute!!.setText(dur + "(" + dis + ")")
        val taxiCost = mBusRouteResult!!.taxiCost.toInt()
        mDesBusRoute!!.text = "打车约" + taxiCost + "元"
        mDesBusRoute!!.visibility = View.VISIBLE
        mBusMap = findViewById(R.id.title_map) as LinearLayout
        mBusMap!!.visibility = View.VISIBLE
        mBuspathview = findViewById(R.id.bus_path) as LinearLayout
        configureListView()
    }

    private fun registerListener() {
        aMap!!.setOnMapLoadedListener(this)
        aMap!!.setOnMapClickListener(this)
        aMap!!.setOnMarkerClickListener(this)
        aMap!!.setOnInfoWindowClickListener(this)
        aMap!!.setInfoWindowAdapter(this)
    }

    private fun configureListView() {
        mBusSegmentList = findViewById(R.id.bus_segment_list) as ListView
        mBusSegmentListAdapter = BusSegmentListAdapter(
                this.applicationContext, mBuspath!!.steps)
        mBusSegmentList!!.adapter = mBusSegmentListAdapter

    }

    fun onBackClick(view: View) {
        this.finish()
    }

    /**
     * 点击事件的回调
     */
    fun onMapClick(view: View) {
            mBuspathview!!.visibility = View.GONE
            mBusMap!!.visibility = View.GONE
            mapView!!.visibility = View.VISIBLE
            aMap!!.clear()// 清理地图上的所有覆盖物
            mBusrouteOverlay = BusRouteOverlay(this, aMap,
                    mBuspath, mBusRouteResult!!.startPos,
                    mBusRouteResult!!.targetPos)
            mBusrouteOverlay!!.removeFromMap()
    }

    override fun onMapLoaded() {
        if (mBusrouteOverlay != null) {
            mBusrouteOverlay!!.addToMap()
            mBusrouteOverlay!!.zoomToSpan()
        }
    }

    override fun onMapClick(arg0: LatLng) {
        // TODO Auto-generated method stub

    }

    override fun getInfoContents(arg0: Marker): View? {
        // TODO Auto-generated method stub
        return null
    }

    override fun getInfoWindow(arg0: Marker): View? {
        // TODO Auto-generated method stub
        return null
    }

    override fun onInfoWindowClick(arg0: Marker) {
        // TODO Auto-generated method stub

    }

    override fun onMarkerClick(arg0: Marker): Boolean {
        // TODO Auto-generated method stub
        return false
    }


}
