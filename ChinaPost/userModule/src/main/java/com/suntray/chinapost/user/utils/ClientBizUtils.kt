package com.suntray.chinapost.user.utils

import android.app.Activity
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.suntray.chinapost.baselibrary.data.bean.CityListAction
import com.suntray.chinapost.baselibrary.data.bean.ProvinceCity
import com.suntray.chinapost.baselibrary.ui.adapter.CityAdapter
import com.suntray.chinapost.baselibrary.ui.adapter.DistrictAdapter
import com.suntray.chinapost.baselibrary.ui.adapter.ProvinceAdapter
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.zhy.autolayout.config.AutoLayoutConifg
import com.zhy.autolayout.utils.AutoUtils
import com.suntray.chinapost.baselibrary.R
import com.suntray.chinapost.baselibrary.ui.adapter.inner.ClickCodeAction
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.presenter.ClientPresenter

/**
 *   客户业务逻辑的工具类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 20:04
 */
object ClientBizUtils {

    /**
     * 展示城市 列表
     */
    var contentView: View?=null
    var window: PopupWindow?=null
    var provinceAdapter: ProvinceAdapter?=null
    var cityAdapter: CityAdapter?=null
    var districtAdapter: DistrictAdapter?=null
    var clickProvinceId:Int?=0

    var clickIndex=0  //点击的 index
    var provinceName=""
    var cityName=""
    var districtName=""

    fun showCityList(context: Activity, rootView: View, action: CityListAction, provinceCity: ArrayList<ProvinceCity>, mapPresenter: ClientPresenter, currentClient: MineClient?, tv_client_dizhi: TextView){
        if(action== CityListAction.ProvinceAtion){
            clickIndex=0
            contentView= View.inflate(context, R.layout.popup_city_list,null);
            if(window!=null && window!!.isShowing){
                window!!.dismiss()
                window=null
            }

            window= PopupWindow(context,null,R.style.Transparent_Dialog);
            window!!.width=  AutoUtils.getPercentWidthSize(750);
            window!!.height= AutoUtils.getPercentHeightSize(1242);
            AutoUtils.autoSize(contentView)
            window!!.contentView=contentView;
            window!!.isOutsideTouchable=false
            window!!.showAtLocation(rootView, Gravity.NO_GRAVITY,0, AutoUtils.getPercentHeightSize(150))
            SystemUtil.printlnStr("showCityList 11111111111111 provinceCity:"+provinceCity.size)

            /**
             *  ok 按钮
             */
            var btnOk=contentView!!.findViewById(R.id.btn_ok) as Button
            btnOk.setOnClickListener({
                if(clickIndex==3){
                    //必须 选择到 3
                    window!!.dismiss()
                    tv_client_dizhi.setText(provinceName+ cityName+ districtName)
                }else{
                    ToastUtil.makeText(context,"必须选择到区")
                }
            })

            /*
             *  取消按钮
             */
            var btnCancel=contentView!!.findViewById(R.id.btn_cancel) as Button
            btnCancel.setOnClickListener({
                window!!.dismiss()
                provinceName=""
                cityName=""
                districtName=""
                clickIndex=0
            })
            //显示 省
            var recyler_province=contentView!!.findViewById(R.id.recyler_province) as RecyclerView
            var linearLayoutManager= LinearLayoutManager(context)
            linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
            recyler_province.layoutManager=linearLayoutManager
            provinceAdapter=ProvinceAdapter(provinceCity,context)
            provinceAdapter!!.clickCodeAction=object: ClickCodeAction {
                override fun clickItem(code: Int, action: CityListAction, provinceCity: ProvinceCity) {
                    mapPresenter.province(action.getCityValue1().toString(),code,-1,action)
                    currentClient!!.province=provinceCity.id
                    provinceName=provinceCity.province
                    clickIndex=1
                }
            }
            recyler_province.adapter=provinceAdapter

            if(cityAdapter!=null){
                cityAdapter!!.provinceList?.clear()
                cityAdapter!!.notifyDataSetChanged()
            }

            if(districtAdapter!=null){
                districtAdapter!!.provinceList?.clear()
                districtAdapter!!.notifyDataSetChanged()
            }
        }else if(action== CityListAction.CityAction){
            SystemUtil.printlnStr("showCityList 2222222222222 provinceCity:"+provinceCity.size)
            //显示的 市
            var recyler_city=contentView!!.findViewById(R.id.recyler_city) as RecyclerView
            var linearLayoutManager= LinearLayoutManager(context)
            linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
            recyler_city.layoutManager=linearLayoutManager
            cityAdapter= CityAdapter(provinceCity,context)
            cityAdapter!!.clickCodeAction=object: ClickCodeAction {
                override fun clickItem(code: Int, action: CityListAction, provinceCity: ProvinceCity) {
                    mapPresenter.province(action.getCityValue1().toString(),-1,code,action)
                    clickProvinceId=provinceCity.provinceId
                    currentClient!!.city=provinceCity.id
                    clickIndex=2
                    if(provinceCity.city.equals("市辖区")|| provinceCity.city.equals("县")
                                            || provinceCity.city.equals("北京市")|| provinceCity.city.equals("天津市")
                                                         || provinceCity.city.equals("上海市")|| provinceCity.city.equals("重庆市")){
                        cityName=""
                    }else{
                        cityName=provinceCity.city
                    }
                }
            }
            recyler_city.adapter=cityAdapter
            if(districtAdapter!=null){
                districtAdapter!!.provinceList?.clear()
                districtAdapter!!.notifyDataSetChanged()
            }
        }else if(action== CityListAction.DistrictAction){
            SystemUtil.printlnStr("showCityList 333333333333333   provinceCity:"+provinceCity.size)
            //显示的区
            var recyler_district=contentView!!.findViewById(R.id.recyler_district) as RecyclerView
            var linearLayoutManager= LinearLayoutManager(context)
            linearLayoutManager.orientation= LinearLayoutManager.VERTICAL
            recyler_district.layoutManager=linearLayoutManager
            districtAdapter= DistrictAdapter(provinceCity,context)
            districtAdapter!!.clickCodeAction=object: ClickCodeAction {

                override fun clickDistrict(provinceCity: ProvinceCity) {
                    super.clickDistrict(provinceCity)
                    clickIndex=3
                    currentClient!!.district=provinceCity.id
                    //最后 点击区域
                    districtName=provinceCity.district
                }
            }
            recyler_district.adapter=districtAdapter
        }
    }

}