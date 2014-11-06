package com.zy.activity;

import java.sql.SQLException;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.my.pulltorefresh.PullToRefreshBase;
import com.my.pulltorefresh.PullToRefreshBase.Mode;
import com.my.pulltorefresh.PullToRefreshListView;
import com.my.utils.AppUtil;
import com.zy.R;
import com.zy.adapter.DownLoadInfoAdapter;
import com.zy.model.DownLoadInfo;

public class DwonloadListActivity extends ZYBaseActivity implements OnClickListener,OnItemClickListener{
	PullToRefreshListView mPullRefreshListView;
	DownLoadInfoAdapter downLoadInfoAdapter;
	String lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	ArrayList<DownLoadInfo> datas = new ArrayList<DownLoadInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dwonloadlist);
		findView();
		init();
		refresh();
	}
	void findView(){
		mPullRefreshListView = (PullToRefreshListView)findViewById(R.id.pull_refresh_list);
	}
	void init(){
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		((TextView)findViewById(R.id.tv_title)).setText("下载列表");
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		View emptyView = findViewById(R.id.layout_empty_view);
		emptyView.setOnClickListener(this);
		mPullRefreshListView.setEmptyView(emptyView);
		downLoadInfoAdapter = new DownLoadInfoAdapter(this,imageLoader);
		mPullRefreshListView.setAdapter(downLoadInfoAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(!mPullRefreshListView.isScrollingWhileRefreshingEnabled());
		mPullRefreshListView.setOnRefreshListener(new com.my.pulltorefresh.PullToRefreshBase.OnRefreshDefaultListener<ListView>() {

			@Override
			public void refresh(PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最近更新"+lastLoadTime);
				DwonloadListActivity.this.refresh();
				resetView();
			}

			@Override
			public void loadMore(PullToRefreshBase<ListView> refreshView) {
				DwonloadListActivity.this.refresh();
				resetView();
			}

		});
		mPullRefreshListView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				refresh();
			}
		}, 200);
	}
	void refresh(){
		try {
			datas.clear();
			datas.addAll(dbHelper.getMyDao(DownLoadInfo.class).queryForAll());
			downLoadInfoAdapter.setDatas(datas);
			resetView();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	void resetView(){
		
		if(null != mPullRefreshListView){
			mPullRefreshListView.onRefreshComplete();
		}
		lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.layout_empty_view:
			mPullRefreshListView.setRefreshing(true);
			refresh();
			break;
		case R.id.btn_title_back:
			AppUtil.finishActivity(this);
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
	}
}
