package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class ChatImmeLayout extends LinearLayout {

	private ImmeListener mListener;
	private int mHeight;
	private boolean mHadInit;
	private boolean mHasKeybord;
	
	public ChatImmeLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ChatImmeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public interface ImmeListener{
		public void onImmeShow();
		public void onImmeHide();
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (!mHadInit) {
			mHadInit = true;
			mHeight = b;
//			if (mListener != null) {
//				mListener.onKeyBoardStateChange();
//			}
		} else {
			mHeight = mHeight < b ? b : mHeight;// 取最大
		}
		if (mHadInit && mHeight > b) {
			mHasKeybord = true;
			if (mListener != null) {
				mListener.onImmeShow();
			}
		}
		if (mHadInit && mHasKeybord && mHeight == b) {
			mHasKeybord = false;
			if (mListener != null) {
				mListener.onImmeHide();
			}
		}
	}
	
	public void setImmeListener(ImmeListener listener){
		this.mListener = listener;
	}
}
