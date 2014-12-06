package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.widget.Toast;

/**
 * 解决Toast重复显示问题
 * 
 * @author wangkang
 * 
 */
public class ViewToast {
	private static Context context = null;

	private static Toast toast = null;

	/**
	 * 若context相同,则关闭上次的
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 * @return
	 */

	public static Toast getToast(Context context, String text, int duration) {

		if (ViewToast.context == context) {
			toast.cancel();
			toast = Toast.makeText(context, text, duration);
		} else {
			ViewToast.context = context;
			toast = Toast.makeText(context, text, duration);
		}

		return toast;

	}

	public static void showToast(Context mContext, String text, int duration) {
		getToast(mContext, text, duration).show();
	}

	public static void showToast(Context mContext, int resId, int duration) {
		showToast(mContext, mContext.getResources().getString(resId), duration);
	}

	public static void cancelToast(Context mContext) {
		if(ViewToast.context == mContext){
			if(toast != null)
			toast.cancel();
		}
	}
}
