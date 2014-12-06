package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.log.CYLog;

/**
 * 搜索推荐关键字
 * 
 * @author wangkang
 * 
 */
public class SearchWordTag extends LinearLayout {

	private CYLog log = CYLog.getInstance();
	private Context mContext;

	public SearchWordTag(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	public SearchWordTag(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.search_word_tag, null);
		addView(view);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		log.d("onMeasure");
		log.d("measureWidth = " + widthMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
