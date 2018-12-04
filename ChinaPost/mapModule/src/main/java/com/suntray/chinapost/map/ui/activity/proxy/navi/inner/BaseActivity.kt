package com.suntray.chinapost.map.ui.activity.proxy.navi.inner

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast

import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.AMapNaviViewListener
import com.amap.api.navi.model.AMapCalcRouteResult
import com.amap.api.navi.model.AMapLaneInfo
import com.amap.api.navi.model.AMapModelCross
import com.amap.api.navi.model.AMapNaviCameraInfo
import com.amap.api.navi.model.AMapNaviCross
import com.amap.api.navi.model.AMapNaviInfo
import com.amap.api.navi.model.AMapNaviLocation
import com.amap.api.navi.model.AMapNaviRouteNotifyData
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo
import com.amap.api.navi.model.AMapServiceAreaInfo
import com.amap.api.navi.model.AimLessModeCongestionInfo
import com.amap.api.navi.model.AimLessModeStat
import com.amap.api.navi.model.NaviInfo
import com.amap.api.navi.model.NaviLatLng
import com.amap.api.services.core.LatLonPoint
import com.autonavi.tbt.TrafficFacilityInfo
import com.suntray.chinapost.map.ui.activity.proxy.navi.util.ErrorInfo
import com.suntray.chinapost.map.ui.activity.proxy.navi.util.TTSController

import java.util.ArrayList


open class BaseActivity : Activity(), AMapNaviListener, AMapNaviViewListener {

     var mAMapNaviView: AMapNaviView? = null
     var mAMapNavi: AMapNavi?=null
    protected var mTtsManager: TTSController? = null
    private val mStartPoint = NaviLatLng(39.942295, 116.335891)//起点，116.335891,39.942295
    private val mEndPoint = NaviLatLng(39.995576, 116.481288)//终点，116.481288,39.995576
    protected val sList: MutableList<NaviLatLng> = ArrayList()
    protected val eList: MutableList<NaviLatLng> = ArrayList()
    protected var mWayPointList: List<NaviLatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mAMapNavi = AMapNavi.getInstance(applicationContext)
        mAMapNavi!!.addAMapNaviListener(this)
        mAMapNavi!!.setUseInnerVoice(true)

