package com.zy.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.my.utils.AppUtil;
import com.zy.R;
import com.zy.model.PersonnelInformation;

/**
 * 
* @ClassName: PersonnelInformationActivity 
* @Description: TODO(约会个人信息) 
* @author LEE 
* @date Aug 3, 2014 6:21:48 PM 
*
 */
public class PersonnelInformationActivity extends ZYBaseActivity implements OnClickListener{

	TextView tv_user_info;
	PersonnelInformation personnelInformation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(null != savedInstanceState){
			personnelInformation = (PersonnelInformation) savedInstanceState.getSerializable("personnelInformation");
		}else{
			personnelInformation = (PersonnelInformation) getIntent().getSerializableExtra("personnelInformation");
		}
		setContentView(R.layout.activity_personnelinformation);
		findView();
	}
	void findView(){
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		((TextView)findViewById(R.id.tv_title)).setText("用户资料");
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		tv_user_info = (TextView) findViewById(R.id.tv_user_info);
		tv_user_info.setText(personnelInformation.toString());
		
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(null != outState){
			outState.putSerializable("personnelInformation", personnelInformation);
		}
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if(null != savedInstanceState){
			personnelInformation = (PersonnelInformation) savedInstanceState.getSerializable("personnelInformation");
		}
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_title_back:
				AppUtil.finishActivity(this);
				break;
		}
	}
}
