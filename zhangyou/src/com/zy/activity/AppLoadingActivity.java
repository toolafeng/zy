package com.zy.activity;

import android.os.Bundle;

import com.my.utils.AppUtil;
import com.my.utils.PreferenceUtil;
import com.zy.R;

public class AppLoadingActivity extends ZYBaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apploading);
		boolean showGuide = PreferenceUtil.getBoolean(this, AppUtil.getAppVersionName(this, this.getPackageName())+"showGuide",true);
		if(showGuide){
			AppUtil.allActivity.add(this);
		}
		showProgressDialog("数据加载中");
		new Thread(){
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						AppLoadingActivity.this.finish();
					}
				});
				
			};
		}.start();
	}
}
