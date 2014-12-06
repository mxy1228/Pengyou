package com.cyou.mrd.pengyou.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.RecommendFriendItem;
import com.cyou.mrd.pengyou.entity.base.RecommendFriendBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.service.LaunchService;
import com.cyou.mrd.pengyou.utils.AvatarUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.RoundImageView;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CompleteRegistActivity extends CYBaseActivity implements OnClickListener
,android.content.DialogInterface.OnClickListener{

	private static final String GENDER = "gender";
	private static final String PICTURE = "picture";
	private static final String NICKNAME = "nickname";
	private static final String BOY = "1";
	private static final String GIRL = "2";
	private static final int NICKNAME_MAX_NUM = 16;
	public static final int COMPLETE_GUIDER = 10;
	
	private RoundImageView mAvatarIBtn;
	private EditText mNicknameET;
	private Button mCompleteBtn;
	private Dialog mAvatarDialog;
	private LinearLayout mMaleLL;
	private LinearLayout mFemaleLL;
	private WaitingDialog mWaitingDialog;
	
	private Map<String,String> mUserInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_regist);
		initView();
		initEvent();
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AvatarUtil.deleteTempFile();
	}
	
	@Override
	protected void initView() {
		this.mAvatarIBtn = (RoundImageView)findViewById(R.id.complete_regist_avatar_ibtn);
		this.mNicknameET = (EditText)findViewById(R.id.complete_regist_nickname_et);
		this.mCompleteBtn = (Button)findViewById(R.id.complete_regist_header_complete_btn);
		this.mAvatarDialog = new AlertDialog.Builder(this).setItems(new CharSequence[]{getString(R.string.from_camera)
				,getString(R.string.from_album)
				,getString(R.string.cancel)}, this).create();
		this.mMaleLL = (LinearLayout)findViewById(R.id.complete_regist_male_ll);
		this.mFemaleLL = (LinearLayout)findViewById(R.id.complete_regist_female_ll);
		this.mWaitingDialog = new WaitingDialog(this);
	}

	@Override
	protected void initEvent() {
		this.mAvatarIBtn.setOnClickListener(this);
		this.mMaleLL.setOnClickListener(this);
		this.mFemaleLL.setOnClickListener(this);
		this.mCompleteBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		this.mUserInfo = new HashMap<String, String>();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.complete_regist_avatar_ibtn:
			mAvatarDialog.show();
			break;
		case R.id.complete_regist_header_complete_btn:
			try {
				String nickname = mNicknameET.getText().toString().trim();
				if(nickname != null && verifyNickname(nickname)){
					if(!TextUtils.isEmpty(nickname)){
						mUserInfo.put(NICKNAME, nickname);
					}
				}else{
					break;
				}
				mUserInfo.put(PICTURE, new File(UserInfoUtil.getUserIconPath()).getAbsolutePath());
				uploadUserInfo();
			} catch (Exception e) {
				log.e(e);
			}
			break;
		case R.id.complete_regist_male_ll:
			mMaleLL.setBackgroundColor(Color.parseColor("#b5d8fd"));
			mFemaleLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.complete_regist_female_normal_bg));
			mUserInfo.put(GENDER, BOY);
			break;
		case R.id.complete_regist_female_ll:
			mMaleLL.setBackgroundColor(getResources().getColor(R.color.white));
			mFemaleLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.complete_regist_female_press_bg));
			mUserInfo.put(GENDER, GIRL);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		dialog.dismiss();
		switch (which) {
			//拍照
		case 0:
			AvatarUtil.startCamera(CompleteRegistActivity.this);
			break;
			//相册
		case 1:
			AvatarUtil.startAlbums(CompleteRegistActivity.this);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case AvatarUtil.RESULT_FROM_ALBUMS:
			AvatarUtil.resultFromAlbums(CompleteRegistActivity.this, resultCode, data);
			break;
		case AvatarUtil.RESULT_FROM_CAMERA:
			AvatarUtil.resultFromCamera(CompleteRegistActivity.this, resultCode);
			break;
		case AvatarUtil.RESULT_FROM_CROP:
			Bitmap bitmap = AvatarUtil.resultFromCrop(CompleteRegistActivity.this, resultCode, data);
			if(bitmap == null){
				return;
			}
			mAvatarIBtn.setImageBitmap(bitmap);
			break;
		case COMPLETE_GUIDER:
			CompleteRegistActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 验证昵称合法性
	 * xumengyang
	 */
	private boolean verifyNickname(String nickname){
		if(TextUtils.isEmpty(nickname)){
			return true;
		}else if(Util.getLength(nickname) > NICKNAME_MAX_NUM){
			showShortToast(R.string.nickname_too_long);
			return false;
		}else{
			return true;
		}
	}
	
	private void uploadUserInfo(){
		RequestParams params = new RequestParams();
		for(Entry<String, String> e : mUserInfo.entrySet()){
			if(e.getKey().equals(PICTURE)){
				try {
					params.put(PICTURE, new File(e.getValue()));
				} catch (Exception e2) {
					log.e(e2);
				}
			}else{
				params.put(e.getKey(), e.getValue());
			}
		}
		MyHttpConnect.getInstance().post(HttpContants.NET.UPDATE_USER,params,new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				showWaitingDialog();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				dismissWaitingDialog();
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(CompleteRegistActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(TextUtils.isEmpty(content)){
					return;
				}
				log.d("完善个人信息result = "+content);
				try {
					JSONObject obj = new JSONObject(content);
					if(obj.has("errorNo") && obj.getInt("errorNo") == 105){
						dismissWaitingDialog();
						showShortToast(R.string.nickname_repeat);
						return;
					}
					if(obj.has("successful") && obj.getString("successful").equals("1")){
						if(obj.has("data")){
							JSONObject data = obj.getJSONObject("data");
							String picture = data.getString("picture");
							String pictureorig = data.getString("pictureorig");
							UserInfoUtil.saveUserPicture(CompleteRegistActivity.this, picture,pictureorig);
						}
						if(mUserInfo.containsKey(GENDER)){
							UserInfoUtil.setGender(Integer.parseInt(mUserInfo.get(GENDER)));
						}
						if(mUserInfo.containsKey(NICKNAME)){
							UserInfoUtil.setNickName(mUserInfo.get(NICKNAME));
						}
						close();
//						Intent intent = new Intent(CompleteRegistActivity.this,GuiderGuessActivity.class);
//						startActivity(intent);
					}else{
						showShortToast(R.string.modify_failed);
					}
					dismissWaitingDialog();
				} catch (Exception e) {
					log.e(e);
				}
			}
		});
	}
	

	private void close(){
		request();
	}
	
	/**
	 * 向服务器请求猜你认识数据
	 * xumengyang
	 */
	private void request(){
		MyHttpConnect.getInstance().post(HttpContants.NET.SAME_GAME_FRIENDS, new RequestParams(), new AsyncHttpResponseHandler(){
			
			@Override
			public void onSuccess(int statusCode, String content) {
				mWaitingDialog.dismiss();
				super.onSuccess(statusCode, content);
				log.d("玩同款游戏猜你认识="+content);
				try {
					RecommendFriendBase base = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.readValue(content, new TypeReference<RecommendFriendBase>() {
							});
					if(base != null){
						ArrayList<RecommendFriendItem> list = base.getData();
						if(list != null && !list.isEmpty()){
							Bundle b = new Bundle();
							b.putParcelableArrayList("list", list);
							Intent intent = new Intent(CompleteRegistActivity.this,GuiderGuessActivity.class);
							intent.putExtras(b);
							startActivityForResult(intent, COMPLETE_GUIDER);
						}else{
							Intent intent = new Intent(CompleteRegistActivity.this,LaunchService.class);
							startService(intent);
							Intent mIntent = new Intent(CompleteRegistActivity.this,MainActivity.class);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(mIntent);
							CompleteRegistActivity.this.finish();
						}
					}else{
						log.e("base is null");
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onStart() {
				mWaitingDialog.show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				mWaitingDialog.dismiss();
			}
		});
			
	}
	
	
}
