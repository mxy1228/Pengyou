package com.cyou.mrd.pengyou.widget;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.MyMessageItem;

public class LetterPB extends LinearLayout {

	private static final int SHOW_TIME = 20;
	
	private Timer mTimer;
	private Context mContext;
	private LayoutInflater mInflater;
	private int mShowTime = 0;
	private ResultListener mListener;
	private MyMessageItem mItem;
	private boolean mSendSuccess = false;
	
	private ProgressBar mPB;
	private ImageView mIV;
	
	public LetterPB(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public LetterPB(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	private void init(){
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.letter_pb, null);
		addView(view);
		this.mPB = (ProgressBar)view.findViewById(R.id.letter_pb);
		this.mIV = (ImageView)view.findViewById(R.id.letter_failed_iv);
		this.mTimer = new Timer();
	}
	
	public void start(){
		this.setVisibility(View.VISIBLE);
		mTimer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				mShowTime += 1;
				if(mShowTime >= SHOW_TIME){
					mHandler.sendEmptyMessage(0);
				}
			}
		}, 0, 1000);
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mPB.setVisibility(View.GONE);
			mIV.setVisibility(View.VISIBLE);
			if(mListener != null){
				if(!mSendSuccess){
					mListener.onFailed(mItem);
				}
			}
		};
	};
	
	public void reset(){
		mIV.setVisibility(View.GONE);
		mPB.setVisibility(View.VISIBLE);
		mShowTime = 0;
		mSendSuccess = false;
		mTimer.cancel();
		mTimer = new Timer();
	}
	
	public void sendFailed(){
		this.setVisibility(View.VISIBLE);
		mPB.setVisibility(View.GONE);
		mIV.setVisibility(View.VISIBLE);
		mSendSuccess = false;
		mShowTime = 0;
		mTimer.cancel();
	}
	
	public void sendSuccess(){
		mSendSuccess = true;
		this.setVisibility(View.GONE);
		mShowTime = 0;
		mTimer.cancel();
	}
	
	public void setResultListener(ResultListener listener,MyMessageItem item){
		this.mListener = listener;
		this.mItem = item;
	}
	
	public interface ResultListener{
		public void onFailed(MyMessageItem i);
	}
	
	/**
	 * 判断是否正在显示Loading
	 * @return
	 */
	public boolean isLoading(){
		return mPB.getVisibility() == View.VISIBLE;
	}
}
