package com.suntray.chinapost.user.data.response

/**
 *  @Title ${name}
 *  @ProjectName ChinaPost
 *  @Description: TODO
 *  @author Administrator
 *  @date 2018/12/1411:25
 *
 */
class FindReservePointByIdResponse {

    var resourceCategoryList:ArrayList<String>?=null
    var pointList:ArrayList<Point>?=null
    /**
     * 预订信息
     */
    inner class Reserve{
        var salesmanname=""
        var clientname=""
        var adtypename=""
        var startdate=""
        var enddate=""
        override fun toString(): String {
            return "Reserve(salesmanname='$salesmanname', clientname='$clientname', adtypename='$adtypename', startdate='$startdate', enddate='$enddate')"
        }
    }

    inner class Point{
      var equid=""
      var pointname=""
      var equlocation=""
        override fun toString(): String {
            return "Point(equid='$equid', pointname='$pointname', equlocation='$equlocation')"
        }
    }

    override fun toString(): String {
        return "FindReservePointByIdResponse(resourceCategoryList=$resourceCategoryList, pointList=$pointList)"
    }


}