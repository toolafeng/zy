package com.zy.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.my.pulltorefresh.PullToRefreshBase;
import com.my.pulltorefresh.PullToRefreshBase.Mode;
import com.my.pulltorefresh.PullToRefreshListView;
import com.my.utils.AppMsg;
import com.zy.R;
import com.zy.adapter.TalkAdapter;
import com.zy.model.Talk;

/**
 * 
* @ClassName: TalkActivity 
* @Description: TODO(大家都在说) 
* @author LEE 
* @date Jul 27, 2014 1:25:42 AM 
*
 */
public class TalkActivity extends Activity implements View.OnClickListener,OnItemClickListener {
	String lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	int pageIndex = 0;
	PullToRefreshListView mPullRefreshListView;
	String appType;
	ArrayList<Talk> datas = new ArrayList<Talk>();
	TalkAdapter talkAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_talk);
		findView();
		refresh();
	}

	void findView(){
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		((TextView)findViewById(R.id.tv_title)).setText("大家都在说");
		talkAdapter = new TalkAdapter(this);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		View emptyView = findViewById(R.id.layout_empty_view);
		emptyView.setOnClickListener(this);
		mPullRefreshListView.setEmptyView(emptyView);
		mPullRefreshListView.setAdapter(talkAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(!mPullRefreshListView.isScrollingWhileRefreshingEnabled());
		mPullRefreshListView.setOnRefreshListener(new com.my.pulltorefresh.PullToRefreshBase.OnRefreshDefaultListener<ListView>() {

			@Override
			public void refresh(PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最近更新"+lastLoadTime);
				pageIndex = 0;
				new MyTask().execute();				
			}

			@Override
			public void loadMore(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				new MyTask().execute();
			}

		});
		findViewById(R.id.btn_title_back).setOnClickListener(this);
	}
	public void refresh(){
		pageIndex = 0;
		if(talkAdapter.isEmpty()){
			mPullRefreshListView.postDelayed(new Runnable() {
				@Override
				public void run() {
					mPullRefreshListView.setRefreshing(true);
					new MyTask().execute();
				}
			}, 200);
		}
	}
	class MyTask extends AsyncTask<Void, Void, Void>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(null == TalkActivity.this) return;
			resetView();
			if(pageIndex>0) pageIndex--;
			AppMsg.showToast(TalkActivity.this, "load   "+pageIndex);
			query();
		}
		
		void resetView(){
			if(null != mPullRefreshListView){
				mPullRefreshListView.onRefreshComplete();
			}
			lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
		}
		void query(){
			datas.addAll(getData());
			talkAdapter.setDatas(datas);
		}
		ArrayList<Talk> getData(){
			ArrayList<Talk> datas = new ArrayList<Talk>();
			for(int i = 0;i<20;i++){
				Talk gameInfo = new Talk();
				gameInfo.title = "儿童节我在马路边上检到了1分钱,大家说下该怎么办啊"+i;
				gameInfo.summary = "好高兴呀,好高兴呀,好高兴呀,好高兴呀,好高兴呀,"+i;
				datas.add(gameInfo);
			}
		    return datas;  
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_title_back:
			this.finish();
			break;
		}
	}

}
