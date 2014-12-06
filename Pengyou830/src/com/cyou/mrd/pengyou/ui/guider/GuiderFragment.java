package com.cyou.mrd.pengyou.ui.guider;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.service.LaunchService;
import com.cyou.mrd.pengyou.ui.CYBaseFragment;
import com.cyou.mrd.pengyou.ui.CompleteRegistActivity;
import com.cyou.mrd.pengyou.ui.MainActivity;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GuiderFragment extends CYBaseFragment implements AnimationListener,OnClickListener{
	private static final String NEW_USER = "1";
	
	private ImageView mIV;
	private ImageView mMirrorIV;
	private TextView mTV1;
	private TextView mTV2;
	private ImageButton mGuiderBtn;
	private WaitingDialog mWaitingDialog;
	
	private int mNum;
	private boolean mAnimHadEnd = false;
	
	private AlphaAnimation mIVAnim;
	private ScaleAnimation mMirrorAnim;
	private AnimationSet mTVAnimSet;
	private TranslateAnimation mTVTransAnim;
	private AlphaAnimation mTVAlphaAnim;
	private AlphaAnimation mFadingInAnim;
	private TranslateAnimation mTransInAnim;
	private AnimationSet mAnimInSet;
	private AnimationSet mTV2AnimSet;
	private TranslateAnimation mTV2TransAnim;
	private AlphaAnimation mTV2AlphaAnim;
	
	static GuiderFragment newInstance(int num){
		GuiderFragment f = new GuiderFragment();
		Bundle b = new Bundle();
		b.putInt("num", num);
		f.setArguments(b);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle b = getArguments();
		View view = null;
		mNum = b.getInt("num");
		switch (mNum) {
		case 0:
			view = inflater.inflate(R.layout.guider1, null);
			mIV = (ImageView)view.findViewById(R.id.guider1_iv);
			mMirrorIV = (ImageView)view.findViewById(R.id.guider1_mirror_iv);
			mTV1 = (TextView)view.findViewById(R.id.guider1_tv1);
			mTV2 = (TextView)view.findViewById(R.id.guider1_tv2);
			break;
		case 1:
			view = inflater.inflate(R.layout.guider2, null);
			mIV = (ImageView)view.findViewById(R.id.guider2_iv);
			mMirrorIV = (ImageView)view.findViewById(R.id.guider2_mirror_iv);
			mTV1 = (TextView)view.findViewById(R.id.guider2_tv1);
			mTV2 = (TextView)view.findViewById(R.id.guider2_tv2);
			break;
		case 2:
			view = inflater.inflate(R.layout.guider3, null);
			mIV = (ImageView)view.findViewById(R.id.guider3_iv);
			mMirrorIV = (ImageView)view.findViewById(R.id.guider3_mirror_iv);
			mTV1 = (TextView)view.findViewById(R.id.guider3_tv1);
			mTV2 = (TextView)view.findViewById(R.id.guider3_tv2);
			break;
		case 3:
			view = inflater.inflate(R.layout.guider4, null);
			mIV = (ImageView)view.findViewById(R.id.guider4_iv);
			mMirrorIV = (ImageView)view.findViewById(R.id.guider4_mirror_iv);
			mTV1 = (TextView)view.findViewById(R.id.guider4_tv1);
			mTV2 = (TextView)view.findViewById(R.id.guider4_tv2);
			mGuiderBtn = (ImageButton)view.findViewById(R.id.guider_btn);
			mWaitingDialog = new WaitingDialog(getActivity());
			break;
		default:
			view = inflater.inflate(R.layout.guider5, null);
			mIV = (ImageView)view.findViewById(R.id.guider5_iv);
			mMirrorIV = (ImageView)view.findViewById(R.id.guider5_mirror_iv);
			mTV1 = (TextView)view.findViewById(R.id.guider5_tv1);
			mTV2 = (TextView)view.findViewById(R.id.guider5_tv2);
			mGuiderBtn = (ImageButton)view.findViewById(R.id.guider_btn);
			mWaitingDialog = new WaitingDialog(getActivity());
			break;
		}
		initData();
		initEvent();
		return view;
	}
	
	@Override
	protected void initEvent() {
		mIVAnim.setAnimationListener(this);
		mMirrorAnim.setAnimationListener(this);
		mTVAnimSet.setAnimationListener(this);
		mTV2AnimSet.setAnimationListener(this);
		if(mGuiderBtn != null){
			mGuiderBtn.setOnClickListener(this);
		}
	}

	@Override
	protected void initData() {
		mIVAnim = new AlphaAnimation(0, 1);
		mIVAnim.setDuration(700);
		mIVAnim.setFillAfter(true);
		mIVAnim.setAnimationListener(this);
		mMirrorAnim = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mMirrorAnim.setDuration(200);
		mMirrorAnim.setFillAfter(true);
		mTVTransAnim = new TranslateAnimation(-30, 0, 0, 0);
		mTVAlphaAnim = new AlphaAnimation(0, 1);
		mTVAnimSet = new AnimationSet(false);
		mTVAnimSet.addAnimation(mTVTransAnim);
		mTVAnimSet.addAnimation(mTVAlphaAnim);
		mTVAnimSet.setDuration(300);
		mTVAnimSet.setFillAfter(true);
		mTV2TransAnim = new TranslateAnimation(-30, 0, 0, 0);
		mTV2AlphaAnim = new AlphaAnimation(0, 1);
		mTV2AnimSet = new AnimationSet(false);
		mTV2AnimSet.addAnimation(mTV2TransAnim);
		mTV2AnimSet.addAnimation(mTV2AlphaAnim);
		mTV2AnimSet.setDuration(200);
		mTV2AnimSet.setFillAfter(true);
		mFadingInAnim = new AlphaAnimation(0, 1);
		mFadingInAnim.setDuration(500);
		mTransInAnim = new TranslateAnimation(-50, 0, 0, 0);
		mTransInAnim.setDuration(200);
		mAnimInSet = new AnimationSet(false);
		mAnimInSet.addAnimation(mTransInAnim);
		mAnimInSet.addAnimation(mFadingInAnim);
		mAnimInSet.setFillAfter(true);
		if(mNum == 0){
			showAnim(0);
		}
	}

	public void showAnim(int index){
		if(mAnimHadEnd){
			return;
		}
		mIV.setVisibility(View.VISIBLE);
		mIV.startAnimation(mIVAnim);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		try {
			if(animation == mIVAnim){
				mIV.clearAnimation();
				mMirrorIV.setVisibility(View.VISIBLE);
				mMirrorIV.startAnimation(mMirrorAnim);
			}else if(animation == mMirrorAnim){
				mMirrorIV.clearAnimation();
				Thread.sleep(200);
				mTV1.setVisibility(View.VISIBLE);
				mTV1.startAnimation(mTVAnimSet);
			}else if(animation == mTVAnimSet){
				mTV1.clearAnimation();
				mTV2.setVisibility(View.VISIBLE);
				mTV2.startAnimation(mTV2AnimSet);
			}else if(animation == mTV2AnimSet){
				mTV1.clearAnimation();
				mAnimHadEnd = true;
				if(mNum == 4){
					if(mGuiderBtn.getVisibility() == View.GONE){
						mGuiderBtn.setVisibility(View.VISIBLE);
						mGuiderBtn.startAnimation(mAnimInSet);
					}
				}
			}
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guider_btn:
			guestUserLogin();
			break;

		default:
			break;
		}
	}
	
	private void guestUserLogin() {
		try {
			PackageManager pm = getActivity().getPackageManager();
			RequestParams params = new RequestParams();
			params.put("type", CyouApplication.getChannel());
			params.put("version", pm.getPackageInfo(getActivity().getPackageName(), 0).versionName);
			params.put("imsi", Util.getIMSI());
			MyHttpConnect.getInstance().post(HttpContants.NET.GUEST_LOGIN, params,
					new JsonHttpResponseHandler() {

						@Override
						public void onStart() {
							mWaitingDialog.show();
						}
						
						@Override
						public void onFailure(Throwable e, JSONArray errorResponse) {
							mWaitingDialog.dismiss();
						}

						@Override
						public void onSuccess(JSONObject response) {
							log.d("guest login result :"+response);
							if (null == response) {
								Toast.makeText(getActivity(), R.string.error_txt,
										Toast.LENGTH_SHORT).show();
								return;
							}
							try {
								if (!response.has("data")) {
									Toast.makeText(getActivity(), R.string.error_txt,
											Toast.LENGTH_SHORT).show();
									return;
								}
								String data = response.getString("data");
								if (TextUtils.isEmpty(data)) {
									Toast.makeText(getActivity(), R.string.error_txt,
											Toast.LENGTH_SHORT).show();
									return;
								}
								String userToken = JsonUtils.getJsonValue(data,
										"uauth");
								if (TextUtils.isEmpty(userToken)) {
									Toast.makeText(getActivity(), R.string.error_txt,
											Toast.LENGTH_SHORT).show();
									return;
								}
								String utoken = JsonUtils.getJsonValue(data, "utoken");
								UserInfoUtil.setUToken(utoken);
								SharedPreferenceUtil.setHasOpenApp();
								UserInfoUtil.setUauth(userToken, true);
								installApp();
								String isNewUser = JsonUtils.getJsonValue(data, "isnewusr");
								if(!TextUtils.isEmpty(isNewUser)){
									if(isNewUser.equals(NEW_USER)){
										Intent iComplete = new Intent(getActivity(),CompleteRegistActivity.class);
										startActivity(iComplete);
									}else{
										Intent intent = new Intent(getActivity(),LaunchService.class);
										getActivity().startService(intent);
										Intent mIntent = new Intent(getActivity(),MainActivity.class);
										mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										startActivity(mIntent);
									}
									getActivity().finish();
								}else{
									log.e("isnewusr is null");
								}
							} catch (Exception e) {
								log.e(e);
							}
							mWaitingDialog.dismiss();
						}

					});
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	private void installApp() {
		RequestParams params = new RequestParams();
		params.put("type", CyouApplication.getChannel());
		params.put("versioncode",
				String.valueOf(Util.getAppVersionCode(getActivity().getPackageName())));
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
						super.onSuccessForString(content);
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}
				});

	}

}
