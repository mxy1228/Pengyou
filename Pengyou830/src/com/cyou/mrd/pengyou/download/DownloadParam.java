package com.cyou.mrd.pengyou.download;

public class DownloadParam {

	public static final String STATE = "state";
	public static final String GAMESTRING = "gameString";
	public static final String GAMECODE = "gamecode";
	public static final String PACKAGE_NAME = "package_name";
	public static final String VERSION_NAME = "version_name";
	public static final String PERCENT = "percent";
	public static final String DOWNLOAD_ITEM = "item";
	public static final String SPEED = "speed";
	public static final String TEMP_SUFFIX = ".cyou";
	public static final String DOWNLOAD_COUNT = "downloadCount";
	public static final String UPDATE_PROGRESS_ACTION = "com.cyou.mrd.pengyou.download.update_progress";
	public static final String INSTALL_DONE_GAMECODE="installedGame";
	public static class TASK{
		public static final int ADD = 0;
		public static final int PAUSE = 1;
		public static final int CONTINUE = 2;
		public static final int DELETE = 3;
		public static final int PAUSE_ALL = 4;//暂停所有
		public static final int CONTINUE_ALL=5;//继续所有
		public static final int DELETE_ALL=6;//删除所有
		public static final int DOWNLOAD_FIRST = 7;//优先下载
		public static final int WAITING_PAUSE = 8;//从等待变为等待暂停
//		public static final int PASE_WAITING=9;//从等待暂停变为等待
		public static final int START_SERVICE=10;//手动开启线程 
		public static final int DONE = 11;//下载完成
		public static final int HAD_CONTAIN_TASK = 12;//已经有了该下载任务
	}
	
	public static class ERROR{
		//文件已经被下载
		public static final int FILE_HAD_DOWNLOAD = -2;
		//下载未知异常
		public static final int DOWNLOAD_ERROR = -1;
		//存储空间不足
		public static final int NO_MEMORY = -3;
		//下载地址问题
		public static final int URL_ERROR=-4;
		//网络中断
		public static final int NETWORK_INTERRUPT=-5;
		//下载完成
		public static final int DOWNLOAD_FINISH=-6;
	}
	
	public static class C_STATE {
		public static final int ADD = -1;//添加了新的下载任务
		public static final int CON = -2;//继续下载被暂停的下载任务
		public static final int PAUSE = -3;//暂停了正在下载的下载任务
		public static final int DELETE = -4;//删除了某个下载任务
		public static final int INSTALLED = -5;//下载并安装成功
		public static final int DOWNLOADING = -6;//正在下载
		public static final int DONE = -7;//下载完成
		public static final int WAITING = -8;//等待被下载
		public static final int DETAULT = 0;
		
	}
	
}
