package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.ui.FindFriendActivity;

public class MyGameItemViewCache {

	private View mView;
	private ImageView imgGameIcon;
	private TextView txtGameName;
	private TextView txtPlayerCount;
	private TextView txtPlayTime;
	private TextView txtCommentCount;
	private RatingBar ratingBar;
	private Button mBtnPlayGame;
	private LinearLayout exchangeCoinsLL;
	private ImageView exchangeCoinsIv;
	private ProgressBar exchangeCoinsPb;
	private ImageView goldRotationIV;
	private LinearLayout layoutBar;
	private Button btnGameDetail;
	private Button btnShareGame;
	private Button btnUpdate;
//	private ToggleButton tBtnIsShowGame;
	private ImageView imgGameUpdate;
	private ImageView imgGameShow;
	private LinearLayout gameCircle;
	private ImageButton imgComment;
	private TextView mTitleTV;
	private Button mDownloadBtn;
	private TextView mGameCircleTV;
	private LinearLayout mTopLL;
	private Button mShowGameBtn;
	private LinearLayout mClickLL;
	private ImageView mUpdateDotIV;
	private ImageView mActivityIV;
	private Button mMsgBtn;
	private ImageView mBannerIV;
	private Button mCloseBtn;
	private ImageView mGuessIconIV;
	private TextView mGuessGameNameTV;
	private Button mGuessChangeBtn;
	private TextView mPlayerCountTV;
	private Button mGuessDownloadBtn;
	private RelativeLayout mBannerRL;
	private RelativeLayout mGuessRL;
	private TextView mGuessPlayCountTV;
	private ImageButton mBannerCloseBtn;
	private TextView mBannerDivider;
	private ProgressBar mGuessPB;
	private LinearLayout mGuessPBLL;

	public MyGameItemViewCache(View view) {
		this.mView = view;
	}

	public View getmView() {
		return mView;
	}

	public ImageView getImgGameIcon() {
		if (imgGameIcon == null) {
			imgGameIcon = (ImageView) mView.findViewById(R.id.img_gameicon);
		}
		return imgGameIcon;
	}

	public TextView getTxtGameName() {
		if (txtGameName == null) {
			txtGameName = (TextView) mView.findViewById(R.id.my_game_item_name_tv);
		}
		return txtGameName;
	}

	public TextView getTxtPlayerCount() {
		if (txtPlayerCount == null) {
			txtPlayerCount = (TextView) mView
					.findViewById(R.id.txt_playercount);
		}
		return txtPlayerCount;
	}
	
	public TextView getTxtPlayTime() {
		if (txtPlayTime == null) {
			txtPlayTime = (TextView) mView
					.findViewById(R.id.txt_playtime);
		}
		return txtPlayTime;
	}
	
//	public TextView getTxtCommentCount() {
//		if (txtCommentCount == null) {
//			txtCommentCount = (TextView) mView
//					.findViewById(R.id.txt_comment_count);
//		}
//		return txtCommentCount;
//	}

	public ImageView getImgGameShow() {
		if (imgGameShow == null) {
			imgGameShow = (ImageView) mView.findViewById(R.id.img_show);
		}
		return imgGameShow;
	}

	public RatingBar getRatingBar() {
		if (ratingBar == null) {
			ratingBar = (RatingBar) mView.findViewById(R.id.rb_game);
		}
		return ratingBar;
	}

	public void setRatingBar(RatingBar ratingBar) {
		this.ratingBar = ratingBar;
	}

	public Button getmBtnPlayGame() {
		if (mBtnPlayGame == null) {
			mBtnPlayGame = (Button) mView.findViewById(R.id.mygame_item_playgame_btn);
		}
		return mBtnPlayGame;
	}

	public LinearLayout getmExchangeCoinsLL() {
		if (exchangeCoinsLL == null) {
			exchangeCoinsLL = (LinearLayout) mView.findViewById(R.id.mygame_item_coins_rl);
		}
		return exchangeCoinsLL;
	}
	
	public ImageView getmExchangeCoinsIv() {
		if (exchangeCoinsIv == null) {
			exchangeCoinsIv = (ImageView) mView.findViewById(R.id.mygame_item_coins_iv);
		}
		return exchangeCoinsIv;
	}

	public ProgressBar getmExchangeCoinsPb() {
		if (exchangeCoinsPb== null) {
			exchangeCoinsPb = (ProgressBar) mView.findViewById(R.id.mygame_item_coins_pb);
		}
		return exchangeCoinsPb;
	}

	public Button getBtnGameDetail() {
		if (btnGameDetail == null) {
			btnGameDetail = (Button) mView.findViewById(R.id.btn_detail);
		}
		return btnGameDetail;
	}

	public Button getBtnShareGame() {
		if (btnShareGame == null) {
			btnShareGame = (Button) mView.findViewById(R.id.btn_share);
		}
		return btnShareGame;
	}

	public Button getBtnUpdate() {
		if (btnUpdate == null) {
			btnUpdate = (Button) mView.findViewById(R.id.my_game_item_updat_btn);
		}
		return btnUpdate;
	}

//	public ToggleButton gettBtnIsShowGame() {
//		if (tBtnIsShowGame == null) {
//			tBtnIsShowGame = (ToggleButton) mView
//					.findViewById(R.id.tbtn_isshowgame);
//		}
//		return tBtnIsShowGame;
//	}

