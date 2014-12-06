package com.cyou.mrd.pengyou.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params.FRIEND_INFO;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;

/**
 * 二维码
 * 
 * @author wangkang
 * 
 */
public class AddFriendByDCodeFragment extends BaseFragment {

	public AddFriendByDCodeFragment(Activity activity) {
		super(activity);
		this.mActivity = activity;
	}

	private CYLog log = CYLog.getInstance();

	/** Called when the activity is first created. */
	private TextView resultTextView;
	private ImageView qrImgImageView;
	private View mView;
	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_DCODE_ID,
				CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_DCODE_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (null != bundle) {
				String scanResult = bundle.getString("result");
				resultTextView.setText(scanResult);
				Intent mIntent = new Intent();
				mIntent.putExtra(FRIEND_INFO.UID, scanResult);
				mIntent.setClass(mActivity, FriendInfoActivity.class);
				startActivity(mIntent);
			}
		}
	}

	// public void loadUserInfo(Bundle bundle) {
	// String scanResult = bundle.getString("result");
	// resultTextView.setText(scanResult);
	// }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.add_friend_dcode_layout, null);
		resultTextView = (TextView) mView.findViewById(R.id.tv_scan_result);
		qrImgImageView = (ImageView) mView.findViewById(R.id.iv_qr_image);
		Button scanBarCodeButton = (Button) mView
				.findViewById(R.id.btn_scan_barcode);
		scanBarCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				// 打开扫描界面扫描条形码或二维码
//				Intent openCameraIntent = new Intent(mActivity,
//						CaptureActivity.class);
//				startActivityForResult(openCameraIntent, 0);
			}
		});
		loadQRCodeImage();
		return mView;
	}

	/**
	 * 生成二维码
	 */
	private void loadQRCodeImage() {
		int userToken = UserInfoUtil.getCurrentUserId();
		Bitmap qrCodeBitmap;
//		try {
//			int srceenWidth = Util.getScreenWidthSize(mActivity);
//			if (srceenWidth > 0) {
////				srceenWidth = srceenWidth * 2 / 3;
////				qrCodeBitmap = EncodingHandler.createQRCode(
////						String.valueOf(userToken), srceenWidth);
////				qrImgImageView.setImageBitmap(qrCodeBitmap);
//			}
//		} catch (WriterException e) {
//			e.printStackTrace();
//			Toast.makeText(mActivity, R.string.load_qr_code_failed, Toast.LENGTH_SHORT).show();
//		}
	}

	@Override
	public void onStart() {
		super.onStart();

	}
}
