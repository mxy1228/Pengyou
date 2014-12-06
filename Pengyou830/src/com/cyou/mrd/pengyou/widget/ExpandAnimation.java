package com.cyou.mrd.pengyou.widget;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.cyou.mrd.pengyou.log.CYLog;

public class ExpandAnimation extends Animation {
	
	private static final int DURATION = 300;
	private CYLog log = CYLog.getInstance();
	
	private View mView;
	private LayoutParams mLayoutParams;
	private int mMarginStart;
	private int mMarginEnd;
	private boolean mWasEndAlready = false;
	private boolean mIsVisibleBefore = false;
	
	
	public ExpandAnimation(View view,int height){
		setDuration(DURATION);
		this.mView = view;
		this.mLayoutParams = (LayoutParams)view.getLayoutParams();
		this.mMarginStart = mLayoutParams.bottomMargin;
		this.mMarginEnd = (mMarginStart == 0 ? (0 - height) : 0);
		this.mIsVisibleBefore = view.getVisibility() == View.VISIBLE ? true : false;
		view.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		if(interpolatedTime < 1.0f){
			mLayoutParams.bottomMargin = mMarginStart + (int)((mMarginEnd - mMarginStart) * interpolatedTime);
			mView.requestLayout();
		}else if(!mWasEndAlready){
			mLayoutParams.bottomMargin = mMarginEnd;
			mView.requestLayout();
			if(mIsVisibleBefore){
				mView.setVisibility(View.GONE);
			}
			mWasEndAlready = true;
		}
		
	}
	
}
