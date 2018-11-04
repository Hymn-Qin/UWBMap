package com.zzdc.uwb.AMap

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.amap.api.navi.*
import com.amap.api.navi.model.AMapNaviLocation
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.LatLonPoint
import com.zzdc.uwb.R
import kotlinx.android.synthetic.main.activity_amap_route.*
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import com.amap.api.services.route.*
import com.zzdc.uwb.AppConfig.UWBConfig.END_POINT
import com.zzdc.uwb.AppConfig.UWBConfig.START_POINT
import com.zzdc.uwb.FMap.FMapStartActivity

class AMapRouteActivity : AMapBasicActivity(),
        RouteSearch.OnRouteSearchListener,
        AMap.OnMapClickListener,
        AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener,
        AMap.InfoWindowAdapter,
        View.OnClickListener, INaviInfoCallback {

    private val TAG = AMapRouteActivity::class.java.simpleName
    private var mStartPoint: LatLonPoint? = null
    private var mEndPoint: LatLonPoint? = null
    private var mRouteSearch: RouteSearch? = null

    private val mCurrentCityName = "郑州"
    private val ROUTE_TYPE_BUS = 1
    private val ROUTE_TYPE_DRIVE = 2
    private val ROUTE_TYPE_WALK = 3
    private val ROUTE_TYPE_CROSSTOWN = 4

    private var progDialog: ProgressDialog? = null

    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amap_route)
        //设置默认位置
        val centerBJPoint = LatLng(super.LHG, super.LAT)
        val mapOptions = AMapOptions()
        mapOptions.camera(CameraPosition(centerBJPoint, 10f, 0f, 0f))
        mapView = MapView(this, mapOptions)
        route_map.addView(mapView)
        mapView!!.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        aMap = mapView!!.map
        aMap!!.mapType = AMap.MAP_TYPE_NORMAL
        mUiSettings = aMap!!.uiSettings

        start_rout.setOnClickListener(this)
        on_drive.setOnClickListener(this)
        on_Bus.setOnClickListener(this)
        on_walk.setOnClickListener(this)
        detail.setOnClickListener(this)
        //去FM MAP
        to_fm.setOnClickListener{
            val intent = Intent(this, FMapStartActivity::class.java)
            startActivity(intent)
        }
        initData()
    }
    override fun initData() {
        super.initData()
        mStartPoint = START_POINT
        mEndPoint = END_POINT
        registerListener()
        mRouteSearch = RouteSearch(this)
        mRouteSearch!!.setRouteSearchListener(this)

        setFromAndToMarker()
        onWalkClick()
    }

    private fun setFromAndToMarker() {
        aMap!!.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint!!))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start)))
        aMap!!.addMarker(MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint!!))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end))
                .title("终点")
                .snippet("详细信息")
                .draggable(true)).showInfoWindow()
    }


    /**
     * 注册监听
     */
    private fun registerListener() {
        aMap!!.setOnMapClickListener(this@AMapRouteActivity)
        aMap!!.setOnMarkerClickListener(this@AMapRouteActivity)
        aMap!!.setOnInfoWindowClickListener(this@AMapRouteActivity)
        aMap!!.setInfoWindowAdapter(this@AMapRouteActivity)
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
    private fun onBusClick() {
        ToastUtil.show(this, R.string.no_result)
        //        searchRouteResult(ROUTE_TYPE_BUS, RouteSearch.BusDefault);
        //        mDrive.setImageResource(R.drawable.route_drive_normal);
        //        mBus.setImageResource(R.drawable.route_bus_select);
        //        mWalk.setImageResource(R.drawable.route_walk_normal);
        //        mapView.setVisibility(View.GONE);
        //        mBusResultLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 驾车路线搜索
     */
    private fun onDriveClick() {
        searchRouteResult(ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault)
        route_drive.setImageResource(R.drawable.route_drive_select)
        route_bus.setImageResource(R.drawable.route_bus_normal)
        route_walk.setImageResource(R.drawable.route_walk_normal)
        mapView!!.visibility = View.VISIBLE
//        bus_result.visibility = View.GONE
    }

    /**
     * 步行路线搜索
     */
    private fun onWalkClick() {
        searchRouteResult(ROUTE_TYPE_WALK, RouteSearch.WalkDefault)
        route_drive.setImageResource(R.drawable.route_drive_normal)
        route_bus.setImageResource(R.drawable.route_bus_normal)
        route_walk.setImageResource(R.drawable.route_walk_select)
        mapView!!.visibility = View.VISIBLE
//        bus_result.visibility = View.GONE
    }

    /**
     * 跨城公交路线搜索
     */
    private fun onCrosstownBusClick() {
        ToastUtil.show(this, R.string.no_result)
        //        searchRouteResult(ROUTE_TYPE_CROSSTOWN, RouteSearch.BusDefault);
        //        mDrive.setImageResource(R.drawable.route_drive_normal);
        //        mBus.setImageResource(R.drawable.route_bus_normal);
        //        mWalk.setImageResource(R.drawable.route_walk_normal);
        //        mapView.setVisibility(View.GONE);
        //        mBusResultLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 开始搜索路径规划方案
     */
    private fun searchRouteResult(routeType: Int, mode: Int) {
        if (mStartPoint == null) {
            ToastUtil.show(this, "起点未设置")
            return
        }
        if (mEndPoint == null) {
            ToastUtil.show(this, "终点未设置")
        }
        showProgressDialog()
        val fromAndTo = RouteSearch.FromAndTo(mStartPoint, mEndPoint)
        when (routeType) {
            ROUTE_TYPE_BUS -> {// 公交路径规划
                val query = RouteSearch.BusRouteQuery(fromAndTo, mode,
                        mCurrentCityName, 0)// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
                mRouteSearch!!.calculateBusRouteAsyn(query)// 异步路径规划公交模式查询
            }
            ROUTE_TYPE_DRIVE -> {// 驾车路径规划
                val query = RouteSearch.DriveRouteQuery(fromAndTo, mode, null, null, "")// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
                mRouteSearch!!.calculateDriveRouteAsyn(query)// 异步路径规划驾车模式查询
            }
            ROUTE_TYPE_WALK -> {// 步行路径规划
                val query = RouteSearch.WalkRouteQuery(fromAndTo)
                mRouteSearch!!.calculateWalkRouteAsyn(query)// 异步路径规划步行模式查询
            }
            ROUTE_TYPE_CROSSTOWN -> {
                val fromAndTo_bus = RouteSearch.FromAndTo(
                        mStartPoint, mEndPoint)
                val query = RouteSearch.BusRouteQuery(fromAndTo_bus, mode,
                        "呼和浩特市", 0)// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
                query.cityd = "农安县"
                mRouteSearch!!.calculateBusRouteAsyn(query)// 异步路径规划公交模式查询
            }
        }
    }

    /**
     * 公交路线搜索结果方法回调
     */
    override fun onBusRouteSearched(result: BusRouteResult?, errorCode: Int) {
        dissmissProgressDialog()

        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.paths != null) {
                if (result.paths.size > 0) {
//                    val mBusResultListAdapter = BusAdapter(this, result)
//                    bus_result_list!!.adapter = mBusResultListAdapter
                } else if (result.paths == null) {
                    ToastUtil.show(this, R.string.no_result)
                }
            } else {
                ToastUtil.show(this, R.string.no_result)
            }
        } else {
            ToastUtil.showerror(this, errorCode)
        }
    }

    /**
     * 驾车路线搜索结果方法回调
     */
    override fun onDriveRouteSearched(result: DriveRouteResult?, errorCode: Int) {
        dissmissProgressDialog()
        aMap!!.clear()
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.paths != null) {
                if (result.paths.size > 0) {
                    val drivePath = result.paths[0] ?: return
                    val drivingRouteOverlay = DrivingRouteOverlay(this, aMap, drivePath,
                            result.startPos,
                            result.targetPos, null)
                    drivingRouteOverlay.setNodeIconVisibility(false)//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true)//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap()
                    drivingRouteOverlay.addToMap()
                    drivingRouteOverlay.zoomToSpan(480)
                    val dis = drivePath.distance.toInt()
                    val dur = drivePath.duration.toInt()
                    val des = AMapUtil.getFriendlyTime(dur) + "    " + AMapUtil.getFriendlyLength(dis)
                    val taxiCost = result.taxiCost.toInt()

                    Log.d(TAG, "dis : $dis  dur : $dur  des : $des")
                    setBottomSheetBehavior(des, "路线备注", drivePath, AmapNaviType.DRIVER)

                } else if (result.paths == null) {
                    ToastUtil.show(this, R.string.no_result)
                }

            } else {
                ToastUtil.show(this, R.string.no_result)
            }
        } else {
            ToastUtil.showerror(this, errorCode)
        }


    }

    /**
     * 骑行路线搜索结果方法回调
     */
    override fun onRideRouteSearched(arg0: RideRouteResult, arg1: Int) {
        // TODO Auto-generated method stub

    }

    /**
     * 步行路线搜索结果方法回调
     */
    override fun onWalkRouteSearched(result: WalkRouteResult?, errorCode: Int) {
        dissmissProgressDialog()
        aMap!!.clear()
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.paths != null) {
                if (result.paths.size > 0) {
                    val walkPath = result.paths[0] ?: return
                    val walkRouteOverlay = WalkRouteOverlay(this, aMap, walkPath,
                            result.startPos,
                            result.targetPos)
                    walkRouteOverlay.removeFromMap()
                    walkRouteOverlay.addToMap()
                    walkRouteOverlay.zoomToSpan(480)

                    val dis = walkPath.distance.toInt()
                    val dur = walkPath.duration.toInt()
                    val des = AMapUtil.getFriendlyTime(dur) + "    " + AMapUtil.getFriendlyLength(dis)


                    Log.d(TAG, "dis : $dis  dur : $dur  des : $des")
                    setBottomSheetBehavior(des, "路线备注", walkPath, AmapNaviType.WALK)

                } else if (result.paths == null) {
                    ToastUtil.show(this, R.string.no_result)
                }

            } else {
                ToastUtil.show(this, R.string.no_result)
            }
        } else {
            ToastUtil.showerror(this, errorCode)
        }
    }

    private fun <T> setBottomSheetBehavior(des: String, note: String, path: T, type: AmapNaviType) {
        firstline!!.text = des
        secondline!!.text = note

        if (path is WalkPath) {
            val mAdapter = SegmentAdapter(
                    this.applicationContext, AMapUtil.addHeardAndEndForWalkStep(path.steps))
            segment_list.adapter = mAdapter
        }
        if (path is DrivePath) {
            val mAdapter = SegmentAdapter(
                    this.applicationContext, AMapUtil.addHeardAndEndForDriveStep(path.steps))
            segment_list.adapter = mAdapter
        }

        bottomSheetBehavior = BottomSheetBehavior.from(bottom_layout)
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        val params: CoordinatorLayout.LayoutParams = bottom_layout.layoutParams as CoordinatorLayout.LayoutParams
        params.height = route_map.height + 60 + 92
        bottom_layout.layoutParams = params
        bottomSheetBehavior!!.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    detail.text = "显示地图"
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    detail.text = "路线详情"
                }
            }
        })
        start_rout.setOnClickListener {
            startNaviToShow(type)
        }

        showMyLocation()
    }

    private fun showMyLocation() {
        myLocationStyle!!.strokeColor(TOU)//边框颜色
        myLocationStyle!!.strokeWidth(0f)//宽度
        myLocationStyle!!.radiusFillColor(TOU)//填充颜色
        myLocationStyle!!.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.caricon))
        aMap!!.myLocationStyle = myLocationStyle!!

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

    override fun onClick(v: View) {
        when (v.id) {
            R.id.on_drive -> onDriveClick()
            R.id.on_Bus -> onBusClick()
            R.id.on_walk -> onWalkClick()
            R.id.detail -> {
                if (bottomSheetBehavior != null && bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
                } else if (bottomSheetBehavior != null && bottomSheetBehavior!!.state == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
    }

    private fun startNaviToShow(type: AmapNaviType) {
        val start = Poi("起点", AMapUtil.convertToLatLng(mStartPoint!!), "")
        val end = Poi("终点", AMapUtil.convertToLatLng(mEndPoint!!), "")
        val amapNaviParams = AmapNaviParams(start, null, end, type, AmapPageType.NAVI)
        amapNaviParams.setUseInnerVoice(true)
        AmapNaviPage.getInstance().showRouteActivity(applicationContext, amapNaviParams, this)
    }

    override fun onGetNavigationText(p0: String?) {
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
    }

    override fun onInitNaviFailure() {
        Log.e(TAG, "导航初始化失败")
    }

    override fun onStrategyChanged(p0: Int) {
    }

    override fun onReCalculateRoute(p0: Int) {
    }

    override fun getCustomNaviView(): View? {
        return null
    }

    override fun onCalculateRouteFailure(p0: Int) {

    }

    override fun onLocationChange(p0: AMapNaviLocation?) {
        Log.d(TAG, "导航中。。。 位置 ： ${p0!!.coord.toString()}")
    }

    override fun getCustomNaviBottomView(): View? {
        return null
    }

    override fun onArrivedWayPoint(p0: Int) {
    }

    override fun onArriveDestination(p0: Boolean) {
        Log.d(TAG, "导航 到达目的地 $p0")
    }

    override fun onStartNavi(p0: Int) {
        Log.d(TAG, "开始导航")
    }

    override fun onStopSpeaking() {
        //停止语音播报
    }

    override fun onExitPage(p0: Int) {
    }
}
