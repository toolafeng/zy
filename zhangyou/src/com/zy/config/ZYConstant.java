package com.zy.config;

import android.os.Environment;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zy.R;

public class ZYConstant {
	public static int RESULT_CODE_LOGIN_SUCCESS = 1;
	public static String gameDetailUrl = "http://app.zhanggame.com/plus/view.php?aid=";
	public static String zhangXiaoPeixun = "http://app.zhanggame.com/plus/list.php?tid=23";//职场培训
	public static String schooleNews = "http://app.zhanggame.com/plus/list.php?tid=24";//本校新闻
	public static String jiTang = "http://app.zhanggame.com/plus/list.php?tid=25";//心灵鸡汤
	public static String yule = "http://app.zhanggame.com/plus/list.php?tid=26";//心灵鸡汤
	
	
	
	
	public static String dir_nosd_path = "/zy/download/";
	public static String dir_nosd_default_path = "/zy/img/cache";
	public static String dir_download_path = Environment.getExternalStorageDirectory() + dir_nosd_path;
	public static String dir_img_cache_path = Environment.getExternalStorageDirectory() + dir_nosd_default_path;;
	private static String baseUrl = "http://app.zhanggame.com";
	public static String getBaseUrl(){
		return baseUrl;
	}
	public static boolean isDebug = true;
	public static DisplayImageOptions getDefaultDisplayImageOptions(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.img_zy_loading)
		.showImageForEmptyUri(R.drawable.img_zy_loading)
		.showImageOnFail(R.drawable.img_zy_loading)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.displayer(new RoundedBitmapDisplayer(20))
		.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.build();
		return options;
	}
	public static DisplayImageOptions getDefaultDisplayImageOptionsNoRounde(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.img_zy_loading)
		.showImageForEmptyUri(R.drawable.img_zy_loading)
		.showImageOnFail(R.drawable.img_zy_loading)
		.imageScaleType(ImageScaleType.EXACTLY)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(0))
		.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.build();
		return options;
	}
	public static DisplayImageOptions getDisplayImageOptions(int round){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.img_zy_loading)
		.showImageForEmptyUri(R.drawable.img_zy_loading)
		.showImageOnFail(R.drawable.img_zy_loading)
		.imageScaleType(ImageScaleType.EXACTLY)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(round))
		.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
		.build();
		return options;
	}
	public static DisplayImageOptions getDisplayImageOptions(int round,int loadingImg,boolean showDefaultLoadImg){
		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		if(showDefaultLoadImg){
			builder.showImageOnLoading(loadingImg);
			builder.showImageForEmptyUri(R.drawable.img_zy_loading);
			builder.showImageOnFail(R.drawable.img_zy_loading);
		}
		builder.imageScaleType(ImageScaleType.EXACTLY);
		builder.cacheInMemory(true);
		builder.cacheOnDisk(true);
		builder.considerExifParams(true);
		builder.displayer(new RoundedBitmapDisplayer(round));
		builder.displayer(new FadeInBitmapDisplayer(100));//是否图片加载好后渐入的动画时间
		return builder.build();
	}
	public static DisplayImageOptions getDisplayImageOptionsNoDefaultLoadImg(int round){
		return getDisplayImageOptions(round,R.drawable.img_zy_loading,false);
	}
}
