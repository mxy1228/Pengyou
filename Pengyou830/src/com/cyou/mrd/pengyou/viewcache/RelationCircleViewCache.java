package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class RelationCircleViewCache {

	private RoundImageView mAvatarIV;
	private TextView mNickNameTV;
	private ImageView mSNSIV;
	private ImageView mEachFocusIV;
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
	private Button mPackUpBtn;
	private Button mCommentIBtn;
	private TextView  mSupportText;
	private LinearLayout mCommTitleLayout;
	private LinearLayout mCommentLayout;
	private TextView mGameType;
	
	private ProgressBar mPB;
	private TextView mEmptyTV;
	private View mView;
	
	private ImageButton mDynamicFailBtn;
	private FrameLayout mDynamicTopBtn;
	
	private LinearLayout  mRelScoredisplayLayout;
	private RatingBar mRelScoreRatingRar;
//	private LinearLayout mRelFromLayout;
//	private ImageView mPlatformIV;
	
	public RelationCircleViewCache(View view){
		this.mView = view;
	}

//   public LinearLayout getmScoredisplayLayout(){
//	    if(mScoredisplayLayout==null){
//	    	mScoredisplayLayout = (LinearLayout) mView.findViewById(R.id.game_score_display);
//	    }
//	    return mScoredisplayLayout;
//   }
   
//   public RatingBar getmScoreRatingBar(){
//	    if(mScoreRatingRar==null){
//	    	mScoreRatingRar = (RatingBar) mView.findViewById(R.id.intro_rating_rb);
//	    }
//	    return mScoreRatingRar;
//   }
   
   public LinearLayout getmRelScoredisplayLayout(){
	    if(mRelScoredisplayLayout==null){
	    	mRelScoredisplayLayout = (LinearLayout) mView.findViewById(R.id.relation_game_score_display);
	    }
	    return mRelScoredisplayLayout;
  }
  
  public RatingBar getmRelScoreRatingBar(){
	    if(mRelScoreRatingRar==null){
	    	mRelScoreRatingRar = (RatingBar) mView.findViewById(R.id.relation_intro_rating_rb);
	    }
	    return mRelScoreRatingRar;
  }
  
//  public LinearLayout getmRelFromLayout(){
//	    if(mRelFromLayout==null){
//	    	mRelFromLayout = (LinearLayout) mView.findViewById(R.id.relationship_circle_item_from);
//	    }
//	    return mRelFromLayout;
// }
   
	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			this.mAvatarIV = (RoundImageView)mView.findViewById(R.id.relationship_circle_item_avatar);
		}
		return mAvatarIV;
	}

	public TextView getmNickNameTV() {
		if(mNickNameTV == null){
			mNickNameTV = (TextView)mView.findViewById(R.id.relationship_cricle_item_nickanme_tv);
		}
		return mNickNameTV;
	}
	
//	public ImageView getmGender() {
//		if(mGenderView == null){
//			mGenderView = (ImageView)mView.findViewById(R.id.relationship_circle_gender);
//		}
//		return mGenderView;
//	}

//	public ImageView getmPlatform() {
//		if(mPlatformIV == null){
//			mPlatformIV = (ImageView)mView.findViewById(R.id.relationship_circle_item_game_from);
//		}
//		return mPlatformIV;
//	}

	public ImageView getmSNSIV() {
		if(mSNSIV == null){
			mSNSIV = (ImageView)mView.findViewById(R.id.relationship_circle_item_sns_iv);
		}
		return mSNSIV;
	}


	public ImageView getmEachFocusIV() {
		if(mEachFocusIV == null){
			this.mEachFocusIV = (ImageView)mView.findViewById(R.id.relationship_circle_item_each_focus_iv);
		}
		return mEachFocusIV;
	}


	public TextView getmTypeTV() {
		if(mTypeTV == null){
			this.mTypeTV = (TextView)mView.findViewById(R.id.relationship_circle_item_type_tv);
		}
		return mTypeTV;
	}


	public TextView getmDateTV() {
		if(mDateTV == null){
			this.mDateTV = (TextView)mView.findViewById(R.id.relationship_circle_item_date_tv);
		}
		return mDateTV;
	}


	public ImageView getmCaptureIV() {
		if(mCaptureIV == null){
			this.mCaptureIV = (ImageView)mView.findViewById(R.id.relationship_circle_item_capture_iv);
		}
		return mCaptureIV;
	}


