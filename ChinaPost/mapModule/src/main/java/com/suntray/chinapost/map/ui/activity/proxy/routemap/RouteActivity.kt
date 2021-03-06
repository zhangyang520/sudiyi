package com.suntray.chinapost.map.ui.activity.proxy.routemap

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.route.*
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.map.data.bean.TaskEntity
import com.suntray.chinapost.map.ui.adapter.proxy.BusResultListAdapter
import com.suntray.chinapost.map.utils.AMapUI
import com.suntray.chinapost.map.utils.AMapUtil
import com.suntray.chinapost.map.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import kotlinx.android.synthetic.main.route_activity.*
import overlay.DrivingRouteOverlay
import overlay.WalkRouteOverlay

/**
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/24 15:06 
 */
/**
 * Route路径规划: 驾车规划、公交规划、步行规划
 */
@Route(path=RouterPath.MapModule.POST_TASK_ROUTE)
class RouteActivity : Activity(), AMap.OnMapClickListener, AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {
    private var aMap: AMap? = null
    private var mapView: MapView? = null
    private var mContext: Context? = null
    private var mRouteSearch: RouteSearch? = null
    private var mDriveRouteResult: DriveRouteResult? = null
    private var mBusRouteResult: BusRouteResult? = null
    private var mWalkRouteResult: WalkRouteResult? = null
    private val mStartPoint = LatLonPoint(39.942295, 116.335891)//起点，116.335891,39.942295
    private val mEndPoint = LatLonPoint(39.995576, 116.481288)//终点，116.481288,39.995576

    private val mStartPoint_bus = LatLonPoint(40.818311, 111.670801)//起点，111.670801,40.818311
    private val mEndPoint_bus = LatLonPoint(44.433942, 125.184449)//终点，

    private val mCurrentCityName = "北京"
    private val ROUTE_TYPE_BUS = 1
    private val ROUTE_TYPE_DRIVE = 2
    private val ROUTE_TYPE_WALK = 3
    private val ROUTE_TYPE_CROSSTOWN = 4

    private var mBusResultLayout: LinearLayout? = null
    private var mBottomLayout: RelativeLayout? = null
    private var mRotueTimeDes: TextView? = null
    private var mRouteDetailDes: TextView? = null
    private var mBus: ImageView? = null
    private var mDrive: ImageView? = null
    private var mWalk: ImageView? = null
    private var mBusResultList: ListView? = null
    private var progDialog: ProgressDialog? = null// 搜索时进度条

    //任务的实体
    private var taskEntity:TaskEntity?=null
    //定位实体
    private var currntLocation:AMapLocation?=null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.route_activity)

        //目标经纬度
        taskEntity=intent.getSerializableExtra("taskEntity") as TaskEntity
        //当前的经纬度
        currntLocation=intent.getParcelableExtra<AMapLocation>("currntLocation")

        mStartPoint.latitude=currntLocation!!.latitude
        mStartPoint.longitude=currntLocation!!.longitude

        mEndPoint.latitude=taskEntity!!.latitude.toDouble()
        mEndPoint.longitude=taskEntity!!.longitude.toDouble()

        mStartPoint_bus.latitude=currntLocation!!.latitude
        mStartPoint_bus.longitude=currntLocation!!.longitude

        mEndPoint_bus.latitude=taskEntity!!.latitude.toDouble()
        mEndPoint_bus.longitude=taskEntity!!.longitude.toDouble()

        mContext = this.applicationContext
        mapView = findViewById(R.id.route_map) as MapView
        mapView!!.onCreate(bundle)// 此方法必须重写
        init()
        //		getIntentData();
        setfromandtoMarker()
    }

    private fun setfromandtoMarker() {
        aMap!!.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)))

        aMap!!.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)))

//        AMapUI.doDistrictCanvas(this, currntLocation!!.district,aMap!!);

