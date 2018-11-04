package com.zzdc.uwb.AMap

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.help.Tip
import com.zzdc.uwb.AppConfig.UWBConfig.END_POINT
import com.zzdc.uwb.FMap.FMapStartActivity
import com.zzdc.uwb.R
import com.zzdc.uwb.View.DemoInfo
import com.zzdc.uwb.View.DestinationAdapter
import kotlinx.android.synthetic.main.activity_amap_input.*

/**
 * 输入提示功能实现
 */
class AMapInputActivity : Activity(), TextWatcher {

    private val TAG = AMapInputActivity::class.java.simpleName

    private val mDemos = arrayOf(
            DemoInfo(R.string.demo_title_main_init, "洗手间（研发中心）", "D区-D12-F2-ZZDC-洗手间", doubleArrayOf(34.55695, 113.84333, 0.0, 0.0), FMapStartActivity::class.java),
            DemoInfo(R.string.demo_title_main_init, "2#会议室（研发中心）", "D区-D12-F2-ZZDC-2#会议室", doubleArrayOf(34.55695, 113.84333, 0.0, 0.0), FMapStartActivity::class.java),
            DemoInfo(R.string.demo_title_main_init, "茶水间（研发中心）", "D区-D12-F2-ZZDC-茶水间", doubleArrayOf(34.55695, 113.84333, 0.0, 0.0), FMapStartActivity::class.java),
            DemoInfo(R.string.demo_title_main_init, "视讯会议室（研发中心）", "D区-D12-F2-ZZDC-视讯会议室", doubleArrayOf(34.55695, 113.84333, 0.0, 0.0), FMapStartActivity::class.java),
            DemoInfo(R.string.demo_title_main_init, "实验室（研发中心）", "D区-D12-F2-ZZDC-实验室", doubleArrayOf(34.55695, 113.84333, 0.0, 0.0), FMapStartActivity::class.java),
            DemoInfo(R.string.demo_title_main_init, "1#会议室（研发中心）", "D区-D12-F2-ZZDC-1#会议室", doubleArrayOf(34.55695, 113.84333, 0.0, 0.0), FMapStartActivity::class.java),
            DemoInfo(R.string.demo_title_main_init, "休息室（研发中心）", "D区-D12-F2-ZZDC-休息室", doubleArrayOf(34.55695, 113.84333, 0.0, 0.0), FMapStartActivity::class.java))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amap_input)

        input_edittext.addTextChangedListener(this)

        onGetInputtips(null, 0)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                   after: Int) {
        // TODO Auto-generated method stub

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        //		String newText = s.toString().trim();
        //        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        //        inputquery.setCityLimit(true);
        //        Inputtips inputTips = new Inputtips(AMapInputActivity.this, inputquery);
        //        inputTips.setInputtipsListener(this);
        //        inputTips.requestInputtipsAsyn();


    }

    override fun afterTextChanged(s: Editable) {
        // TODO Auto-generated method stub

    }

    private fun onGetInputtips(tipList: List<Tip>?, rCode: Int) {
        val aAdapter = DestinationAdapter(applicationContext, mDemos)
        inputlist.adapter = aAdapter
        aAdapter.notifyDataSetChanged()
        inputlist.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            END_POINT = LatLonPoint(mDemos[position].location[0], mDemos[position].location[1])
            Log.d(TAG, "选择了目的地 ： ${mDemos[position].title}  ${mDemos[position].msg}")
            val intent = Intent(this@AMapInputActivity, AMapRouteActivity::class.java)
            startActivity(intent)

        }

        //        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
        //            List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
        //            if(tipList != null) {
        //            	int size = tipList.size();
        //				for (int i = 0; i < size; i++) {
        //					Tip tip = tipList.get(i);
        //					if(tip != null) {
        //						HashMap<String, String> map = new HashMap<String, String>();
        //						map.put("name", tipList.get(i).getName());
        //						map.put("address", tipList.get(i).getDistrict());
        //						listString.add(map);
        //					}
        //				}
        //				SimpleAdapter aAdapter = new SimpleAdapter(getApplicationContext(), listString, R.layout.item_layout,
        //						new String[]{"name", "address"}, new int[]{R.id.poi_field_id, R.id.poi_value_id});
        //				minputlist.setAdapter(aAdapter);
        //				aAdapter.notifyDataSetChanged();
        //			}
        //
        //        } else {
        //			ToastUtil.showerror(this.getApplicationContext(), rCode);
        //		}

    }

}
