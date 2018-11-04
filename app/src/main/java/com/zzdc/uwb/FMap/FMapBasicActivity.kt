package com.zzdc.uwb.FMap

import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.CheckBox
import android.widget.CompoundButton
import com.fengmap.android.FMDevice
import com.fengmap.android.map.FMMap
import com.fengmap.android.map.FMMapUpgradeInfo
import com.fengmap.android.map.FMMapView
import com.fengmap.android.map.FMViewMode
import com.fengmap.android.map.animator.FMLinearInterpolator
import com.fengmap.android.map.event.OnFMCompassListener
import com.fengmap.android.map.event.OnFMMapInitListener
import com.fengmap.android.map.event.OnFMSwitchGroupListener
import com.fengmap.android.map.geometry.FMMapCoord
import com.fengmap.android.map.layer.FMLayer
import com.fengmap.android.widget.FM3DControllerButton
import com.fengmap.android.widget.FMFloorControllerComponent
import com.fengmap.android.widget.FMMultiFloorControllerButton
import com.fengmap.android.widget.FMZoomComponent
import com.zzdc.uwb.BasicActivity
import com.zzdc.uwb.R
import com.zzdc.uwb.Utils.FileUtils
import com.zzdc.uwb.Utils.ViewHelper

/**
 * @author qxj
 * @version  FM 2.1.3
 */
