package com.zy.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.my.utils.AppUtil;
import com.my.utils.PreferenceUtil;
import com.viewpagerindicator.LinePageIndicator;
import com.zy.R;

public class GuideActivity extends FragmentActivity {
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        boolean showGuide = PreferenceUtil.getBoolean(this, AppUtil.getAppVersionName(this, this.getPackageName())+"showGuide",true);
		if(showGuide){
			AppUtil.allActivity.add(this);
		}
        final ZyFragmentAdapter mAdapter = new ZyFragmentAdapter(getSupportFragmentManager());
        ViewPager mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        
        LinePageIndicator mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	if(position == mAdapter.getCount()-1){
            		
            	}
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
		}
		
		for(int i = 0;i<AppUtil.allActivity.size();i++){
			AppUtil.allActivity.get(i).finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	interface IconPagerAdapter {
		int getIconResId(int index);
		int getCount();
	}

	static class ZyFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		protected static final Integer[] backgroundIds = new Integer[] { R.drawable.img_logo_01, R.drawable.img_logo_02,R.drawable.img_logo_03, R.drawable.img_logo_04};

		private int mCount = backgroundIds.length;

		public ZyFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ZyFragment.newInstance(backgroundIds[position % backgroundIds.length]);
		}

		@Override
		public int getCount() {
			return mCount;
		}


		@Override
		public int getIconResId(int index) {
			return backgroundIds[index % backgroundIds.length];
		}

		public void setCount(int count) {
			if (count > 0 && count <= 10) {
				mCount = count;
				notifyDataSetChanged();
			}
		}
	}

	public static class ZyFragment extends Fragment {
		private static final String KEY_CONTENT = "TestFragment:Content";

		public static ZyFragment newInstance(int backgroundId) {
			ZyFragment fragment = new ZyFragment();
			fragment.backgroundId = backgroundId;

			return fragment;
		}

		private int backgroundId = 0;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if ((savedInstanceState != null)&& savedInstanceState.containsKey(KEY_CONTENT)) {
				backgroundId = savedInstanceState.getInt(KEY_CONTENT);
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			container = (ViewGroup) inflater.inflate(R.layout.v_app_start, null);
			ImageView iv_logo = (ImageView) container.findViewById(R.id.iv_logo);
			iv_logo.setBackgroundResource(backgroundId);
			if(backgroundId == R.drawable.img_logo_04){
				iv_logo.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						PreferenceUtil.saveBoolean(getActivity(), AppUtil.getAppVersionName(getActivity(), getActivity().getPackageName())+"showGuide", false);
						getActivity().finish();
					}
				});
			}
			
			return container;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);
			outState.putInt(KEY_CONTENT, backgroundId);
		}
	}
}
