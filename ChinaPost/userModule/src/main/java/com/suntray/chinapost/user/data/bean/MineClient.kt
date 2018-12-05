package com.suntray.chinapost.user.data.bean

import com.lidroid.xutils.db.annotation.Table
import java.io.Serializable

/**
 *   我的客户实体
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 15:31
 */
@Table
class MineClient:Serializable{
   var salesmanname:String=""  //归属销售id
   var profession:Int=0 //客户行业
   var state:Int=0  //客户状态
   var stage:Int=0 //客户阶段
   var city:Int=0 //客户所在市编码
   var id:Int=0 //客户主键id
   var nature:Int=0 //客户性质
   var name:String="" //客户名称
   var province:Int=0 //客户所在省编码
   var naturename:String="" //客户性质名称
   var district:Int=0 //客户所在区编码
   var origin:Int=0  //客户来源
   var linkmanname:String="" //联系人姓名
   var originname:String=""  //客户来源名称
   var linkmanphone:String="" //联系人手机
   var salesman:Int=0;  //当前销售人员的id
   var professionname="" //客户行业名称
   var address:String="" //客户详细地址
   var stagename:String="" //客户阶段名称
   var brand:String=""   //客户品牌
   var statename:String="" //客户账号状态名称
   var cityname=""//城市名称
   var provincename=""//省的名称
   var districtname=""//区域名称


   override fun toString(): String {
      return "MineClient(salesmanname='$salesmanname', profession=$profession, state=$state, stage=$stage, city=$city, id=$id, nature=$nature, name='$name', province=$province, naturename='$naturename', district=$district, origin=$origin, linkmanname='$linkmanname', originname='$originname', linkmanphone='$linkmanphone', salesman=$salesman, professionname='$professionname', address='$address', stagename='$stagename', brand='$brand', statename='$statename')"
   }
}
