package com.zy.receiver;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import com.my.db.DBHelper;
import com.my.log.L;
import com.my.utils.AppUtil;
import com.zy.config.DBConfig;
import com.zy.model.DownLoadInfo;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class DownLoadReceiver extends BroadcastReceiver{
	String TAG = "DownLoadReceiver";
	Context context;
	private DownloadManager downloadManager;
	DBHelper dbHelper;
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		dbHelper = DBHelper.newInstance(context, DBConfig.DATABASE_VERSION, DBConfig.getModel());
		downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE); 
		Long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
		L.i(TAG,""+ intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
		queryDownloadStatus(id);
	}
	private void queryDownloadStatus(Long id) {
		List<DownLoadInfo> downLoadInfos = null;
		try {
			downLoadInfos = dbHelper.getMyDao(DownLoadInfo.class).queryForEq("dwonLoadId", id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(id);
		Cursor cursor = downloadManager.query(query);
		if(cursor!= null){
			while(cursor.moveToNext()){
				int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
				String local_filename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
				String local_uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
				String total_size = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
				L.i(TAG, "status  "+status+"  local_filename  "+local_filename+" local_uri "+local_uri+"  total_size "+total_size);
				for(int i = 0 ;i<downLoadInfos.size();i++){
					downLoadInfos.get(i).status = status;
					try {
						dbHelper.getMyDao(DownLoadInfo.class).update(downLoadInfos.get(i));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				switch (status) {
				case DownloadManager.STATUS_PAUSED:
					L.i(TAG, "STATUS_PAUSED");
				case DownloadManager.STATUS_PENDING:
					L.i(TAG, "STATUS_PENDING");
				case DownloadManager.STATUS_RUNNING:
					// 正在下载，不做任何事情
					L.i(TAG, "STATUS_RUNNING");
					break;
				case DownloadManager.STATUS_SUCCESSFUL:
					// 完成
					L.i(TAG, "下载完成");
					install(local_filename);
					break;
				case DownloadManager.STATUS_FAILED:
					// 清除已下载的内容，重新下载
					L.i(TAG, "STATUS_FAILED");
					break;
				}
			}
		}
	}
	public void install(String pathName){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(pathName)), "application/vnd.android.package-archive");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		AppUtil.startActivity(context, intent);
	}

}
