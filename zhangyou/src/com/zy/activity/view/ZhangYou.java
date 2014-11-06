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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
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
import com.zy.activity.AppDetailActivity;
import com.zy.activity.HomeActivity;
import com.zy.adapter.MainGameAdapter;
import com.zy.config.ZYConstant;
import com.zy.model.GameInfo;
import com.zy.utils.IView;

public class ZhangYou extends BaseView  implements View.OnClickListener,OnItemClickListener,IView{
	String TAG = "ZhangYou";
	SlidingMenu slidingMenu;
	ArrayList<GameInfo> datas = new ArrayList<GameInfo>();
	ArrayList<GameInfo> datasTemp = new ArrayList<GameInfo>();
	LayoutInflater inflater;
	Context context;
	View container;
	PullToRefreshListView mPullRefreshListView;
	ViewFlipper flipper;
	MainGameAdapter gameAdapter;
	int layout = R.layout.v_main_zhangyou;
	boolean isLoad = false;
	int pageIndex = 0;
	String lastLoadTime = com.my.utils.DateUtil.getCurrentDateTime();
	public ZhangYou(Context context,View mainContainer,SlidingMenu slidingMenu) {
		super(context);
		this.context = context;
		this.slidingMenu = slidingMenu;
		this.mainContainer = mainContainer;
		this.inflater = ((Activity) context).getLayoutInflater();
		
		findView();
		init();
	}
	
	void findView(){
		this.container = (ViewGroup) inflater.inflate(layout, null);
		mPullRefreshListView = (PullToRefreshListView) container.findViewById(R.id.pull_refresh_list);
		
	}

	public View getView() {
		return container;
	}
	
	public void init(){
		View emptyView = container.findViewById(R.id.layout_empty_view);
		emptyView.setOnClickListener(this);
		mPullRefreshListView.setEmptyView(emptyView);
		gameAdapter = new MainGameAdapter(context,imageLoader);
		mPullRefreshListView.setAdapter(gameAdapter);
		mPullRefreshListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setScrollingWhileRefreshingEnabled(!mPullRefreshListView.isScrollingWhileRefreshingEnabled());
		mPullRefreshListView.setOnRefreshListener(new com.my.pulltorefresh.PullToRefreshBase.OnRefreshDefaultListener<ListView>() {

			@Override
			public void refresh(PullToRefreshBase<ListView> refreshView) {
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最近更新"+lastLoadTime);
				pageIndex = 0;
				ZhangYou.this.refresh();
			}

			@Override
			public void loadMore(PullToRefreshBase<ListView> refreshView) {
				pageIndex++;
				ZhangYou.this.refresh();
			}

		});
		
		flipper = (ViewFlipper) container.findViewById(R.id.flipper);  
		flipper.startFlipping(); 
		flipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
//		refresh();
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
			slidingMenu.toggle();
			break;
		case R.id.btn_title_more:
			refresh();
			break;
		}
	}
	@Override
	public void refresh(){
		L.i(isDebug, "TAG", "posrUrl   "+getDefaultBaseUrl()+"/game/home.php");
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
				isLoad = true;
				JsonArray datas_slide_imgArrary = JsonUtil.getJsonArray(jsonObject, "datas_slide_img");
				JsonArray datas_game = JsonUtil.getJsonArray(jsonObject, "datas_game");
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
	View.OnClickListener focusOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String pid = v.getTag()+"";
			Intent intent = new Intent(context, AppDetailActivity.class);
			AppMsg.showToast(context, "pid   "+pid);
//			intent.putExtra("gameInfo", gameInfo);
//			AppUtil.startActivity(context, intent);
		}
	};
	void query(){
		if(pageIndex == 0){
			datas.clear();
		}
		datas.addAll(datasTemp);
		gameAdapter.setDatas(datas);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		GameInfo gameInfo = (GameInfo) gameAdapter.getItem(arg2-1);
		Log.i("TAG", "gameInfo   "+gameInfo.summary);
		Intent intent = new Intent(context, AppDetailActivity.class);
		intent.putExtra("gameInfo", gameInfo);
		AppUtil.startActivity(context, intent);
		
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

	@Override
	public boolean openMore() {
		return false;
	}
	@Override
	public void onPageSelected(int position) {
		super.onPageSelected(position);
		if(!isLoad()){
			refresh();
		}
	}
}
