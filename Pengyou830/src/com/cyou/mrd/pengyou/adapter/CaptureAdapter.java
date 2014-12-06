package com.cyou.mrd.pengyou.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.ShowPhotoActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.FileUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.CaptureItemViewCache;
import com.cyou.mrd.pengyou.widget.ViewToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CaptureAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mData=new ArrayList<String>();
	private DisplayImageOptions mOption;
	private LayoutInflater mInflater;
	Bitmap bitmap;
	 private CYLog log = CYLog.getInstance();
	boolean flag=true;
	
	public CaptureAdapter(Context context,List<String> data){
		this.mData = data;
		this.mContext = context;
		this.mOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.capture_default)
		.showImageOnFail(R.drawable.capture_default)
		.showStubImage(R.drawable.capture_default)
		.build();
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		CaptureItemViewCache viewCache = null;
//		if(convertView == null){
//			convertView = mInflater.inflate(R.layout.capture_item, null);
//			viewCache = new CaptureItemViewCache(convertView);
//			convertView.setTag(viewCache);
//		}else{
//			viewCache = (CaptureItemViewCache)convertView.getTag();
//		}
//		CYImageLoader.displayIconImage(mData.get(position) 
//				, viewCache.getmIV()
//				, mOption);
//		return convertView;
//	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		CaptureItemViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.capture_item, null);
			viewCache = new CaptureItemViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (CaptureItemViewCache)convertView.getTag();
		}
		final CaptureItemViewCache tmpviewcache=viewCache;
		if(mData!=null && !mData.isEmpty()){

		CYImageLoader.displayIconImage(mData.get(position) , tmpviewcache.getmIV(), mOption,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
//				if (!TextUtils.isEmpty(mData.get(position))) {
//				
//				}
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
				if(flag){
					log.d("--------------smallimge(or bigimg) onLoadingFailed");
					log.d("----"+failReason.toString());
					ViewToast.showToast(mContext, R.string.pic_load_error, 0);
				flag=false;
				}
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub			
				try {
						float width = loadedImage.getWidth();
						//Log.e("outWidth---------------", String.valueOf(width));
						float height =  loadedImage.getHeight();
						//Log.e("outheight--------------", String.valueOf(height));
						if(width>height){				
					
							Matrix matrix = new Matrix(); 
							matrix.setRotate((float)90.0, (float) width/ 2, (float)height/ 2);					
							bitmap = Bitmap.createBitmap(loadedImage,0,0,(int)width,(int)height,matrix,true);
							tmpviewcache.getmIV().setImageBitmap(bitmap);  							
						}
				} catch (Exception e) {
					CYLog.getInstance().e(e);
					// TODO: handle exception
					log.d("--------------smallimge(or bigimg)创建图片失败");
					// System.out.print("创建图片失败！");
				}catch (OutOfMemoryError e) {
					log.d("--------------smallimge(or bigimg)内存溢出！");
					// System.out.print("内存溢出！");
				}
			}
			
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
					
				}
			});
		
	}
		return convertView;
	}

}
