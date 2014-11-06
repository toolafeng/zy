package com.zy.activity.view;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.my.log.L;
import com.my.pulltorefresh.PullToRefreshBase;
import com.my.pulltorefresh.PullToRefreshBase.Mode;
import com.my.pulltorefresh.PullToRefreshListView;
import com.my.utils.AppUtil;
import com.my.utils.HttpUtil;
import com.my.utils.JsonUtil;
import com.my.utils.StringUtil;
import com.zy.R;
import com.zy.activity.AppDetailActivity;
import com.zy.activity.EvaluatingActivity;
import com.zy.activity.HomeActivity;
import com.zy.activity.IncomeListActivity;
import com.zy.adapter.TaoJingAppAdapter;
import com.zy.config.ZYConstant;
import com.zy.model.GameInfo;
import com.zy.utils.IView;

public class TaoJing extends BaseView implements View.OnClickListener,OnItemClickListener{
	String TAG = "TaoJing";
	SlidingMenu slidingMenu;
	LayoutInflater inflater;
	Context context;
	View container;
	int layout = R.layout.v_main_taojing;
	PullToRefreshListView mPullRefreshListView;
	TaoJingAppAdapter taoJingAppAdapter;
	boolean isLoad = false;
	int pageIndex = 0;
	ArrayList<GameInfo> datas = new ArrayList<GameInfo>();
	ArrayList<GameInfo> datasTemp = new ArrayList<GameInfo>();
	String lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	public TaoJing(Context context,View mainContainer,SlidingMenu slidingMenu) {
		super(context);
		this.context = context;
		this.mainContainer = mainContainer;
		this.slidingMenu = slidingMenu;
		this.inflater = ((Activity) context).getLayoutInflater();
		
		findView();
		init();
	}

	public View getView() {
		return container;
	}
	void findView(){
		this.container = (ViewGroup) inflater.inflate(layout, null);
		View emptyView = container.findViewById(R.id.layout_empty_view);
		emptyView.setOnClickListener(this);
		this.container = (ViewGroup) inflater.inflate(layout, null);
		mPullRefreshListView = (PullToRefreshListView) container.findViewById(R.id.pull_refresh_list);
		
		mPullRefreshListView.setEmptyView(emptyView);
		taoJingAppAdapter = new TaoJingAppAdapter(context,imageLoader);
		mPullRefreshListView.setAdapter(taoJingAppAdapter);
//		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(!mPullRefreshListView.isScrollingWhileRefreshingEnabled());
		mPullRefreshListView.setOnRefreshListener(new com.my.pulltorefresh.PullToRefreshBase.OnRefreshDefaultListener<ListView>() {

			@Override
			public void refresh(PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最近更新"+lastLoadTime);
				pageIndex = 0;
				TaoJing.this.refresh();
			}

			@Override
			public void loadMore(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				TaoJing.this.refresh();
			}

		});
		
		
		container.findViewById(R.id.btn_income_list).setOnClickListener(this);
		container.findViewById(R.id.btn_ceping).setOnClickListener(this);
	}
	public void init(){
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(context instanceof HomeActivity){
			return ((HomeActivity)context).callSuperDispatchTouchEvent(ev);
		}
		return true;
	}

	@Override
	public boolean isLoad() {
		return isLoad;
	}

