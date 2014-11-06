package com.zy.adapter;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zy.R;
import com.zy.config.ZYConstant;
import com.zy.model.GameInfo;

public class MainGameAdapter extends BaseAdapter{
	ImageLoader imageLoader;
	ArrayList<GameInfo> datas = new ArrayList<GameInfo>();
	Context context;
	LayoutInflater inflater;
	public MainGameAdapter(Context context,ImageLoader imageLoader) {
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
			 convertView = (ViewGroup) inflater.inflate(R.layout.item_zhangyou_game, null);
			 holder = new ViewHolder();
             holder.iv_game = (ImageView) convertView.findViewById(R.id.iv_game);
             holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
             holder.tv_summary = (TextView) convertView.findViewById(R.id.tv_summary);
             holder.tv_game_info = (TextView) convertView.findViewById(R.id.tv_game_info);
             holder.btn_download = (Button) convertView.findViewById(R.id.btn_download);
             convertView.setTag(holder);
		}else{
			 holder = (ViewHolder) convertView.getTag();
		}
		GameInfo gameInfo = datas.get(position);
		holder.tv_title.setText(gameInfo.title);
		holder.tv_game_info.setText(gameInfo.introduce);
		holder.tv_summary.setText(gameInfo.downloadTimes+" 人安装"+gameInfo.fileSize+"MB"+"\n价格:"+gameInfo.price);
		imageLoader.displayImage(ZYConstant.getBaseUrl()+gameInfo.imgUrl, holder.iv_game, ZYConstant.getDefaultDisplayImageOptionsNoRounde());
		return convertView;
	}
	static class ViewHolder {
        public ImageView iv_game;
        public TextView tv_title;
        public TextView tv_summary;
        public TextView tv_game_info;
        public Button btn_download;
        public StringBuilder textBuilder = new StringBuilder();
    }

}
