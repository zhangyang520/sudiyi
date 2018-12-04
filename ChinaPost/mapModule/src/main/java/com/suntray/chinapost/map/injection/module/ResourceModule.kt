package com.suntray.chinapost.map.injection.module

import com.suntray.chinapost.baselibrary.injection.PerComponentScope
import com.suntray.chinapost.map.service.ResourceService
import com.suntray.chinapost.map.service.impl.ResourceServiceImpl
import dagger.Module
import dagger.Provides

/**
 *   资源位 相关的数据module
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/21 13:25
 */
@PerComponentScope
@Module
class ResourceModule {

    @Provides
    fun  providesResourceService(resourceService: ResourceServiceImpl):ResourceService{
        return resourceService
    }
}