//	public TextView getmFromTV() {
//		if(mFromTV == null){
//			this.mFromTV = (TextView)mView.findViewById(R.id.relationship_circle_item_from_tv);
//		}
//		return mFromTV;
//	}


	public Button getmSupportBtn() {
		if(mSupportBtn == null){
			this.mSupportBtn = (Button)mView.findViewById(R.id.relationship_circle_item_support_btn);
		}
		return mSupportBtn;
	}

	public Button getmOpenCommentBtn() {
		if(mOpenCommentBtn == null){
			this.mOpenCommentBtn = (Button)mView.findViewById(R.id.relationship_circle_item_comment_btn_pull);
		}
		return mOpenCommentBtn;
	}


	public TextView getmContentTV() {
		if(mContentTV == null){
			this.mContentTV = (TextView)mView.findViewById(R.id.relationship_circle_item_content_tv);
		}
		return mContentTV;
	}
	
	public TextView getmContentALL() {
		if(mContentALL == null){
			this.mContentALL = (TextView)mView.findViewById(R.id.relationship_circle_item_content_tv1);
		}
		return mContentALL;
	}


	public LinearLayout getmGameRL() {
		if(mGameRL == null){
			this.mGameRL = (LinearLayout)mView.findViewById(R.id.relationship_circle_item_game_rl);
		}
		return mGameRL;
	}


	public ImageView getmGameIconIV() {
		if(mGameIconIV == null){
			this.mGameIconIV = (ImageView)mView.findViewById(R.id.relationship_circle_item_game_icon_iv);
		}
		return mGameIconIV;
	}

	public ImageView getmGameIconFrom() {
		if(mGameIconFrom == null){
			this.mGameIconFrom = (ImageView)mView.findViewById(R.id.relationship_circle_item_game_icon_platform);
		}
		return mGameIconFrom;
	}
	
	public ImageView getmGameZoneIcon() {
		if(mGameZoneFrom == null){
			this.mGameZoneFrom = (ImageView)mView.findViewById(R.id.relationship_circle_item_game_zone_from);
		}
		return mGameZoneFrom;
	}

	public TextView getmGameNameTV() {
		if(mGameNameTV == null){
			this.mGameNameTV = (TextView)mView.findViewById(R.id.relationship_circle_item_game_name_tv);
		}
		return mGameNameTV;
	}


	public TextView getmPlayerCountTV() {
		if(mPlayerCountTV == null){
			this.mPlayerCountTV = (TextView)mView.findViewById(R.id.relationship_circle_item_game_playercount_tv);
		}
		return mPlayerCountTV;
	}


	public LinearLayout getmCommentRL() {
		if(mCommentRL == null){
			mCommentRL = (LinearLayout)mView.findViewById(R.id.relationship_circle_item_comment_rl);
		}
		return mCommentRL;
	}
	
	public View getmCommentSegLine() {
		if(mCommentSL == null){
			mCommentSL = (View)mView.findViewById(R.id.relation_circle_comment_seg_line);
		}
		return mCommentSL;
	}
	
//	public LinearLayout getmCommentRL2() {
//		if(mCommentRL2 == null){
//			mCommentRL2 = (LinearLayout)mView.findViewById(R.id.relationship_circle_item_comment_rl2);
//		}
//		return mCommentRL2;
//	}


//	public TextView getmCommentTV() {
//		if(mCommentTV == null){
//			mCommentTV = (TextView)mView.findViewById(R.id.relationship_circle_item_comment_tv);
//		}
//		return mCommentTV;
//	}


	public Button getmPackUpBtn() {
		if(mPackUpBtn == null){
			mPackUpBtn = (Button)mView.findViewById(R.id.relationship_circle_item_pack_up_btn);
		}
		return mPackUpBtn;
	}

	public Button getmCommentIBtn() {
		if(mCommentIBtn == null){
			mCommentIBtn = (Button)mView.findViewById(R.id.relationship_circle_item_comment_btn);
		}
		return mCommentIBtn;
	}
	
//	public ImageView getmSupportImg() {
//		if(mSupportImg == null){
//			mSupportImg = (ImageView)mView.findViewById(R.id.relationship_circle_item_support_img);
//		}
//		return mSupportImg;
//	}

	public TextView getmSupportText() {
		if(mSupportText == null){
			mSupportText = (TextView)mView.findViewById(R.id.relationship_circle_item_suport_users);
		}
		return mSupportText;
	}
	
	public LinearLayout getmCommentTitleLayout() {
		if(mCommTitleLayout == null){
			mCommTitleLayout = (LinearLayout)mView.findViewById(R.id.relationship_circle_item_comment_head);
		}
		return mCommTitleLayout;
	}
	
	public LinearLayout getmCommentLayout() {
		if(mCommentLayout == null){
			mCommentLayout = (LinearLayout)mView.findViewById(R.id.relationship_circle_item_comment_rl_head);
		}
		return mCommentLayout;
	}
	
	public TextView getmGameType() {
		if(mGameType == null){
			this.mGameType = (TextView)mView.findViewById(R.id.relationship_circle_item_game_type);
		}
		return mGameType;
	}

	public ProgressBar getmPB() {
		if(mPB == null){
			mPB = (ProgressBar)mView.findViewById(R.id.relationship_circle_item_comment_item_pb);
		}
		return mPB;
	}


	public TextView getmEmptyTV() {
		if(mEmptyTV == null){
			mEmptyTV = (TextView)mView.findViewById(R.id.relationship_circle_item_comment_item_empty_tv);
		}
		return mEmptyTV;
	}


	public ImageButton getmDynamicFailBtn() {
		if(mDynamicFailBtn == null){
			mDynamicFailBtn = (ImageButton)mView.findViewById(R.id.relationship_circle_item_send_fail);
		}
		return mDynamicFailBtn;
	}
	
	public FrameLayout getmDynamicTopBtn() {
		if(mDynamicTopBtn == null){
			mDynamicTopBtn = (FrameLayout)mView.findViewById(R.id.relationship_circle_item_dynamic_top);
		}
		return mDynamicTopBtn;
	}
	
}
