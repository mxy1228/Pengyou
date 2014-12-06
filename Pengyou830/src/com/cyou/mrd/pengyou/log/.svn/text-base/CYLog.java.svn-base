package com.cyou.mrd.pengyou.log;

import android.util.Log;

import com.cyou.mrd.pengyou.config.PYVersion;

public class CYLog{
//	private static final String FORMAT = "yyyyMMdd-HH:mm:ss";
//	private static final String SPLIT = "\n-------------------------------------------------------------------------\n";
	private static String TAG = "com.cyou.mrd.pengyou";
	private static CYLog log;
	
	private CYLog(){
		
	}
	
	public static CYLog getInstance(){
		if(log == null){
			log = new CYLog();
		}
		TAG=getCurrentInfo();
		return log;
	}

	private void debug(String tag,Object obj) {
		String funName = getFunName();
		String info = (funName == null ? obj.toString() : (funName + ":" + obj.toString()));
		Log.d(tag, info);
	}
	/**
	 * 当前日志行号
	 * @return
	 */
	private static String getCurrentInfo() {

		StackTraceElement[] eles = Thread.currentThread().getStackTrace();
		StackTraceElement targetEle = eles[5];
		String info = "(" + targetEle.getClassName() + "."
				+ targetEle.getMethodName() + ":" + targetEle.getLineNumber()
				+ ")";
		return info;
	}

	private void error(String tag,Exception exception) {
		try {
			Log.e(tag, "", exception);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void error(String tag,Object obj) {
		try {
			if (obj != null) {
				String funName = getFunName();
				String info = (funName == null ? obj.toString() : (funName+ ":" + obj.toString()));
				Log.e(tag, info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private String getFileName(String s) {
//		SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyyMMdd");
//		StringBuffer stringbuffer = new StringBuffer();
//		stringbuffer.append(simpledateformat.format(new Date())).append(s);
//		return stringbuffer.toString();
//	}

	private String getFunName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return "[" + Thread.currentThread().getId() + ":"
					+ st.getFileName() + ":" + st.getLineNumber() + "]";
		}
		return null;
	}

//	private String getTimerFileName() {
//		SimpleDateFormat simpledateformat = new SimpleDateFormat(FORMAT);
//		StringBuffer stringbuffer = new StringBuffer();
//		stringbuffer.append(simpledateformat.format(new Date())).append(
//				"Timer.log");
//		return stringbuffer.toString();
//	}

	private synchronized void verbose(String tag,Object obj) {
		String funName = getFunName();
		String info = (funName == null ? obj.toString() : (funName + ":" + obj
				.toString()));
		Log.v(tag, info);
	}

	private synchronized void warn(String tag,Object obj) {
		String funName = getFunName();
		String info = (funName == null ? obj.toString() : (funName + ":" + obj
				.toString()));
		Log.w(tag, info);
	}

	public synchronized void d(Object obj) {
		if (PYVersion.DEBUG){
			debug(TAG,obj);
		}
	}

	public synchronized void e(Exception exception) {
		if (PYVersion.DEBUG){
			error(TAG,exception);
		}
	}

	public synchronized void e(Object obj) {
		if (PYVersion.DEBUG){
			error(TAG,obj);
		}
	}

	public synchronized void i(Object obj) {
		if(PYVersion.DEBUG){
			String funName = getFunName();
			String info = (funName == null ? obj.toString() : (funName + ":" + obj
					.toString()));
			Log.i(TAG, info);
		}
	}

	public synchronized void v(Object obj) {
		if (PYVersion.DEBUG){
			verbose(TAG,obj);
		}
	}

	public synchronized void w(Object obj) {
		if (PYVersion.DEBUG){
			warn(TAG,obj);
		}
	}

}
