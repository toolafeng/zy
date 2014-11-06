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

public class MyInComeInfoAdapter extends BaseAdapter{
	ArrayList<InComeInfo> datas = new ArrayList<InComeInfo>();
	Context context;
	LayoutInflater inflater;
	public MyInComeInfoAdapter(Context context) {
		super();
		this.context = context;
		inflater = ((Activity)context).getLayoutInflater();
	}
	public void setDatas(ArrayList<InComeInfo> datas){
		this.datas = datas;
		this.notifyDataSetChanged();
	}
	public ArrayList<InComeInfo> getDatas(){
		return datas;
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
			 convertView = (ViewGroup) inflater.inflate(R.layout.item_my_incomeinfo, null);
			 holder = new ViewHolder();
             holder.tv_inComeName = (TextView) convertView.findViewById(R.id.tv_inComeName);
             holder.tv_inCome = (TextView) convertView.findViewById(R.id.tv_inCome);
             convertView.setTag(holder);
		}else{
			 holder = (ViewHolder) convertView.getTag();
		}
		InComeInfo inComeInfo = datas.get(position);
		holder.tv_inComeName.setText(inComeInfo.appname);
		holder.tv_inCome.setText(inComeInfo.shouru);
		return convertView;
	}
	static class ViewHolder {
        public TextView tv_inComeName;
        public TextView tv_inCome;
    }

}
