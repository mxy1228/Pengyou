package com.cyou.mrd.pengyou.ui;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.cyou.mrd.pengyou.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ShowPhotoActivity extends BaseActivity {
	private ImageView imageView;
	private String imgUrl;
	private DisplayImageOptions mIconOptions;
	private CYLog cyLog = CYLog.getInstance();
	int photoType;
	private String imageMiddleUrl;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.photo_show_layout);
		photoType = getIntent().getIntExtra(Params.SHOW_PHOTO.PHOTO_TYPE, 0);
		initView();
	}

	Bitmap bitmap;

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
			CYImageLoader.displayImg(imgUrl, imageView, mIconOptions,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							if (!TextUtils.isEmpty(imageMiddleUrl)) {
								// CYImageLoader.displayImg(imageMiddleUrl,
								// imageView, mIconOptions);
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							showToastMessage(getString(R.string.pic_load_error), 0);
							progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressBar.setVisibility(View.GONE);
							try {
								File mFile = CYImageLoader
										.getImageFileByUrl(PYVersion.IP.IMG_HOST
												+ imgUrl);
								if (null != mFile && mFile.exists()) {
									BitmapFactory.Options o = new BitmapFactory.Options();
									o.inJustDecodeBounds = true;
									bitmap = BitmapFactory.decodeFile(
											mFile.getPath(), o);
									float width = o.outWidth;
									float height = o.outHeight;
									float temp = width / height;
									int imgHeight = (int) (Util
											.getScreenHeightSize(ShowPhotoActivity.this) / temp);
									bitmap = FileUtil.getBitmapFromFile(
											mFile,
											Util.getScreenHeightSize(ShowPhotoActivity.this),
											imgHeight);
									imageView.setLayoutParams(new LayoutParams(
											Util.getScreenHeightSize(ShowPhotoActivity.this),
											imgHeight));
									imageView.setImageBitmap(bitmap);
								} else {
									showToastMessage(getString(R.string.pic_load_error), 0);
									progressBar.setVisibility(View.GONE);
								}
							} catch (Exception e) {
								cyLog.e(e);
								showToastMessage(getString(R.string.pic_load_error), 0);
								progressBar.setVisibility(View.GONE);
							} catch (OutOfMemoryError e) {
								cyLog.e(e);
								progressBar.setVisibility(View.GONE);
								showToastMessage(getString(R.string.pic_load_error), 0);
							}
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							progressBar.setVisibility(View.GONE);
						}
					});
		}
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
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

}
