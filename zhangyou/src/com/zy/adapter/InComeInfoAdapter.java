package com.zy.adapter;



import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zy.R;
import com.zy.model.InComeInfo;

public class InComeInfoAdapter extends BaseAdapter{
	ImageLoader imageLoader;
	ArrayList<InComeInfo> datas = new ArrayList<InComeInfo>();
	Context context;
	LayoutInflater inflater;
	public InComeInfoAdapter(Context context,ImageLoader imageLoader) {
		super();
		this.context = context;
		this.imageLoader = imageLoader;
		inflater = ((Activity)context).getLayoutInflater();
	}
	public void setDatas(ArrayList<InComeInfo> datas){
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
			 convertView = (ViewGroup) inflater.inflate(R.layout.item_incomeinfo, null);
			 holder = new ViewHolder();
             holder.tv_rank = (TextView) convertView.findViewById(R.id.tv_rank);
             holder.tv_inCome = (TextView) convertView.findViewById(R.id.tv_inCome);
             holder.tv_school = (TextView) convertView.findViewById(R.id.tv_school);
             holder.tv_uname = (TextView) convertView.findViewById(R.id.tv_uname);
             convertView.setTag(holder);
		}else{
			 holder = (ViewHolder) convertView.getTag();
		}
		InComeInfo inComeInfo = datas.get(position);
		holder.tv_rank.setText(inComeInfo.shouru);
		holder.tv_inCome.setText(inComeInfo.shouru);
		holder.tv_school.setText(inComeInfo.school);
		holder.tv_uname.setText(inComeInfo.uname);
		return convertView;
	}
	static class ViewHolder {
        public TextView tv_rank;
        public TextView tv_inCome;
        public TextView tv_school;
        public TextView tv_uname;
    }

}
