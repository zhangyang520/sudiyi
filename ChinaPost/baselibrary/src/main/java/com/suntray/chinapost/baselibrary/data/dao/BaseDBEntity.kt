package com.suntray.chinapost.baselibrary.data.dao

import com.lidroid.xutils.db.annotation.Id
import com.lidroid.xutils.db.annotation.NoAutoIncrement

/**
 * 数据库的基础类
 */
open class BaseDBEntity {

    @Id
    lateinit var id:Integer
}