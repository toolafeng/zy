package com.zy.activity.view;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.my.log.L;
import com.zy.R;
import com.zy.activity.HomeActivity;
import com.zy.activity.WebViewActivity;
import com.zy.config.ZYConstant;
import com.zy.utils.IView;

public class ZhangXiao extends BaseView implements View.OnClickListener{
	SlidingMenu slidingMenu;
	LayoutInflater inflater;
	Context context;
	View container;
	int layout = R.layout.v_main_zhangxiao;
	ViewFlipper flipper;
	boolean isLoad = false;
	public ZhangXiao(Context context,View mainContainer,SlidingMenu slidingMenu) {
		super(context);
		this.context = context;
		this.slidingMenu = slidingMenu;
		this.mainContainer = mainContainer;
		this.inflater = ((Activity) context).getLayoutInflater();
		
		findView();
		init();
	}

	public View getView() {
		return container;
	}
	void findView(){
		this.container = (ViewGroup) inflater.inflate(layout, null);
		flipper = (ViewFlipper) container.findViewById(R.id.flipper);
		
		container.findViewById(R.id.layout_news).setOnClickListener(this);
		container.findViewById(R.id.layout_train).setOnClickListener(this);
		container.findViewById(R.id.layout_entertainment).setOnClickListener(this);
		container.findViewById(R.id.layout_chicken_soup).setOnClickListener(this);
	}
	public void init(){
		flipper.startFlipping(); 
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(context instanceof HomeActivity){
			return ((HomeActivity)context).callSuperDispatchTouchEvent(ev);
		}
		return true;
	}
	@Override
	public void refresh() {
		
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
	}

	@Override
	public void onClick(View v) {
		
		int id = v.getId();
		L.i("TAG", "id  "+id);
		switch (id) {
		case R.id.btn_title_back:
			slidingMenu.toggle();
			break;
		case R.id.btn_title_more:
			openMore();
			break;
		case R.id.layout_news:
			openWebView(ZYConstant.schooleNews,"本校新闻");
			break;
		case R.id.layout_train:
			openWebView(ZYConstant.zhangXiaoPeixun,"职场培训");
			break;
		case R.id.layout_chicken_soup:
			openWebView(ZYConstant.jiTang,"心灵鸡汤");
			break;
		case R.id.layout_entertainment:
			openWebView(ZYConstant.yule,"娱乐八卦");
			break;
		}
	}
	Intent intent;
	void openWebView(String url,String title){
		intent = new Intent(context, WebViewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		intent.putExtra("backClose", false);
		context.startActivity(intent);
	}
}
