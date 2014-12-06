package com.cyou.mrd.pengyou.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.ui.LaunchActivity;
import com.tessarmobile.sdk.TessarMobileSDK;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author user
 * 
 */
public class LogHandler implements UncaughtExceptionHandler {

	public static final String TAG = "CrashHandler";

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static LogHandler INSTANCE = new LogHandler();
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private LogHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static LogHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	PendingIntent restartIntent;

	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Intent intent = new Intent();
		// 参数1：包名，参数2：程序入口的activity
		// intent.setClassName(this.getPackageName(),
		// this.getPackageName()+".LaunchActivity.class");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(mContext, LaunchActivity.class);
		restartIntent = PendingIntent.getActivity(
				mContext.getApplicationContext(), 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
//		try {
//			TessarMobileSDK.getInstance(CyouApplication.getMainActiv(), CyouApplication.getMainActiv().getString(R.string.app_name)
//					, CyouApplication.getChannel()
//					, CyouApplication.getMainActiv().getPackageManager().getPackageInfo(CyouApplication.getMainActiv().getPackageName(), 0).versionName
//					, 5).onException();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			mDefaultHandler.uncaughtException(thread, ex);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(1);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		ex.printStackTrace();
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				/**
				 * 暂时支持Toast提示
				 */
				// Activity currentAct = CyouApplication.getCurrentActivity();
				// if (null == currentAct) {
				// Toast.makeText(mContext,
				// mContext.getString(R.string.app_error),
				// Toast.LENGTH_LONG).show();

				// } else {
				// // currentAct.setContentView(R.layout.error);
				// MessageBox.show(currentAct,
				// mContext.getString(R.string.app_error),
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface arg0,
				// int arg1) {
				// // 退出程序
				// AlarmManager mgr = (AlarmManager) mContext
				// .getSystemService(Context.ALARM_SERVICE);
				// mgr.set(AlarmManager.RTC,
				// System.currentTimeMillis() + 1000,
				// restartIntent); // 1秒钟后重启应用
				// android.os.Process
				// .killProcess(android.os.Process
				// .myPid());
				// System.exit(1);
				// }
				// });
				// }
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
//		if (PYVersion.DEBUG) {
			saveCrashInfo2File(ex);
//		}
		// 退出程序
		AlarmManager mgr = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		// mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
		// restartIntent); // 1秒钟后重启应用
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex) {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				long timestamp = System.currentTimeMillis();
				String time = formatter.format(new Date());
				String fileName = "crash-" + time + "-" + timestamp + ".log";
				String rootPath = SharedPreferenceUtil
						.getRootPath(CyouApplication.mAppContext);
				File path = new File(rootPath + Contants.PATH.ERRORLOG_PATH);
				if (!path.exists()) {
					path.mkdirs();
				}
				File file = new File(rootPath + Contants.PATH.ERRORLOG_PATH,
						fileName);
				if (!file.exists())
					file.createNewFile();
				FileWriter filewriter = new FileWriter(file, true);
				filewriter.write(sb.toString());
				filewriter.close();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
