package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cyou.mrd.pengyou.R;

public class RoundImageViewVer2 extends ImageView {

	private Path mPath;
	private int mWidth;
	private int mHeight;
	private Context mContext;
	private Paint mRectPaints;
	
	public RoundImageViewVer2(Context context) {
		super(context);
		this.mContext = context;
		initView();
	}
	
	public RoundImageViewVer2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}
	
	public RoundImageViewVer2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initView();
	}
	
	private void initView(){
		this.mWidth = mContext.getResources().getDimensionPixelSize(R.dimen.avatar_width);
		this.mHeight = mContext.getResources().getDimensionPixelSize(R.dimen.avatar_height);
		mPath = new Path();
		mPath.moveTo(0, 0);
		mPath.addCircle(0, 0, mWidth/2, Direction.CCW);
		setDrawingCacheEnabled(true);
		mRectPaints = new Paint();		
		mRectPaints.setAntiAlias(true);
		mRectPaints.setStyle(Paint.Style.FILL);
		mRectPaints.setStrokeWidth(12);
//		mRectPaints.setColor(mBGColor);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.clipPath(mPath);
		Bitmap b = getDrawingCache();
		canvas.drawBitmap(b, 0, 0, mRectPaints);
//		Drawable d = getDrawable();
//		if(d != null){
//			d.draw(canvas);
//		}
	}
}
