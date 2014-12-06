package com.cyou.mrd.pengyou.ui;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DynamicDao;
import com.cyou.mrd.pengyou.entity.DynamicInfoItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.SendDynamicThread;
import com.cyou.mrd.pengyou.utils.FileUtil;
import com.cyou.mrd.pengyou.utils.ImageUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.StringUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;

/**
 * 发表动态
 * 
 * @author wangkang 2013-8-15 Reconstruction tzh
 */
public class SendMyDynamicAcivity extends BaseActivity implements
		OnClickListener {
	private CYLog log = CYLog.getInstance();
	private EditText editShareGame;
	private TextView mImageAddText;
	private ImageView imgGameIcon;
	private ImageView mImageDelete;
	private Button mOkBtn;
	private ImageButton mBackBtn;
	private static final int RESULT_FROM_CAMERA = 1;
	private static final int RESULT_FROM_CONTENT = 2;
	private static final int RESULT_FROM_ENLARGE = 3;
	public String imagePath;
	public boolean hasPicture = false;
	public static final String TEXT = "text";
	public static final int PIC_MAXSIZE = 1024;
	private Dialog mSharedDialog;
	private boolean canAddPicture = true;
	private boolean takePicture = false;
	private DynamicDao mDao;
	private Integer imgHeigth;
	private Integer imgWidth;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.send_myact_layout);
		initView();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	protected void onResume() {
		if (mDao == null) {
			mDao = new DynamicDao(this);
		}
		super.onResume();
	}

	private void initView() {
		try {
			View headerBar = findViewById(R.id.add_friend_headerbar);
			mBackBtn = (ImageButton) headerBar.findViewById(R.id.sub_header_bar_left_ibtn);
			TextView mHeaderTV = (TextView) headerBar.findViewById(R.id.sub_header_bar_tv);
			mOkBtn = (Button) headerBar.findViewById(R.id.sub_header_bar_right_ibtn);
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mOkBtn.setText(R.string.btn_share);
			mOkBtn.setEnabled(false);
			editShareGame = (EditText) findViewById(R.id.edit_sharegame);
			imgGameIcon = (ImageView) findViewById(R.id.img_add_pic);
			mImageAddText = (TextView) findViewById(R.id.img_add_txt);
			mImageDelete = (ImageView) findViewById(R.id.img_delete_icon);
			// mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mHeaderTV.setText(R.string.publish_mydynamic);
			editShareGame.setFocusable(true);
			editShareGame.setHint(R.string.publish_hint_text);
			editShareGame.setHintTextColor(Color.parseColor("#c6c6c6"));
			editShareGame.addTextChangedListener(watcher);
			startInputMethod();
			mBackBtn.setOnClickListener(this);
			mOkBtn.setOnClickListener(this);
			imgGameIcon.setOnClickListener(this);
			mImageDelete.setOnClickListener(this);
			mOkBtn.setClickable(false);
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * Start inputmethod
	 */
	private void startInputMethod() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) SendMyDynamicAcivity.this
						.getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 100);

	}

	private void hideInputMethod() {
		try {
			InputMethodManager imm = (InputMethodManager) SendMyDynamicAcivity.this
					.getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getCurrentFocus()
					.getApplicationWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 监听输入事件
	 */
	TextWatcher watcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			temp = s;

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			temp = temp.toString().trim();
			if (temp.length() > 0) {
				updateButtonStatus(1);

			} else if (TextUtils.isEmpty(imagePath)) {
				updateButtonStatus(0);
			}

		}
	};

	/**
	 * 
	 * @param clickable
	 *            1： 可点击,0：不可点击
	 */
	public void updateButtonStatus(int clickable) {
		if (clickable == 1) {
			mOkBtn.setClickable(true);
			mOkBtn.setEnabled(true);
		} else {
			mOkBtn.setClickable(false);
			mOkBtn.setEnabled(false);
		}
	}

	public void showShareGameSelector() {
		try {
			mSharedDialog = new AlertDialog.Builder(SendMyDynamicAcivity.this)
					.setItems(
							new CharSequence[] {
									getString(R.string.from_camera),
									getString(R.string.from_album),
									getString(R.string.cancel) },
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									switch (which) {
									case 0:
										startCamera();
										break;
									case 1:
										selectPicture();
										break;
									case 3:
										mSharedDialog.dismiss();
										break;
									default:
										break;
									}
								}
							}).create();
			// mSharedDialog.setTitle(getString(R.string.share));
			mSharedDialog.setTitle(null);
			mSharedDialog.setCanceledOnTouchOutside(true);
			mSharedDialog.show();
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * show the bottom dialog
	 */
	// private void showBottomDialog() {
	// View view = getLayoutInflater().inflate(R.layout.item_dialog, null);
	// Button btnTakePicture = (Button)
	// view.findViewById(R.id.btn_take_picture);
	// Button btnSelectPicture = (Button)
	// view.findViewById(R.id.btn_select_picture);
	// Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
	// btnTakePicture.setOnClickListener(this);
	// btnSelectPicture.setOnClickListener(this);
	// btnCancel.setOnClickListener(this);
	//
	// mSharedDialog = new Dialog(this, R.style.transparentFrameWindowStyle);
	// mSharedDialog.setContentView(view,
	// new LayoutParams(getWindowManager().getDefaultDisplay()
	// .getWidth() - 10, LayoutParams.WRAP_CONTENT));
	// Window window = mSharedDialog.getWindow();
	// window.setWindowAnimations(R.style.main_menu_animstyle);
	// WindowManager.LayoutParams wl = window.getAttributes();
	// wl.x = 0;
	// wl.y = getWindowManager().getDefaultDisplay().getHeight();
	// mSharedDialog.onWindowAttributesChanged(wl);
	// mSharedDialog.setCanceledOnTouchOutside(true);
	// mSharedDialog.setCancelable(true);
	// mSharedDialog.show();
	// }

	/**
	 * start camera
	 */
	private void startCamera() {
		imagePath = SharedPreferenceUtil
				.getImgPath(CyouApplication.mAppContext)
				+ "/temp"
				+ UUID.randomUUID().toString().toString() + ".jpg";
		// 解决中星、htc手机拍照后 imagePath 为null ，先存起来
		SharedPreferences sp = CyouApplication.mAppContext
				.getSharedPreferences(Contants.SP_NAME.SETTING,
						Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putString(Params.SP_PARAMS.CAMERA_IMAGE_PATH, imagePath);
		e.putInt(Params.SP_PARAMS.CAMERA_IMAGE_WIDTH,
				imgGameIcon.getMeasuredWidth() - 10);
		e.putInt(Params.SP_PARAMS.CAMERA_IMAGE_HEIGHT,
				imgGameIcon.getMeasuredHeight() - 10);
		e.commit();
		Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File photo = new File(imagePath);
		if (photo != null && photo.exists()) {
			photo.delete();
		}
		Uri uri = Uri.fromFile(photo);
		iCamera.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION,
				Configuration.ORIENTATION_PORTRAIT);
		iCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// iCamera.putExtra("rotation", 90);
		iCamera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		startActivityForResult(iCamera, RESULT_FROM_CAMERA);
		mSharedDialog.dismiss();
	}

	/**
	 * start picture selector
	 */
	private void selectPicture() {
		takePicture = false;
		Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
		intent1.setType("image/*");
		startActivityForResult(intent1, RESULT_FROM_CONTENT);
		mSharedDialog.dismiss();
	}

	private void startEnlargeActivity() {
		Intent intent = new Intent(this, LargerImageViewActivity.class);
		CYLog.getInstance().d(imagePath);
		intent.putExtra("FILE_PATH", imagePath);
		startActivityForResult(intent, RESULT_FROM_ENLARGE);
	}

	/**
	 * delete the picture when the user click the delete button on the right
	 * corner of the picture
	 * 
	 */
	private void deletePicture() {
		imgGameIcon.setBackgroundDrawable(null);
		imgGameIcon.setImageResource(R.drawable.img_take_photo);
		mImageAddText.setVisibility(View.VISIBLE);
		mImageDelete.setVisibility(View.INVISIBLE);
		if (imagePath != null && !"".equals(imagePath)) {
			File file = new File(imagePath);
			// only can delete the user take the picture by the app just a
			// moment ago ,can not delete the user exists picture
			if (file.exists()
					&& imagePath.contains(SharedPreferenceUtil
							.getImgPath(CyouApplication.mAppContext))
					&& takePicture == true) {
				file.delete();
			}
		}
		String content = editShareGame.getText().toString().trim();
		if (content == null || "".equals(content)) {
			updateButtonStatus(0);
		}
		imagePath = "";
		takePicture = false;
		canAddPicture = true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		log.d("onActivityResult start......");
		if (mSharedDialog != null && mSharedDialog.isShowing()) {
			mSharedDialog.dismiss();
		}
		switch (requestCode) {
		// when clik the delete button on the largerImageView
		case RESULT_FROM_ENLARGE:
			if (resultCode == Activity.RESULT_OK) {
				deletePicture();
			}
			break;
		case RESULT_FROM_CONTENT:
			try {
				if (data != null) {
					imagePath = SharedPreferenceUtil
							.getImgPath(CyouApplication.mAppContext)
							+ "/temp"
							+ UUID.randomUUID().toString().toString() + ".jpg";
					log.i("SendMyDynamicAcivity imagePath:" + imagePath);
	                if(!isValidatePath()){
	                	return;
	                }
					Uri uri = data.getData();
					String[] filePathColumn = { MediaStore.Images.Media.DATA };
					Cursor cursor = getContentResolver().query(uri,
							filePathColumn, null, null, null);
					cursor.moveToFirst();
					int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					String tempFile = cursor.getString(columnIndex);
					final File mFile = new File(tempFile);
					cursor.close();
					if (null != mFile && mFile.exists()) {
						hasPicture = true;
						final long size = mFile.length() / 1024;
						log.d("image size = " + size);
						Bitmap mp;
						if (size < 50)
							mp = FileUtil.readBitMap(mFile);
						else
							// 压缩图片
//							mp = FileUtil.getBitmapFromFilePath(tempFile,
//									Config.IMAGE_MAX_WIDTH,
//									Config.IMAGE_MAX_HEIGHT);
						    mp = ImageUtil.getCompressedBitmapImage(mFile.getPath(), size);
						if (mp != null) {
							mp = FileUtil.setRotateBitmap(mp, imagePath);
						    imgHeigth = mp.getHeight();
							imgWidth = mp.getWidth();
							Bitmap bitmap = ThumbnailUtils.extractThumbnail(mp,
									imgGameIcon.getMeasuredWidth() - 10,
									imgGameIcon.getMeasuredHeight() - 10);
							imgGameIcon.setImageBitmap(bitmap);
							imgGameIcon.setBackgroundDrawable(null);
							imgGameIcon
									.setBackgroundResource(R.drawable.dynamic_small_photo_bg);
							mImageAddText.setVisibility(View.INVISIBLE);
							mImageDelete.setVisibility(View.VISIBLE);
							canAddPicture = false;
							updateButtonStatus(1);
						}
						ImageUtil.compressBitmapToFile(mp, imagePath, size);
//						new Thread(new  Runnable() {
//							
//							@Override
//							public void run() {
//								ImageUtil.updateImageFile(mFile.getPath(),imagePath, size);
//							}
//						}).start();
						// imagePath = mFile.getAbsolutePath();
					}
				}
			} catch (Exception e) {
				log.e(e);
			}
			break;
		case RESULT_FROM_CAMERA:
			try {
				Bitmap bitmap = null;
				File photo = null;
				SharedPreferences sp = CyouApplication.mAppContext
						.getSharedPreferences(Contants.SP_NAME.SETTING,
								Context.MODE_PRIVATE);
				imagePath = sp
						.getString(Params.SP_PARAMS.CAMERA_IMAGE_PATH, "");
				int width = sp.getInt(Params.SP_PARAMS.CAMERA_IMAGE_WIDTH, 100);
				int height = sp.getInt(Params.SP_PARAMS.CAMERA_IMAGE_HEIGHT,
						100);
				log.d("RESULT_FROM_CAMERA imagePath:" + imagePath);
				if(!isValidatePath()){
                	return;
                }
				if (imagePath != null && !"".equals(imagePath)) {
					photo = new File(imagePath);
				}
				if (photo != null && photo.exists()) {
					hasPicture = true;
					final long size = photo.length() / 1024;
					log.d("image size = " + size);
//					Bitmap mp = FileUtil.getBitmapFromFilePath(imagePath,
//							Config.IMAGE_MAX_WIDTH, Config.IMAGE_MAX_HEIGHT);
					Bitmap mp = ImageUtil.getCompressedBitmapImage(photo.getPath(), size);
					if (mp != null) {
						mp = FileUtil.setRotateBitmap(mp, imagePath);
						log.d("imgGameIcon" + imgGameIcon.getMeasuredWidth()
								+ "==" + imgGameIcon.getMeasuredHeight());
						imgHeigth = mp.getHeight();
						imgWidth = mp.getWidth();
						bitmap = ThumbnailUtils.extractThumbnail(mp, width,
								height);
						imgGameIcon.setImageBitmap(bitmap);
						imgGameIcon.setBackgroundDrawable(null);
						imgGameIcon
								.setBackgroundResource(R.drawable.dynamic_small_photo_bg);
						mImageAddText.setVisibility(View.INVISIBLE);
						mImageDelete.setVisibility(View.VISIBLE);
						takePicture = false;
						canAddPicture = false;
						takePicture = true;
						updateButtonStatus(1);
					}
					// 保存图片
					ImageUtil.compressBitmapToFile(mp, imagePath, size);
//					new Thread(new  Runnable() {
//						
//						@Override
//						public void run() {
//							ImageUtil.updateImageFile(imagePath,imagePath, size);
//						}
//					}).start();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			hideInputMethod();
			finish();
			break;
		case R.id.sub_header_bar_right_ibtn:
			try {
				if (isValidateContent()) {
					mOkBtn.setClickable(false);
					Integer pid = mDao.getMaxId();
					DynamicInfoItem item = new DynamicInfoItem();
					item.setContent(StringUtil.replaceBlank(editShareGame.getText().toString().trim()));
					item.setPicture(imagePath);
					item.setDate(System.currentTimeMillis() / 1000);
					item.setPid(pid);
					item.setUid(UserInfoUtil.getCurrentUserId());
					item.setStatus(Contants.SEND_DYNAMIC_STATUS.SENDING);
					item.setWidth(imgWidth);
					item.setHeight(imgHeigth);
					mDao.insert(item);
					SendDynamicThread task = new SendDynamicThread(CyouApplication.mAppContext, item);
					sendDynamicSendingBroadCast(pid);
					task.start();
					imagePath = "";
					editShareGame.clearComposingText();
					finish();
				}
			} catch (Exception e) {

			}
			break;
		case R.id.img_add_pic:
			if (canAddPicture == true) {
				// showBottomDialog();
				showShareGameSelector();
			} else {
				startEnlargeActivity();
			}
			break;
		case R.id.btn_take_picture:
			startCamera();
			break;
		case R.id.btn_select_picture:
			selectPicture();
			break;
		case R.id.btn_cancel:
			mSharedDialog.dismiss();
			break;
		case R.id.img_delete_icon:
			deletePicture();
			break;
		default:
			break;
		}
	}

	/**
	 * send the sendding broadcast to RefreshReceiver receiver to refresh the UI
	 * 
	 * @author tuozhonghua_zk
	 */
	private void sendDynamicSendingBroadCast(Integer pid) {
		Intent intent = new Intent(Contants.ACTION.SEND_DYNAMIC_SENDING);
		intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,pid);
		log.d("pid1=" + pid);
		sendBroadcast(intent);
	}

	/**
	 * verify the content
	 * 
	 * @return
	 * @author tuozhonghua_zk
	 */
	private boolean isValidateContent() {
		String content = editShareGame.getText().toString().trim();
		if (TextUtils.isEmpty(imagePath) && TextUtils.isEmpty(content)) {
			showToastMessage(getString(R.string.send_dynamic_empty), 0);
			return false;
		}
		return true;
	}

	private boolean isValidatePath() {
		if (Util.isExStorageAbl()) {
			float size = Util.getExtStorageAvailableSize();
			log.i("SendMyDynamicAcivity sdcard size:" + size);
			if (size <= 1f) {
				imagePath = "";
				showToastMessage("SD卡空间不足，请清理", 0);
				return false;
			}
		} else {
			imagePath = "";
			showToastMessage("SD卡不存在或无写权限，无法添加图片", 0);
			return false;
		}
		if (imagePath != null && !imagePath.contains("sdcard")) {
			String rootPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			String imgPath = rootPath + Contants.PATH.IMG_PATH;
			SharedPreferenceUtil.saveImgPath(getApplicationContext(), imgPath);
			File iPath = new File(imgPath);
			if (!iPath.exists()) {
				try {
					iPath.mkdirs();
					log.i("create dir:" + iPath.getAbsolutePath());
				} catch (Exception e) {
					imagePath = "";
					showToastMessage("创建文件异常", 0);
					return false;
				}
				imagePath = SharedPreferenceUtil
						.getImgPath(CyouApplication.mAppContext)
						+ "/temp"
						+ UUID.randomUUID().toString().toString() + ".jpg";
			}
		}

		boolean isCanPicker = true;
		// 保存图片
		File file = new File(imagePath);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				imagePath = "";
				isCanPicker = false;
			} finally {
				if (!isCanPicker) {
					imagePath = "";
					showToastMessage("创建文件异常", 0);
					return false;
				}
			}

		}

		return true;
	}

}
