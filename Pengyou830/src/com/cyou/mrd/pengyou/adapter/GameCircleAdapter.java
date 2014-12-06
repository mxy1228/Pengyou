package com.cyou.mrd.pengyou.adapter;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.GameDynamicDao;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.entity.GameDynamicInfoItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.service.SendGameDynamicThread;
import com.cyou.mrd.pengyou.ui.FriendInfoActivity;
import com.cyou.mrd.pengyou.ui.ShowLargePhotoActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.RelationCircleViewCache;

public class GameCircleAdapter extends RelationshipBaseAdapter implements OnClickListener{
	
	public GameCircleAdapter(Activity context,List<DynamicItem> data,Handler handler){
		super(context, data, handler);
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
		RelationCircleViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.relationship_circle_item, null);
			viewCache = new RelationCircleViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (RelationCircleViewCache)convertView.getTag();
		}
		DynamicItem item = mData.get(position);
		if(item != null){
			switch (item.getType()) {
			case Contants.DYNAMIC_TYPE.SHARE_GAME:
				viewCache.getmRelScoredisplayLayout().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.GONE);
				//游戏圈不再显示该游戏的图标，名称
				viewCache.getmGameRL().setVisibility(View.GONE);
				viewCache.getmTypeTV().setText(mContext.getString(R.string.dynamic_hint,""));

				if(item.getGame()!= null){
				viewCache.getmGameType().setText(item.getGame().getGametype());
				if(item.getGame().getPlatform() == 2){
				  viewCache.getmGameIconFrom().setVisibility(View.GONE);
				}
				else{
				  viewCache.getmGameIconFrom().setVisibility(View.VISIBLE);
				}
				
				CYImageLoader.displayIconImage(item.getGame().getGameicon(), viewCache.getmGameIconIV(), mIconOption);
				viewCache.getmGameNameTV().setText(item.getGame().getGamenm());

				viewCache.getmPlayerCountTV().setVisibility(View.VISIBLE);
				viewCache.getmPlayerCountTV().setText(mContext.getString(R.string.play_count, item.getGame().getPlaynum()));
				}
				viewCache.getmDynamicFailBtn().setVisibility(View.GONE);
				break;
			case Contants.DYNAMIC_TYPE.PUB_TEXT:
				viewCache.getmRelScoredisplayLayout().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.GONE);
				viewCache.getmGameRL().setVisibility(View.GONE);
				viewCache.getmTypeTV().setText(mContext.getString(R.string.dynamic_hint,""));
				viewCache.getmDynamicFailBtn().setVisibility(View.GONE);
				break;
			case Contants.DYNAMIC_TYPE.PUB_PIC:
				viewCache.getmRelScoredisplayLayout().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.VISIBLE);
				viewCache.getmGameRL().setVisibility(View.GONE);

				if (item.getPicturemiddle() != null) {
					LayoutParams  params  = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					viewCache.getmCaptureIV().setLayoutParams(params);
					CYImageLoader.displayImg(item.getPicturemiddle().getPath(), viewCache.getmCaptureIV(), mCaptrueOption);
				}
				viewCache.getmTypeTV().setText(mContext.getString(R.string.dynamic_hint,""));
				viewCache.getmCaptureIV().setOnClickListener(this);
				viewCache.getmCaptureIV().setTag(position);
				viewCache.getmCaptureIV().setOnTouchListener(Util.onTouchCaptureListener);
				viewCache.getmDynamicFailBtn().setVisibility(View.GONE);
				break;
			case Contants.DYNAMIC_TYPE.MY_DYNAMIC:
				viewCache.getmRelScoredisplayLayout().setVisibility(View.GONE);
				viewCache.getmGameRL().setVisibility(View.GONE);
				if (item.getMyPicture() != null && !"".equals(item.getMyPicture())) {
					// bug 6094 自己发布的图片要限制显示尺寸
					if(item.getPicture()!= null){
						if(item.getPicture().getHeight() > item.getPicture().getWidth()){
							int  height = item.getPicture().getHeight();
							int  width = item.getPicture().getWidth();
							if(Config.screenWidthWithDip > 0 && height > 0 && width > 0){
								int mPicHeight = (Config.screenWidthWithDip - 70)*3/5;
								int mPicWidth = (mPicHeight*width)/height;
								LayoutParams  params  = new LayoutParams((int)(mPicWidth*Config.SreenDensity)
										, (int)(mPicHeight*Config.SreenDensity));
								params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
								params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
								viewCache.getmCaptureIV().setLayoutParams(params);
							}
						}else{
							int  height = item.getPicture().getHeight();
							int  width =  item.getPicture().getWidth();
							if(Config.screenWidthWithDip > 0  && height > 0 && width > 0){
								int mPicWidth = (Config.screenWidthWithDip - 70)*3/5;
								int mPicHeight = (mPicWidth*height)/width;
								LayoutParams  params  = new LayoutParams((int)(mPicWidth*Config.SreenDensity)
										, (int)(mPicHeight*Config.SreenDensity));							
								params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
								params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.padding_left_right);
								viewCache.getmCaptureIV().setLayoutParams(params);
						   }
						}
						
					}
					viewCache.getmCaptureIV().setVisibility(View.VISIBLE);
					viewCache.getmCaptureIV().setOnTouchListener(Util.onTouchCaptureListener);
					viewCache.getmCaptureIV().setOnClickListener(this);
					viewCache.getmCaptureIV().setTag(position);
					CYImageLoader.displayImgFromSDCard(item.getMyPicture(), viewCache.getmCaptureIV(), mCaptrueOption);
				}else {
					viewCache.getmCaptureIV().setVisibility(View.GONE);
				}
				String star = item.getStar();
				if (star != null && !star.equals("") && !star.equals("0.0")) {
					viewCache.getmRelScoredisplayLayout().setVisibility(View.VISIBLE);
					viewCache.getmRelScoreRatingBar().setRating(
							Float.valueOf(mData.get(position).getStar()));
					viewCache.getmRelScoreRatingBar().setIsIndicator(true);

				} else {
					viewCache.getmRelScoredisplayLayout().setVisibility(View.GONE);
				}
				
				viewCache.getmTypeTV().setText(mContext.getString(R.string.dynamic_hint,""));
				viewCache.getmDynamicFailBtn().setOnClickListener(this);
				viewCache.getmDynamicFailBtn().setTag(position);
               break;
			case Contants.DYNAMIC_TYPE.GAME_CIRCLE:
				viewCache.getmDynamicFailBtn().setVisibility(View.GONE);
				viewCache.getmGameRL().setVisibility(View.GONE);
				if (item.getPicturemiddle() != null) {
					LayoutParams  params  = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					viewCache.getmCaptureIV().setLayoutParams(params);
					CYImageLoader.displayImg(item.getPicturemiddle().getPath(), viewCache.getmCaptureIV(), mCaptrueOption);
					viewCache.getmCaptureIV().setVisibility(View.VISIBLE);
					viewCache.getmCaptureIV().setOnClickListener(this);
					viewCache.getmCaptureIV().setTag(position);
					viewCache.getmCaptureIV().setOnTouchListener(Util.onTouchCaptureListener);
				}
				else {
				    viewCache.getmCaptureIV().setVisibility(View.GONE);
				}

				viewCache.getmTypeTV().setText(mContext.getString(R.string.dynamic_hint,""));
				String score = item.getStar();
				if (score != null && !score.equals("")) {
					viewCache.getmRelScoredisplayLayout().setVisibility(View.VISIBLE);
					viewCache.getmRelScoreRatingBar().setRating(
							Float.valueOf(mData.get(position).getStar()));
					viewCache.getmRelScoreRatingBar().setIsIndicator(true);
				} else {
					viewCache.getmRelScoredisplayLayout().setVisibility(View.GONE);
				}

				break;
			default:
				viewCache.getmRelScoredisplayLayout().setVisibility(View.GONE);
				viewCache.getmGameRL().setVisibility(View.GONE);
				viewCache.getmCaptureIV().setVisibility(View.GONE);
				viewCache.getmContentTV().setVisibility(View.GONE);
				break;
			}
			//置顶功能
			if(item.getCursor() < 0){
				viewCache.getmDynamicTopBtn().setVisibility(View.VISIBLE);
			}
			else {
				viewCache.getmDynamicTopBtn().setVisibility(View.GONE);
		    }
				
			CYImageLoader.displayImg(item.getAvatar(), viewCache.getmAvatarIV().getImageView(), mAvatarOption);
			viewCache.getmNickNameTV().setText(item.getNickname());
			if(item.getGender() == 2){
				viewCache.getmNickNameTV().setCompoundDrawablesWithIntrinsicBounds(null
						, null
						, mContext.getResources().getDrawable(R.drawable.girl_sign)
						, null );
			}else {
				viewCache.getmNickNameTV().setCompoundDrawablesWithIntrinsicBounds(null
						, null
						, mContext.getResources().getDrawable(R.drawable.boy_sign)
						, null );
			}

			if (item.getText() != null && !"".equals(item.getText().trim())) {
				if(item.getText().toCharArray().length > Config.dynamicCharCountLimit){
					viewCache.getmContentALL().setTag(R.id.relationship_circle_item_content_tv, viewCache.getmContentTV());
					viewCache.getmContentALL().setTag(position);
					viewCache.getmContentALL().setOnClickListener(this);
					viewCache.getmContentALL().setVisibility(View.VISIBLE);
					if(item.isNeedPullDown()){
						String tempStr = item.getText().subSequence(0, Config.dynamicCharShowLimit).toString() + "...";
						viewCache.getmContentTV().setText(tempStr);
						viewCache.getmContentALL().setText(mContext.getString(R.string.see_all_text));
					}
					else {
						viewCache.getmContentTV().setText(item.getText());
						viewCache.getmContentALL().setText(mContext.getString(R.string.pack_up));
					}
				}else{
					viewCache.getmContentALL().setVisibility(View.GONE);
					viewCache.getmContentTV().setText(item.getText());
				}
				viewCache.getmContentTV().setVisibility(View.VISIBLE);
			}else{
				viewCache.getmContentALL().setVisibility(View.GONE);
				if(item.getType() == Contants.DYNAMIC_TYPE.PUB_TEXT){
					viewCache.getmContentTV().setVisibility(View.VISIBLE);
					viewCache.getmContentTV().setText("");
				}
				else {
					viewCache.getmContentTV().setVisibility(View.GONE);
				}
			}

			if(item.isOpen()){
				viewCache.getmCommentRL().setVisibility(View.VISIBLE);
			}else{
				viewCache.getmCommentRL().setVisibility(View.GONE);
			}
			if(item.getCommentcnt() < 0){
				item.setCommentcnt(0);
			}
			if(item.getSupportcnt() < 0){
				item.setSupportcnt(0);
			}
			if(item.getCommentcnt() ==0 && item.getSupportcnt() == 0){
				viewCache.getmCommentLayout().setVisibility(View.GONE);
				viewCache.getmPackUpBtn().setVisibility(View.GONE);
			}
			else if(item.getSupportcnt() > 0 && item.getCommentcnt() == 0){
				viewCache.getmCommentLayout().setVisibility(View.VISIBLE);
				viewCache.getmCommentTitleLayout().setVisibility(View.VISIBLE);
				viewCache.getmOpenCommentBtn().setVisibility(View.GONE);
				viewCache.getmPackUpBtn().setVisibility(View.GONE);
				Drawable  drawable  = mContext.getResources().getDrawable(R.drawable.supported_icon);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
				SpannableString spanStr = new SpannableString(" ");
				spanStr.setSpan(new ImageSpan(drawable), spanStr.length() - 1, spanStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE); 
				spannableStringBuilder.append(spanStr);
				spannableStringBuilder.append(" ");
				CharSequence  suppCharSequence = "";
				if(item.getSupportcnt()<=9999){
				   suppCharSequence = mContext.getString(R.string.number_feel_support, item.getSupportcnt());
			    }else{
			       suppCharSequence = mContext.getString(R.string.number_feel_support, "9999+");
			    }
				spannableStringBuilder.append( new SpannableString(suppCharSequence));
				viewCache.getmSupportText().setText(spannableStringBuilder);
			}
			else if(item.getCommentcnt() > 0 && item.getSupportcnt() == 0){
				viewCache.getmCommentLayout().setVisibility(View.VISIBLE);
				if(item.isOpen()){
					viewCache.getmCommentTitleLayout().setVisibility(View.GONE);
					viewCache.getmPackUpBtn().setVisibility(View.VISIBLE);
				}
				else{
					viewCache.getmCommentTitleLayout().setVisibility(View.VISIBLE);
					viewCache.getmOpenCommentBtn().setVisibility(View.VISIBLE);
					viewCache.getmPackUpBtn().setVisibility(View.GONE);
					if(item.getCommentcnt()<=9999){
						viewCache.getmSupportText().setText(mContext.getString(R.string.people_give_comment, item.getCommentcnt()));
					}else{
						viewCache.getmSupportText().setText(mContext.getString(R.string.people_give_comment, "9999+"));
				    }
				}	
			}
			else {
				viewCache.getmCommentLayout().setVisibility(View.VISIBLE);
				viewCache.getmCommentTitleLayout().setVisibility(View.VISIBLE);
				Drawable  drawable  = mContext.getResources().getDrawable(R.drawable.supported_icon);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
				SpannableString spanStr = new SpannableString(" ");
				spanStr.setSpan(new ImageSpan(drawable), spanStr.length() - 1, spanStr.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE); 
				spannableStringBuilder.append(spanStr);
				spannableStringBuilder.append(" ");
				CharSequence  suppCharSequence = "";
				if(item.getCommentcnt()>9999  && item.getSupportcnt()> 9999){
					 suppCharSequence = mContext.getString(R.string.number_feel_support, "9999+")+ " , " + mContext.getString(R.string.people_give_comment, "9999+");
				}
				else if(item.getSupportcnt()>9999){
					 suppCharSequence = mContext.getString(R.string.number_feel_support, "9999+") + " , " +  mContext.getString(R.string.people_give_comment, item.getCommentcnt() );
				}
				else if(item.getCommentcnt()>9999){
					 suppCharSequence = mContext.getString(R.string.number_feel_support, item.getSupportcnt())+ " , " + mContext.getString(R.string.people_give_comment,"9999+");
				}
				else{
					 suppCharSequence = mContext.getString(R.string.number_feel_support, item.getSupportcnt()) + " , "   + mContext.getString(R.string.people_give_comment, item.getCommentcnt());
				}
				spannableStringBuilder.append( new SpannableString(suppCharSequence));
				viewCache.getmSupportText().setText(spannableStringBuilder);
				
				if(item.isOpen()){
					viewCache.getmOpenCommentBtn().setVisibility(View.GONE);
					viewCache.getmPackUpBtn().setVisibility(View.VISIBLE);
				}
				else{
					viewCache.getmOpenCommentBtn().setVisibility(View.VISIBLE);
					viewCache.getmPackUpBtn().setVisibility(View.GONE);
				}
				
			}
			if(viewCache.getmPB().getVisibility() == View.VISIBLE){
				viewCache.getmPB().setVisibility(View.GONE);
			}

			viewCache.getmDateTV().setText(Util.getFormatDate(item.getCreatedtime()));
			viewCache.getmSupportBtn().setOnClickListener(this);
			viewCache.getmSupportBtn().setTag(position);
			viewCache.getmCommentIBtn().setOnClickListener(this);
			viewCache.getmCommentIBtn().setTag(position);
			if(viewCache.getmOpenCommentBtn().getVisibility() == View.VISIBLE){
				viewCache.getmCommentTitleLayout().setTag(position);
				viewCache.getmCommentTitleLayout().setOnClickListener(this);
				viewCache.getmCommentTitleLayout().setClickable(true);
				viewCache.getmCommentTitleLayout().setTag(
						R.id.relationship_circle_item_comment_item_pb,
						viewCache.getmPB());
				viewCache.getmCommentTitleLayout().setTag(R.id.relationship_circle_item_comment_rl, viewCache.getmCommentRL());
				viewCache.getmCommentTitleLayout().setTag(R.id.relation_circle_comment_seg_line,viewCache.getmOpenCommentBtn());
				viewCache.getmCommentTitleLayout().setTag(R.id.relation_circle_dot,viewCache.getmPackUpBtn());
			}
			else{
				viewCache.getmCommentTitleLayout().setClickable(false);
			}
			viewCache.getmEmptyTV().setVisibility(View.GONE);
			LinearLayout ly = (LinearLayout)viewCache.getmCommentRL();

			if (item.getSubCommentData().size() > 0 && item.isOpen()) {
				ly.removeAllViews();
				for (int i = item.getSubCommentData().size()-1; i >=0; i--) {
					TextView tView = new TextView(mContext);
					tView.setLayoutParams(LP_FW);
					tView.setTag(R.id.relation_circle_message_empty,item.getSubCommentData().get(i));
					tView.setTag(R.id.relation_circle_dot,Integer.valueOf(position));
					tView.setTag(R.id.relation_circle_message_lv,Integer.valueOf(2));
					tView.setBackgroundResource(R.drawable.dynamic_comment_item_xbg);
					tView.setOnClickListener(ClickListener);
				
					String reNameString = null;
					int reUid = 0;
					if(item.getSubCommentData().get(i).getReplyto()!= null){
						DynamicCommentReplyItem reply =item.getSubCommentData().get(i).getReplyto();
						reNameString = reply.getNickname();
						reUid = reply.getUid();
					}
					String  nickName = item.getSubCommentData().get(i).getNickname();
					int uid = item.getSubCommentData().get(i).getUid();
					SpannableString  spstr;
					if(reNameString != null){
						spstr = getReplyUser(nickName, uid, reNameString, reUid, item.getSubCommentData().get(i).getComment());
						spstr.setSpan(new ClickSpan(uid), 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						spstr.setSpan(new ClickSpan(reUid), nickName.length() + 2, nickName.length() + 2 + reNameString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						tView.setText(spstr);
					}
					else {
						spstr = getSingleUser(nickName, uid, item.getSubCommentData().get(i).getComment());
						spstr.setSpan(new ClickSpan(uid), 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						tView.setText(spstr);
					}					
					tView.setMovementMethod(LinkMovementMethod.getInstance());
					tView.setHighlightColor(Color.parseColor("#c2cedf"));
//					Util.initComment(tView);
					ly.addView(tView);
				}
			}
			else {
				ly.removeAllViews();
				if(item.isOpen() && item.isCommentUpdate()){
					viewCache.getmEmptyTV().setVisibility(View.VISIBLE);
				}
			}
			//相互关注
			if(item.getBilateral() == Contants.EACH_FOCUS.YES){
				viewCache.getmEachFocusIV().setVisibility(View.VISIBLE);
			}else{
				viewCache.getmEachFocusIV().setVisibility(View.GONE);
			}
			 //新浪微博关注
			if(item.getSnstag() == Contants.SNS_TAG.SINA){
				viewCache.getmSNSIV().setVisibility(View.VISIBLE);
			}
			else {
				viewCache.getmSNSIV().setVisibility(View.GONE);
			}
			
			switch (item.getSendStatus()) {
			//tzh add 发送失败或正在发送的动态不准点赞和评论
			case Contants.SEND_DYNAMIC_STATUS.SENDING:
				viewCache.getmSupportBtn().setClickable(false);
				viewCache.getmCommentIBtn().setClickable(false);
				viewCache.getmSupportBtn().setBackgroundResource(R.drawable.support_disable);
				viewCache.getmCommentIBtn().setBackgroundResource(R.drawable.comment_disable);
				viewCache.getmDynamicFailBtn().setVisibility(View.GONE);
				viewCache.getmCommentLayout().setVisibility(View.GONE);
				break;
			case Contants.SEND_DYNAMIC_STATUS.FAIL:
				viewCache.getmSupportBtn().setClickable(false);
				viewCache.getmCommentIBtn().setClickable(false);
				viewCache.getmSupportBtn().setBackgroundResource(R.drawable.support_disable);
				viewCache.getmCommentIBtn().setBackgroundResource(R.drawable.comment_disable);
				viewCache.getmDynamicFailBtn().setVisibility(View.VISIBLE);
				viewCache.getmDynamicFailBtn().setOnClickListener(this);
				viewCache.getmCommentLayout().setVisibility(View.GONE);
				break;
			case Contants.SEND_DYNAMIC_STATUS.SUCCESS:
				viewCache.getmSupportBtn().setClickable(true);
				viewCache.getmCommentIBtn().setClickable(true);
				if(item.getSupported() == Contants.SUPPORT.YES){
					viewCache.getmSupportBtn().setBackgroundResource(R.drawable.btn_supported_bg);
				}else{
					viewCache.getmSupportBtn().setBackgroundResource(R.drawable.dynamic_support_btn_xbg);
				}
				if(item.getCommentcnt() > 0){
					viewCache.getmCommentIBtn().setBackgroundResource(R.drawable.commented);
				}
				else{
					viewCache.getmCommentIBtn().setBackgroundResource(R.drawable.dynamic_comment_btn_xbg);
				}
				viewCache.getmDynamicFailBtn().setVisibility(View.GONE);
				break;
			default:
				viewCache.getmSupportBtn().setClickable(true);
				viewCache.getmCommentIBtn().setClickable(true);
				viewCache.getmDynamicFailBtn().setVisibility(View.GONE);
				if(item.getSupported() == Contants.SUPPORT.YES){
					viewCache.getmSupportBtn().setBackgroundResource(R.drawable.btn_supported_bg);
				}else{
					viewCache.getmSupportBtn().setBackgroundResource(R.drawable.dynamic_support_btn_xbg);
				}
				if(item.getCommentcnt() > 0){
					viewCache.getmCommentIBtn().setBackgroundResource(R.drawable.commented);
				}
				else{
					viewCache.getmCommentIBtn().setBackgroundResource(R.drawable.dynamic_comment_btn_xbg);
				}
				break;
			}

			if(item.getCommentcnt() != 0 && item.getSupportcnt() != 0 && item.isOpen()){
				viewCache.getmCommentSegLine().setVisibility(View.VISIBLE);
			}
			else{
			   viewCache.getmCommentSegLine().setVisibility(View.GONE);
			}
			
			viewCache.getmAvatarIV().setOnClickListener(this);
			viewCache.getmAvatarIV().setTag(position);
			viewCache.getmGameRL().setOnClickListener(this);
			viewCache.getmGameRL().setTag(position);
			viewCache.getmNickNameTV().setOnClickListener(this);
			viewCache.getmNickNameTV().setTag(position);
            if (viewCache.getmPackUpBtn().getVisibility() == View.VISIBLE) {
				viewCache.getmPackUpBtn().setOnClickListener(this);
				viewCache.getmPackUpBtn().setTag(position);
			}			
		}
		return convertView;
	}
	
//	private void support(final int aid, final int position, final int cancel) {
//		if(mSupporting){
//			return;
//		}
//		RequestParams params = new RequestParams();
//		params.put("aid", String.valueOf(aid));
//		if(cancel == 1){
//			params.put("cancel", "1");
//		}
//		MyHttpConnect.getInstance().post(HttpContants.NET.SUPPORT_DYNAMIC,
//				params, new AsyncHttpResponseHandler() {
//					@Override
//					public void onStart() {
//						mSupporting = true;
//					}
//					
//					@Override
//					public void onFailure(Throwable error, String content) {
//						mSupporting = false;
//						Config.needResendSupport = true;
//						relCache.saveFailedSupport(aid,cancel);	
//						if(cancel == 0){
//							mData.get(position).setSupportcnt(mData.get(position).getSupportcnt() + 1);
//						    Toast.makeText(mContext, mContext.getString(R.string.support_failed_comments), Toast.LENGTH_SHORT).show();
//						}
//						else{
//							mData.get(position).setSupportcnt(mData.get(position).getSupportcnt() - 1);
//						    Toast.makeText(mContext, mContext.getString(R.string.cancel_support_failed_comments), Toast.LENGTH_SHORT).show();
//						}
//						GameCircleAdapter.this.notifyDataSetChanged();
//					}
//
//					@Override
//					public void onLoginOut() {
//						LoginOutDialog dialog = new LoginOutDialog(mContext);
//						dialog.create().show();
//					}
//					
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						super.onSuccess(statusCode, content);
//						if (TextUtils.isEmpty(content)) {
//							mSupporting = false;
//							return;
//						}
//						try {
//							JSONObject obj = new JSONObject(content);
//							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
//								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
//									if (cancel == 0) {
//										mData.get(position).setSupportcnt(
//												mData.get(position)
//														.getSupportcnt() + 1);
//										mData.get(position).setSupported(
//												Contants.SUPPORT.YES);
//									} else {
//										mData.get(position).setSupportcnt(
//												mData.get(position)
//														.getSupportcnt() - 1);
//										mData.get(position).setSupported(
//												Contants.SUPPORT.NO);
//									}
//									GameCircleAdapter.this
//											.notifyDataSetChanged();
//									try {
//										Intent intent = new Intent(
//												Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO);
//										intent.putExtra(
//												Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,
//												mData.get(position).getAid());
//										intent.putExtra(
//												Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_SUPPORT,
//												cancel);
//										intent.putExtra(
//												Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,
//												2); // 0：我的动态， 1：广场 2： 游戏圈
//										mContext.sendBroadcast(intent);
//									} catch (Exception e) {
//										// TODO: handle exception
//									}
//									if (cancel == 0)
//										Toast.makeText(
//												mContext,
//												mContext.getString(R.string.support_success),
//												Toast.LENGTH_SHORT).show();
//									else
//										Toast.makeText(mContext, mContext.getString(R.string.cancel_support_success), Toast.LENGTH_SHORT).show();
//								}
//								else {
//									String errorNo = JsonUtils.getJsonValue(
//											content, "errorNo");
//	        						if (!TextUtils.isEmpty(errorNo))
//	        						{
//	        							if(errorNo.equals(Contants.ERROR_NO.ERROR_MASK_WORD_STRING)){
//	        								Toast.makeText(mContext,R.string.comment_maskword_failed, Toast.LENGTH_SHORT).show();
//	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_NOT_EXIST_STRING)){
//	        								Toast.makeText(mContext,R.string.comment_deleted_send_failed, Toast.LENGTH_SHORT).show();
//	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_OPERATION_STRING)){
//	        								Toast.makeText(mContext,R.string.comment_operation_send_failed, Toast.LENGTH_SHORT).show();
//	        							}else {
//	        								Toast.makeText(mContext,R.string.comment_send_failed, Toast.LENGTH_SHORT).show();
//	        							}
//	        						}
//	        						if (cancel == 0) {
//										mData.get(position).setSupported(
//												Contants.SUPPORT.NO);
//									} else {
//										mData.get(position).setSupported(
//												Contants.SUPPORT.YES);
//									}
//	        						GameCircleAdapter.this
//									.notifyDataSetChanged();
//								}
//							}
//						} catch (Exception e) {
//							log.e(e);
//						}
//						mSupporting = false;
//					}
//				});
//	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		int position = Integer.parseInt(v.getTag().toString());
		DynamicItem item = mData.get(position);
		if(item == null){
			Toast.makeText(mContext, R.string.wating_for_get_data, Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		case R.id.relationship_circle_item_support_btn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.RELATION.BTN_RELATION_TOP_ID,
					CYSystemLogUtil.RELATION.BTN_RELATION_TOP_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			log.d(" OnClicked  item.getSupported()==" + item.getSupported());
			if(item.getSupported() != Contants.SUPPORT.YES){
				item.setSupported( Contants.SUPPORT.YES);
				v.setBackgroundResource(R.drawable.btn_supported_bg);
//				support(item.getAid(), position , 0 ,2);
				Message msg = mHandler.obtainMessage();
				msg.what = 9;
				msg.arg1 = position;
				msg.arg2 = item.getAid();
				mHandler.sendMessage(msg);
			}
			else{
				item.setSupported( Contants.SUPPORT.NO);
				v.setBackgroundResource(R.drawable.dynamic_support_btn_xbg);
//				support(item.getAid(), position , 1, 2);
				Message msg = mHandler.obtainMessage();
				msg.what = 10;
				msg.arg1 = position;
				msg.arg2 = item.getAid();
				mHandler.sendMessage(msg);
			}
			break;
		case R.id.relationship_circle_item_comment_head:
			 behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.RELATION.BTN_RELATION_COMMENT_ID,
					CYSystemLogUtil.RELATION.BTN_RELATION_COMMENT_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			LinearLayout rl = (LinearLayout)v.getTag(R.id.relationship_circle_item_comment_rl);
			ProgressBar pb = (ProgressBar)v.getTag(R.id.relationship_circle_item_comment_item_pb);
            ((Button)v.getTag(R.id.relation_circle_comment_seg_line)).setVisibility(View.GONE);
            if(rl.isShown()){
			}else{
				rl.setVisibility(View.VISIBLE);
				((Button)v.getTag(R.id.relation_circle_dot)).setVisibility(View.VISIBLE);
				item.setOpen(true);
				pb.setVisibility(View.VISIBLE);
//				getComments(item.getAid(), position, 2);
				Message msg = mHandler.obtainMessage();
				msg.what = 5;
				msg.arg1 = position;
				msg.arg2 = item.getAid();
				mHandler.sendMessage(msg);
			}

			break;
		case R.id.relationship_circle_item_pack_up_btn:
			 behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.RELATION.BTN_RELATION_STOP_ID,
					CYSystemLogUtil.RELATION.BTN_RELATION_STOP_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			item.setOpen(false);
			GameCircleAdapter.this.notifyDataSetChanged();
			break;
		case R.id.relationship_circle_item_comment_btn:
			Message msg = mHandler.obtainMessage();
			msg.what = 1;
			msg.arg1 = position;
			msg.arg2 = this.getCount();//点击评论时ListView的Item总量
			msg.obj = null;
			mHandler.sendMessage(msg);
			break;
		case R.id.relationship_circle_item_avatar:
		case R.id.relationship_cricle_item_nickanme_tv:
			intent.setClass(mContext, FriendInfoActivity.class);
			intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
			intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
			mContext.startActivity(intent);
			break;
		case R.id.relationship_circle_item_game_rl:
			break;
		case R.id.relationship_circle_item_capture_iv:
			 behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.RELATION.BTN_RELATION_PIC_DETAIL_ID,
					CYSystemLogUtil.RELATION.BTN_RELATION_PIC_DETAIL_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			intent.putExtra(Params.SHOW_PHOTO.PHOTO_TYPE,Params.SHOW_PHOTO.PHOTO_PIC);
			intent.setClass(mContext, ShowLargePhotoActivity.class);
			if (item.getMyPicture() != null && !"".equals(item.getMyPicture()) ) {
				intent.putExtra(Params.PHOTO_URL, item.getMyPicture());
			}else {
				intent.putExtra(Params.PHOTO_MIDDLE_URL, item.getPicturemiddle().getPath());
				intent.putExtra(Params.PHOTO_URL, item.getPicture().getPath());
				
			}
			mContext.startActivity(intent);
			break;

		//点击发送失败的警示按钮	
		case R.id.relationship_circle_item_send_fail:
			showRepublishSelector(item);
			break;
		case R.id.relationship_circle_item_content_tv1:
			 TextView  tView = (TextView)v.getTag(R.id.relationship_circle_item_content_tv);
			if(item.isNeedPullDown()){
				tView.setText(item.getText());
			    item.setNeedPullDown(false);
			    ((TextView)v).setText(mContext.getString(R.string.pack_up));
			}
			else {
				((TextView)v).setText(mContext.getString(R.string.see_all_text));
				tView.setText(item.getText().subSequence(0, Config.dynamicCharShowLimit).toString() + "...");
				item.setNeedPullDown(true);
			}
		default:
			break;
		}
	}
	/**
	 * tzh add 点击发送失败警示按钮弹窗
	 * @param item
	 */
    private void showRepublishSelector(final DynamicItem item) {
        try {
            mDialog = new AlertDialog.Builder(mContext)
                    .setItems(
                            new CharSequence[] {
                            		mContext.getString(R.string.republish_dynamic),
                            		mContext.getString(R.string.delete),
                            		mContext.getString(R.string.cancel) },
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                        int which) {
                                    switch (which) {
                                    case 0:
                                    	republish(item);
                                        break;
                                    case 1:
                                    	deleteDynamic (item);
                                        break;
                                    case 3:
                                    	mDialog.dismiss();
                                        break;
                                    default:
                                        break;
                                    }
                                }
                            }).create();
            mDialog.setTitle(null);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
        } catch (Exception e) {
            log.e(e);
        }
    }
    
