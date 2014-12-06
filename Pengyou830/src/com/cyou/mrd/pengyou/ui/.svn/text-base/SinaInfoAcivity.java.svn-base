package com.cyou.mrd.pengyou.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.RootWorker;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.WeiboApi;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.RoundImageView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 注册过程中的完善个人信息
 * 
 * @author wangkang
 * 
 */
public class SinaInfoAcivity extends BaseActivity {

	private CYLog log = CYLog.getInstance();
	private RoundImageView imgHeaderIcon;
	private EditText txtUserSign;
	private EditText txtUsername;
	private DisplayImageOptions mOptions;
	public static final String NICKNAME = "nickname";
	private final String SIGN = "signature";
	private final String PICTURE = "picture";
	private Dialog mAvatarDialog;
	private static final int RESULT_FROM_CAMERA = 1;
	private static final int RESULT_FROM_CONTENT = 2;
	private static final int RESULT_FROM_CROP = 3;
	private LinearLayout lyot_icon;
	private boolean mUploading = false;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.sina_info_layout);
		initView();
		initData();
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
								BehaviorInfo behaviorInfo = new BehaviorInfo(
										CYSystemLogUtil.ME.BTN_USERINFO_ICON_EDIT_ID,
										CYSystemLogUtil.ME.BTN_USERINFO_ICON_EDIT_NAME);
								CYSystemLogUtil.behaviorLog(behaviorInfo);
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
								behaviorInfo = new BehaviorInfo(
										CYSystemLogUtil.ME.BTN_USERINFO_ICON_EDIT_ID,
										CYSystemLogUtil.ME.BTN_USERINFO_ICON_EDIT_NAME);
								CYSystemLogUtil.behaviorLog(behaviorInfo);
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
			this.mOptions = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.avatar_defaul)
					.showImageForEmptyUri(R.drawable.avatar_defaul)
					.showImageOnFail(R.drawable.avatar_defaul).cacheInMemory()
					.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(15))
					.resetViewBeforeLoading().build();
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
					// updateUserInfo();
					uploadUserInfo();
				}
			});
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mOkBtn.setText(R.string.btn_finish);
			TextView mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			mHeaderTV.setText(R.string.app_name);
			txtUsername = (EditText) findViewById(R.id.edit_nickname);
			txtUserSign = (EditText) findViewById(R.id.edit_password);
			imgHeaderIcon = (RoundImageView) findViewById(R.id.edit_info_header_iv);
			lyot_icon = (LinearLayout) findViewById(R.id.lyot_icon);
			lyot_icon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!mUploading) {
						showSharePhotoSelector();
					} else {
						Toast.makeText(SinaInfoAcivity.this,
								getString(R.string.avatar_uploading),
								Toast.LENGTH_SHORT).show();
					}
				}
			});
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void uploadUserInfo() {
		String nickName = txtUsername.getText().toString();
		if (TextUtils.isEmpty(nickName)) {
			Toast.makeText(SinaInfoAcivity.this,
					R.string.please_input_nickname, Toast.LENGTH_SHORT).show();
			return;
		}
		File mFile = avatarFile;
		if (mFile == null || !mFile.exists()) {
			if (!TextUtils.isEmpty(imageUrl)) {
				mFile = CYImageLoader.getImageFileByUrl(imageUrl);
			}
		}
		showLoadDoingProgressDialog();
		RequestParams params = new RequestParams();
		if (mFile != null && mFile.exists()) {
			try {
				params.put(PICTURE, mFile);
			} catch (FileNotFoundException e) {
				log.e(e);
			}
		}
		mUploading = true;
		params.put(NICKNAME, txtUsername.getText().toString().trim());
		params.put(SIGN, txtUserSign.getText().toString().trim());
		MyHttpConnect.getInstance().post(HttpContants.NET.UPDATE_USER, params,
				new JSONAsyncHttpResponseHandler() {
					@Override
					public void onSuccessForString(String content) {
						dismissProgressDialog();
						// 开始上传游戏
						try {
							try {
								if (!TextUtils.isEmpty(content)) {
									String errorNo = JsonUtils.getJsonValue(
											content, "errorNo");
									if (String.valueOf(
											Contants.ERROR_NO.ERROR_4).equals(
											errorNo)) {
										Toast.makeText(
												SinaInfoAcivity.this,
												getString(R.string.nickname_repeat),
												Toast.LENGTH_SHORT).show();
										txtUsername.selectAll();
										return;
									}
									JSONObject obj = new JSONObject(content);
									if (obj.has("successful")) {
										String avatar = obj
												.getString("successful");
										if ("1".equals(avatar)) {
											String data = JsonUtils
													.getJsonValue(content,
															"data");
											String picture = JsonUtils
													.getJsonValue(data,
															"picture");
											String pictureorig = JsonUtils
													.getJsonValue(data,
															"pictureorig");
											UserInfoUtil
													.saveUserPicture(SinaInfoAcivity.this,picture,
															pictureorig);
										}
									}
									Intent mIntent = new Intent(
											SinaInfoAcivity.this,
											RootWorker.class);
									startService(mIntent);
									Toast.makeText(SinaInfoAcivity.this,
											getString(R.string.regist_success),
											Toast.LENGTH_SHORT).show();
									UserInfo userInfo = new UserInfo();
									userInfo.setNickname(txtUsername.getText()
											.toString().trim());
									userInfo.setSignature(txtUserSign.getText()
											.toString().trim());
									UserInfoUtil.saveUserInfo(userInfo);
									Intent mIntent1 = new Intent(
											SinaInfoAcivity.this,
											AddFriendSinaActivity.class);
									startActivity(mIntent1);
									finish();
								}
							} catch (Exception e) {
								log.e(e);
							}
						} catch (Exception e) {
							log.e(e);
						}
						super.onSuccess(content);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								SinaInfoAcivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onFailure(Throwable error, String content) {
						dismissProgressDialog();
						Intent mIntent = new Intent(SinaInfoAcivity.this,
								RootWorker.class);
						startService(mIntent);
						Toast.makeText(SinaInfoAcivity.this,
								getString(R.string.regist_success),
								Toast.LENGTH_SHORT).show();
						UserInfo userInfo = new UserInfo();
						userInfo.setNickname(txtUsername.getText().toString()
								.trim());
						userInfo.setSignature(txtUserSign.getText().toString()
								.trim());
						UserInfoUtil.saveUserInfo(userInfo);
						Intent mIntent1 = new Intent(SinaInfoAcivity.this,
								AddFriendSinaActivity.class);
						startActivity(mIntent1);
						finish();
						super.onFailure(error, content);
					}
				});
	}

	private MyHandler myHandler = new MyHandler();

	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == -1 || msg.what == -2) {
				Intent mIntent = new Intent(SinaInfoAcivity.this,
						AddFriendSinaActivity.class);
				startActivity(mIntent);
				finish();
			} else {
				Bundle b = msg.getData();
				String data = b.getString("data");
				loadUserInfo(data);
			}
			super.handleMessage(msg);
		}

	}

	String imageUrl;

	private void loadUserInfo(String data) {
		if (TextUtils.isEmpty(data)) {
			Intent mIntent = new Intent(SinaInfoAcivity.this,
					AddFriendSinaActivity.class);
			startActivity(mIntent);
			finish();
			return;
		}
		imageUrl = JsonUtils.getJsonValue(data, "profile_image_url");
		String username = JsonUtils.getJsonValue(data, "name");
		String sigh = JsonUtils.getJsonValue(data, "description");
		CYImageLoader.displayOtherImage(imageUrl, imgHeaderIcon.getImageView(),
				mOptions);
		if (!TextUtils.isEmpty(username)) {
			txtUsername.setText(username);
		}
		if (!TextUtils.isEmpty(sigh)) {
			txtUserSign.setText(sigh);
		}
	}

	private void initData() {
		UsersAPI usersApi = new UsersAPI(new Oauth2AccessToken(
				SharedPreferenceUtil.getSinaWeiboToken(),
				String.valueOf(WeiboApi.EXPIRES_IN_DEFAULT)));

		long userId = 0;
		try {
			userId = Long.parseLong(SharedPreferenceUtil.getSinaWeiboUid());
		} catch (Exception e) {
			log.e(e);
		}
		usersApi.show(userId, new RequestListener() {

			@Override
			public void onIOException(IOException arg0) {
				myHandler.sendEmptyMessage(-1);
			}

			@Override
			public void onError(WeiboException arg0) {
				myHandler.sendEmptyMessage(-1);

			}

			@Override
			public void onComplete(String arg0) {
				Message msg = new Message();
				Bundle bunlder = new Bundle();
				bunlder.putString("data", arg0);
				msg.setData(bunlder);
				myHandler.sendMessage(msg);
			}
		});
	}

	File avatarFile = null;
	Bitmap bitmap = null;

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
					bitmap = (Bitmap) data.getExtras().getParcelable("data");
					if (bitmap != null) {

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
							bitmap.compress(Bitmap.CompressFormat.PNG, 100,
									fileoutputstream);
							fileoutputstream.flush();
							fileoutputstream.close();
						} catch (Exception exception1) {
							log.e(exception1);
						}
						imgHeaderIcon.getImageView().setImageBitmap(bitmap);
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

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		try {
			if (null != bitmap) {
				bitmap.recycle();
				bitmap = null;
			}
		} catch (Exception e) {
			log.e(e);
		}
		super.onDestroy();
	}
}
