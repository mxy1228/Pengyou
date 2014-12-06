package com.cyou.mrd.pengyou.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import com.cyou.mrd.pengyou.R;

public class RoundImageView extends RelativeLayout {
	private ImageView mImageView;
	private RoundMaskView mMaskView;
	private Context mContext;
	private boolean mSmall = false;
	private int mWidth;
	private int mHeight;
	private int mBGColor;

	public RoundImageView(Context context) {
		super(context);
		this.mContext = context;
		initView();
		
	}
	
	public RoundImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		TypedArray array = mContext.obtainStyledAttributes(attrs,R.styleable.RoundImageView);
		this.mSmall = array.getBoolean(R.styleable.RoundImageView_small, false);
		this.mBGColor = array.getColor(R.styleable.RoundImageView_bg_color, mContext.getResources().getColor(R.color.empty_bg));
		array.recycle();
		initView();
	}
	
	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		TypedArray array = mContext.obtainStyledAttributes(attrs,R.styleable.RoundImageView);
		this.mSmall = array.getBoolean(R.styleable.RoundImageView_small, false);
		this.mBGColor = array.getColor(R.styleable.RoundImageView_bg_color, mContext.getResources().getColor(R.color.empty_bg));
		array.recycle();
		initView();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	
	private void initView(){
		LayoutParams iv_params = null;
		LayoutParams mask_params = null;
		if(mSmall){
			this.mWidth = mContext.getResources().getDimensionPixelSize(R.dimen.small_avatar_width);
			this.mHeight = mContext.getResources().getDimensionPixelSize(R.dimen.small_avatar_height);
		}else{
			this.mWidth = mContext.getResources().getDimensionPixelSize(R.dimen.avatar_width);
			this.mHeight = mContext.getResources().getDimensionPixelSize(R.dimen.avatar_height);
		}
		iv_params = new LayoutParams(mWidth,mHeight);
		mask_params = new LayoutParams(mWidth, mHeight);
		this.mImageView = new ImageView(mContext);
		this.mImageView.setLayoutParams(iv_params);
		this.mImageView.setScaleType(ScaleType.FIT_XY);
		this.mImageView.setImageResource(R.drawable.avatar_defaul);
		addView(mImageView);
		this.mMaskView = new RoundMaskView(mContext,mSmall,mBGColor);
		this.mMaskView.setLayoutParams(mask_params);
		this.mMaskView.setWidth(mWidth);
		this.mMaskView.setHeight(mHeight);
		addView(mMaskView);
	}
	
	public ImageView getImageView(){
		return this.mImageView;
	}
	
	public void setImageResource(int id){
		this.mImageView.setImageResource(id);
	}
	
	public void setImageBitmap(Bitmap bm){
		this.mImageView.setImageBitmap(bm);
	}
	
	public void setBackColor(String color){
		this.mBGColor = Color.parseColor(color);
	}
	
	public void isSmall(boolean b){
		this.mSmall = b;
	}
}
