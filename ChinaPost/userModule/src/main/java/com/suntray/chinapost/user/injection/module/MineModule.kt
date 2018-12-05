package com.suntray.chinapost.user.injection.module

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.user.service.ClientService
import com.suntray.chinapost.user.service.MineDotService
import com.suntray.chinapost.user.service.MineService
import com.suntray.chinapost.user.service.impl.ClientServiceImpl
import com.suntray.chinapost.user.service.impl.MineDotServiceImpl
import com.suntray.chinapost.user.service.impl.MineServiceImpl
import dagger.Module
import dagger.Provides

/**
 *  我的模块的 module 信息
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/18 11:48
 */
@PerComponentScope
@Module
class MineModule {

    @Provides
    fun  providesMineService(mineServiceImpl: MineServiceImpl):MineService{
        return mineServiceImpl
    }

    @Provides
    fun  providesClientService(clientServiceImpl: ClientServiceImpl):ClientService{
        return clientServiceImpl
    }

    @Provides
    fun  providesMineDotService(mineDotServiceImpl: MineDotServiceImpl):MineDotService{
        return mineDotServiceImpl
    }
}