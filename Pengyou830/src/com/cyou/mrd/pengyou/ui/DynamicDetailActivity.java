package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.DynamicDetailCommentAdapter;
import com.cyou.mrd.pengyou.adapter.DynamicDetailSupportersAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.DynamicComment;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicDetail;
import com.cyou.mrd.pengyou.entity.DynamicSupporters;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.base.DynamicDetailBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.RoundImageView;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
/**
 * 动态详情
 * @author 
 *
 */
public class DynamicDetailActivity extends CYBaseActivity implements OnClickListener{

	private CYLog log = CYLog.getInstance();
	
	private RoundImageView mAvatarIV;
	private TextView mNickNameTV;
	private ImageView mCaptureIV;
	private TextView mDataTV;
	private TextView mContentTV;
	private GridView mSupportGV;
	private TextView mSupportCountTV;
	private PullToRefreshListView mListView;
	private TextView mCommentCountTV;
	private View mHeaderView;
	private EditText mDynamicDetailET;
	private Button mSendBtn;
	private ImageButton mBackIBtn;
	private TextView mHeaderTV;
	private LinearLayout mSupporterLL;
	private WaitingDialog mWaitingDialog;
	private TextView mEmptyTV;
	private TextView mDevideTV;
	private TextView mFromTV;
	private ImageButton mDeleteBtn;
	private Button mRightBtn;
	private ProgressBar mCapturePB;
	private RelativeLayout mCaptureRL;
	private TextView mRatingTV;
	
	private int mAID;
	private int mUID;
	private int mPosition;
	private boolean mIfShowDelBtn;
	private DynamicDetail mDynamicDetail;
	private MyHttpConnect mConn;
	private List<DynamicCommentItem> mCommentData;
	private DynamicDetailCommentAdapter mAdapter;
	private boolean mHadInit = false;
	private DynamicDetailSupportersAdapter mSupporterAdapter;
	private InputMethodManager mImm;
	private boolean mSendingComment = false;
	private DisplayImageOptions mIconOptions;
	private DisplayImageOptions mCaptureOptions;
	private LinearLayout mGameLL;
	private TextView mGameNameTV;
	private TextView mGamePlayCountTV;
	private ImageView mGameIconIV;
	private ImageView mGameZoneIV;
	private TextView mGameTypeTV;
	private LinearLayout mRatingLL;
	private RatingBar mRB;
	private RefreshReceiver mRefreshReceiver;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.dynamic_detail);
		mImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
		initData();
		if(mRefreshReceiver == null){
			mRefreshReceiver = new RefreshReceiver();			
		}
		registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.UPDATE_RELATION_COMMENT_INFO));
	}
	@Override
	protected void initView() {
		this.mHeaderView = getLayoutInflater().inflate(R.layout.dynamic_detail_headerview, null);
		this.mAvatarIV = (RoundImageView)mHeaderView.findViewById(R.id.dynamic_detail_avatar_iv);
		this.mNickNameTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_nickname_tv);
		this.mCaptureIV = (ImageView)mHeaderView.findViewById(R.id.dynamic_detail_capture_iv);
		this.mCaptureRL = (RelativeLayout)mHeaderView.findViewById(R.id.dynamic_detail_capture_rl);
		this.mCapturePB = (ProgressBar)mHeaderView.findViewById(R.id.dynamic_detail_capture_pb);
		this.mDataTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_time_tv);
		this.mContentTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_content_tv);
		this.mSupportGV = (GridView)mHeaderView.findViewById(R.id.dynamic_detail_support_gv);
		this.mGameLL = (LinearLayout)mHeaderView.findViewById(R.id.dynamic_detail_item_game_ll);
		this.mGameZoneIV = (ImageView)mHeaderView.findViewById(R.id.dynamic_detail_item_game_zone_from);
		this.mGameNameTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_item_game_name_tv);
		this.mGamePlayCountTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_item_play_count_tv);
		this.mGameIconIV = (ImageView)mHeaderView.findViewById(R.id.dynamic_detail_item_game_icon_iv);
		this.mGameTypeTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_item_game_type_tv);
		this.mRatingLL = (LinearLayout)mHeaderView.findViewById(R.id.dynamic_detail_item_rating_ll);
		this.mRB = (RatingBar)mHeaderView.findViewById(R.id.dynamic_detail_item_ratingbar);
		
		this.mSupportGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				DynamicSupporters s = (DynamicSupporters)mSupportGV.getItemAtPosition(arg2);
				if(s == null){
					return;
				}
				Intent intent = new Intent(DynamicDetailActivity.this,FriendInfoActivity.class);
				intent.putExtra(Params.FRIEND_INFO.UID, s.getUid());
				startActivity(intent);
			}
		});
		this.mSupportCountTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_support_count_tv);
		this.mListView = (PullToRefreshListView)findViewById(R.id.dynamic_detail_comment_lv);
		this.mCommentCountTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_comment_count_tv);
		this.mListView.addHeaderView(mHeaderView);
		this.mDynamicDetailET = (EditText)findViewById(R.id.dynamic_detail_et);
		this.mSendBtn = (Button)findViewById(R.id.dynamic_detail_send_btn);
		this.mSendBtn.setOnClickListener(this);
