package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.DistanceItem
import com.amap.api.services.route.DistanceResult
import com.amap.api.services.route.DistanceSearch
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.utils.AMapUtil

import java.util.ArrayList

/**
 * 距离测量 实现
 */
class RouteDistanceActivity : Activity(), DistanceSearch.OnDistanceSearchListener, AMap.OnMapLoadedListener {
    private var aMap: AMap? = null
    private var mapView: MapView? = null
    private var mContext: Context? = null
    private var distanceSearch: DistanceSearch? = null


    private val mDistanceText = ArrayList<Text>()


    // 北京站
    private val start0 = LatLonPoint(39.902896, 116.42792)
    // 北京南站
    private val start1 = LatLonPoint(39.865208, 116.378596)

    // 北京西站
    private val start2 = LatLonPoint(39.894914, 116.322062)

    // 北京北站
    private val start3 = LatLonPoint(39.945261, 116.352994)

    // 北京市政府
    private val dest = LatLonPoint(39.90455, 116.407555)


    private var progDialog: ProgressDialog? = null// 搜索时进度条
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.route_activity)

        mContext = this.applicationContext
        mapView = findViewById(R.id.route_map) as MapView
        mapView!!.onCreate(bundle)// 此方法必须重写
        init()

        setFromAndTo()
    }


    /**
     * 初始化AMap对象
     */
    private fun init() {
        if (aMap == null) {
            aMap = mapView!!.map
        }

        aMap!!.setOnMapLoadedListener(this)

        //隐藏顶部控件
        (findViewById(R.id.routemap_header) as RelativeLayout).setVisibility(View.GONE)
    }


    private fun setFromAndTo() {

        val latLngStart0 = AMapUtil.convertToLatLng(start0)
        val latLngStart1 = AMapUtil.convertToLatLng(start1)
        val latLngStart2 = AMapUtil.convertToLatLng(start2)
        val latLngStart3 = AMapUtil.convertToLatLng(start3)
        val latLngEnd = AMapUtil.convertToLatLng(dest)

        aMap!!.addMarker(MarkerOptions()
                .position(latLngStart0)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_1)))
        aMap!!.addMarker(MarkerOptions()
                .position(latLngStart1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_2)))
        aMap!!.addMarker(MarkerOptions()
                .position(latLngStart2)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_3)))
        aMap!!.addMarker(MarkerOptions()
                .position(latLngStart3)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.poi_marker_4)))

        aMap!!.addMarker(MarkerOptions()
                .position(latLngEnd)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)))



        aMap!!.addPolyline(PolylineOptions().add(latLngStart0, latLngEnd).color(Color.GREEN))
        aMap!!.addPolyline(PolylineOptions().add(latLngStart1, latLngEnd).color(Color.GREEN))
        aMap!!.addPolyline(PolylineOptions().add(latLngStart2, latLngEnd).color(Color.GREEN))
        aMap!!.addPolyline(PolylineOptions().add(latLngStart3, latLngEnd).color(Color.GREEN))


        mDistanceText.add(aMap!!.addText(TextOptions().position(getMidLatLng(latLngStart0, latLngEnd)).text("cal distance ...")))
        mDistanceText.add(aMap!!.addText(TextOptions().position(getMidLatLng(latLngStart1, latLngEnd)).text("cal distance ...")))
        mDistanceText.add(aMap!!.addText(TextOptions().position(getMidLatLng(latLngStart2, latLngEnd)).text("cal distance ...")))
        mDistanceText.add(aMap!!.addText(TextOptions().position(getMidLatLng(latLngStart3, latLngEnd)).text("cal distance ...")))

    }

    /**
     * 求两个经纬度的中点
     * @param l1
     * @param l2
     * @return
     */
    private fun getMidLatLng(l1: LatLng, l2: LatLng): LatLng {
        return LatLng((l1.latitude + l2.latitude) / 2, (l1.longitude + l2.longitude) / 2)
    }


    override fun onMapLoaded() {

        //展示出所有点
        val builder = LatLngBounds.Builder()
        builder.include(AMapUtil.convertToLatLng(start0))
        builder.include(AMapUtil.convertToLatLng(start1))
        builder.include(AMapUtil.convertToLatLng(start2))
        builder.include(AMapUtil.convertToLatLng(start3))
        builder.include(AMapUtil.convertToLatLng(dest))
        aMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))

        distanceSearch = DistanceSearch(this)
        distanceSearch!!.setDistanceSearchListener(this)

        searchDistanceResult(DistanceSearch.TYPE_DRIVING_DISTANCE)
    }


    /**
     * 开始搜索路径规划方案
     */
    fun searchDistanceResult(mode: Int) {
        showProgressDialog()
        val distanceQuery = DistanceSearch.DistanceQuery()

        val latLonPoints = ArrayList<LatLonPoint>()
        latLonPoints.add(start0)
        latLonPoints.add(start1)
        latLonPoints.add(start2)
        latLonPoints.add(start3)
        distanceQuery.origins = latLonPoints
        distanceQuery.destination = dest
        distanceQuery.type = mode

        distanceSearch!!.calculateRouteDistanceAsyn(distanceQuery)
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

    override fun onDistanceSearched(distanceResult: DistanceResult, errorCode: Int) {

        dissmissProgressDialog()

        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            try {
                Log.i("amap", "onDistanceSearched $distanceResult")
                val distanceItems = distanceResult.distanceResults
                val distanceQuery = distanceResult.distanceQuery
                val origins = distanceQuery.origins
                val destLatlon = distanceQuery.destination

                if (distanceItems == null) {
                    return
                }

                var index = 1
                for (item in distanceItems) {
                    val stringBuffer = StringBuffer()
                    //item.getOriginId() - 1 是因为 下标从1开始
                    stringBuffer.append("\n\torid: ").append(item.originId).append(" ").append(origins[item.originId - 1]).append("\n")
                    stringBuffer.append("\tdeid: ").append(item.destId).append(" ").append(destLatlon).append("\n")
                    stringBuffer.append("\tdis: ").append(item.distance).append(" , ")
                    stringBuffer.append("\tdur: ").append(item.duration)

                    if (item.errorInfo != null) {
                        stringBuffer.append(" , ").append("err: ").append(item.errorCode).append(" ").append(item.errorInfo)
                    }

                    stringBuffer.append("\n")
                    Log.i("amap", "onDistanceSearched " + index + " : " + stringBuffer.toString())


                    mDistanceText[index - 1].text = item.distance.toString() + " 米 " + item.duration + " 秒"

                    index++


                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
    }

}

