package com.zzdc.uwb.AppConfig

import com.amap.api.services.core.LatLonPoint
import com.zzdc.uwb.Utils.ConfigPreference

object UWBConfig {

    var default_city by ConfigPreference("DEFAULT_CITY", "郑州")
    var user_lat by ConfigPreference("LAT", 113.658075.toLong())//默认经度
    var user_lng by ConfigPreference("LNG", 34.745793.toLong())//默认纬度

    var START_POINT = LatLonPoint(34.558488, 113.843754)//起点，116.335891,39.942295
    var END_POINT = LatLonPoint(34.55695, 113.84333)//终点，116.481288,39.995576
}