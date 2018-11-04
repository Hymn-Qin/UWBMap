package com.zzdc.uwb.AMap

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.zzdc.uwb.R
import kotlinx.android.synthetic.main.activity_amap_start.*

class AMapStartActivity : AMapBasicActivity(), AMap.OnMyLocationChangeListener, View.OnClickListener {

    private val TAG = AMapStartActivity::class.java.simpleName

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amap_start)
        //设置默认位置
        val centerBJPoint = LatLng(super.LHG, super.LAT)
        val mapOptions = AMapOptions()
        mapOptions.camera(CameraPosition(centerBJPoint, 10f, 0f, 0f))
        mapView = MapView(this, mapOptions)
        amap_view.addView(mapView)
        mapView!!.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {
        aMap = mapView!!.map
        aMap!!.mapType = AMap.MAP_TYPE_NORMAL
        mUiSettings = aMap!!.uiSettings
        initData()
    }

    override fun initData() {
        super.initData()
        input_edit_text.setOnClickListener(this)
        start_destination_set.setOnClickListener(this)
    }


    override fun LocationSuccess(p0: Location?) {
        super.LocationSuccess(p0)
        Log.d(TAG, "定位成功")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.start_destination_set -> {
                val intent = Intent(this, AMapRouteActivity::class.java)
                this.startActivity(intent)
            }
            R.id.input_edit_text -> {
                val intent = Intent(this, AMapInputActivity::class.java)
                this.startActivity(intent)
            }
        }
    }
}