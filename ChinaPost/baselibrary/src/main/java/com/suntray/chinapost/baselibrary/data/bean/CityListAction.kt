package com.suntray.chinapost.baselibrary.data.bean

/**
 *   城市 列表的请求 操作
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 18:47
 */
enum class CityListAction(var cityValue:Int,var errorMsg:String){
    ProvinceAtion(0,"没有省级数据"){
        override fun getCityValue1():Int{
            return cityValue
        }

    },CityAction(1,"没有市级数据"){
        override fun getCityValue1():Int{
            return cityValue
        }
    },DistrictAction(2,"没有区级数据"){
        override fun getCityValue1():Int{
            return cityValue
        }
    };
    abstract fun getCityValue1():Int
}