package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.*
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.amap.api.services.route.RouteSearch.DriveRouteQuery
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.utils.AMapUtil
import com.suntray.chinapost.map.utils.ToastUtil
import overlay.DrivingRouteOverlay

/**
 * 驾车出行路线规划 实现
 */
class DriveRouteActivity : Activity(), OnMapClickListener, OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter, OnRouteSearchListener {
    private var aMap: AMap? = null
    private var mapView: MapView? = null
    private var mContext: Context? = null
    private var mRouteSearch: RouteSearch? = null
    private var mDriveRouteResult: DriveRouteResult? = null
    private val mStartPoint = LatLonPoint(39.942295, 116.335891)//起点，39.942295,116.335891
    private val mEndPoint = LatLonPoint(39.995576, 116.481288)//终点，39.995576,116.481288

    private val ROUTE_TYPE_DRIVE = 2

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
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault)
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
        mRotueTimeDes = findViewById(R.id.firstline) as TextView
        mRouteDetailDes = findViewById(R.id.secondline) as TextView
        mHeadLayout!!.visibility = View.GONE
    }

    /**
     * 注册监听
     */
    private fun registerListener() {
        aMap!!.setOnMapClickListener(this@DriveRouteActivity)
        aMap!!.setOnMarkerClickListener(this@DriveRouteActivity)
        aMap!!.setOnInfoWindowClickListener(this@DriveRouteActivity)
        aMap!!.setInfoWindowAdapter(this@DriveRouteActivity)

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
        if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            val query = DriveRouteQuery(fromAndTo, mode, null, null, "")// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch!!.calculateDriveRouteAsyn(query)// 异步路径规划驾车模式查询
        }
    }

    override fun onBusRouteSearched(result: BusRouteResult, errorCode: Int) {

    }

    override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {
        dissmissProgressDialog()
        aMap!!.clear()// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.paths != null) {
                if (result.paths.size > 0) {
                    mDriveRouteResult = result
                    val drivePath = mDriveRouteResult!!.paths[0] ?: return
                    val drivingRouteOverlay = DrivingRouteOverlay(
                            mContext, aMap, drivePath,
                            mDriveRouteResult!!.startPos,
                            mDriveRouteResult!!.targetPos, null)
                    drivingRouteOverlay.setNodeIconVisibility(false)//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true)//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap()
                    drivingRouteOverlay.addToMap()
                    drivingRouteOverlay.zoomToSpan()
                    mBottomLayout!!.visibility = View.VISIBLE
                    val dis = drivePath.distance.toInt()
                    val dur = drivePath.duration.toInt()
                    val des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")"
                    mRotueTimeDes!!.setText(des)
                    mRouteDetailDes!!.visibility = View.VISIBLE
                    val taxiCost = mDriveRouteResult!!.taxiCost.toInt()
                    mRouteDetailDes!!.text = "打车约" + taxiCost + "元"
                    mBottomLayout!!.setOnClickListener {
                        val intent = Intent(mContext,
                                DriveRouteDetailActivity::class.java)
                        intent.putExtra("drive_path", drivePath)
                        intent.putExtra("drive_result",
                                mDriveRouteResult)
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

    override fun onWalkRouteSearched(result: WalkRouteResult, errorCode: Int) {

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

