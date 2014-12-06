package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.View;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.utils.Util;

public class RoundMaskView extends View {

	private static final int PAINT_WIDTH_DEFAULT = 11;//画笔默认宽度(单位：dip)
	private static final int CIRCLE_WIDTH = 3;//圆圈的宽度
	
	private static final int SMALL_PAINT_WIDTH_DEFAULT = 8;//画笔默认宽度(单位：dip)
	private static final int SMALL_CIRCLE_WIDTH = 3;//圆圈的宽度
	
	private RectF mCircleRectF;
	private Paint mRectPaints;
	private Paint mCirclePaint;//圆圈画笔
	private Context mContext; 
	private int mPaintWidthPX;//画笔的宽度(单位：px)
	private int mDrawPos = -90;// 绘制圆形的起点（默认为-90度即12点钟方向）
	private int mCircleWidthPX;
	private int mWidth;
	private int mHeight;
	private int mBGColor;
	private Path mPath;
	
	public RoundMaskView(Context context,boolean small,int color) {
		super(context);
		this.mContext = context;
		if(!small){
			mPaintWidthPX = Util.dip2px(mContext, PAINT_WIDTH_DEFAULT);
			mCircleWidthPX = Util.dip2px(mContext, CIRCLE_WIDTH);
		}else{
			mPaintWidthPX = Util.dip2px(mContext, SMALL_PAINT_WIDTH_DEFAULT);
			mCircleWidthPX = Util.dip2px(mContext, SMALL_CIRCLE_WIDTH);
		}
		this.mBGColor = color;
		initView();
	}
	
	public RoundMaskView(Context context,boolean small,int color, int circleWidth) {
		super(context);
		this.mContext = context;
		if(!small){
			mPaintWidthPX = Util.dip2px(mContext, PAINT_WIDTH_DEFAULT);
			mCircleWidthPX = Util.dip2px(mContext, circleWidth);
		}else{
			mPaintWidthPX = Util.dip2px(mContext, SMALL_PAINT_WIDTH_DEFAULT);
			mCircleWidthPX = Util.dip2px(mContext, circleWidth);
		}
		this.mBGColor = color;
		initView();
	}
	
	public RoundMaskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}
	
	public RoundMaskView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		initView();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float sweep = 360 * 100;
		canvas.drawArc(mCircleRectF, mDrawPos, sweep, false, mCirclePaint);//画白圈
		mPath.addCircle(mWidth/2, mHeight/2, mWidth/2-2, Direction.CCW);
		canvas.clipPath(mPath,Op.DIFFERENCE);//将后面的图片画成圆形并用BGColor遮挡剩余区域
		canvas.drawRect(0, 0, mWidth, mHeight, mRectPaints);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(resolveSize(mWidth, widthMeasureSpec), resolveSize(mHeight, heightMeasureSpec));
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mCircleRectF.set(CIRCLE_WIDTH, CIRCLE_WIDTH ,w-CIRCLE_WIDTH, h-CIRCLE_WIDTH);
	}
	
	private void initView(){
		mPath = new Path();
		
		mCircleRectF = new RectF();
		
		mRectPaints = new Paint();		
		mRectPaints.setAntiAlias(true);
		mRectPaints.setStyle(Paint.Style.FILL);
		mRectPaints.setStrokeWidth(mPaintWidthPX);
		mRectPaints.setColor(mBGColor);
		
		mCirclePaint = new Paint();		
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setStyle(Paint.Style.FILL);
		mCirclePaint.setStrokeWidth(mCircleWidthPX);
		mCirclePaint.setColor(mContext.getResources().getColor(R.color.white));
		mCirclePaint.setStyle(Paint.Style.STROKE);
	}
	
	public void setWidth(int width){
		this.mWidth = width;
	}
	
	public void setHeight(int height){
		this.mHeight = height;
	}
	
}
