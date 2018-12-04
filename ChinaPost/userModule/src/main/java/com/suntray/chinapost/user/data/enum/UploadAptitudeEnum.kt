package com.suntray.chinapost.user.data.enum

import com.suntray.chinapost.user.data.bean.AptitudeInfo

/**
 * 上传 资质的图片的类型
 */
enum class UploadAptitudeEnum(var yingyePathId:String,var currentNumber: Int,
                              var imageList:ArrayList<AptitudeInfo?>,var newAddList:ArrayList<AptitudeInfo>
                              ,var deleteList:ArrayList<AptitudeInfo>) {
    Yingye("licenseFiles",0, arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "licenseFiles"
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
    },Trade("trademarkFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "trademarkFiles"
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
    },
    Food("healthFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "healthFiles"
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
    },Portrait("portraitFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "portraitFiles"
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
    },Host("urlFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "urlFiles"
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
    },ZCode("qrcodeFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "qrcodeFiles"
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
    },ClientMianze("clientdisclaimerFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "clientdisclaimerFiles"
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
    },PostMianze("expressdisclaimerFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "expressdisclaimerFiles"
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
    },DiChanXiaoShouLicense("landsaleFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "landsaleFiles"
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
    },Other("otherFiles",0,arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "otherFiles"
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