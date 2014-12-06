package com.cyou.mrd.pengyou.ui;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 分享到关系圈
 * 
 * @author wangkang
 * 
 */
public class ShareGameToFZoneAcivity extends BaseActivity {
	private CYLog log = CYLog.getInstance();
	private MyHttpConnect mConn;
	private EditText editShareGame;
	private String gameCode;
	private String gameName;
	private ImageView imgGameIcon;
	private DisplayImageOptions mOptions;
	private String gameIconUrl;
	private int mSending = 0;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.share_game_fzone);
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_default)
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15))
				.resetViewBeforeLoading().build();
		initView();
	}

	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		try {
			View headerBar = findViewById(R.id.add_friend_headerbar);
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
			Button mOkBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mOkBtn.setText(R.string.share);
			mOkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(mSending == 0 || mSending == 2){
						if(waitingDialog != null){
							waitingDialog.setCanceledOnTouchOutside(false);
							showProgressDialog(getString(R.string.sharegame_rc_operating));
						}
					    shareGame2FZone();
					}
					else{
						if(waitingDialog != null && !waitingDialog.isShowing()){
							showProgressDialog(getString(R.string.sharegame_rc_operating));
						}
					}
//						showToastMessage(getString(R.string.sharegame_rc_operating), 0);
				}
			});
			mHeaderTV.setText(R.string.share_game_fzone);
			editShareGame = (EditText) findViewById(R.id.edit_sharegame);
			gameName = getIntent().getStringExtra(Params.GAME_INFO.GAME_NAME);
			gameCode = getIntent().getStringExtra(Params.GAME_INFO.GAME_CODE);
			gameIconUrl = getIntent()
					.getStringExtra(Params.GAME_INFO.GAME_ICON);
			if (TextUtils.isEmpty(gameName) || TextUtils.isEmpty(gameCode)) {
				log.e(" game code is null");
				showToastMessage(getString(R.string.loadgame_error), 0);
				finish();
				return;
			}
			String defauleText = getString(R.string.share_game_fzone_text,
					gameName);
			editShareGame.setText(defauleText);
			editShareGame.setSelection(defauleText.length());
			imgGameIcon = (ImageView) findViewById(R.id.img_gameicon);
			if (!TextUtils.isEmpty(gameIconUrl)) {
				CYImageLoader.displayIconImage(gameIconUrl, imgGameIcon,
						mOptions);
			}
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * 分享游戏到关系圈
	 */
	private void shareGame2FZone() {
		if (isValidateText()) {
			mSending = 1;
			if (mConn == null) {
				mConn = MyHttpConnect.getInstance();
			}
			RequestParams params = new RequestParams();
			params.put("text", editShareGame.getText().toString().trim());
			params.put("gamecode", gameCode);
			MyHttpConnect.getInstance().post2Json(
					HttpContants.NET.SHARE_GAME_FZONE, params,
					new JSONAsyncHttpResponseHandler() {
						@Override
						public void onFailure(Throwable error, String content) {
							mSending = 2;
							showToastMessage(getString(R.string.sharegame_rc_error), 0);
							log.e("error is:" + content);
							dismissProgressDialog();
							super.onFailure(error, content);
						}
						@Override
						protected void onLoginOut() {
							mSending = 2;
							dismissProgressDialog();
							LoginOutDialog dialog = new LoginOutDialog(
									ShareGameToFZoneAcivity.this);
							dialog.create().show();
							super.onLoginOut();
						}
						@Override
						public void onSuccess(int statusCode,
								JSONObject response) {
							dismissProgressDialog();
							if (null == response) {
								mSending = 2;
								showToastMessage(getString(R.string.sharegame_rc_error), 0);
								return;
							}
							log.d(" update userinfo result is:"
									+ response.toString());
							RootPojo rootPojo = JsonUtils.fromJson(
									response.toString(), RootPojo.class);
							if (null == rootPojo) {
								mSending = 2;
								showToastMessage(getString(R.string.sharegame_rc_error), 0);
								return;
							}
							try {
								String successful = rootPojo.getSuccessful();
								if ("1".equals(successful)) {
									showToastMessage(getString(R.string.sharegame_rc_success), 0);
									mSending = 3;
									finish();
									return;
								}
								else{
									String errorNo = rootPojo.getErrorNo();
	        						if (!TextUtils.isEmpty(errorNo)
	        								&& Contants.ERROR_NO.ERROR_MASK_WORD_STRING
	        										.equals(errorNo)) {  
	        							showToastMessage(getString(R.string.publish_maskword_failed), 0);
	        						} else {
	        							showToastMessage(getString(R.string.sharegame_rc_error), 0);
	        						}
	        						mSending = 2;
								}
	
							} catch (Exception e) {
								log.e(e);
								showToastMessage(getString(R.string.sharegame_rc_error), 0);
								mSending = 2;
								return;
							}

							super.onSuccess(statusCode, response);
						}

					});
		}
	}

	private boolean isValidateText() {
		String nickName = editShareGame.getText().toString();
		if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(nickName.trim())) {
			showToastMessage(getString(R.string.sharegame_rc_nocontent), 0);
			return false;
		}
		return true;
	}

	UserInfo playerinfo;

}
