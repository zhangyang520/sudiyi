package com.suntray.chinapost.user.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ListView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.suntray.chinapost.baselibrary.data.bean.RefreshAction
import com.suntray.chinapost.baselibrary.data.dao.UserDao
import com.suntray.chinapost.baselibrary.rx.getTxt
import com.suntray.chinapost.baselibrary.ui.activity.BaseAcvitiy
import com.suntray.chinapost.baselibrary.ui.activity.BaseMvpActivity
import com.suntray.chinapost.baselibrary.ui.refreshView.PullToRefreshBase
import com.suntray.chinapost.baselibrary.utils.SystemUtil
import com.suntray.chinapost.baselibrary.utils.ToastUtil
import com.suntray.chinapost.provider.RouterPath
import com.suntray.chinapost.user.R
import com.suntray.chinapost.user.data.bean.MineReservedDot
import com.suntray.chinapost.user.data.request.MineReservedDotRequest
import com.suntray.chinapost.user.injection.component.DaggerMineComponent
import com.suntray.chinapost.user.presenter.MineDotPresenter
import com.suntray.chinapost.user.presenter.view.MineDotView
import com.suntray.chinapost.user.ui.adapter.MyClientAdapter
import com.suntray.chinapost.user.ui.adapter.MyReservedDotAdapter
import com.suntray.chinapost.user.ui.adapter.MyReservedDotRecylerAdapter
import com.suntray.chinapost.user.ui.dialog.DotRenewDialog
import kotlinx.android.synthetic.main.activity_mine_reserved_dot.*
import kotlinx.android.synthetic.main.layout_search_title.*

/**
 *   我预定的点位的界面
 *  @Author 张扬 @version 1.0
 *  @Date 2018/9/14 17:58
 */
@Route(path = RouterPath.MineModule.MINE_RESERVERD_DOT)
class MineReservedDotActivity : BaseMvpActivity<MineDotPresenter>(),MineDotView{

    var pageNumber=1;
    var pagePreNumber=-1
    var preSearch=""

