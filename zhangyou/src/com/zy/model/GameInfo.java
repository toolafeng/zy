package com.zy.model;

import java.io.Serializable;

public class GameInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String packageName;
	public String title;
	public String summary;
	public String introduce;//介绍
	public String downloadTimes;//下载次数
	public String price;//价格
	public boolean isInstall;//是否已经安装
	public String fileSize;//文件大小
	public String imgUrl;//文件大小
	public String downLoadUrl;//文件大小
	public String launguage;//语音
	public String payType;//游戏类型,免费,收费等 
	public String versionName;//游戏版本
	public String company;//游戏版本
	public String versionCode;//游戏版本
}
