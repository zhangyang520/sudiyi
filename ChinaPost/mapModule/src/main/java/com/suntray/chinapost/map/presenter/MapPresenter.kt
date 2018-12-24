package com.suntray.chinapost.map.presenter

import com.suntray.chinapost.baselibrary.data.bean.*
import com.suntray.chinapost.baselibrary.data.dao.*
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.data.bean.MapDot
import com.suntray.chinapost.map.data.request.*
import com.suntray.chinapost.map.data.response.ClientDictResponse
import com.suntray.chinapost.map.data.response.UpdateResponse
import com.suntray.chinapost.map.presenter.view.MapView
import com.suntray.chinapost.map.presenter.view.ResourceView
import com.suntray.chinapost.map.service.impl.DictServiceImpl
import com.suntray.chinapost.map.service.impl.MapServiceImpl
import com.suntray.chinapost.map.service.impl.OtherServiceImpl
import javax.inject.Inject

/**
 *
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/19 15:19
 */
class MapPresenter @Inject constructor():BasePresenter<MapView>(){

    @Inject
    lateinit var mapService: MapServiceImpl

    @Inject
    lateinit var dictService: DictServiceImpl

    @Inject
    lateinit var otherService:OtherServiceImpl

    /**
     * @param province
     * @param city
     * @param district
     * @param zoneaddress
     * @param isReserve
     * @param page
     * @param rows
     */
    fun provinceDot(provinceRequest: ProvinceDotRequest){
        //ProvinceDotRequest(province,city,district,zoneaddress,isReserve,page,rows,UserDao.getLocalUser().id)
        mapService.provinceDot(provinceRequest).
                    execute(object:BaseSucriber<ArrayList<MapDot>>(baseView,MapPresenter::javaClass.name){

                        override fun onError(e: Throwable?) {
                            if(e is ContentException){
                                assertMethod(baseView,{
                                    baseView.hideLoading()
                                    (baseView as MapView).onError(e.errorContent)
                                    (baseView as MapView).onProvinceDotRequestError()
                                })
                            }else{
                                super.onError(e)
                            }
                        }

                        override fun onNext(t: ArrayList<MapDot>) {
                            super.onNext(t)
                            baseView.hideLoading()
                            (baseView as MapView).onProvinceDotRequest(t)
                        }
        },lifecylerProvider)
    }


    /**
     * 点位 半径 业务请求
     */
    fun radiusDot(radiusDotRequest:RadiusDotRequest){
        mapService.radiusDot(radiusDotRequest).
                execute(object:BaseSucriber<ArrayList<MapDot>>(baseView,MapPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                baseView.hideLoading()
                                (baseView as MapView).onError(e.errorContent)
                                (baseView as MapView).onRadiusDotRequestError()
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onNext(t: ArrayList<MapDot>) {
                        super.onNext(t)
                        baseView.hideLoading()
                        (baseView as MapView).onRadisDotRequest(t)
                    }
                },lifecylerProvider)

    }

