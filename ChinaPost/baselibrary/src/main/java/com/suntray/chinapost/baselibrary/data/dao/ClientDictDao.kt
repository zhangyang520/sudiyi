package com.suntray.chinapost.baselibrary.data.dao

import com.lidroid.xutils.db.sqlite.Selector
import com.lidroid.xutils.exception.DbException
import com.suntray.chinapost.baselibrary.common.BaseApplication
import com.suntray.chinapost.baselibrary.common.BaseConstants
import com.suntray.chinapost.baselibrary.data.bean.ClientDict
import com.suntray.chinapost.baselibrary.exception.ContentException

/**
 * 用户相关的dao
 * @Author 张扬 @version 1.0
 * @Date 2018/6/15 15:42
 */
object ClientDictDao {

    /**
     * 查找数据
     * @param
     * @return
     */
    @Throws(ContentException::class)
    fun getall(): List<ClientDict>
    {
            try {
                val datas = BaseApplication.dbUtils.findAll(ClientDict::class.java)
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
     * 删除所有的数据
     */
    fun deleteAll(){
        try {
          BaseApplication.dbUtils.deleteAll(ClientDict::class.java)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException()
        }
    }
    /**
     * 保存所有数据
     * @param user
     */
    fun saveAll(clientDict: ClientDict) {
        try {
            BaseApplication.dbUtils.save(clientDict)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException("保存数据失败!")
        }

    }


    /**
     * 保存更新数据
     * @param user
     */
    fun saveUpDate(clientDict: ClientDict) {
        try {
            BaseApplication.dbUtils.saveOrUpdate(clientDict)
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException("保存数据失败!")
        }

    }
    /**
     * 保存更新数据
     * @param user
     */
    fun saveUpDate(clientDict: ArrayList<ClientDict>) {
        try {
            BaseApplication.dbUtils.saveAll(clientDict)
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
    fun getClient(name: String): ClientDict {
        try {
            val datas =
                    BaseApplication.dbUtils.findAll<ClientDict>(Selector.from(ClientDict::class.java).where("name", "=", name))
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

    /**
     * 得到User对象的UserId
     * @param userId
     * @return
     */
    @Throws(ContentException::class)
    fun getClient(id: Int): ClientDict {
        try {
            val datas =
                    BaseApplication.dbUtils.findAll<ClientDict>(Selector.from(ClientDict::class.java).where("id", "=", id))
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

    /**
     * 通过 pid 查找 客户相关字段的集合
     */
    fun getClients(pid:Int):MutableList<ClientDict>{
        try {
            val datas =
                    BaseApplication.dbUtils.findAll<ClientDict>(Selector.from(ClientDict::class.java).where("pid", "=", pid))
             if (datas!=null && datas.size > 0) {
                 return  datas!!
            } else {
                throw ContentException(BaseConstants.NO_LOGIN_TYPE)
            }
        } catch (e: DbException) {
            e.printStackTrace()
            throw RuntimeException()
        }
    }
}
