package com.suntray.chinapost.user.data.bean

import java.io.Serializable

/**
 *   我的消息的界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/20 19:48
 */
class MineMessage:Serializable{
    var id:Int=0
    var title:String=""
    var content=""
    var type:Int=0
    var senddate=""
    var isnew=1

    override fun toString(): String {
        return "MineMessage(id=$id, title='$title', content='$content', type=$type, messageDate='$senddate', isnew=$isnew)"
    }

}