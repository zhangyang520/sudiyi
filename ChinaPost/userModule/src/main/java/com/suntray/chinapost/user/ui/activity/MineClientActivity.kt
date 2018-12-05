package com.suntray.chinapost.user.ui.activity

import android.content.Intent
import android.view.View
import android.widget.ListView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.rx.hasTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.refreshView.PullToRefreshBase
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineClient
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.MinePresenter
import com.suntray.chinapost.user.presenter.view.MineEditView
import com.suntray.chinapost.user.ui.adapter.MyClientAdapter
import kotlinx.android.synthetic.main.activity_mine_client.*
import kotlinx.android.synthetic.main.layout_search_title.*

/**
 *  我的客户界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/16 10:53
 */
@Route(path = RouterPath.MineModule.MINE_CLIENT)
class MineClientActivity:BaseMvpActivity<MinePresenter>(),MineEditView{

    var pageNumber=1;
    var pagePreNumber=-1
    var preSearch=""
    var adapter:MyClientAdapter?=null
    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }


    override fun onError(content: String, action: RefreshAction) {
        if(action==RefreshAction.NormalAction){
            ToastUtil.makeText(this@MineClientActivity,"暂无客户")
//            et_input_search.setText("")
//            if(adapter!!.datas==null){
//                adapter!!.datas
//            }
//            adapter!!.notifyDataSetChanged()
        }else if(action==RefreshAction.PullDownRefresh){
            recylerView.onPullDownRefreshComplete()
            ToastUtil.makeText(this@MineClientActivity,content)
        }else if(action==RefreshAction.UpMore){
            pageNumber-=1
            ToastUtil.makeText(this@MineClientActivity,content)
            recylerView.onPullUpRefreshComplete()
        }else if(action==RefreshAction.SearchAction){
            ToastUtil.makeText(this@MineClientActivity,content)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SystemUtil.printlnStr("onActivityResult MineClientActivity")
        if((requestCode==100 && resultCode==102)|| (requestCode==100 && resultCode==101)){
            //增加数据成功之后 ，重新请求
            SystemUtil.printlnStr("onActivityResult MineClientActivity")
//            recylerView.scrollBy(0,-500)
            pageNumber=1;
            basePresenter.myClient(UserDao.getLocalUser().id,et_input_search.getTxt().trim(),pageNumber,10,RefreshAction.PullDownRefresh);
        }
    }
    /**
     * 成功数据的回调
     */
    override fun onMineClient(myClientList: ArrayList<MineClient>,action:RefreshAction) {
        if(action==RefreshAction.NormalAction){
             recylerView.setRefreshTitle("我的客户列表,")
            if(adapter==null){
                adapter = MyClientAdapter(myClientList, recylerView.getRefreshableView(),this@MineClientActivity)
                recylerView.getmListView().setAdapter(adapter)
            }else {
                adapter!!.datas = myClientList;
                adapter!!.notifyDataSetChanged()
            }
        }else if(action==RefreshAction.PullDownRefresh){
            //下拉刷新
            ToastUtil.makeText(this@MineClientActivity,"刷新完成...")
            SystemUtil.printlnStr("mineClientlist size:"+myClientList.size)
            if(adapter==null){
               adapter = MyClientAdapter(myClientList, recylerView.getRefreshableView(),this@MineClientActivity)
                recylerView.getmListView().setAdapter(adapter)
            }else{
                adapter!!.datas=myClientList;
            }
            adapter!!.notifyDataSetChanged()
            recylerView.onPullDownRefreshComplete()
        }else if(action==RefreshAction.UpMore){
            //上拉加载更多
            if(myClientList.size>0){
                if(adapter==null){
                    adapter = MyClientAdapter(myClientList, recylerView.getRefreshableView(),this@MineClientActivity)
                    recylerView.getmListView().setAdapter(adapter)
                }else{
                    adapter!!.datas.addAll(myClientList);
                }
                adapter!!.notifyDataSetChanged()
                ToastUtil.makeText(this@MineClientActivity,"加载更多完成...")
            }else{
                pageNumber-=1
                ToastUtil.makeText(this@MineClientActivity,"没有更多数据了...")
            }
            recylerView.onPullUpRefreshComplete()
        }else if(action==RefreshAction.SearchAction){
            pageNumber=1
            if(adapter==null){
                adapter = MyClientAdapter(myClientList, recylerView.getRefreshableView(),this@MineClientActivity)
                recylerView.getmListView().setAdapter(adapter)
            }else{
                adapter!!.datas=myClientList;
            }
            adapter!!.notifyDataSetChanged()
            ToastUtil.makeText(this@MineClientActivity,"搜索数据加载成功...")
        }
    }


    override fun initView() {
        isBlackShow=true
        isTitleShow=false
        isRightShow=false

        et_input_search.setHint("客户名称")
        basePresenter.myClient(UserDao.getLocalUser().id,"",pageNumber,10,RefreshAction.NormalAction);
        /**
         * 点击 查询的按钮
         */
        recylerView.setOnRefreshListener(object:PullToRefreshBase.OnRefreshListener<ListView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber=1;
                basePresenter.myClient(UserDao.getLocalUser().id,et_input_search.getTxt().trim(),pageNumber,10,RefreshAction.PullDownRefresh);
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber+=1
                basePresenter.myClient(UserDao.getLocalUser().id,et_input_search.getTxt().trim(),pageNumber,10,RefreshAction.UpMore);
            }
        })

        //添加客户
        iv_plus.setOnClickListener({
            ARouter.getInstance().build(RouterPath.MineModule.MINE_ADD_CLIENT).navigation(this@MineClientActivity,100)
        })

        //右侧 的搜索出发点
        tv_right.setOnClickListener({
            if(et_input_search.hasTxt() || !preSearch.trim().equals("")){
                preSearch=et_input_search.getTxt()
                pagePreNumber=1
                basePresenter.myClient(UserDao.getLocalUser().id,et_input_search.getTxt().trim(),pagePreNumber,10,RefreshAction.NormalAction);
            }else{
                ToastUtil.makeText(this@MineClientActivity,"请输入内容")
            }
        })

        //取消的点击事件
        rl_map_location_search.setOnClickListener({
            if(et_input_search.hasTxt() || !preSearch.trim().equals("")){
                preSearch=et_input_search.getTxt()
                pagePreNumber=1
                basePresenter.myClient(UserDao.getLocalUser().id,et_input_search.getTxt().trim(),pagePreNumber,10,RefreshAction.NormalAction);
            }else{
                ToastUtil.makeText(this@MineClientActivity,"请输入内容")
            }
        })
    }

    override fun getView(): View {
        return View.inflate(this@MineClientActivity, R.layout.activity_mine_client,null)
    }
}