    var isProvinceRequesting=false
    /**
     *  省市区的请求
     */
    fun province(type:String,provinceId:Int,cityId:Int,action: CityListAction){
        isProvinceRequesting=true
        mapService.province(ProvinceRequest(type,provinceId,cityId)).
                execute(object:BaseSucriber<ArrayList<ProvinceCity>>(baseView,MapPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        isProvinceRequesting=false
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MapView).hideLoading()
                                (baseView as MapView).onError(e.errorContent)
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onNext(t: ArrayList<ProvinceCity>) {
                        super.onNext(t)
                        (baseView as MapView).hideLoading()
                        (baseView as MapView).onProvinceCityRequest(t,action)
                        isProvinceRequesting=false
                    }
                },lifecylerProvider)

    }

    /**
     * 获取小区列表
     */
    fun getDistrictTypeList(){
        dictService.getDistrictTypeList().
                execute(object:BaseSucriber<ArrayList<DistrictType>>(baseView,MapPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MapView).onError(e.errorContent)
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onStart() {

                    }
                    override fun onNext(t: ArrayList<DistrictType>) {
                        super.onNext(t)
                        if(t.size>0){
                            DistrictTypeDao.deleteAll()
                            DistrictTypeDao.saveUpDate(t)
                        }else{
                            baseView.onError("小区类型数据为空")
                        }
                    }
                },lifecylerProvider)
    }

    /**
     * 获取资源位 类型数据
     */
    fun  getResourceTypeList(){
        dictService.getResourceList().
                execute(object:BaseSucriber<ArrayList<ResourceType>>(baseView,MapPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MapView).onError(e.errorContent)
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onStart() {

                    }
                    override fun onNext(t: ArrayList<ResourceType>) {
                        super.onNext(t)
                        if(t.size>0){
                            ResourceTypeDao.deleteAll()
                            ResourceTypeDao.saveUpDate(t)
                        }else{
                            baseView.onError("资源位类型数据为空")
                        }
                    }
                },lifecylerProvider)
    }


    /**
     * 客户相关的字典项
     */
    fun getClientNameList(){
        dictService.getClientNameList().
                execute(object:BaseSucriber<ClientDictResponse>(baseView,MapPresenter::javaClass.name){

                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MapView).onError(e.errorContent)
                            })
                        }else{
                            super.onError(e)
                        }
                    }

                    override fun onStart() {

                    }
                    override fun onNext(t: ClientDictResponse) {
                        super.onNext(t)
                        ClientDictDao.deleteAll()
                        SystemUtil.printlnStr("ClientDictResponse:"+t.toString())
                        if(t!=null){
                            if(t.natureList!=null && t!!.natureList!!.size>0){
                                ClientDictDao.saveUpDate(t.natureList!!)
                            }
                            if(t.originList!=null && t!!.originList!!.size>0){
                                ClientDictDao.saveUpDate(t.originList!!)
                            }
                            if(t.professionList!=null && t!!.professionList!!.size>0){
                                ClientDictDao.saveUpDate(t.professionList!!)
                            }
                            if(t.stageList!=null && t!!.stageList!!.size>0){
                                ClientDictDao.saveUpDate(t.stageList!!)
                            }
                            SystemUtil.printlnStr("ClientDictResponse all:"+ClientDictDao.getall().toString())
                        }else{
                            baseView.onError("客户字典数据为空")
                        }
                    }
                },lifecylerProvider)
    }


    /**
     *  获取 广告类型列表
     */
    fun getAdTypeList() {
        dictService.getAdTypeList().execute(object : BaseSucriber<ArrayList<AdStyle>>(baseView, ResourcePresenter::javaClass.name) {

            override fun onError(e: Throwable?) {
                if (e is ContentException) {
                    assertMethod(baseView, {
                        (baseView as MapView).onError(e.errorContent)
                    })
                } else {
                    super.onError(e)
                }
            }

            override fun onStart() {

            }
            override fun onNext(t: ArrayList<AdStyle>) {
                super.onNext(t)
                AdStyleDao.deleteAll()
                AdStyleDao.saveUpDate(t)
                SystemUtil.printlnStr("t:"+t.toString())
                (baseView as MapView).onGetAdStyleList(t)
            }
        }, lifecylerProvider)
    }

    /**
     * 更新版本
     */
    fun updateAppVersion(updateRequest: UpdateRequest){
        SystemUtil.printlnStr("updateAppVersion .......:")
        otherService.update(updateRequest).execute(object : BaseSucriber<UpdateResponse>(baseView, MapPresenter::javaClass.name) {

            override fun onError(e: Throwable?) {
                if (e is ContentException) {
                    assertMethod(baseView, {
//                        (baseView as MapView).onError(e.errorContent)
                    })
                } else {
//                    super.onError(e)
                }
            }

            override fun onStart() {

            }

            override fun onNext(t: UpdateResponse) {
                super.onNext(t)
                (baseView as MapView).onGetAppVersion(t)
            }
        }, lifecylerProvider)
    }

    /**
     * 查询一键预订的数量
     */
    fun findReserveNum(findReserveNumRequest: FindReserveNumRequest){
        mapService.findReserveNum(findReserveNumRequest).execute(object : BaseSucriber<Int>(baseView, ResourcePresenter::javaClass.name) {

            override fun onError(e: Throwable?) {
                if (e is ContentException) {
                    assertMethod(baseView, {
                        (baseView as MapView).hideLoading()
                        (baseView as MapView).onError(e.errorContent)
                    })
                } else {
                    super.onError(e)
                }
            }

            override fun onStart() {

            }
            override fun onNext(t:Int) {
                super.onNext(t)
                (baseView as MapView).hideLoading()
                (baseView as MapView).onFindReserverNumber(t)
            }
        }, lifecylerProvider)
    }
}