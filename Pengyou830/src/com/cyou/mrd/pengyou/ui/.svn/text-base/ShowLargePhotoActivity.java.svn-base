package com.cyou.mrd.pengyou.ui;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.FileUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ShowLargePhotoActivity extends BaseActivity {
	private ImageView imageView;
	private String imgUrl;
	private DisplayImageOptions mIconOptions;
	private CYLog cyLog = CYLog.getInstance();
	int photoType;
	private String imageMiddleUrl;
	private ProgressBar progressBar;
	private PhotoViewAttacher mAttacher;
	public static final int scaleType = 3;
	private static boolean  mVisibility  = false;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.photo_show_layout);
		photoType = getIntent().getIntExtra(Params.SHOW_PHOTO.PHOTO_TYPE, 0);
		mVisibility = true;
		initView();
	}

	Bitmap bitmap;

	@Override
	protected void onStop(){
		mVisibility = false;
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		try {
			if (null != bitmap) {
				bitmap.recycle();
				bitmap = null;
			}
		} catch (Exception e) {
			cyLog.e(e);
		}
		super.onDestroy();
	}

	private void initView() {
		imageView = (ImageView) findViewById(R.id.img_photo);
		mAttacher = new PhotoViewAttacher(imageView);
		mAttacher.setOnPhotoTapListener(new PhotoTapListener());
		progressBar=(ProgressBar) findViewById(R.id.lanch_pb);
		if (photoType == Params.SHOW_PHOTO.PHOTO_USER) {
			imageView.setImageResource(R.drawable.image_default);
			this.mIconOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true)
					.showImageForEmptyUri(R.drawable.image_default)
					.showImageOnFail(R.drawable.image_default)
					.showStubImage(R.drawable.image_default).build();
		} else {
			this.mIconOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true).build();
		}
		imgUrl = getIntent().getStringExtra(Params.PHOTO_URL);
		imageMiddleUrl = getIntent().getStringExtra(Params.PHOTO_MIDDLE_URL);
		cyLog.d("image url is:"+imgUrl);
		if (!TextUtils.isEmpty(imgUrl)) {
			File file = new File(imgUrl);
			if (file.exists()) {
//				Bitmap bitmap = FileUtil.createBitMap(file);
//				//显示长图时改变scaletype
//				if (bitmap != null) {
//					if(bitmap.getWidth() < 100 && bitmap.getHeight() < 100){
//						LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
//						params.width = LayoutParams.WRAP_CONTENT;
////					params.width = (int)(Config.screenWidthWithDip*Config.SreenDensity/3);
////					params.height = params.width * bitmap.getHeight()/bitmap.getWidth();
//						imageView.setLayoutParams(params);
//					}else if (bitmap.getHeight() >= bitmap.getWidth() && (bitmap.getHeight()/bitmap.getWidth()) >= scaleType) {
//						mAttacher.setScaleType(ImageView.ScaleType.CENTER);
//						LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
//						params.width = LayoutParams.MATCH_PARENT;
//						params.height = LayoutParams.MATCH_PARENT;
//					}else if (bitmap.getHeight() < bitmap.getWidth() && (bitmap.getWidth()/bitmap.getHeight()) >= scaleType){
//						mAttacher.setScaleType(ImageView.ScaleType.CENTER);
//						LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
//						params.width = LayoutParams.MATCH_PARENT;
//						params.height = LayoutParams.MATCH_PARENT;
//					}else{
//						LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
//						params.width = LayoutParams.MATCH_PARENT;
//						params.height = LayoutParams.MATCH_PARENT;
//					}
//				}
//				imageView.setImageBitmap(bitmap);
//				progressBar.setVisibility(View.GONE);

				CYImageLoader.displayImgFromSDCard(imgUrl, imageView, mIconOptions, new ImageLoadingListener(){

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						try {
							progressBar.setVisibility(View.GONE);
							File mFile = CYImageLoader.getImageFileByUrl("file://" + imgUrl);
							if (null != mFile && mFile.exists()) {
								if (loadedImage != null) {
									if(loadedImage.getWidth() < 100 && loadedImage.getHeight() < 100){
										LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
										params.width = LayoutParams.WRAP_CONTENT;
										imageView.setLayoutParams(params);										
									}else if (loadedImage.getHeight() >= loadedImage.getWidth() && (loadedImage.getHeight()/loadedImage.getWidth()) >= scaleType) {
										mAttacher.setScaleType(ImageView.ScaleType.CENTER);
										LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
										params.width = LayoutParams.MATCH_PARENT;
										params.height = LayoutParams.MATCH_PARENT;
									}else if (loadedImage.getHeight() < loadedImage.getWidth() && (loadedImage.getWidth()/loadedImage.getHeight()) >= scaleType){
										mAttacher.setScaleType(ImageView.ScaleType.CENTER);
										LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
										params.width = LayoutParams.MATCH_PARENT;
										params.height = LayoutParams.MATCH_PARENT;
									}else{
										LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
										params.width = LayoutParams.MATCH_PARENT;
										params.height = LayoutParams.MATCH_PARENT;
									}
									imageView.setImageBitmap(loadedImage);
								}
								else {
									if(mVisibility)
									   showToastMessage(getString(R.string.pic_load_error), 0);
								}

							} else {
								if(mVisibility)
								   showToastMessage(getString(R.string.pic_load_error), 0);
							}
						} catch (Exception e) {
							cyLog.e(e);
							if(mVisibility)
							   showToastMessage(getString(R.string.pic_load_error), 0);
						} catch (OutOfMemoryError e) {
							cyLog.e(e);
							if(mVisibility)
							   showToastMessage(getString(R.string.pic_load_error), 0);
						}
						
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
						if(mVisibility){
							showToastMessage(getString(R.string.pic_load_error), 0);
							progressBar.setVisibility(View.GONE);
						}
					}

					@Override
					public void onLoadingStarted(String arg0, View view) {
						// TODO Auto-generated method stub
						
					}
					
				});

			}else {
				CYImageLoader.displayImg(imgUrl, imageView, mIconOptions,
						new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						if (!TextUtils.isEmpty(imageMiddleUrl)&& mVisibility) {
							File mFile = CYImageLoader.getImageFileByUrl(PYVersion.IP.IMG_HOST + imageMiddleUrl);
							if (null != mFile && mFile.exists()) {
								bitmap = FileUtil.createBitMap(mFile);
								if (bitmap != null) {
									imageView.setImageBitmap(bitmap);
								}
							}
//							 CYImageLoader.displayImg(imageMiddleUrl,imageView, mIconOptions);
						}
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						if(mVisibility){
							showToastMessage(getString(R.string.pic_load_error), 0);
							progressBar.setVisibility(View.GONE);
						}
					}
					
					@Override
					public void onLoadingComplete(String imageUri,View view, Bitmap loadedImage) {
						try {
							File mFile = CYImageLoader.getImageFileByUrl(PYVersion.IP.IMG_HOST + imgUrl);
							if (null != mFile && mFile.exists() && mVisibility) {
//								bitmap = FileUtil.createBitMap(mFile);
//								BitmapFactory.Options o = new BitmapFactory.Options();
//								o.inJustDecodeBounds = true;
//								bitmap = BitmapFactory.decodeFile(mFile.getPath(), o);
//								float width = o.outWidth;
//								float height = o.outHeight;
//								float temp = width / height;
//								int imgHeight = 0;
//								int imgWidth = 0;
//								if(width < height){
//								    imgHeight = (int) (Util.getScreenHeightSize(ShowLargePhotoActivity.this) / temp);
//								    bitmap = FileUtil.getBitmapFromFile(mFile,Util.getScreenHeightSize(ShowLargePhotoActivity.this),imgHeight);
//								}
//								else {
//									 imgWidth = (int) (Util
//											.getScreenHeightSize(ShowLargePhotoActivity.this) *temp);
//									 bitmap = FileUtil.getBitmapFromFile(
//												mFile,imgWidth,
//												Util.getScreenHeightSize(ShowLargePhotoActivity.this)
//												);
//								}
//							    else{
//							    	if(width < height){
//							    		imageView.setLayoutParams(new LayoutParams(Util.getScreenHeightSize(ShowLargePhotoActivity.this),imgHeight));
//							    	}
//							    	else {
//							    		imageView.setLayoutParams(new LayoutParams(imgWidth,
//												Util.getScreenHeightSize(ShowLargePhotoActivity.this)
//												));
//							    	}
//							    }
							  //显示长图时改变scaletype
								if (loadedImage != null && mVisibility) {
									progressBar.setVisibility(View.GONE);
									if(loadedImage.getWidth() < 100 && loadedImage.getHeight() < 100){
										LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
										params.width = LayoutParams.MATCH_PARENT;
										params.height = LayoutParams.MATCH_PARENT;
//									params.width = (int)(Config.screenWidthWithDip*Config.SreenDensity/3);
//									params.height = params.width * bitmap.getHeight()/bitmap.getWidth();
//										imageView.setLayoutParams(params);
										
									}else if (loadedImage.getHeight() >= loadedImage.getWidth() && (loadedImage.getHeight()/loadedImage.getWidth()) >= scaleType) {
										mAttacher.setScaleType(ImageView.ScaleType.CENTER);
										LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
										params.width = LayoutParams.MATCH_PARENT;
										params.height = LayoutParams.MATCH_PARENT;
									}else if (loadedImage.getHeight() < loadedImage.getWidth() && (loadedImage.getWidth()/loadedImage.getHeight()) >= scaleType){
										mAttacher.setScaleType(ImageView.ScaleType.CENTER);
										LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
										params.width = LayoutParams.MATCH_PARENT;
										params.height = LayoutParams.MATCH_PARENT;
									}else{
										LayoutParams  params =  (LayoutParams)imageView.getLayoutParams();
										params.width = LayoutParams.MATCH_PARENT;
										params.height = LayoutParams.MATCH_PARENT;
									}
									imageView.setImageBitmap(loadedImage);
								}
								else {
									if(mVisibility){
										showToastMessage(getString(R.string.pic_load_error), 0);
										progressBar.setVisibility(View.GONE);
									}
								}

							} else {
								if(mVisibility){
									showToastMessage(getString(R.string.pic_load_error), 0);
									progressBar.setVisibility(View.GONE);
								}
							}
						} catch (Exception e) {
							cyLog.e(e);
							if(mVisibility){
								showToastMessage(getString(R.string.pic_load_error), 0);
								progressBar.setVisibility(View.GONE);
							}
						} catch (OutOfMemoryError e) {
							cyLog.e(e);
							if(mVisibility){
								showToastMessage(getString(R.string.pic_load_error), 0);
							    progressBar.setVisibility(View.GONE);
							}
						}
					}
					
					@Override
					public void onLoadingCancelled(String imageUri,View view) {
						if(mVisibility){
							progressBar.setVisibility(View.GONE);
						}
					}
				});
				
			}
		}
//		imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});
	}
	
	private class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View view, float x, float y) {
			finish();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& isOutOfBounds(this, event)) {
			finish();
			return true;
		}
		return super.onTouchEvent(event);
	}

	private boolean isOutOfBounds(Activity context, MotionEvent event) {
		final int x = (int) event.getX();
		final int y = (int) event.getY();
		final int slop = ViewConfiguration.get(context)
				.getScaledWindowTouchSlop();
		final View decorView = context.getWindow().getDecorView();
		return (x < -slop) || (y < -slop)
				|| (x > (decorView.getWidth() + slop))
				|| (y > (decorView.getHeight() + slop));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mVisibility = false;
			cancelToastMessage();
		}
		return super.onKeyDown(keyCode, event);
	}

}
