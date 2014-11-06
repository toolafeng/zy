package com.zy.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_DownLoadInfo")
public class DownLoadInfo {
	@DatabaseField(id = true)
	public String id;
	@DatabaseField
	public long dwonLoadId;// 主键
	@DatabaseField
	public String downLoadUrl;// 下载地址
	@DatabaseField
	public String startTime;// 开始时间
	@DatabaseField
	public String endTime;// 结束时间
	@DatabaseField
	public int status;// 下载状态
	@DatabaseField
	public String gameId;//游戏ID
	@DatabaseField
	public String packageName;//包名
	@DatabaseField
	public String title;//游戏标题
	@DatabaseField
	public String summary;//游戏内容
	@DatabaseField
	public String introduce;// 介绍
	@DatabaseField
	public String downloadTimes;// 下载次数
	@DatabaseField
	public String price;// 价格
	@DatabaseField
	public boolean isInstall;// 是否已经安装
	@DatabaseField
	public String fileSize;// 文件大小
	@DatabaseField
	public String imgUrl;// 图片地址
}
