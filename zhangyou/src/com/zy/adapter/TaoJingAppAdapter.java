package com.zy.adapter;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.imagecache.AsyncImageView;
import com.my.utils.AppUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zy.R;
import com.zy.activity.AppDetailActivity;
import com.zy.config.ZYConstant;
import com.zy.model.GameInfo;

public class TaoJingAppAdapter extends BaseAdapter{
	ImageLoader imageLoader;
	ArrayList<GameInfo> datas = new ArrayList<GameInfo>();
	Context context;
	LayoutInflater inflater;
	public TaoJingAppAdapter(Context context,ImageLoader imageLoader) {
		super();
		this.context = context;
		this.imageLoader = imageLoader;
		inflater = ((Activity)context).getLayoutInflater();
	}
	public void setDatas(ArrayList<GameInfo> datas){
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(null == convertView){
			 convertView = (ViewGroup) inflater.inflate(R.layout.item_taojing_app, null);
			 holder = new ViewHolder();
             holder.iv_game = (ImageView) convertView.findViewById(R.id.iv_game);
             holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
             holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
             holder.tv_game_info = (TextView) convertView.findViewById(R.id.tv_game_info);
             holder.btn_download = (Button) convertView.findViewById(R.id.btn_download);
             holder.btn_share = (Button) convertView.findViewById(R.id.btn_share);
             convertView.setTag(holder);
		}else{
			 holder = (ViewHolder) convertView.getTag();
		}
		GameInfo gameInfo = datas.get(position);
		holder.tv_title.setText(gameInfo.title);
		holder.tv_game_info.setText(gameInfo.introduce);
		setListener(holder.btn_download,holder.btn_share,gameInfo);
		holder.tv_summary.setText(gameInfo.downloadTimes+" 人安装"+gameInfo.fileSize+"MB"+"\n价格:"+gameInfo.price);
		imageLoader.displayImage(ZYConstant.getBaseUrl()+gameInfo.imgUrl, holder.iv_game, ZYConstant.getDefaultDisplayImageOptionsNoRounde());
		return convertView;
	}
	public void setListener(Button btn_download,Button btn_share,final GameInfo gameInfo){
		btn_download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("TAG", "gameInfo   " + gameInfo.summary);
				Intent intent = new Intent(context, AppDetailActivity.class);
				intent.putExtra("gameInfo", gameInfo);
				AppUtil.startActivity(context, intent);
			}
		});
		btn_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent=new Intent(Intent.ACTION_SEND);
//				intent.setType("text/plain");
//				intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
//				intent.putExtra(Intent.EXTRA_TEXT,"I would like to share this with you...");
//				((Activity)context).startActivity(Intent.createChooser(intent, ((Activity)context).getTitle()));
				
				
				Intent intent=new Intent(Intent.ACTION_SEND);   
                intent.setType("image/*");   
                intent.putExtra(Intent.EXTRA_SUBJECT, "Share");   
                intent.putExtra(Intent.EXTRA_TEXT, "http://zhanggame.com.cn?appi=121?userId=fdsfdsafa");   
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
                ((Activity)context).startActivity(Intent.createChooser(intent, ((Activity)context).getTitle()));   
			}
		});
//		 Intent intent=new Intent(Intent.ACTION_SEND);
	}
	static class ViewHolder {
        public ImageView iv_game;
        public TextView tv_title;
        public TextView tv_summary;
        public TextView tv_game_info;
        public Button btn_download;
        public Button btn_share;
        public StringBuilder textBuilder = new StringBuilder();
    }

}
