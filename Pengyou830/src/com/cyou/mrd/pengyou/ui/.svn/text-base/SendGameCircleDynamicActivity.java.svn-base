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
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.GameDynamicDao;
import com.cyou.mrd.pengyou.entity.GameDynamicInfoItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.SendGameDynamicThread;
import com.cyou.mrd.pengyou.utils.FileUtil;
import com.cyou.mrd.pengyou.utils.ImageUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.StringUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;

/**
 * 游戏圈发布动态
 * 
 */
public class SendGameCircleDynamicActivity extends BaseActivity implements
		OnClickListener {
	private CYLog log = CYLog.getInstance();
	private EditText editShareGame;
	private TextView mImageAddText;
	private ImageView imgGameIcon;
	private ImageView mImageDelete;
	private ImageButton mBackBtn;
	private static final int RESULT_FROM_CAMERA = 1;
	private static final int RESULT_FROM_CONTENT = 2;
	private static final int RESULT_FROM_ENLARGE = 3;
	private static final int GAMe_SCORE_ZERO = 0;
	private static final int GAME_SCORE_TWO = 2;
	private static final int GAME_SCORE_FOUR = 4;
	public String imagePath;
	public boolean hasPicture = false;
	public static final String TEXT = "text";
	public static final int PIC_MAXSIZE = 1024;
	private Dialog mSharedDialog;
	private boolean canAddPicture = true;
	private boolean takePicture = false;
	private GameDynamicDao mDao;
	private RatingBar mRatingBar;
	private String mGamecode;
	private String mGcid; // 游戏圈id
	// 评分文字显示
	private TextView mTextView;
	private String gameName;
	private LinearLayout releaseGameScoreLayout;
	private boolean gameIsInstalled = false;
    private Button mOkBtn;
	private boolean isDisplayStar = true;
	InputMethodManager imm;
	private Integer imgWidth;
	private Integer imgHeight;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.send_gamecircle_dynamic_layout);
		mGamecode = getIntent().getStringExtra(Params.INTRO.GAME_CODE);
		mGcid = getIntent().getStringExtra(Params.Dynamic.GAME_CIRCLE_ID);
		gameName = getIntent().getStringExtra(Params.INTRO.GAME_NAME);
		gameIsInstalled = getIntent().getBooleanExtra(Params.INTRO.GAME_ISINSTALLED, false);
		if(UserInfoUtil.getGameScore(mGamecode) != 0){
			isDisplayStar = false;
		}
		imm = (InputMethodManager) SendGameCircleDynamicActivity.this
					.getSystemService(INPUT_METHOD_SERVICE);
		mDao = new GameDynamicDao(this);
		initView();
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
	}

	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		try {
			View headerBar = findViewById(R.id.add_friend_headerbar);
			mBackBtn = (ImageButton) headerBar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			TextView mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			mOkBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			editShareGame = (EditText) findViewById(R.id.edit_sharegame);
			imgGameIcon = (ImageView) findViewById(R.id.img_add_pic);
			mImageAddText = (TextView) findViewById(R.id.img_add_txt);
			mImageDelete = (ImageView) findViewById(R.id.img_delete_icon);
			mTextView = (TextView) findViewById(R.id.intro_rating_hint_tv);
			mTextView.setText(getString(R.string.rating_hint, gameName));
			mRatingBar = (RatingBar) findViewById(R.id.intro_rating_rb);
			releaseGameScoreLayout = (LinearLayout) findViewById(R.id.release_gamescore_layout);
//			if (gameIsInstalled && mScore <= 0 && imm.isActive()) {
//				releaseGameScoreLayout.setVisibility(View.VISIBLE);
//			} else {
//				releaseGameScoreLayout.setVisibility(View.GONE);
//			}
			if(!isDisplayStar){
				releaseGameScoreLayout.setVisibility(View.GONE);
				startInputMethod();
			}else if(gameIsInstalled){
				releaseGameScoreLayout.setVisibility(View.VISIBLE);
			}else{
				releaseGameScoreLayout.setVisibility(View.GONE);
				startInputMethod();
			}
			mRatingBar
					.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

						@Override
						public void onRatingChanged(RatingBar ratingBar,
								float rating, boolean fromUser) {
							if(rating <= 0.25){
								mRatingBar.setRating(0f);
							}
							else if(rating <= 0.5){
								mRatingBar.setRating(0.5f);
							}
							else if(rating <= 1f){
								mRatingBar.setRating(1f);
							}
							else if(rating <= 1.5f){
								mRatingBar.setRating(1.5f);
							}
							else if(rating <= 2f){
								mRatingBar.setRating(2f);
							}
							else if(rating <= 2.5f){
								mRatingBar.setRating(2.5f);
							}
							else if(rating <= 3f){
								mRatingBar.setRating(3f);
							}
							else if(rating <= 3.5f){
								mRatingBar.setRating(3.5f);
							}
							else if(rating <= 4f){
								mRatingBar.setRating(4f);
							}
							else if(rating <= 4.5f){
								mRatingBar.setRating(4.5f);
							}
							else if(rating <= 5f){
								mRatingBar.setRating(5f);
							}
							
							scoreTextDisplay(rating);
							updateButtonStatus(1);

						}
					});
			// 发布按钮
			mOkBtn.setText(R.string.btn_share);
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			updateButtonStatus(0);
			editShareGame.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (!TextUtils.isEmpty(editShareGame.getText().toString()
							.trim())) {
						updateButtonStatus(1);
					} else {
						updateButtonStatus(0);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});
			mHeaderTV.setText(R.string.publish_mydynamic);
			editShareGame.setFocusable(true);
			mBackBtn.setOnClickListener(this);
			mOkBtn.setOnClickListener(this);
			imgGameIcon.setOnClickListener(this);
			mImageDelete.setOnClickListener(this);
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * Start inputmethod
	 */
	private void startInputMethod() {
		try {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {				
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}, 200);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * hide inputmethod
	 */
	private void hideInputMethod() {
		try {
		   if (imm.isActive()) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
		   }
		}
		 catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void showShareGameSelector() {
		try {
			mSharedDialog = new AlertDialog.Builder(
					SendGameCircleDynamicActivity.this).setItems(
					new CharSequence[] { getString(R.string.from_camera),
							getString(R.string.from_album),
							getString(R.string.cancel) },
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
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
		//iCamera.putExtra("rotation", 90);
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
						if(size < 50)
							mp = FileUtil.readBitMap(mFile);
						else
//						    mp = FileUtil.getBitmapFromFilePath(tempFile,
//								480, 800);
							mp = ImageUtil.getCompressedBitmapImage(mFile.getPath(), size);
						if (mp != null) {
							mp = FileUtil.setRotateBitmap(mp, imagePath);
							imgHeight = mp.getHeight();
							imgWidth  = mp.getWidth();
							Bitmap bitmap = ThumbnailUtils.extractThumbnail(mp,
									imgGameIcon.getMeasuredWidth() - 10,
									imgGameIcon.getMeasuredHeight() - 10);
							// Bitmap bitmap = FileUtil.getBitmapFromFile(mFile,
							// imgGameIcon.getMeasuredWidth(),
							// imgGameIcon.getMeasuredHeight());
							imgGameIcon.setImageBitmap(bitmap);
							imgGameIcon.setBackgroundDrawable(null);
							imgGameIcon
									.setBackgroundResource(R.drawable.dynamic_small_photo_bg);
							mImageAddText.setVisibility(View.INVISIBLE);
							mImageDelete.setVisibility(View.VISIBLE);
							canAddPicture = false;
//							imagePath = mFile.getAbsolutePath();
							updateButtonStatus(1);
						}
						// 保存图片
						ImageUtil.compressBitmapToFile(mp, imagePath, size);
//						if(size < 100){
//							ImageUtil.saveImage(mp, imagePath);
//						}
//						else
//						new Thread(new  Runnable() {
//							
//							@Override
//							public void run() {
//								ImageUtil.updateImageFile(mFile.getPath(),imagePath, size);
//							}
//						}).start();
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
//					Bitmap mp = FileUtil.getBitmapFromFilePath(imagePath, 480,
//							800);
					Bitmap mp = ImageUtil.getCompressedBitmapImage(photo.getPath(), size);
					if (mp != null) {
						mp = FileUtil.setRotateBitmap(mp, imagePath);
						imgHeight = mp.getHeight();
						imgWidth  = mp.getWidth();
						bitmap = ThumbnailUtils.extractThumbnail(mp,
								imgGameIcon.getMeasuredWidth() - 10,
								imgGameIcon.getMeasuredHeight() - 10);
						log.i("@@@" + bitmap.getHeight() + "=="
								+ bitmap.getWidth());
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
//					ImageUtil.saveImage(mp, imagePath);
//                    new Thread(new  Runnable() {
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
					GameDynamicInfoItem item = new GameDynamicInfoItem();
					String  sendString = StringUtil.replaceBlank(editShareGame
							.getText().toString().trim());
					item.setContent(sendString);
					item.setPicture(imagePath);
					item.setHeight(imgHeight);
					item.setWidth(imgWidth);
					item.setDate(System.currentTimeMillis() / 1000);
					item.setPid(pid);
					item.setStatus(Contants.SEND_DYNAMIC_STATUS.SENDING);
					item.setGameCircleId(mGcid);
					item.setUid(UserInfoUtil.getCurrentUserId());
					float  score = mRatingBar.getRating();
					log.i("mRatingBar.getRating()="+ score);
					item.setScore(score);
					item.setGamecode(mGamecode);
					log.i(" save in table gamecode="+ mGamecode);
					mDao.insert(item);
					SendGameDynamicThread task = new SendGameDynamicThread(
							CyouApplication.mAppContext, item, mGamecode, mGcid,
							score);
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
		case R.id.intro_rating_rb:
			scoreTextDisplay(mRatingBar.getRating());
			break;
		default:
			break;
		}
	}

	// 评分界面文字动态显示
	private void scoreTextDisplay(float score) {
		if(score<=GAMe_SCORE_ZERO){
			mTextView.setText(getString(R.string.rating_hint, gameName));
		}else if (score < GAME_SCORE_TWO) {
			mTextView.setText(R.string.gamescore_poor_comment);
		} else if (score < GAME_SCORE_FOUR) {
			mTextView.setText(R.string.gamescore_gengeral_comment);
		} else {
			mTextView.setText(R.string.gamescore_good_comment);
		}

	}
    //更新发布按钮状态
	public void updateButtonStatus(int clickable) {
		if (clickable == 1) {
			mOkBtn.setClickable(true);
			mOkBtn.setEnabled(true);
		} else {
			mOkBtn.setClickable(false);
			mOkBtn.setEnabled(false);
		}
	}
	
	/**
	 * send the sendding broadcast to RefreshReceiver receiver to refresh the UI
	 * 
	 * @author tuozhonghua_zk
	 */
	private void sendDynamicSendingBroadCast(Integer pid) {
		Intent intent = new Intent(Contants.ACTION.SEND_GAMEDYNAMIC_SENDING);
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
		if (TextUtils.isEmpty(imagePath) && TextUtils.isEmpty(content)
				&& mRatingBar.getRating() <= 0) {
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
