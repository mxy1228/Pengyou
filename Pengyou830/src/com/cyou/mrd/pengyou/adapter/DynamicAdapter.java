package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.DynamicComment;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.DynamicDetailActivity;
import com.cyou.mrd.pengyou.ui.GameCircleDetailActivity;
import com.cyou.mrd.pengyou.ui.ShowPhotoActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.RelationCircleCache;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.DynamicViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DynamicAdapter extends BaseAdapter implements OnClickListener {

	private CYLog log = CYLog.getInstance();
	private Activity mContext;
	private List<DynamicItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mIconOptions;
	private DisplayImageOptions mCaptureOptions;
	private PullToRefreshListView mListView;
	private Handler mHandler;
	private boolean mSupporting = false;
	private RelationCircleCache   relCache;

	public DynamicAdapter(Activity context, List<DynamicItem> data,
			PullToRefreshListView listview, Handler handler) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		this.mListView = listview;
		this.mHandler = handler;
		this.relCache = new RelationCircleCache(mContext);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DynamicViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.dynamic_item, null);
			viewCache = new DynamicViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (DynamicViewCache) convertView.getTag();
		}
		DynamicItem item = mData.get(position);
		if (item != null) {
			switch (item.getType()) {
			case Contants.DYNAMIC_TYPE.SHARE_GAME:
				viewCache.getmGameLL().setVisibility(View.VISIBLE);
				viewCache.getmGameZoneIV().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.GONE);
				viewCache.getmRatingLL().setVisibility(View.GONE);
				viewCache.getmFromTV().setText("");
				if (!TextUtils.isEmpty(item.getText()) && !TextUtils.isEmpty(item.getText().trim())) {
					viewCache.getmContentTV().setVisibility(View.VISIBLE);
					viewCache.getmContentTV().setVisibility(View.VISIBLE);
					viewCache.getmContentTV().setText(item.getText());
				}else{
					viewCache.getmContentTV().setVisibility(View.GONE);
					viewCache.getmContentTV().setText("");
				}
				if (item.getGame() != null) {
					CYImageLoader.displayIconImage(item.getGame().getGameicon(),
							viewCache.getmGameIconIV(), mIconOptions);
					if (!TextUtils.isEmpty(item.getGame().getGamenm())) {
						viewCache.getmGameNameTV().setText(
								item.getGame().getGamenm());
					}
					viewCache.getmGamePlayCountTV().setText(
							mContext.getString(R.string.play_count, item
									.getGame().getPlaynum()));
					viewCache.getmGameTypeTV().setText(item.getGame().getGametype());
				}
				break;
			case Contants.DYNAMIC_TYPE.PUB_TEXT:
				viewCache.getmGameLL().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.GONE);
				viewCache.getmRatingLL().setVisibility(View.GONE);
				viewCache.getmFromTV().setText("");
				if (TextUtils.isEmpty(item.getText()) || TextUtils.isEmpty(item.getText().trim())) {
					viewCache.getmContentTV().setVisibility(View.GONE);
					viewCache.getmContentTV().setVisibility(View.GONE);
					viewCache.getmContentTV().setText("");
				} else {
					viewCache.getmContentTV().setVisibility(View.VISIBLE);
					viewCache.getmContentTV().setText(item.getText());
				}
				break;
			case Contants.DYNAMIC_TYPE.CAPTURE:
				viewCache.getmGameLL().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.VISIBLE);
				viewCache.getmCaptureIV().setOnTouchListener(Util.onTouchCaptureListener);
				viewCache.getmRatingLL().setVisibility(View.GONE);
				viewCache.getmFromTV().setText("");
				break;
			case Contants.DYNAMIC_TYPE.PUB_PIC:
				viewCache.getmGameLL().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.VISIBLE);
				viewCache.getmCaptureIV().setOnTouchListener(Util.onTouchCaptureListener);
				viewCache.getmRatingLL().setVisibility(View.GONE);
				viewCache.getmFromTV().setText("");
				if (TextUtils.isEmpty(item.getText()) || TextUtils.isEmpty(item.getText().trim())) {
					viewCache.getmContentTV().setVisibility(View.GONE);
					viewCache.getmContentTV().setText("");
				} else {
					viewCache.getmContentTV().setVisibility(View.VISIBLE);
					viewCache.getmContentTV().setText(item.getText());
				}
				LayoutParams params = null;
				if(item.getPicturemiddle().getHeight() > item.getPicturemiddle().getWidth()){
					int  height = item.getPicturemiddle().getHeight();
					int  width = item.getPicturemiddle().getWidth();
					if(Config.screenWidthWithDip > 0){
						int mPicHeight = (Config.screenWidthWithDip - 70)*3/5;  //可显示的五分之三
						int mPicWidth = (mPicHeight*width)/height;
						params = new LayoutParams((int)(mPicWidth*Config.SreenDensity),(int)(mPicHeight*Config.SreenDensity));
						params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dynamic_margin_left);					
						params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						viewCache.getmCaptureIV().setLayoutParams(params );
					}
					else{
						params = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.pic_width), mContext.getResources().getDimensionPixelSize(R.dimen.pic_height));
						params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dynamic_margin_left);
						params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						viewCache.getmCaptureIV().setLayoutParams(params);
					}																		

				}else{
					int  height = item.getPicturemiddle().getHeight();
					int  width = item.getPicturemiddle().getWidth();
					if(Config.screenWidthWithDip > 0){
						int mPicWidth = (Config.screenWidthWithDip - 70)*3/5;  //可显示的五分之三
						int mPicHeight = (mPicWidth*height)/width;
						params =  new LayoutParams((int)(mPicWidth*Config.SreenDensity)
								, (int)(mPicHeight*Config.SreenDensity));
						params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dynamic_margin_left);
						params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						viewCache.getmCaptureIV().setLayoutParams(params);
					}
					else{
						params = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.pic_height)
								, mContext.getResources().getDimensionPixelSize(R.dimen.pic_width));
						params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dynamic_margin_left);
						params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
						viewCache.getmCaptureIV().setLayoutParams(params);
					}
				}
				CYImageLoader.displayImg(item.getPicturemiddle().getPath(),
						viewCache.getmCaptureIV(), mCaptureOptions);
				viewCache.getmCaptureIV().setTag(position);
				viewCache.getmCaptureIV().setOnClickListener(this);
				break;
			case Contants.DYNAMIC_TYPE.PLAY_GAME:
				viewCache.getmGameLL().setVisibility(View.VISIBLE);
				viewCache.getmGameZoneIV().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.GONE);
				viewCache.getmRatingLL().setVisibility(View.GONE);
				viewCache.getmFromTV().setText("");
				if (!TextUtils.isEmpty(item.getText()) && !TextUtils.isEmpty(item.getText().trim())) {
					viewCache.getmContentTV().setVisibility(View.VISIBLE);
					viewCache.getmContentTV().setVisibility(View.VISIBLE);
					viewCache.getmContentTV().setText(item.getText());
				}else{
					viewCache.getmContentTV().setVisibility(View.GONE);
				}
				if (item.getGame() != null) {
					CYImageLoader.displayIconImage(item.getGame().getGameicon(),
							viewCache.getmGameIconIV(), mIconOptions);
					if (!TextUtils.isEmpty(item.getGame().getGamenm())) {
						viewCache.getmGameNameTV().setText(
								item.getGame().getGamenm());
					}
					viewCache.getmGamePlayCountTV().setText(
							mContext.getString(R.string.play_count, item
									.getGame().getPlaynum()));
					viewCache.getmGameTypeTV().setText(item.getGame().getGametype());
				}
				break;
			case Contants.DYNAMIC_TYPE.GAME_CIRCLE:
				viewCache.getmFromTV().setText("");
				if (!TextUtils.isEmpty(item.getText())&& !TextUtils.isEmpty(item.getText().trim())) {
					viewCache.getmContentTV().setVisibility(View.VISIBLE);
					viewCache.getmContentTV().setText(item.getText());
				}else{
					viewCache.getmContentTV().setVisibility(View.GONE);
				}
				if(item.getPicturemiddle() != null){
					viewCache.getmCaptureIV().setVisibility(View.VISIBLE);
					LayoutParams rparams = null;
					if(item.getPicturemiddle().getHeight() > item.getPicturemiddle().getWidth()){
						int  height = item.getPicturemiddle().getHeight();
						int  width = item.getPicturemiddle().getWidth();
						if(Config.screenWidthWithDip > 0){
							int mPicHeight = (Config.screenWidthWithDip - 70)*3/5;  //可显示的五分之三
							int mPicWidth = (mPicHeight*width)/height;
							rparams = new LayoutParams((int)(mPicWidth*Config.SreenDensity),(int)(mPicHeight*Config.SreenDensity));
							rparams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dynamic_margin_left);
							rparams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
							rparams.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
							viewCache.getmCaptureIV().setLayoutParams(rparams );
						}
						else{
							rparams = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.pic_width), mContext.getResources().getDimensionPixelSize(R.dimen.pic_height));
							rparams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dynamic_margin_left);
							rparams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
							rparams.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
							viewCache.getmCaptureIV().setLayoutParams(rparams);
						}																		

					}else{
						int  height = item.getPicturemiddle().getHeight();
						int  width = item.getPicturemiddle().getWidth();
						if(Config.screenWidthWithDip > 0){
							int mPicWidth = (Config.screenWidthWithDip - 70)*3/5;  //可显示的五分之三
							int mPicHeight = (mPicWidth*height)/width;
							rparams =  new LayoutParams((int)(mPicWidth*Config.SreenDensity)
									, (int)(mPicHeight*Config.SreenDensity));
							rparams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dynamic_margin_left);
							rparams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
							rparams.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
							viewCache.getmCaptureIV().setLayoutParams(rparams);
						}
						else{
							rparams = new LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.pic_height)
									, mContext.getResources().getDimensionPixelSize(R.dimen.pic_width));
							rparams.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dynamic_margin_left);
							rparams.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
							rparams.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
							viewCache.getmCaptureIV().setLayoutParams(rparams);
						}
					}
					CYImageLoader.displayImg(item.getPicturemiddle().getPath(),
							viewCache.getmCaptureIV(), mCaptureOptions);
					viewCache.getmCaptureIV().setTag(position);
					viewCache.getmCaptureIV().setOnClickListener(this);
					viewCache.getmCaptureIV().setOnTouchListener(Util.onTouchCaptureListener);
				}else{
					viewCache.getmCaptureIV().setVisibility(View.GONE);
				}

				if(!TextUtils.isEmpty(item.getStar())){
					viewCache.getmRatingLL().setVisibility(View.VISIBLE);
					viewCache.getmRB().setRating(Float.valueOf(item.getStar()));
					viewCache.getmRB().setIsIndicator(true);
					
				}else{
					viewCache.getmRatingLL().setVisibility(View.GONE);
					
				}
				if(item.getGame() != null){
					viewCache.getmGameLL().setVisibility(View.VISIBLE);
					viewCache.getmGameZoneIV().setVisibility(View.VISIBLE);
					CYImageLoader.displayIconImage(item.getGame().getGameicon(),
							viewCache.getmGameIconIV(), mIconOptions);
					if (!TextUtils.isEmpty(item.getGame().getGamenm())) {
						viewCache.getmGameNameTV().setText(
								item.getGame().getGamenm());
					}
					viewCache.getmGamePlayCountTV().setText(
							mContext.getString(R.string.play_count, item
									.getGame().getPlaynum()));
					viewCache.getmGameTypeTV().setText(item.getGame().getGametype());
				}
				else {
					viewCache.getmGameLL().setVisibility(View.GONE);
				}
				break;
			default:
				break;
			}

			viewCache.getmDateTV().setText(Util.getFormatDate(item.getCreatedtime()));
			viewCache.getmCommentBtn().setTag(position);
			viewCache.getmCommentBtn().setOnClickListener(this);
			viewCache.getmCommentBtn().setTag(R.id.dynamic_item_comment_item_pb, viewCache.getmPB());
			viewCache.getmCommentBtn().setTag(R.id.dynamic_item_comment_rl, viewCache.getmCommentLL());
			viewCache.getmCommentBtn().setTag(R.id.dynamic_item_comment_item_empty_tv,viewCache.getmEmptyTV());
			if (!TextUtils.isEmpty(item.getNickname())) {
				viewCache.getmNameTV().setText(
						mContext.getString(R.string.dynamic_hint,
								item.getNickname()));
			}
			if (item.isOpen()) {
				viewCache.getmCommentLL().setVisibility(View.VISIBLE);
			} else {
				viewCache.getmCommentLL().setVisibility(View.GONE);
			}
			viewCache.getmPackUpIBtn().setTag(position);
			viewCache.getmPackUpIBtn().setOnClickListener(this);
			// 初始化评论列表相关
			viewCache.getmCommentTV().setText(
					Html.fromHtml(item.getComment().toString()));
			viewCache.getmCommentTV().setMovementMethod(LinkMovementMethod.getInstance());
			Util.initComment(viewCache.getmCommentTV());
			if(item.isLoadingComments()){
				viewCache.getmPB().setVisibility(View.VISIBLE);
				viewCache.getmEmptyTV().setVisibility(View.GONE);
			}else{
				if(TextUtils.isEmpty(item.getComment().toString())){
					viewCache.getmEmptyTV().setVisibility(View.VISIBLE);
				}else{
					viewCache.getmEmptyTV().setVisibility(View.GONE);
				}
				viewCache.getmPB().setVisibility(View.GONE);
			}
			viewCache.getmSupportBtn().setTag(position);
			viewCache.getmSupportBtn().setOnClickListener(this);
			viewCache.getmCommentIBtn().setTag(position);
			viewCache.getmCommentIBtn().setOnClickListener(this);
			viewCache.getmTimeTV().setText(Util.getDate(item.getCreatedtime()));
			viewCache.getmGameLL().setOnClickListener(this);
			viewCache.getmGameLL().setTag(position);
			if(item.getSupported() == Contants.SUPPORT.YES){
				viewCache.getmSupportBtn().setBackgroundResource(R.drawable.btn_supported_bg);
			}else{
				viewCache.getmSupportBtn().setBackgroundResource(R.drawable.dynamic_support_btn_xbg2);
			}
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		DynamicItem item = mData.get(position);
		if(item == null){
			Toast.makeText(mContext, R.string.failed_get_data, Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		case R.id.dynamic_item_comment_btn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_COMMENT_ALL_ID,
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_COMMENT_ALL_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_COMMENT_MORE_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_COMMENT_MORE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			//若为自己的动态则点击进详情页
			if(item.getUid() == UserInfoUtil.getCurrentUserId()){
				Intent intent = new Intent(mContext,DynamicDetailActivity.class);
				intent.putExtra(Params.DYNAMIC_DETAIL.AID, item.getAid());
				intent.putExtra(Params.DYNAMIC_DETAIL.UID, item.getUid());
				intent.putExtra(Params.DYNAMIC_DETAIL.POSITION, position);
				mContext.startActivity(intent);
			}else{
				ProgressBar pb = (ProgressBar)v.getTag(R.id.dynamic_item_comment_item_pb);
				RelativeLayout rl = (RelativeLayout)v.getTag(R.id.dynamic_item_comment_rl);
				TextView emptyTV = (TextView)v.getTag(R.id.dynamic_item_comment_item_empty_tv);
				if(rl.isShown()){
					rl.setVisibility(View.GONE);
					item.setOpen(false);
				}else{
					rl.setVisibility(View.VISIBLE);
					if(position == mData.size() - 1){
						mListView.setSelection(position);
					}
					item.setOpen(true);
					if (TextUtils.isEmpty(item.getComment().toString())) {
						getComments(item.getAid(), position);
						pb.setVisibility(View.VISIBLE);
						emptyTV.setVisibility(View.GONE);
					}
				}
			}
			break;
		case R.id.dynamic_comment_item_pack_up_ibtn:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_GONE_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_GONE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			item.setOpen(false);
			this.notifyDataSetChanged();
			break;
		case R.id.dynamic_item_support_btn:
			BehaviorInfo behaviorInfo1 = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_ASSIST_ID,
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_ASSIST_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo1);
			
			behaviorInfo1 = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_TOP_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_TOP_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo1);

			if (item.getSupported() == Contants.SUPPORT.YES) {
				item.setSupported( Contants.SUPPORT.NO);
				v.setBackgroundResource(R.drawable.dynamic_support_btn_xbg);
				support(item.getAid(), position, 1);
			} else {
				item.setSupported( Contants.SUPPORT.YES);
				v.setBackgroundResource(R.drawable.btn_supported_bg);
				support(item.getAid(), position, 0);
			}
			break;
		case R.id.dynamic_comment_item_comment_ibtn:
			BehaviorInfo behaviorInfo3 = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_COMMENT_ID,
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_COMMENT_ID);
			CYSystemLogUtil.behaviorLog(behaviorInfo3);
			behaviorInfo3 = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_COMMENT_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_COMMENT_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo3);
			Message msg = mHandler.obtainMessage();
			msg.what = 0;
			msg.arg1 = position;
			mHandler.sendMessage(msg);
			break;
		case R.id.dynamic_item_game_ll:
			if(item.getGame() == null){
				Toast.makeText(mContext, R.string.failed_get_data, Toast.LENGTH_SHORT).show();
				return;
			}
			BehaviorInfo behaviorInfo2 = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_GAMEDETIAL_ID,
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_GAMEDETIAL_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo2);
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_DETAIL_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_DETAIL_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent iIntro = new Intent(mContext, GameCircleDetailActivity.class);
			iIntro.putExtra(Params.INTRO.GAME_CODE, item.getGame().getGamecode());
			iIntro.putExtra(Params.INTRO.GAME_NAME, item.getGame().getGamenm());
			if(item.getType() == Contants.DYNAMIC_TYPE.GAME_CIRCLE){
				iIntro.putExtra(Params.INTRO.GAME_CIRCLE, true);
			}
			else {
				iIntro.putExtra(Params.INTRO.GAME_CIRCLE, false);
			}
			mContext.startActivity(iIntro);
			break;
		case R.id.dynamic_item_capture_iv:
			Intent intent = new Intent();
			intent.setClass(mContext, ShowPhotoActivity.class);
			intent.putExtra(Params.SHOW_PHOTO.PHOTO_TYPE,Params.SHOW_PHOTO.PHOTO_PIC);
			intent.putExtra(Params.PHOTO_URL, item.getPicture().getPath());
			intent.putExtra(Params.PHOTO_MIDDLE_URL, item.getPicturemiddle().getPath());
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取动态评论
	 * 
	 * @param aid
	 *            //动态id
	 */
	private void getComments(int aid, final int position) {
		final DynamicItem i = mData.get(position);
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		if (!TextUtils.isEmpty(i.getComment())) {
			params.put("cursor", String.valueOf(i.getLastcommentid()));
		}
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		MyHttpConnect.getInstance().post(HttpContants.NET.GET_DYNAMIC_COMMENT,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						i.setLoadingComments(true);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("get dynamic comment result = " + content);
						try {
							DynamicComment comment = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
									.readValue(
											content,
											new TypeReference<DynamicComment>() {
											});
							if (comment != null) {
								if (comment.getData() != null
										&& !comment.getData().isEmpty()) {
									DynamicItem item = mData.get(position);
									StringBuilder sb = item.getComment();
									for (DynamicCommentItem i : comment
											.getData()) {
										sb.append(Util.getDynamicComment(i.getNickname(), i.getComment(), i.getUid()));
									}
									item.setComment(sb);
									item.setLastcommentid(comment.getData()
											.get(0).getCid());
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
						i.setLoadingComments(false);
						DynamicAdapter.this.notifyDataSetChanged();
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mContext);
						dialog.create().show();
					}
				});
	}

	private void support(final int aid, final int position, final int cancel) {
		if(mSupporting){
			return;
		}
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		if(cancel == 1){
			params.put("cancel", "1");
		}
		MyHttpConnect.getInstance().post(HttpContants.NET.SUPPORT_DYNAMIC,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						mSupporting = true;
					}

					@Override
					public void onFailure(Throwable error, String content) {
						mSupporting = false;
						Config.needResendSupport = true;
						try {
							relCache.saveFailedSupport(aid,cancel);
							if(cancel == 0)
								Toast.makeText(mContext, mContext.getString(R.string.support_failed_comments), Toast.LENGTH_SHORT).show();
							else
								Toast.makeText(mContext, mContext.getString(R.string.cancel_support_failed_comments), Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							mSupporting = false;
							return;
						}
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
									DynamicItem item = mData.get(position);
									if (cancel == 0)
										item.setSupported(Contants.SUPPORT.YES);
									else {
										item.setSupported(Contants.SUPPORT.NO);
									}
									DynamicAdapter.this.notifyDataSetChanged();
									if (cancel == 0)
										Toast.makeText(
												mContext,
												mContext.getString(R.string.support_success),
												Toast.LENGTH_SHORT).show();
									else
										Toast.makeText(
												mContext,
												mContext.getString(R.string.cancel_support_success),
												Toast.LENGTH_SHORT).show();

									try {
										Intent intent = new Intent(
												Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO);
										intent.putExtra(
												Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,
												item.getAid());
										intent.putExtra(
												Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_SUPPORT,
												cancel);
										intent.putExtra(
												Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,
												0); // 0：我的动态， 1：广场 2： 游戏圈
										mContext.sendBroadcast(intent);
									} catch (Exception e) {
										// TODO: handle exception
									}
								}
								
							}
							mSupporting = false;
						} catch (Exception e) {
							log.e(e);
						}
						mSupporting = false;
					}
					
					@Override
					public void onLoginOut() {
						mSupporting = false;
						LoginOutDialog dialog = new LoginOutDialog(mContext);
						dialog.create().show();
					}
				});
	}
}
