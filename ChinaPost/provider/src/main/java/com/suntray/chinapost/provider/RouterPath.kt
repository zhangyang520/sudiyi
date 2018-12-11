package com.suntray.chinapost.provider

/**
 *   页面的路由的路径
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/11 13:32
 */
object RouterPath {

    /**
     * 地图的模块
     */
    class MapModule{
        companion object {
            //关键字搜索
            const val POI_KEYWORD_SEARCH = "/map/PoiKeywordSearchActivity";

            //周边搜索
            const val POI_AROUND = "/map/PoiAroundSearchActivity"

            //邮政快递的 地图界面
            const val POST_POI_SEARCH = "/map/PostPoiSearchActivity"

            //地图的广告资源位 界面
            const val POST_AD_INFO = "/map/PostAdInfoActivity";

            //地图 资源预定的界面
            const val POST_AD_RESOURCE_RESERVED = "/map/PostAdResourceReservedActivity"

            //下刊资源图
            const val POST_AD_DOWN = "/map/PostAdDownActivity"

            //预定 资源列表界面
            const val POST_AD_RESERVED_LIST="/map/PostAdReservedListActivity"

            //预定 资源列表界面
            const val POST_AD_CHECK_DATE="/map/PostAdCheckDateActivity"

            //一键预定的界面
            const val POST_AD_ONE_KEY_RESERVED="/map/PostAdOneKeyReservedActivity"

            //一键预定的结果界面
            const val POST_AD_RESERVED_RESULT="/map/PostReservedAdResultActivity"

            //任务列表的activity
            const val POST_TASK_LIST="/map/TaskListActivity"

            //任务的详情页
            const val POST_TASK_DETAIL="/map/TaskDetailActivity"

            //路线的类
            const val POST_TASK_ROUTE="/map/RouteActivity"

            //单个导航 驾车路线规划
            const val POST_SINGLE_ROUTE_CALCULATE_ACTIVITY="/map/SingleRouteCalculateActivity"

            //步行导航的界面
            const val POST_WALK_ROUTE_CALCULATE_ACTIVITY="/map/WalkRouteCalculateActivity"

            //骑行导航的界面
            const val POST_RIDE_ROUTE_CALCULATE_ACTIVITY="map/RideRouteCalculateActivity"
        }
    }
    class AppModule{
        companion object {
            //登录界面
            const val LOGIN_ACTIVITY = "/app/LoginActivity";
        }
    }

    /**
     * 我的的模块
     */
    class MineModule{
        companion object {
            //我的模块
            const val MINE_ACTIVITY="/mine/MineActivity"

            //我的 点位资源 预定界面
            const val MINE_RESERVERD_DOT="/mine/MineReservedDotActivity"

            //我的客户的界面
            const val MINE_CLIENT="/mine/MineClientActivity";

            //客户增加的界面
            const val MINE_ADD_CLIENT="/mine/MineAddClientActivity"

            //上传的资质的界面
            const val MINE_UPLOAD_APTITUDE="/mine/UploadAptitudeActivity";

            //系统消息的界面
            const val MINE_MESSAGE="/mine/MineMessageActivity"

            //审批通知的界面
            const val MINE_EXAMINE_ENDORSE="/mine/MineExamineEndorseActivity";

            //修改密码界面
            const val MINE_EDIT_PWD="/mine/MineEditPwdActivity";

            //客户的详情页
            const val MINE_CLIENT_DETAIL="/mine/MineClientDetail"
        }
    }
}