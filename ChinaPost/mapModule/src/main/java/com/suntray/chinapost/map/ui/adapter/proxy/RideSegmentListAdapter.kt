package com.suntray.chinapost.map.ui.adapter.proxy

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.services.route.RideStep
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.utils.AMapUtil

import java.util.ArrayList

/**
 * 骑行路线详情页adapter
 *
 */
class RideSegmentListAdapter(private val mContext: Context,
                             steps: List<RideStep>) : BaseAdapter() {
    private val mItemList = ArrayList<RideStep>()

    init {
        mItemList.add(RideStep())
        for (rideStep in steps) {
            mItemList.add(rideStep)
        }
        mItemList.add(RideStep())
    }

    override fun getCount(): Int {
        return mItemList.size
    }

    override fun getItem(position: Int): Any {
        return mItemList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder? =
                null
        if (convertView == null) {
            holder = ViewHolder()
            convertView = View.inflate(mContext, R.layout.item_bus_segment, null)
            holder.lineName = convertView!!
                    .findViewById(R.id.bus_line_name) as TextView
            holder.dirIcon = convertView
                    .findViewById(R.id.bus_dir_icon) as ImageView
            holder.dirUp = convertView
                    .findViewById(R.id.bus_dir_icon_up) as ImageView
            holder.dirDown = convertView
                    .findViewById(R.id.bus_dir_icon_down) as ImageView
            holder.splitLine = convertView
                    .findViewById(R.id.bus_seg_split_line) as ImageView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val item = mItemList[position]
        if (position == 0) {
            holder.dirIcon!!.setImageResource(R.drawable.dir_start)
            holder.lineName!!.text = "出发"
            holder.dirUp!!.visibility = View.INVISIBLE
            holder.dirDown!!.visibility = View.VISIBLE
            holder.splitLine!!.visibility = View.INVISIBLE
            return convertView
        } else if (position == mItemList.size - 1) {
            holder.dirIcon!!.setImageResource(R.drawable.dir_end)
            holder.lineName!!.text = "到达终点"
            holder.dirUp!!.visibility = View.VISIBLE
            holder.dirDown!!.visibility = View.INVISIBLE
            return convertView
        } else {
            holder.splitLine!!.visibility = View.VISIBLE
            holder.dirUp!!.visibility = View.VISIBLE
            holder.dirDown!!.visibility = View.VISIBLE
            val actionName = item.action
            val resID = AMapUtil.getWalkActionID(actionName)
            holder.dirIcon!!.setImageResource(resID)
            holder.lineName!!.text = item.instruction
            return convertView
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