//        AMapUI.poiDot(this,aMap!!,LatLonPoint(mEndPoint.latitude,mEndPoint.longitude),1000)

        onDriveClick(null);
        rl_back.setOnClickListener({
            finish()
        })
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
        mBusResultLayout = findViewById(R.id.bus_result) as LinearLayout
        mRotueTimeDes = findViewById(R.id.firstline) as TextView
        mRouteDetailDes = findViewById(R.id.secondline) as TextView
        mDrive = findViewById(R.id.route_drive) as ImageView
        mBus = findViewById(R.id.route_bus) as ImageView
        mWalk = findViewById(R.id.route_walk) as ImageView
        mBusResultList = findViewById(R.id.bus_result_list) as ListView
    }

    /**
     * 注册监听
     */
    private fun registerListener() {
        aMap!!.setOnMapClickListener(this@RouteActivity)
        aMap!!.setOnMarkerClickListener(this@RouteActivity)
        aMap!!.setOnInfoWindowClickListener(this@RouteActivity)
        aMap!!.setInfoWindowAdapter(this@RouteActivity)

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
     * 公交路线搜索
     */
    fun onBusClick(view: View) {
        searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault)
        mDrive!!.setImageResource(R.drawable.route_drive_normal)
        mBus!!.setImageResource(R.drawable.route_bus_select)
        mWalk!!.setImageResource(R.drawable.route_walk_normal)
        mapView!!.visibility = View.GONE
        mBusResultLayout!!.visibility = View.VISIBLE
    }

    /**
     * 驾车路线搜索
     */
    fun onDriveClick(view: View?) {
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault)
        mDrive!!.setImageResource(R.drawable.route_drive_select)
        mBus!!.setImageResource(R.drawable.route_bus_normal)
        mWalk!!.setImageResource(R.drawable.route_walk_normal)
        mapView!!.visibility = View.VISIBLE
        mBusResultLayout!!.visibility = View.GONE
    }

    /**
     * 步行路线搜索
     */
    fun onWalkClick(view: View) {
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault)
        mDrive!!.setImageResource(R.drawable.route_drive_normal)
        mBus!!.setImageResource(R.drawable.route_bus_normal)
        mWalk!!.setImageResource(R.drawable.route_walk_select)
        mapView!!.visibility = View.VISIBLE
        mBusResultLayout!!.visibility = View.GONE
    }

    /**
     * 跨城公交路线搜索
     */
    fun onCrosstownBusClick(view: View) {
        searchRouteResult(ROUTE_TYPE_CROSSTOWN, RouteSearch.BusDefault)
        mDrive!!.setImageResource(R.drawable.route_drive_normal)
        mBus!!.setImageResource(R.drawable.route_bus_normal)
        mWalk!!.setImageResource(R.drawable.route_walk_normal)
        mapView!!.visibility = View.GONE
        mBusResultLayout!!.visibility = View.VISIBLE
    }

    /**
     * 开始搜索路径规划方案
     */
    fun searchRouteResult(routeType: Int, mode: Int) {
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "起点未设置")
            return
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置")
        }
        showProgressDialog()
        val fromAndTo = RouteSearch.FromAndTo(
                mStartPoint, mEndPoint)
        if (routeType == ROUTE_TYPE_BUS) {// 公交路径规划
            val query = RouteSearch.BusRouteQuery(fromAndTo, mode,
                    mCurrentCityName, 0)// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            mRouteSearch!!.calculateBusRouteAsyn(query)// 异步路径规划公交模式查询
        } else if (routeType == ROUTE_TYPE_DRIVE) {// 驾车路径规划
            val query = RouteSearch.DriveRouteQuery(fromAndTo, mode, null, null, "")// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
            mRouteSearch!!.calculateDriveRouteAsyn(query)// 异步路径规划驾车模式查询
        } else if (routeType == ROUTE_TYPE_WALK) {// 步行路径规划
            val query = RouteSearch.WalkRouteQuery(fromAndTo)
            mRouteSearch!!.calculateWalkRouteAsyn(query)// 异步路径规划步行模式查询
        } else if (routeType == ROUTE_TYPE_CROSSTOWN) {
            val fromAndTo_bus = RouteSearch.FromAndTo(
                    mStartPoint_bus, mEndPoint_bus)
            val query = RouteSearch.BusRouteQuery(fromAndTo_bus, mode,
                    "呼和浩特市", 0)// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
            query.cityd = "农安县"
            mRouteSearch!!.calculateBusRouteAsyn(query)// 异步路径规划公交模式查询
        }
    }

    /**
     * 公交路线搜索结果方法回调
     */
    override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {
        dissmissProgressDialog()
        mBottomLayout!!.visibility = View.GONE
        aMap!!.clear()// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.paths != null) {
                if (result.paths.size > 0) {
                    mBusRouteResult = result
                    val mBusResultListAdapter = BusResultListAdapter(this@RouteActivity, mBusRouteResult!!)
                    mBusResultList!!.adapter = mBusResultListAdapter
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
     * 驾车路线搜索结果方法回调
     */
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
                        intent.putExtra("startPoint",mStartPoint)
                        intent.putExtra("endPoint",mEndPoint)
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

    /**
     * 步行路线搜索结果方法回调
     */
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
                        intent.putExtra("startPoint",mStartPoint)
                        intent.putExtra("endPoint",mEndPoint)
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

    /**
     * 骑行路线搜索结果方法回调
     */
    override fun onRideRouteSearched(arg0: RideRouteResult, arg1: Int) {
        // TODO Auto-generated method stub

    }

}