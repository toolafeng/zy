package com.zy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.zy.adapter.MyInComeInfoAdapter;
import com.zy.model.InComeInfo;

/**
 * 
* @ClassName: MyIncomeActivity 
* @Description: TODO(我的收入) 
* @author LEE 
* @date Jul 22, 2014 12:43:00 AM 
*
 */
public class MyIncomeActivity extends ZYBaseActivity implements View.OnClickListener,OnItemClickListener{
	PullToRefreshListView mPullRefreshListView;
	MyInComeInfoAdapter comeInfoAdapter;
	int pageIndex = 0;
	String TAG = MyIncomeActivity.class.getName();
	String lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myincome);
		findView();
	}
	void findView(){
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("我的收入");
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		View emptyView = findViewById(R.id.layout_empty_view);
		emptyView.setOnClickListener(this);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setEmptyView(emptyView);
		comeInfoAdapter = new MyInComeInfoAdapter(this);
		mPullRefreshListView.setAdapter(comeInfoAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(!mPullRefreshListView.isScrollingWhileRefreshingEnabled());
		mPullRefreshListView.setOnRefreshListener(new com.my.pulltorefresh.PullToRefreshBase.OnRefreshDefaultListener<ListView>() {
			@Override
			public void refresh(PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最近更新"+lastLoadTime);
				pageIndex = 0;
				MyIncomeActivity.this.refresh();
			}

			@Override
			public void loadMore(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				MyIncomeActivity.this.refresh();
			}
		});
		refresh();
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_title_back:
				AppUtil.finishActivity(this);
				break;
			case R.id.btn_title_more:
				AppUtil.startActivity(this, new Intent(this, MyIncomeActivity.class));
				break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
	public void refresh(){
		L.i(TAG, "posrUrl   "+getDefaultBaseUrl()+"/game/home.php");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("pageIndex", pageIndex+""); 
        L.i(TAG, "postUrl  "+HttpUtil.getRequestUrl(getDefaultBaseUrl()+"/game/home.php", params));
//        http://app.zhanggame.com/tao/my_shouru.php
        post(getDefaultBaseUrl()+"/tao/my_shouru.php", params, responseHandler);
	}
	void query(ArrayList<InComeInfo> list){
		if(pageIndex == 0){
			comeInfoAdapter.getDatas().clear();
		}
		comeInfoAdapter.setDatas(list);
	}
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			closeProgressDialog();
			resetView();
			String result = StringUtil.byte2String(responseBody);
			L.i(TAG,"result   "+StringUtil.byte2String(responseBody));
			if(StringUtil.stringIsNotEmpty(result)){
				query(mappingJson(result));
				if(StringUtil.stringIsEmpty(result)) return;
				JsonObject jsonObject = JsonUtil.getJsonObject(result);
				if(null == jsonObject) return;
				try{
					JsonArray datas_my_xinxi = jsonObject.get("datas_my_xinxi").getAsJsonArray();
					JsonObject datas_my_xinxiObj = datas_my_xinxi.get(0).getAsJsonObject();
					((TextView)findViewById(R.id.tv_uname)).setText(JsonUtil.getString(datas_my_xinxiObj, "uname"));
					((TextView)findViewById(R.id.tv_school)).setText(JsonUtil.getString(datas_my_xinxiObj, "school"));
					((TextView)findViewById(R.id.tv_zongshouru)).setText(JsonUtil.getString(datas_my_xinxiObj, "zongshouru"));
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{
				AppMsg.showToast(MyIncomeActivity.this, "未查到收入明细");
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
		ArrayList<InComeInfo> mappingJson(String result){
			ArrayList<InComeInfo> datasTemp = new ArrayList<InComeInfo>();
			if(StringUtil.stringIsEmpty(result)) return datasTemp;
			JsonObject jsonObject = JsonUtil.getJsonObject(result);
			JsonArray jsonArray = JsonUtil.getJsonArray(jsonObject, "datas_my_shouru_list");
			if(null == jsonArray )return datasTemp;
			for(int i = 0;i<jsonArray.size();i++){
				InComeInfo comeInfo = new InComeInfo();
				JsonObject obj = JsonUtil.getJsonObject(jsonArray, i);
				comeInfo.pid = JsonUtil.getString(obj, "pid");
				comeInfo.shouru = JsonUtil.getString(obj, "shouru");
				comeInfo.appname = JsonUtil.getString(obj, "appname");
				datasTemp.add(comeInfo);
			}
			return datasTemp;
		}
	};
}
