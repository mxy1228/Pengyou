package com.cyou.mrd.pengyou.download;

public interface DownloadListener {

	//更新下载进度
	public void updateProgress(String gameString,int percent,int speed);
	//下载完成
	public void finish(DownloadTask task);
	public void preDownload(DownloadTask task);
	//下载出现异常
	public void error(DownloadTask task,int error);
	//更新DB中的下载任务
	public void updateItemInDB(DownloadItem item);
	//删除下载任务
	public void deleteTask(DownloadItem item);
}