open class FMapBasicActivity : BasicActivity(),
        OnFMMapInitListener,
        OnFMSwitchGroupListener,
        OnFMCompassListener,
        CompoundButton.OnCheckedChangeListener {

    private val TAG = FMapBasicActivity::class.java.simpleName

    val CENTER_COORD = FMMapCoord(1.296164E7, 4861800.0)
    var mMap: FMMap? = null
    val Level = 20
    val Rotate = 60f
    val Tilt = 45f
    val GroupId = 1

    var mTextBtn: FM3DControllerButton? = null//3D2D
    var mMapView: FMMapView? = null

    var mZoomComponent: FMZoomComponent? = null//缩放

    var mMultiFloorBtn: FMMultiFloorControllerButton? = null//多层0

    var mFloorComponent: FMFloorControllerComponent? = null//多层1

    var mLayerIds: IntArray? = null

    private var mAnimateEnded = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    open fun initView() {

    }

    open fun initData() {
        openMapByPath()
    }

    /**
     * 加载地图数据
     */
    private fun openMapByPath() {
        mMap!!.setOnFMMapInitListener(this)
        //设置指北针点击事件
        mMap!!.onFMCompassListener = this
        //加载离线数据
        val path = FileUtils.getDefaultMapPath(this)
        mMap!!.openMapByPath(path)
        //加载在线地图数据，并自动更新地图数据
        //        mMap.openMapById(id,true);
        //         mMap.openMapById(FileUtils.DEFAULT_MAP_ID,true);


    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()
    }

    open fun onResumeToDo() {

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    /**
     * 地图销毁调用
     */
    override fun onBackPressed() {
        mMap!!.onDestroy()
        super.onBackPressed()
    }

    override fun onCompassClick() {
        //恢复为初始状态
        mMap!!.resetCompassToNorth()
    }

    override fun onMapInitSuccess(path: String) {
        //加载离线主题
        mMap!!.loadThemeByPath(FileUtils.getDefaultThemePath(this))
        //加载在线主题文件
        //        mMap.loadThemeById(FMMap.DEFAULT_THEME_CANDY);
        //2D显示模式
        //        mMap.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
        //缩放级别
        mMap!!.setZoomLevel(Level.toFloat(), false)
        //旋转角度
        mMap!!.rotateAngle = Rotate
        //倾角
        mMap!!.tiltAngle = Tilt
        //地图中心点
        mMap!!.mapCenter = CENTER_COORD

        //初始化2D 3D
        init3DControllerComponent()

        if (mZoomComponent == null) {
            initZoomComponent()
        }
        //多层
        if (mFloorComponent == null) {
            initFloorControllerComponent()
        }
        //图层标注显示
        val groupId = mMap!!.focusGroupId
        val layers = arrayOfNulls<FMLayer>(2)
        //获取标注图层
        layers[0] = mMap!!.fmLayerProxy.getFMLabelLayer(groupId)
        //获取设施图层
        layers[1] = mMap!!.fmLayerProxy.getFMFacilityLayer(groupId)

        for (i in mLayerIds!!.indices) {
            val checkBox = ViewHelper.getView<CheckBox>(this, mLayerIds!![i])
            checkBox.tag = layers[i]
            checkBox.setOnCheckedChangeListener(this)
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val layer = buttonView!!.tag as FMLayer
        //设置控件是否显示true、隐藏false
        layer.isVisible = isChecked
        mMap!!.updateMap()
    }

    /**
     * 地图加载失败回调事件
     *
     * @param path      地图所在sdcard路径
     * @param errorCode 失败加载错误码，可以通过{@link FMErrorMsg#getErrorMsg(int)}获取加载地图失败详情
     */
    override fun onMapInitFailure(p0: String?, p1: Int) {

    }

    /**
     * 当{@link FMMap#openMapById(String, boolean)}设置openMapById(String, false)时地图不自动更新会
     * 回调此事件，可以调用{@link FMMap#upgrade(FMMapUpgradeInfo, OnFMDownloadProgressListener)}进行
     * 地图下载更新
     *
     * @param upgradeInfo 地图版本更新详情,地图版本号{@link FMMapUpgradeInfo#getVersion()},<br/>
     *                    地图id{@link FMMapUpgradeInfo#getMapId()}
     * @return 如果调用了{@link FMMap#upgrade(FMMapUpgradeInfo, OnFMDownloadProgressListener)}地图下载更新，
     * 返回值return true,因为{@link FMMap#upgrade(FMMapUpgradeInfo, OnFMDownloadProgressListener)}
     * 会自动下载更新地图，更新完成后会加载地图;否则return false。
     */

    override fun onUpgrade(p0: FMMapUpgradeInfo?): Boolean {
        //TODO 获取到最新地图更新的信息，可以进行地图的下载操作
        return false
    }


    /**
     * 加载2d/3d切换控件
     */
    private fun init3DControllerComponent() {
        mTextBtn = FM3DControllerButton(this)
        //设置初始状态为3D(true),设置为false为2D模式
        mTextBtn!!.initState(true)
        mTextBtn!!.measure(0, 0)
        val width = mTextBtn!!.measuredWidth
        //设置3D控件位置
        mMapView!!.addComponent(mTextBtn, FMDevice.getDeviceWidth() - 10 - width, 50)
        //2、3D点击监听
        mTextBtn!!.setOnClickListener{ v ->
            val button = v as FM3DControllerButton
            if (button.isSelected) {
                button.isSelected = false
                mMap!!.setFMViewMode(FMViewMode.FMVIEW_MODE_2D)
            } else {
                button.isSelected = true
                mMap!!.setFMViewMode(FMViewMode.FMVIEW_MODE_3D)
            }
        }
    }

    /**
     * 初始化缩放控件
     */
    private fun initZoomComponent() {
        mZoomComponent = FMZoomComponent(this)
        mZoomComponent!!.measure(0, 0)
        val width = mZoomComponent!!.measuredWidth
        val height = mZoomComponent!!.measuredHeight
        //缩放控件位置
        val offsetX = FMDevice.getDeviceWidth() - width - 10
        val offsetY = FMDevice.getDeviceHeight() - 400 - height
        mMapView!!.addComponent(mZoomComponent, offsetX, offsetY)

        mZoomComponent!!.setOnFMZoomComponentListener(object : FMZoomComponent.OnFMZoomComponentListener {
            override fun onZoomIn(view: View) {
                //地图放大
                mMap!!.zoomIn()
            }

            override fun onZoomOut(view: View) {
                //地图缩小
                mMap!!.zoomOut()
            }
        })
    }

    /**
     * 楼层切换控件初始化
     */
    private fun initFloorControllerComponent() {
        // 楼层切换
        mFloorComponent = FMFloorControllerComponent(this)
        mFloorComponent!!.setMaxItemCount(4)
        //楼层切换事件监听
        mFloorComponent!!.setOnFMFloorControllerComponentListener(object : FMFloorControllerComponent.OnFMFloorControllerComponentListener {
            override fun onSwitchFloorMode(view: View, currentMode: FMFloorControllerComponent.FMFloorMode) {
                if (currentMode == FMFloorControllerComponent.FMFloorMode.SINGLE) {
                    setSingleDisplay()
                } else {
                    setMultiDisplay()
                }
            }

            override fun onItemSelected(groupId: Int, floorName: String): Boolean {
                if (mAnimateEnded) {
                    switchFloor(groupId)
                    return true
                }
                return false
            }
        })
        //设置为单层模式
        mFloorComponent!!.setFloorMode(FMFloorControllerComponent.FMFloorMode.SINGLE)
        val groupId = 1
        mFloorComponent!!.setFloorDataFromFMMapInfo(mMap!!.fmMapInfo, groupId)
        val offsetX = (FMDevice.getDeviceDensity() * 5).toInt()
        val offsetY = (FMDevice.getDeviceDensity() * 330).toInt()
        mMapView!!.addComponent(mFloorComponent, offsetX, offsetY)
    }

    /**
     * 切换楼层
     *
     * @param groupId 楼层id
     */
    fun switchFloor(groupId: Int) {
        mMap!!.setFocusByGroupIdAnimated(groupId, FMLinearInterpolator(), this)
    }

    /**
     * 组切换结束之后。
     */
    override fun afterGroupChanged() {
        mAnimateEnded = true
    }

    /**
     * 组切换开始之前。
     */
    override fun beforeGroupChanged() {
        mAnimateEnded = false
    }

    /**
     * 单层显示
     */
    fun setSingleDisplay() {
        val gids = intArrayOf(mMap!!.focusGroupId)       //获取当前地图焦点层id
        mMap!!.setMultiDisplay(gids, 0, this)
    }

    /**
     * 多层显示
     */
    fun setMultiDisplay() {
        val gids = mMap!!.mapGroupIds    //获取地图所有的group
        val fd = mFloorComponent!!.getFloorData(mFloorComponent!!.selectedPosition)
        var focus = 0
        for (i in gids.indices) {
            if (gids[i] == fd.groupId) {
                focus = i
                break
            }
        }
        mMap!!.setMultiDisplay(gids, focus, this)
    }
}