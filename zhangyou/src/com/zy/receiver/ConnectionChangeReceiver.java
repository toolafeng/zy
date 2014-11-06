package com.zy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionChangeReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		boolean mobNetFlag = mobNetInfo.isConnected();
		boolean wifiNetFlag = wifiNetInfo.isConnected();
		if(mobNetFlag){
			Toast.makeText(context, "GPRS链接", 1).show();
		}
		if(wifiNetFlag){
			Toast.makeText(context, "wifi链接", 1).show();
		}
		
		if(!mobNetFlag && !wifiNetFlag){
			Toast.makeText(context, "无网络链接", 1).show();
		}
		
	}

}
