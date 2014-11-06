package com.zy.activity.view;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.my.db.DBHelper;
import com.my.utils.PreferenceUtil;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.L;
import com.zy.R;
import com.zy.config.DBConfig;
import com.zy.config.ZYConstant;
import com.zy.utils.IView;

public abstract class BaseView implements IView{
	View mainContainer;
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return false;
	}


	@Override
	public void refresh() {
		
	}

	@Override
	public boolean openMore() {
		return false;
	}


	@Override
	public boolean isLoad() {
		return false;
	}
	public void refreshTitlle(int position){
		Log.i("BaseView", "refreshTitlle  "+position);
		View titleLayout = mainContainer.findViewById(R.id.layout_title);
		ObjectAnimator.ofFloat(titleLayout, "rotationX", 0, 180, 0).setDuration(duration).start();
		View view = mainContainer.findViewById(R.id.layout_main_title);
		View layout_home_title = view.findViewById(R.id.layout_home_title);
		View layout_taojing_title = view.findViewById(R.id.layout_taojing_title);
		View layout_zhangyou_title = view.findViewById(R.id.layout_zhangyou_title);
		View layout_zhangxiao_title = view.findViewById(R.id.layout_zhangxiao_title);
		View layout_zhangyue_title = view.findViewById(R.id.layout_zhangyue_title);
		View[] views = new View[]{layout_home_title,layout_taojing_title,layout_zhangyou_title,layout_zhangxiao_title,layout_zhangyue_title};
		for(int i = 0;i<views.length;i++){
			views[i].setVisibility(View.GONE);
		}
		
		views[position].setVisibility(View.VISIBLE);
		
	}
	public void onPageSelected(int position){
		refreshTitlle(position);
		
	}
	public abstract View getView();
	private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	boolean isDebug = false;
	Context context;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	DBHelper dbHelper;
	public BaseView(Context context) {
		super();
		this.context = context;
		this.isDebug = ZYConstant.isDebug;
		dbHelper = DBHelper.newInstance(context, DBConfig.DATABASE_VERSION, DBConfig.getModel());
	}
	public String getString(int resid){
		return context.getResources().getString(resid);
	}
	public String getString(String key){
		return PreferenceUtil.getString(context, key);
	}
	public String getString(String key,String defaultValue){
		return defaultValue;
	}
	ProgressDialog progressDialog;
  	public void showProgressDialog(String msg){
  		if(null != progressDialog){
  			progressDialog.cancel();
  			progressDialog = null;
  		}
  		progressDialog = new ProgressDialog(context);
  		progressDialog.setMessage(msg);
  		progressDialog.setCanceledOnTouchOutside(false);
  		progressDialog.show();
  	}
  	public void showProgressDialog(String msg,boolean cancel){
  		if(null != progressDialog){
  			progressDialog.cancel();
  			progressDialog = null;
  		}
  		progressDialog = new ProgressDialog(context);
  		progressDialog.setMessage(msg);
  		progressDialog.setCanceledOnTouchOutside(cancel);
  		progressDialog.show();
  	}
  	public void closeProgressDialog(){
  		try{
  			if(null != progressDialog){
  	  			progressDialog.cancel();
  	  			progressDialog = null;
  	  		}
  		}catch(Exception e){
  			e.printStackTrace();
  		}
  		
  	}
  	public ProgressDialog getProgressDialog(){
  		if(null != progressDialog){
  			return progressDialog;
  		}else{
  			return null;
  		}
  	}
	public String getDefaultBaseUrl(){
		return ZYConstant.getBaseUrl();
	}
	public void onCancelButtonPressed() {
        asyncHttpClient.cancelRequests(context, true);
    }
    public AsyncHttpClient getAsyncHttpClient() {
        return this.asyncHttpClient;
    }

    public void setAsyncHttpClient(AsyncHttpClient client) {
        this.asyncHttpClient = client;
    }
    public void post(String URL, RequestParams params, ResponseHandlerInterface responseHandler){
    	getAsyncHttpClient().post(URL, params,responseHandler);
    }

	public void post(String URL, HashMap<String, String> params,ResponseHandlerInterface responseHandler) {
		RequestParams requestParams = new RequestParams();
		Iterator iter = params.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey()+"";
			Object value = entry.getValue()+"";
			requestParams.put(key, value);
		}
		getAsyncHttpClient().post(URL, requestParams, responseHandler);
	}
    public void get(String url,ResponseHandlerInterface handler){
    	getAsyncHttpClient().get(url, handler);
    }
}
