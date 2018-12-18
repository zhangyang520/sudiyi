package com.suntray.chinapost.map.ui.adapter.saler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.map.R
import com.suntray.chinapost.map.data.bean.AdStyleEnum
import com.suntray.chinapost.map.data.bean.ResourceDotLocation
import com.suntray.chinapost.map.ui.dialog.ImageInfoDialog
import com.suntray.chinapost.provider.RouterPath

/**
 *   广告资源位对应的适配器
 *
 *   约定大于配置
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 13:48
 */
class AdResourceAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //view Type值 0标题 1:内容的条目
    final val TITLE_VIEW_TYPE=-1

    var resourceDotLocation: ResourceDotLocation? = null
    var context:Context?=null;
    constructor(resourceDotLocation:ResourceDotLocation,context: Context){
        this.resourceDotLocation=resourceDotLocation;
        this.context=context
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0){
            return TITLE_VIEW_TYPE
        }else{
            //返回对应的状态值
            return resourceDotLocation!!.resourceAdList.get(position-1).state
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType==AdStyleEnum.AdIdle.getStyle()){
            //空闲
            return IdleRecylerViewHolder(View.inflate(context, R.layout.recylerview_dot_state_idle,null));
        }else if(viewType==AdStyleEnum.AdLocked.getStyle()) {
            //已锁定
            return LockedRecylerViewHolder(View.inflate(context, R.layout.recylerview_dot_state_locked, null));
        }else if(viewType==TITLE_VIEW_TYPE){
            //标题
            return TitleRecylerViewHolder(View.inflate(context,R.layout.recylerview_dot_info,null));
        }else if( viewType==AdStyleEnum.UNUSED.getStyle()){
            //未用
            return UnUsedRecylerViewHolder(View.inflate(context,R.layout.recylerview_dot_state_unused,null))
        }else{
            //已预定
            return ReservedRecylerViewHolder(View.inflate(context, R.layout.recylerview_dot_state_reserved, null))
        }
    }

    override fun getItemCount(): Int {
        if(resourceDotLocation!=null){
            if(resourceDotLocation!!.resourceAdList!=null &&
                            resourceDotLocation!!.resourceAdList.size>0){
                return resourceDotLocation!!.resourceAdList.size+1 //注意从 index 0 开始
            }
            return 1
        }else{
            return 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TitleRecylerViewHolder){
            //设置图片
            Glide.with(context!!).load(resourceDotLocation!!.infoImage).error((context!!.resources.getDrawable(R.drawable.icon_default))).into(holder.iv_info!!)
            holder.tv_info_devive_specification!!.setText(resourceDotLocation!!.deviceSpecification)

            //事件点击
            holder.iv_info!!.setOnClickListener({
                if(resourceDotLocation!!.infoImage!=null && !resourceDotLocation!!.infoImage.equals("")){
                    var imageDialog= ImageInfoDialog(context)
                    imageDialog.show()
                    imageDialog.setContent(resourceDotLocation!!.infoImage)
                }else{
                    com.suntray.chinapost.map.utils.ToastUtil.show(context,"暂无图片信息")
                }
            })
            //网点名称
            holder.tv_wangdain_value!!.setText(resourceDotLocation!!.wangdianName)

            holder.tv_device_position_value!!.setText(resourceDotLocation!!.cityname
                                   +resourceDotLocation!!.districtname
                                   +resourceDotLocation!!.zoneaddress
                                   +resourceDotLocation!!.equlocation)

            holder.tv_info_district_type!!.setText(resourceDotLocation!!.zonename)

            holder.tv_info_limit_type!!.setText(resourceDotLocation!!.publishtypename)

            holder.tv_dot_location!!.setText(resourceDotLocation!!.resourceLocation)
            holder.tv_info_location!!.setText(resourceDotLocation!!.deviceId)

        }else if(holder is IdleRecylerViewHolder){
            //判断 任务类型 ---> 1:上刊 2:下刊
            if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==1){
                //查看下刊报告
                holder.btn_chakan!!.visibility=View.VISIBLE
                holder.btn_chakan!!.setText("查看下刊报告")
            }else if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==2){
                //查看 上刊报告
                holder.btn_chakan!!.visibility=View.VISIBLE
                holder.btn_chakan!!.setText("查看上刊报告")
            }else{
                //进行隐藏
                holder.btn_chakan!!.visibility=View.GONE
            }

            holder.btn_chakan!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_DOWN).withSerializable("ad",resourceDotLocation!!.resourceAdList.get(position-1)).navigation(context)
            })

            holder.btn_yuding!!.setOnClickListener({
                //跳转到 预定资源位
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_RESOURCE_RESERVED)
                           .withSerializable("resorceAd",resourceDotLocation!!.resourceAdList.get(position-1)).navigation(context)
            })

            holder.tv_ad_name!!.setText(resourceDotLocation!!.resourceAdList.get(position-1).name)

            if(resourceDotLocation!!.resourceAdList.get(position-1).typename==null ||
                                resourceDotLocation!!.resourceAdList.get(position-1).typename.equals("")){
                holder.tv_ad_type_value!!.setText("暂无类型")
            }else{
                holder.tv_ad_type_value!!.setText(resourceDotLocation!!.resourceAdList.get(position-1).typename)
            }

            if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==1){
                holder.tv_ad_down!!.visibility=View.VISIBLE
                holder.tv_ad_down_value!!.visibility=View.VISIBLE
                holder.tv_ad_down!!.text="上刊时间"
                holder.tv_ad_down_value!!.text=resourceDotLocation!!.resourceAdList.get(position-1).uploadDate
            }else if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==2){
                holder.tv_ad_down!!.visibility=View.VISIBLE
                holder.tv_ad_down_value!!.visibility=View.VISIBLE
                holder.tv_ad_down!!.text = "下刊时间"
                holder.tv_ad_down_value!!.text = resourceDotLocation!!.resourceAdList.get(position - 1).uploadDate
            }else{
                holder.tv_ad_down!!.visibility=View.GONE
                holder.tv_ad_down_value!!.visibility=View.GONE
            }

            //查看排期
            holder.btn_check_date!!.setOnClickListener({
               ARouter.getInstance().build(RouterPath.MapModule.POST_AD_CHECK_DATE)
                        .withSerializable("resorceAd",resourceDotLocation!!.resourceAdList.get(position - 1))
                        .withSerializable("resourceDotLocation",resourceDotLocation).navigation()
            })

        }else if(holder is UnUsedRecylerViewHolder){
            //空闲状态
            if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==1){
                holder.tv_ad_down!!.visibility=View.VISIBLE
                holder.tv_ad_down_unused_value!!.visibility=View.VISIBLE
                holder.tv_ad_down!!.text="上刊时间"
                holder.tv_ad_down_unused_value!!.text=resourceDotLocation!!.resourceAdList.get(position-1).uploadDate
            }else if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==2){
                holder.tv_ad_down!!.visibility=View.VISIBLE
                holder.tv_ad_down_unused_value!!.visibility=View.VISIBLE
                holder.tv_ad_down!!.text = "下刊时间"
                holder.tv_ad_down_unused_value!!.text = resourceDotLocation!!.resourceAdList.get(position - 1).uploadDate
            }else{

                holder.tv_ad_down!!.visibility=View.GONE
                holder.tv_ad_down_unused_value!!.visibility=View.GONE
            }

            //判断 任务类型 ---> 1:上刊 2:下刊
            if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==1){
                //查看下刊报告
                holder.btn_chakan_unused!!.visibility=View.VISIBLE
                holder.btn_chakan_unused!!.setText("查看下刊报告")
            }else if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==2){
                //查看 上刊报告
                holder.btn_chakan_unused!!.visibility=View.VISIBLE
                holder.btn_chakan_unused!!.setText("查看上刊报告")
            }else{
                //进行隐藏
                holder.btn_chakan_unused!!.visibility=View.GONE
            }

            holder.btn_chakan_unused!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_DOWN).withSerializable("ad",resourceDotLocation!!.resourceAdList.get(position-1)).navigation(context)
            })

            holder.btn_yuding_unused!!.setOnClickListener({
                //跳转到 预定资源位
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_RESOURCE_RESERVED)
                        .withSerializable("resorceAd",resourceDotLocation!!.resourceAdList.get(position-1)).navigation(context)
            })

            holder.tv_ad_name_unused!!.setText(resourceDotLocation!!.resourceAdList.get(position-1).name)

            if(resourceDotLocation!!.resourceAdList.get(position-1).typename==null ||
                    resourceDotLocation!!.resourceAdList.get(position-1).typename.equals("")){
                holder.tv_ad_name_unused!!.setText("暂无类型")
            }else{
                holder.tv_ad_name_unused!!.setText(resourceDotLocation!!.resourceAdList.get(position-1).typename)
            }

            //查看排期
            holder.btn_check_date!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_CHECK_DATE)
                        .withSerializable("resorceAd",resourceDotLocation!!.resourceAdList.get(position - 1))
                        .withSerializable("resourceDotLocation",resourceDotLocation).navigation()
            })

        }else if(holder is LockedRecylerViewHolder){
            //空闲状态
            if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==1){
                holder.tv_ad_down_error!!.visibility=View.VISIBLE
                holder.tv_ad_down_error!!.visibility=View.VISIBLE
                holder.tv_ad_down_error!!.text="上刊时间"
                holder.tv_ad_down_value_error!!.text=resourceDotLocation!!.resourceAdList.get(position-1).uploadDate
            }else if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==2){
                holder.tv_ad_down_error!!.visibility=View.VISIBLE
                holder.tv_ad_down_error!!.visibility=View.VISIBLE
                holder.tv_ad_down_error!!.text = "下刊时间"
                holder.tv_ad_down_value_error!!.text = resourceDotLocation!!.resourceAdList.get(position - 1).uploadDate
            }else{
                holder.tv_ad_down_error!!.visibility=View.GONE
                holder.tv_ad_down_error!!.visibility=View.GONE
            }

            //判断 任务类型 ---> 1:上刊 2:下刊
            if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==1){
                //查看下刊报告
                holder.btn_chakan_error!!.visibility=View.VISIBLE
                holder.btn_chakan_error!!.setText("查看下刊报告")
            }else if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==2){
                //查看 上刊报告
                holder.btn_chakan_error!!.visibility=View.VISIBLE
                holder.btn_chakan_error!!.setText("查看上刊报告")
            }else{
                //进行隐藏
                holder.btn_chakan_error!!.visibility=View.GONE
            }

            holder.btn_chakan_error!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_DOWN).withSerializable("ad",resourceDotLocation!!.resourceAdList.get(position-1)).navigation(context)
            })

            holder.btn_yuding_error!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_RESOURCE_RESERVED)
                        .withSerializable("resorceAd",resourceDotLocation!!.resourceAdList.get(position-1)).navigation(context)
            })

            //下刊时间
            holder.tv_ad_down_value_error!!.setText("无")
            holder.tv_ad_name_error!!.setText(resourceDotLocation!!.resourceAdList.get(position - 1).name)

            if(resourceDotLocation!!.resourceAdList.get(position-1).typename==null ||
                    resourceDotLocation!!.resourceAdList.get(position-1).typename.equals("")){
                holder.tv_ad_type_value_error!!.setText("暂无类型")
            }else{
                holder.tv_ad_type_value_error!!.setText(resourceDotLocation!!.resourceAdList.get(position-1).typename)
            }

            //查看排期
            holder.btn_check_date!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_CHECK_DATE)
                        .withSerializable("resorceAd",resourceDotLocation!!.resourceAdList.get(position - 1))
                        .withSerializable("resourceDotLocation",resourceDotLocation).navigation()
            })

        }else if(holder is ReservedRecylerViewHolder){
             //空闲状态
            if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==1){
                holder.tv_ad_period!!.visibility=View.VISIBLE
                holder.tv_ad_period_value!!.visibility=View.VISIBLE
                holder.tv_ad_period!!.text="上刊时间"
                holder.tv_ad_period_value!!.text=resourceDotLocation!!.resourceAdList.get(position-1).uploadDate
            }else if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==2){
                holder.tv_ad_period!!.visibility=View.VISIBLE
                holder.tv_ad_period_value!!.visibility=View.VISIBLE
                holder.tv_ad_period!!.text = "下刊时间"
                holder.tv_ad_period_value!!.text = resourceDotLocation!!.resourceAdList.get(position - 1).uploadDate
            }else{
                holder.tv_ad_period!!.visibility=View.GONE
                holder.tv_ad_period_value!!.visibility=View.GONE
                holder.tv_ad_period!!.text = "暂无时间"
                holder.tv_ad_period_value!!.text =""
            }

            //判断 任务类型 ---> 1:上刊 2:下刊
            if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==1){
                //查看下刊报告
                holder.btn_chakan_reserved!!.visibility=View.VISIBLE
                holder.btn_chakan_reserved!!.setText("查看下刊报告")
            }else if(resourceDotLocation!!.resourceAdList.get(position-1).taskType==2){
                //查看 上刊报告
                holder.btn_chakan_reserved!!.visibility=View.VISIBLE
                holder.btn_chakan_reserved!!.setText("查看上刊报告")
            }else{
                //进行隐藏
                holder.btn_chakan_reserved!!.visibility=View.GONE
            }

            holder.btn_chakan_reserved!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_DOWN).withSerializable("ad",resourceDotLocation!!.resourceAdList.get(position-1)).navigation(context)
            })

            holder.btn_yuding_reserved!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_RESOURCE_RESERVED)
                        .withSerializable("resorceAd",resourceDotLocation!!.resourceAdList.get(position-1)).navigation(context)
            })

            //设置文本内容
            //todo
            holder.tv_ad_belong_value!!.setText(resourceDotLocation!!.resourceAdList.get(position - 1).salesmanname)
            holder.tv_ad_name!!.setText(resourceDotLocation!!.resourceAdList.get(position - 1).name)

            holder.tv_ad_period_value!!.setText("无")

            if(resourceDotLocation!!.resourceAdList.get(position-1).typename==null ||
                    resourceDotLocation!!.resourceAdList.get(position-1).typename.equals("")){
                holder.tv_ad_summary_value!!.setText("暂无类型")
            }else{
                holder.tv_ad_summary_value!!.setText(resourceDotLocation!!.resourceAdList.get(position-1).typename)
            }

            //查看排期
            holder.btn_check_date!!.setOnClickListener({
                ARouter.getInstance().build(RouterPath.MapModule.POST_AD_CHECK_DATE)
                        .withSerializable("resorceAd",resourceDotLocation!!.resourceAdList.get(position - 1))
                        .withSerializable("resourceDotLocation",resourceDotLocation).navigation()
            })
        }
    }


    //标题的条目
    inner class TitleRecylerViewHolder:RecyclerView.ViewHolder{

        var tv_info_district_type:TextView?=null
        var tv_info_location:TextView?=null
        var tv_info_limit_type:TextView?=null
        var tv_info_devive_specification:TextView?=null
        var tv_dot_location:TextView?=null
        var tv_wangdain_value:TextView?=null
        var tv_device_position_value:TextView?=null


        var iv_info:ImageView?=null
        constructor(itemView: View?) : super(itemView!!){
            tv_info_devive_specification=itemView!!.findViewById(R.id.tv_info_devive_specification) as TextView
            tv_info_district_type=itemView!!.findViewById(R.id.tv_info_district_type) as TextView
            tv_info_location=itemView!!.findViewById(R.id.tv_info_location) as TextView
            tv_info_limit_type=itemView!!.findViewById(R.id.tv_info_limit_type) as TextView
            tv_dot_location=itemView!!.findViewById(R.id.tv_dot_location) as TextView

            tv_wangdain_value=itemView!!.findViewById(R.id.tv_wangdain_value) as TextView
            tv_device_position_value=itemView!!.findViewById(R.id.tv_device_position_value) as TextView

            iv_info=itemView!!.findViewById(R.id.iv_info) as ImageView
        }
    }

    //空闲的 item
    inner class IdleRecylerViewHolder:RecyclerView.ViewHolder{
        var tv_ad_name:TextView?=null
        var tv_ad_type_value:TextView?=null
        var tv_ad_down_value:TextView?=null
        var btn_yuding:Button?=null
        var btn_chakan:Button?=null
        var tv_ad_down:TextView?=null
        var btn_check_date:Button?=null

        constructor(itemView: View?) : super(itemView!!){
            tv_ad_name=itemView!!.findViewById(R.id.tv_ad_name) as TextView
            tv_ad_type_value=itemView!!.findViewById(R.id.tv_ad_type_value) as TextView
            tv_ad_down_value=itemView!!.findViewById(R.id.tv_ad_down_value) as TextView
            btn_yuding=itemView!!.findViewById(R.id.btn_yuding) as Button
            btn_chakan=itemView!!.findViewById(R.id.btn_chakan) as Button
            btn_check_date=itemView!!.findViewById(R.id.btn_check_date) as Button

            tv_ad_down=itemView!!.findViewById(R.id.tv_ad_down) as TextView
        }
    }

    //空闲的 item
    inner class UnUsedRecylerViewHolder:RecyclerView.ViewHolder{
        var tv_ad_name_unused:TextView?=null
        var tv_ad_type_unused_value:TextView?=null
        var tv_ad_down_unused_value:TextView?=null
        var btn_yuding_unused:Button?=null
        var btn_chakan_unused:Button?=null
        var tv_ad_down:TextView?=null
        var btn_check_date:Button?=null

        constructor(itemView: View?) : super(itemView!!){
            tv_ad_name_unused=itemView!!.findViewById(R.id.tv_ad_name_unused) as TextView
            tv_ad_type_unused_value=itemView!!.findViewById(R.id.tv_ad_type_unused_value) as TextView
            tv_ad_down_unused_value=itemView!!.findViewById(R.id.tv_ad_down_unused_value) as TextView
            tv_ad_down=itemView!!.findViewById(R.id.tv_ad_down) as TextView
            btn_yuding_unused=itemView!!.findViewById(R.id.btn_yuding_unused) as Button
            btn_chakan_unused=itemView!!.findViewById(R.id.btn_chakan_unused) as Button
            btn_check_date=itemView!!.findViewById(R.id.btn_check_date) as Button
        }
    }

    // 已预订的 viewHolder
    inner class ReservedRecylerViewHolder:RecyclerView.ViewHolder{

        var tv_ad_name:TextView?=null
        var tv_ad_belong_value:TextView?=null
        var tv_ad_period_value:TextView?=null
        var tv_ad_summary_value:TextView?=null
        var btn_yuding_reserved:Button?=null
        var btn_chakan_reserved:Button?=null
        var btn_check_date:Button?=null
        var tv_ad_period:TextView ?=null

        constructor(itemView: View?) : super(itemView!!){
            tv_ad_name=itemView!!.findViewById(R.id.tv_ad_name) as TextView
            tv_ad_belong_value=itemView!!.findViewById(R.id.tv_ad_belong_value) as TextView
            tv_ad_period_value=itemView!!.findViewById(R.id.tv_ad_period_value) as TextView
            tv_ad_summary_value=itemView!!.findViewById(R.id.tv_ad_summary_value) as TextView

            tv_ad_period=itemView!!.findViewById(R.id.tv_ad_period) as TextView
            btn_yuding_reserved=itemView!!.findViewById(R.id.btn_yuding_reserved) as Button
            btn_chakan_reserved=itemView!!.findViewById(R.id.btn_chakan_reserved) as Button
            btn_check_date=itemView!!.findViewById(R.id.btn_check_date) as Button

        }
    }

    //锁定的 viewHolder
    inner class LockedRecylerViewHolder:RecyclerView.ViewHolder{

        var tv_ad_name_error:TextView?=null
        var tv_ad_type_value_error:TextView?=null
        var tv_ad_down_value_error:TextView?=null
        var btn_yuding_error:Button?=null
        var btn_chakan_error:Button?=null
        var tv_ad_down_error:TextView?=null
        var btn_check_date:Button?=null

        constructor(itemView: View?) : super(itemView!!){
            tv_ad_name_error=itemView!!.findViewById(R.id.tv_ad_name_error)  as TextView
            tv_ad_type_value_error=itemView!!.findViewById(R.id.tv_ad_type_value_error) as TextView
            tv_ad_down_value_error=itemView!!.findViewById(R.id.tv_ad_down_value_error) as TextView

            tv_ad_down_error=itemView!!.findViewById(R.id.tv_ad_down_error) as TextView
            btn_yuding_error=itemView!!.findViewById(R.id.btn_yuding_error) as Button
            btn_chakan_error=itemView!!.findViewById(R.id.btn_chakan_error) as Button
            btn_check_date=itemView!!.findViewById(R.id.btn_check_date) as Button
        }
    }
}