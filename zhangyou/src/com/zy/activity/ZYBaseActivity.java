package com.zy.activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.my.activity.MyActivity;
import com.my.db.DBHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zy.config.DBConfig;
import com.zy.config.ZYConstant;

public class ZYBaseActivity extends MyActivity{
	DBHelper dbHelper;
	private AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = DBHelper.newInstance(this, DBConfig.DATABASE_VERSION, DBConfig.getModel());
	}
	public String getDefaultBaseUrl(){
		return ZYConstant.getBaseUrl();
	}
	public void onCancelButtonPressed() {
        asyncHttpClient.cancelRequests(this, true);
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
    ProgressDialog progressDialog;
  	public void showProgressDialog(String msg){
  		if(null != progressDialog){
  			progressDialog.cancel();
  			progressDialog = null;
  		}
  		progressDialog = new ProgressDialog(this);
  		progressDialog.setMessage(msg);
  		progressDialog.setCanceledOnTouchOutside(false);
  		progressDialog.show();
  	}
  	public void showProgressDialog(String msg,boolean cancel){
  		if(null != progressDialog){
  			progressDialog.cancel();
  			progressDialog = null;
  		}
  		progressDialog = new ProgressDialog(this);
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
}
