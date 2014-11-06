package com.zy.activity;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.Header;
import android.content.Intent;
import android.os.Bundle;
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
import com.my.utils.AppUtil;
import com.my.utils.HttpUtil;
import com.my.utils.JsonUtil;
import com.my.utils.StringUtil;
import com.zy.R;
import com.zy.adapter.EvaluatingAdapter;
import com.zy.model.EvaluatingInfo;
import com.zy.model.EvaluatingInfo;

/**
 * 
* @ClassName: EvaluatingActivity 
* @Description: TODO(测评) 
* @author LEE 
* @date Jul 21, 2014 11:30:33 PM 
*
 */
public class EvaluatingActivity extends ZYBaseActivity implements View.OnClickListener,OnItemClickListener{
	String TAG = "EvaluatingActivity";
	PullToRefreshListView mPullRefreshListView;
	EvaluatingAdapter evaluatingAdapter;
	boolean isLoad = false;
	int pageIndex = 0;
	String lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	ArrayList<EvaluatingInfo> datas = new ArrayList<EvaluatingInfo>();
	ArrayList<EvaluatingInfo> datasTemp = new ArrayList<EvaluatingInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluating);
		findView();
		init();
		refresh();
	}
	void findView(){
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
//		button.setText("我要投稿");
		((Button) findViewById(R.id.btn_title_more)).setVisibility(View.VISIBLE);
		((Button) findViewById(R.id.btn_title_more)).setOnClickListener(this);
		tv_title.setText("评测");
	}
	public void init(){
		View emptyView = findViewById(R.id.layout_empty_view);
		emptyView.setOnClickListener(this);
		mPullRefreshListView.setEmptyView(emptyView);
		evaluatingAdapter = new EvaluatingAdapter(this,imageLoader);
		mPullRefreshListView.setAdapter(evaluatingAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(!mPullRefreshListView.isScrollingWhileRefreshingEnabled());
		mPullRefreshListView.setOnRefreshListener(new com.my.pulltorefresh.PullToRefreshBase.OnRefreshDefaultListener<ListView>() {

			@Override
			public void refresh(PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最近更新"+lastLoadTime);
				pageIndex = 0;
				EvaluatingActivity.this.refresh();
			}

			@Override
			public void loadMore(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				EvaluatingActivity.this.refresh();
			}

		});
		
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_title_back:
				AppUtil.finishActivity(this);
				break;
			case R.id.btn_title_more:
				AppUtil.startActivity(this, new Intent(this,ContributeActivity.class));
				break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}
	public void refresh(){
		L.i("TAG", "posrUrl   "+getDefaultBaseUrl()+"/tao/feedbacklist.php");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("pageIndex", pageIndex+""); 
        L.i(TAG, "postUrl  "+HttpUtil.getRequestUrl(getDefaultBaseUrl()+"/tao/feedbacklist.php", params));
        post(getDefaultBaseUrl()+"/tao/feedbacklist.php", params, responseHandler);
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
				JsonArray datas_feedback = JsonUtil.getJsonArray(jsonObject, "datas_feedback");
				if(null != datas_feedback){
					datasTemp.clear();
					mappingJson(datasTemp,datas_feedback);
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
		void mappingJson(ArrayList<EvaluatingInfo> datasTemp,JsonArray datas_feedback){
			for(int i = 0;i<datas_feedback.size();i++){
				EvaluatingInfo evaluatingInfo = new EvaluatingInfo();
				JsonObject jsonObject = JsonUtil.getJsonObject(datas_feedback, i);
				evaluatingInfo.pid = JsonUtil.getString(jsonObject, "pid");
				evaluatingInfo.usrName = JsonUtil.getString(jsonObject, "usrname");
				evaluatingInfo.evaluatingContent = JsonUtil.getString(jsonObject, "body");
				datasTemp.add(evaluatingInfo);
			}
		}
	};
	
	void query(){
		if(pageIndex == 0){
			datas.clear();
		}
		datas.addAll(datasTemp);
		evaluatingAdapter.setDatas(datas);
	}
}
