package com.cyou.mrd.pengyou.widget;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * 提示框
 * 
 * @author Administrator
 * 
 */
public class MessageBox {

	public static void show(Context context, String message,
			final OnClickListener yesListener, final OnClickListener noListener) {
		Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setCancelable(true);
		builder.setPositiveButton("确定", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (yesListener != null) {
					yesListener.onClick(dialog, which);
				}

			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (noListener != null) {
					noListener.onClick(dialog, which);
				}
			}

		});

		builder.show();

	}

	public static void show(Context context, String title, String message,
			final OnClickListener yesListener, final OnClickListener noListener) {
		Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (yesListener != null) {
					yesListener.onClick(dialog, which);
				}

			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (noListener != null) {
					noListener.onClick(dialog, which);
				}
			}

		});

		builder.show();

	}

	public static void show(Context context, String message,
			final OnClickListener okListener) {

		Builder builder = new Builder(context);
		builder.setMessage(message);
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				if (okListener != null) {
					okListener.onClick(dialog, which);
				}
			}
		});

		builder.show();

	}

}
