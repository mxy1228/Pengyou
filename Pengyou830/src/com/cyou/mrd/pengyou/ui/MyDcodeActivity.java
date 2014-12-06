package com.cyou.mrd.pengyou.ui;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 我的二维码
 * 
 * @author wangkang
 * 
 */
public class MyDcodeActivity extends BaseActivity {

	private CYLog log = CYLog.getInstance();

	private ImageView imgIcon;
	private TextView txtNickname;
	private ImageView imgDCode;
	private DisplayImageOptions mOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mydcode_layout);
		this.mOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15))
				.resetViewBeforeLoading().build();
		initView();
		initData();
	}

	private void initView() {
		View headerBar = findViewById(R.id.mydcode_header_bar);
		ImageButton mBackBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView mHeaderTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		mHeaderTV.setText(R.string.personal_dcode);
		imgIcon = (ImageView) findViewById(R.id.img_persion_icon);
		txtNickname = (TextView) findViewById(R.id.txt_nickname);
		imgDCode = (ImageView) findViewById(R.id.img_dcode);
	}

	private void initData() {
//		UserInfo userInfo = SharedPreferenceUtil.getCurrentUserInfo();
//		if (null == userInfo) {
//			showToastMessage(getString(R.string.get_userinfo_error), 1);
//			log.e("get userinfo is null");
//			finish();
//			return;
//		}
		File file = new File(UserInfoUtil.getUserIconPath());
		if (UserInfoUtil.getCurrentUserGender() == Contants.GENDER_TYPE.BOY) {
			if (file.exists()) {
				imgIcon.setImageBitmap(BitmapFactory.decodeFile(file
						.getAbsolutePath()));
			} else {
				if (!TextUtils.isEmpty(UserInfoUtil.getCurrentUserPicture())) {
					CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture(),
							imgIcon, mOptions);
				}
			}
		} else {
			if (file.exists()) {
				imgIcon.setImageBitmap(BitmapFactory.decodeFile(file
						.getAbsolutePath()));
			} else {
				CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture(),
						imgIcon, mOptions);
			}
		}
		txtNickname.setText(UserInfoUtil.getCurrentUserNickname());
		loadQRCodeImage();
	}

	private Bitmap userIconBitmap;

	@Override
	public void onDestroy() {
		if (null != userIconBitmap) {
			userIconBitmap.recycle();
			userIconBitmap = null;
		}

		super.onDestroy();
	}
	/**
	 * 生成二维码
	 */
	private void loadQRCodeImage() {
		int userToken = UserInfoUtil.getCurrentUserId();
		Bitmap qrCodeBitmap;
//		try {
//			int srceenWidth = Util.getScreenWidthSize(this);
//			if (srceenWidth > 0) {
//				srceenWidth = srceenWidth * 2 / 3;
////				qrCodeBitmap = EncodingHandler.createQRCode(String.valueOf(userToken),
////						srceenWidth);
////				imgDCode.setImageBitmap(qrCodeBitmap);
//			}
//		} catch (WriterException e) {
//			e.printStackTrace();
//			showToastMessage(getString(R.string.load_dcode_error), 1);
//		}
	}
}
