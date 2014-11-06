package com.zy.activity;

import java.util.HashMap;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandlerNew;
import com.loopj.android.http.RequestParamsUtil;
import com.my.utils.AppMsg;
import com.my.utils.AppUtil;
import com.my.utils.JsonUtil;
import com.my.utils.ProgressDialogHelper;
import com.my.utils.StringUtil;
import com.zy.R;
import com.zy.config.ZYConstant;

/**
 * 
* @ClassName: LoginRegistActivity 
* @Description: TODO(注册登陆) 
* @author LEE 
* @date Jul 21, 2014 11:32:22 PM 
*
 */
public class LoginRegistActivity extends Activity implements
		View.OnClickListener {

	TextView tv_title,layout_login_regist;
	View layout_login,layout_regist;
	Button btn_regist_login;
	EditText et_login_userid,et_login_pwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_regist);
		findView();
	}

	void findView() {
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		layout_login = findViewById(R.id.layout_login);
		layout_regist = findViewById(R.id.layout_regist);
		et_login_userid = (EditText)findViewById(R.id.et_login_userid);
		et_login_pwd = (EditText)findViewById(R.id.et_login_pwd);
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		
		btn_regist_login = (Button) findViewById(R.id.btn_regist_login);
		
		layout_login_regist = (TextView) findViewById(R.id.layout_login_regist);
		tv_title.setText("登陆/注册");
		layout_login_regist.setOnClickListener(this);
		btn_regist_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_title_back:
			this.finish();
			break;
		case R.id.layout_login_regist:
			switchView(false);
			break;
		case R.id.btn_regist_login:
			switchView(true);
			break;
		case R.id.btn_login:
			login();
			break;
			
		}
	}
	ProgressDialogHelper dialogHelper;
	void login(){
		String userId = et_login_userid.getText()+"";
		String pwd = et_login_pwd.getText()+"";
		
		userId = "张淼";
		pwd = "110110";
		if(StringUtil.stringIsEmpty(userId)){
			AppMsg.showToast(this, "请输入账号");
			return;
		}
		if(StringUtil.stringIsEmpty(pwd)){
			AppMsg.showToast(this, "请输入密码");
			return;
		}
		dialogHelper = new ProgressDialogHelper(this);
		dialogHelper.showProgressDialog("登录中,请稍等...");
		String url = ZYConstant.getBaseUrl()+"/member/api.php";
		HashMap<String, String> paramsMap = new HashMap<String, String>();
		paramsMap.put("dopost", "login");
		paramsMap.put("userid", userId);
		paramsMap.put("pwd", pwd);
		
		new AsyncHttpClient().post(url, RequestParamsUtil.getRequestParams(paramsMap), new AsyncHttpResponseHandlerNew(){

			@Override
			public void onStart() {
				super.onStart();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers,String responseBody) {
				dialogHelper.closeProgressDialog();
				Log.i("TAG", "LoginRegistActivity result "+responseBody);
				if(StringUtil.stringIsNotEmpty(responseBody)){
					JsonObject jsonObject = JsonUtil.getJsonObject(responseBody);
					String success = JsonUtil.getString(jsonObject, "success");
					String msg = JsonUtil.getString(jsonObject, "msg");
					if("true".equals(success)){
//						AppMsg.showToast(LoginRegistActivity.this, "登录成功");
						Intent intent = new Intent();
						AppUtil.finishActivityWidthResult(LoginRegistActivity.this, intent, ZYConstant.RESULT_CODE_LOGIN_SUCCESS);
//						LoginRegistActivity.this.finish();
					}else{
						AppMsg.showToast(LoginRegistActivity.this, msg);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,String responseBody, Throwable error) {
				dialogHelper.closeProgressDialog();
				AppMsg.showToast(LoginRegistActivity.this, "网络链接失败");
			}
		});
	}
	void switchView(boolean showLoginView){
		if(showLoginView){
			layout_regist.setVisibility(View.GONE);
			layout_login.setVisibility(View.VISIBLE);
		}else{
			layout_regist.setVisibility(View.VISIBLE);
			layout_login.setVisibility(View.GONE);
		}
	}

}
