package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;


public class IntroDownloadBtn extends FrameLayout {

	private static final int MAX_PROGRESS = 100;
	
	private  ProgressBar mPB;
	private TextView mTV;
	private Context mContext;
	private int mCurrentProgress;
    private ImageView mImageView;
    private TextView mTextView;
    private LinearLayout mLy;

 //   private ImageButton mImageButton;

	
	
	public IntroDownloadBtn(Context context) {
		super(context);
		this.mContext = context;
		init();
	}
	
	public IntroDownloadBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}
	
	public IntroDownloadBtn(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		init();
	}

	public ProgressBar getmPB() {
//		if(mPB==null){
//			mPB = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
//		}
		return mPB;
	}

	private void init(){
		if(mPB==null){
		mPB = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
		}
		LayoutParams pbParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mPB.setLayoutParams(pbParams);		
		mPB.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.intro_download_btn_xbg));
		mPB.setMax(100);
		mPB.setProgress(0);
		addView(mPB);		
		mLy=new LinearLayout(mContext);
		LayoutParams lyParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams imParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mLy.setGravity(Gravity.CENTER);
		imParams.setMargins(0, 0,10, 0);
		imParams.gravity=Gravity.CENTER;
		lyParams.gravity=Gravity.CENTER;		
		mLy.setLayoutParams(lyParams);
		mImageView=new ImageView(mContext);
		mImageView.setLayoutParams(imParams);	
	//	mImageView.setImageResource(R.drawable.xiazai);
		mTextView=new TextView(mContext);
		mTextView.setLayoutParams(lyParams);
		mTextView.setTextColor(getResources().getColor(R.color.white));
		mTextView.setTextSize(16);
	//	mTextView.setText(R.string.download);
		mLy.addView(mImageView);
		mLy.addView(mTextView);
		addView(mLy);

	}
	
	public void reinit(){
		    this.removeAllViews();
		    mPB = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
			LayoutParams pbParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
			mPB.setLayoutParams(pbParams);			
			mPB.setProgressDrawable(mContext.getResources().getDrawable(R.drawable.intro_download_btn_xbg));
		//	mPB.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.intro_download_btn_bg));
			mPB.setMax(100);
			mPB.setProgress(0);
			addView(mPB);			
			mLy=new LinearLayout(mContext);
			LayoutParams lyParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			LinearLayout.LayoutParams imParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mLy.setGravity(Gravity.CENTER);
			imParams.setMargins(0, 0,10, 0);
			imParams.gravity=Gravity.CENTER;
			lyParams.gravity=Gravity.CENTER;			
			mLy.setLayoutParams(lyParams);
			mImageView=new ImageView(mContext);
			mImageView.setLayoutParams(imParams);	
			mImageView.setImageResource(R.drawable.xiazai);
		//	mImageView.setVisibility(View.GONE);
			mTextView=new TextView(mContext);
			mTextView.setLayoutParams(lyParams);
			mTextView.setTextColor(getResources().getColor(R.color.white));
			mTextView.setTextSize(16);
			mTextView.setText(R.string.download);
			mLy.addView(mImageView);
			mLy.addView(mTextView);
			addView(mLy);
	}

	public void setProgress(int progress){
		mCurrentProgress = progress;
		if(progress <= 0 ){
			mCurrentProgress = 0;
		}else if(progress > MAX_PROGRESS){
			mCurrentProgress = MAX_PROGRESS;
		}
		mPB.setProgress(mCurrentProgress);
	}
	public void setSomething(int drawable,String string,boolean flag){
		if(flag){
			mImageView.setVisibility(View.VISIBLE);
			mImageView.setImageResource(drawable);
		}else{
			mImageView.setVisibility(View.GONE);
		}
		
		mTextView.setText(string);
		
	}
	public void setSomething(int drawable,SpannableStringBuilder style,boolean flag){
		if(flag){
			mImageView.setVisibility(View.VISIBLE);
			mImageView.setImageResource(drawable);
		}else{
			mImageView.setVisibility(View.GONE);
		}
		
		mTextView.setText(style);
		
	}
	public String getText(){
		if(mTV==null){
			return "";
		}
		return mTV.getText().toString();
	}
	public void setText(String str){
		if(mTV != null){
			mTV.setText(str);
		}
	}
	
	public void setText(int id){
		if(mTV != null){
			mTV.setText(mContext.getString(id));
		}
	}
}
