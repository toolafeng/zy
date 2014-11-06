package com.zy.activity;

import com.zy.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 
* @ClassName: ReportActivity 
* @Description: TODO(意见反馈) 
* @author LEE 
* @date Jul 21, 2014 11:32:36 PM 
*
 */
public class ReportActivity extends Activity implements View.OnClickListener{

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		findView();
	}

	void findView() {
		((TextView) findViewById(R.id.tv_title)).setText("意见/反馈");
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		findViewById(R.id.btn_title_back).setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_title_back:
			this.finish();
			break;
		}
	}
}
