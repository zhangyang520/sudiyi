package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.RelativeLayout
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.InfoWindowAdapter
import com.amap.api.maps.AMap.OnInfoWindowClickListener
import com.amap.api.maps.AMap.OnMapClickListener
import com.amap.api.maps.AMap.OnMarkerClickListener
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener
import com.amap.api.services.route.RouteSearch.WalkRouteQuery
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.utils.AMapUtil
import com.suntray.chinapost.map.utils.ToastUtil
import overlay.WalkRouteOverlay

/**
 * 步行路径规划 示例
 */
class WalkRouteActivity : Activity(), OnMapClickListener, OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener {
    private var aMap: AMap? = null
    private var mapView: MapView? = null
    private var mContext: Context? = null
    private var mRouteSearch: RouteSearch? = null
    private var mWalkRouteResult: WalkRouteResult? = null
    private val mStartPoint = LatLonPoint(39.942295, 116.335891)//起点，116.335891,39.942295
    private val mEndPoint = LatLonPoint(39.995576, 116.481288)//终点，116.481288,39.995576
    private val ROUTE_TYPE_WALK = 3

    private var mBottomLayout: RelativeLayout? = null
    private var mHeadLayout: RelativeLayout? = null
    private var mRotueTimeDes: TextView? = null
    private var mRouteDetailDes: TextView? = null
    private var progDialog: ProgressDialog? = null// 搜索时进度条
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.route_activity)

        mContext = this.applicationContext
        mapView = findViewById(R.id.route_map) as MapView
        mapView!!.onCreate(bundle)// 此方法必须重写
        init()
        setfromandtoMarker()
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault)
    }

    private fun setfromandtoMarker() {
        aMap!!.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)))
        aMap!!.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)))
    }

    /**
     * 初始化AMap对象
     */
    private fun init() {
        if (aMap == null) {
            aMap = mapView!!.map
        }
        registerListener()
        mRouteSearch = RouteSearch(this)
        mRouteSearch!!.setRouteSearchListener(this)
        mBottomLayout = findViewById(R.id.bottom_layout) as RelativeLayout
        mHeadLayout = findViewById(R.id.routemap_header) as RelativeLayout
        mHeadLayout!!.visibility = View.GONE
        mRotueTimeDes = findViewById(R.id.firstline) as TextView
        mRouteDetailDes = findViewById(R.id.secondline) as TextView

    }

    /**
     * 注册监听
     */
    private fun registerListener() {
        aMap!!.setOnMapClickListener(this@WalkRouteActivity)
        aMap!!.setOnMarkerClickListener(this@WalkRouteActivity)
        aMap!!.setOnInfoWindowClickListener(this@WalkRouteActivity)
        aMap!!.setInfoWindowAdapter(this@WalkRouteActivity)

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

    override fun onMapClick(arg0: LatLng) {
        // TODO Auto-generated method stub

    }


    /**
     * 开始搜索路径规划方案
     */
    fun searchRouteResult(routeType: Int, mode: Int) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "定位中，稍后再试...")
            return
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置")
        }
        showProgressDialog()
        val fromAndTo = RouteSearch.FromAndTo(
                mStartPoint, mEndPoint)
        if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            val query = WalkRouteQuery(fromAndTo, mode)
            mRouteSearch!!.calculateWalkRouteAsyn(query)// 异步路径规划步行模式查询
        }
    }

    override fun onBusRouteSearched(result: BusRouteResult, errorCode: Int) {

    }

    override fun onDriveRouteSearched(result: DriveRouteResult, errorCode: Int) {

    }

    override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {
        dissmissProgressDialog()
        aMap!!.clear()// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.paths != null) {
                if (result.paths.size > 0) {
                    mWalkRouteResult = result
                    val walkPath = mWalkRouteResult!!.paths[0] ?: return
                    val walkRouteOverlay = WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult!!.startPos,
                            mWalkRouteResult!!.targetPos)
                    walkRouteOverlay.removeFromMap()
                    walkRouteOverlay.addToMap()
                    walkRouteOverlay.zoomToSpan()
                    mBottomLayout!!.visibility = View.VISIBLE
                    val dis = walkPath.distance.toInt()
                    val dur = walkPath.duration.toInt()
                    val des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")"
                    mRotueTimeDes!!.setText(des)
                    mRouteDetailDes!!.visibility = View.GONE
                    mBottomLayout!!.setOnClickListener {
                        val intent = Intent(mContext,
                                WalkRouteDetailActivity::class.java)
                        intent.putExtra("walk_path", walkPath)
                        intent.putExtra("walk_result",
                                mWalkRouteResult)
                        startActivity(intent)
                    }
                } else if (result != null && result.paths == null) {
                    ToastUtil.show(mContext, R.string.no_result)
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result)
            }
        } else {
            ToastUtil.showerror(this.applicationContext, errorCode)
        }
    }


    /**
     * 显示进度框
     */
    private fun showProgressDialog() {
        if (progDialog == null) {
            progDialog = ProgressDialog(this)
        }
        progDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progDialog!!.isIndeterminate = false
        progDialog!!.setCancelable(true)
        progDialog!!.setMessage("正在搜索")
        progDialog!!.show()
    }

    /**
     * 隐藏进度框
     */
    private fun dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog!!.dismiss()
        }
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onRideRouteSearched(arg0: RideRouteResult, arg1: Int) {
        // TODO Auto-generated method stub

    }

}

