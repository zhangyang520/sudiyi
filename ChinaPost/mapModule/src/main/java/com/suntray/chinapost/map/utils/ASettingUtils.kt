package com.suntray.chinapost.map.utils

import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.baselibrary.exception.ContentException
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.ut.base.utils.AppPrefsUtils
import com.suntray.chinapost.baselibrary.utils.DateUtil
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.map.data.constants.MapContstants
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList

/**
 *  设置 弹出框相关设置
 *  @Author 张扬 @version 1.0
 *  @Date 2018/10/16 11:13
 */
object ASettingUtils {

    /**
     * 得到String形式的数组形式
     * @param uploadList
     * @return
     */
    @Throws(ContentException::class)
    fun getIntList(uploadList: MutableList<ResourceType>?): String {
        if (uploadList != null) {
            val stringList = ArrayList<Int>()
            for (bean in uploadList) {
                stringList.add(bean.id)
            }
            try{
                SystemUtil.printlnStr("Arrays.toString(stringList.toTypedArray()):"+Arrays.toString(stringList.toTypedArray()))
                return Arrays.toString(stringList.toTypedArray())
//                return URLEncoder.encode(Arrays.toString(stringList.toTypedArray()), "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
        throw ContentException()
    }

    /**
     * 获取解析的内容
     */
    fun getDecoderString(contentString:String):MutableList<Int>{
//        var idsArrays= URLDecoder.decode(contentString,"UTF-8");
        var list=contentString.subSequence(1,contentString.length-1).split(", ")
        var idList= mutableListOf<Int>()
        for(data in list){
            idList.add(data.toInt())
        }
        return idList;
    }

    /**
     * 清空设置条件
     */
    fun clearSetting(){
        AppPrefsUtils.putInt(MapContstants.SETTING_KEYWORDINDEX,0)
        AppPrefsUtils.putInt(MapContstants.SETTING_ADTYPEID,-1)
        AppPrefsUtils.putString(MapContstants.SETTING_STARTTIME, DateUtil.dateFormat(Calendar.getInstance().time))
        AppPrefsUtils.putString(MapContstants.SETTING_ENDTIME,DateUtil.dateFormat(Calendar.getInstance().time))
        AppPrefsUtils.putString(MapContstants.SETTING_RESOURCEIDS,"[]")
    }
}