	public LinearLayout getLayoutBar() {
		if (layoutBar == null) {
			layoutBar = (LinearLayout) mView.findViewById(R.id.my_game_bar);
		}
		return layoutBar;
	}

    public LinearLayout getLayoutGameCircle() {
    	if(gameCircle == null) {
    		gameCircle = (LinearLayout)mView.findViewById(R.id.game_circle);
    	}
    	return gameCircle;
    }

//	public TextView getmTitleTV() {
//		if(mTitleTV == null){
//			mTitleTV = (TextView)mView.findViewById(R.id.my_game_item_top_tv);
//		}
//		return mTitleTV;
//	}

	public Button getmDownloadBtn() {
		if(mDownloadBtn == null){
			mDownloadBtn = (Button)mView.findViewById(R.id.mygame_item_download);
		}
		return mDownloadBtn;
	}

	public TextView getmGameCircleTV() {
		if(mGameCircleTV == null){
			mGameCircleTV = (TextView)mView.findViewById(R.id.my_game_item_game_circle);
		}
		return mGameCircleTV;
	}

	public LinearLayout getmTopLL() {
		if(mTopLL == null){
			mTopLL = (LinearLayout)mView.findViewById(R.id.my_game_item_top_ll);
		}
		return mTopLL;
	}

	public Button getmShowGameBtn() {
		if(mShowGameBtn == null){
			mShowGameBtn = (Button)mView.findViewById(R.id.my_game_item_showgame_btn);
		}
		return mShowGameBtn;
	}

	public LinearLayout getmClickLL() {
		if(mClickLL == null){
			mClickLL = (LinearLayout)mView.findViewById(R.id.my_game_item_click_ll);
		}
		return mClickLL;
	}

	public ImageView getmUpdateDotIV() {
		if(mUpdateDotIV == null){
			mUpdateDotIV = (ImageView)mView.findViewById(R.id.my_game_item_update_dot_iv);
		}
		return mUpdateDotIV;
	}

	public ImageView getmActivityIV() {
		if(mActivityIV == null){
			mActivityIV = (ImageView)mView.findViewById(R.id.my_game_item_activity_iv);
		}
		return mActivityIV;
	}

	public Button getmMsgBtn() {
		if(mMsgBtn == null){
			mMsgBtn = (Button)mView.findViewById(R.id.img_msg_count);
		}
		return mMsgBtn;
	}

	public ImageView getmBannerIV() {
		if(mBannerIV == null){
			this.mBannerIV = (ImageView)mView.findViewById(R.id.my_game_item_banner_iv);
		}
		return mBannerIV;
	}

	public ImageView getmGuessIconIV() {
		if(mGuessIconIV == null){
			this.mGuessIconIV = (ImageView)mView.findViewById(R.id.my_game_item_guess_icon_iv);
		}
		return mGuessIconIV;
	}

	public TextView getmGuessGameNameTV() {
		if(mGuessGameNameTV == null){
			mGuessGameNameTV = (TextView)mView.findViewById(R.id.my_game_item_guess_name_tv);
		}
		return mGuessGameNameTV;
	}

	public Button getmGuessChangeBtn() {
		if(mGuessChangeBtn == null){
			mGuessChangeBtn = (Button)mView.findViewById(R.id.my_game_item_guess_change_btn);
		}
		return mGuessChangeBtn;
	}

	public Button getmGuessDownloadBtn() {
		if(mGuessDownloadBtn == null){
			mGuessDownloadBtn = (Button)mView.findViewById(R.id.my_game_item_guess_download_btn);
		}
		return mGuessDownloadBtn;
	}

	public RelativeLayout getmBannerRL() {
		if(mBannerRL == null){
			mBannerRL = (RelativeLayout)mView.findViewById(R.id.my_game_item_banner_rl);
		}
		return mBannerRL;
	}

	public RelativeLayout getmGuessRL() {
		if(mGuessRL == null){
			mGuessRL = (RelativeLayout)mView.findViewById(R.id.my_game_item_guess_rl);
		}
		return mGuessRL;
	}

	public TextView getmGuessPlayCountTV() {
		if(mGuessPlayCountTV == null){
			this.mGuessPlayCountTV = (TextView)mView.findViewById(R.id.my_game_item_guess_playcount_tv);
		}
		return mGuessPlayCountTV;
	}

	public ImageButton getmBannerCloseBtn() {
		if(mBannerCloseBtn == null){
			mBannerCloseBtn = (ImageButton)mView.findViewById(R.id.my_game_item_banner_close_btn);
		}
		return mBannerCloseBtn;
	}

	public TextView getmBannerDivider() {
		if(mBannerDivider == null){
			mBannerDivider = (TextView)mView.findViewById(R.id.my_game_item_banner_diviver);
		}
		return mBannerDivider;
	}

	public ProgressBar getmGuessPB() {
		if(mGuessPB == null){
			mGuessPB = (ProgressBar)mView.findViewById(R.id.my_game_item_guess_pb);
		}
		return mGuessPB;
	}

	public LinearLayout getmGuessPBLL() {
		if(mGuessPBLL == null){
			mGuessPBLL = (LinearLayout)mView.findViewById(R.id.my_game_item_guess_pb_ll);
		}
		return mGuessPBLL;
	}
    
}
