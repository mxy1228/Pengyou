package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GuessGameAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.AdvertBean;
import com.cyou.mrd.pengyou.entity.GuessGameItem;
import com.cyou.mrd.pengyou.entity.base.AdvertBase;
import com.cyou.mrd.pengyou.entity.base.GuessGameBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class GuessGameFragment extends CYBaseFragment implements OnItemClickListener,OnClickListener{

	public static final String DATA = "data";
	
	private ListView mListView;
	private View mFootView;
	private RelativeLayout mChangeRL;
	private ImageView mBannerIV;
	private RelativeLayout mBannerRL;
	private ImageButton mCloseIBtn;
	
	private GuessGameAdapter mAdapter;
	private List<GuessGameItem> mData;
	private int mGuessPageNo = 2;
	private Activity mActivity;
	private boolean mLoading;
	private AdvertBean mAdvertBean;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.guess_game_lv, null);
		this.mListView = (ListView)view.findViewById(R.id.my_game_guess_lv);
		this.mFootView = getActivity().getLayoutInflater().inflate(R.layout.guess_game_footer, null);
		this.mChangeRL = (RelativeLayout)mFootView.findViewById(R.id.guess_game_change_rl);
		this.mListView.addFooterView(mFootView);
		this.mBannerIV = (ImageView)view.findViewById(R.id.my_game_guess_banner_iv);
		this.mBannerRL = (RelativeLayout)view.findViewById(R.id.my_game_item_banner_rl);
		this.mCloseIBtn = (ImageButton)view.findViewById(R.id.my_game_guess_banner_close_btn);
		initEvent();
		initData();
		return view;
	}
	
	@Override
	protected void initEvent() {
		this.mListView.setOnItemClickListener(this);
		this.mChangeRL.setOnClickListener(this);
		this.mCloseIBtn.setOnClickListener(this);
		this.mBannerIV.setOnClickListener(this);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	protected void initData() {
		this.mData = new ArrayList<GuessGameItem>();
		this.mAdapter = new GuessGameAdapter(getActivity(), mData);
		this.mListView.setAdapter(mAdapter);
		ArrayList<GuessGameItem> list = getArguments().getParcelableArrayList(DATA);
		if(list != null && mData != null){
			this.mData.addAll(list);
			this.mAdapter.notifyDataSetChanged();
		}
		getBannerPic();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		GuessGameItem item = (GuessGameItem)mListView.getItemAtPosition(arg2);
		if(item != null){
			Intent detailIntent = new Intent();
			detailIntent.setClass(getActivity(),
					GameCircleDetailActivity.class);
			detailIntent.putExtra(Params.INTRO.GAME_CODE,
					item.getGamecode());
			detailIntent.putExtra(Params.INTRO.GAME_NAME,
					item.getName());
			getActivity().startActivity(detailIntent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.guess_game_change_rl:
			loadGuessGame();
			break;
		case R.id.my_game_guess_banner_close_btn:
			mBannerRL.setVisibility(View.GONE);
			break;
		case R.id.my_game_guess_banner_iv:
			Intent intent = new Intent();
			String type = Util.getAdsType(mAdvertBean.getUri());
			if (TextUtils.isEmpty(type)) {
				Toast.makeText(
						mActivity,
						mActivity.getText(R.string.gamestore_error_params),
						0).show();
				return;
			}
			String value = "";
			value = Util.getGameCodeByUrl(mAdvertBean.getUri(), type);
			if (Contants.SCROLL_ADS.GAME.equals(type)) {// 游戏详细
				String gameName = mAdvertBean.getName();
				intent.putExtra(Params.INTRO.GAME_CODE, value);
				intent.setClass(mActivity,
						GameCircleDetailActivity.class);
				intent.putExtra(Params.INTRO.GAME_NAME, gameName);
			} else if (Contants.SCROLL_ADS.TOPIC.equals(type)) {// 专题
				// 跳转至专题详细
				intent.putExtra(Params.GAME_SPECIAL.SPECIAL_ID, value);
				intent.setClass(mActivity,
						GameSpecialDetailActivity.class);
			} else if (Contants.SCROLL_ADS.CIRCLE.equals(type)) {// 游戏圈
				intent.putExtra(Params.Dynamic.GAME_CIRCLE_ID, value);
				if (Util.isInstallByread(mAdvertBean.getIdentifier())) {
					intent.putExtra(Params.INTRO.GAME_ISINSTALLED, true);
				} else {
					intent.putExtra(Params.INTRO.GAME_ISINSTALLED,
							false);
				}
				intent.setClass(mActivity, GameCircleActivity.class);
				intent.putExtra(Params.INTRO.GAME_CODE,
						mAdvertBean.getGamecode());
			    intent.putExtra(Params.INTRO.GAME_PKGE,mAdvertBean.getIdentifier());
			} else if (Contants.SCROLL_ADS.WEB.equals(type)) {// HTML
				intent.putExtra(Params.GAMESTORE_HTML, value);
				intent.putExtra(Params.GAMESTORE_HTML_NAME,
						mAdvertBean.getName());
				intent.setClass(mActivity, GameStoreWebActivity.class);
			} else if (Contants.SCROLL_ADS.GIFT.equals(type)) {// 礼包
				// 跳转至礼包详细
				intent.putExtra(Params.DYNAMIC_DETAIL.GAMEGIFTID, value);
				intent.setClass(mActivity, GameGiftDetailActivity.class);
			} else if (Contants.SCROLL_ADS.GLOBALACT.equals(type)) {// 广场
				if(null!=CyouApplication.getMainActiv()){
					CyouApplication.getMainActiv().intent2GRelationCircle();
				}
				return;
			} else {
				Toast.makeText(
						mActivity,
						mActivity.getText(R.string.gamestore_error_params),
						0).show();
				return;
			}
			intent.putExtra(Params.INTRO.GAME_CIRCLE, false);
			mActivity.startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 加载猜你喜欢
	 * xumengyang
	 */
	private void loadGuessGame(){
		if(mLoading){
			return;
		}
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mGuessPageNo));
		params.put("count", "3");
		MyHttpConnect.getInstance().post(HttpContants.NET.GUESS_GAMS, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				mLoading = true;
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(mActivity);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				mLoading = false;
				if(TextUtils.isEmpty(content)){
					log.e("content is null");
					return;
				}
				try {
					log.d("猜你喜欢 = "+content);
					GuessGameBase base = new ObjectMapper().configure(
							DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
							false).readValue(content,
							new TypeReference<GuessGameBase>() {
							});
					if(base == null){
						log.e("base is null");
						return;
					}
					ArrayList<GuessGameItem> list = base.getData();
					mData.clear();
					mData.addAll(list);
					mAdapter.notifyDataSetChanged();
					mGuessPageNo++;
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				showShortToast(R.string.download_error_network_error);
				mLoading = false;
			}
		});
	}
	
	public void notifyData(){
		if(mAdapter != null){
			this.mAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 获取广告页
	 * xumengyang
	 */
	private void getBannerPic(){
		RequestParams params = new RequestParams();
		params.put("type", "scrollnotxhomepage");
		params.put("multitype", "1");
		MyHttpConnect.getInstance().post(HttpContants.NET.GAME_STORE_TOPADS, params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				log.d("获取广告页="+content);
				try {
					AdvertBase base = new ObjectMapper().configure(
							DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
							false).readValue(content,
							new TypeReference<AdvertBase>() {
							});
					if(base != null){
						if(base.getData() != null && !base.getData().isEmpty()){
							mAdvertBean = base.getData().get(0);
							CYImageLoader.displayGameImg(mAdvertBean.getImg()
									, mBannerIV
									, new DisplayImageOptions.Builder()
									.cacheInMemory(true)
									.cacheOnDisc(true)
									.showImageOnFail(R.drawable.gamestore_recommend_defaul_bg)
									.showImageForEmptyUri(R.drawable.gamestore_recommend_defaul_bg)
									.build());
						}else{
							mBannerIV.setImageResource(R.drawable.gamestore_recommend_defaul_bg);
						}
					}else{
						log.e("base is null");
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
			}
			
			@Override
			protected void onLoginOut() {
				super.onLoginOut();
			}
		});
	}
}
