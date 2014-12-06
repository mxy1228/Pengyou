package com.cyou.mrd.pengyou.widget.CoinAnimation;

import com.cyou.mrd.pengyou.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CoinNumberView extends ImageView implements AnimationListener{
	Canvas mCanvas;
	Context mContext;
	String mText;
//	Paint mPaint;

	public CoinNumberView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public CoinNumberView(Context context, String text) {
		super(context);
		mContext = context;
		mText = text;
//		mPaint = new Paint();
	}
	
	public static void start(Context c, View v, AnimationListener l){
//		v.requestLayout();
//		v.invalidate();
		Animation a =AnimationUtils.loadAnimation(c, R.anim.push_up_out_scale);
		a.setAnimationListener(l);
		v.startAnimation(a);
	}
	
	
	protected void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec) {
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		int wmode = View.MeasureSpec.getMode(widthMeasureSpec);
		int hmode = View.MeasureSpec.getMode(heightMeasureSpec);
		int wm = View.MeasureSpec.makeMeasureSpec(width, wmode);
		int hm = View.MeasureSpec.makeMeasureSpec(height, hmode);
		this.setMeasuredDimension(wm, hm);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mText != null) {
			Paint paint = new Paint();
//			Paint paint = mPaint;
			Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
			paint.setTypeface( font );
			paint.setColor(Color.YELLOW);
			paint.setTextSize(20);
			float tw = paint.measureText(mText);
			canvas.drawText(mText, (getWidth() - tw)/2, getHeight(), paint);
		}

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		clearAnimation();
		ViewParent parent =  getParent();
		View child = this;
		while (parent != null && !(parent instanceof FrameLayout)) {
			((ViewGroup) parent).removeView(child);
			child = (View) parent;
			parent = child.getParent();
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
	

}
