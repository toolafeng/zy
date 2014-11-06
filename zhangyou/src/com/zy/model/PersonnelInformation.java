package com.zy.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "T_PersonnelInformation")
public class PersonnelInformation implements Serializable{
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	@DatabaseField(id = true)
	public String id;
	@DatabaseField
	public String mid;
	@DatabaseField
	public String uname;//用户名
	@DatabaseField
	public String bigimg;// 用户图片地址
	@DatabaseField
	public String qianming;// 签名
	@DatabaseField
	public String age;//年龄
	@Override
	public String toString() {
		return "PersonnelInformation [id=" + id + ", mid=" + mid + ", uname="
				+ uname + ", bigimg=" + bigimg + ", qianming=" + qianming
				+ ", age=" + age + "]";
	}
	
	
	
}
