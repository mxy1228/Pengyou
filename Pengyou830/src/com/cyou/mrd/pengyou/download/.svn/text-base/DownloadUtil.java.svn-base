package com.cyou.mrd.pengyou.download;

import android.os.Environment;
import android.os.StatFs;

public class DownloadUtil {

	private static final String URL = "url";
	
	/**
	 * 将该Task对应的url存储到SP上
	 * @param task
	 * @param context
	 * @param index
	 */
//	public static boolean storeUrl(DownloadTask task,Context context,int index){
//		SharedPreferences sp = context.getSharedPreferences(ApplicationContants.SP_NAME.DOWNLOAD
//				, Context.MODE_WORLD_READABLE);
//		Editor e = sp.edit();
//		e.putString(URL+index, task.getDownloadItem().getmURL());
//		return e.commit();
//	}
	
	/**
	 * 获取存储介质的剩余空间
	 * @return
	 */
	public static long getAvailableStorage() {

        String storageDirectory = null;
        storageDirectory = Environment.getExternalStorageDirectory().toString();

        try {
            StatFs stat = new StatFs(storageDirectory);
            long avaliableSize = ((long) stat.getAvailableBlocks() * (long) stat.getBlockSize());
            return avaliableSize;
        } catch (RuntimeException ex) {
            return 0;
        }
    }
	
	/**
	 * 获取DownloadItem对应的临时文件
	 * @param item
	 * @return
	 */
//	public static File getTempFile(String packageName){
//		if(!TextUtils.isEmpty(Utils.getRootPath())
//				&& !TextUtils.isEmpty(ApplicationContants.FLODER_NAME.APK_PATH)
//				&& !TextUtils.isEmpty(DownloadParam.TEMP_SUFFIX)
//				&& !TextUtils.isEmpty(packageName)){
//			return new File(Utils.getRootPath()+ApplicationContants.FLODER_NAME.APK_PATH, packageName 
//					+ DownloadParam.TEMP_SUFFIX);
//		}
//		return null;
//	}
	
	/**
	 * 获取DownloadItem对应的已下载完成的文件
	 * @param item
	 * @return
	 */
//	public static File getFile(String packageName){
//		if(!TextUtils.isEmpty(packageName)){
//			if(!TextUtils.isEmpty(Utils.getRootPath())
//					&& !TextUtils.isEmpty(ApplicationContants.FLODER_NAME.APK_PATH)){
//				return new File(Utils.getRootPath()+ApplicationContants.FLODER_NAME.APK_PATH, packageName);
//			}
//		}
//		return null;
//	}
	
	/**
	 * 删除url
	 */
//	public static boolean deleteUrl(int index,String url,Context context){
//		SharedPreferences sp = context.getSharedPreferences(ApplicationContants.SP_NAME.DOWNLOAD
//				, Context.MODE_WORLD_READABLE);
//		Editor e = sp.edit();
//		e.remove(URL+index);
//		return e.commit();
//	}
}
