package com.suntray.chinapost.map.injection.module

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.map.service.DictService
import com.suntray.chinapost.map.service.MapService
import com.suntray.chinapost.map.service.impl.DictServiceImpl
import com.suntray.chinapost.map.service.impl.MapServiceImpl
import dagger.Module
import dagger.Provides

@PerComponentScope
@Module
class MapModule {

    @Provides
    fun  provicesMapService(mapService:MapServiceImpl):MapService{
        return mapService
    }

    @Provides
    fun  providesDictService(dictServiceImpl: DictServiceImpl):DictService{
        return dictServiceImpl;
    }
}