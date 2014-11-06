package com.zy.activity.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.my.imagecache.ChainImageProcessor;
import com.my.imagecache.ImageProcessor;
import com.my.imagecache.MaskImageProcessor;
import com.my.imagecache.ScaleImageProcessor;
import com.my.log.L;
import com.my.utils.AppMsg;
import com.my.utils.AppUtil;
import com.my.utils.HttpUtil;
import com.my.utils.JsonUtil;
import com.my.utils.StringUtil;
import com.zy.R;
import com.zy.activity.APPActivity;
import com.zy.activity.AppDetailActivity;
import com.zy.activity.HomeActivity;
import com.zy.activity.PersonnelInformationActivity;
import com.zy.activity.PersonnelInformationListActivity;
import com.zy.activity.TalkActivity;
import com.zy.config.ZYConstant;
import com.zy.model.GameInfo;
import com.zy.model.PersonnelInformation;

public class Home extends BaseView implements View.OnClickListener{
	String TAG = "Home";
	SlidingMenu slidingMenu;
	LayoutInflater inflater;
	Context context;
	View container;
	int layout = R.layout.v_main_home;
	ViewFlipper flipper;
	boolean isLoad = false;
	Intent intent;
	List<ImageView> dates = new ArrayList<ImageView>();
	List<ImageView> plays = new ArrayList<ImageView>();
	List<ImageView> talks = new ArrayList<ImageView>();
	List<ImageView> soft = new ArrayList<ImageView>();
	
	public Home(Context context,View mainContainer,SlidingMenu slidingMenu) {
		super(context);
		this.context = context;
		this.slidingMenu = slidingMenu;
		this.mainContainer = mainContainer;
		this.inflater = ((Activity) context).getLayoutInflater();
		findView();
		init();
		initImageProcessor(context);
	}
	void findView(){
		container = (ViewGroup) inflater.inflate(layout, null);
		flipper = (ViewFlipper) container.findViewById(R.id.flipper);
		
		container.findViewById(R.id.btn_date_more).setOnClickListener(this);
		container.findViewById(R.id.btn_everybody_play_more).setOnClickListener(this);
		container.findViewById(R.id.btn_everybody_talk_more).setOnClickListener(this);
		container.findViewById(R.id.btn_necessary_soft_more).setOnClickListener(this);
		
		
		flipper.startFlipping(); 
		flipper.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
	}
	@Override
	public View getView() {
		return container;
	}
	
