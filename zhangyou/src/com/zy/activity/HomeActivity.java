package com.zy.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.my.utils.AppMsg;
import com.my.utils.AppUtil;
import com.my.utils.PreferenceUtil;
import com.my.utils.Util;
import com.nostra13.universalimageloader.utils.L;
import com.zy.R;
import com.zy.activity.helper.SlidingMenuHelper;
import com.zy.activity.view.Home;
import com.zy.activity.view.TaoJing;
import com.zy.activity.view.ZhangXiao;
import com.zy.activity.view.ZhangYou;
import com.zy.activity.view.ZhangYue;
import com.zy.adapter.ViewPagerAdapter;
import com.zy.config.ZYConstant;
import com.zy.utils.IView;
import com.zy.widget.TabView;
import com.zy.widget.TabView.OnTabChangeListener;


public class HomeActivity extends SlidingFragmentActivity implements OnTabChangeListener,OnClickListener {
	ViewPagerAdapter viewPagerAdapter;
	ViewPager homeViewPager;
	IView  taoJing,zhangXiao,zhangYou,zhangYue,home;
	ArrayList<IView> iViews = new ArrayList<IView>();
	SlidingMenuHelper slidingMenuHelper;
	TabView tab_view;
	View slidingMenuView,mainContainer;
	SlidingMenu slidingMenu;
	String TAG = "HomeActivity";
	/** 当前状态 */
	public HomeActivity(){
		
	}
	void initSlidingMenu(){
		slidingMenuView = getLayoutInflater().inflate(R.layout.v_sliding_menu, null);
		setBehindContentView(slidingMenuView);
		slidingMenu = getSlidingMenu();
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setShadowWidthRes(R.dimen.width_shadow);
		slidingMenu.setShadowDrawable(R.drawable.shadowleft);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		int i = 1/0;
		boolean showGuide = PreferenceUtil.getBoolean(this, AppUtil.getAppVersionName(this, this.getPackageName())+"showGuide",true);
		Intent intent;
		if(showGuide){
			AppUtil.allActivity.add(this);
			intent = new Intent(this, GuideActivity.class);
		}else{
			intent = new Intent(this, AppLoadingActivity.class);
		}
		startActivity(intent);
		mainContainer = getLayoutInflater().inflate(R.layout.activity_home, null);
		setContentView(mainContainer);
		initSlidingMenu();
		slidingMenuHelper = new SlidingMenuHelper(this, slidingMenuView);
		homeViewPager = (ViewPager) findViewById(R.id.homeViewPager);
		tab_view = (TabView) findViewById(R.id.tab_view);
		home = new Home(this,mainContainer,slidingMenu);
		zhangYou = new ZhangYou(this,mainContainer,slidingMenu);
		taoJing = new TaoJing(this,mainContainer,slidingMenu);
		zhangXiao = new ZhangXiao(this,mainContainer,slidingMenu);
		zhangYue = new ZhangYue(this,mainContainer,slidingMenu);
		
		iViews.add(home);
		iViews.add(taoJing);
		iViews.add(zhangYou);
		iViews.add(zhangXiao);
		iViews.add(zhangYue);
		
		viewPagerAdapter = new ViewPagerAdapter(iViews);
		homeViewPager.setAdapter(viewPagerAdapter);
		homeViewPager.setOnPageChangeListener(changeListener);
//		startZYService();
		tab_view.setOnTabChangeListener(this);
		tab_view.setCurrentTab(0);
		setSlidingListener();
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		findViewById(R.id.btn_title_refresh).setOnClickListener(this);
		findViewById(R.id.btn_title_more).setOnClickListener(this);
		
	}
	
	void setSlidingListener(){
		getSlidingMenu().setSlidingEnabled(false);
		getSlidingMenu().setOnOpenedListener(new OnOpenedListener() {
			
			@Override
			public void onOpened() {
				Log.i("TAG", "getSlidingMenu  onOpened ");
				slidingMenuHelper.onOpened();
				getSlidingMenu().setSlidingEnabled(true);
			}
		});
		getSlidingMenu().setOnClosedListener(new OnClosedListener() {
			
			@Override
			public void onClosed() {
				Log.i("TAG", "getSlidingMenu  onClosed ");
				slidingMenuHelper.onClosed();
				getSlidingMenu().setSlidingEnabled(false);
			}
		});
	}
	
	public boolean callSuperDispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	
	OnPageChangeListener changeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
			tab_view.setCurrentTab(position);
			iViews.get(homeViewPager.getCurrentItem()).onPageSelected(position);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}
	};
	  private long exitTime = 0; 
	     
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 2000) {
				AppMsg.showToast(getApplicationContext(), "再按一次退出程序");
				exitTime = System.currentTimeMillis();
			} else {
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v){
		int id = v.getId();
		if(id == R.id.btn_title_back){
			slidingMenu.toggle();
		}
		if(id == R.id.btn_title_refresh){
			iViews.get(homeViewPager.getCurrentItem()).refresh();
		}
		if(id == R.id.btn_title_more){
			iViews.get(homeViewPager.getCurrentItem()).openMore();
		}
		
		
	}
	
	@Override
	public void onTabChange(String tag) {
		int position = Util.nullObject2int(tag, 0);
		homeViewPager.setCurrentItem(position);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == ZYConstant.RESULT_CODE_LOGIN_SUCCESS){
//			AppMsg.showToast(this, "ZYConstant.RESULT_CODE_LOGIN_SUCCESS");
			slidingMenu.toggle();
			AppMsg.showToast(this, "登录成功");
//			findViewById(R.id.btn_title_back).performClick();
		}
	}
	
}
