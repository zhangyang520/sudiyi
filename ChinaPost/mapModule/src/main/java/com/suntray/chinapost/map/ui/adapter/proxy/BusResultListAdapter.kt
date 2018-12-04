package com.suntray.chinapost.map.ui.adapter.proxy

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.amap.api.services.route.BusPath
import com.amap.api.services.route.BusRouteResult
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.ui.activity.proxy.routemap.BusRouteDetailActivity
import com.suntray.chinapost.map.utils.AMapUtil

class BusResultListAdapter(private val mContext: Context, private val mBusRouteResult: BusRouteResult) : BaseAdapter() {
    private val mBusPathList: List<BusPath>

    init {
        mBusPathList = mBusRouteResult.paths
    }

    override fun getCount(): Int {
        return mBusPathList.size
    }

    override fun getItem(position: Int): Any {
        return mBusPathList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder? = null
        if (convertView == null) {
            holder = ViewHolder()
            convertView = View.inflate(mContext, R.layout.item_bus_result, null)
            holder.title = convertView!!.findViewById(R.id.bus_path_title) as TextView
            holder.des = convertView.findViewById(R.id.bus_path_des) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val item = mBusPathList[position]
        holder.title!!.text = AMapUtil.getBusPathTitle(item)
        holder.des!!.text = AMapUtil.getBusPathDes(item)

        convertView.setOnClickListener {
            var intent=Intent(mContext.getApplicationContext(),BusRouteDetailActivity::class.java)
            intent!!.putExtra("bus_path", item);
            intent.putExtra("bus_result", mBusRouteResult);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
        return convertView
    }

    private inner class ViewHolder {
        internal var title: TextView? = null
        internal var des: TextView? = null
    }

}
