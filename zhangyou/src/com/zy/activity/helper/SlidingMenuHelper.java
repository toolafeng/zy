package com.zy.activity.helper;

import java.io.File;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;

import com.my.log.L;
import com.my.utils.AppUtil;
import com.zy.R;
import com.zy.activity.DwonloadListActivity;
import com.zy.activity.LoginRegistActivity;
import com.zy.activity.ReportActivity;
import com.zy.activity.WebViewActivity;
import com.zy.config.ZYConstant;

public class SlidingMenuHelper implements OnClickListener{

	Context context;
	View slidingMenuView;

	public SlidingMenuHelper(Context context,View slidingMenuView) {
		super();
		this.context = context;
		this.slidingMenuView = slidingMenuView;
		init();
	}
	void init(){
		slidingMenuView.findViewById(R.id.btn_sliding_menu_login).setOnClickListener(this);
		slidingMenuView.findViewById(R.id.btn_about_product).setOnClickListener(this);
		slidingMenuView.findViewById(R.id.btn_disclaimer).setOnClickListener(this);
		slidingMenuView.findViewById(R.id.btn_business_cooperation).setOnClickListener(this);
		slidingMenuView.findViewById(R.id.btn_report).setOnClickListener(this);
		slidingMenuView.findViewById(R.id.btn_check_update).setOnClickListener(this);
		slidingMenuView.findViewById(R.id.btn_download_manager).setOnClickListener(this);
		slidingMenuView.findViewById(R.id.btn_clear_cache).setOnClickListener(this);
		
		
	}
	Intent intent;
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_sliding_menu_login:
			intent = new Intent(context, LoginRegistActivity.class);
			AppUtil.startActivityForResult(context, intent, 1);
			break;
		case R.id.btn_clear_cache:
			new ClearCacheTask().execute();
			break;
		case R.id.btn_about_product:
			intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("url", "http://app.zhanggame.com/about/app_about_product.html");
			intent.putExtra("title", "产品信息");
			AppUtil.startActivity(context, intent);
			break;
		case R.id.btn_disclaimer:
			intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("url", "http://app.zhanggame.com/about/app_disclaimer.html");
			intent.putExtra("title", "免责声明");
			AppUtil.startActivity(context, intent);
			break;
		case R.id.btn_business_cooperation:
			intent = new Intent(context, WebViewActivity.class);
			intent.putExtra("url", "http://app.zhanggame.com/about/app_business_cooperation.html");
			intent.putExtra("title", "商务合作");
			AppUtil.startActivity(context, intent);
			break;
		case R.id.btn_report:
			intent = new Intent(context, ReportActivity.class);
			AppUtil.startActivity(context, intent);
			break;
		case R.id.btn_check_update:
			intent = new Intent(context, ReportActivity.class);
			AppUtil.startActivity(context, intent);
			break;
		case R.id.btn_download_manager:
			intent = new Intent(context, DwonloadListActivity.class);
			AppUtil.startActivity(context, intent);
			break;
			
//			
		}
	}
	class ClearCacheTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgressDialog("缓存删除中...");
		}
		@Override
		protected Void doInBackground(Void... params) {
			File cacheDir = new File(ZYConstant.dir_img_cache_path);
			if(null != cacheDir && cacheDir.exists() && cacheDir.isDirectory()){
				String[] fileNames = cacheDir.list();
				for(int i = 0;i<fileNames.length;i++){
					File cacheFile = new File(cacheDir.getPath()+"/"+fileNames[i]);
					if(null != cacheFile){
						boolean success = cacheFile.delete();
						L.i("TAG", "fileNames[i]  "+fileNames[i]+"  "+success);
					}
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			closeProgressDialog();
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("提示").setMessage("缓存清除成功").setNegativeButton("关闭", null).show();
		}
		
	}
	class MyTask extends AsyncTask<Integer, Void, String>{

		int actionInt = 0;
		String request(){
			
			return "";
			
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
		}
		@Override
		protected String doInBackground(Integer... params) {
			actionInt = params[0];
			if(0 == actionInt){
				
			}
			if(0 == actionInt){
				
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
		}
		
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
	public void onOpened(){
		
	}
	
	public void onClosed(){
		
	}
	
}