    var selectedList:Array<String?> ?=null
    override fun initView() {
        isBlackShow=true
        isTitleShow=true
        isRightShow=true
        viewtitle="我预定的点位"
        rightTitle="取消预定"
        basePresenter.mineReservedDot(MineReservedDotRequest(UserDao.getLocalUser().id,pageNumber,10),RefreshAction.NormalAction)


        /**
         * 点击 查询的按钮
         */
        recylerView.setOnRefreshListener(object: PullToRefreshBase.OnRefreshListener<ListView>{
            override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber=1;
                basePresenter.mineReservedDot(MineReservedDotRequest(UserDao.getLocalUser().id,pageNumber,10),RefreshAction.PullDownRefresh);
            }

            override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
                pageNumber+=1
                basePresenter.mineReservedDot(MineReservedDotRequest(UserDao.getLocalUser().id,pageNumber,10),RefreshAction.UpMore);
            }
        })

        rl_bottom.setOnClickListener({
            if(adapter!!.myReservedDotHolder!!.selectPositionList.size>0){
                selectedList=arrayOfNulls<String>(adapter!!.myReservedDotHolder!!.selectPositionList.size)
                for(position in adapter!!.myReservedDotHolder!!.selectPositionList.indices){
                    //获取数值
                    var position=adapter!!.myReservedDotHolder!!.selectPositionList!!.get(position)
                    selectedList!!.set(position,adapter!!.datas.get(position).id.toString())
                }

                //调用接口
                //调用接口
            }else{
                ToastUtil.makeText(this@MineReservedDotActivity,"请选择点位")
            }
        })
    }


    var adapter: MyReservedDotAdapter?=null
    override fun onGetMineReservedDot(reservedDotList: ArrayList<MineReservedDot>,action: RefreshAction) {
        if(action==RefreshAction.NormalAction){
            if(adapter==null){
                adapter = MyReservedDotAdapter(reservedDotList, recylerView.getRefreshableView(),this@MineReservedDotActivity,basePresenter)
                recylerView.getmListView().setAdapter(adapter)
            }else {
                adapter!!.datas = reservedDotList;
                adapter!!.notifyDataSetChanged()
            }
        }else if(action==RefreshAction.PullDownRefresh){
            //下拉刷新
            ToastUtil.makeText(this@MineReservedDotActivity,"刷新完成...")
            if(adapter==null){
                adapter = MyReservedDotAdapter(reservedDotList, recylerView.getRefreshableView(),this@MineReservedDotActivity,basePresenter)

            }else{
                adapter!!.datas=reservedDotList;
            }
            adapter!!.notifyDataSetChanged()
            recylerView.onPullDownRefreshComplete()
        }else if(action==RefreshAction.UpMore){
            //上拉加载更多
            if(reservedDotList.size>0){
                if(adapter==null){
                    adapter = MyReservedDotAdapter(reservedDotList, recylerView.getRefreshableView(),this@MineReservedDotActivity,basePresenter)
                }else{
                    adapter!!.datas.addAll(reservedDotList);
                }
                adapter!!.notifyDataSetChanged()
                ToastUtil.makeText(this@MineReservedDotActivity,"加载更多完成...")
            }else{
                pageNumber-=1
                ToastUtil.makeText(this@MineReservedDotActivity,"没有更多数据了...")
            }
            recylerView.onPullUpRefreshComplete()
        }else if(action==RefreshAction.SearchAction){
            pageNumber=1
            if(adapter==null){
                adapter = MyReservedDotAdapter(reservedDotList, recylerView.getRefreshableView(),this@MineReservedDotActivity,basePresenter)
            }else{
                adapter!!.datas=reservedDotList;
            }
            adapter!!.notifyDataSetChanged()
            ToastUtil.makeText(this@MineReservedDotActivity,"搜索数据加载成功...")
        }
    }

    override fun onError(content: String, action: RefreshAction) {
        if(action==RefreshAction.NormalAction){
            ToastUtil.makeText(this@MineReservedDotActivity,content)
        }else if(action==RefreshAction.PullDownRefresh){
            recylerView.onPullDownRefreshComplete()
            ToastUtil.makeText(this@MineReservedDotActivity,content)
        }else if(action==RefreshAction.UpMore){
            pageNumber-=1
            ToastUtil.makeText(this@MineReservedDotActivity,content)
            recylerView.onPullUpRefreshComplete()
        }else if(action==RefreshAction.SearchAction){
            ToastUtil.makeText(this@MineReservedDotActivity,content)
        }
    }

    override fun onReservedDot() {
        super.onReservedDot()
        SystemUtil.printlnStr("onReservedDot ......adapter!=null:"+(adapter!=null))
        try {
            if(adapter!=null){
                //重新请求
                adapter!!.dismissDilog()
                pageNumber=1
                basePresenter.mineReservedDot(MineReservedDotRequest(UserDao.getLocalUser().id,pageNumber,10),RefreshAction.NormalAction)
                ToastUtil.makeText(this@MineReservedDotActivity,"续约成功!")
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
    override fun injectCompontent() {
        DaggerMineComponent.builder().activityComponent(activityComponent).build().bind(this)
        basePresenter.baseView=this
    }

    override fun getView(): View {
        return View.inflate(this@MineReservedDotActivity, R.layout.activity_mine_reserved_dot,null);
    }

    override fun onResume() {
        super.onResume()
    }

    fun setSelectNumer(number:Int){
        tv_reserved_number.setText(number.toString())
    }

    /**
     * 右边标题的点击 事件处理
     */
    override fun rightTitleClick() {
        super.rightTitleClick()
    }

    override  fun onRelieveSaveResponse(){
        ToastUtil.makeText(this@MineReservedDotActivity,"申请取消预订成功")
    }
}