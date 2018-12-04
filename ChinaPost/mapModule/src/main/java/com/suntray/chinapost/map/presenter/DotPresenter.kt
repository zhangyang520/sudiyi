package com.suntray.chinapost.map.presenter

import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.data.bean.ResourceDotLocation
import com.suntray.chinapost.map.data.request.DotOfResourceInfoRequest
import com.suntray.chinapost.map.data.response.DotOfResourceListResponse
import com.suntray.chinapost.map.presenter.view.DotView
import com.suntray.chinapost.map.service.impl.ResourceServiceImpl
import retrofit2.http.Body
import rx.Observable
import javax.inject.Inject

/**
 *   点位的 相关的业务
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 13:33
 */
class DotPresenter @Inject constructor():BasePresenter<DotView>(){

     @Inject
     lateinit var resourceServiceImpl: ResourceServiceImpl
    /**
     * 获取 点位的资源位 列表的数据
     */
    fun getDotOfResourceInfo(id:Int){
        resourceServiceImpl.getDotOfResourceInfo(DotOfResourceInfoRequest(id)).
                execute(object: BaseSucriber<DotOfResourceListResponse>(baseView,DotOfResourceListResponse::javaClass.name){

                            override fun onError(e: Throwable?) {
                                if(e is ContentException){
                                    assertMethod(baseView,{
                                        baseView.hideLoading()
                                        (baseView as DotView).onError(e.errorContent)
                                    })
                                }else{
                                    super.onError(e)
                                }
                            }

                            override fun onNext(t: DotOfResourceListResponse) {
                                super.onNext(t)
                                baseView.hideLoading()
                                var resourceDotLocation=ResourceDotLocation()
                                if(t.resourceList!=null){
                                    resourceDotLocation.resourceAdList=t.resourceList!!
                                }

                                if(t.point!=null){
                                    if(t.point!!.zonename==null || t.point!!.zonename.equals("")){
                                        resourceDotLocation.zonename="无"
                                    }else {
                                        resourceDotLocation.zonename = t.point!!.zonename
                                    }

                                    if(t.point!!.publishtypename==null || t.point!!.publishtypename.equals("")){
                                        resourceDotLocation.publishtypename="无"
                                    }else{
                                        resourceDotLocation.publishtypename=t.point!!.publishtypename
                                    }
                                    if(t.point!!.pointname==null){
                                        resourceDotLocation.wangdianName="无"
                                    }else{
                                        resourceDotLocation.wangdianName=t.point!!.pointname
                                    }

                                    if(t.point!!.equlocation==null){
                                        resourceDotLocation.devicePosition="无"
                                    }else{
                                        resourceDotLocation.devicePosition=t.point!!.equlocation
                                    }

                                    if(t.point!!.zoneaddress==null){
                                        resourceDotLocation.deviceIdLocation="无"
                                    }else{
                                        resourceDotLocation.deviceIdLocation=t.point!!.zoneaddress
                                    }

                                    if (t.point!!.equspecify == null) {
                                        resourceDotLocation.deviceSpecification ="无"
                                    } else {
                                        resourceDotLocation.deviceSpecification = t.point!!.equspecify
                                    }

                                    resourceDotLocation.districtType=t.point!!.zonetype
                                    resourceDotLocation.id=t.point!!.id

                                    if(t.point!!.equid==null){
                                        resourceDotLocation.deviceId="无"
                                    }else{
                                        resourceDotLocation.deviceId=t.point!!.equid
                                    }
                                    if(t.point!!.imgpath!=null){
                                        resourceDotLocation.infoImage=t.point!!.imgpath
                                    }
                                    try {
                                        resourceDotLocation.limitType=t.point!!.publishtype.toInt()
                                    }catch (e:Exception){
                                        resourceDotLocation.limitType=-1
                                    }
                                    if(t.point!!.zoneaddress!=null){
                                        resourceDotLocation.resourceLocation=t.point!!.zoneaddress
                                    }
                                    if(t.point!!.zoneaddress!=null){
                                        resourceDotLocation.zoneaddress=t.point!!.zoneaddress
                                    }
                                    if(t.point!!.cityname!=null){
                                        resourceDotLocation.cityname=t.point!!.cityname
                                    }
                                    if(t.point!!.districtname!=null){
                                        resourceDotLocation.districtname=t.point!!.districtname
                                    }
                                    if(t.point!!.equlocation!=null){
                                        resourceDotLocation.equlocation=t.point!!.equlocation
                                    }
                                }
                                (baseView as DotView).onGetDotOfResourceList(resourceDotLocation)
                            }
        },lifecylerProvider)
    }
}