//    public void showSelectorDelete(final DynamicCommentItem commentItem, final int pos) {
//        try {
//            mRepublishDialog = new AlertDialog.Builder(mContext)
//                    .setItems(
//                            new CharSequence[] {
//                            		mContext.getString(R.string.delete),
//                            		mContext.getString(R.string.cancel) },
//                            new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                        int which) {
//                                    switch (which) {
//                                    case 0:
//                                    	deleteComment(commentItem, pos);
//                                        break;
//                                    case 1:
//                                    	mRepublishDialog.dismiss();
//                                        break;
//                                    default:
//                                        break;
//                                    }
//                                }
//                            }).create();
//            mRepublishDialog.setTitle(null);
//            mRepublishDialog.setCanceledOnTouchOutside(true);
//            mRepublishDialog.show();
//        } catch (Exception e) {
//            log.e(e);
//        }
//    }
    
    /**
     * tzh add 重新发送
     * @param item
     */
    private void republish (DynamicItem item) {
    	if (item != null) {
    		GameDynamicDao mDao = new GameDynamicDao(this.mContext);
    		GameDynamicInfoItem  infoItem = mDao.getDynamicByPid(item.getPid());
    		String star = item.getStar();   		
    		if((star != null && !star.equals("") && !star.equals("0.0")) && UserInfoUtil.getGameScore(infoItem.getGamecode()) != 0){
    			Toast.makeText(mContext, R.string.game_gave_stars, Toast.LENGTH_SHORT).show();
    			return;
    		};
    		if(infoItem.getStatus() == Contants.SEND_DYNAMIC_STATUS.SENDING){
    			Toast.makeText(mContext, R.string.dynamic_sending, Toast.LENGTH_SHORT).show();
    			return;
    		};
    		if(infoItem.getStatus() == Contants.SEND_DYNAMIC_STATUS.SUCCESS){
    			Toast.makeText(mContext, R.string.dynamic_send_sucess, Toast.LENGTH_SHORT).show();
    			return;
    		};
    		item.setSendStatus(Contants.SEND_DYNAMIC_STATUS.SENDING);
    		notifyDataSetChanged();

//    		Long date = System.currentTimeMillis()/1000;
//    		infoItem.setContent(item.getText());
//    		infoItem.setDate(date);
//    		infoItem.setPicture(item.getMyPicture());
//    		infoItem.setStatus(Contants.SEND_DYNAMIC_STATUS.SENDING);
//    		infoItem.setPid(item.getPid());
//    		mDao.updateDate(Contants.SEND_DYNAMIC_STATUS.SENDING,date, item.getPid());
    		SendGameDynamicThread task = new SendGameDynamicThread(mContext, infoItem,infoItem.getGamecode(),infoItem.getGameCircleId(),infoItem.getScore());
    		sendDynamicRepublishBroadCast(item.getPid());
    		task.start();
    	}
    }
    
    private void sendDynamicRepublishBroadCast (int pid) {
    	Intent intent = new Intent(Contants.ACTION.SEND_GAMEDYNAMIC_REPUBLISH);
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, pid);
        log.d("pid1="+pid);
        mContext.sendBroadcast(intent);
    }
    
    /**
     * tzh add 删除发送失败的动态
     * @param item
     */
    private void deleteDynamic (DynamicItem item) {
    	try {
    		GameDynamicDao mDao = new GameDynamicDao(mContext);
    		Integer result = mDao.deleteByPid(item.getPid());
    		if (result > -1) {
    			this.mData.remove(item);
    			sendDynamicDeleteBroadCast(item.getPid());
    			this.notifyDataSetChanged();
    		}
			
		} catch (Exception e) {
			
		}
    }
    private void sendDynamicDeleteBroadCast (int pid) {
    	Intent intent = new Intent(Contants.ACTION.SEND_GAMEDYNAMIC_DELETE);
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, pid);
        log.d("pid1="+pid);
        mContext.sendBroadcast(intent);
    }
	/**
	 * 获取动态评论
	 * 
	 * @param aid
	 *            //动态id
	 */
