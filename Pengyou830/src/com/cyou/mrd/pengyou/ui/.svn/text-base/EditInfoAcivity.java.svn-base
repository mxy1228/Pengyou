package com.cyou.mrd.pengyou.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.AvatarUtil;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.utils.WeiboApi;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.RoundImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.AsyncWeiboRunner;
import com.weibo.sdk.android.net.RequestListener;

public class EditInfoAcivity extends BaseActivity {
	private final String BIRTHDAY = "birthday";
	private final String GENDER = "gender";
	private final String AREAID = "areaid";
	private final String FAVGAME = "favgame";
	private final String PICTURE = "picture";
	private final String BOY = "1";
	private final String GIRL = "2";

	private static final int RESULT_FROM_CAMERA = 1;
	private static final int RESULT_FROM_CONTENT = 2;
	private static final int RESULT_FROM_CROP = 3;
	private static final int EDIT_NICKNAME_SUCCESS = 4;
	private static final int RESULT_FROM_LOCATION = 5;
	public static final int RESULT_NICKNAME_CODE = 10;// 修改昵称返回码
	public static final int RESULT_SIGN_CODE = 11;// 修改签名返回码
	private CYLog log = CYLog.getInstance();
	private Dialog mAvatarDialog;
	private Dialog mFavGameDialog;
	private View mAvatarSelectorView;
	private ImageButton mBackBtn;
	private Button mBirthdayBtn;
	private Dialog mBirthdayDialog;
	private TextView mBirthdayTV;
	private Button mCancelBtn;
	private DatePicker mDatePicker;
	// private Button mFromAlbumBtn;
	// private Button mFromCameraBtn;
	private String mGender;
	private Dialog mGenderDialog;
	private TextView mGenderTV;
	private RoundImageView mHeaderIV;
	private TextView mLocateTV;
	private TextView mNickNameTV;
	private LinearLayout mUploadHeaderLL;
	private LinearLayout mNickNameLL;
	private LinearLayout mGenderLL;
	private LinearLayout mLocateLL;
	private LinearLayout mBirthdayLL;
	private LinearLayout mFavGameLl;
	private LinearLayout mTagLL;
	private TextView mTagTV;
	private boolean mUploading = false;
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private MyHttpConnect mConn;
	private DisplayImageOptions mOptions;
	private TextView txtFavGame;
	private TextView mHeaderTV;
	private ImageView imgSex;
	private LinearLayout lyotMyDCode;
	private Button btnBindTelephone;// 绑定/更换手机
	private TextView txtBindTele;
	private Button btnBindSns;
	private TextView txtBindSns;

