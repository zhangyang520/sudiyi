package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.TruckPath
import com.amap.api.services.route.TruckRouteRestult
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.utils.AMapUtil
import com.suntray.chinapost.map.utils.ToastUtil
import overlay.TruckRouteColorfulOverLay

/**
 * 货车出行路线规划 实现
 */
class TruckRouteActivity : Activity(), RouteSearch.OnTruckRouteSearchListener {
    private var aMap: AMap? = null
    private var mapView: MapView? = null
    private var mContext: Context? = null
    private var mRouteSearch: RouteSearch? = null
    private var truckRouteResult: TruckRouteRestult? = null
    private val mStartPoint = LatLonPoint(39.902896, 116.42792)
    private val mEndPoint = LatLonPoint(39.894914, 116.322062)//终点，39.995576,116.481288


    private var progDialog: ProgressDialog? = null// 搜索时进度条
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.route_activity)

        mContext = this.applicationContext
        mapView = findViewById(R.id.route_map) as MapView
        mapView!!.onCreate(bundle)// 此方法必须重写
        init()
        setfromandtoMarker()
        searchRouteResult(RouteSearch.TRUCK_AVOID_CONGESTION)
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
        mRouteSearch = RouteSearch(this)
        mRouteSearch!!.setOnTruckRouteSearchListener(this)


        //隐藏顶部控件
        (findViewById(R.id.routemap_header) as RelativeLayout).setVisibility(View.GONE)
    }


    /**
     * 开始搜索路径规划方案
     */
    fun searchRouteResult(mode: Int) {
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


        fromAndTo.plateNumber = "A6BN05"
        fromAndTo.plateProvince = "京"

        // 第一个参数表示路径规划的起点和终点，第二个参数表示计算路径的模式，第三个参数表示途经点，第四个参数货车大小 必填
        val query = RouteSearch.TruckRouteQuery(fromAndTo, mode, null, RouteSearch.TRUCK_SIZE_HEAVY)

        query.truckAxis = 6f
        query.truckHeight = 3.9f
        query.truckWidth = 3f
        query.truckLoad = 45f
        query.truckWeight = 50f

        //异步查询
        mRouteSearch!!.calculateTruckRouteAsyn(query)
    }

    /**
     * 显示进度框
     */
    private fun showProgressDialog() {
        if (progDialog == null) {
            progDialog = ProgressDialog(this)
            progDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            progDialog!!.isIndeterminate = false
            progDialog!!.setCancelable(true)
            progDialog!!.setMessage("正在搜索")
            progDialog!!.show()
        }
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


    override fun onTruckRouteSearched(result: TruckRouteRestult?, rCode: Int) {
        dissmissProgressDialog()

        //建议通过TruckPath中getRestriction() 判断路线上是否存在限行
        /**
         * 限行结果
         * 0，未知（未输入完整/正确车牌号信息时候显示）
         * 1，已规避限行
         * 2，起点限行
         * 3，途径点在限行区域内（设置途径点才出现此报错）
         * 4，途径限行区域
         * 5，终点限行
         */

        if (rCode == 1000) {
            if (result != null && result.paths != null
                    && result.paths.size > 0) {
                truckRouteResult = result
                val path = truckRouteResult!!.paths[0] ?: return
                aMap!!.clear()// 清理地图上的所有覆盖物

                val drivingRouteOverlay = TruckRouteColorfulOverLay(
                        this, aMap, path, truckRouteResult!!.startPos,
                        truckRouteResult!!.targetPos, null)

                drivingRouteOverlay.removeFromMap()
                drivingRouteOverlay.setIsColorfulline(true)
                drivingRouteOverlay.addToMap()
                drivingRouteOverlay.zoomToSpan()
            } else {
                ToastUtil.show(this@TruckRouteActivity, R.string.no_result)
            }
        } else if (rCode == 1904) {
            ToastUtil.show(this@TruckRouteActivity, R.string.error_network)
        } else if (rCode == 1002) {
            ToastUtil.show(this@TruckRouteActivity, R.string.error_key)
        } else {
            Toast.makeText(this@TruckRouteActivity, "结果：$rCode", Toast.LENGTH_SHORT).show()
        }
    }
}

