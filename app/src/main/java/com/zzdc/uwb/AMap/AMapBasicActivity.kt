package com.zzdc.uwb.AMap

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.Window
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.*
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.hankcs.hanlp.HanLP
import com.zzdc.uwb.AppApplication
import com.zzdc.uwb.BasicActivity
import com.zzdc.uwb.R
import java.text.SimpleDateFormat
import java.util.*
/**
 * @author qxj
 * @version 3DMap 6.5.0  Navi 6.4.0  AMapLocation 4.3.0
 */
open class AMapBasicActivity : BasicActivity(),
        AMap.OnMyLocationChangeListener,
        AMap.OnCameraChangeListener,
        AMap.OnMapTouchListener,
        LocationSource {

    private val TAG = AMapBasicActivity::class.java.simpleName
    open var mapView: MapView? = null//高德地图容器
    open var aMap: AMap? = null//地图控制器
    open var myLocationStyle: MyLocationStyle? = null
    open var mUiSettings: UiSettings? = null
    val STROKE_COLOR = Color.argb(150, 3, 145, 255)
    val FILL_COLOR = Color.argb(18, 0, 0, 170)

    val TOU = Color.argb(0, 3, 145, 255)
    open var first = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    open fun initView() {

    }

    open fun initData() {
        StartLocation()
    }

    override fun onResume() {
        super.onResume()
        mapView!!.onResume()

    }

    override fun onStart() {
        super.onStart()
    }

    open fun onResumeToDo() {

    }

    override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }


    fun StartLocation() {
        myLocationStyle = MyLocationStyle()//定位蓝点样式
        myLocationStyle!!.interval(10000)
        myLocationStyle!!.showMyLocation(true)
        myLocationStyle!!.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)//定位一次，且将视角移动到地图中心点)
        myLocationStyle!!.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked))//定位蓝点图标
        myLocationStyle!!.anchor(0.5.toFloat(), 0.5.toFloat())
        myLocationStyle!!.strokeColor(STROKE_COLOR)//边框颜色
        myLocationStyle!!.strokeWidth(1f)//宽度
        myLocationStyle!!.radiusFillColor(FILL_COLOR)//填充颜色

        aMap!!.myLocationStyle = myLocationStyle

        val mCameraUpdate = CameraUpdateFactory.zoomTo(15f)
        aMap!!.moveCamera(mCameraUpdate)//默认显示范围
        mUiSettings!!.setMyLocationButtonEnabled(false)//定位按钮
        mUiSettings!!.setZoomControlsEnabled(false)//放大缩放
        //        mUiSettings.setCompassEnabled(true);//指南针
        mUiSettings!!.setScaleControlsEnabled(true)//比例尺
        //手势
        mUiSettings!!.setAllGesturesEnabled(true)

        aMap!!.isMyLocationEnabled = true
        aMap!!.setOnMyLocationChangeListener(this)
        aMap!!.setOnCameraChangeListener(this)// 对amap添加移动地图事件监听器
        aMap!!.setOnMapTouchListener(this)

    }


    override fun deactivate() {

    }

    override fun activate(p0: LocationSource.OnLocationChangedListener?) {

    }

    override fun onTouch(p0: MotionEvent?) {
        //地图手势
    }

    override fun onCameraChangeFinish(p0: CameraPosition?) {
        val visibleRegion = aMap!!.projection.visibleRegion // 获取可视区域、

        val latLngBounds = visibleRegion.latLngBounds// 获取可视区域的Bounds
        if (first) {
            val user = LatLng(super.LHG, super.LAT)
            val isContain = latLngBounds.contains(user)// 判断上海经纬度是否包括在当前地图可见区域
            if (isContain) {
                //用户在地图当前可见区域内
                onCameraChangeFinishInScreen()
            } else {
                onCameraChangeFinishOutScreen()
            }
        }
    }

    open fun onCameraChangeFinishInScreen() {

    }

    open fun onCameraChangeFinishOutScreen() {
        aMap!!.setLocationSource(this)
    }

    override fun onCameraChange(p0: CameraPosition?) {

    }

    override fun onMyLocationChange(p0: Location?) {
        if (p0 != null) {
            val bundle = p0.getExtras()
            if (bundle != null) {
                val errorCode = bundle!!.getInt(MyLocationStyle.ERROR_CODE)
                val errorInfo = bundle!!.getString(MyLocationStyle.ERROR_INFO)
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                val locationType = bundle!!.getInt(MyLocationStyle.LOCATION_TYPE)
                if (errorCode == 0) {
                    if (!first) {
                        first = true
                        aMap!!.myLocationStyle = myLocationStyle!!.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
                    }
                    Log.e(TAG, "onMyLocationChange 定位成功")
                    super.LHG = p0.latitude//获取纬度
                    super.LAT = p0.longitude//获取经度
                    Log.d(TAG, "定位" + super.LHG + ":" + super.LAT)
                    LocationSuccess(p0)

                } else {
                    Log.e(TAG, "定位错误， code: $errorCode errorInfo: $errorInfo locationType: $locationType")
                    LocationFailed()
                }
            } else {
                Log.e(TAG, "定位信息， bundle is null ")
            }

        } else {
            Log.e(TAG, "定位失败")
        }
    }

    open fun LocationSuccess(p0: Location?) {

    }

    open fun LocationFailed() {
        if (AppApplication.instance.getCount() == 0) {
            StartBackgroundLocation()
        }
    }

    fun StartBackgroundLocation() {
        val mLocationListener: AMapLocationListener = AMapLocationListener { aMapLocation ->
            if (aMapLocation != null) {
                if (aMapLocation.errorCode == 0) {
                    //解析amapLocation获取相应内容。
                    val LHG = aMapLocation.latitude//获取纬度
                    val LAT = aMapLocation.longitude//获取经度
                    val ADDRESS = HanLP.convertToTraditionalChinese(aMapLocation.address)//地址，
                    //                        String address = HanLP.convertToTraditionalChinese(aMapLocation.getAddress());
                    //                        float a = aMapLocation.getAccuracy();//获取精度信息
                    //获取定位时间
                    @SuppressLint("SimpleDateFormat")
                    val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    val date = Date(aMapLocation.time)


                } else {

                }
            }
        }
        //初始化定位
        val mLocationClient = AMapLocationClient(applicationContext)
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)

        //初始化AMapLocationClientOption对象
        val mLocationOption = AMapLocationClientOption()
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Battery_Saving

        mLocationOption.isOnceLocationLatest = true
        //启动周期定位 最低1000ms。
        mLocationOption.interval = 1000000
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.isNeedAddress = true

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.enableBackgroundLocation(2001, buildNotification())
        //启动定位
        mLocationClient.startLocation()
    }

    val NOTIFICATION_CHANNEL_NAME = "BackgroundLocation"
    var notificationManager: NotificationManager? = null
    var isCreateChannel = false
    var builder: Notification.Builder? = null
    var notification: Notification? = null
    @SuppressLint("NewApi")
    private fun buildNotification(): Notification? {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            val channelId = packageName
            if (!isCreateChannel) {
                val notificationChannel = NotificationChannel(channelId,
                        NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
                notificationChannel.enableLights(true)//是否在桌面icon右上角展示小圆点
                notificationChannel.lightColor = Color.BLUE //小圆点颜色
                notificationChannel.setShowBadge(true) //是否在久按桌面图标时显示此渠道的通知
                notificationManager!!.createNotificationChannel(notificationChannel)
                isCreateChannel = true
            }
            builder = Notification.Builder(applicationContext, channelId)
        } else {
            builder = Notification.Builder(applicationContext)
        }
        builder!!.setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("UWB")
                .setContentText("正在后台定位")
                .setWhen(System.currentTimeMillis())

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder!!.build()
        } else {
            return builder!!.notification
        }
        return notification
    }
}