//	private void getComments(int aid, final int position) {
//		final DynamicItem i = mData.get(position);
//		RequestParams params = new RequestParams();
//		params.put("aid", String.valueOf(aid));
//		if (!TextUtils.isEmpty(i.getComment())) {
//			params.put("cursor", String.valueOf(i.getLastcommentid()));
//		}
////		params.put("gamecode",i.getGame().getGamecode());
//		params.put("count", String.valueOf(Config.PAGE_SIZE));
//
//		MyHttpConnect.getInstance().post(HttpContants.NET.GET_DYNAMIC_COMMENT,
//				params, new AsyncHttpResponseHandler() {
//					@Override
//					public void onStart() {
//						i.setLoadingComments(true);
//					}
//
//					@Override
//					public void onFailure(Throwable error, String content) {
//						super.onFailure(error, content);
//	                    Toast.makeText(mContext, R.string.networks_available, Toast.LENGTH_SHORT).show();
//	                    i.setCommentUpdate(true);
//	                    GameCircleAdapter.this.notifyDataSetChanged();
//					}
//					
//					@Override
//					public void onLoginOut() {
//						LoginOutDialog dialog = new LoginOutDialog(mContext);
//						dialog.create().show();
//					}
//
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						super.onSuccess(statusCode, content);
//						log.i(" !!!!!!!!!!!!!  game circle  getComments result = " + content);
//						if (TextUtils.isEmpty(content)) {
//							i.setLoadingComments(false);
//							GameCircleAdapter.this.notifyDataSetChanged();
//							return;
//						}
//						try {
//							DynamicComment comment = new ObjectMapper()
//							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//									.readValue(
//											content,
//											new TypeReference<DynamicComment>() {
//											});
//							if (comment != null) {
//								if (comment.getData() != null
//										&& !comment.getData().isEmpty()) {
//									i.setSubCommentData(comment.getData());
//									i.setComment(Util.dynamicComment2Html(comment.getData()));
//									i.setLastcommentid(comment.getData()
//											.get(0).getCid());
//									i.setCommentcnt(comment.getData().size());
//									i.setCommentUpdate(false);
//								}
//								else{
//									i.setCommentcnt(0);
//								}
//							}
//							else {
//								i.setCommentcnt(0);
//							}
//						} catch (Exception e) {
//							log.e(e);
//						}
//						i.setLoadingComments(false);
//						GameCircleAdapter.this.notifyDataSetChanged();
//					}
//				});
//	}
	
