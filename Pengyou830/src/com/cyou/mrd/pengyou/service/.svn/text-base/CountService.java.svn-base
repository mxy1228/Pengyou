package com.cyou.mrd.pengyou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.CountDownTimer;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.db.MyGamePlayRecordDao;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;

public class CountService extends Service {

	Timer timer;
	private Map<String, Long> mGamePlayTimeMap = new HashMap<String, Long>();
	private String beforeGameName;
	private String afterGameName;
	String packageName_PY;
	private static final int NOTIFICATION = -1;
	private static final int MEMORY_CLEAR = 0;
	private CYLog log = CYLog.getInstance();
	private MyGamePlayRecordDao mMyGamePlayRecordDao;
	private MyGameDao mMyGameDao;
	PackageManager pm;
	private long time = 4000;
	private WindowManager wm;
	private View view;
	ActivityManager mActivityManager;
	private  String uid;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		log.i("jifen 开始创建计数器,并且提升优先级，不让随便被杀死");
		setForeground();
		timer = new Timer();
		mMyGamePlayRecordDao = new MyGamePlayRecordDao(getApplicationContext());
		mMyGameDao = new MyGameDao(getApplicationContext());
        try {
			packageName_PY = getApplication().getPackageManager()
					.getPackageInfo(getApplicationInfo().packageName, 0).packageName;
			beforeGameName = packageName_PY;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        pm = getPackageManager();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		log.i("jifen  开始启动计数器,清进程");
		close();
		memoryClear();
		//凡是从朋游启动的游戏，就要存入到map中去
        if (intent != null) {
            String identifier = intent
                    .getStringExtra("identifier");
            if (identifier != null) {
            	log.i("jifen  存入包名和点击启动的时间" + mGamePlayTimeMap.toString());
            	mGamePlayTimeMap.put(identifier, System.currentTimeMillis());//存入包名和点击启动的时间
            }
        }
        uid = String.valueOf(UserInfoUtil.getCurrentUserId());
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				afterGameName = mActivityManager.getRunningTasks(1).get(0).topActivity
						.getPackageName();
				if (!afterGameName.equals(beforeGameName) ) {
					log.i("jifen  app发生切换了, 切换后的包名： " + afterGameName + " 切换前的包名： " + beforeGameName + "  uid:" + uid);
					if(mGamePlayTimeMap.containsKey(beforeGameName))//如果上一个游戏是从朋游启动的，就更新DB和map
					{
						long recentBeginPlayDate = mGamePlayTimeMap.get(beforeGameName);//启动时间点
						//更新上次玩的游戏的数据库中的结束时间以及玩的时长以及更新积分
						//将刚刚玩的那款游戏的启动时间和结束时间 插入在数据库中的这款游戏的时间片list中
						mMyGamePlayRecordDao.insert(beforeGameName, recentBeginPlayDate, System.currentTimeMillis(),uid);
						log.i("jifen  mMyGameDao.updateLastPlayInfo");
						mMyGameDao.updateLastPlayInfo(beforeGameName, recentBeginPlayDate, System.currentTimeMillis());
					}
                    //如果置为顶的是朋游启动的之前放在后台的游戏，就更新它的玩的起始时间
					if(mGamePlayTimeMap.containsKey(afterGameName)) {
						//如果是从朋游启动的并置为后台的游戏又被置为前台
						log.i("jifen  置为顶的是朋游启动的之前放在后台的游戏，就更新它的玩的起始时间");
						mGamePlayTimeMap.put(afterGameName, System.currentTimeMillis());//保存这个刚置为top的游戏的起始时间
					}
					beforeGameName = afterGameName;
				}
			}}, 0, 1000);
		startForeground(1, new Notification());
	}

	@Override
	public void onDestroy() {
		log.i("jifen  终止计数器");
		super.onDestroy();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NOTIFICATION: 
				log.i("jifen 被杀死要重启了");
				break;
			case MEMORY_CLEAR:
				int memorykb = msg.arg1;
				log.i("jifen  朋游释放");
                showDialog(memorykb);
				break;
		    default:break;
			}
		}
	};

	private void setForeground()
    {
            Notification note = new Notification(0, null, System.currentTimeMillis());
            note.flags |= Notification.FLAG_NO_CLEAR;
            startForeground(1339, note);
    }
	/**
	 * 用户应用程序的过滤器
	 * 
	 * @param info
	 * @return true 用户应用 false 系统应用
	 */
	private boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			// 代表的是系统的应用,但是被用户升级了. 用户应用
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			// 代表的用户的应用
			return true;
		}
		return false;
	}
	private void memoryClear() {
		try {
			List<RunningAppProcessInfo> processinfos = mActivityManager
					.getRunningAppProcesses();
			int memorykb = 0;
			for (RunningAppProcessInfo info : processinfos) {
				if (info.processName.contains(packageName_PY) 
					|| "com.tencent.mm:push".contains(info.processName)
					|| "com.tencent.mqq:push".contains(info.processName)
					|| "com.tencent.mobileqq:push".contains(info.processName)
						) {
					log.i("clearMemory  朋游/微信/QQ  = " + info.processName);
					continue;
				}
				try {
					PackageInfo packageinfo = pm.getPackageInfo(info.processName, 0);
					log.d("check--->" + filterApp(packageinfo.applicationInfo) + " : " + packageinfo.packageName);
					if(!filterApp(packageinfo.applicationInfo)){
						log.i("clearMemory  不清除  info.processName = " + info.processName);
						continue;
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				log.i("clearMemory  清除  info.processName = " + info.processName);
				mActivityManager.killBackgroundProcesses(info.processName);
				MemoryInfo[] memoryinfos = mActivityManager
						.getProcessMemoryInfo(new int[] { info.pid });
				memorykb += memoryinfos[0].getTotalPrivateDirty();
			}
			Message msg = new Message();
			msg.what = MEMORY_CLEAR;
			msg.arg1 = memorykb;
			mHandler.sendMessage(msg);
		} catch (Exception e) {
			// TODO: handle exception
			log.e(e);
		}
	}
	
	

	/**
	 * 定时器
	 */
	private void start() {
		/**
		 * 初始化定时器，第一个参数为设定的时间，第二参数为设定的提醒时间
		 */
		CountDownTimer timer = new CountDownTimer(time, time) {

			/**
			 * 提醒时间到，调用此方法
			 */
			public void onTick(long millisUntilFinished) {
//				showDialog();
			}

			/**
			 * 定时时间到，调用此方法
			 */
			public void onFinish() {
				close();

			}
		};
		// 开启定时器
		timer.start();
	}

	/**
	 * 关闭弹窗
	 */
	private void close() {
		// this.dismiss();
		if (view != null) {
			wm.removeView(view);
			view = null;
		}
	}

	/**
	 * 设置定时时间，单位：毫秒
	 * 
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
	}

	private void showDialog(long memory) {
        try {
        	memory = memory * 1024;
        	long tenMB = 10*1024*1024;
        	if(memory < tenMB){
        		memory = tenMB + memory;
        	}
			String mLenString = Formatter.formatFileSize(
					getApplicationContext(), memory);
			String content = getApplicationContext().getResources().getString(
					R.string.clear_memory_dialog_content, mLenString);
			log.v("showDialog content = " + content);
			view = View.inflate(getApplicationContext(),
					R.layout.clear_memory_dialog, null);
			// 从sp里面获取一下 要显示的风格
			// int index = sp.getInt("address_style", 0);
			// String[] items ={"半透明","活力橙","卫士蓝","苹果绿"};
			// view.setBackgroundResource(drawables[index]);
			TextView tv = (TextView) view
					.findViewById(R.id.tv_show_memory_info);
			//朋游为您释放了%1$s内存 ，玩游戏更顺畅
			SpannableStringBuilder ssb = new SpannableStringBuilder(content);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")),
					0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#fff000")),
					7, mLenString.length() + 7,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")),
					mLenString.length() + 7, content.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv.setText(ssb);
			WindowManager.LayoutParams params = new LayoutParams();
			params.height = WindowManager.LayoutParams.WRAP_CONTENT;
			params.width = WindowManager.LayoutParams.FILL_PARENT;
			params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
			params.format = PixelFormat.TRANSLUCENT;
			params.type = WindowManager.LayoutParams.TYPE_TOAST;
			params.setTitle("Toast");
			int x = 0;// sp.getInt("lastx", 0);
			int y = 0;// sp.getInt("lasty", 0);
			params.gravity = Gravity.TOP | Gravity.LEFT; // 设置布局的坐标是从左上角开始的
			params.x = x;
			params.y = y;
			wm.addView(view, params);
			start();
		} catch (Exception e) {
			// TODO: handle exception
			log.e(e);
		}
	}
}
