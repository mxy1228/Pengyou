package com.cyou.mrd.pengyou.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.RequestParams;

/**
 * 修改签名*
 * 
 * @author wangkang
 * 
 */
public class EditSignAcivity extends BaseActivity {
	private final String SIGN = "signature";
	private CYLog log = CYLog.getInstance();
	private MyHttpConnect mConn;
	private EditText editSign;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.edit_personal_sign);
		initView();
	}

	UserInfo userInfo;

	/**
	 * 完善个人信息
	 */
	private void updateUserPwd() {
		if (isValidateUser()) {
			if (mConn == null) {
				mConn = MyHttpConnect.getInstance();
			}
			showLoadDoingProgressDialog();
			RequestParams params = new RequestParams();
			params.put(SIGN, editSign.getText().toString().trim());
			MyHttpConnect.getInstance().post2Json(HttpContants.NET.UPDATE_USER,
					params, new JSONAsyncHttpResponseHandler() {
						@Override
						public void onFailure(Throwable error, String content) {
							dismissProgressDialog();
							showToastMessage(
									getString(R.string.update_sign_error), 0);
							log.e("error is:" + content);
							
							super.onFailure(error, content);
						}
						@Override
						protected void onLoginOut() {
							dismissProgressDialog();
							LoginOutDialog dialog = new LoginOutDialog(
									EditSignAcivity.this);
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
										getString(R.string.update_sign_error),
										0);
								return;
							}
							log.d(" update userinfo result is:"
									+ response.toString());
							RootPojo rootPojo = JsonUtils.fromJson(
									response.toString(), RootPojo.class);
							if (null == rootPojo) {
								showToastMessage(
										getString(R.string.update_sign_error),
										0);
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
									SharedPreferenceUtil.upadateUserSign();
									userInfo.setSignature(editSign.getText()
											.toString().trim());
									UserInfoUtil.saveUserInfo(userInfo);
									Intent mIntent = new Intent();
									mIntent.putExtra("sign", editSign.getText()
											.toString().trim());
									setResult(
											EditInfoAcivity.RESULT_NICKNAME_CODE,
											mIntent);
									finish();
									return;
								}
								showToastMessage(
										getString(R.string.update_sign_error),
										0);

							} catch (Exception e) {
								log.e(e);
								showToastMessage(
										getString(R.string.update_sign_error),
										0);
								return;
							}

							super.onSuccess(statusCode, response);
						}

					});
		}
	}

	private boolean isValidateUser() {
		String nickName = editSign.getText().toString();
		if (nickName != null
				&& nickName.trim().length() > Config.USER_EDIT.USER_SIGN_MAX_COUNT) {
			showToastMessage(
					getString(R.string.user_sign_max_count, String
							.valueOf(Config.USER_EDIT.USER_SIGN_MAX_COUNT)),
					0);
			return false;
		}
		return true;
	}

	UserInfo playerinfo;

	class EditTextEnterFilter implements InputFilter {

		public EditTextEnterFilter() {
		}

		/**
		 * 
		 * @param source
		 *            当前输入的字符串（输入的字符串）
		 * @param start
		 *            输入字符串开始位置
		 * @param end
		 *            输入字符串结束位置
		 * @param dest
		 *            目标原字符串(输入框内的字符串)
		 * @param dstart
		 *            目标的开始位置（光标）
		 * @param dend
		 *            目标的结束位置（光标）
		 * @return
		 */
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dest, int dstart, int dend) {
			boolean bool = src.equals("\n");
			if (!bool) {
				// try {
				// // 转换成中文字符集的长度
				// int destLen = dest.toString().getBytes("GB18030").length;
				// int sourceLen = src.toString().getBytes("GB18030").length;
				// // 如果超过40个字符
				// if (destLen + sourceLen > 40) {
				// return "";
				// }
				// // 如果按回退键
				// if (src.length() < 1 && (dend - dstart >= 1)) {
				// return dest.subSequence(dstart, dend - 1);
				// }
				// // 其他情况直接返回输入的内容
				// return dest.subSequence(dstart, dstart) + src.toString();
				// } catch (UnsupportedEncodingException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }

				int destLen = dest.length(); // 获取字符个数(一个中文算2个字符)

				int sourceLen = src.length();

				if (destLen + sourceLen > Config.USER_EDIT.USER_SIGN_MAX_COUNT) {
					showToastMessage(
							getString(
									R.string.user_sign_max_count,
									String.valueOf(Config.USER_EDIT.USER_SIGN_MAX_COUNT)),
							0);
					return "";
				}
				return dest.subSequence(dstart, dstart) + src.toString();
			}
			return dest.subSequence(dstart, dend);
		}

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
			mOkBtn.setText(R.string.btn_finish);
			mOkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					updateUserPwd();
				}
			});
			mHeaderTV.setText(R.string.select_sign);
			editSign = (EditText) findViewById(R.id.edit_sign);
			userInfo = UserInfoUtil.getCurrentUserInfo();
			editSign.setText(userInfo.getSignature());
			editSign.setFilters(new InputFilter[] { new EditTextEnterFilter() });
			// editSign.addTextChangedListener(mTextWatcher);
		} catch (Exception e) { 
			log.e(e);
		}
	}
}
