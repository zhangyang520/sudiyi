package com.suntray.chinapost.user.ui.activity

import android.view.View
import android.widget.ListView
import com.alibaba.android.arouter.facade.annotation.Route
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseAcvitiy
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.refreshView.PullToRefreshBase
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineMessage
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.MinePresenter
import com.suntray.chinapost.user.presenter.view.MineEditView
import com.suntray.chinapost.user.ui.adapter.MyClientAdapter
import com.suntray.chinapost.user.ui.adapter.MyMessageAdapter
import kotlinx.android.synthetic.main.activity_message.*

/**
 *  系统消息的界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/16 16:55
 */
@Route(path = RouterPath.MineModule.MINE_MESSAGE)
class MineMessageActivity:BaseMvpActivity<MinePresenter>(),MineEditView{

    var pageNumber=1;
    var pagePreNumber=-1
    var adapter: MyMessageAdapter?=null

    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun initView() {
        isBlackShow=true
        isTitleShow=true
        isRightShow=false
        viewtitle="系统消息"

        /**
         * 点击 查询的按钮
         */
        recylerView.setOnRefreshListener(object: PullToRefreshBase.OnRefreshListener<ListView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber=1;
                basePresenter.myMessage(UserDao.getLocalUser().id,pageNumber,10,RefreshAction.PullDownRefresh);
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber+=1
                basePresenter.myMessage(UserDao.getLocalUser().id,pageNumber,10,RefreshAction.UpMore);
            }
        })

    }

    override fun onResume() {
        super.onResume()
        pageNumber=1
        basePresenter.myMessage(UserDao.getLocalUser().id,pageNumber,10,RefreshAction.NormalAction);
    }

    override fun onError(content: String, action: RefreshAction) {
        if(action==RefreshAction.NormalAction){
            ToastUtil.makeText(this@MineMessageActivity,content)
        }else if(action==RefreshAction.PullDownRefresh){
            recylerView.onPullDownRefreshComplete()
            ToastUtil.makeText(this@MineMessageActivity,content)
        }else if(action==RefreshAction.UpMore){
            pageNumber-=1
            ToastUtil.makeText(this@MineMessageActivity,content)
            recylerView.onPullUpRefreshComplete()
        }else if(action==RefreshAction.SearchAction){
            ToastUtil.makeText(this@MineMessageActivity,content)
        }
    }


    override fun onMineMessage(myClientList: ArrayList<MineMessage>, action: RefreshAction) {
        super.onMineMessage(myClientList, action)
        if(action==RefreshAction.NormalAction){
            recylerView.setRefreshTitle("我的客户列表,")
            if(adapter==null){
                adapter = MyMessageAdapter(myClientList, recylerView.getRefreshableView(),this@MineMessageActivity)
                recylerView.getmListView().setAdapter(adapter)
            }else {
                adapter!!.datas = myClientList;
                adapter!!.notifyDataSetChanged()
            }
        }else if(action==RefreshAction.PullDownRefresh){
            //下拉刷新
            ToastUtil.makeText(this@MineMessageActivity,"刷新完成...")
            if(adapter==null){
                adapter = MyMessageAdapter(myClientList, recylerView.getRefreshableView(),this@MineMessageActivity)
            }else{
                adapter!!.datas=myClientList;
            }
            adapter!!.notifyDataSetChanged()
            recylerView.onPullDownRefreshComplete()
        }else if(action==RefreshAction.UpMore){
            //上拉加载更多
            if(myClientList.size>0){
                if(adapter==null){
                    adapter = MyMessageAdapter(myClientList, recylerView.getRefreshableView(),this@MineMessageActivity)
                }else{
                    adapter!!.datas.addAll(myClientList);
                }
                adapter!!.notifyDataSetChanged()
                ToastUtil.makeText(this@MineMessageActivity,"加载更多完成...")
            }else{
                pageNumber-=1
                ToastUtil.makeText(this@MineMessageActivity,"没有更多数据了...")
            }
            recylerView.onPullUpRefreshComplete()
        }
    }


    override fun getView(): View {
        return View.inflate(this@MineMessageActivity,R.layout.activity_message,null)
    }
}