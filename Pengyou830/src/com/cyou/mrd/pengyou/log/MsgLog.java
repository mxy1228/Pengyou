package com.cyou.mrd.pengyou.log;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

import com.cyou.mrd.pengyou.config.PYVersion;

public class MsgLog {
	private static String TAG = "com.cyou.mrd.pengyou";
	private static MsgLog log;
	private SimpleDateFormat mFormat;
	
	private MsgLog(){
	}
	
	public static MsgLog getInstance(){
		if(log == null){
			log = new MsgLog();
		}
		return log;
	}
	
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
	
	public synchronized void v(Object obj) {
		if (PYVersion.DEBUG){
			verbose(TAG,obj);
		}
	}
	
	private synchronized void verbose(String tag,Object obj) {
		String funName = getFunName();
		String info = (funName == null ? obj.toString() : (funName + ":" + obj
				.toString()));
		Log.v(tag, info);
		try {
			if(PYVersion.DEBUG){
				mFormat = new SimpleDateFormat("yyyy-MM-dd");
				File dir = new File(Environment.getExternalStorageDirectory()+"/PYDebug");
				File file = new File(dir, "CYMsg"+mFormat.format(new Date())+".txt");
				if(!file.exists()){
					file.exists();
				}
				if(file.exists()){
					FileWriter writer = new FileWriter(file, true);
					writer.write(info);
					writer.write("\n");
					writer.flush();
					writer.close();
				}
			}
		} catch (Exception e) {
		}
	}
	
	public void test(){
		
	}
}
