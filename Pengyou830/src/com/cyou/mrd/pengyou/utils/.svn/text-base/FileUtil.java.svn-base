package com.cyou.mrd.pengyou.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.http.MyHttpConnect.FormFile;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FileUtil {
	/**
	 * 下载网络图片到指定的目录下
	 * 
	 * @param strUrl
	 * @param savaPath
	 * @return
	 */
	public static void downloadBitmap(String strUrl, String savaPath) {
		if (TextUtils.isEmpty(strUrl)) {
			return;
		}
		// Bitmap bitmap = null;
		URL imageUrl = null;
		try {
			imageUrl = new URL(strUrl);
		} catch (MalformedURLException e) {
			CYLog.getInstance().e(e);
		}
		InputStream is = null;
		File file = null;
		FileOutputStream fOut = null;
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) imageUrl.openConnection();
			conn.connect();
			is = conn.getInputStream();
			file = new File(savaPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			fOut = new FileOutputStream(file);
			// bitmap = BitmapFactory.decodeStream(is);
			// bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			// return bitmap;
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (null != fOut) {
					fOut.flush();
					fOut.close();
					fOut = null;
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				CYLog.getInstance().e(e);
			}
		}
	}

	/**
	 * 计算文件夹文件大小 只为判断该目录下是否有文件
	 * 
	 * @param folder
	 * @return
	 */
	public static long getFileSize(File folder) {
		long foldersize = 0;
		File[] filelist = folder.listFiles();
		for (int i = 0; i < filelist.length; i++) {
			if (filelist[i].isDirectory()) {
				foldersize += getFileSize(filelist[i]);
			} else {
				foldersize += filelist[i].length();
			}
			if (foldersize > 0) {
				return foldersize;
			}
		}
		return foldersize;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		if (w >= h && (w / h) > 3) {
			int lowerBound = 2;
			return lowerBound;
		} else if (w < h && (h / w) > 3) {
			int lowerBound = 2;
			return lowerBound;
		}
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	public static Bitmap getBitmapFromFile(File dst, int width, int height) {
		if (null != dst && dst.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				return BitmapFactory.decodeFile(dst.getPath(), opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static Bitmap getBitmapFromFilePath(String filePath, int width,
			int height) {
		File file = new File(filePath);
		InputStream is = null;
		if (file.exists()) {
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(filePath, opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength,
						width * height);
				opts.inDither = false;// 使图片不抖动。
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				is = new FileInputStream(file);
				return BitmapFactory.decodeStream(is, null, opts);
				// return BitmapFactory.decodeFile(filePath, opts);
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			} catch (Exception e) {
				CYLog.getInstance().e(e);
			}
		}
		return null;
	}

	public static Bitmap readBitMap(File file) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		InputStream is = null;
		opt.inDither = false;// 使图片不抖动。
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		try {
			is = new FileInputStream(file);
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		}
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static Bitmap zoomImage(File file) {
		Bitmap bitmap = null;
		if (file != null && file.exists()) {
			bitmap = readBitMap(file);
			Long filesize = file.length();
			double mid = filesize / 1024;
			if (mid > 650) {
				// 获取bitmap大小 是允许最大大小的多少倍
				double i = mid / 650;
				// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
				// （1.保持宽度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
				bitmap = ImageUtil.zoomImage(bitmap, bitmap.getWidth() / i,
						bitmap.getHeight() / i);
			}

		}
		return bitmap;
	}

	public static Bitmap createBitMap(File file) {
		Matrix matrix = null;
		ExifInterface exifInterface = null;
		Bitmap bitmap = null;
		int width = 0;
		int height = 0;
		try {
			// is = new FileInputStream(file);
			// 获取图片的旋转角度
			if (file.exists()) {
				bitmap = readBitMap(file);
			}
			/**************************
			 * 解决三星手机图片旋转问题 start *
			 *************************/
			if (bitmap != null) {
				width = bitmap.getWidth();
				height = bitmap.getHeight();
			} else {
				bitmap = readBitMap(file);
				if (bitmap != null) {
					width = bitmap.getWidth();
					height = bitmap.getHeight();

				}
			}
			exifInterface = new ExifInterface(file.getAbsolutePath());
			int tag = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			int orientation = 0;
			matrix = new Matrix();
			if (tag == 0) {
				return bitmap;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
				orientation = 90;
				matrix.setRotate(orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				return bitmap;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
				orientation = 180;
				matrix.setRotate(orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				return bitmap;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
				orientation = 270;
				matrix.setRotate(orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				return bitmap;
			}

			/*************************
			 * 解决三星手机图片旋转问题 end *
			 ************************/
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		} catch (OutOfMemoryError e) {
			CYLog.getInstance().e(e);
		}
		return bitmap;
	}

	/**
	 * 解决图片旋转问题
	 * 
	 * @param bitmap
	 * @param imagePath
	 * @return
	 */
	public static Bitmap setRotateBitmap(Bitmap bitmap, String imagePath) {
		Matrix matrix = null;
		ExifInterface exifInterface = null;
		// Bitmap bmp = null;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		try {
			exifInterface = new ExifInterface(imagePath);
			int tag = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION, -1);
			int orientation = 0;
			matrix = new Matrix();
			if (tag == 0) {
				return bitmap;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_90) {
				orientation = 90;
				matrix.setRotate(orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				return bitmap;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_180) {
				orientation = 180;
				matrix.setRotate(orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				return bitmap;
			} else if (tag == ExifInterface.ORIENTATION_ROTATE_270) {
				orientation = 270;
				matrix.setRotate(orientation);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
						matrix, true);
				return bitmap;
			}

		} catch (Exception e) {
			CYLog.getInstance().e(e);
		} catch (OutOfMemoryError e) {
			CYLog.getInstance().e(e);
		}
		return bitmap;
	}

	public int copy(String fromFile, String toFile) {
		// 要复制的文件目录
		File[] currentFiles;
		File root = new File(fromFile);
		// 如同判断SD卡是否存在或者文件是否存在
		// 如果不存在则 return出去
		if (!root.exists()) {
			return -1;
		}
		// 如果存在则获取当前目录下的全部文件 填充数组
		currentFiles = root.listFiles();

		// 目标目录
		File targetDir = new File(toFile);
		// 创建目录
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		// 遍历要复制该目录下的全部文件
		for (int i = 0; i < currentFiles.length; i++) {
			if (currentFiles[i].isDirectory())// 如果当前项为子目录 进行递归
			{
				copy(currentFiles[i].getPath() + "/",
						toFile + currentFiles[i].getName() + "/");

			} else// 如果当前项为文件则进行文件拷贝
			{
				copySdcardFile(currentFiles[i].getPath(), toFile
						+ currentFiles[i].getName());
			}
		}
		return 0;
	}

	// 文件拷贝
	// 要复制的目录下的所有非子目录(文件夹)文件拷贝
	public static int copySdcardFile(String fromFile, String toFile) {

		try {
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;

		} catch (Exception ex) {
			CYLog.getInstance().e(ex);
			return -1;
		}
	}

	/**
	 * 后台上传日志
	 */
	public static void uploadLogFile() {
//		if (!CyouApplication.TIMER_SWITCH) {
//			return;
//		}
		try {
			// 优化上传日志文件 待测试3g,网络下上传情况
			File filePath = new File(SharedPreferenceUtil.getBehaviorPath());
			if (filePath == null || !filePath.exists()) {
				return;
			}
			File[] logFiles = filePath.listFiles();
			if (null == logFiles || logFiles.length == 0) {
				return;
			}
			for (int i = 0; i < logFiles.length; i++) {
				if (i > CYSystemLogUtil.FILE_MAXNUM) {// 当读取文件大于最大读取数时,停止读取
					break;
				}
				final File logFile = logFiles[i];
				if (null == logFile
						|| !logFile.exists()
						|| logFile.length() > 1024 * 1024 * 50
						|| !logFile.getName().contains(
								CYSystemLogUtil.ARGEENMENT_USER)) { // 进行文件过滤
					logFile.delete();
					continue;
				}
				RequestParams params = new RequestParams();
				params.put("logfile", logFile);
				MyHttpConnect.getInstance().post(HttpContants.NET.UPLOAD_LOG,
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String content) {
								CYLog.getInstance().d("上传用户行为统计:" + content);
								try {
									String result=JsonUtils.getJsonValue(content, "successful");
									if ("1".equals(result)) {
										logFile.delete();
									}
								} catch (Exception e) {
									CYLog.getInstance().e(e);
								}
								super.onSuccess(content);
							}

							@Override
							public void onFailure(Throwable error) {
								if (null != error) {
									CYLog.getInstance()
											.d("上传用户行为统计错误:" + error.getMessage());
								}
								super.onFailure(error);
							}
						});
			}
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		}
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				List<FormFile> formFileLst = new ArrayList<FormFile>();
//				File filePath = new File(SharedPreferenceUtil.getBehaviorPath());
//				if (filePath == null || !filePath.exists()) {
//					return;
//				}
//				File[] logFiles = filePath.listFiles();
//				if (null == logFiles || logFiles.length == 0) {
//					return;
//				}
//				for (int i = 0; i < logFiles.length; i++) {
//					if (i > CYSystemLogUtil.FILE_MAXNUM) {// 当读取文件大于最大读取数时,停止读取
//						break;
//					}
//					File logFile = logFiles[i];
//					if (null == logFile
//							|| !logFile.exists()
//							|| logFile.length() > 1024 * 1024 * 50
//							|| !logFile.getName().contains(
//									CYSystemLogUtil.ARGEENMENT_USER)) { // 进行文件过滤
//						logFile.delete();
//						continue;
//					}
//					FormFile formFile = new FormFile();
//					formFile.setName("logfile");
//					formFile.setFile(logFile);
//					formFileLst.add(formFile);
//
//				}
//				if (null == formFileLst || formFileLst.size() == 0) {
//					return;
//				}
//				String result;
//				Map<String, String> paramsMap = new HashMap<String, String>();
//				paramsMap.put(Params.HttpParams.UAUTH, UserInfoUtil.getUauth());
//				paramsMap.put(Params.HttpParams.UID,
//						String.valueOf(UserInfoUtil.getCurrentUserId()));
//				paramsMap.put(Params.HttpParams.PLATFORM,
//						Params.HttpParams.PLATFORM_VALUE);
//				paramsMap.put(Params.HttpParams.UDID, Util.getUDIDNum());
//				try {
//					result = MyHttpConnect
//							.postAsMultipart(HttpContants.NET.UPLOAD_LOG,
//									paramsMap, formFileLst);
//					JSONObject obj = new JSONObject(result);
//					if (obj.has("successful")) {
//						String avatar = obj.getString("successful");
//						if ("1".equals(avatar)) {
//							for (int i = 0; i < formFileLst.size(); i++) {
//								File mFile = formFileLst.get(i).getFile();
//								if (null != mFile && mFile.exists()) {
//									mFile.delete();
//									mFile = null;
//								}
//							}
//						}
//					}
//				} catch (Exception e) {
//					CYLog.getInstance().e(e);
//				}
//			}
//		}).start();
	}
}
