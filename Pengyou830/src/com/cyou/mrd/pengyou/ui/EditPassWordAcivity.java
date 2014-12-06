package com.cyou.mrd.pengyou.ui;

import java.io.File;
import java.io.FileOutputStream;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.RootWorker;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.ActivityManager;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.RoundImageView;
import com.loopj.android.http.RequestParams;

/**
 * 注册过程中的完善个人信息
 * 
 * @author wangkang
 * 
 */
public class EditPassWordAcivity extends BaseActivity {
	private final String PICTURE = "picture";
	private static final int RESULT_FROM_CAMERA = 1;
	private static final int RESULT_FROM_CONTENT = 2;
	private static final int RESULT_FROM_CROP = 3;
	private static final int EDIT_NICKNAME_SUCCESS = 4;
	private static final int RESULT_FROM_LOCATION = 5;
	private static final int NICKNAME_MAX_NUM = 8;// 昵称字数最大值

	private CYLog log = CYLog.getInstance();
	private String phoneNum;
	private String vCode;
	private Dialog mAvatarDialog;
	private boolean mUploading = false;
	private MyHttpConnect mConn;
	private LinearLayout lyot_icon;
	private EditText editNickName;
	private EditText editPassword;
	private TextView txtUserIconinfo;
	private RoundImageView mHeaderIV;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edit_personal_password);
		ActivityManager.getInstance().addActivity(this);
		initView();
	}

	protected void onResume() {
		super.onResume();
	}

	public void showSharePhotoSelector() {
		try {
			mAvatarDialog = new AlertDialog.Builder(this).setItems(
					new CharSequence[] { getString(R.string.from_camera),
							getString(R.string.from_album),
							getString(R.string.cancel) },
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								mAvatarDialog.dismiss();
								Intent iCamera = new Intent(
										MediaStore.ACTION_IMAGE_CAPTURE);
								File photo = new File(UserInfoUtil
										.getUserIconPath());
								Uri uri = Uri.fromFile(photo);
								iCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
								startActivityForResult(iCamera,
										RESULT_FROM_CAMERA);
								break;
							case 1:
								mAvatarDialog.dismiss();
								Intent intent1 = new Intent(
										Intent.ACTION_GET_CONTENT);
								intent1.setType("image/*");
								startActivityForResult(intent1,
										RESULT_FROM_CONTENT);
								break;
							case 2:
								mAvatarDialog.dismiss();
								break;
							default:
								break;
							}
						}
					}).create();
			mAvatarDialog.setTitle(getString(R.string.point));
			mAvatarDialog.setCanceledOnTouchOutside(true);
			mAvatarDialog.show();
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void initView() {
		try {
			phoneNum = getIntent().getStringExtra(Params.REGIST.TELEPHONE);
			vCode = getIntent().getStringExtra(Params.REGIST.VALIDATE_CODE);
			View headerBar = findViewById(R.id.edit_personal_pwd_headerbar);
			ImageButton mBackBtn = (ImageButton) headerBar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			mBackBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			Button mOkBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			mOkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!isValidateUser()) {
						return;
					}
					registerUser();
				}
			});
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mOkBtn.setText(R.string.btn_finish);
			TextView mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			mHeaderTV.setText(R.string.regist_tele_title);
			editNickName = (EditText) findViewById(R.id.edit_nickname);
			editPassword = (EditText) findViewById(R.id.edit_password);
			lyot_icon = (LinearLayout) findViewById(R.id.lyot_icon);
			// lyot_icon.setOnClickListener(new ChangeAvatarListener());
			lyot_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!mUploading) {
						showSharePhotoSelector();
					} else {
						Toast.makeText(EditPassWordAcivity.this,
								getString(R.string.avatar_uploading),
								Toast.LENGTH_SHORT).show();
					}
				}
			});
			txtUserIconinfo = (TextView) findViewById(R.id.txt_usericon);
			mHeaderIV = (RoundImageView) findViewById(R.id.edit_info_header_iv);
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void installApp() {
		showLoadListProgressDialog();
		RequestParams params = new RequestParams();
		params.put("type", CyouApplication.getChannel());
		params.put("versioncode",
				String.valueOf(Util.getAppVersionCode(this.getPackageName())));
		String rootPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + Contants.PATH.ROOT_PATH;
		File mFilePath = new File(rootPath);
		String update = "0";
		if (mFilePath.exists() && mFilePath.length() > 0) {
			update = "1";
		}
		params.put("update", update);
		String url = HttpContants.NET.APP_INSTALL;
		MyHttpConnect.getInstance().post2Json(url, params,
				new JSONAsyncHttpResponseHandler() {
					@Override
					public void onSuccessForString(String content) {
						dismissProgressDialog();
						super.onSuccessForString(content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						dismissProgressDialog();
						super.onFailure(error, content);
					}
				});

	}

	private void registerUser() {
		if (!isValidateUser()) {
			return;
		}
		showLoadDoingProgressDialog();
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		RequestParams params = new RequestParams();

		params.put("phone", phoneNum);
		params.put("vfcode", vCode);
		params.put("nickname", editNickName.getText().toString().trim());
		params.put("password", editPassword.getText().toString().trim());
		params.put("country", Util.getCountryID());
		if (null != avatarFile && avatarFile.exists()
				&& avatarFile.length() > 0) {
			try {
				params.put(PICTURE, avatarFile);
			} catch (Exception e) {
				e.fillInStackTrace();
			}
		}
		mConn.post2Json(HttpContants.NET.REGISTER_PHONE, params,
				new JSONAsyncHttpResponseHandler() {
					@Override
					public void onSuccessForString(String content) {
						super.onSuccess(content);
						dismissProgressDialog();
						log.d("get register result is:" + content);
						if (TextUtils.isEmpty(content)) {
							Toast.makeText(EditPassWordAcivity.this,
									getString(R.string.tele_regist_error), Toast.LENGTH_SHORT).show();
							return;
						}
						String errorNo = JsonUtils.getJsonValue(content,
								"errorNo");
						if (String.valueOf(Contants.ERROR_NO.ERROR_2).equals(
								errorNo)) {
							Toast.makeText(EditPassWordAcivity.this,
									getString(R.string.tele_vacode_timeout), Toast.LENGTH_SHORT).show();
							return;
						}
						if (String.valueOf(Contants.ERROR_NO.ERROR_3).equals(
								errorNo)) {
							Toast.makeText(EditPassWordAcivity.this,
									getString(R.string.vacode_error), Toast.LENGTH_SHORT).show();
							return;
						}
						if (String.valueOf(Contants.ERROR_NO.ERROR_4).equals(
								errorNo)) {
							Toast.makeText(EditPassWordAcivity.this,
									getString(R.string.nickname_repeat), Toast.LENGTH_SHORT).show();
							editNickName.selectAll();
							return;
						}
						String data = JsonUtils.getJsonValue(content, "data");
						if (TextUtils.isEmpty(data)) {
							Toast.makeText(EditPassWordAcivity.this,
									getString(R.string.tele_regist_error), Toast.LENGTH_SHORT).show();
							return;
						}
						String uauth = JsonUtils.getJsonValue(data, "uauth");
						if (TextUtils.isEmpty(uauth)) {
							Toast.makeText(EditPassWordAcivity.this,
									getString(R.string.tele_regist_error), Toast.LENGTH_SHORT).show();
							return;
						} else {// 注册成功
							// 开始上传游戏
							SharedPreferenceUtil.saveLastTelephone(phoneNum);
							UserInfoUtil.setUauth(uauth, false);
							Intent mIntent = new Intent(
									EditPassWordAcivity.this, RootWorker.class);
							startService(mIntent);
							installApp();
							Toast.makeText(EditPassWordAcivity.this,getString(R.string.regist_success),
									Toast.LENGTH_SHORT).show();

							UserInfo userInfo = new UserInfo();
							userInfo.setNickname(editNickName.getText()
									.toString().trim());
							userInfo.setPassword(editPassword.getText()
									.toString().trim());
							UserInfoUtil.saveUserInfo(userInfo);
							Intent intent = new Intent(
									EditPassWordAcivity.this,
									GetContactsActivity.class);
							intent.putExtra("from", 2);
							startActivity(intent);
						}
						String utoken = JsonUtils.getJsonValue(data, "utoken");
						if(!TextUtils.isEmpty(utoken)){
							UserInfoUtil.setUToken(utoken);
						}
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								EditPassWordAcivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onFailure(Throwable error, String content) {
						dismissProgressDialog();
						Toast.makeText(EditPassWordAcivity.this,
								getString(R.string.getvacode_error), Toast.LENGTH_SHORT).show();
						super.onFailure(error, content);
					}
				});
	}

	private boolean isValidateUser() {
		String nickName = editNickName.getText().toString();
		String password = editPassword.getText().toString();
		if (TextUtils.isEmpty(nickName)) {
			Toast.makeText(EditPassWordAcivity.this, getString(R.string.please_input_nickname),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(EditPassWordAcivity.this,getString(R.string.bind_error_pwd),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (password.length() < 6) {
			Toast.makeText(EditPassWordAcivity.this, getString(R.string.password_length_error),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
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

	UserInfo playerinfo;
	File avatarFile = null;

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RESULT_FROM_CONTENT:
			try {
				if (data != null) {
					Intent intent2 = new Intent(
							"com.android.camera.action.CROP");
					intent2.setData(data.getData());
					intent2.putExtra("crop", "true");
					intent2.putExtra("aspectX", 1);
					intent2.putExtra("aspectY", 1);
					intent2.putExtra("outputX", Config.USERICON_WIDTH);
					intent2.putExtra("outputY", Config.USERICON_HEIGHT);
					intent2.putExtra("noFaceDetection", true);
					intent2.putExtra("return-data", true);
					startActivityForResult(intent2, RESULT_FROM_CROP);
				}
			} catch (Exception e) {
				log.e(e);
			}
			break;
		case RESULT_FROM_CAMERA:
			try {
				File photo = new File(UserInfoUtil.getUserIconPath());
				Intent intent1 = new Intent("com.android.camera.action.CROP");
				intent1.setDataAndType(Uri.fromFile(photo), "image/*");
				intent1.putExtra("crop", "true");
				intent1.putExtra("aspectX", 1);
				intent1.putExtra("aspectY", 1);
				intent1.putExtra("outputX", Config.USERICON_WIDTH);
				intent1.putExtra("outputY", Config.USERICON_HEIGHT);
				intent1.putExtra("noFaceDetection", true);
				intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				intent1.putExtra("return-data", true);
				startActivityForResult(intent1, RESULT_FROM_CROP);
			} catch (Exception e) {
				log.e(e);
			}
			break;
		case RESULT_FROM_CROP:
			try {
				if (data != null) {
					 userIconBitmap = (Bitmap) data.getExtras().getParcelable(
							"data");
					if (userIconBitmap != null) {

						// if (TextUtils.isEmpty(SharedPreferenceUtil
						// .getCurrentUserInfo().getPicture())) {
						avatarFile = new File(
								UserInfoUtil.getUserIconPath());
						// } else {
						// avatarFile = new File(SharedPreferenceUtil
						// .getCurrentUserInfo().getPicture());
						// }
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
							userIconBitmap.compress(Bitmap.CompressFormat.PNG, 100,
									fileoutputstream);
							fileoutputstream.flush();
							mHeaderIV.getImageView().setImageBitmap(userIconBitmap);
							fileoutputstream.close();
						} catch (Exception exception1) {
							log.e(exception1);
						}
					} else {
						log.e("bitmap is null");
					}
				} else {
					log.e("data is null");
				}
			} catch (Exception e) {
				log.e(e);
			}
			break;
		case EDIT_NICKNAME_SUCCESS:
			try {
				if (data != null) {
					String nickname = data.getStringExtra("nickname");
					// mNickNameTV.setText(nickname);
				} else {
					log.e("data is null");
				}
			} catch (Exception e) {
				log.e(e);
			}
			break;

		default:
			break;
		}
	}
}