	public void init(){
		refresh();
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
		L.i(TAG, "posrUrl   "+getDefaultBaseUrl()+"/home/appindex.php");
//		get(getDefaultBaseUrl()+"/home/appindex.php",responseHandler);
		HashMap<String, String> params = new HashMap<String, String>();
		String userpass = null;
		params.put("username", ""); // 设置请求的参数名和参数值  
        params.put("userpass", userpass);// 设置请求的参数名和参数  
        L.i(TAG, "postUrl  "+HttpUtil.getRequestUrl(getDefaultBaseUrl()+"/home/appindex.php", params));
        post(getDefaultBaseUrl()+"/home/appindex.php", params, responseHandler);
	}
	@Override
	public boolean isLoad() {
		return isLoad;
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btn_title_back:
				slidingMenu.toggle();
				break;
			case R.id.btn_title_more:
				refresh();
				break;
			case R.id.btn_date_more:
				intent = new Intent(context, PersonnelInformationListActivity.class);
				AppUtil.startActivity(context, intent);
				break;
			case R.id.btn_everybody_play_more:
				intent = new Intent(context, APPActivity.class);
				intent.putExtra("title", "大家都在玩");
				AppUtil.startActivity(context, intent);
				break;
			case R.id.btn_everybody_talk_more:
				intent = new Intent(context, TalkActivity.class);
				AppUtil.startActivity(context, intent);
				break;
			case R.id.btn_necessary_soft_more:
				intent = new Intent(context, APPActivity.class);
				intent.putExtra("appType", "necessary");
				intent.putExtra("title", "掌友必备");
				AppUtil.startActivity(context, intent);
				break;
		}
	}
	
	@Override
	public void onPageSelected(int position) {
		super.onPageSelected(position);
		if(!isLoad){
			refresh();
		}
	}
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			closeProgressDialog();
			String result = StringUtil.byte2String(responseBody);
			L.i(TAG,"result   "+StringUtil.byte2String(responseBody));
			JsonObject jsonObject = JsonUtil.getJsonObject(result);
			if(null != jsonObject){
				JsonArray datas_slide_img = JsonUtil.getJsonArray(jsonObject, "datas_slide_img");
				JsonArray datas_yuehui_girl = JsonUtil.getJsonArray(jsonObject, "datas_yuehui_girl");
				JsonArray datas_yuehui_boy = JsonUtil.getJsonArray(jsonObject, "datas_yuehui_boy");
				JsonArray datas_game = JsonUtil.getJsonArray(jsonObject, "datas_game");
				JsonArray datas_say = JsonUtil.getJsonArray(jsonObject, "datas_say");
				JsonArray datas_soft = JsonUtil.getJsonArray(jsonObject, "datas_soft");
//				AppMsg.showToast(context, "datas_soft  "+datas_soft);
				isLoad = true;
				
				
				if(null == datas_slide_img) return;
				int vCount = flipper.getChildCount();
				for(int i = 0;i<datas_slide_img.size();i++){
					if(i<=vCount-1){
						ImageView img = (ImageView)flipper.getChildAt(i);
						JsonObject  obj = JsonUtil.getJsonObject(datas_slide_img, i);
						String imgSrc = JsonUtil.getString(obj, "img");
						String pid = JsonUtil.getString(obj, "pid");
						img.setTag(pid+"");
						img.setOnClickListener(focusOnClickListener);
						imageLoader.displayImage(getDefaultBaseUrl()+imgSrc, img, ZYConstant.getDisplayImageOptions(10));
					}
				}
				LinearLayout layout_yuehui_girl = (LinearLayout) container.findViewById(R.id.layout_yuehui_girl);
				int layout_yuehui_girl_v_count = layout_yuehui_girl.getChildCount();
				for(int i = 0;i<datas_yuehui_girl.size();i++){
					if(i<=layout_yuehui_girl_v_count-1){
						ViewGroup viewGroup = (ViewGroup) layout_yuehui_girl.getChildAt(i);
						ImageView img = (ImageView)viewGroup.getChildAt(0);
						TextView tv_userName = (TextView) viewGroup.getChildAt(1);
						TextView tv_userAge = (TextView) viewGroup.getChildAt(2);
						
						JsonObject  obj = JsonUtil.getJsonObject(datas_yuehui_girl, i);
						String imgPath = JsonUtil.getString(obj, "face");
						String uname = JsonUtil.getString(obj, "uname");
						String mid = JsonUtil.getString(obj, "mid");
						String age = JsonUtil.getString(obj, "age");
						img.setTag(mid);
						tv_userName.setText(uname);
						tv_userAge.setText(age);
						img.setOnClickListener(dateOnClickListener);
						imageLoader.displayImage(getDefaultBaseUrl()+imgPath, img, ZYConstant.getDisplayImageOptions(50));
					}
				}
				LinearLayout layout_yuehui_boy = (LinearLayout) container.findViewById(R.id.layout_yuehui_boy);
				int layout_yuehui_boy_v_count = layout_yuehui_boy.getChildCount();
				for(int i = 0;i<datas_yuehui_boy.size();i++){
					if(i<=layout_yuehui_boy_v_count-1){
						ViewGroup viewGroup = (ViewGroup) layout_yuehui_boy.getChildAt(i);
						ImageView img = (ImageView)viewGroup.getChildAt(0);
						TextView tv_userName = (TextView) viewGroup.getChildAt(1);
						TextView tv_userAge = (TextView) viewGroup.getChildAt(2);
						
						JsonObject  obj = JsonUtil.getJsonObject(datas_yuehui_boy, i);
						String imgPath = JsonUtil.getString(obj, "face");
						String uname = JsonUtil.getString(obj, "uname");
						String mid = JsonUtil.getString(obj, "mid");
						String age = JsonUtil.getString(obj, "age");
						img.setTag(mid);
						tv_userName.setText(uname);
						tv_userAge.setText(age);
						img.setOnClickListener(dateOnClickListener);
						imageLoader.displayImage(getDefaultBaseUrl()+imgPath, img, ZYConstant.getDisplayImageOptions(10));
					}
				}
				
				
//				LinearLayout layout_everybody_talk = (LinearLayout) container.findViewById(R.id.layout_everybody_talk);
//				int layout_everybody_talk_v_count = layout_everybody_talk.getChildCount();
//				for(int i = 0;i<datas_say.size();i++){
//					if(i<=layout_everybody_talk_v_count-1){
//						ImageView img = (ImageView)layout_everybody_talk.getChildAt(i);
//						JsonObject  obj = JsonUtil.getJsonObject(datas_say, i);
//						String imgPath = JsonUtil.getString(obj, "pic");
//						String pid = JsonUtil.getString(obj, "pid");
//						img.setTag(pid);
//						
//						imageLoader.displayImage(getDefaultBaseUrl()+imgPath, img, ZYConstant.getDefaultDisplayImageOptions());
//					}
//				}
				LinearLayout layout_everybody_talk = (LinearLayout) container.findViewById(R.id.layout_everybody_talk);
				int layout_everybody_talk_v_count = layout_everybody_talk.getChildCount();
				for(int i = 0;i<datas_say.size();i++){
					if(i<=layout_everybody_talk_v_count-1){
						ImageView img = (ImageView)layout_everybody_talk.getChildAt(i);
						JsonObject  obj = JsonUtil.getJsonObject(datas_game, i);
						String imgPath = JsonUtil.getString(obj, "pic");
						String pid = JsonUtil.getString(obj, "pid");
						img.setTag(pid);
						img.setOnClickListener(gameOnClickListener);
						imageLoader.displayImage(getDefaultBaseUrl()+imgPath, img, ZYConstant.getDefaultDisplayImageOptions());
					}
				}
				
				LinearLayout layout_everybody_play_game = (LinearLayout) container.findViewById(R.id.layout_everybody_play_game);
				int layout_everybody_play_game_v_count = layout_everybody_play_game.getChildCount();
				for(int i = 0;i<datas_game.size();i++){
					if(i<=layout_everybody_play_game_v_count-1){
						ImageView img = (ImageView)layout_everybody_play_game.getChildAt(i);
						JsonObject  obj = JsonUtil.getJsonObject(datas_game, i);
						String imgPath = JsonUtil.getString(obj, "pic");
						String pid = JsonUtil.getString(obj, "pid");
						img.setTag(pid);
						img.setOnClickListener(gameOnClickListener);
						imageLoader.displayImage(getDefaultBaseUrl()+imgPath, img, ZYConstant.getDefaultDisplayImageOptions());
					}
				}
				
				LinearLayout layout_soft = (LinearLayout) container.findViewById(R.id.layout_soft);
				int layout_soft_c_count = layout_soft.getChildCount();
				for(int i = 0;i<datas_soft.size();i++){
					if(i<=layout_soft_c_count-1){
						ImageView img = (ImageView)layout_soft.getChildAt(i);
						JsonObject  obj = JsonUtil.getJsonObject(datas_soft, i);
						String imgPath = JsonUtil.getString(obj, "pic");
						String pid = JsonUtil.getString(obj, "pid");
						img.setTag(pid);
						img.setOnClickListener(gameOnClickListener);
						imageLoader.displayImage(getDefaultBaseUrl()+imgPath, img, ZYConstant.getDefaultDisplayImageOptions());
					}
				}
			}
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,byte[] responseBody, Throwable error) {
			closeProgressDialog();
			L.i(TAG, "onFailure");
		}

		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			super.onProgress(bytesWritten, totalSize);
			L.i(TAG,"onProgress");
		}

		@Override
		public void onFinish() {
			super.onFinish();
			L.i(TAG, "onFinish");
		}

		@Override
		public void onRetry(int retryNo) {
			super.onRetry(retryNo);
			L.i(TAG,"onRetry   retryNo  "+retryNo);
		}

		@Override
		public void onStart() {
			super.onStart();
			L.i(TAG,"onStart");
		}

		@Override
		public void onCancel() {
			super.onCancel();
			L.i(TAG,"onCancel");
		}
	};
	View.OnClickListener dateOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String pid = v.getTag()+"";
			Intent intent = new Intent(context, PersonnelInformationActivity.class);
			PersonnelInformation personnelInformation = new PersonnelInformation();
			personnelInformation.id = pid;
			AppMsg.showToast(context, "pid   "+pid);
			intent.putExtra("personnelInformation", personnelInformation);
			AppUtil.startActivity(context, intent);
		}
	};
	View.OnClickListener focusOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String pid = v.getTag()+"";
			Intent intent = new Intent(context, AppDetailActivity.class);
			AppMsg.showToast(context, "pid   "+pid);
