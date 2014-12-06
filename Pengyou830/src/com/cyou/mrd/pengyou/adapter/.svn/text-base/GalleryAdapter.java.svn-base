package com.cyou.mrd.pengyou.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.FileUtil;
import com.cyou.mrd.pengyou.widget.ViewToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class GalleryAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mBigimgurl;
	private List<String> mSmallimgurl;
	private DisplayImageOptions mOptions;
	private ProgressBar mpb;
	private Bitmap bitmap;
	 private CYLog log = CYLog.getInstance();

	 boolean flag=true;
	
	public GalleryAdapter(Context context,List<String>  bigimgurl,List<String> smallimgurl,ProgressBar progressbar){
		this.mContext = context;
		this.mBigimgurl = bigimgurl;
		this.mSmallimgurl=smallimgurl;
		this.mpb=progressbar;
		this.mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.build();
//	    .showImageForEmptyUri(R.drawable.capture_default)
//		.showImageOnFail(R.drawable.capture_default)
//		.showStubImage(R.drawable.capture_default)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ture_default)
	}
	
	@Override
	public int getCount() {
		//return mSmallimgurl.size();
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int arg0) {
		return mBigimgurl.get(arg0%mBigimgurl.size());
	}

	@Override
	public long getItemId(int arg0) {
		return arg0%mBigimgurl.size();
	}
	
	

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		mpb.setVisibility(View.VISIBLE);
		if(arg1 == null){
			arg1 = new ImageView(mContext);
			LayoutParams params = new android.widget.Gallery.LayoutParams(android.widget.Gallery.LayoutParams.FILL_PARENT,android.widget.Gallery.LayoutParams.FILL_PARENT);
			arg1.setLayoutParams(params);
			((ImageView)arg1).setScaleType(ScaleType.FIT_CENTER);
			((ImageView)arg1).setAdjustViewBounds(true);
		}
		final ImageView imageview=(ImageView)arg1;
		if(mBigimgurl!=null && !mBigimgurl.isEmpty()){
		//	log.d("------mBigimgurl不空");
		CYImageLoader.displayIconImage(mBigimgurl.get(arg0%mBigimgurl.size()), (ImageView)arg1, mOptions,new ImageLoadingListener() {
		//	CYImageLoader.displayImg(mBigimgurl.get(arg0), (ImageView)arg1, mOptions,new ImageLoadingListener() {
			
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(mSmallimgurl.get(arg0%mSmallimgurl.size()))) {
					File mFile = CYImageLoader.getImageFileByUrl(PYVersion.IP.ICON_HOST + mSmallimgurl.get(arg0%mSmallimgurl.size()));
					if (null != mFile && mFile.exists()) {
						bitmap = FileUtil.createBitMap(mFile);
						if (bitmap != null) {
							imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE); 
							imageview.setImageBitmap(bitmap);
						//	log.d( "----mSmallimgurl  先显示");
						}
					}
				}
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				if(flag){
					log.d("-----bigimg  onLoadingFailed");
					log.d("------"+failReason.toString());
					ViewToast.showToast(mContext, R.string.pic_load_error, 0);
				flag=false;
				}
				mpb.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				try {
					imageview.setScaleType(ScaleType.FIT_CENTER);
					float width = loadedImage.getWidth();
					//Log.e("outWidth---------------", String.valueOf(width));
					float height =  loadedImage.getHeight();
					if(width>height){								
						Matrix matrix = new Matrix(); 
						matrix.setRotate((float)90.0, (float) width/ 2, (float)height/ 2);					
						bitmap = Bitmap.createBitmap(loadedImage,0,0,(int)width,(int)height,matrix,true);						
						imageview.setImageBitmap(bitmap);  							
					}
					mpb.setVisibility(View.GONE);
			} catch (Exception e) {
				CYLog.getInstance().e(e);
				// TODO: handle exception
				log.d("-----创建图片失败！");
				 mpb.setVisibility(View.GONE);
			}catch (OutOfMemoryError e) {
				log.d("-------内存溢出！");
				 mpb.setVisibility(View.GONE);
//			}
			}
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				log.d("-----bigimg  onLoadingCancelled");
				mpb.setVisibility(View.GONE);
			}
		});
		}
//		else if(mSmallimgurl!=null && !mSmallimgurl.isEmpty()){// if big image is null,then show small image
//			CYImageLoader.displayIconImage(mSmallimgurl.get(arg0), (ImageView)arg1, mOptions,new ImageLoadingListener() {
//
//				@Override
//				public void onLoadingCancelled(String arg0, View arg1) {
//					// TODO Auto-generated method stub
//					mpb.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onLoadingComplete(String arg0, View arg1,Bitmap loadedImage) {
//					// TODO Auto-generated method stub
//					try {
//						float width = loadedImage.getWidth();
//						float height =  loadedImage.getHeight();
//						if(width>height){									
//							Matrix matrix = new Matrix(); 
//							matrix.setRotate((float)90.0, (float) width/ 2, (float)height/ 2);					
//							bitmap = Bitmap.createBitmap(loadedImage,0,0,(int)width,(int)height,matrix,true);
//							imageview.setImageBitmap(bitmap);  							
//						}
//						mpb.setVisibility(View.GONE);
//				} catch (Exception e) {
//					CYLog.getInstance().e(e);
//					// TODO: handle exception
//					 System.out.print("创建图片失败！");
//					 mpb.setVisibility(View.GONE);
//				}catch (OutOfMemoryError e) {
//					 System.out.print("内存溢出！");
//					 mpb.setVisibility(View.GONE);
////				}
//				}
//				}
//
//				@Override
//				public void onLoadingFailed(String arg0, View arg1,
//						FailReason arg2) {
//					// TODO Auto-generated method stub
//					if(flag){
//						ViewToast.showToast(mContext, R.string.pic_load_error, 0);
//					    flag=false;
//					}
//					mpb.setVisibility(View.GONE);
//				}
//
//				@Override
//				public void onLoadingStarted(String imageUri, View view) {
//					// TODO Auto-generated method stub
//					// TODO Auto-generated method stub
//					if (!TextUtils.isEmpty(mSmallimgurl.get(arg0))) {
//						File mFile = CYImageLoader.getImageFileByUrl(PYVersion.IP.IMG_HOST + mSmallimgurl.get(arg0));
//						if (null != mFile && mFile.exists()) {
//							bitmap = FileUtil.createBitMap(mFile);
//							if (bitmap != null) {
//								imageview.setImageBitmap(bitmap);
//								Log.e("------", "mSmallimgurl");
//							}
//						}
//					}
//				}
//				
//			});
//		}
		else{
		
			imageview.setBackgroundResource(R.drawable.capture_default);
			if(flag){
				log.d("-----bigimage is null");
				ViewToast.showToast(mContext, R.string.pic_load_error, 0);
			    flag=false;
			}
			mpb.setVisibility(View.GONE);
		}
		return arg1;
		
	}

}