	public boolean openMore() {
		refresh();
		return true;
	}
	public boolean ceping() {
		Intent intent = new Intent(context, EvaluatingActivity.class);
		AppUtil.startActivity(context, intent);
		return false;
	}
	@Override
	public void onPageSelected(int position) {
		super.onPageSelected(position);
		if(!isLoad()){
			refresh();
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_title_back:
				slidingMenu.toggle();
				break;
			case R.id.btn_title_more:
				openMore();
				break;
			case R.id.btn_ceping:
				ceping();
				break;
			case R.id.layout_empty_view:
				mPullRefreshListView.setRefreshing(true);
				refresh();
				break;
			case R.id.btn_income_list:
				AppUtil.startActivity(context, new Intent(context, IncomeListActivity.class));
				break;
		}
	}
	@Override
	public void refresh(){
		L.i(isDebug, "TAG", "posrUrl   "+getDefaultBaseUrl()+"/game/home.php");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("pageIndex", pageIndex+""); 
        L.i(TAG, "postUrl  "+HttpUtil.getRequestUrl(getDefaultBaseUrl()+"/game/home.php", params));
//        http://app.zhanggame.com/tao/taojin.php
        post(getDefaultBaseUrl()+"/tao/taojin.php", params, responseHandler);
	}
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			closeProgressDialog();
			resetView();
			String result = StringUtil.byte2String(responseBody);
			L.i(TAG,"result   "+StringUtil.byte2String(responseBody));
			JsonObject jsonObject = JsonUtil.getJsonObject(result);
			if(null != jsonObject){
				isLoad = true;
				JsonArray datas_img = JsonUtil.getJsonArray(jsonObject, "datas_img");
				JsonArray datas_tao_tui_img = JsonUtil.getJsonArray(jsonObject, "datas_tao_tui_img");
				JsonArray datas_tao_game = JsonUtil.getJsonArray(jsonObject, "datas_tao_game");
				JsonArray datas_slide_img = JsonUtil.getJsonArray(jsonObject, "datas_slide_img");
				
//				for(int i = 0;i<datas_img.size();i++){
//					
//				}
				if(null != datas_tao_game){
					for(int i = 0;i<datas_tao_game.size();i++){
						datasTemp.clear();
						mappingJson(datasTemp,datas_tao_game);
					}
				}
				
				LinearLayout layout_recommend = (LinearLayout) container.findViewById(R.id.layout_recommend);
				int layout_recommend_v_count = layout_recommend.getChildCount();
				for(int i = 0;i<datas_tao_tui_img.size();i++){
					if(i<=layout_recommend_v_count-1){
						ImageView img = (ImageView)layout_recommend.getChildAt(i);
						JsonObject  obj = JsonUtil.getJsonObject(datas_tao_tui_img, i);
						String imgPath = JsonUtil.getString(obj, "pic");
						String pid = JsonUtil.getString(obj, "pid");
						img.setTag(pid);
//						img.setOnClickListener(gameOnClickListener);
						imageLoader.displayImage(getDefaultBaseUrl()+imgPath, img, ZYConstant.getDefaultDisplayImageOptions());
					}
				}
				
//				
				query();
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,byte[] responseBody, Throwable error) {
			closeProgressDialog();
			resetView();
			L.i(TAG, "onFailure");
		}

		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			super.onProgress(bytesWritten, totalSize);
			L.i(TAG,"onProgress");
		}

		@Override
		public void onFinish() {
			super.onFinish();
			L.i(TAG, "onFinish");
		}

		@Override
		public void onRetry(int retryNo) {
			super.onRetry(retryNo);
			L.i(TAG,"onRetry   retryNo  "+retryNo);
		}

		@Override
		public void onStart() {
			super.onStart();
			L.i(TAG,"onStart");
		}

		@Override
		public void onCancel() {
			super.onCancel();
			L.i(TAG,"onCancel");
		}
		void resetView(){
			
			if(null != mPullRefreshListView){
				mPullRefreshListView.onRefreshComplete();
			}
			lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
		}
		void mappingJson(ArrayList<GameInfo> datasTemp,JsonArray datas_game){
			for(int i = 0;i<datas_game.size();i++){
				GameInfo gameInfo = new GameInfo();
				JsonObject jsonObject = JsonUtil.getJsonObject(datas_game, i);
				
				gameInfo.title = JsonUtil.getString(jsonObject, "gamename");
				gameInfo.summary = JsonUtil.getString(jsonObject, "body");
				gameInfo.imgUrl = JsonUtil.getString(jsonObject, "pic");
				gameInfo.id = JsonUtil.getString(jsonObject, "pid");
				gameInfo.downloadTimes = JsonUtil.getString(jsonObject, "downnums");
				
				gameInfo.downLoadUrl = JsonUtil.getString(jsonObject, "dx");
				gameInfo.price = JsonUtil.getString(jsonObject, "price");
				gameInfo.fileSize = JsonUtil.getString(jsonObject, "size");
				
				gameInfo.introduce = JsonUtil.getString(jsonObject, "body");
				gameInfo.launguage = JsonUtil.getString(jsonObject, "yy");
				gameInfo.versionName = JsonUtil.getString(jsonObject, "versionname");
				gameInfo.versionCode = JsonUtil.getString(jsonObject, "versioncode");
				gameInfo.packageName = JsonUtil.getString(jsonObject, "package");
				gameInfo.payType = JsonUtil.getString(jsonObject, "zf");
				gameInfo.company = JsonUtil.getString(jsonObject, "cp");
				datasTemp.add(gameInfo);
			}
		}
	};
	
	void query(){
		if(pageIndex == 0){
			datas.clear();
		}
		datas.addAll(datasTemp);
		taoJingAppAdapter.setDatas(datas);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		GameInfo gameInfo = (GameInfo) taoJingAppAdapter.getItem(position - 1);
		Log.i("TAG", "gameInfo   " + gameInfo.summary);
		Intent intent = new Intent(context, AppDetailActivity.class);
		intent.putExtra("gameInfo", gameInfo);
		AppUtil.startActivity(context, intent);

	}
}
