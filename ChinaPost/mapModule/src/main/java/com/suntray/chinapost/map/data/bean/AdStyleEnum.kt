package com.suntray.chinapost.map.data.bean

/**
 *  广告类型的 枚举类
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/17 13:45
 */
enum class AdStyleEnum(style:Int,name:String){
    AdIdle(4,"空闲"){
        override fun getStyle(): Int {
            return 4
        }

    },AdLocked(2,"已锁定"){
        override fun getStyle(): Int {
            return 2;
        }
    },AdReserved(3,"已预订"){
        override fun getStyle(): Int {
          return 3;
        }
    },UNUSED(1,"不可用"){
        override fun getStyle(): Int {
            return 1;
        }
    };
    abstract fun getStyle():Int;
}