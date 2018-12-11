package com.suntray.chinapost.user.data.enum

import com.suntray.chinapost.user.data.bean.AptitudeInfo

/**
 * 上传 资质的图片的类型
 */
enum class UploadAptitudeEnum(var yingyePathId:String,var currentNumber: Int,
                              var imageList:ArrayList<AptitudeInfo?>,var newAddList:ArrayList<AptitudeInfo>
                              ,var deleteList:ArrayList<AptitudeInfo>) {
    JiBenXinxi("baseFiles",0, arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "baseFiles"
        }

        override fun addAutoCurrentNumer() {
            this.currentNumber++
        }

        override fun addPath(path: AptitudeInfo) {
            this.imageList.add(0,path)
        }

        override fun getPathList(): ArrayList<AptitudeInfo?> {
            return imageList
        }
    },HangyeTeshu("specialFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "specialFiles"
        }

        override fun addAutoCurrentNumer() {
            this.currentNumber++
        }

        override fun addPath(path: AptitudeInfo) {
            this.imageList.add(0,path)
        }

        override fun getPathList(): ArrayList<AptitudeInfo?> {
            return imageList
        }
    };

    abstract fun getCurrentNumber1():Int
    abstract fun getPathId():String
    abstract fun addAutoCurrentNumer();
    abstract fun addPath(path:AptitudeInfo)
    abstract fun getPathList():ArrayList<AptitudeInfo?>
}