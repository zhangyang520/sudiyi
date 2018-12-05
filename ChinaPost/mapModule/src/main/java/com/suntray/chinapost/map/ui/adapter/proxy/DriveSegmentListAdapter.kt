package com.suntray.chinapost.map.ui.adapter.proxy

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.services.route.DriveStep
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.utils.AMapUtil

import java.util.ArrayList

/**
 * 驾车路线详情页adapter
 *
 * @author ligen
 */
class DriveSegmentListAdapter(private val mContext: Context, list: List<DriveStep>) : BaseAdapter() {
    private val mItemList = ArrayList<DriveStep>()

    init {
        mItemList.add(DriveStep())
        for (driveStep in list) {
            mItemList.add(driveStep)
        }
        mItemList.add(DriveStep())
    }

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return mItemList.size
    }

    override fun getItem(position: Int): Any {
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
        var holder: ViewHolder? =
                null
        if (convertView == null) {
            holder = ViewHolder()
            convertView = View.inflate(mContext, R.layout.item_bus_segment, null)
            holder.driveDirIcon = convertView!!
                    .findViewById(R.id.bus_dir_icon) as ImageView
            holder.driveLineName = convertView
                    .findViewById(R.id.bus_line_name) as TextView
            holder.driveDirUp = convertView
                    .findViewById(R.id.bus_dir_icon_up) as ImageView
            holder.driveDirDown = convertView
                    .findViewById(R.id.bus_dir_icon_down) as ImageView
            holder.splitLine = convertView
                    .findViewById(R.id.bus_seg_split_line) as ImageView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        val item = mItemList[position]
        if (position == 0) {
            holder.driveDirIcon!!.setImageResource(R.drawable.dir_start)
            holder.driveLineName!!.text = "出发"
            holder.driveDirUp!!.visibility = View.GONE
            holder.driveDirDown!!.visibility = View.VISIBLE
            holder.splitLine!!.visibility = View.GONE
            return convertView
        } else if (position == mItemList.size - 1) {
            holder.driveDirIcon!!.setImageResource(R.drawable.dir_end)
            holder.driveLineName!!.text = "到达终点"
            holder.driveDirUp!!.visibility = View.VISIBLE
            holder.driveDirDown!!.visibility = View.GONE
            holder.splitLine!!.visibility = View.VISIBLE
            return convertView
        } else {
            val actionName = item.action
            val resID = AMapUtil.getDriveActionID(actionName)
            holder.driveDirIcon!!.setImageResource(resID)
            holder.driveLineName!!.text = item.instruction
            holder.driveDirUp!!.visibility = View.VISIBLE
            holder.driveDirDown!!.visibility = View.VISIBLE
            holder.splitLine!!.visibility = View.VISIBLE
            return convertView
        }

    }

    private inner class ViewHolder {
        internal var driveLineName: TextView? = null
        internal var driveDirIcon: ImageView? = null
        internal var driveDirUp: ImageView? = null
        internal var driveDirDown: ImageView? = null
        internal var splitLine: ImageView? = null
    }

}
