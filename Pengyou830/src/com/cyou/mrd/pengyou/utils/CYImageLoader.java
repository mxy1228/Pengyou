package com.cyou.mrd.pengyou.utils;

import java.io.File;
import java.net.URL;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CYImageLoader {

//	public static void displayIconImage(String url, ImageView iv) {
//		StringBuilder sb = new StringBuilder();
//		if (url != null && !TextUtils.isEmpty(url)) {
//			sb.append(HttpContants.ICON_HOST + url);
//		} else {
//			sb.append("");
//		}
//		ImageLoader.getInstance().displayImage(sb.toString(), iv);
//	}

	public static void displayIconImage(String uri, ImageView imageView,
			DisplayImageOptions options) {
		StringBuilder sb = new StringBuilder();
		if (uri != null && !TextUtils.isEmpty(uri)) {
			sb.append(PYVersion.IP.ICON_HOST + uri);
		} else {
			sb.append("");
		}
		ImageLoader.getInstance().displayImage(sb.toString(), imageView,
				options);
	}
	
//	public static void displaySpecialIconImage(String uri, ImageView imageView,
//			DisplayImageOptions options) {
//		StringBuilder sb = new StringBuilder();
//		if (uri != null && !TextUtils.isEmpty(uri)) {
//			sb.append("http://10.127.131.22/" + uri);
//		} else {
//			sb.append("");
//		}
//		ImageLoader.getInstance().displayImage(sb.toString(), imageView,
//				options);
//	}
	//game detail scrollview
	public static void displayIconImage(String uri, ImageView imageView,
			DisplayImageOptions options, ImageLoadingListener listener) {
		StringBuilder sb = new StringBuilder();
		if (uri != null && !TextUtils.isEmpty(uri)) {
			sb.append(PYVersion.IP.ICON_HOST + uri);
		} else {
			sb.append("");
		}
		ImageLoader.getInstance().displayImage(sb.toString(), imageView,
				options, listener);
	}	
	public static void displayImg(String uri,ImageView imageView,DisplayImageOptions options){
		StringBuilder sb = new StringBuilder();
		if (uri != null && !TextUtils.isEmpty(uri)) {
			sb.append(PYVersion.IP.IMG_HOST + uri);
		} else {
			sb.append("");
		}
		ImageLoader.getInstance().displayImage(sb.toString(), imageView,
				options);
	}
	/**
	 * 游戏库顶部轮播图
	 * @param uri
	 * @param imageView
	 * @param options
	 */
	public static void displayGameImg(String uri,ImageView imageView,DisplayImageOptions options){
		StringBuilder sb = new StringBuilder();
		if (uri != null && !TextUtils.isEmpty(uri)) {
			sb.append(PYVersion.IP.GAME_IMG_IP + uri);
		} else {
			sb.append("");
		}
		ImageLoader.getInstance().displayImage(sb.toString(), imageView,
				options);
	}
	public static void displayOtherImage(String uri, ImageView imageView,
			DisplayImageOptions options) {
		StringBuilder sb = new StringBuilder();
		if (uri != null && !TextUtils.isEmpty(uri)) {
			sb.append(uri);
		} else {
			sb.append("");
		}
		ImageLoader.getInstance().displayImage(sb.toString(), imageView,
				options);
	}

	public static void displayIconImage(String uri, ImageView imageView,
			ImageLoadingListener listener) {
		StringBuilder sb = new StringBuilder();
		if (uri != null && !TextUtils.isEmpty(uri)) {
			sb.append(PYVersion.IP.ICON_HOST + uri);
		} else {
			sb.append("");
		}
		ImageLoader.getInstance().displayImage(sb.toString(), imageView,
				listener);
	}
/////
	public static void displayImg(String uri, ImageView imageView,
			DisplayImageOptions options, ImageLoadingListener listener) {
		StringBuilder sb = new StringBuilder();
		if (uri != null && !TextUtils.isEmpty(uri)) {
			sb.append(PYVersion.IP.IMG_HOST + uri);
		} else {
			sb.append("");
		}
		ImageLoader.getInstance().displayImage(sb.toString(), imageView,
				options, listener);
	}

	public static File getImageFileByUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		File mFile = ImageLoader.getInstance().getDiscCache().get(url);
		return mFile;
	}

	public static Bitmap getImageBitmapByUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		Bitmap mBitmap = ImageLoader.getInstance().getMemoryCache().get(url);
		return mBitmap;
	}
	
	/**
	 * 获取并展示本地存储图片
	 * @param filePath 本地图片路径
	 * @param iv
	 * @param options
	 */
	public static void displayImgFromSDCard(String filePath,ImageView iv,DisplayImageOptions options){
		ImageLoader.getInstance().displayImage(new StringBuilder().append("file://").append(filePath).toString()
				, iv
				, options);
	}

	public static void displayImgFromSDCard(String filePath,ImageView iv,DisplayImageOptions options, ImageLoadingListener listener){
		ImageLoader.getInstance().displayImage(new StringBuilder().append("file://").append(filePath).toString()
				, iv
				, options,listener);
	}
}
