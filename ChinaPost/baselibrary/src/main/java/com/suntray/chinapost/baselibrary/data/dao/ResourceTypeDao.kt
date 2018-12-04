package com.suntray.chinapost.baselibrary.data.dao

import com.lidroid.xutils.db.sqlite.Selector
import com.lidroid.xutils.exception.DbException
import com.suntray.chinapost.baselibrary.common.BaseApplication
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.DistrictType
import com.suntray.chinapost.baselibrary.data.bean.ResourceType
import com.suntray.chinapost.baselibrary.exception.ContentException

/**
 * 用户相关的dao
 * @Author 张扬 @version 1.0
 * @Date 2018/6/15 15:42
 */
object ResourceTypeDao {


    /**
     * 删除所有的数据
     */
    fun deleteAll(){
        try {
            BaseApplication.dbUtils.deleteAll(ResourceType::class.java)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException()
        }
    }


    /**
     * 查找数据
     * @param
     * @return
     */
    @Throws(ContentException::class)
    fun getall(): List<ResourceType>
    {
            try {
                val datas = BaseApplication.dbUtils.findAll(ResourceType::class.java)
                if (datas == null || datas.size == 0) {
                    throw ContentException("没有用户列表!")
                }
                return datas
            } catch (e: DbException) {
                e.printStackTrace()
                throw RuntimeException()
            }
     }

    /**
     * 保存所有数据
     * @param user
     */
    fun saveAll(user: ResourceType) {
        try {
            BaseApplication.dbUtils.save(user)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException("保存数据失败!")
        }

    }


    /**
     * 保存更新数据
     * @param user
     */
    fun saveUpDate(user: ResourceType) {
        try {
            BaseApplication.dbUtils.saveOrUpdate(user)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException("保存数据失败!")
        }
    }

    /**
     *  保存 所有的数据
     */
    fun saveUpDate(user: ArrayList<ResourceType>) {
        try {
            BaseApplication.dbUtils.saveAll(user)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException("保存数据失败!")
        }

    }

    /**
     * 得到User对象的UserId
     * @param userId
     * @return
     */
    @Throws(ContentException::class)
    fun getDistrictType(id: Int): ResourceType {
        try {
            val datas = BaseApplication.dbUtils.findAll<ResourceType>(Selector.from(ResourceType::class.java).where("id", "=", id))
            return if (datas!=null && datas.size > 0) {
                datas[0]
            } else {
                throw ContentException(BaseConstants.NO_LOGIN_TYPE)
            }
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException()
        }
    }
}
