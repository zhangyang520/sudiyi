package com.suntray.chinapost.baselibrary.common

/**
 * Created by zhangyang on 2018/5/17 16:46.
 * 基础常量类
 * version 1
 */
object BaseConstants {

        /**
         * 请求失败展示信息
         */
        val ERROR_MESSAGE = "请求失败，请稍后再试"
        var SUCCESS = "0";//成功的状态

        //http://localhost:8080/admin/mobile/   http://localhost:8090/
        var BASE_URL = "http://47.94.207.62:19999/admin/mobile/"//生产环境1010
        var BASE_UPLOAD_URL="http://47.94.207.62:19999/admin/"
        var BASE_DOWNLOAD_URL="http://47.94.207.62:19999/admin/"
        var DEFALUT_PRINT_VALUE = "b"//缺省的当前是否打印的变量
        var CURRENT_PRINT_VALUE = "a"//当前的打印的值
        val versionCode = 10

        //登录相关的url
        const val LOGIN = "login.do"
        const val EDIT_PWD="updatePwd.do"
        const val UPLOAD_IMG="uploadHeadImg.do"
        const val MY_CLIENT="findMyClient.do"

        const val MY_MESSAGE="findNoticeList.do"
        const val VIEW_MESSAGE="viewNotice.do"

        //客户的相关的api
        const val ADD_CLIENT="saveMyClientBase.do" //保存 客户信息
        const val FIND_CLIENT="findClientBase.do"//查找客户信息
        const val FIND_APTITUDE="findClientQua.do"// 查找客户对应的资质
        const val SAVE_APTITUDE_IMG="saveMyClientQuaImage.do"//保存资质的图片
        const val SAVE_CLINET_INFO="saveMyClientQua.do"// 保存客户信息

        const val UPLOAD_ALL_APTITUDE_IMAGE="saveMyClientQua.do"//上传所有的图片
        const val GET_ALL_UPLOAD_APTITUDE="findClientQua.do" //通过 客户的id  查找资质信息

        //地图模块的相关接口
        const val PROVINCE_DOT="findPointByDistrict.do"
        const val RADIUS_DOT="findPointByRadius.do"
        const val PROVINCE="findAreaList.do"

        //提交一键预定  toSubmitReserve
        const val ONE_KEY_SAVE_SUBMIT_RESULT="toSubmitReserve.do"
        const val ONE_KEY_SUBMIT="submitReserve.do"
        const val DOT_OF_RESOURCE_LIST="findResource.do"
        const val SAVE_SUBMIT_RESERVE="saveSubmitReserve.do"
        const val RESOURCE_DATE="resourceSchedule.do"//日期的周期
        const val FindResourceReport="findResourceReport.do"; //通过 id 查看 上下刊报告

        //字典接口
        const val RESOURCE_LSIT="findResourceTypeDic.do"
        const val AD_TYPE_LIST="findAdTypeDic.do"
        const val CLIENT_NAME_LIST="findClientDic.do"
        const val DISTRICT_TYPE_LIST="findZoneTypeDic.do" //小区类型

        const val MINE_RESERVED_DOT="findMyReserve.do"
        const val MINE_XUDING_DOT="renewSave.do" //点位续订
        const val RELIEVE_SAVE="relieveSave.do"//申请取消预订

        const val UPDATE_APP="getNewApp.do"//更新版本

        const val GET_TASK_LIST="PointTasks.do"//获取 任务列表 PointTasks
        const val GET_TASK_LIST_2="pointTasksA.do"//第二个接口
        const val TASK_COUNT_NUMBER="getPointTaskCounts.do";//任务数量
        const val UPLOAT_TASK_IMG="uploadScenePhotos.do" //上传 任务的图片
        val MACHINE_ID = "MACHINE_ID"

        /**
         * 一些标识!
         */
        const val SCORE_TYPE_LIULIDU = 0//流利度类型
        const val SCORE_TYPE_ZHUNQUEDU = 1//准确度类型
        const val SCORE_TYPE_WAN_ZHENG_DU = 2//准确度类型
        const val SCORE_TYPE_ZONGFEN = 3//总分
        const val TIME_TYPE_SECOND = 4
        const val LIULIDU = "流利度: "
        const val ZHUN_QUE_DU = "准确度: "
        const val WAN_ZHENG_DU = "完整度: "
        const val ZONG_FEN = "总分: "

        const val EMPTY_SPACE = ""
        //下载中的进度条中的常量
        const val SUCCESS_RESULT_PROGRESS = "试题加载成功,点击进入"

        /**
         * 对应的UI的样式！
         * （4级考试：0自我介绍 1短文朗读 2问答资源列表3个人陈述4小组互动）
         * （6级考试：5自我介绍 6简短问答 7陈述试题 8讨论实体 9问答试题）
         * (10四级模拟  11六级模拟)
         */
        val UI_INTRODUCE_TYPE = 0//ui 的 四级的自我介绍
        val UI_DUANWEN_LANGDU_TYPE = 1//短文朗读类型
        val UI_SIJI_WENDA_TYPE = 2//四级问答形式
        val UI_PRESENTER_TYPE = 3//四级个人陈述类型
        val UI_INTERACTION_TYPE = 4//小组互动的形式
        val UI_SIJI_MONI_TYPE = 10//四级模拟
        val UI_LIUJI_PRESENTER_TYPE = 7//六级的陈述类型

        val RESULT_CODE_LOGIN_ERROR = "0001"//登录时候的错误码
        //目录的常量
        val ROOTPATH = "chinaPost"
        val picCache = "picCache"
        val zipDir = "zipDir"
        val uploadPic = "uploadPic"
        val downloadDir = "downloadDir"
        val recordPathDir = "recordPathDir"
        val debugPath = "debugPath"
        val newTaskPath = "newTaskPath"
        //进行控制是否将文件保存在sd卡中
        val IS_SD_CAN = true

        /**
         * 需要解析类型的判断
         */
        //权限的请求吗
        val CAMERA = 101
        val WRITE_EXTERNAL_STORAGE = 102
        val REDA_EXTERNAL_STORAGE = 103

        /***
         * 模块的分类型
         */
        val STRING_GAP = "--------"//字符串的分割符
        //最大的进度的值
        val MAX_PROGRESS = 1000
        //问题
        val QUESTION_STRING = "Question "

        val XIAOMI_TIME_WUCHA = 500//小米手机的时间的误差


        val YUNXIN_VOLUME_START_VALUE = 100//云信的相关的初始化值
        val YUNXIN_VOLUME_CHAZHI = 600//云信的相关的差值

        //友盟统计的点击事件的ID
        val CLASS_FOUR_JI = "0"
        val KEY_SP_TOKEN: String = "KEY_SP_TOKEN"
        val TABLE_PREFS: String = "TABLE_PREFS"
        val NO_LOGIN_TYPE: Int=101;

        var SELECTEDROLEINDEX=4  //当前账户对应的选择的 类型
        var DB_VERSION=11 //增加了 orgId
}