//	public void  deleteComment(final DynamicCommentItem dynamicCommentItem, final int position){
//		final DynamicItem i = mData.get(position);
//		RequestParams params = new RequestParams();
//		log.i(" deleteComment  cid=  " + dynamicCommentItem.getCid());
//		if(dynamicCommentItem.getSendSuccess() == 1){
//			i.getSubCommentData().remove(dynamicCommentItem);
//			if(i.getCommentcnt() >0){
//                i.setCommentcnt(i.getCommentcnt() - 1);
//            }
//			RelationCircleCommentDao commentDao = new RelationCircleCommentDao(mContext);
//			commentDao.delete(i.getAid(), dynamicCommentItem.getCid(), 0, dynamicCommentItem.getTimestamp());
//			GameCircleAdapter.this.notifyDataSetChanged();
//			return;
//		}
//		params.put("cid", String.valueOf(dynamicCommentItem.getCid()));
//		MyHttpConnect.getInstance().post(HttpContants.NET.DELETE_COMMENT, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onStart() {
//						super.onStart();
//					}
//					
//					@Override
//					public void onFailure(Throwable error, String content) {
//						super.onFailure(error, content);
//	                    Toast.makeText(mContext,R.string.networks_available, Toast.LENGTH_SHORT).show();
//					}
//					
//					@Override
//					public void onLoginOut() {
//						LoginOutDialog dialog = new LoginOutDialog(mContext);
//						dialog.create().show();
//					}
//
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						super.onSuccess(statusCode, content);
//						if (TextUtils.isEmpty(content)) {
//							return;
//						}
//						log.i("send comment result = " + content);
//						try {
//							JSONObject obj = new JSONObject(content);
//							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
//								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
//									if (i == null) {
//										return;
//									}
//
//									for(int ii = 0; ii < i.getSubCommentData().size(); ii++){
//										if(i.getSubCommentData().get(ii).getCid()  == dynamicCommentItem.getCid()){
//											i.getSubCommentData().remove(ii);
//											if(i.getCommentcnt() > 0){
//			        							i.setCommentcnt(i.getCommentcnt() - 1);
//			                                }
//											Intent intent = new Intent(
//													Contants.ACTION.UPDATE_RELATION_COMMENT_INFO);
//											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,1); //0:评论 1：删除评论
//											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,2); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态， 4：关系圈
//											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,i.getAid());
//											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, dynamicCommentItem);
//											mContext.sendBroadcast(intent);
//											Toast.makeText(mContext, R.string.delete_success, Toast.LENGTH_SHORT).show();
//											break;
//										}
//									}
//									GameCircleAdapter.this.notifyDataSetChanged();
//								}
//							}
//						} catch (Exception e) {
//							log.e(e);
//						}
//					}
//				});
//	}

