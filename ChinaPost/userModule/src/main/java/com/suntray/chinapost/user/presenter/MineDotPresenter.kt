package com.suntray.chinapost.user.presenter

import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.presenter.BasePresenter
import com.suntray.chinapost.baselibrary.rx.BaseSucriber
import com.suntray.chinapost.baselibrary.rx.assertMethod
import com.suntray.chinapost.baselibrary.rx.execute
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.data.request.MineReservedDotRequest
import com.suntray.chinapost.user.data.request.MineXudingDotRequest
import com.suntray.chinapost.user.data.request.RelieveSaveRequest
import com.suntray.chinapost.user.presenter.view.ClientView
import com.suntray.chinapost.user.presenter.view.MineDotView
import com.suntray.chinapost.user.service.impl.MineDotServiceImpl
import rx.Observable
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 *   预定点位 的preseneter
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/22 :10
 */
class MineDotPresenter @Inject constructor():BasePresenter<MineDotView>(){

    @Inject
    lateinit var mineDotServiceImpl: MineDotServiceImpl



    fun relieveSave(relieveSaveRequest: RelieveSaveRequest){
        mineDotServiceImpl.relieveSave(relieveSaveRequest).
                execute(object: BaseSucriber<ArrayList<Object>>(baseView,MineDotPresenter::javaClass.name){
                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MineDotView).onError(e.errorContent);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as MineDotView).onError("请求超时!")
                            }else{
                                (baseView as MineDotView).onError("请求失败")
                            }
                        }
                    }

                    override fun onNext(t:ArrayList<Object>) {
                        super.onNext(t)
                        assertMethod(baseView,{
                            (baseView as MineDotView).onRelieveSaveResponse()
                        })
                    }
                },lifecylerProvider);
    }
    /**
     * 我预定的点位
     */
    fun mineReservedDot(mineReservedDotRequest: MineReservedDotRequest,action: RefreshAction){
        mineDotServiceImpl.mineReservedDot(mineReservedDotRequest).
                execute(object: BaseSucriber<ArrayList<MineReservedDot>>(baseView,MineDotPresenter::javaClass.name){
                    override fun onError(e: Throwable?) {
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MineDotView).onError(e.errorContent,action);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as MineDotView).onError("请求超时!",action)
                            }else{
                                (baseView as MineDotView).onError("请求失败",action)
                            }
                        }
                    }

                    override fun onNext(t:ArrayList<MineReservedDot>) {
                        super.onNext(t)
                        assertMethod(baseView,{
                            (baseView as MineDotView).onGetMineReservedDot(t,action)
                        })
                    }
                },lifecylerProvider);
    }

    /**
     * 续订保存
     */
    fun dotXuding(mineDotXudingDotRequest: MineXudingDotRequest){
        mineDotServiceImpl.dotXuding(mineDotXudingDotRequest).
                execute(object: BaseSucriber<ArrayList<Object>>(baseView,MineDotPresenter::javaClass.name){
                    override fun onError(e: Throwable?) {
                        SystemUtil.printlnStr("OK  onError ......")
                        if(e is ContentException){
                            assertMethod(baseView,{
                                (baseView as MineDotView).onError(e.errorContent);
                                baseView.hideLoading()
                            })
                        }else{
                            if(e is SocketTimeoutException){
                                (baseView as MineDotView).onError("请求超时!")
                            }else{
                                (baseView as MineDotView).onError("请求失败")
                            }
                        }
                    }

                    override fun onNext(t:ArrayList<Object>) {
                        super.onNext(t)
                        SystemUtil.printlnStr("OK  onNext ......")
                        assertMethod(baseView,{
                            (baseView as MineDotView).onReservedDot()
                        })
                    }
                },lifecylerProvider);
    }
}