package com.cyou.mrd.pengyou.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.log.CYLog;

/**
 * 更改头像
 * @author xumengyang
 *
 */
public class AvatarUtil {

	public static final int RESULT_FROM_CAMERA = 1;
	public static final int RESULT_FROM_ALBUMS = 2;
	public static final int RESULT_FROM_CROP = 3;
	public static final String CROP_URI = "com.android.camera.action.CROP";
	
	private static String tempImageFilePath = 
			SharedPreferenceUtil.getRootPath(CyouApplication.mAppContext) + "/temp.jpg";//临时图片路径
	private static CYLog log = CYLog.getInstance();
	/**
	 * 验证SD卡
	 * xumengyang
	 * @return
	 */
	private static boolean verifySDCard(){
		if(!(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
				&& Environment.getExternalStorageDirectory().canWrite() 
				&& !Environment.getDownloadCacheDirectory().canWrite())){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 开启相机
	 * xumengyang
	 */
	public static void startCamera(Context context){
		try {
			if (!verifySDCard()) {
				Toast.makeText(context, R.string.sdcard_write_error, Toast.LENGTH_SHORT).show();
				return;
			}
			Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File photo = new File(tempImageFilePath);
			Uri uri = Uri.fromFile(photo);
			iCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			((Activity) context).startActivityForResult(iCamera,RESULT_FROM_CAMERA);
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	/**
	 * 开启相册
	 * @param context
	 */
	public static void startAlbums(Context context){
		try {
			if (!verifySDCard()) {
				Toast.makeText(context, R.string.sdcard_write_error, Toast.LENGTH_SHORT).show();
				return;
			}
			Intent iAlbums = new Intent(Intent.ACTION_PICK);
			iAlbums.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
			((Activity)context).startActivityForResult(iAlbums,RESULT_FROM_ALBUMS);
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	/**
	 * 相机回调
	 * xumengyang
	 * @param context
	 * @param resultCode
	 */
	public static void resultFromCamera(Context context,int resultCode){
		try {
			if(resultCode != ((Activity)context).RESULT_OK){
				log.e("resultFromCamera : resultCode != RESULT_OK");
				return;
			}
			File photo = new File(tempImageFilePath);
			Intent intent1 = new Intent(CROP_URI);
			intent1.setDataAndType(Uri.fromFile(photo), "image/*");
			intent1.putExtra("crop", "true");
			intent1.putExtra("aspectX", 1);
			intent1.putExtra("aspectY", 1);
			intent1.putExtra("outputX", Config.USERICON_WIDTH);
			intent1.putExtra("outputY", Config.USERICON_HEIGHT);
			intent1.putExtra("noFaceDetection", true);
			intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			intent1.putExtra("return-data", true);
			((Activity)context).startActivityForResult(intent1, RESULT_FROM_CROP);
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	/**
	 * 相册回调
	 * @param context
	 * @param resultCode
	 */
	public static void resultFromAlbums(Context context,int resultCode,Intent data){
		try {
			if (resultCode != ((Activity)context).RESULT_OK) {
				return;
			}
			try {
				if (data != null) {
					Intent intent2 = new Intent(CROP_URI);
					intent2.setData(data.getData());
					intent2.putExtra("crop", "true");
					intent2.putExtra("aspectX", 1);
					intent2.putExtra("aspectY", 1);
					intent2.putExtra("outputX", Config.USERICON_WIDTH);
					intent2.putExtra("outputY", Config.USERICON_HEIGHT);
					intent2.putExtra("noFaceDetection", true);
					intent2.putExtra("return-data", true);
					((Activity)context).startActivityForResult(intent2, RESULT_FROM_CROP);
				}
			} catch (Exception e) {
				log.e(e);
			}
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	/**
	 * 裁剪回调
	 * @param context
	 * @param resultCode
	 * @param data
	 */
	public static Bitmap resultFromCrop(Context context,int resultCode,Intent data){
		try {
			if (data != null) {
				Bitmap bitmap = (Bitmap) data.getExtras().getParcelable("data");
				if (bitmap != null) {
					File avatarFile = null;
					avatarFile = new File(UserInfoUtil.getUserIconPath());
					try {
						if (avatarFile.exists()) {
							avatarFile.delete();
						}
						avatarFile.createNewFile();
					} catch (Exception e) {
						log.e(e);
					}
					FileOutputStream fileoutputstream = null;
					try {
						fileoutputstream = new FileOutputStream(avatarFile);
					} catch (Exception e) {
						log.e(e);
					}
					bitmap.compress(Bitmap.CompressFormat.PNG, 100,
							fileoutputstream);
					try {
						fileoutputstream.flush();
						fileoutputstream.close();
					} catch (Exception exception1) {
						log.e(exception1);
					}
					return bitmap;
				} else {
					log.e("bitmap is null");
				}
			} else {
				log.e("data is null");
			}
		} catch (Exception e) {
			log.e(e);
		}
		return null;
	}
	
	/**
	 * 删除临时文件
	 * xumengyang
	 */
	public static void deleteTempFile(){
		try {
			File mFile = new File(tempImageFilePath);
			if (null != mFile && mFile.exists()) {
				mFile.delete();
			}
		} catch (Exception e) {
			log.e(e);
		}
	}
}