//    private Handler selectorHandler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case 1:
//				if(Config.clickedFlag){
//					Config.clickedFlag = false;
//					return;
//				} 
//				DynamicCommentItem dynamicCommentItem = (DynamicCommentItem)msg.obj;
//				int position = msg.arg1;
//				showSelectorDelete(dynamicCommentItem,position);
//				break;
//			case 2:
//				//((TextView)msg.obj).setBackgroundResource(R.drawable.dynamic_comment_item_xbg);
//			default:
//				break;
//			}
//		}
//	};

//	private boolean  mSpanNameClick = false;
//	private class  ClickSpan extends ClickableSpan implements OnClickListener{
//	    private int uid;
//		public ClickSpan(int uid){
//			this.uid = uid;
//		}
//			@Override
//			public void onClick(View v) {
//				log.i("--------------ClickSpan---------------- uid: " + uid);
//				mSpanNameClick = true;
//				Intent iFans = new Intent(mContext,
//						FriendInfoActivity.class);
//				iFans.putExtra(Params.FANS.UID, uid);
//				mContext.startActivity(iFans);
//				
//			}
//			@Override
//			public void updateDrawState(TextPaint ds) {
//				ds.setUnderlineText(false);
//			}
//			
//		}
		
//		private  OnClickListener ClickListener = new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(mSpanNameClick){
//					mSpanNameClick = false;
//					return;
//			   }
//			   DynamicCommentItem dyItem = (DynamicCommentItem)v.getTag(R.id.relation_circle_message_empty);
//			    int pos = ((Integer) v
//						.getTag(R.id.relation_circle_dot))
//						.intValue();
//			    log.i("-------------------ClickListener----------- pos: " + pos);
//			    log.i("-------------------ClickListener----------- dyItem.getUid(): " + dyItem.getUid());
//
//				if (dyItem.getUid() == UserInfoUtil
//						.getCurrentUserId()) {
//					if(mRepublishDialog != null && mRepublishDialog.isShowing()){
//						return;
//					}
//					Message msg = selectorHandler.obtainMessage();
//					msg.what = 1;
//					msg.arg1 = pos;
//					msg.obj = dyItem;
//					selectorHandler.removeMessages(1);
//					selectorHandler.sendMessageDelayed(msg,100);
//				} else {
//					Message msg = mHandler.obtainMessage();
//					DynamicCommentReplyItem reItem = new DynamicCommentReplyItem();
//					reItem.setNickname(dyItem.getNickname());
//					reItem.setUid(dyItem.getUid());
//					msg.what = 1;
//					msg.arg1 = pos;
//					msg.arg2 = GameCircleAdapter.this.getCount();
//					msg.obj = reItem;
//					mHandler.sendMessageDelayed(msg,100);
//				}
//			}
//
//		}; 

	
}
