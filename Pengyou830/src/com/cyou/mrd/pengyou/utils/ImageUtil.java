package com.cyou.mrd.pengyou.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.cyou.mrd.pengyou.log.CYLog;

public class ImageUtil {
	public static final int MAX_IMAGE = 650;

	/**
	 * 把字节数组保存为一个文件 b 字节流 outputFile 生成文件的路径、文件名
	 */

	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					CYLog.getInstance().e(e1);
				}
			}
		}
		return file;
	}

	/**
	 * 将Bitmap转化为字节数组
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmap2byte(Bitmap bitmap) {
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			byte[] array = baos.toByteArray();
			baos.flush();
			baos.close();
			return array;
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		}
		return null;

	}

	/**
	 * 将byte数组转化为bitmap
	 * 
	 * @param data
	 * @return
	 */
	public static Bitmap byte2bitmap(byte[] data) {
		if (null == data) {
			return null;
		}
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	/**
	 * 将Drawable转化为Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawable2bitmap(Drawable drawable) {
		if (null == drawable) {
			return null;
		}
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);// 重点
		return bitmap;

	}

	/**
	 * 将bitmap转化为drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		return new BitmapDrawable(bitmap);
	}

	/**
	 * 按指定宽度和高度缩放图片,不保证宽高比例
	 * 
	 * @param bitmap
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		if (bitmap == null) {
			return null;
		}
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int TOP = 3;
	public static final int BOTTOM = 4;

	/** */
	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	/** */
	/**
	 * 去色同时加圆角
	 * 
	 * @param bmpOriginal
	 *            原图
	 * @param pixels
	 *            圆角弧度
	 * @return 修改后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
		return toRoundCorner(toGrayscale(bmpOriginal), pixels);
	}

	/** */
	/**
	 * 把图片变成圆角
	 * 
	 * @param bitmap
	 *            需要修改的图片
	 * @param pixels
	 *            圆角的弧度
	 * @return 圆角图片
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	/** */
	/**
	 * 使圆角功能支持BitampDrawable
	 * 
	 * @param bitmapDrawable
	 * @param pixels
	 * @return
	 */
	public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
			int pixels) {
		Bitmap bitmap = bitmapDrawable.getBitmap();
		bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
		return bitmapDrawable;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	/**
	 * 读取路径中的图片，然后将其转化为缩放后的bitmap
	 * 
	 * @param path
	 */
	public static Bitmap saveBefore(String path, String toPath) {
		// 图片允许最大空间 单位：KB
		Bitmap bitMap = null;
		// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Long fileSize = 0L;
		try {
			File mFile = new File(path);
			if (!mFile.exists()) {
				CYLog.getInstance().d("file  is no exits");
				return null;
			} else {
				CYLog.getInstance().d(
						"image file size is:" + mFile.length() / 1024);
				fileSize = mFile.length();
				bitMap = FileUtil.createBitMap(mFile);
			}
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		} catch (OutOfMemoryError e) {
			CYLog.getInstance().e(e);
		}
		if (null == bitMap) {
			CYLog.getInstance().e("bitmap is null");
			return null;
		}
		// bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = fileSize / 1024;
		if (mid > MAX_IMAGE) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / MAX_IMAGE;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持宽度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
					bitMap.getHeight() / Math.sqrt(i));
		}
		if (null == bitMap) {
			CYLog.getInstance().d("bitmap is null");
			return null;
		}

		return bitMap;
	}

	public static void saveImage(final Bitmap bitMap, final String toPath) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				compressImage(bitMap, toPath);
			}
		}).start();
	}

	// /**
	// * 读取路径中的图片，然后将其转化为缩放后的bitmap
	// *
	// * @param path
	// */
	// public static void saveBefore(String path, String toPath) {
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// // 获取这个图片的宽和高
	// Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
	// options.inJustDecodeBounds = false;
	// // 计算缩放比
	// int be = (int) (options.outHeight / (float) 640);
	// if (be <= 0)
	// be = 1;
	// if (be > 3) {
	// options.inSampleSize = 3; // 图片长宽各缩小五分之一
	// } else {
	// options.inSampleSize = 3; // 图片长宽各缩小五分之一
	// }
	//
	// // 重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
	// bitmap = BitmapFactory.decodeFile(path, options);
	// int w = bitmap.getWidth();
	// int h = bitmap.getHeight();
	// System.out.println(w + " " + h);
	// // savePNG_After(bitmap,path);
	// saveJPGE_After(bitmap, toPath);
	// }

	/**
	 * 保存图片为PNG
	 * 
	 * @param bitmap
	 * @param name
	 */
	public static void savePNG_After(Bitmap bitmap, String name) {
		File file = new File(name);
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
				out.close();
			}
		} catch (FileNotFoundException e) {
			CYLog.getInstance().e(e);
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		}
	}

	/**
	 * 保存图片为JPEG
	 * 
	 * @param bitmap
	 * @param path
	 */
	public static void saveJPGE_After(Bitmap bitmap, String path) {
		File file = new File(path);
		Long size = file.length() / 1024;
		try {
			FileOutputStream out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
				size = file.length() / 1024;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	private static void compressImage(Bitmap bitmap, String path) {
		File file = new File(path);
		Long size = file.length() / 1024;
		try {
			FileOutputStream out = new FileOutputStream(file);
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// int options = 100;
			// //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			// bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			// //循环判断如果压缩后图片是否大于100kb,大于继续压缩
			// while ( baos.toByteArray().length / 1024 > 1024) {
			// baos.reset();//重置baos即清空baos
			// options -= 10;//每次都减少10
			// bitmap.compress(Bitmap.CompressFormat.JPEG, options, out);
			// size = file.length()/1024;
			// }
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
				out.flush();
				out.close();
				size = file.length() / 1024;
			}

		} catch (FileNotFoundException e) {
			CYLog.getInstance().e(e);
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
				bitmap = null;
			}
		}
	}

	/**
	 * 水印
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap createBitmapForWatermark(Bitmap src, Bitmap watermark) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		int ww = watermark.getWidth();
		int wh = watermark.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	/**
	 * 图片合成
	 * 
	 * @return
	 */
	public static Bitmap potoMix(int direction, Bitmap... bitmaps) {
		if (bitmaps.length <= 0) {
			return null;
		}
		if (bitmaps.length == 1) {
			return bitmaps[0];
		}
		Bitmap newBitmap = bitmaps[0];
		// newBitmap = createBitmapForFotoMix(bitmaps[0],bitmaps[1],direction);
		for (int i = 1; i < bitmaps.length; i++) {
			newBitmap = createBitmapForFotoMix(newBitmap, bitmaps[i], direction);
		}
		return newBitmap;
	}

	private static Bitmap createBitmapForFotoMix(Bitmap first, Bitmap second,
			int direction) {
		if (first == null) {
			return null;
		}
		if (second == null) {
			return first;
		}
		int fw = first.getWidth();
		int fh = first.getHeight();
		int sw = second.getWidth();
		int sh = second.getHeight();
		Bitmap newBitmap = null;
		if (direction == LEFT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, sw, 0, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == RIGHT) {
			newBitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, fw, 0, null);
		} else if (direction == TOP) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, sh, null);
			canvas.drawBitmap(second, 0, 0, null);
		} else if (direction == BOTTOM) {
			newBitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh,
					Config.ARGB_8888);
			Canvas canvas = new Canvas(newBitmap);
			canvas.drawBitmap(first, 0, 0, null);
			canvas.drawBitmap(second, 0, fh, null);
		}
		return newBitmap;
	}

	/**
	 * 将Bitmap转换成指定大小
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap createBitmapBySize(Bitmap bitmap, int width, int height) {
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}

	/**
	 * Drawable 转 Bitmap
	 * 
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmapByBD(Drawable drawable) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
		return bitmapDrawable.getBitmap();
	}

	/**
	 * Bitmap 转 Drawable
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Drawable bitmapToDrawableByBD(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	/**
	 * byte[] 转 bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap bytesToBimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * bitmap 转 byte[]
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 将bitmap位图保存到path路径下，图片格式为Bitmap.CompressFormat.PNG，质量为100
	 * 
	 * @param bitmap
	 * @param path
	 */
	public static boolean saveBitmap(Bitmap bitmap, String path) {
		try {
			File file = new File(path);
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			boolean b = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			return b;
		} catch (FileNotFoundException e) {
			CYLog.getInstance().e(e);
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		}
		return false;
	}

	/**
	 * 将bitmap位图保存到path路径下
	 * 
	 * @param bitmap
	 * @param path
	 *            保存路径-Bitmap.CompressFormat.PNG或Bitmap.CompressFormat.JPEG.PNG
	 * @param format
	 *            格式
	 * @param quality
	 *            Hint to the compressor, 0-100. 0 meaning compress for small
	 *            size, 100 meaning compress for max quality. Some formats, like
	 *            PNG which is lossless, will ignore the quality setting
	 * @return
	 */
	public static boolean saveBitmap(Bitmap bitmap, String path,
			CompressFormat format, int quality) {
		try {
			File file = new File(path);
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			boolean b = bitmap.compress(format, quality, fos);
			fos.flush();
			fos.close();
			return b;
		} catch (FileNotFoundException e) {
			CYLog.getInstance().e(e);
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		}
		return false;
	}

	/**
	 * 获得圆角图片
	 * 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if (bitmap == null) {
			return null;
		}

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	// 缩放图片
	public static Bitmap zoomImg(String img, int newWidth, int newHeight) {
		// 图片源
		Bitmap bm = BitmapFactory.decodeFile(img);
		if (null != bm) {
			return zoomImg(bm, newWidth, newHeight);
		}
		return null;
	}

	public static Bitmap zoomImg(Context context, String img, int newWidth,
			int newHeight) {
		// 图片源
		try {
			Bitmap bm = BitmapFactory.decodeStream(context.getAssets()
					.open(img));
			if (null != bm) {
				return zoomImg(bm, newWidth, newHeight);
			}
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		}
		return null;
	}

	// 缩放图片
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * 获得带倒影的图片
	 */
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}

	/**
	 * 得到相机拍摄图片原图路径
	 */
	public static String getRealPathFromURI(Uri uri, ContentResolver resolver) {

		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = resolver.query(uri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String str = cursor.getString(column_index);
		cursor.close();

		return str;

	}

	/**
	 * 图片文件转换在bitmap
	 */

	public static Bitmap decodeFile(File f) {
		Bitmap b = null;
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;

			FileInputStream fis = new FileInputStream(f);
			BitmapFactory.decodeStream(fis, null, o);
			try {
				fis.close();
			} catch (IOException e) {
				CYLog.getInstance().e(e);
			}

			int scale = 1;
			if (o.outHeight > 200 || o.outWidth > 200) {
				scale = (int) Math.pow(
						2,
						(int) Math.round(Math.log(100 / (double) Math.max(
								o.outHeight, o.outWidth)) / Math.log(0.5)));
			}

			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			fis = new FileInputStream(f);
			b = BitmapFactory.decodeStream(fis, null, o2);
			try {
				fis.close();
			} catch (IOException e) {
				CYLog.getInstance().e(e);
			}
		} catch (FileNotFoundException e) {
			CYLog.getInstance().e(e);
		}
		return b;
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFile(Bitmap bm, String path) {
		// File dirFile = new File(path);
		// if(!dirFile.exists()){
		// dirFile.mkdir();
		// }
		File myCaptureFile = new File(path);
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		} finally {
		}

	}

	public static Bitmap decodeBitmap(String path, int displayWidth,
			int displayHeight) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
		// 获取比例大小
		int wRatio = (int) Math.ceil(op.outWidth / (float) displayWidth);
		int hRatio = (int) Math.ceil(op.outHeight / (float) displayHeight);
		// 如果超出指定大小，则缩小相应的比例
		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				op.inSampleSize = wRatio;
			} else {
				op.inSampleSize = hRatio;
			}
		}
		op.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, op);
		return Bitmap
				.createScaledBitmap(bmp, displayWidth, displayHeight, true);
	}

	/**
	 * 采用复杂计算来决定缩放
	 * 
	 * @param path
	 * @param maxImageSize
	 * @return
	 */
	public static Bitmap decodeBitmap(String path, int maxImageSize) {
		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, op); // 获取尺寸信息
		int scale = 1;
		if (op.outWidth > maxImageSize || op.outHeight > maxImageSize) {
			scale = (int) Math.pow(
					2,
					(int) Math.round(Math.log(maxImageSize
							/ (double) Math.max(op.outWidth, op.outHeight))
							/ Math.log(0.5)));
		}
		op.inJustDecodeBounds = false;
		op.inSampleSize = scale;
		bmp = BitmapFactory.decodeFile(path, op);
		return bmp;
	}

	public static Cursor queryThumbnails(Activity context) {
		String[] columns = new String[] { MediaStore.Images.Thumbnails.DATA,
				MediaStore.Images.Thumbnails._ID,
				MediaStore.Images.Thumbnails.IMAGE_ID };
		return context.managedQuery(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, columns,
				null, null, MediaStore.Images.Thumbnails.DEFAULT_SORT_ORDER);
	}

	public static Cursor queryThumbnails(Activity context, String selection,
			String[] selectionArgs) {
		String[] columns = new String[] { MediaStore.Images.Thumbnails.DATA,
				MediaStore.Images.Thumbnails._ID,
				MediaStore.Images.Thumbnails.IMAGE_ID };
		return context.managedQuery(
				MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, columns,
				selection, selectionArgs,
				MediaStore.Images.Thumbnails.DEFAULT_SORT_ORDER);
	}

	public static Bitmap queryThumbnailById(Activity context, int thumbId) {
		String selection = MediaStore.Images.Thumbnails._ID + " = ?";
		String[] selectionArgs = new String[] { thumbId + "" };
		Cursor cursor = queryThumbnails(context, selection, selectionArgs);

		if (cursor.moveToFirst()) {
			String path = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
			cursor.close();
			return decodeBitmap(path, 100, 100);
		} else {
			cursor.close();
			return null;
		}
	}

	public static Bitmap[] queryThumbnailsByIds(Activity context,
			Integer[] thumbIds) {
		Bitmap[] bitmaps = new Bitmap[thumbIds.length];
		for (int i = 0; i < bitmaps.length; i++) {
			bitmaps[i] = queryThumbnailById(context, thumbIds[i]);
		}

		return bitmaps;
	}

	/**
	 * 获取全部
	 * 
	 * @param context
	 * @return
	 */
	public static List<Bitmap> queryThumbnailList(Activity context) {
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		Cursor cursor = queryThumbnails(context);
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			String path = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
			Bitmap b = decodeBitmap(path, 100, 100);
			bitmaps.add(b);
		}
		cursor.close();
		return bitmaps;
	}

	public static List<Bitmap> queryThumbnailListByIds(Activity context,
			int[] thumbIds) {
		List<Bitmap> bitmaps = new ArrayList<Bitmap>();
		for (int i = 0; i < thumbIds.length; i++) {
			Bitmap b = queryThumbnailById(context, thumbIds[i]);
			bitmaps.add(b);
		}

		return bitmaps;
	}

	public static Cursor queryImages(Activity context) {
		String[] columns = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DISPLAY_NAME };
		return context.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
	}

	public static Cursor queryImages(Activity context, String selection,
			String[] selectionArgs) {
		String[] columns = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
				MediaStore.Images.Media.DISPLAY_NAME };
		return context.managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
				selection, selectionArgs,
				MediaStore.Images.Media.DEFAULT_SORT_ORDER);
	}

	public static Bitmap queryImageById(Activity context, int imageId) {
		String selection = MediaStore.Images.Media._ID + "=?";
		String[] selectionArgs = new String[] { imageId + "" };
		Cursor cursor = queryImages(context, selection, selectionArgs);
		if (cursor.moveToFirst()) {
			String path = cursor.getString(cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			cursor.close();
			// return BitmapUtils.decodeBitmap(path, 260, 260);
			return decodeBitmap(path, 220); // 看看和上面这种方式的差别,看了，差不多
		} else {
			cursor.close();
			return null;
		}
	}

	/**
	 * 根据缩略图的Id获取对应的大图
	 * 
	 * @param context
	 * @param thumbId
	 * @return
	 */
	public static Bitmap queryImageByThumbnailId(Activity context,
			Integer thumbId) {

		String selection = MediaStore.Images.Thumbnails._ID + " = ?";
		String[] selectionArgs = new String[] { thumbId + "" };
		Cursor cursor = queryThumbnails(context, selection, selectionArgs);

		if (cursor.moveToFirst()) {
			int imageId = cursor
					.getInt(cursor
							.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID));
			cursor.close();
			return queryImageById(context, imageId);
		} else {
			cursor.close();
			return null;
		}
	}

	public static void updateImageFile(String fromPath, String toPath, long size) {
		try {
			// 获取源图片的大小
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fromPath, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
			if (srcWidth > srcHeight) {
				ratio = srcWidth / 480;
				destWidth = 480;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = srcHeight / 800;
				destHeight = 800;
				destWidth = (int) (srcWidth / ratio);
			}
			if (srcHeight > srcWidth * 3) {// 处理竖长图
				destWidth = opts.outWidth;
				destHeight = opts.outHeight;
				ratio = 0;
			}
			if (srcWidth > srcHeight * 3) {
				destWidth = opts.outWidth;
				destHeight = opts.outHeight;
				ratio = 0;
			}

			// 长图片 分辨率低 (暂定<200K) 压缩会模糊
			if (size <= 200) {
				ratio = 0;
			}
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inSampleSize = (int) ratio + 1;
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 添加尺寸信息，
			// 获取缩放后图片
			Bitmap destBm = BitmapFactory.decodeFile(fromPath, newOpts);

			if (destBm == null) {

			} else {
				File destFile = new File(toPath);
				// 创建文件输出流
				OutputStream os = new FileOutputStream(destFile);
				// 存储
				if (size <= 200) {
					destBm.compress(CompressFormat.JPEG, 95, os);
				} else {
					destBm.compress(CompressFormat.JPEG, 50, os);
				}
				// 关闭流
				os.close();
			}
		} catch (Exception e) {
			e.fillInStackTrace();
		}
	}
	
	public static Bitmap  getCompressedBitmapImage(String path, long size){	
		try {
			// 获取源图片的大小
			BitmapFactory.Options opts = new BitmapFactory.Options();
			// 当opts不为null时，但decodeFile返回空，不为图片分配内存，只获取图片的大小，并保存在opts的outWidth和outHeight
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;
			int srcHeight = opts.outHeight;
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
			if (srcWidth > srcHeight) {
				ratio = srcWidth / 480;
				destWidth = 480;
				destHeight = (int) (srcHeight / ratio);
				ratio=ratio-1;
			} else {
				ratio = srcHeight / 800;
				destHeight = 800;
				destWidth = (int) (srcWidth / ratio);
				ratio=ratio-1;
			}
			if (srcHeight > srcWidth * 3) {// 处理竖长图
				destWidth = opts.outWidth;
				destHeight = opts.outHeight;
				ratio = 1;
			}
			if (srcWidth > srcHeight * 3 ) {
				destWidth = opts.outWidth;
				destHeight = opts.outHeight;
				ratio = 1;
			}

			// 长图片 分辨率低 (暂定<200K) 压缩会模糊
			if (size <= 200) {
				ratio = 0;
			}
			// 对图片进行压缩，是在读取的过程中进行压缩，而不是把图片读进了内存再进行压缩
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inSampleSize = (int) ratio + 1;
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 添加尺寸信息，
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
            
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return null;
	}
	
	public  static void   compressBitmapToFile(Bitmap bitmap, String path, long size ){
		if (bitmap == null) {

		} else {
			try {
				File destFile = new File(path);
				// 创建文件输出流
				OutputStream os = new FileOutputStream(destFile);
				// 存储
				if (size <= 200) {
					bitmap.compress(CompressFormat.JPEG, 100, os);
				} else {
					if(size>1024){
						bitmap.compress(CompressFormat.JPEG, 45, os);
					}else{
						bitmap.compress(CompressFormat.JPEG, 75, os);
					}
				}
				// 关闭流
				os.close();
			} catch (Exception e) {
				e.fillInStackTrace();
			}
			finally{
				if(bitmap != null && !bitmap.isRecycled()){
					bitmap.recycle();
				}
			}
			
		}
	}

}
