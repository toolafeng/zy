package com.zy.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.zy.utils.IView;

public class ViewPagerAdapter extends PagerAdapter {
	
	ArrayList<IView> mViewGroups = new ArrayList<IView>();
	
	
	public ViewPagerAdapter(ArrayList<IView> mViewGroups) {
		super();
		this.mViewGroups = mViewGroups;
	}

	@Override
	public int getCount() {
		return mViewGroups.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView(mViewGroups.get(position).getView());
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(mViewGroups.get(position).getView(), 0);
		return mViewGroups.get(position).getView();
	}

	
}