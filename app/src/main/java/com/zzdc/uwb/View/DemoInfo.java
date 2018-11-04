package com.zzdc.uwb.View;

import android.app.Activity;

public class DemoInfo {
    public final int mTitle;
    public final String title;
    public final String msg;
    public final double location[];
    public final Class<? extends Activity> mClazz;

    public DemoInfo(int mTitle, String title, String msg, double location[],
                    Class<? extends Activity> clazz) {
        this.mTitle = mTitle;
        this.title = title;
        this.msg = msg;
        this.location = location;
        this.mClazz = clazz;
    }
}
