package com.zy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.my.log.L;
import com.my.pulltorefresh.PullToRefreshBase;
import com.my.pulltorefresh.PullToRefreshBase.Mode;
import com.my.pulltorefresh.PullToRefreshListView;
import com.my.utils.AppMsg;
import com.my.utils.AppUtil;
import com.my.utils.HttpUtil;
import com.my.utils.JsonUtil;
import com.my.utils.StringUtil;
import com.zy.R;
import com.zy.activity.view.ZhangYou;
import com.zy.adapter.MainGameAdapter;
import com.zy.model.GameInfo;
import com.zy.utils.IView;
/**
 * 
* @ClassName: APPActivity 
* @Description: TODO(APP应用列表) 
* @author LEE 
* @date Jul 21, 2014 11:33:32 PM 
*
 */
public class APPActivity extends ZYBaseActivity implements View.OnClickListener,OnItemClickListener{
	String TAG = "APPActivity";
	String lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	int pageIndex = 0;
	PullToRefreshListView mPullRefreshListView;
	MainGameAdapter gameAdapter;
	String appType;
	ArrayList<GameInfo> datas = new ArrayList<GameInfo>();
	ArrayList<GameInfo> datasTemp = new ArrayList<GameInfo>();
	View container;
	Button btn_title_back;
	String title = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(null != savedInstanceState){
			appType = savedInstanceState.getString("appType");
			title = savedInstanceState.getString("title");
		}else{
			appType = getIntent().getStringExtra("appType");
			title = getIntent().getStringExtra("title");
		}
		container = getLayoutInflater().inflate(R.layout.activity_app, null);
		setContentView(container);
		findView();
		refresh();
	}
	void findView(){
		gameAdapter = new MainGameAdapter(this,imageLoader);
		if(StringUtil.stringIsNotEmpty(title)){
			((TextView)findViewById(R.id.tv_title)).setText(title);
		}else{
			((TextView)findViewById(R.id.tv_title)).setText("游戏");
		}
		btn_title_back = (Button) findViewById(R.id.btn_title_back);
		btn_title_back.setOnClickListener(this);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		View emptyView = findViewById(R.id.layout_empty_view);
		emptyView.setOnClickListener(this);
		mPullRefreshListView.setEmptyView(emptyView);
		mPullRefreshListView.setAdapter(gameAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(!mPullRefreshListView.isScrollingWhileRefreshingEnabled());
		mPullRefreshListView.setOnRefreshListener(new com.my.pulltorefresh.PullToRefreshBase.OnRefreshDefaultListener<ListView>() {

			@Override
			public void refresh(PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最近更新"+lastLoadTime);
				pageIndex = 0;
				APPActivity.this.refresh();
			}

			@Override
			public void loadMore(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				APPActivity.this.refresh();
			}

		});
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("appType", appType);
		outState.putString("title", title);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(null != savedInstanceState){
			appType = savedInstanceState.getString("appType");
			title = savedInstanceState.getString("title");
		}
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		GameInfo gameInfo = (GameInfo) gameAdapter.getItem(arg2);
		Log.i("TAG", "gameInfo   "+gameInfo.summary);
		Intent intent = new Intent(this, AppDetailActivity.class);
		intent.putExtra("gameInfo", gameInfo);
		AppUtil.startActivity(this, intent);
		
	}
	public void refresh(){
		L.i("TAG", "posrUrl   "+getDefaultBaseUrl()+"/game/home.php");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("pageIndex", pageIndex+""); 
        L.i(TAG, "postUrl  "+HttpUtil.getRequestUrl(getDefaultBaseUrl()+"/game/home.php", params));
        post(getDefaultBaseUrl()+"/game/home.php", params, responseHandler);
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
				JsonArray datas_img = JsonUtil.getJsonArray(jsonObject, "datas_img");
				JsonArray datas_game = JsonUtil.getJsonArray(jsonObject, "datas_game");
//				for(int i = 0;i<datas_img.size();i++){
//					
//				}
				
				for(int i = 0;i<datas_game.size();i++){
					datasTemp.clear();
					mappingJson(datasTemp,datas_game);
				}
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
				gameInfo.downloadTimes = i+"";
				gameInfo.downLoadUrl = JsonUtil.getString(jsonObject, "dx");
				gameInfo.price = (5+i)+"";
				gameInfo.fileSize = (20*i)+"";
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
		gameAdapter.setDatas(datas);
	}
	
}
