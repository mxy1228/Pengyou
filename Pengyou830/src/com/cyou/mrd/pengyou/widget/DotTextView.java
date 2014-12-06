package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.widget.TextView;

public class DotTextView extends TextView {

	
	public DotTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public DotTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public DotTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);        
        paint.setColor(Color.parseColor("#d8d9da"));
        Path path = new Path();
        path.moveTo(0, getHeight()/2);
        path.lineTo(getWidth(), getHeight()/2);
        PathEffect effects = new DashPathEffect(
                        new float[]{5,5,5,5}, 1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
	}
}
