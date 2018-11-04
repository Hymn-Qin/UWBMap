package com.zzdc.uwb

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

import com.fengmap.android.FMMapSDK

/**
 * 应用层初始化
 *
 * @author qxj
 * @version
 */
class AppApplication : Application(), Application.ActivityLifecycleCallbacks {

    private val TAG = AppApplication::class.java.simpleName

    private var mActivityCount = 0

    companion object {
        lateinit var instance: AppApplication
            private set
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(this)
    }

    override fun onTerminate() {
        unregisterActivityLifecycleCallbacks(this)
        super.onTerminate()
    }

    override fun onActivityPaused(activity: Activity?) {
        Log.d(TAG, "onActivityPaused")
    }

    override fun onActivityResumed(activity: Activity?) {
        Log.d(TAG, "onActivityResumed")
    }

    override fun onActivityStarted(activity: Activity?) {
        Log.d(TAG, "onActivityStarted")
        mActivityCount ++
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Log.d(TAG, "onActivityDestroyed")
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Log.d(TAG, "onActivitySaveInstanceState")
    }

    override fun onActivityStopped(activity: Activity?) {
        Log.d(TAG, "onActivityStopped")
        mActivityCount --
        Log.d(TAG, "now Activity count = $mActivityCount")
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated")
    }
    open fun getCount(): Int {
        return mActivityCount
    }
}