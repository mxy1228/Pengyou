package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GameGridView extends GridView {
	public GameGridView(Context context) {
		super(context);
	}

	public GameGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GameGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}
}