	private final String APPKEY = "3443142555";
	private final String URL = "http://123.126.49.182/weibocallback.html";
	private Weibo mWeibo;
	private final String TOKEN = "access_token";
	private final String EXPIRES_IN = "expires_in";
	private final String UID = "uid";
	public String SNS_SINA = "sina";
	private ProgressBar progressBar;
	private TextView txtScoreDiscrib;
	private TextView txtProgress;
	private ImageView imgTeleFinish;
	private ImageView imgUserIconFinish;
	private ImageView imgNicknameFinish;
	private ImageView imgSexFinish;
	private ImageView imgBirthdayFinish;
	private ImageView imgAddressFinish;
	private ImageView imgSignFinish;
	private Drawable progressDrawable;
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edit_personal_info);
		this.mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnFail(R.drawable.avatar_defaul)
				.displayer(new RoundedBitmapDisplayer(15)).build();
		initView();
		initPersonalInfo();
		initBirthdaySelector();
	}

	@Override
	protected void onRestart() {
		log.d("重新加载!");
		initPersonalInfo();
		super.onRestart();
	}

	private void initView() {
		try {
			mHeaderIV = (RoundImageView) findViewById(R.id.edit_info_header_iv);
			mUploadHeaderLL = (LinearLayout) findViewById(R.id.edit_icon_ll);
			mUploadHeaderLL.setOnClickListener(new ChangeAvatarListener());
			mNickNameTV = (TextView) findViewById(R.id.edit_info_nickname_tv);
			mGenderTV = (TextView) findViewById(R.id.edit_info_gender_tv);
			mLocateTV = (TextView) findViewById(R.id.edit_info_locate_tv);
			mBirthdayTV = (TextView) findViewById(R.id.edit_info_birthday_tv);
			mNickNameLL = (LinearLayout) findViewById(R.id.edit_nickname_ll);
			mNickNameLL.setOnClickListener(new ClickListener());
			mGenderLL = (LinearLayout) findViewById(R.id.edit_gender_ll);
			mGenderLL.setOnClickListener(new ClickListener());
			mLocateLL = (LinearLayout) findViewById(R.id.edit_locate_ll);
			mLocateLL.setOnClickListener(new ClickListener());
			mBirthdayLL = (LinearLayout) findViewById(R.id.edit_birthday_ll);
			mBirthdayLL.setOnClickListener(new ClickListener());
			mFavGameLl = (LinearLayout) findViewById(R.id.edit_favgame_ll);
			mFavGameLl.setOnClickListener(new ClickListener());
			this.mTagLL = (LinearLayout) findViewById(R.id.edit_info_tag_ll);
			this.mTagLL.setOnClickListener(new ClickListener());
			this.mTagTV = (TextView) findViewById(R.id.edit_info_tag_tv);
			txtFavGame = (TextView) findViewById(R.id.edit_info_favgame_tv);
			View headerBar = findViewById(R.id.edit_headerbar);
			this.mBackBtn = (ImageButton) headerBar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			mBackBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			this.mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			this.mHeaderTV.setText(R.string.personal_info);
			imgSex = (ImageView) findViewById(R.id.img_sex);
			lyotMyDCode = (LinearLayout) findViewById(R.id.edit_mydcode_ll);
			lyotMyDCode.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					BehaviorInfo behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.ME.BTN_USERINFO_DCODE_ID,
							CYSystemLogUtil.ME.BTN_USERINFO_DCODE_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					Intent mIntent = new Intent();
					mIntent.setClass(EditInfoAcivity.this,
							MyDcodeActivity.class);
					startActivity(mIntent);
				}
			});
		} catch (Exception e) {
			log.e(e);
		}
		mHeaderIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String photoUrl = UserInfoUtil.getCurrentPicOrig();
				Intent mIntent = new Intent();
				mIntent.putExtra(Params.SHOW_PHOTO.PHOTO_TYPE,
						Params.SHOW_PHOTO.PHOTO_USER);
				mIntent.setClass(EditInfoAcivity.this, ShowPhotoActivity.class);
				mIntent.putExtra(Params.PHOTO_URL, photoUrl);
				startActivity(mIntent);
			}
		});
		txtBindTele = (TextView) findViewById(R.id.txt_telephone);
		btnBindTelephone = (Button) findViewById(R.id.btn_bindtele);
		btnBindTelephone.setOnClickListener(new ClickListener());
		btnBindSns = (Button) findViewById(R.id.btn_bindsns);
		btnBindSns.setOnClickListener(new ClickListener());
		txtBindSns = (TextView) findViewById(R.id.txt_bindsns_state);
		progressBar = (ProgressBar) findViewById(R.id.pb_personal_info);
        txtScoreDiscrib = (TextView) findViewById(R.id.txt_personal_score_discrib);
		txtProgress = (TextView) findViewById(R.id.txt_personal_sprogress);
		imgTeleFinish = (ImageView) findViewById(R.id.img_tele_finish);
		imgAddressFinish = (ImageView) findViewById(R.id.img_address_finish);
		imgBirthdayFinish = (ImageView) findViewById(R.id.img_birthday_finish);
		imgNicknameFinish = (ImageView) findViewById(R.id.img_nickname_finish);
		imgSignFinish = (ImageView) findViewById(R.id.img_tag_finish);
		imgSexFinish = (ImageView) findViewById(R.id.img_sex_finish);
		imgUserIconFinish = (ImageView) findViewById(R.id.img_usericon_finish);
		progressDrawable=getResources().getDrawable(R.drawable.userinfo_pb);

	}
	private class ChangeAvatarListener implements
			android.view.View.OnClickListener {

		public void onClick(View view) {
			try {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_USERINFO_ICON_ID,
						CYSystemLogUtil.ME.BTN_USERINFO_ICON_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if (!mUploading) {
					showSharePhotoSelector();
				} else {
					Toast.makeText(EditInfoAcivity.this,
							getString(R.string.avatar_uploading),
							Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) {
				log.e(e);
			}
		}

	}

	private class ClickListener implements android.view.View.OnClickListener {

		public void onClick(View view) {
			try {
				switch (view.getId()) {
				// case R.id.header_left_btn:
				// EditInfoAcivity.this.finish();
				// break;
				case R.id.edit_nickname_ll:// 昵称
					// mNickNameDialog.show();
					BehaviorInfo behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.PLAYGAME.BTN_PLAYGAME_PERSONAL_ID,
							CYSystemLogUtil.PLAYGAME.BTN_PLAYGAME_PERSONAL_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					Intent mIntent = new Intent();
					mIntent.setClass(EditInfoAcivity.this,
							EditNickNameAcivity.class);
					startActivityForResult(mIntent, RESULT_NICKNAME_CODE);

					break;
				case R.id.edit_gender_ll:
					behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.ME.BTN_USERINFO_GENDER_ID,
							CYSystemLogUtil.ME.BTN_USERINFO_GENDER_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					showShareSexSelector();
					break;
				case R.id.edit_locate_ll:
					behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.ME.BTN_USERINFO_AREAID_ID,
							CYSystemLogUtil.ME.BTN_USERINFO_AREAID_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					// mSchoolDialog.show();
					Intent locate = new Intent(EditInfoAcivity.this,
							LocationSelectActivity.class);
					startActivityForResult(locate, RESULT_FROM_LOCATION);
					break;
				case R.id.edit_favgame_ll:
					behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.ME.BTN_USERINFO_GAMELIKE_ID,
							CYSystemLogUtil.ME.BTN_USERINFO_GAMELIKE_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					mFavGameDialog.show();
					break;
				case R.id.edit_birthday_ll:
					behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.ME.BTN_USERINFO_BIRTHDAY_ID,
							CYSystemLogUtil.ME.BTN_USERINFO_BIRTHDAY_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					mBirthdayDialog.show();
					mDatePicker.clearFocus();
					break;
				case R.id.edit_info_birthday_btn:
					// 若不加clearFocus()则用户手工输入后无法获取输入的值
					mDatePicker.clearFocus();
					mBirthdayDialog.dismiss();
					year = mDatePicker.getYear() - 1900;
					month = mDatePicker.getMonth();
					day = mDatePicker.getDayOfMonth();
					Date date = new Date(year, month, day);
					log.d("checked date:" + year + "_" + month + "_" + day);
					if (date.after(new Date())) {
						Toast.makeText(EditInfoAcivity.this,
								R.string.birthday_error, Toast.LENGTH_SHORT)
								.show();
					} else {
						updateInfo(BIRTHDAY,
								String.valueOf(date.getTime() / 1000));
					}
					break;
				case R.id.edit_info_tag_ll:
					behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.ME.BTN_USERINFO_SIGN_ID,
							CYSystemLogUtil.ME.BTN_USERINFO_SIGN_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					// mTagDialog.show();
					Intent mIntent1 = new Intent();
					mIntent1.setClass(EditInfoAcivity.this,
							EditSignAcivity.class);
					startActivityForResult(mIntent1, RESULT_SIGN_CODE);

					break;
				case R.id.btn_bindtele:// 绑定/更换
					bindOrChangeTele();
					break;
				case R.id.btn_bindsns:// 绑定新浪微博
					mWeibo = Weibo.getInstance(APPKEY, URL);
					mWeibo.authorize(EditInfoAcivity.this,
							new AuthorizeListener());
					break;
				}

			} catch (Exception e) {
				log.e(e);
			}
		}
	}

	/**
	 * 社交账号登陆
	 * 
	 * @param snsName
	 * @param snsToken
	 * @param snsUid
	 */
	private void snsUserLogin(String snsName, final String snsToken,
			final String snsUid, final String extime) {
		showLoadDoingProgressDialog();
		RequestParams params = new RequestParams();
		params.put("snsnm", snsName);
		params.put("snstoken", snsToken);
		params.put("snsusrid", snsUid);
		params.put("country", Util.getCountryID());
		MyHttpConnect.getInstance().post(HttpContants.NET.SNS_BIND, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						dismissProgressDialog();
						showToastMessage(
								getResources().getString(
										R.string.download_error_network_error),
								1);
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(EditInfoAcivity.this);
						dialog.create().show();
					}
					
					@Override
					public void onSuccess(int statusCode,String content) {
						super.onSuccess(statusCode, content);
						dismissProgressDialog();
						log.d("sns user login is:" + content);
						if (TextUtils.isEmpty(content)) {
							showToastMessage(
									getResources()
											.getString(
													R.string.download_error_network_error),
									1);
							return;
						}
						String successful = JsonUtils.getJsonValue(content,
								"successful");
						if (!TextUtils.isEmpty(successful)
								&& "1".equals(successful)) {
							saveToken(snsToken, extime, snsUid);
							// 授权成功
							txtBindSns
									.setText(getString(R.string.txt_bind_sina_name));
							btnBindSns.setOnClickListener(null);
							btnBindSns.setTextColor(getResources().getColor(
									R.color.bind_sns_text));
							btnBindSns
									.setBackgroundResource(R.drawable.btn_tele_bind_clicked);
							btnBindSns
									.setText(getString(R.string.txt_bindsina_state));
							Intent mIntent1 = new Intent(EditInfoAcivity.this,
									AddFriendSinaActivity.class);
							mIntent1.putExtra(Params.FROM, Params.FROM_EDIT);
							startActivity(mIntent1);
							return;
						}
						String errorNo = JsonUtils.getJsonValue(content,
								"errorNo");
						weiboLogOut(snsToken);
						WeiboApi.getInstance().unBindSina(EditInfoAcivity.this);
						if (!TextUtils.isEmpty(errorNo)
								&& String.valueOf(
										Contants.ERROR_NO.ERROR_SNS_BINDED)
										.equals(errorNo)) {
							showToastMessage(
									getResources().getString(
											R.string.bind_sns_bined_error), 1);

						} else {
							showToastMessage(
									getResources()
											.getString(
													R.string.download_error_network_error),
									1);
						}
						super.onSuccess(content);
					}
				});
	}

	private void bindOrChangeTele() {
		Intent mIntent = new Intent();
		mIntent.setClass(EditInfoAcivity.this,
				RegisterForTelePhoneActivity.class);
		startActivity(mIntent);
	}

	private void updateInfo(final String key, final String value) {
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		RequestParams params = new RequestParams();
		if (PICTURE.equals(key)) {
			File mFile = new File(value);
			if (mFile == null || !mFile.exists()) {
				Toast.makeText(EditInfoAcivity.this,
						R.string.upload_avatar_failed, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			try {
				params.put(PICTURE, mFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			params.put(key, value);
		}
		MyHttpConnect.getInstance().post2Json(HttpContants.NET.UPDATE_USER,
				params, new JSONAsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						Toast.makeText(EditInfoAcivity.this,
								R.string.modify_failed, Toast.LENGTH_SHORT)
								.show();
						log.e("error is:" + content);
						super.onFailure(error, content);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								EditInfoAcivity.this);
						dialog.create().show();
						super.onLoginOut();
					}

					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						super.onSuccess(statusCode,response);
						log.d(" update userinfo result is:"
								+ response.toString());
						if (null == response || !response.has("successful")) {
							Toast.makeText(EditInfoAcivity.this,
									R.string.modify_failed, Toast.LENGTH_SHORT)
									.show();
							return;
						}
						try {
							playerinfo = UserInfoUtil.getCurrentUserInfo();
							String successful = response
									.getString("successful");
							if ("1".equals(successful)) {
								if (GENDER.equals(key)) {
									if (!TextUtils.isEmpty(value)) {
										playerinfo.setGender(Integer
												.parseInt(mGender));
									}
								}
								if (BIRTHDAY.equals(key)) {
									if (!TextUtils.isEmpty(value)) {
										playerinfo.setBirthday(Long
												.parseLong(value));
									}
								}
								if (AREAID.equals(key)) {
									playerinfo.setAreaid(value);
								}
								if (FAVGAME.equals(key)) {
									playerinfo.setFavgame(value);
								}
								if (PICTURE.equals(key)) {
									String data = JsonUtils.getJsonValue(
											response.toString(), "data");
									String picture = JsonUtils.getJsonValue(
											data, "picture");
									String pictureorig = JsonUtils
											.getJsonValue(data, "pictureorig");
									playerinfo.setPicture(picture);
									playerinfo.setPictureorig(pictureorig);
									UserInfoUtil.saveUserPicture(
											EditInfoAcivity.this, picture,
											pictureorig);
									Toast.makeText(
											EditInfoAcivity.this,
											getString(R.string.upload_avatar_success),
											Toast.LENGTH_SHORT).show();
								}
								UserInfoUtil.saveUserInfo(playerinfo);
								initPersonalInfo();
								return;
							}
							Toast.makeText(EditInfoAcivity.this,
									R.string.modify_failed, Toast.LENGTH_SHORT)
									.show();

						} catch (Exception e) {
							log.e(e);
							Toast.makeText(EditInfoAcivity.this,
									R.string.modify_failed, Toast.LENGTH_SHORT)
									.show();
							return;
						}

						super.onSuccess(statusCode, response);
					}

				});
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
								dialog.dismiss();
								AvatarUtil.startCamera(EditInfoAcivity.this);
//								mAvatarDialog.dismiss();
//								if (!(Environment.getExternalStorageState()
//										.equals(Environment.MEDIA_MOUNTED)
//										&& Environment
//												.getExternalStorageDirectory()
//												.canWrite() && !Environment
//										.getDownloadCacheDirectory().canWrite())) {
//
//									showToastMessage(
//											getString(R.string.sdcard_write_error),
//											0);
//									return;
//								}
								BehaviorInfo behaviorInfo = new BehaviorInfo(
										CYSystemLogUtil.ME.BTN_USERINFO_ICON_EDIT_ID,
										CYSystemLogUtil.ME.BTN_USERINFO_ICON_EDIT_NAME);
								CYSystemLogUtil.behaviorLog(behaviorInfo);
//								Intent iCamera = new Intent(
//										MediaStore.ACTION_IMAGE_CAPTURE);
//								File photo = new File(tempImageFilePath);
//								Uri uri = Uri.fromFile(photo);
//								iCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//								startActivityForResult(iCamera,
//										RESULT_FROM_CAMERA);
								break;
							case 1:
								dialog.dismiss();
								AvatarUtil.startAlbums(EditInfoAcivity.this);
//								if (!(Environment.getExternalStorageState()
//										.equals(Environment.MEDIA_MOUNTED)
//										&& Environment
//												.getExternalStorageDirectory()
//												.canWrite() && !Environment
//										.getDownloadCacheDirectory().canWrite())) {
//
//									showToastMessage(
//											getString(R.string.sdcard_write_error),
//											0);
//									return;
//								}
//								mAvatarDialog.dismiss();
								behaviorInfo = new BehaviorInfo(
										CYSystemLogUtil.ME.BTN_USERINFO_ICON_EDIT_ID,
										CYSystemLogUtil.ME.BTN_USERINFO_ICON_EDIT_NAME);
								CYSystemLogUtil.behaviorLog(behaviorInfo);
//								Intent intent1 = new Intent(
//										Intent.ACTION_GET_CONTENT);
//								intent1.setType("image/*");
//								startActivityForResult(intent1,
//										RESULT_FROM_CONTENT);
								break;
							case 2:
								mAvatarDialog.dismiss();
								break;
							default:
								break;
							}
						}
					}).create();
			mAvatarDialog.setTitle(R.string.point);
			mAvatarDialog.setCanceledOnTouchOutside(true);
			mAvatarDialog.show();
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void initBirthdaySelector() {
		try {
			mBirthdayDialog = new Dialog(this);
			mBirthdayDialog.setContentView(R.layout.edit_info_birthday);
			mBirthdayBtn = (Button) mBirthdayDialog
					.findViewById(R.id.edit_info_birthday_btn);
			mBirthdayBtn.setOnClickListener(new ClickListener());
			mDatePicker = (DatePicker) mBirthdayDialog
					.findViewById(R.id.edit_info_birthday_dp);
			String birthday = mBirthdayTV.getText().toString();
			String[] time = birthday.split("-");
			if (null != time && time.length > 2) {
				mDatePicker.updateDate(Integer.parseInt(time[0]),
						Integer.parseInt(time[1]) - 1,
						Integer.parseInt(time[2]));
			}
			mBirthdayDialog.setTitle(R.string.point);
			mBirthdayDialog.setCanceledOnTouchOutside(true);
		} catch (Exception e) {
			log.e(e);
		}
	}

	public void showShareSexSelector() {
		try {
			mGenderDialog = new AlertDialog.Builder(this).setItems(
					new CharSequence[] { getString(R.string.boy),
							getString(R.string.girl),
							getString(R.string.cancel) },
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								mGenderDialog.dismiss();
								mGender = BOY;
								updateInfo(GENDER, mGender);
								break;
							case 1:
								mGenderDialog.dismiss();
								mGender = GIRL;
								updateInfo(GENDER, mGender);
								break;
							case 2:
								mBirthdayDialog.dismiss();
								break;
							default:
								break;
							}
						}
					}).create();
			mGenderDialog.setTitle(R.string.point);
			mGenderDialog.setCanceledOnTouchOutside(true);
			mGenderDialog.show();
		} catch (Exception e) {
			log.e(e);
		}
	}

	UserInfo playerinfo;
	Bitmap bitmap;

	private void initPersonalInfo() {
		try {
			playerinfo = UserInfoUtil.getCurrentUserInfo();
			float progress = UserInfoUtil.getPersonalInfoProgress();
			int personProgress = (int) (progress * 10);
			try {
				progressBar = (ProgressBar) findViewById(R.id.pb_personal_info);
				progressBar.setProgressDrawable(progressDrawable);
			} catch (Exception e) {
				CYLog.getInstance().e(e);
			} catch (Error e) {
				CYLog.getInstance().e(e);
			}
			progressBar.setProgress(personProgress);
			txtProgress.setText(personProgress + "%");
			if(UserInfoUtil.getIsCanExchangeScore()) {
				txtScoreDiscrib.setText(getScoreDescribeStyle(UserInfoUtil.getCMSPersonalInfoScore()));
				txtScoreDiscrib.setVisibility(View.VISIBLE);
			} else {
				txtScoreDiscrib.setVisibility(View.GONE);
			}

			log.d(playerinfo.toString());
			if (playerinfo != null) {
				if (playerinfo.getGender() == Contants.GENDER_TYPE.BOY) {
					mGenderTV.setText(getString(R.string.boy));
					imgSexFinish.setVisibility(View.VISIBLE);
					imgSex.setVisibility(View.VISIBLE);
					imgSex.setImageResource(R.drawable.img_person_sex_male);
				} else if (playerinfo.getGender() == Contants.GENDER_TYPE.GIRL) {
					mGenderTV.setText(getString(R.string.girl));
					imgSexFinish.setVisibility(View.VISIBLE);
					imgSex.setVisibility(View.VISIBLE);
					imgSex.setImageResource(R.drawable.img_person_sex_famale);
				} else {
					mGenderTV.setText("");
					imgSexFinish.setVisibility(View.GONE);
					imgSex.setVisibility(View.GONE);
				}
				if (!TextUtils.isEmpty(playerinfo.getPicture())) {
					CYImageLoader.displayImg(playerinfo.getPicture(),
							mHeaderIV.getImageView(), mOptions);
					imgUserIconFinish.setVisibility(View.VISIBLE);
				}
				mNickNameTV.setText(playerinfo.getNickname());
				if (!TextUtils.isEmpty(playerinfo.getNickname())) {
					imgNicknameFinish.setVisibility(View.VISIBLE);
				}
				mLocateTV.setText(playerinfo.getAreaid());
				if (!TextUtils.isEmpty(playerinfo.getAreaid())) {
					imgAddressFinish.setVisibility(View.VISIBLE);
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				if (0 != playerinfo.getBirthday()) {
					Date date = new Date(playerinfo.getBirthday() * 1000);
					mBirthdayTV.setText(format.format(date));
					imgBirthdayFinish.setVisibility(View.VISIBLE);
				}
				mTagTV.setText(playerinfo.getSignature());
				if (SharedPreferenceUtil.hasUserSignUpdate()) {
					imgSignFinish.setVisibility(View.VISIBLE);
				}
				txtFavGame.setText(playerinfo.getFavgame());
				String telephone = playerinfo.getPhone();
				if (!UserInfoUtil.isBindPhone()) {// 若手机号为空 则未绑定
					String txtBindTeleString = getString(R.string.txt_bindtele,
							"");
					txtBindTeleString = txtBindTeleString.replace(":", "");
					txtBindTele.setText(txtBindTeleString);
					btnBindTelephone
							.setText(getString(R.string.btn_bindtele_unbind));
					btnBindTelephone
							.setBackgroundResource(R.drawable.bind_telp_btn_blue_xbg);
					btnBindTelephone.setTextColor(Color.WHITE);
				} else {
					String phone = getString(R.string.txt_tele_binded,
							telephone);
					btnBindTelephone
							.setText(getString(R.string.btn_bindtele_change));
					btnBindTelephone
							.setBackgroundResource(R.drawable.bind_telp_btn_xbg);
					int index = phone.indexOf(telephone);
					SpannableStringBuilder style = new SpannableStringBuilder(
							phone);
					style.setSpan(new ForegroundColorSpan(Color.BLACK), index,
							index + telephone.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					txtBindTele.setText(style);
					btnBindTelephone.setTextColor(getResources().getColor(
							R.color.bind_sns_text_state));
					if (!TextUtils.isEmpty(telephone)) {
						imgTeleFinish.setVisibility(View.VISIBLE);
					}
				}
				boolean isBindSina = WeiboApi.getInstance().isBindSina();
				if (isBindSina) {// 已绑定
					txtBindSns.setText(getString(R.string.txt_bind_sina_name));
					btnBindSns.setOnClickListener(null);
					btnBindSns.setText(getString(R.string.txt_bindsina_state));
					btnBindSns.setTextColor(getResources().getColor(
							R.color.bind_sns_text));
					btnBindSns
							.setBackgroundResource(R.drawable.btn_tele_bind_clicked);
				} else {
					txtBindSns.setText(getString(R.string.txt_bindsina));
					btnBindSns.setOnClickListener(new ClickListener());
					btnBindSns.setTextColor(getResources().getColor(
							R.color.bind_sns_text_state));
				}
			} else {
				log.e("CyouApplication.mUserInfo is null");
			}
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		log.d("拍照回调:" + requestCode + "_" + resultCode);
		switch (requestCode) {
		case RESULT_FROM_CONTENT:
			AvatarUtil.resultFromAlbums(EditInfoAcivity.this, resultCode, data);
//			if (resultCode != RESULT_OK) {
//				return;
//			}
//			try {
//				if (data != null) {
//					Intent intent2 = new Intent(
//							"com.android.camera.action.CROP");
//					intent2.setData(data.getData());
//					intent2.putExtra("crop", "true");
//					intent2.putExtra("aspectX", 1);
//					intent2.putExtra("aspectY", 1);
//					intent2.putExtra("outputX", Config.USERICON_WIDTH);
//					intent2.putExtra("outputY", Config.USERICON_HEIGHT);
//					intent2.putExtra("noFaceDetection", true);
//					intent2.putExtra("return-data", true);
//					startActivityForResult(intent2, RESULT_FROM_CROP);
//				}
//			} catch (Exception e) {
//				log.e(e);
//			}
			break;
		case RESULT_FROM_CAMERA:
			AvatarUtil.resultFromCamera(EditInfoAcivity.this, resultCode);
//			try {
//				if (resultCode != RESULT_OK) {
//					return;
//				}
//				File photo = new File(tempImageFilePath);
//				Intent intent1 = new Intent("com.android.camera.action.CROP");
//				intent1.setDataAndType(Uri.fromFile(photo), "image/*");
//				intent1.putExtra("crop", "true");
//				intent1.putExtra("aspectX", 1);
//				intent1.putExtra("aspectY", 1);
//				intent1.putExtra("outputX", Config.USERICON_WIDTH);
//				intent1.putExtra("outputY", Config.USERICON_HEIGHT);
//				intent1.putExtra("noFaceDetection", true);
//				intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
//				intent1.putExtra("return-data", true);
//				startActivityForResult(intent1, RESULT_FROM_CROP);
//			} catch (Exception e) {
//				log.e(e);
//			}
			break;
		case RESULT_FROM_CROP:
			Bitmap bitmap = AvatarUtil.resultFromCrop(EditInfoAcivity.this, resultCode, data);
			if(bitmap != null){
				mHeaderIV.setImageBitmap(bitmap);
				updateInfo(PICTURE, new File(UserInfoUtil.getUserIconPath()).getAbsolutePath());
			}
//			try {
//				if (data != null) {
//					bitmap = (Bitmap) data.getExtras().getParcelable("data");
//					if (bitmap != null) {
//						File avatarFile = null;
//						avatarFile = new File(UserInfoUtil.getUserIconPath());
//						try {
//							if (avatarFile.exists()) {
//								avatarFile.delete();
//							}
//							avatarFile.createNewFile();
//						} catch (Exception e) {
//							log.e(e);
//						}
//						FileOutputStream fileoutputstream = null;
//						try {
//							fileoutputstream = new FileOutputStream(avatarFile);
//						} catch (Exception e) {
//							log.e(e);
//						}
//						bitmap.compress(Bitmap.CompressFormat.PNG, 100,
//								fileoutputstream);
//						try {
//							fileoutputstream.flush();
//							fileoutputstream.close();
//						} catch (Exception exception1) {
//							log.e(exception1);
//						}
//						mHeaderIV.setImageBitmap(bitmap);
//						updateInfo(PICTURE, avatarFile.getAbsolutePath());
//					} else {
//						log.e("bitmap is null");
//					}
//				} else {
//					log.e("data is null");
//				}
//			} catch (Exception e) {
//				log.e(e);
//			}
			break;
		case EDIT_NICKNAME_SUCCESS:
			try {
				if (data != null) {
					String nickname = data.getStringExtra("nickname");
					mNickNameTV.setText(nickname);
				} else {
					log.e("data is null");
				}
			} catch (Exception e) {
				log.e(e);
			}
			break;
		case RESULT_FROM_LOCATION:
			try {
				if (data != null) {
					String location = data.getStringExtra("location");
					log.i("location = " + location);
					if (location != null) {
						updateInfo(AREAID, location);
					} else {
						log.e("location is null");
					}
				}
			} catch (Exception e) {
				log.e(e);
			}
			break;
		case RESULT_NICKNAME_CODE:// 修改昵称
			try {
				if (data != null) {
					String nickname = data.getStringExtra("nickname");
					mNickNameTV.setText(nickname);
					showToastMessage(getString(R.string.update_nickname_suc), 0);
				} else {
					log.e("data is null");
				}
			} catch (Exception e) {
				log.e(e);
			}
			break;
		case RESULT_SIGN_CODE:// 修改签名
			try {
				if (data != null) {
					String sign = data.getStringExtra("sign");
					mTagTV.setText(sign);
					showToastMessage(getString(R.string.update_sign_suc), 0);
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
			AvatarUtil.deleteTempFile();
//			if (null != bitmap) {
//				bitmap.recycle();
//				bitmap = null;
//			}
//			File mFile = new File(tempImageFilePath);
//			if (null != mFile && mFile.exists()) {
//				mFile.delete();
//			}
		} catch (Exception e) {
			log.e(e);
		}
		super.onDestroy();
	}

	private class AuthorizeListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
			log.d("weibo->onCancel");
		}

		@Override
		public void onComplete(Bundle arg0) {
			try {
				String token = arg0.getString(TOKEN);
				String expires_in = arg0.getString(EXPIRES_IN);
				String uid = arg0.getString(UID);
				snsUserLogin(SNS_SINA, token, uid, expires_in);
			} catch (Exception e) {
				log.e(e);
			}

		}

		@Override
		public void onError(WeiboDialogError arg0) {
			log.d("weibo->onError");
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			log.d("weibo->onWeiboException");
		}
	}

	private void saveToken(String token, String expires_in, String uid) {
		try {
			log.d("weibo->save info " + token + "____" + expires_in + "__"
					+ uid);
			SharedPreferenceUtil.saveSinaWeiboInfo(uid, token, expires_in);
		} catch (Exception e) {
			log.e(e);
		}
	}

	public boolean isBindSns() {
		return WeiboApi.getInstance().isBindSina();
	}

	public boolean isBindPhone() {
		return btnBindTelephone.getText().equals(
				getString(R.string.btn_bindtele_unbind)) ? false : true;
	}

	public boolean isBindQQ() {
		return false;
	}
	
	private void weiboLogOut(String snsToken) {
		//做登出操作
		WeiboParameters params = new WeiboParameters();
		params.add("access_token", snsToken);
		AsyncWeiboRunner.request("https://api.weibo.com/oauth2/revokeoauth2", params, "POST", new RequestListener() {
			
			@Override
			public void onIOException(IOException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(WeiboException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private SpannableStringBuilder getScoreDescribeStyle(int scores) {
		//填写个人信息获取积分的界面美化效果
		String scores2str = String.valueOf(scores);
		String content = getApplicationContext().getResources().getString(R.string.personal_info_can_getscore, scores2str );
		
		SpannableStringBuilder scorestyle=new SpannableStringBuilder(content);
		scorestyle.setSpan(new AbsoluteSizeSpan(12,true), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		scorestyle.setSpan(new ForegroundColorSpan(Color.parseColor("#7c7c7c")), 0, 15, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		scorestyle.setSpan(new AbsoluteSizeSpan(17,true), 15, 15 + scores2str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		scorestyle.setSpan(new StyleSpan(Typeface.BOLD), 15, 15 + scores2str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		scorestyle.setSpan(new ForegroundColorSpan(Color.parseColor("#ffb400")), 15, 15 + scores2str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		scorestyle.setSpan(new AbsoluteSizeSpan(12,true), 15 + scores2str.length(), content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		scorestyle.setSpan(new ForegroundColorSpan(Color.parseColor("#7c7c7c")), 15 + scores2str.length(), content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return scorestyle;
	}
}
