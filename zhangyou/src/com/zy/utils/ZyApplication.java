package com.zy.utils;

import java.io.File;
import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.my.service.LogService;
import com.my.utils.MyApplication;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zy.config.ZYConstant;

public class ZyApplication extends MyApplication{
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		super.onCreate();
		startService();
		initImageLoader(getApplicationContext());
	}
	public void startService(){
		 startService(new Intent(this, LogService.class));  
	}
	public static void initImageLoader(Context context) {
//		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "/zy/img/cache/");
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "zy/img/cache");  
//		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");  
//		File cacheDir = new File(ZYConstant.dir_img_cache_path);
		DiskCache diskCache = null;
		try {
			diskCache = new LruDiscCache(cacheDir, new Md5FileNameGenerator(), 500);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
//				.discCache(diskCache)//自定义缓存路径 
				.threadPoolSize(5)//线程池内加载的数量
				.denyCacheImageMultipleSizesInMemory()  
				.memoryCacheSize(2 * 1024 * 1024)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
				.build();
		ImageLoader.getInstance().init(config);
	}
}
