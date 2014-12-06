package com.cyou.mrd.pengyou.widget.pull2refresh;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class ListHeaderView extends ViewGroup{

	private static final int UPDATE_IDLE = 0;
	private static final int UPDATE_READY = 1;
	private static final int UPDATE_ING = 2;
	private static final int UPDATE_FINISH = 3;
	private static final int MAX_PULL_HEIGHT_DP = 200;
	private static final int MAX_DURATION = 350;
	private static final String UPDATE_TIME_SP_NAME = "pull2refresh_update_time";
	private static final int MIN_UPDATE_TIME = 1000;//下拉刷新的最小时间
	
	private TextView mTipsTV;
	private TextView mTimeTV;
	private ImageView mArrowIV;
	private ProgressBar mPB;
	
	private PullToRefreshListView mListViewBase;
	private RotateAnimation mAnimationUP;
	private RotateAnimation mAnimationDown;
	private Context mContext;
	private SharedPreferences mSP;
	private Activity mActivity;
	//用户可以下拉的最大高度
	private int mMaxPullHeight;
	private int mHeight;
	private int mUpdateHeight;//HeaderView的原始高度
	private int mState = UPDATE_IDLE;
	private boolean mCanUpdate;
	private boolean mImediateUpdate = false;
	private int mInitHeight;
	private int mDistance;
	private int mNextState = -1;
	
	private static final Interpolator mInterpolatot = new Interpolator() {
		
		@Override
		public float getInterpolation(float arg0) {
			arg0 -= 1.0f;
			return arg0 * arg0 * arg0 + 1.0f;
		}
	};
	
	public ListHeaderView(Context context,PullToRefreshListView listview){
		super(context);
//		this.setBackgroundColor(Color.parseColor("#3f414b"));
		this.mListViewBase = listview;
		this.mMaxPullHeight = (int) (context.getResources().getDisplayMetrics().density
				* MAX_PULL_HEIGHT_DP + 0.5f);
		this.mAnimationDown = new RotateAnimation(-180, 0
				, RotateAnimation.RELATIVE_TO_SELF, 0.5f
				, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.mAnimationDown.setFillAfter(true);
		this.mAnimationDown.setDuration(200);
		this.mAnimationDown.setInterpolator(new LinearInterpolator());
		this.mAnimationUP = new RotateAnimation(0, -180
				, RotateAnimation.RELATIVE_TO_SELF, 0.5f
				, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		this.mAnimationUP.setFillAfter(true);
		this.mAnimationUP.setDuration(200);
		this.mAnimationUP.setInterpolator(new LinearInterpolator());
		this.mContext = context;
		this.mSP = mContext.getSharedPreferences(UPDATE_TIME_SP_NAME, Context.MODE_PRIVATE);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		View childView = getChildView();
		if(childView == null){
			return;
		}
		int childViewWidth = childView.getMeasuredWidth();
		int childViewHeight = childView.getMeasuredHeight();
		int measureHeight = getMeasuredHeight();
		childView.layout(0
				, measureHeight - childViewHeight
				, childViewWidth
				, measureHeight);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		if(mHeight < 0){
			mHeight = 0;
		}
		setMeasuredDimension(width, mHeight);
		View childView = getChildView();
		if(childView != null){
			childView.measure(widthMeasureSpec, heightMeasureSpec);
			mUpdateHeight = childView.getMeasuredHeight();
		}
	}
	@Override
	public void addView(View child) {
		this.mTipsTV = (TextView)child.findViewById(R.id.pull_fresh_tipstv);
		this.mTipsTV.setText(R.string.pull_to_refresh);
		this.mTimeTV = (TextView)child.findViewById(R.id.pull_fresh_timetv);
		this.mArrowIV = (ImageView)child.findViewById(R.id.pull_fresh_arrowiv);
		this.mPB = (ProgressBar)child.findViewById(R.id.pull_fresh_pb);
		super.addView(child);
	}
	
	private View getChildView(){
		if(getChildCount() != 1){
			return null;
		}
		return getChildAt(0);
	}

	public void setHeaderHeight(int height){
		if(mHeight == height && height == 0){
			return;
		}
		if(height > mMaxPullHeight){
			return;
		}
		int updateHeight = mUpdateHeight;
		mHeight = height;
		if(mState != UPDATE_IDLE){
			if(mState == UPDATE_READY){
				onViewUpdating();
				mState = UPDATE_ING;
			}
		}else {
			if((height < updateHeight) && mCanUpdate){
				mCanUpdate = false;
				onViewChanged();
			}else if(height > updateHeight && !mCanUpdate){
				mCanUpdate = true;
				onViewChanged();
			}
		}
		requestLayout();
		if(height == 0){
			mState = UPDATE_IDLE;
			mCanUpdate = false;
		}
//		Log.d("pengyou", mTipsTV.getText().toString());
	}
	
	private void onViewChanged(){
		if(mCanUpdate){
			mArrowIV.clearAnimation();
	        mArrowIV.startAnimation(mAnimationUP);
	        mTipsTV.setText(mContext.getString(R.string.release_to_refresh));
		}else{
			mArrowIV.clearAnimation();
	        mArrowIV.startAnimation(mAnimationDown);
	        mTipsTV.setText(mContext.getString(R.string.pull_to_refresh));
		}
	}
	
	private void onViewStartFresh(){
		mPB.setVisibility(View.GONE);
		mArrowIV.clearAnimation();
		mArrowIV.setVisibility(View.VISIBLE);
		mTipsTV.setText(mContext.getString(R.string.pull_to_refresh));
	}
	
	public boolean isNeedUpdate(){
		if(mImediateUpdate){
			mImediateUpdate = false;
			return true;
		}
		int distance = mHeight - mUpdateHeight;
		return distance >= 0;
	}
	
	public void startUpdate(){
		Log.d("pengyou", "startUpdate");
//		onViewStartFresh();
		mState = UPDATE_READY;
		this.mInitHeight = mHeight;
		this.mDistance = mInitHeight - mUpdateHeight;
		if(mDistance < 0){
			mDistance = mInitHeight;
		}
//		int duration = (int)(mDistance * 3);
		int duration = (int)(mDistance);
		duration = duration > MAX_DURATION ? MAX_DURATION : duration;
		CloseTimer timer = new CloseTimer(duration);
		timer.startTimer();
	}
	
	public void finishUpdate(final int nextState,long startline,final ListView listview){
//		final long delta = MIN_UPDATE_TIME - (System.currentTimeMillis() - startline);
//		if(delta > 0){
			Runnable run = new Runnable() {
				
				@Override
				public void run() {
//					try {
//						Thread.sleep(delta);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
					listview.post(new Runnable() {
						
						@Override
						public void run() {
							close(nextState);
						}
					});
					
				}
			};
			Thread t = new Thread(run);
			t.start();
//		}else{
//			close(nextState);
//		}
	}
	
	private void onViewUpdating(){
		Log.d("pengyou", "onViewUpdating");
		this.mPB.setVisibility(View.VISIBLE);
		this.mArrowIV.clearAnimation();
        this.mArrowIV.setVisibility(View.GONE);
        this.mTipsTV.setText(R.string.refreshing);
	}
	
	private void onUpdateFinish(){
		this.mPB.setVisibility(View.GONE);
		this.mArrowIV.clearAnimation();
		this.mArrowIV.setVisibility(View.VISIBLE);
        this.mTipsTV.setText(R.string.pull_to_refresh);
        String date = new Date().toLocaleString();
        this.mTimeTV.setText(date);
//        Editor e = mSP.edit();
//        e.putString(mActivity.getLocalClassName(), date);
//        e.commit();
	}
	
	private class CloseTimer extends CountDownTimer{

		private static final int COUNT_DOWN_INTERVAL = 15;//频率
		private long mStart;
		private float mDurationReciprocal;
		
		public CloseTimer(long millisInFuture) {
			super(millisInFuture, COUNT_DOWN_INTERVAL);
			mDurationReciprocal = 1.0f / millisInFuture;
		}
		
		public void startTimer(){
			mStart = AnimationUtils.currentAnimationTimeMillis();
			start();
		}
		
		@Override
		public void onFinish() {
			float x = 1.0f;
			if(mNextState != -1){
				mListViewBase.setState(mNextState);
				mNextState = -1;
			}
			setHeaderHeight((int)(mInitHeight - mDistance * x));
		}

		@Override
		public void onTick(long millisUntilFinished) {
			int interval = (int)(AnimationUtils.currentAnimationTimeMillis() - mStart);
			float x = interval * mDurationReciprocal;
			x = mInterpolatot.getInterpolation(x);
			setHeaderHeight((int)(mInitHeight - mDistance * x));
		}
		
	}
	
	public void close(int nextState){
		this.mState = UPDATE_IDLE;
		mDistance = mInitHeight = mHeight;
		int duration = (int)(mDistance);
		duration = duration > MAX_DURATION ? MAX_DURATION : duration;
		mNextState = nextState;
		CloseTimer timer = new CloseTimer(duration);
		timer.startTimer();
		onUpdateFinish();
	}
	
	public void setActivity(Activity activity){
		this.mActivity = activity;
		if(this.mTimeTV != null){
			this.mTimeTV.setText(mContext.getString(R.string.pull_to_refresh_update_time
					, mSP.getString(activity.getLocalClassName(),new Date().toLocaleString() )));
		}
	}
}
