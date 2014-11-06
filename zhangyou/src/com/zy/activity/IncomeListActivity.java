package com.zy.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
import com.zy.adapter.InComeInfoAdapter;
import com.zy.config.ZYConstant;
import com.zy.model.InComeInfo;
/**
 * 
* @ClassName: IncomeListActivity 
* @Description: TODO(收入榜) 
* @author LEE 
* @date Jul 21, 2014 11:32:01 PM 
*
 */
public class IncomeListActivity extends ZYBaseActivity implements View.OnClickListener,OnItemClickListener{
	String TAG = "IncomeListActivity";
	PullToRefreshListView mPullRefreshListView;
	ViewFlipper flipper;
	InComeInfoAdapter inComeInfoAdapter;
	boolean isLoad = false;
	int pageIndex = 0;
	String lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	ArrayList<InComeInfo> datas = new ArrayList<InComeInfo>();
	ArrayList<InComeInfo> datasTemp = new ArrayList<InComeInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_incomelist);
		findView();
		init();
		refresh();
	}
	void findView(){
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		Button button = (Button) findViewById(R.id.btn_title_more);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
		button.setText("我的收入");
		button.setOnClickListener(this);
		button.setVisibility(View.VISIBLE);
		tv_title.setText("收入榜TOP50");
		flipper = (ViewFlipper) findViewById(R.id.flipper);  
		flipper.startFlipping(); 
	}
	public void init(){
		View emptyView = findViewById(R.id.layout_empty_view);
		emptyView.setOnClickListener(this);
		mPullRefreshListView.setEmptyView(emptyView);
		inComeInfoAdapter = new InComeInfoAdapter(this,imageLoader);
		mPullRefreshListView.setAdapter(inComeInfoAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(!mPullRefreshListView.isScrollingWhileRefreshingEnabled());
		mPullRefreshListView.setOnRefreshListener(new com.my.pulltorefresh.PullToRefreshBase.OnRefreshDefaultListener<ListView>() {

			@Override
			public void refresh(PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最近更新"+lastLoadTime);
				pageIndex = 0;
				IncomeListActivity.this.refresh();
			}

			@Override
			public void loadMore(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				IncomeListActivity.this.refresh();
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
				AppUtil.startActivity(this, new Intent(this, MyIncomeActivity.class));
				break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}
	public void refresh(){
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("pageIndex", pageIndex+""); 
        L.i(TAG, "postUrl  "+HttpUtil.getRequestUrl(getDefaultBaseUrl()+"/tao/tao_shouru.php", params));
//        http://app.zhanggame.com/tao/tao_shouru.php
        post(getDefaultBaseUrl()+"/tao/tao_shouru.php", params, responseHandler);
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
				JsonArray datas_slide_imgArrary = JsonUtil.getJsonArray(jsonObject, "datas_slide_img");
				JsonArray datas_tao_shourul_list = JsonUtil.getJsonArray(jsonObject, "datas_tao_shourul_list");
				
				for(int i = 0;i<datas_tao_shourul_list.size();i++){
					datasTemp.clear();
					mappingJson(datasTemp,datas_tao_shourul_list);
				}
				query();
				if(null == datas_slide_imgArrary) return;
				int vCount = flipper.getChildCount();
				for(int i = 0;i<datas_slide_imgArrary.size();i++){
					if(i<=vCount-1){
						ImageView img = (ImageView)flipper.getChildAt(i);
						JsonObject  obj = JsonUtil.getJsonObject(datas_slide_imgArrary, i);
						String imgSrc = JsonUtil.getString(obj, "img");
						String pid = JsonUtil.getString(obj, "pid");
						img.setTag(pid+"");
						img.setOnClickListener(focusOnClickListener);
						imageLoader.displayImage(getDefaultBaseUrl()+imgSrc, img, ZYConstant.getDisplayImageOptions(10));
					}
				}
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
		void mappingJson(ArrayList<InComeInfo> datasTemp,JsonArray datas_game){
			for(int i = 0;i<datas_game.size();i++){
				InComeInfo inComeInfo = new InComeInfo();
				JsonObject jsonObject = JsonUtil.getJsonObject(datas_game, i);
				inComeInfo.mid = JsonUtil.getString(jsonObject, "mid");
				inComeInfo.uname = JsonUtil.getString(jsonObject, "uname");
				inComeInfo.school = JsonUtil.getString(jsonObject, "school");
				inComeInfo.shouru = JsonUtil.getString(jsonObject, "shouru");
				inComeInfo.id = JsonUtil.getString(jsonObject, "mid");
				datasTemp.add(inComeInfo);
			}
		}
	};
	View.OnClickListener focusOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String pid = v.getTag()+"";
//			Intent intent = new Intent(context, AppDetailActivity.class);
//			AppMsg.showToast(context, "pid   "+pid);
//			intent.putExtra("gameInfo", gameInfo);
//			AppUtil.startActivity(context, intent);
		}
	};
	void query(){
		if(pageIndex == 0){
			datas.clear();
		}
		datas.addAll(datasTemp);
		inComeInfoAdapter.setDatas(datas);
	}
}
