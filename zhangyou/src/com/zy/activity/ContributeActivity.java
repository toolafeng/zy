package com.zy.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.my.activity.MyActivity;
import com.my.utils.AppMsg;
import com.my.utils.AppUtil;
import com.zy.R;

/**
 * 
* @ClassName: ContributeActivity 
* @Description: TODO(投稿) 
* @author LEE 
* @date Jul 21, 2014 11:29:48 PM 
*
 */
public class ContributeActivity extends MyActivity implements View.OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contribute);
		findView();
	}
	void findView(){
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		Button button = (Button) findViewById(R.id.btn_title_more);
		TextView tv_title = (TextView) findViewById(R.id.tv_title);
//		button.setText("投稿");
		button.setVisibility(View.VISIBLE);
		tv_title.setText("我要投稿");
		button.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_title_back:
				AppUtil.finishActivity(this);
				break;
			case R.id.btn_title_more:
				AppMsg.showToast(this, "提交中...");
				break;
		}
	}

}
