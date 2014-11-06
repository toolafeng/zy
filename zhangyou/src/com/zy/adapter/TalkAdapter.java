package com.zy.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.zy.R;
import com.zy.model.Talk;

public class TalkAdapter extends BaseAdapter{

	LayoutInflater inflater;
	Context context;
	
	public TalkAdapter(Context context) {
		super();
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	ArrayList<Talk> talks = new ArrayList<Talk>();
	@Override
	public int getCount() {
		return talks.size();
	}

	@Override
	public Object getItem(int position) {
		return talks.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.item_talk, null);
		TextView tv_01 = (TextView) convertView.findViewById(R.id.tv_01);
		TextView tv_02 = (TextView) convertView.findViewById(R.id.tv_02);
		TextView tv_03 = (TextView) convertView.findViewById(R.id.tv_03);
		
		Talk talk = talks.get(position);
		tv_01.setText(talk.title);
		tv_02.setText(talk.summary);
		tv_03.setText(talk.addTime);
		return convertView;
	}
	
	public void setDatas(ArrayList<Talk> datas){
		this.talks = datas;
		this.notifyDataSetChanged();
	}

}
