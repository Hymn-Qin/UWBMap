package com.zzdc.uwb.FMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.fengmap.android.FMDevice;
import com.fengmap.android.map.FMViewMode;
import com.fengmap.android.map.animator.FMLinearInterpolator;
import com.fengmap.android.map.event.OnFMCompassListener;
import com.fengmap.android.map.event.OnFMSwitchGroupListener;
import com.fengmap.android.map.layer.FMLayer;
import com.fengmap.android.widget.FM3DControllerButton;
import com.fengmap.android.widget.FMFloorControllerComponent;
import com.fengmap.android.widget.FMMultiFloorControllerButton;
import com.fengmap.android.widget.FMZoomComponent;
import com.zzdc.uwb.BasicActivity;
import com.zzdc.uwb.Utils.FileUtils;
import com.zzdc.uwb.Utils.ViewHelper;
import com.fengmap.android.FMErrorMsg;
import com.fengmap.android.data.OnFMDownloadProgressListener;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapUpgradeInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.zzdc.uwb.R;
import com.zzdc.uwb.View.Widget.NavigationBar;

import java.util.ArrayList;
import java.util.List;

public class FMapStartActivity extends FMapBasicActivity {

    private static final String TAG = FMapStartActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmap_start);

        initView();
    }

    @Override
    public void initView() {
        super.initView();
        //        Intent intent = getIntent();
//        String title = intent.getStringExtra("title");
//        Log.d(TAG, "title is " + title);
//        NavigationBar navigationBar = ViewHelper.getView(FMapStartActivity.this, R.id.navigation_bar_title);
//        navigationBar.setTitle(title);
        setMLayerIds(new int[]{R.id.cb_layers_label, R.id.cb_layers_facility});
        String info = getResources().getString(R.string.map_init_tips, getGroupId(), getCENTER_COORD().x,
                getCENTER_COORD().y, getRotate(), getTilt(), getLevel());
        setMMapView((FMMapView) findViewById(R.id.map_view));
        setMMap(getMMapView().getFMMap());

        Log.d(TAG, "msg is " + info);
//        TextView textView = ViewHelper.getView(FMapStartActivity.this, R.id.map_result);
//        textView.setText(info);

//        RelativeLayout right = ViewHelper.getView(FMapStartActivity.this, R.id.fm_mp_mun);
//        drawerLayout = ViewHelper.getView(FMapStartActivity.this, R.id.fm_mp);
//        drawerLayout.openDrawer(right);
        initData();
    }
}
