package com.zy.activity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.my.log.L;
import com.my.pulltorefresh.xlistview.PLA_AdapterView;
import com.my.pulltorefresh.xlistview.PLA_AdapterView.OnItemClickListener;
import com.my.pulltorefresh.xlistview.XListView;
import com.my.pulltorefresh.xlistview.XListView.IXListViewListener;
import com.my.utils.AppMsg;
import com.my.utils.AppUtil;
import com.my.utils.DateUtil;
import com.my.utils.HttpUtil;
import com.my.utils.JsonUtil;
import com.my.utils.StringUtil;
import com.zy.R;
import com.zy.adapter.PersonnelInformationAdapter;
import com.zy.cache.ImageFetcher;
import com.zy.config.ZYConstant;
import com.zy.model.PersonnelInformation;

/**
 * 
* @ClassName: ZhangYueActivity 
* @Description: TODO(掌约(约会推荐列表)) 
* @author LEE 
* @date Jul 21, 2014 11:33:05 PM 
*
 */
public class PersonnelInformationListActivity  extends ZYBaseActivity implements View.OnClickListener,OnItemClickListener,IXListViewListener {
	SlidingMenu slidingMenu;
	XListView xListView;
	PersonnelInformationAdapter zhangYueIntroduceAdapter;
	ImageFetcher mImageFetcher;
	ViewFlipper flipper;
	private int currentPage = 0;
	boolean isLoad = false;
	ContentTask task = new ContentTask(this, 2);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhangyue);
		findView();
		init();
		refresh();
	}

	void findView(){
		findViewById(R.id.btn_title_back).setOnClickListener(this);
		findViewById(R.id.btn_title_more).setOnClickListener(this);
		((TextView)findViewById(R.id.tv_title)).setText("约会推荐");
		findViewById(R.id.btn_title_more).setVisibility(View.GONE);
		xListView = (XListView) findViewById(R.id.xListView);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
	}
	public void init(){
		mImageFetcher = new ImageFetcher(this, 240);
//        mImageFetcher.setLoadingImage(R.drawable.img_zhangyue_introduce_item_bg);
        mImageFetcher.setExitTasksEarly(false);
        
		zhangYueIntroduceAdapter = new PersonnelInformationAdapter(this,imageLoader);
		xListView.setAdapter(zhangYueIntroduceAdapter);
		xListView.setOnItemClickListener(this);
		flipper = (ViewFlipper) findViewById(R.id.flipper);  
		flipper.startFlipping(); 
	}
    private void AddItemToContainer(int pageindex, int type) {
        String url = "http://www.duitang.com/album/1733789/masn/p/" + pageindex + "/24/";
        url = "http://app.zhanggame.com/appointment/home.php";
        Log.d("MainActivity", "current url:" + url);
        ContentTask task = new ContentTask(this, type);
        task.execute(url);
    }
	 private class ContentTask extends AsyncTask<String, Integer, String> {

	        private Context mContext;
	        private int mType = 1;

	        public ContentTask(Context context, int type) {
	            super();
	            mContext = context;
	            mType = type;
	        }

	        @Override
	        protected String doInBackground(String... params) {
            	L.i(ZYConstant.isDebug, "TAG", "url  "+params[0]);
            	String url = params[0];
            	String returnJson = "";
            	if (HttpUtil.checkConnection(mContext)) {
            		returnJson = HttpUtil.post(PersonnelInformationListActivity.this, url, null);
            	}
                return returnJson;
	        }

	        @Override
	        protected void onPostExecute(String result) {
	        	if(StringUtil.stringIsNotEmpty(result)&&!"-1".equals(result)){
	        		isLoad = true;
	        	}
	        	ArrayList<PersonnelInformation> tempDatas = null;
				try {
					tempDatas = parseNewsJSON(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	xListView.setRefreshTime(DateUtil.getCurrentDateTime());
	            if (mType == 1) {
	            	zhangYueIntroduceAdapter.getDatas().clear();
	            	xListView.stopRefresh();

	            } else if (mType == 2) {
	            	xListView.stopLoadMore();
	            }
	            if(null != tempDatas){
	            	zhangYueIntroduceAdapter.setDatas(tempDatas);
	            }
				try {
					JSONObject newsObject = new JSONObject(result);
					JSONArray datas_slide_imgArrary = newsObject.getJSONArray("datas_slide_img");
					if(null == datas_slide_imgArrary) return;
					int vCount = flipper.getChildCount();
					for(int i = 0;i<datas_slide_imgArrary.length();i++){
						if(i<=vCount-1){
							ImageView img = (ImageView)flipper.getChildAt(i);
							JSONObject  obj = JsonUtil.getJSONObject(datas_slide_imgArrary, i);
							String imgSrc = JsonUtil.getString(obj, "img");
							String pid = JsonUtil.getString(obj, "pid");
							img.setTag(pid+"");
							img.setOnClickListener(focusOnClickListener);
							imageLoader.displayImage(getDefaultBaseUrl()+imgSrc, img, ZYConstant.getDisplayImageOptions(10));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

	        }

	        @Override
	        protected void onPreExecute() {
	        }

	        public ArrayList<PersonnelInformation> parseNewsJSON(String json) throws IOException {
	        	ArrayList<PersonnelInformation> duitangs = new ArrayList<PersonnelInformation>();
	            L.i(ZYConstant.isDebug, "TAG", "json  "+json);

	            try {
	                if (null != json) {
	                    JSONObject newsObject = new JSONObject(json);
	                    JSONArray datas_userimgs = newsObject.getJSONArray("datas_userimg");

	                    for (int i = 0; i < datas_userimgs.length(); i++) {
	                        JSONObject newsInfoLeftObject = datas_userimgs.getJSONObject(i);
	                        PersonnelInformation personnelInformation = new PersonnelInformation();
	                        personnelInformation.mid = (newsInfoLeftObject.isNull("mid") ? "" : newsInfoLeftObject.getString("mid"));
	                        personnelInformation.uname = (newsInfoLeftObject.isNull("uname") ? "" : newsInfoLeftObject.getString("uname"));
	                        personnelInformation.bigimg = (newsInfoLeftObject.isNull("bigimg") ? "" : newsInfoLeftObject.getString("bigimg"));
	                        personnelInformation.qianming = (newsInfoLeftObject.isNull("qianming") ? "" : newsInfoLeftObject.getString("qianming"));
	                        personnelInformation.age = (newsInfoLeftObject.isNull("age") ? "" : newsInfoLeftObject.getString("age"));
	                        duitangs.add(personnelInformation);
	                    }
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	            return duitangs;
	        }
	    }
		View.OnClickListener focusOnClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pid = v.getTag()+"";
				Intent intent = new Intent(PersonnelInformationListActivity.this, AppDetailActivity.class);
				AppMsg.showToast(PersonnelInformationListActivity.this, "pid   "+pid);
//				intent.putExtra("gameInfo", gameInfo);
//				AppUtil.startActivity(context, intent);
			}
		};
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
				case R.id.btn_title_back:
					AppUtil.finishActivity(this);
					break;
			}
		}


		@Override
		public void onRefresh() {
			xListView.setRefreshTime(DateUtil.getCurrentDateTime());
			AddItemToContainer(++currentPage, 1);
		}

		public void onLoadMore() {
			 AddItemToContainer(++currentPage, 2);
		}

		public void refresh() {
			if(zhangYueIntroduceAdapter.getCount() == 0){
				AddItemToContainer(++currentPage, 1);
			}
		}

		@Override
		public void onItemClick(PLA_AdapterView<?> parent, View view,int position, long id) {
			Intent intent = new Intent(this, PersonnelInformationActivity.class);
			try{
				intent.putExtra("personnelInformation", (Serializable)zhangYueIntroduceAdapter.getItem(position));
				AppUtil.startActivity(this, intent);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

}
