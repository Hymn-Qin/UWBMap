package com.zzdc.uwb.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zzdc.uwb.R;

public class DestinationAdapter extends BaseAdapter {

    DemoInfo[] mDemos;
    Context context;
    public DestinationAdapter(Context context, DemoInfo[] demoInfos) {
        this.context = context;
        this.mDemos = demoInfos;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_demo_info, null);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView msg = convertView.findViewById(R.id.msg);

        title.setText(mDemos[index].title);
        msg.setText(mDemos[index].msg);
        return convertView;
    }

    @Override
    public int getCount() {
        return mDemos.length;
    }

    @Override
    public Object getItem(int index) {
        return mDemos[index];
    }

    @Override
    public long getItemId(int id) {
        return id;
    }
}
