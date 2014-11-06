package com.zy.config;

import java.util.HashMap;
import com.zy.model.DownLoadInfo;

public class DBConfig {
	public static int DATABASE_VERSION = 1;
	public static HashMap<String, Class> getModel() {
		HashMap<String, Class> returnMap = new HashMap<String, Class>();
		returnMap.put(DownLoadInfo.class.getSimpleName(), DownLoadInfo.class);
		return returnMap;
	}
}
