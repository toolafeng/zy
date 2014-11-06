package com.zy.activity.helper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ZYWebChromeClient extends WebChromeClient {
	Context context;
	ProgressBar progressBar;
	public ZYWebChromeClient(Context context,ProgressBar progressBar) {
		super();
		this.progressBar = progressBar;
		this.context = context;
	}


	@Override
	public Bitmap getDefaultVideoPoster() {
		return super.getDefaultVideoPoster();
	}

	@Override
	public View getVideoLoadingProgressView() {
		// TODO Auto-generated method stub
		return super.getVideoLoadingProgressView();
	}

	@Override
	public void getVisitedHistory(ValueCallback<String[]> callback) {
		// TODO Auto-generated method stub
		super.getVisitedHistory(callback);
	}

	@Override
	public void onCloseWindow(WebView window) {
		// TODO Auto-generated method stub
		super.onCloseWindow(window);
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
		// TODO Auto-generated method stub
		return super.onConsoleMessage(consoleMessage);
	}

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		// TODO Auto-generated method stub
		super.onConsoleMessage(message, lineNumber, sourceID);
	}

	@Override
	public boolean onCreateWindow(WebView view, boolean dialog,
			boolean userGesture, Message resultMsg) {
		// TODO Auto-generated method stub
		return super.onCreateWindow(view, dialog, userGesture, resultMsg);
	}

	@Override
	public void onExceededDatabaseQuota(String url, String databaseIdentifier,
			long currentQuota, long estimatedSize, long totalUsedQuota,
			QuotaUpdater quotaUpdater) {
		// TODO Auto-generated method stub
		super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota,
				estimatedSize, totalUsedQuota, quotaUpdater);
	}

	@Override
	public void onGeolocationPermissionsHidePrompt() {
		// TODO Auto-generated method stub
		super.onGeolocationPermissionsHidePrompt();
	}

	@Override
	public void onGeolocationPermissionsShowPrompt(String origin,
			Callback callback) {
		// TODO Auto-generated method stub
		super.onGeolocationPermissionsShowPrompt(origin, callback);
	}

	@Override
	public void onHideCustomView() {
		// TODO Auto-generated method stub
		super.onHideCustomView();
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				view.getContext());
		builder.setTitle("").setMessage(message).setPositiveButton("确定", null);
		// 不需要绑定按键事件
		// 屏蔽keycode等于84之类的按键
		builder.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (true)
					Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
				return true;
			}
		});
		// 禁止响应按back键的事件
		builder.setCancelable(false);
		AlertDialog dialog = builder.create();
		dialog.show();
		result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
		return true;

		// return super.onJsAlert(view, url, message, result);
	}

	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message,
			JsResult result) {
		// TODO Auto-generated method stub
		return super.onJsBeforeUnload(view, url, message, result);
	}

	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			JsResult result) {
		// TODO Auto-generated method stub
		
		
		return super.onJsConfirm(view, url, message, result);
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult result) {
		// TODO Auto-generated method stub
		return super.onJsPrompt(view, url, message, defaultValue, result);
	}

	@Override
	public boolean onJsTimeout() {
		// TODO Auto-generated method stub
		return super.onJsTimeout();
	}
	ProgressDialog progressDialog;
	void showProgressDialog(){
		if(null != progressDialog && progressDialog.isShowing()){
			return;
		}
		cancelProgressDialog();
		progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("加载中...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
	}
	void cancelProgressDialog(){
		if(null != progressDialog){
			progressDialog.cancel();
			progressDialog = null;
		}
	}
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if(null != progressBar){
	        	
	        
	        progressBar.setProgress(newProgress);
//	        showProgressDialog();
	        if(newProgress==100){  
//	        	cancelProgressDialog();
	        	progressBar.setVisibility(View.GONE);  
	        }
        }
        
//        if(newProgress==100){  
//        	cancelProgressDialog();
//        }else{
//        	showProgressDialog();
//        }
        super.onProgressChanged(view, newProgress);  
        
	}
	@Override
	public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota,
			QuotaUpdater quotaUpdater) {
		// TODO Auto-generated method stub
		super.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota,
				quotaUpdater);
	}

	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		// TODO Auto-generated method stub
		super.onReceivedIcon(view, icon);
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		// TODO Auto-generated method stub
		super.onReceivedTitle(view, title);
	}

	@Override
	public void onReceivedTouchIconUrl(WebView view, String url,
			boolean precomposed) {
		// TODO Auto-generated method stub
		super.onReceivedTouchIconUrl(view, url, precomposed);
	}

	@Override
	public void onRequestFocus(WebView view) {
		// TODO Auto-generated method stub
		super.onRequestFocus(view);
	}

	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		// TODO Auto-generated method stub
		super.onShowCustomView(view, callback);
	}

}
