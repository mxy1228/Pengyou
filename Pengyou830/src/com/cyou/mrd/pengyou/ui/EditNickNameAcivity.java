package com.cyou.mrd.pengyou.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.NameLengthFilter;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.RequestParams;

/**
 * 修改昵称
 * 
 * @author wangkang
 * 
 */
public class EditNickNameAcivity extends BaseActivity {
	private final String NICK_NAME = "nickname";
	private static final int NICKNAME_MAX_NUM = 16;// 昵称字数最大值
	private CYLog log = CYLog.getInstance();
	private MyHttpConnect mConn;
	private EditText editNickName;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edit_personal_nickname);
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
			final Button mOkBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mOkBtn.setText(R.string.btn_finish);
			mOkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					updateNickName();
				}
			});
			mHeaderTV.setText(R.string.edit_nickname_title);
			editNickName = (EditText) findViewById(R.id.edit_nickname);
	        InputFilter[] filters = { new NameLengthFilter(16) };
	        editNickName.setFilters(filters);
			userInfo = UserInfoUtil.getCurrentUserInfo();
			editNickName.setText(userInfo.getNickname());

//			editNickName.addTextChangedListener(new TextWatcher() {
//
//				@Override
//				public void onTextChanged(CharSequence s, int start,
//						int before, int count) {
//
//				}
//
//				@Override
//				public void beforeTextChanged(CharSequence s, int start,
//						int count, int after) {
//					
//				}
//
//				@Override
//				public void afterTextChanged(Editable s) {
//					if (!TextUtils.isEmpty(editNickName.getText().toString()
//							.trim())) {
//						mOkBtn.setClickable(true);
//						mOkBtn.setEnabled(true);
//					} else {
//						mOkBtn.setEnabled(false);
//						mOkBtn.setClickable(false);
//					}
//				}
//			});
		} catch (Exception e) {
			log.e(e);
		}
	}

	UserInfo userInfo;

	/**
	 * 完善个人信息
	 */
	private void updateNickName() {
		if (isValidateUser()) {
			if (mConn == null) {
				mConn = MyHttpConnect.getInstance();
			}
			showLoadDoingProgressDialog();
			RequestParams params = new RequestParams();
			params.put(NICK_NAME, editNickName.getText().toString().trim());
			MyHttpConnect.getInstance().post2Json(HttpContants.NET.UPDATE_USER,
					params, new JSONAsyncHttpResponseHandler() {
						@Override
						public void onFailure(Throwable error, String content) {
							dismissProgressDialog();
							showToastMessage(
									getString(R.string.modify_nickname_failed),
									0);
							log.e("error is:" + content);
							super.onFailure(error, content);
						}
						@Override
						protected void onLoginOut() {
							dismissProgressDialog();
							LoginOutDialog dialog = new LoginOutDialog(
									EditNickNameAcivity.this);
							dialog.create().show();
							super.onLoginOut();
						}
						@Override
						public void onSuccess(int statusCode,
								JSONObject response) {
							dismissProgressDialog();
							super.onSuccess(statusCode, response);

							if (null == response) {
								showToastMessage(
										getString(R.string.modify_nickname_failed),
										0);
								return;
							}
							log.d(" update userinfo result is:"
									+ response.toString());
							RootPojo rootPojo = JsonUtils.fromJson(
									response.toString(), RootPojo.class);
							if (null == rootPojo) {
								showToastMessage(
										getString(R.string.modify_nickname_failed),
										0);
								return;
							}
							if (String.valueOf(Contants.ERROR_NO.ERROR_4)
									.equals(rootPojo.getErrorNo())) {
								showToastMessage(
										getString(R.string.nickname_repeat), 0);
								editNickName.selectAll();
								return;
							}
							if (!TextUtils.isEmpty(rootPojo.getErrorNo())
									&& String.valueOf(
											Contants.ERROR_NO.ERROR_MASK_WORD_STRING)
											.equals(rootPojo.getErrorNo())) {
								showToastMessage(
										getString(R.string.modify_maskword_failed),
										0);
								return;
							}
							try {
								String successful = rootPojo.getSuccessful();
								if ("1".equals(successful)) {
									userInfo = UserInfoUtil
											.getCurrentUserInfo();
									userInfo.setNickname(editNickName.getText()
											.toString().trim());
									UserInfoUtil.saveUserInfo(userInfo);
									Intent mIntent = new Intent();
									mIntent.putExtra("nickname", editNickName
											.getText().toString().trim());
									setResult(
											EditInfoAcivity.RESULT_NICKNAME_CODE,
											mIntent);
									finish();
									return;
								}
								showToastMessage(
										getString(R.string.modify_nickname_failed),
										0);

							} catch (Exception e) {
								log.e(e);
								showToastMessage(
										getString(R.string.modify_nickname_failed),
										0);
								return;
							}

						}

					});
		}
	}

	private boolean isValidateUser() {
		String nickName = editNickName.getText().toString();
		if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(nickName.trim())) {
			showToastMessage(getString(R.string.please_input_nickname), 0);
			return false;
		}
		if (Util.getLength(nickName) > NICKNAME_MAX_NUM) {
			showToastMessage(getString(R.string.nickname_too_long), 0);
			return false;
		}
		return true;
	}

	UserInfo playerinfo;

}
