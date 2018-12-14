package com.suntray.chinapost.map.utils

import android.content.Context
import android.graphics.BitmapFactory
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import java.util.ArrayList
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.MapDot

/**
 * 自定义PoiOverlay
 *
 */

open class myPoiOverlay(private val mamap: AMap?, private val mPois: List<MapDot>?, val currntLocation: LatLonPoint?=null) {
    private val mPoiMarks = ArrayList<Marker>()

    private val latLngBounds: LatLngBounds
        get() {
            val b = LatLngBounds.builder()
            if (mPois != null) {
                val size = mPois.size
                for (i in 0 until size) {
                    if(mPois[i].latitude!=null && !mPois[i].latitude.trim().equals("")  && mPois[i].longitude!=null && !mPois[i].longitude.trim().equals("")){
                        b.include(LatLng(mPois[i].latitude.toDouble(),
                                mPois[i].longitude.toDouble()))
                    }
                }
            }
            return b.build()
        }

    /**
     * 添加Marker到地图中。
     * @since V2.1.0
     */
    fun addToMap(context: Context) {
        if (mPois != null) {
            val size = mPois.size
            for (i in 0 until size) {
                if(mPois!![i].latitude!=null && !mPois[i].latitude.trim().equals("")  && mPois[i].longitude!=null && !mPois[i].longitude.trim().equals("")){
                    val marker = mamap!!.addMarker(getMarkerOptions(i,context))
                    val item = mPois[i]
                    marker.`object` = item
                    mPoiMarks.add(marker)
                }
            }
        }
    }

    /**
     * 去掉PoiOverlay上所有的Marker。
     *
     * @since V2.1.0
     */
    fun removeFromMap() {
        for (mark in mPoiMarks) {
            mark.remove()
        }
    }

    /**
     * 移动镜头到当前的视角。
     * @since V2.1.0
     */
    fun zoomToSpan() {
        if (mPois != null && mPois.size > 0) {
            if (mamap == null)
                return
            val bounds = latLngBounds
            mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }else if(currntLocation!=null){
            val bounds = getLatLngBounds1()
            mamap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 5))
//            mamap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currntLocation.getLatitude(),
//                    currntLocation.getLongitude()), 13f))
        }
    }

     fun getLatLngBounds1(): LatLngBounds? {
        if(mPois!!.size>0){
            val b = LatLngBounds.builder()
            for (i in mPois.indices) {
                b.include(LatLng(mPois.get(i).latitude.toDouble(),
                        mPois.get(i).longitude.toDouble()))
            }
            return b.build()
        }else{
            val b = LatLngBounds.builder()
                b.include(LatLng(currntLocation!!.latitude,
                        currntLocation.longitude))
            return b.build()
        }
    }

    private fun getMarkerOptions(index: Int,context: Context): MarkerOptions {
        return MarkerOptions()
                .position(
                        LatLng(mPois!![index]
                                .latitude.toDouble(), mPois[index]
                                .longitude.toDouble()))
                .title(mPois!![index].pointname).snippet(mPois!![index].zoneaddress)
                .icon(getBitmapDescriptor(index,context))
    }


    /**
     * 从marker中得到poi在list的位置。
     *
     * @param marker 一个标记的对象。
     * @return 返回该marker对应的poi在list的位置。
     * @since V2.1.0
     */
    fun getPoiIndex(marker: Marker): Int {
        for (i in mPoiMarks.indices) {
            if (mPoiMarks[i] == marker) {
                return i
            }
        }
        return -1
    }

    /**
     * 返回第index的poi的信息。
     * @param index 第几个poi。
     * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 **[PoiItem](../../../../../../Search/com/amap/api/services/core/PoiItem.html)**。
     * @since V2.1.0
     */
    fun getPoiItem(index: Int): MapDot? {
        return if (index < 0 || index >= mPois!!.size) {
            null
        } else mPois[index]
    }

    protected fun getBitmapDescriptor(index: Int,context: Context): BitmapDescriptor? {
        return if (index < 10) {
            BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(context.getResources(), markers[index]))
        } else {
            BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(context.getResources(), R.drawable.dingwei_default))
        }
    }

    companion object {
//        val markers = intArrayOf(R.drawable.poi_marker_1, R.drawable.poi_marker_2,
//                R.drawable.poi_marker_3, R.drawable.poi_marker_4, R.drawable.poi_marker_5,
//                R.drawable.poi_marker_6, R.drawable.poi_marker_7, R.drawable.poi_marker_8,
//                R.drawable.poi_marker_9, R.drawable.poi_marker_10)

        val markers = intArrayOf(R.drawable.dingwei_default, R.drawable.dingwei_default,
                R.drawable.dingwei_default, R.drawable.dingwei_default, R.drawable.dingwei_default,
                R.drawable.dingwei_default, R.drawable.dingwei_default, R.drawable.dingwei_default,
                R.drawable.dingwei_default, R.drawable.dingwei_default)
    }
}