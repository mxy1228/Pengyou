package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import com.cyou.mrd.pengyou.R;

public class FriendInfoViewCache {

	private TextView mTypeTV;
	private TextView mDateTV;
	private ImageView mCaptureIV;
//	private TextView mFromTV;
	private Button mSupportBtn;
	private Button mOpenCommentBtn;
	private TextView mContentTV;
	private TextView mContentALL;
	private LinearLayout mGameRL;
	private ImageView mGameIconIV;
	private ImageView mGameIconFrom;
	private ImageView mGameZoneFrom;
	private TextView mGameNameTV;
	private TextView mPlayerCountTV;
	private LinearLayout mCommentRL;
	private View mCommentSL;
	private TextView mTimeTV;
	private Button mPackUpBtn;
	private Button mCommentIBtn;
	private TextView  mSupportText;
	private LinearLayout mCommTitleLayout;
	private LinearLayout mCommentLayout;
	private TextView mGameType;	
	private ProgressBar mPB;
	private TextView mEmptyTV;
	private View mView;	
	private LinearLayout  mRelScoredisplayLayout;
	private RatingBar mRelScoreRatingRar;
//	private LinearLayout mRelFromLayout;
	
	public FriendInfoViewCache(View view){
		this.mView = view;
	}
   
   public LinearLayout getmRelScoredisplayLayout(){
	    if(mRelScoredisplayLayout==null){
	    	mRelScoredisplayLayout = (LinearLayout) mView.findViewById(R.id.friendinfo_dynamic_item_score_display);
	    }
	    return mRelScoredisplayLayout;
  }
  
  public RatingBar getmRelScoreRatingBar(){
	    if(mRelScoreRatingRar==null){
	    	mRelScoreRatingRar = (RatingBar) mView.findViewById(R.id.friendinfo_dynamic_item_rating_rb);
	    }
	    return mRelScoreRatingRar;
  }
  
//  public LinearLayout getmRelFromLayout(){
//	    if(mRelFromLayout==null){
//	    	mRelFromLayout = (LinearLayout) mView.findViewById(R.id.friendinfo_dynamic_item_from);
//	    }
//	    return mRelFromLayout;
//  }

  public TextView getmTypeTV() {
		if(mTypeTV == null){
			this.mTypeTV = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_type_tv);
		}
		return mTypeTV;
  }

  public TextView getmDateTV() {
		if(mDateTV == null){
			this.mDateTV = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_date_tv);
		}
		return mDateTV;
  }
  
  public TextView getmTimeTV() {
		if(mTimeTV == null){
			this.mTimeTV = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_time_tv);
		}
		return mTimeTV;
  }


	public ImageView getmCaptureIV() {
		if(mCaptureIV == null){
			this.mCaptureIV = (ImageView)mView.findViewById(R.id.friendinfo_dynamic_item_capture_iv);
		}
		return mCaptureIV;
	}


//	public TextView getmFromTV() {
//		if(mFromTV == null){
//			this.mFromTV = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_from_tv);
//		}
//		return mFromTV;
//	}


	public Button getmSupportBtn() {
		if(mSupportBtn == null){
			this.mSupportBtn = (Button)mView.findViewById(R.id.friendinfo_dynamic_item_support_btn);
		}
		return mSupportBtn;
	}

	public Button getmOpenCommentBtn() {
		if(mOpenCommentBtn == null){
			this.mOpenCommentBtn = (Button)mView.findViewById(R.id.friendinfo_dynamic_item_comment_btn_pull);
		}
		return mOpenCommentBtn;
	}


	public TextView getmContentTV() {
		if(mContentTV == null){
			this.mContentTV = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_content_tv);
		}
		return mContentTV;
	}
	
	public TextView getmContentALL() {
		if(mContentALL == null){
			this.mContentALL = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_content_tv1);
		}
		return mContentALL;
	}


	public LinearLayout getmGameRL() {
		if(mGameRL == null){
			this.mGameRL = (LinearLayout)mView.findViewById(R.id.friendinfo_dynamic_item_game_rl);
		}
		return mGameRL;
	}


	public ImageView getmGameIconIV() {
		if(mGameIconIV == null){
			this.mGameIconIV = (ImageView)mView.findViewById(R.id.friendinfo_dynamic_item_game_icon_iv);
		}
		return mGameIconIV;
	}

	public ImageView getmGameIconFrom() {
		if(mGameIconFrom == null){
			this.mGameIconFrom = (ImageView)mView.findViewById(R.id.friendinfo_dynamic_item_game_icon_platform);
		}
		return mGameIconFrom;
	}

	public TextView getmGameNameTV() {
		if(mGameNameTV == null){
			this.mGameNameTV = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_game_name_tv);
		}
		return mGameNameTV;
	}


	public TextView getmPlayerCountTV() {
		if(mPlayerCountTV == null){
			this.mPlayerCountTV = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_game_playercount_tv);
		}
		return mPlayerCountTV;
	}


	public LinearLayout getmCommentRL() {
		if(mCommentRL == null){
			mCommentRL = (LinearLayout)mView.findViewById(R.id.friendinfo_dynamic_item_comment_rl);
		}
		return mCommentRL;
	}
	
	public View getmCommentSegLine() {
		if(mCommentSL == null){
			mCommentSL = (View)mView.findViewById(R.id.friendinfo_dynamic_item_comment_seg_line);
		}
		return mCommentSL;
	}


	public Button getmPackUpBtn() {
		if(mPackUpBtn == null){
			mPackUpBtn = (Button)mView.findViewById(R.id.friendinfo_dynamic_item_pack_up_btn);
		}
		return mPackUpBtn;
	}

	public Button getmCommentIBtn() {
		if(mCommentIBtn == null){
			mCommentIBtn = (Button)mView.findViewById(R.id.friendinfo_dynamic_item_comment_btn);
		}
		return mCommentIBtn;
	}

	public TextView getmSupportText() {
		if(mSupportText == null){
			mSupportText = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_suport_users);
		}
		return mSupportText;
	}
	
	public LinearLayout getmCommentTitleLayout() {
		if(mCommTitleLayout == null){
			mCommTitleLayout = (LinearLayout)mView.findViewById(R.id.friendinfo_dynamic_item_comment_head);
		}
		return mCommTitleLayout;
	}
	
	public LinearLayout getmCommentLayout() {
		if(mCommentLayout == null){
			mCommentLayout = (LinearLayout)mView.findViewById(R.id.friendinfo_dynamic_item_comment_rl_head);
		}
		return mCommentLayout;
	}
	
	public TextView getmGameType() {
		if(mGameType == null){
			this.mGameType = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_game_type);
		}
		return mGameType;
	}

	public ProgressBar getmPB() {
		if(mPB == null){
			mPB = (ProgressBar)mView.findViewById(R.id.friendinfo_dynamic_item_comment_item_pb);
		}
		return mPB;
	}

	public TextView getmEmptyTV() {
		if(mEmptyTV == null){
			mEmptyTV = (TextView)mView.findViewById(R.id.friendinfo_dynamic_item_comment_item_empty_tv);
		}
		return mEmptyTV;
	}
	
	public ImageView getmGameZoneIcon() {
		if(mGameZoneFrom == null){
			this.mGameZoneFrom = (ImageView)mView.findViewById(R.id.friendinfo_dynamic_item_game_zone_from);
		}
		return mGameZoneFrom;
	}
	
}
