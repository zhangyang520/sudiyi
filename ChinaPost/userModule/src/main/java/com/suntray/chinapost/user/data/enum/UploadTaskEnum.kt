package com.suntray.chinapost.user.data.enum

import com.suntray.chinapost.user.data.bean.TaskUpload

/**
 * 上传 资质的图片的类型
 */
enum class UploadTaskEnum(var yingyePathId:String, var currentNumber: Int,
                          var imageList:ArrayList<TaskUpload?>, var newAddList:ArrayList<TaskUpload>
                          , var deleteList:ArrayList<Int>,var newIdsList:ArrayList<Int>) {
   UpKan("UpKan",0,arrayListOf(),arrayListOf(),arrayListOf(), arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "UpKan"
        }

        override fun addAutoCurrentNumer() {
            this.currentNumber++
        }

        override fun addPath(path: TaskUpload) {
            this.imageList.add(0,path)
        }

        override fun getPathList(): ArrayList<TaskUpload?> {
            return imageList
        }

       override fun addPath(path: ArrayList<TaskUpload>) {
           imageList.addAll(path)
       }
   },DownKan("DownKan",0,arrayListOf(),arrayListOf(),arrayListOf(),arrayListOf()){
        override fun getCurrentNumber1(): Int {
            return this.currentNumber;
        }

        override fun getPathId(): String {
            return "DownKan"
        }

        override fun addAutoCurrentNumer() {
            this.currentNumber++
        }

        override fun addPath(path: TaskUpload) {
            this.imageList.add(0,path)
        }

        override fun getPathList(): ArrayList<TaskUpload?> {
            return imageList
        }
        override fun addPath(path: ArrayList<TaskUpload>) {
            imageList.addAll(path)
        }
    };

    abstract fun getCurrentNumber1():Int
    abstract fun getPathId():String
    abstract fun addAutoCurrentNumer();
    abstract fun addPath(path:TaskUpload)
    abstract fun addPath(path:ArrayList<TaskUpload>)
    abstract fun getPathList():ArrayList<TaskUpload?>
}