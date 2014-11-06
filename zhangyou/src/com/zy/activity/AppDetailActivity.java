package com.zy.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.my.log.L;
import com.my.utils.AppMsg;
import com.my.utils.AppUtil;
import com.my.utils.Util;
import com.zy.R;
import com.zy.activity.helper.ZYWebChromeClient;
import com.zy.config.ZYConstant;
import com.zy.model.DownLoadInfo;
import com.zy.model.GameInfo;

/**
 * 
* @ClassName: GameDetailActivity 
* @Description: TODO(游戏详情) 
* @author LEE 
* @date Jul 21, 2014 11:31:42 PM 
*
 */
public class AppDetailActivity extends ZYBaseActivity implements View.OnClickListener{
	String TAG = AppDetailActivity.class.getSimpleName();
	GameInfo gameInfo;
	private DownloadManager downloadManager;
	DownLoadInfo downLoadInfo = null;
	boolean hasDownLoad = false,isInstall = false;;
	Button btn_download;
	WebView webView;
	String url = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appdetail);
		if(null != savedInstanceState){
			gameInfo = (GameInfo) savedInstanceState.getSerializable("gameInfo");
		}else{
			gameInfo = (GameInfo) getIntent().getSerializableExtra("gameInfo"); 
		}
		downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		url = ZYConstant.gameDetailUrl+gameInfo.id;
		L.i(TAG, "url   "+url);
		initDownload();
		findView();
	}
	public void initDownload(){
		downLoadInfo = new DownLoadInfo();
		downLoadInfo.gameId = gameInfo.id;
		downLoadInfo.packageName = gameInfo.packageName;
		downLoadInfo.title = gameInfo.title;
		downLoadInfo.summary = gameInfo.summary;
		downLoadInfo.introduce = gameInfo.introduce	;
		downLoadInfo.downloadTimes = gameInfo.downloadTimes;
		downLoadInfo.price = gameInfo.price;
		downLoadInfo.isInstall = gameInfo.isInstall;
		downLoadInfo.summary = gameInfo.summary;
		downLoadInfo.downLoadUrl = gameInfo.downLoadUrl;
		downLoadInfo.imgUrl = gameInfo.imgUrl;
	}
	void findView(){
		((TextView)findViewById(R.id.tv_title)).setText("应用详情");
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		webView = (WebView) findViewById(R.id.webView);
		webView.setWebChromeClient(new ZYWebChromeClient(this,null));
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
	class DetailWebChromeClient extends ZYWebChromeClient{

		public DetailWebChromeClient(Context context, ProgressBar progressBar) {
			super(context, progressBar);
		}
		
		
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == com.my.config.MyConfig.getHomeId()){
			AppUtil.finishActivity(this);
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("gameInfo", gameInfo);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(null != savedInstanceState){
			gameInfo = (GameInfo) savedInstanceState.getSerializable("gameInfo");
		}
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_title_back:
				AppUtil.finishActivity(this);
				break;
		default:
			break;
		}
	}
	public String getFileName(String downUrl){
		String fileName = "";
		try{
			fileName = downUrl.split("\\/")[downUrl.split("\\/").length-1];
		}catch(Exception e){
			e.printStackTrace();
			fileName = "";
		}
		return fileName;
	}
	public void downLoad(View v){
		if(hasDownLoad&&!isInstall){//如果下载了,但未安装
			btn_download.setText("安装");
			String fileName = getFileName(downLoadInfo.downLoadUrl);
			String pathName = ZYConstant.dir_download_path+fileName;
			install(pathName);
		}
		if(!hasDownLoad&&isInstall||!hasDownLoad){//如果未下载,但已安装,则叫用户重新下载
			try{
				downLoad();
			}catch(Exception e){
				AppMsg.showToast(this, "下载失败,可能地址不正确");
			}
		}
		if(hasDownLoad&&isInstall){//如果已经下载,并且已经安装
			btn_download.setText("打开");
			Intent intent = new Intent();
			intent.putExtra("openner", "_zy");
			intent.putExtra("userName", "zy");
			intent.putExtra("userPwd", "zy");
			AppUtil.startOtherApp(this,intent,downLoadInfo.packageName);
		}
	}
	public void install(String pathName){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(pathName)), "application/vnd.android.package-archive");
		AppUtil.startActivity(this, intent);
	}
	public void downLoad() throws IllegalArgumentException{
		String downUrl = gameInfo.downLoadUrl;
		L.i(TAG, "downUrl  "+downUrl);
		Uri resource = Uri.parse(encodeGB(downUrl)); 
		DownloadManager.Request request = new DownloadManager.Request(resource);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);   
        request.setAllowedOverRoaming(false);
      //设置文件类型  
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();  
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downUrl));  
        request.setMimeType(mimeString);  
        //在通知栏中显示   
        request.setShowRunningNotification(true);  
        request.setVisibleInDownloadsUi(true);  
        request.setDestinationInExternalPublicDir(ZYConstant.dir_nosd_path, getFileName(downUrl));
        request.setTitle(downLoadInfo.title);
        long id = downloadManager.enqueue(request);
        downLoadInfo.dwonLoadId = id;
        downLoadInfo.id = Util.getUUID(this);
        try {
			dbHelper.getMyDao(DownLoadInfo.class).create(downLoadInfo);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public String encodeGB(String string) {
		// 转换中文编码
		String split[] = string.split("/");
		for (int i = 1; i < split.length; i++) {
			try {
				split[i] = URLEncoder.encode(split[i], "GB2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			split[0] = split[0] + "/" + split[i];
		}
		split[0] = split[0].replaceAll("\\+", "%20");// 处理空格
		return split[0];
	} 
}
