package com.zy.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.my.log.L;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zy.R;
import com.zy.config.ZYConstant;
import com.zy.model.GameInfo;
import com.zy.model.PersonnelInformation;

public class PersonnelInformationAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<PersonnelInformation> mInfos = new ArrayList<PersonnelInformation>();
    Display display;
    int width;
    int height;
    ImageLoader imageLoader;
    String TAG = PersonnelInformationAdapter.class.getName();
    public PersonnelInformationAdapter(Context context, ImageLoader imageLoader) {
        mContext = context;
        this.imageLoader = imageLoader;
        DisplayMetrics dm = new DisplayMetrics();  
        display = ((Activity)context).getWindow().getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getWidth();
        L.i(TAG, "width "+width+"  height  "+height);
//        DisplayMetrics dm = new DisplayMetrics();  
//        ((Activity)context).getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        
//        width=Util.nullObject2int(dm.widthPixels*dm.density+"");   
//        height=Util.nullObject2int(dm.heightPixels*dm.density+"");   
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        PersonnelInformation personnelInformation = mInfos.get(position);

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
            convertView = layoutInflator.inflate(R.layout.item_zhangyue_introduce, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_user);
            holder.contentView = (TextView) convertView.findViewById(R.id.news_title);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        holder.contentView.setText(personnelInformation.qianming);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        holder.imageView.setLayoutParams(params);
        imageLoader.displayImage(ZYConstant.getBaseUrl()+personnelInformation.bigimg, holder.imageView, ZYConstant.getDisplayImageOptionsNoDefaultLoadImg(0));
        return convertView;
    }

    class ViewHolder {
    	ImageView imageView;
        TextView contentView;
        TextView timeView;
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mInfos.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }
	public void setDatas(ArrayList<PersonnelInformation> datas){
		mInfos.addAll(datas);
		this.notifyDataSetChanged();
	}
	public ArrayList<PersonnelInformation> getDatas(){
		return mInfos;
	}
}
