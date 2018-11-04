package com.zzdc.uwb.AMap

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.services.route.DriveStep
import com.amap.api.services.route.WalkStep
import com.zzdc.uwb.R

/**
 * 驾车路线详情页adapter
 *
 * @author ligen
 */
class SegmentAdapter<T>(private val mContext: Context, var mItemList: List<T>) : BaseAdapter() {

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return mItemList.size
    }

    override fun getItem(position: Int): T {
        // TODO Auto-generated method stub
        return mItemList[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // TODO Auto-generated method stub
        var holder: ViewHolder? = null
        if (convertView == null) {
            holder = ViewHolder()
            convertView = View.inflate(mContext, R.layout.item_segment, null)
            holder.dirIcon = convertView!!
                    .findViewById<View>(R.id.bus_dir_icon) as ImageView
            holder.lineName = convertView
                    .findViewById<View>(R.id.bus_line_name) as TextView
            holder.dirUp = convertView
                    .findViewById<View>(R.id.bus_dir_icon_up) as ImageView
            holder.dirDown = convertView
                    .findViewById<View>(R.id.bus_dir_icon_down) as ImageView
            holder.splitLine = convertView
                    .findViewById<View>(R.id.bus_seg_split_line) as ImageView
            convertView.tag = holder
        } else {
            holder = convertView.tag as SegmentAdapter<T>.ViewHolder
        }
        val item = mItemList[position]
        when (position) {
            0 -> {
                holder.dirIcon!!.setImageResource(R.drawable.dir_start)
                holder.lineName!!.text = "出发"
                holder.dirUp!!.visibility = View.GONE
                holder.dirDown!!.visibility = View.VISIBLE
                holder.splitLine!!.visibility = View.GONE
                return convertView
            }
            mItemList.size - 1 -> {
                holder.dirIcon!!.setImageResource(R.drawable.dir_end)
                holder.lineName!!.text = "到达终点"
                holder.dirUp!!.visibility = View.VISIBLE
                holder.dirDown!!.visibility = View.GONE
                holder.splitLine!!.visibility = View.VISIBLE
                return convertView
            }
            else -> {
                var actionName: String?
                var resID: Int?
                if (item is DriveStep) {
                    actionName = item.action
                    resID = AMapUtil.getDriveActionID(actionName)
                    holder.dirIcon!!.setImageResource(resID)
                    holder.lineName!!.text = item.instruction
                }
                if (item is WalkStep) {
                    actionName = item.action
                    resID = AMapUtil.getWalkActionID(actionName)
                    holder.dirIcon!!.setImageResource(resID)
                    holder.lineName!!.text = item.instruction
                }
                holder.dirUp!!.visibility = View.VISIBLE
                holder.dirDown!!.visibility = View.VISIBLE
                holder.splitLine!!.visibility = View.VISIBLE
                return convertView
            }
        }

    }

    private inner class ViewHolder {
        internal var lineName: TextView? = null
        internal var dirIcon: ImageView? = null
        internal var dirUp: ImageView? = null
        internal var dirDown: ImageView? = null
        internal var splitLine: ImageView? = null
    }

}