//		View headerbar = findViewById(R.id.dynamic_detail_headerbar);
		this.mBackIBtn = (ImageButton)findViewById(R.id.dynamic_detail_back_ibtn);
		this.mBackIBtn.setOnClickListener(this);
		this.mDeleteBtn = (ImageButton)findViewById(R.id.dynamic_detail_delete_ibtn);
		this.mRightBtn =(Button)findViewById(R.id.sub_header_bar_right_ibtn);
		this.mDeleteBtn.setOnClickListener(this);
		this.mHeaderTV = (TextView)findViewById(R.id.dynamic_detail_header_tv);
		this.mHeaderTV.setText(R.string.dynamic_detail);
		this.mSupporterLL = (LinearLayout)findViewById(R.id.dynamic_supporter_ll);
		this.mWaitingDialog = new WaitingDialog(this);
		this.mDevideTV = (TextView)findViewById(R.id.dynamic_detail_comment_devide);
		this.mEmptyTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_empty_view);
		this.mFromTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_item_from_tv);
		this.mRatingTV = (TextView)mHeaderView.findViewById(R.id.dynamic_detail_rating_tv);
		this.mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				final DynamicCommentItem item = (DynamicCommentItem)mListView.getItemAtPosition(arg2);
				if(item == null){
					return;
				}
				if(item.getUid() == UserInfoUtil.getCurrentUserId()){
					new AlertDialog.Builder(DynamicDetailActivity.this)
					.setTitle(getResources().getString(R.string.delete_comment))
					.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							deleteComment(item.getCid());
							dialog.dismiss();
						}
					})
					.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.create()
					.show();
				}else{
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.toggleSoftInput(0,
									InputMethodManager.HIDE_NOT_ALWAYS);
						}
					}, 100);
					mDynamicDetailET.setTag(item);
					mDynamicDetailET.setHint(getString(R.string.reply)+item.getNickname()+":");
					mDynamicDetailET.requestFocus();
				}
			}
		});
		
		this.mIconOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default)
				.showStubImage(R.drawable.icon_default)
				.displayer(new RoundedBitmapDisplayer(Config.ROUND)).build();
		this.mCaptureOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true).cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.capture_default)
		.showImageOnFail(R.drawable.capture_default)
		.showStubImage(R.drawable.capture_default)
		.build();
	}
	
	@Override
	protected void initData() {
		mWaitingDialog.show();
		Intent intent = getIntent();
		mAID = intent.getIntExtra(Params.DYNAMIC_DETAIL.AID, 0);
		mUID = intent.getIntExtra(Params.DYNAMIC_DETAIL.UID, 0);
		mPosition = intent.getIntExtra(Params.DYNAMIC_DETAIL.POSITION, 0);
		mIfShowDelBtn = intent.getBooleanExtra(Params.DYNAMIC_DETAIL.DELETE_BUTTON_SHOW, false);
		mDeleteBtn.setVisibility(mIfShowDelBtn == true? View.VISIBLE : View.GONE);
		mRightBtn.setVisibility(mIfShowDelBtn == true? View.GONE : View.VISIBLE);
		mCommentData = new ArrayList<DynamicCommentItem>();
		mAdapter = new DynamicDetailCommentAdapter(this, mCommentData);
		mListView.setAdapter(mAdapter);
		mConn = MyHttpConnect.getInstance();
		getDynamicDetail();
	}
	
	private void initContent(){
		if(!TextUtils.isEmpty(mDynamicDetail.getNickname())){
			mNickNameTV.setText(getString(R.string.pub_a_dynamic, mDynamicDetail.getNickname()));
		}
		if(!TextUtils.isEmpty(mDynamicDetail.getText()) &&!TextUtils.isEmpty(mDynamicDetail.getText().trim())){
			mContentTV.setText(mDynamicDetail.getText());
		}
		else {
			mContentTV.setVisibility(View.GONE);
		}
		mDataTV.setText(Util.getDate(mDynamicDetail.getCreatedtime()));
		if(!TextUtils.isEmpty(mDynamicDetail.getAvatar())){
			DisplayImageOptions option = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.showImageForEmptyUri(R.drawable.avatar_defaul)
			.showImageOnFail(R.drawable.avatar_defaul)
			.showStubImage(R.drawable.avatar_defaul)
			.build();
			CYImageLoader.displayImg(mDynamicDetail.getAvatar()
					, mAvatarIV.getImageView(), option);
		}
		if(mDynamicDetail.getSupportusr() != null && !mDynamicDetail.getSupportusr().isEmpty()){
			if(mSupporterAdapter == null){
				mSupporterAdapter = new DynamicDetailSupportersAdapter(mDynamicDetail.getSupportusr(), this);
			}
			mSupportGV.setAdapter(mSupporterAdapter);
			int columWidth = 
//					getResources().getDimensionPixelSize(R.dimen.padding) * 2 + 
					getResources().getDimensionPixelSize(R.dimen.small_avatar_width);
			int totalWidth = mDynamicDetail.getSupportusr().size() * columWidth;
			LayoutParams params = new LayoutParams(totalWidth, LayoutParams.WRAP_CONTENT);
			mSupportGV.setLayoutParams(params);
			mSupportGV.setNumColumns(mDynamicDetail.getSupportusr().size());
			mSupportGV.setColumnWidth(columWidth);
			mSupportGV.setStretchMode(GridView.NO_STRETCH);
			mSupportCountTV.setText(String.valueOf(mDynamicDetail.getSupportusr().size()));
		}else{
			mSupporterLL.setVisibility(View.GONE);
		}
		mFromTV.setText("");
		switch (mDynamicDetail.getType()) {
		case Contants.DYNAMIC_TYPE.PUB_PIC:
			mCaptureRL.setVisibility(View.VISIBLE);
			mGameLL.setVisibility(View.GONE);
			mRatingLL.setVisibility(View.GONE);
			mCaptureIV.setOnTouchListener(Util.onTouchCaptureListener);
			mCaptureIV.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(DynamicDetailActivity.this, ShowPhotoActivity.class);
					intent.putExtra(Params.SHOW_PHOTO.PHOTO_TYPE,Params.SHOW_PHOTO.PHOTO_PIC);
					intent.putExtra(Params.PHOTO_URL, mDynamicDetail.getPicture().getPath());
					intent.putExtra(Params.PHOTO_MIDDLE_URL, mDynamicDetail.getPicturemiddle().getPath());
					startActivity(intent);
				}
			});

			if(mDynamicDetail.getPicture().getHeight() > mDynamicDetail.getPicture().getWidth()){
				int  height = mDynamicDetail.getPicture().getHeight();
				int  width =  mDynamicDetail.getPicture().getWidth();
				if(Config.screenWidthWithDip > 0){
					int mPicHeight = (Config.screenWidthWithDip - 70)*3/5;  //可显示的五分之三
					int mPicWidth = (mPicHeight*width)/height;
					RelativeLayout.LayoutParams  params  = new RelativeLayout.LayoutParams((int)(mPicWidth*Config.SreenDensity)
							, (int)(mPicHeight*Config.SreenDensity));							
					params.topMargin = getResources().getDimensionPixelSize(R.dimen.padding_left_right);
					params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.padding_left_right);
					mCaptureIV.setLayoutParams(params);
				}
				else																		
					mCaptureIV.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.pic_width)
						, getResources().getDimensionPixelSize(R.dimen.pic_height)));
			}else{
				int  height = mDynamicDetail.getPicture().getHeight();
				int  width = mDynamicDetail.getPicture().getWidth();
				if(Config.screenWidthWithDip > 0){
					int mPicWidth = (Config.screenWidthWithDip - 70)*3/5;  //可显示的五分之三
					int mPicHeight = (mPicWidth*height)/width;
					RelativeLayout.LayoutParams  params  =  new RelativeLayout.LayoutParams((int)(mPicWidth*Config.SreenDensity)
							, (int)(mPicHeight*Config.SreenDensity));							
					params.topMargin = getResources().getDimensionPixelSize(R.dimen.padding_left_right);
					params.bottomMargin = getResources().getDimensionPixelSize(R.dimen.padding_left_right);
					mCaptureIV.setLayoutParams(params);
				}
				else						
					mCaptureIV.setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.pic_height)
						, getResources().getDimensionPixelSize(R.dimen.pic_width)));
			}
			DisplayImageOptions option = new DisplayImageOptions.Builder()
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.showImageForEmptyUri(R.drawable.capture_default)
			.showImageOnFail(R.drawable.capture_default)
			.showStubImage(R.drawable.capture_default)
			.build();
			
			CYImageLoader.displayImg(mDynamicDetail.getPicture().getPath()
					, mCaptureIV, option);
			break;
		case Contants.DYNAMIC_TYPE.SHARE_GAME:
			mCaptureIV.setVisibility(View.GONE);
			mGameZoneIV.setVisibility(View.GONE);
			if (mDynamicDetail.getGame() != null) {
				mGameLL.setVisibility(View.VISIBLE);
				CYImageLoader.displayIconImage(mDynamicDetail.getGame().getGameicon(),
						mGameIconIV, mIconOptions);
				if (!TextUtils.isEmpty(mDynamicDetail.getGame().getGamenm())) {
					mGameNameTV.setText(
							mDynamicDetail.getGame().getGamenm());
				}
				mGamePlayCountTV.setText(
						this.getString(R.string.play_count, mDynamicDetail
								.getGame().getPlaynum()));
				mGameTypeTV.setText(mDynamicDetail.getGame().getGametype());
			}
			else{
				mGameLL.setVisibility(View.GONE);
			}
			mGameLL.setOnClickListener(new OnClickListener() {					
				@Override
				public void onClick(View v) {
					BehaviorInfo behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.ME.BTN_MYDYNAMIC_GAMEDETIAL_ID,
							CYSystemLogUtil.ME.BTN_MYDYNAMIC_GAMEDETIAL_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_DETAIL_ID,
							CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_DETAIL_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					Intent iIntro = new Intent(DynamicDetailActivity.this, GameCircleDetailActivity.class);
					iIntro.putExtra(Params.INTRO.GAME_CODE, mDynamicDetail.getGame()
							.getGamecode());
					iIntro.putExtra(Params.INTRO.GAME_NAME, mDynamicDetail.getGame().getGamenm());
					iIntro.putExtra(Params.INTRO.GAME_CIRCLE, false);
					DynamicDetailActivity.this.startActivity(iIntro);
				}
			});
			mRatingLL.setVisibility(View.GONE);
			break;
		case Contants.DYNAMIC_TYPE.PUB_TEXT:
			mCaptureIV.setVisibility(View.GONE);
			mGameLL.setVisibility(View.GONE);
			mRatingLL.setVisibility(View.GONE);
			break;
		case Contants.DYNAMIC_TYPE.GAME_CIRCLE:
			mGameLL.setVisibility(View.GONE);
			if(mDynamicDetail.getPicturemiddle() != null){
				mCaptureRL.setVisibility(View.VISIBLE);
				RelativeLayout.LayoutParams rparams = null;
				if(mDynamicDetail.getPicturemiddle().getHeight() > mDynamicDetail.getPicturemiddle().getWidth()){
					int  height = mDynamicDetail.getPicturemiddle().getHeight();
					int  width = mDynamicDetail.getPicturemiddle().getWidth();
					if(Config.screenWidthWithDip > 0){
						int mPicHeight = (Config.screenWidthWithDip - 70)*3/5;  //可显示的五分之三
						int mPicWidth = (mPicHeight*width)/height;
						rparams = new RelativeLayout.LayoutParams((int)(mPicWidth*Config.SreenDensity),(int)(mPicHeight*Config.SreenDensity));
						rparams.topMargin = getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						rparams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						mCaptureIV.setLayoutParams(rparams );
					}
					else{
						rparams = new RelativeLayout.LayoutParams(this.getResources().getDimensionPixelSize(R.dimen.pic_width), this.getResources().getDimensionPixelSize(R.dimen.pic_height));
						mCaptureIV.setLayoutParams(rparams);
					}																		

				}else{
					int  height = mDynamicDetail.getPicturemiddle().getHeight();
					int  width = mDynamicDetail.getPicturemiddle().getWidth();
					if(Config.screenWidthWithDip > 0){
						int mPicWidth = (Config.screenWidthWithDip - 70)*3/5;  //可显示的五分之三
						int mPicHeight = (mPicWidth*height)/width;
						rparams =  new RelativeLayout.LayoutParams((int)(mPicWidth*Config.SreenDensity)
								, (int)(mPicHeight*Config.SreenDensity));
						rparams.topMargin = getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						rparams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						mCaptureIV.setLayoutParams(rparams);
					}
					else{
						rparams = new RelativeLayout.LayoutParams(this.getResources().getDimensionPixelSize(R.dimen.pic_height)
								, this.getResources().getDimensionPixelSize(R.dimen.pic_width));
						mCaptureIV.setLayoutParams(rparams);
					}
				}	
				CYImageLoader.displayImg(mDynamicDetail.getPicturesmall().getPath(), mCaptureIV, mCaptureOptions,new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String arg0, View arg1) {
						
					}
					
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
						
					}
					
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
						try {
							if(arg2 == null){
								return;
							}
							mCaptureIV.setImageBitmap(arg2);
						} catch (Exception e) {
							log.e(e);
						}
						CYImageLoader.displayImg(mDynamicDetail.getPicturemiddle().getPath(), mCaptureIV, mCaptureOptions, new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								mCapturePB.setVisibility(View.VISIBLE);
							}
							
							@Override
							public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
								mCapturePB.setVisibility(View.GONE);
							}
							
							@Override
							public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
								mCapturePB.setVisibility(View.GONE);
								mCaptureIV.setImageBitmap(arg2);
							}
							
							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								mCapturePB.setVisibility(View.GONE);
							}
						});
					}
					
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
						
					}
				});
				mCaptureIV.setOnTouchListener(Util.onTouchCaptureListener);
				mCaptureIV.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(DynamicDetailActivity.this, ShowPhotoActivity.class);
						intent.putExtra(Params.SHOW_PHOTO.PHOTO_TYPE,Params.SHOW_PHOTO.PHOTO_PIC);
						intent.putExtra(Params.PHOTO_URL, mDynamicDetail.getPicture().getPath());
						intent.putExtra(Params.PHOTO_MIDDLE_URL, mDynamicDetail.getPicturemiddle().getPath());
						startActivity(intent);
					}
				});
			}else{
				mCaptureIV.setVisibility(View.GONE);
			}
			if(!TextUtils.isEmpty(mDynamicDetail.getStar())){
				mRatingLL.setVisibility(View.VISIBLE);
				mRB.setRating(Float.valueOf(mDynamicDetail.getStar()));
				mRB.setIsIndicator(true);
				if(mUID == UserInfoUtil.getCurrentUserId()){
					mRatingTV.setText(R.string.me_game_score_display);
				}else{
					mRatingTV.setText(R.string.game_score_display);
				}
			}else{
				mRatingLL.setVisibility(View.GONE);
			}
			if(mDynamicDetail.getGame() != null){
				mGameLL.setVisibility(View.VISIBLE);
				mGameZoneIV.setVisibility(View.VISIBLE);
				CYImageLoader.displayIconImage(mDynamicDetail.getGame().getGameicon(),
						mGameIconIV, mIconOptions);
				if (!TextUtils.isEmpty(mDynamicDetail.getGame().getGamenm())) {
					mGameNameTV.setText(
							mDynamicDetail.getGame().getGamenm());
				}
				mGamePlayCountTV.setText(
						this.getString(R.string.play_count, mDynamicDetail
								.getGame().getPlaynum()));
				mGameTypeTV.setText(mDynamicDetail.getGame().getGametype());
				mGameLL.setOnClickListener(new OnClickListener() {					
					@Override
					public void onClick(View v) {
						BehaviorInfo behaviorInfo = new BehaviorInfo(
								CYSystemLogUtil.ME.BTN_MYDYNAMIC_GAMEDETIAL_ID,
								CYSystemLogUtil.ME.BTN_MYDYNAMIC_GAMEDETIAL_NAME);
						CYSystemLogUtil.behaviorLog(behaviorInfo);
						behaviorInfo = new BehaviorInfo(
								CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_DETAIL_ID,
								CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_DETAIL_NAME);
						Intent iIntro = new Intent(DynamicDetailActivity.this, GameCircleDetailActivity.class);
						iIntro.putExtra(Params.INTRO.GAME_CODE, mDynamicDetail.getGame()
								.getGamecode());
						iIntro.putExtra(Params.INTRO.GAME_NAME, mDynamicDetail.getGame().getGamenm());
//						if(TextUtils.isEmpty(mDynamicDetail.getStar())){
//							iIntro.putExtra(Params.INTRO.GAME_CIRCLE, true);
//						}
//						else {
//							iIntro.putExtra(Params.INTRO.GAME_CIRCLE, false);
//						}
						iIntro.putExtra(Params.INTRO.GAME_CIRCLE, true);
						DynamicDetailActivity.this.startActivity(iIntro);
					}
				});
			}
			else {
				mGameLL.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}	
		
		
	}
	
	private void getDynamicDetail(){
		RequestParams params = new RequestParams();
		params.put("uid", String.valueOf(mUID));
		params.put("aid", String.valueOf(mAID));
		mConn.post(HttpContants.NET.GET_USER_DYNAMIC, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				 showNetErrorDialog(DynamicDetailActivity.this,new ReConnectListener() {								
						@Override
						public void onReconnect() {
							getDynamicDetail();
						}
					});
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(DynamicDetailActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(TextUtils.isEmpty(content)){
					return;
				}
				log.i("get user dynamic result = "+content);
				try {
					DynamicDetailBase base = new ObjectMapper()
					.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(content, new TypeReference<DynamicDetailBase>() {});
					if(base == null || base.getData().getAid() == 0){
						Toast.makeText(DynamicDetailActivity.this, R.string.dynamic_had_been_deleted, Toast.LENGTH_SHORT).show();
						DynamicDetailActivity.this.finish();
						return;
					}
					if(base.getData() != null){
						mDynamicDetail = base.getData();
						getDynamicComments();
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
		});
	}
	
	/**
	 * 获取动态评论
	 */
	private void getDynamicComments(){
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(mAID));
		if(!mCommentData.isEmpty()){
			params.put("cursor",String.valueOf(mCommentData.get(0).getCid()));
		}
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		mConn.post(HttpContants.NET.GET_DYNAMIC_COMMENT, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				mWaitingDialog.dismiss();
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(DynamicDetailActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(TextUtils.isEmpty(content)){
					return;
				}
				log.i("dynamic comments result ="+content);
				try {
					DynamicComment comment = new ObjectMapper()
					.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(content, new TypeReference<DynamicComment>() {});
					if(!mHadInit){
						mHadInit = true;
						initContent();
					}
					if(comment == null){
						return;
					}
					if(comment.getData() != null && !comment.getData().isEmpty()){
						if(mCommentData!=null){
							mCommentData.clear();
						}
						mCommentData.addAll(comment.getData());
						mAdapter.notifyDataSetChanged();
						if(comment.getData().size()<Config.PAGE_SIZE){
							mListView.loadComplete();
						}
						mListView.loadingFinish();
					}
					if(mCommentData.isEmpty()){
						mEmptyTV.setVisibility(View.VISIBLE);
						mDevideTV.setVisibility(View.GONE);
					}
					mCommentCountTV.setText(getString(R.string.dynamic_comment_count, mCommentData.size()));
				} catch (Exception e) {
					log.e(e);
				}
				mWaitingDialog.dismiss();
				mListView.loadComplete();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dynamic_detail_send_btn:
			String content = mDynamicDetailET.getText().toString();
			mDynamicDetailET.setText("");
			if(TextUtils.isEmpty(content.trim())){
				Toast.makeText(DynamicDetailActivity.this, R.string.input_empty, Toast.LENGTH_SHORT).show();
				return;
			}
			DynamicCommentItem item = (DynamicCommentItem)mDynamicDetailET.getTag();
			sendComment(content,item);
			mDynamicDetailET.setTag(null);
			View currentFocusView = DynamicDetailActivity.this.getCurrentFocus();
			if (currentFocusView != null) {
				IBinder binder = currentFocusView.getWindowToken();
				if (binder != null) {
					mImm.hideSoftInputFromWindow(binder,
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
			break;
		case R.id.dynamic_detail_back_ibtn:
			DynamicDetailActivity.this.finish();
			break;
		case R.id.dynamic_detail_delete_ibtn:
			if(mDynamicDetail == null){
				return;
			}
			new AlertDialog.Builder(DynamicDetailActivity.this)
				.setTitle("删除动态")
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						deleteDynamic(mDynamicDetail.getAid());
						
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				})
				.create()
				.show();
//			deleteDynamic(mDynamicDetail.getAid());
			break;
		default:
			break;
		}
	}
	
	private void deleteDynamic(int aid){
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		mConn.post(HttpContants.NET.DELETE_DYNAMIC, params,new AsyncHttpResponseHandler(){
			
			@Override
			public void onStart() {
				mWaitingDialog.show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				mWaitingDialog.dismiss();
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(DynamicDetailActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				log.i("delete dynamic result = "+content);
				try {
					mWaitingDialog.dismiss();
					JSONObject obj = new JSONObject(content);
					if (obj.has(Params.HttpParams.SUCCESSFUL)) {
						UserInfoUtil.changeDyanmicCount(-1);
						Intent intent = new Intent(DynamicDetailActivity.this,MyDynamicActivity.class);
						intent.putExtra(Params.DYNAMIC_DETAIL.POSITION, mPosition);
						DynamicDetailActivity.this.setResult(1, intent);
						DynamicDetailActivity.this.finish();
					}
					Util.requestFansAndLetterCount(DynamicDetailActivity.this,SystemCountMsgItem.SYSTEM);
				} catch (Exception e) {
					log.e(e);
				}
			}
		});
	}

	private void deleteComment(final int cid){
		RequestParams params = new RequestParams();
		params.put("cid", String.valueOf(cid));
		mConn.post(HttpContants.NET.DELETE_COMMENT, params,new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				mWaitingDialog.show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				mWaitingDialog.dismiss();
				Toast.makeText(DynamicDetailActivity.this,DynamicDetailActivity.this.getString(R.string.networks_available), Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(DynamicDetailActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					mWaitingDialog.dismiss();
					JSONObject obj = new JSONObject(content);
					if(obj.has(Params.HttpParams.SUCCESSFUL)){
						Iterator<DynamicCommentItem> it = mCommentData.iterator();
						while(it.hasNext()){
							DynamicCommentItem nextItem = it.next();
							if(nextItem.getCid() == cid){
								it.remove();
								//TODO 当删除自己回复其他人的评论的时候。广播产生异常
								Intent intent = new Intent(
										Contants.ACTION.UPDATE_RELATION_COMMENT_INFO);
								intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,1); //0:评论 1：删除评论
								intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,0); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态, 4:关系圈
								intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,mAID);
								intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, nextItem);
								sendBroadcast(intent);
								break;
							}
						}
						
					}
				} catch (Exception e) {
					log.e(e);
				}
				
					mAdapter.notifyDataSetChanged();
					mCommentCountTV.setText(getString(R.string.dynamic_comment_count, mCommentData.size()));
					if(mCommentData.isEmpty()){
						mEmptyTV.setVisibility(View.VISIBLE);
						mDevideTV.setVisibility(View.GONE);
					
				}
			}
		});
	}
	
	private void sendComment(final String content,final DynamicCommentItem replyTo){
		if(mSendingComment){
			Toast.makeText(this, R.string.comment_sending_waiting, Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(mAID));
		params.put("text", content);
		if(replyTo != null){
			params.put("reuid", String.valueOf(replyTo.getUid()));
		}
		mConn.post(HttpContants.NET.SEND_DYNAMIC_COMMENT, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				mSendingComment = true;
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				mSendingComment = false;
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(DynamicDetailActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String result) {
				super.onSuccess(statusCode, result);
				mDynamicDetailET.setHint("");
				mSendingComment = false;
				if(TextUtils.isEmpty(result)){
					return;
				}
				log.i("send comment result = "+result);
				try {
					JSONObject obj = new JSONObject(result);
					if(obj.has(Params.HttpParams.SUCCESSFUL)){
						if(obj.getInt(Params.HttpParams.SUCCESSFUL) == 1){
    						String cid = JsonUtils.getJsonValue(
    								JsonUtils.getJsonValue(result, "data"),
    								"cid");
    						if (TextUtils.isEmpty(cid)) {
    							showShortToast(R.string.download_error_network_error);
    							return;
    						}
							DynamicCommentItem item = new DynamicCommentItem();
							item.setAvatar(UserInfoUtil.getCurrentUserPicture());
							item.setNickname(UserInfoUtil.getCurrentUserNickname());
							item.setComment(content);
							item.setText(content);
							item.setTimestamp(System.currentTimeMillis()/1000);
							item.setUid(UserInfoUtil.getCurrentUserId());
							item.setCid(Integer.parseInt(cid));
							if(replyTo != null){
								DynamicCommentReplyItem reply = new DynamicCommentReplyItem();
								reply.setNickname(replyTo.getNickname());
								reply.setUid(replyTo.getUid());
								item.setReplyto(reply);
							}
							mCommentData.add(0, item);
							mAdapter.notifyDataSetChanged();
							mCommentCountTV.setText(getString(R.string.dynamic_comment_count, mCommentData.size()));
							Intent intent = new Intent(
									Contants.ACTION.UPDATE_RELATION_COMMENT_INFO);
							intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,0); //0:评论 1：删除评论
							intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,0); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态, 4:关系圈
							intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,mAID);
							intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, item);
							sendBroadcast(intent);
						}
						else {
							String errorNo = JsonUtils.getJsonValue(result,
									"errorNo");
							if (!TextUtils.isEmpty(errorNo)
									&& String.valueOf(
											Contants.ERROR_NO.ERROR_MASK_WORD_STRING)
											.equals(errorNo)) {
								showShortToast(R.string.comment_maskword_failed);
							}
						}
						if(!mCommentData.isEmpty()){
							mEmptyTV.setVisibility(View.GONE);
							mDevideTV.setVisibility(View.VISIBLE);
						}
						View v = DynamicDetailActivity.this.getCurrentFocus();
						if (v != null) {
							IBinder binder = v.getWindowToken();
							if (binder != null) {
								mImm.hideSoftInputFromWindow(binder,
										InputMethodManager.HIDE_NOT_ALWAYS);
							}
						}
					}
				} catch (Exception e) {
					log.e(e);
				}
				mSendingComment = false;
			}
		});
	}

	
	@Override
	protected void initEvent() {
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if( mRefreshReceiver != null){
				unregisterReceiver(mRefreshReceiver);
				mRefreshReceiver = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 接到广播后刷新数据
	 * @author bichunhe
	 *
	 */
	private class RefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Contants.ACTION.UPDATE_RELATION_COMMENT_INFO.equals(intent
					.getAction())) {
				try {
					int source = intent.getIntExtra(
							Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, 0);
					if (source == 0) {
						return;
					}
					int type = intent.getIntExtra(
							Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE, 0);
					int aid = intent.getIntExtra(
							Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, 0);
					DynamicCommentItem mCItem = (DynamicCommentItem) intent
							.getSerializableExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT);
					if (mCommentData != null) {
						boolean needed = false;

						if (mAID == aid) {
							needed = true;
						}
						if (needed) {
							if (type == 0) {
								for (DynamicCommentItem mItem : mCommentData) {
									if (mItem.getCid() == mCItem.getCid()) {
										needed = false;
										break;
									}
								}
								if (needed) {
									mCommentData.add(0, mCItem);
									mEmptyTV.setVisibility(View.GONE);
									mDevideTV.setVisibility(View.VISIBLE);
									mAdapter.notifyDataSetChanged();
									mCommentCountTV.setText(getString(
											R.string.dynamic_comment_count,
											mCommentData.size()));
								}
							}
							else {
									int cIndex = 0;
									for (DynamicCommentItem mItem : mCommentData) {
										if (mItem.getCid() == mCItem.getCid()) {
											cIndex = mCommentData.indexOf(mItem);
											needed = true;
											break;
										} else {
											needed = false;
										}
									}

									if (needed) {
										mCommentData.remove(cIndex);
										if(mCommentData.isEmpty()){
											mEmptyTV.setVisibility(View.VISIBLE);
											mDevideTV.setVisibility(View.GONE);
										}
										mAdapter.notifyDataSetChanged();
										mCommentCountTV.setText(getString(
												R.string.dynamic_comment_count,
												mCommentData.size()));
									}
							}
						}
					}
				} catch (Exception e) {

					// TODO: handle exception
				}
			}

		}
	}

}
