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
import com.zy.model.EvaluatingInfo;

public class EvaluatingAdapter extends BaseAdapter{
	ImageLoader imageLoader;
	ArrayList<EvaluatingInfo> datas = new ArrayList<EvaluatingInfo>();
	Context context;
	LayoutInflater inflater;
	public EvaluatingAdapter(Context context,ImageLoader imageLoader) {
		super();
		this.context = context;
		this.imageLoader = imageLoader;
		inflater = ((Activity)context).getLayoutInflater();
	}
	public void setDatas(ArrayList<EvaluatingInfo> datas){
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
			 convertView = (ViewGroup) inflater.inflate(R.layout.item_evaluating, null);
			 holder = new ViewHolder();
             holder.tv_usrName = (TextView) convertView.findViewById(R.id.tv_usrName);
             holder.tv_evaluatingContent = (TextView) convertView.findViewById(R.id.tv_evaluatingContent);
             convertView.setTag(holder);
		}else{
			 holder = (ViewHolder) convertView.getTag();
		}
		EvaluatingInfo evaluatingInfo = datas.get(position);
		holder.tv_usrName.setText(evaluatingInfo.usrName+":");
		holder.tv_evaluatingContent.setText(evaluatingInfo.evaluatingContent);
		return convertView;
	}
	static class ViewHolder {
        public TextView tv_usrName;
        public TextView tv_evaluatingContent;
    }

}