//			intent.putExtra("gameInfo", gameInfo);
//			AppUtil.startActivity(context, intent);
		}
	};
	View.OnClickListener gameOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String pid = v.getTag()+"";
			Intent intent = new Intent(context, AppDetailActivity.class);
			AppMsg.showToast(context, "pid   "+pid);
			GameInfo gameInfo = new GameInfo();
			gameInfo.id = pid;
			intent.putExtra("gameInfo", gameInfo);
			AppUtil.startActivity(context, intent);
		}
	};
	private ImageProcessor mImageProcessor;
	private void initImageProcessor(Context context) {
        
        final int thumbnailSize = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_size);
        final int thumbnailRadius = context.getResources().getDimensionPixelSize(R.dimen.thumbnail_radius);

        if (Math.random() >= 0.5f) {
            mImageProcessor = new ChainImageProcessor(
                    new ScaleImageProcessor(thumbnailSize, thumbnailSize, ScaleType.FIT_XY),
                    new MaskImageProcessor(thumbnailRadius));
        } else {
            Path path = new Path();
            path.moveTo(thumbnailRadius, 0);
            path.lineTo(thumbnailSize - thumbnailRadius, 0);
            path.lineTo(thumbnailSize, thumbnailRadius);
            path.lineTo(thumbnailSize, thumbnailSize - thumbnailRadius);
            path.lineTo(thumbnailSize - thumbnailRadius, thumbnailSize);
            path.lineTo(thumbnailRadius, thumbnailSize);
            path.lineTo(0, thumbnailSize - thumbnailRadius);
            path.lineTo(0, thumbnailRadius);
            
            path.close();
            
            Bitmap mask = Bitmap.createBitmap(thumbnailSize, thumbnailSize, Config.ARGB_8888);
            Canvas canvas = new Canvas(mask);
            
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Style.FILL_AND_STROKE);
            paint.setColor(Color.RED);
            
            canvas.drawPath(path, paint);
            
            mImageProcessor = new ChainImageProcessor(
                    new ScaleImageProcessor(thumbnailSize, thumbnailSize, ScaleType.FIT_XY),
                    new MaskImageProcessor(mask));
        }
    }

}
