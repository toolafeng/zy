package com.zy.utils;

import android.view.MotionEvent;
import android.view.View;

public interface IView {
	public final int duration = 500;
	public boolean dispatchTouchEvent(MotionEvent ev);
	public View getView();
	public void init();
	public void refresh();
	public boolean isLoad();//数据是否已经加载过
	public boolean openMore();//数据是否已经加载过
	public void onPageSelected(int position);
	
}
