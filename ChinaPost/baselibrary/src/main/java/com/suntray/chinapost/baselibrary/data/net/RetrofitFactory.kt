package com.suntray.chinapost.baselibrary.data.net

import com.suntray.chinapost.baselibrary.common.BaseConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by ASUS on 2018/5/29.
 */
class RetrofitFactory  private constructor(){

    companion object{
        val instance:RetrofitFactory by lazy {
            RetrofitFactory();
        }
    }

   var intercept:Interceptor
   var retrofit:Retrofit

    init {
        intercept= Interceptor {
            chain -> var request=chain.request().newBuilder()
                .addHeader("Content_Type","application/json")
                .addHeader("charset","utf-8")
                .addHeader(BaseConstants.KEY_SP_TOKEN,"").build();
            chain.proceed(request);
        }

        retrofit=Retrofit.Builder().baseUrl(BaseConstants.BASE_URL)
                         .addConverterFactory(GsonConverterFactory.create())
                         .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(initClient()).build();
    }


    /**
     * 初始化客户端
     */
    private fun  initClient(): OkHttpClient? {
        /**
         * 初始化日志的拦截器
         */
        return  OkHttpClient.Builder().addInterceptor(initLogInterceptor())
                              .addInterceptor(intercept)
                              .connectTimeout(60, TimeUnit.SECONDS)
                              .writeTimeout(60,TimeUnit.SECONDS)
                              .readTimeout(60,TimeUnit.SECONDS).build();
    }

    /**
     * 初始化日志的拦截器
     */
    private fun  initLogInterceptor(): Interceptor? {
        var interceptor= HttpLoggingInterceptor();
        interceptor.level=HttpLoggingInterceptor.Level.BODY
        return interceptor;
    }

    /**
     * 创建 对应的service 接口
     */
    fun <T>create(service: Class<T>):T{
        return retrofit.create(service)
    }
}