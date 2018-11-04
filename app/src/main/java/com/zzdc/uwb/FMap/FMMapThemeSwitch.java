package com.zzdc.uwb.FMap;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.zzdc.uwb.Utils.FileUtils;
import com.zzdc.uwb.Utils.ViewHelper;
import com.fengmap.android.FMErrorMsg;
import com.fengmap.android.FMMapSDK;
import com.fengmap.android.data.OnFMDownloadProgressListener;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapUpgradeInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.zzdc.uwb.R;

/**
 * 主题切换
 * <p>在地图{@link OnFMMapInitListener#onMapInitSuccess(String)}地图加载完成后，即可调用主题切
 * 换功能,Fengmap地图主题默认路径为sdcard/fengmap/theme,可以通过{@link FMMapSDK#init(Application, String)}
 * 设置Fengmap地图主题缓存路径
 *
 * @author hezutao@fengmap.com
 * @version 2.0.0
 */
public class FMMapThemeSwitch extends Activity implements OnFMMapInitListener,
    CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private FMMap mMap;
    private View mLocal1Btn;
    private View mLocal2Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmap_theme);

        initView();
        openMapByPath();
    }

    private void initView() {
        ViewHelper.setViewCheckedChangeListener(FMMapThemeSwitch.this, R.id.btn_switch, this);
        mLocal1Btn = ViewHelper.getView(FMMapThemeSwitch.this, R.id.btn_local_theme1);
        mLocal1Btn.setOnClickListener(this);

        mLocal2Btn = ViewHelper.getView(FMMapThemeSwitch.this, R.id.btn_local_theme2);
        mLocal2Btn.setOnClickListener(this);
    }

    /**
     * 加载地图数据
     */
    private void openMapByPath() {
        FMMapView mapView = (FMMapView) findViewById(R.id.map_view);
        mMap = mapView.getFMMap();
        mMap.setOnFMMapInitListener(this);
        //加载离线数据
        String path = FileUtils.getDefaultMapPath(this);
        mMap.openMapByPath(path);
    }

    /**
     * 地图加载成功回调事件
     *
     * @param path 地图所在sdcard路径
     */
    @Override
    public void onMapInitSuccess(String path) {
        //TODO 可以在地图加载成功设置地图初始状态，初始化地图所需资源
    }

    /**
     * 地图加载失败回调事件
     *
     * @param path      地图所在sdcard路径
     * @param errorCode 失败加载错误码，可以通过{@link FMErrorMsg#getErrorMsg(int)}获取加载地图失败详情
     */
    @Override
    public void onMapInitFailure(String path, int errorCode) {
        //TODO 可以提示用户地图加载失败原因，进行地图加载失败处理
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
    @Override
    public boolean onUpgrade(FMMapUpgradeInfo upgradeInfo) {
        //TODO 获取到最新地图更新的信息，可以进行地图的下载操作
        return false;
    }

    /**
     * 地图销毁调用
     */
    @Override
    public void onBackPressed() {
        if (mMap != null) {
            mMap.onDestroy();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_local_theme1:
                loadLocalTheme1();
                switchStatus(false);
                break;
            case R.id.btn_local_theme2:
                loadNetTheme2();
                switchStatus(true);
                break;
            default:
                break;
        }
    }

    /**
     * 加载本地主题2001
     */
    private void loadLocalTheme1() {
        String themeId = "2001";
        String path = FileUtils.getThemePath(this, themeId);
        mMap.loadThemeByPath(path);
    }

    /**
     * 加载本地主题2002
     */
    private void loadNetTheme2() {
        String themeId = "2002";
        String path = FileUtils.getThemePath(this, themeId);
        mMap.loadThemeByPath(path);
    }

    /**
     * 切换主题状态
     *
     * @param enable 是否可用
     */
    private void switchStatus(boolean enable) {
        mLocal1Btn.setEnabled(enable);
        mLocal2Btn.setEnabled(!enable);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int visibility = isChecked ? View.VISIBLE : View.GONE;
        ViewHelper.setViewVisibility(FMMapThemeSwitch.this, R.id.layout_theme, visibility);
    }
}