        //设置模拟导航的行车速度
        mAMapNavi!!.setEmulatorNaviSpeed(0)
        sList.add(mStartPoint!!)
        eList.add(mEndPoint!!)
    }

    override fun onResume() {
        super.onResume()
        mAMapNaviView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mAMapNaviView!!.onPause()

        //
        //        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
        //        mAMapNavi.stopNavi();
    }

    override fun onDestroy() {
        super.onDestroy()
        mAMapNaviView!!.onDestroy()
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi!!.stopNavi()
        mAMapNavi!!.destroy()
    }

    override fun onInitNaviFailure() {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show()
    }

    override fun onInitNaviSuccess() {
        //初始化成功
    }

    override fun onStartNavi(type: Int) {
        //开始导航回调
    }

    override fun onTrafficStatusUpdate() {
        //
    }

    override fun onLocationChange(location: AMapNaviLocation) {
        //当前位置回调
    }

    override fun onGetNavigationText(type: Int, text: String) {
        //播报类型和播报文字回调
    }

    override fun onGetNavigationText(s: String) {

    }

    override fun onEndEmulatorNavi() {
        //结束模拟导航
    }

    override fun onArriveDestination() {
        //到达目的地
    }

    override fun onCalculateRouteFailure(errorInfo: Int) {
        //路线计算失败
        Log.e("dm", "--------------------------------------------")
        Log.i("dm", "路线计算失败：错误码=" + errorInfo + ",Error Message= " + ErrorInfo.getError(errorInfo))
        Log.i("dm", "错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/")
        Log.e("dm", "--------------------------------------------")
        Toast.makeText(this, "errorInfo：" + errorInfo + ",Message：" + ErrorInfo.getError(errorInfo), Toast.LENGTH_LONG).show()
    }

    override fun onReCalculateRouteForYaw() {
        //偏航后重新计算路线回调
    }

    override fun onReCalculateRouteForTrafficJam() {
        //拥堵后重新计算路线回调
    }

    override fun onArrivedWayPoint(wayID: Int) {
        //到达途径点
    }

    override fun onGpsOpenStatus(enabled: Boolean) {
        //GPS开关状态回调
    }

    override fun onNaviSetting() {
        //底部导航设置点击回调
    }

    override fun onNaviMapMode(isLock: Int) {
        //地图的模式，锁屏或锁车
    }

    override fun onNaviCancel() {
        finish()
    }


    override fun onNaviTurnClick() {
        //转弯view的点击回调
    }

    override fun onNextRoadClick() {
        //下一个道路View点击回调
    }


    override fun onScanViewButtonClick() {
        //全览按钮点击回调
    }

    @Deprecated("")
    override fun onNaviInfoUpdated(naviInfo: AMapNaviInfo) {
        //过时
    }

    override fun updateCameraInfo(aMapCameraInfos: Array<AMapNaviCameraInfo>) {

    }

    override fun onServiceAreaUpdate(amapServiceAreaInfos: Array<AMapServiceAreaInfo>) {

    }

    override fun onNaviInfoUpdate(naviinfo: NaviInfo) {
        //导航过程中的信息更新，请看NaviInfo的具体说明
    }

    override fun OnUpdateTrafficFacility(trafficFacilityInfo: TrafficFacilityInfo) {
        //已过时
    }

    override fun OnUpdateTrafficFacility(aMapNaviTrafficFacilityInfo: AMapNaviTrafficFacilityInfo) {
        //已过时
    }

    override fun showCross(aMapNaviCross: AMapNaviCross) {
        //显示转弯回调
    }

    override fun hideCross() {
        //隐藏转弯回调
    }

    override fun showLaneInfo(laneInfos: Array<AMapLaneInfo>, laneBackgroundInfo: ByteArray, laneRecommendedInfo: ByteArray) {
        //显示车道信息

    }

    override fun hideLaneInfo() {
        //隐藏车道信息
    }

    override fun onCalculateRouteSuccess(ints: IntArray) {
        //多路径算路成功回调
    }

    override fun notifyParallelRoad(i: Int) {
        if (i == 0) {
            Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show()
            Log.d("wlx", "当前在主辅路过渡")
            return
        }
        if (i == 1) {
            Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show()

            Log.d("wlx", "当前在主路")
            return
        }
        if (i == 2) {
            Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show()

            Log.d("wlx", "当前在辅路")
        }
    }

    override fun OnUpdateTrafficFacility(aMapNaviTrafficFacilityInfos: Array<AMapNaviTrafficFacilityInfo>) {
        //更新交通设施信息
    }

    override fun updateAimlessModeStatistics(aimLessModeStat: AimLessModeStat) {
        //更新巡航模式的统计信息
    }


    override fun updateAimlessModeCongestionInfo(aimLessModeCongestionInfo: AimLessModeCongestionInfo) {
        //更新巡航模式的拥堵信息
    }

    override fun onPlayRing(i: Int) {

    }


    override fun onLockMap(isLock: Boolean) {
        //锁地图状态发生变化时回调
    }

    override fun onNaviViewLoaded() {
        Log.d("wlx", "导航页面加载成功")
        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑")
    }

    override fun onMapTypeChanged(i: Int) {

    }

    override fun onNaviViewShowMode(i: Int) {

    }

    override fun onNaviBackClick(): Boolean {
        return false
    }


    override fun showModeCross(aMapModelCross: AMapModelCross) {

    }

    override fun hideModeCross() {

    }

    override fun updateIntervalCameraInfo(aMapNaviCameraInfo: AMapNaviCameraInfo, aMapNaviCameraInfo1: AMapNaviCameraInfo, i: Int) {

    }

    override fun showLaneInfo(aMapLaneInfo: AMapLaneInfo) {

    }

    override fun onCalculateRouteSuccess(aMapCalcRouteResult: AMapCalcRouteResult) {

    }

    override fun onCalculateRouteFailure(aMapCalcRouteResult: AMapCalcRouteResult) {

    }

    override fun onNaviRouteNotify(aMapNaviRouteNotifyData: AMapNaviRouteNotifyData) {

    }
}
