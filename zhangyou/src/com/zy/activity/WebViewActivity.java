package com.zy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.utils.AppUtil;
import com.zy.R;
import com.zy.activity.helper.ZYWebChromeClient;
import com.zy.activity.helper.ZYWebViewClient;

public class WebViewActivity extends Activity implements OnClickListener{
	WebView webView;
	TextView tv_title;
	Button btn_title_back,btn_title_more;
	ProgressBar progressBar;
	ZYWebViewClient eomsWebViewClient;
	String url = "";
	String title = "";
	boolean backClose = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		
		if(null == savedInstanceState){
			url = getIntent().getStringExtra("url");
			title = getIntent().getStringExtra("title");
			backClose = getIntent().getBooleanExtra("backClose", true);
		}else{
			url = savedInstanceState.getString("url");
			title = savedInstanceState.getString("title");
			backClose = savedInstanceState.getBoolean("backClose", true);
		}
		Log.i("TAG", "url  "+url);
		webView = (WebView) findViewById(R.id.webView);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_title_more = (Button) findViewById(R.id.btn_title_more);
		btn_title_back = (Button) findViewById(R.id.btn_title_back);
		tv_title.setText(title);
		btn_title_back.setOnClickListener(this);
		btn_title_more.setVisibility(View.GONE);
		progressBar = (ProgressBar) findViewById(R.id.pb);
		webView.setWebChromeClient(new ZYWebChromeClient(this,progressBar));
		webView.setWebViewClient(new WebViewClient(){
			public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				view.loadUrl(url);
				return true;
			}
		});
        webView.getSettings().setDefaultTextEncodingName("utf-8") ;
        webView.clearCache(true);
		webView.clearHistory();
		webView.clearFormData();
		webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url+""); 
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(null != outState){
			outState.putString("url", url);
			outState.putString("title", title);
		}
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(null != savedInstanceState){
			url = savedInstanceState.getString("url");
			title = savedInstanceState.getString("title");
		}
	}
	
	void clearCache(){
		webView.clearCache(true);
		webView.clearHistory();
		webView.clearFormData();
	}
	@Override
	public void finish() {
		super.finish();
		clearCache();
	}

	@Override
	public void onClick(View v) {
		if(!backClose){
			webView.goBack();
			return;
		}
		int id = v.getId();
		switch (id) {
			case R.id.btn_title_back:
				AppUtil.finishActivity(this);
				break;
		}
	}
}
