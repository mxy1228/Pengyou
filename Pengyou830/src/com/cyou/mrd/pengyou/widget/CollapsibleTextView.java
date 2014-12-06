package com.cyou.mrd.pengyou.widget;

import com.cyou.mrd.pengyou.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.view.View.OnClickListener;

public class CollapsibleTextView extends LinearLayout implements
		OnClickListener {
	private static final int DEFAULT_MAX_LINE_COUNT = 3;
	private ImageButton mPackUpBtn;
	private TextView desc;
	//private TextView descOp;
	private static final int COLLAPSIBLE_STATE_NONE = 0;
	private static final int COLLAPSIBLE_STATE_SHRINKUP = 1;
	private static final int COLLAPSIBLE_STATE_SPREAD = 2;

	private String shrinkup;
	private String spread;
	
	private int mState;
	private boolean flag;

	public CollapsibleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	//	shrinkup = "收起。。。。。";
	//	spread = "显示更多";
		
		View view = inflate(context, R.layout.collapsible_textview, this);
		view.setPadding(0, -1, 0, 0);
		desc = (TextView) findViewById(R.id.desc_tv);
		//descOp = (TextView) findViewById(R.id.desc_op_tv);
		mPackUpBtn = (ImageButton) findViewById(R.id.intro_info_pack_up_btn);
		mPackUpBtn.setOnClickListener(this);
		//descOp.setOnClickListener(this);
	}

	public CollapsibleTextView(Context context) {
		this(context, null);
	}

	public final void setDesc(CharSequence charSequence, BufferType bufferType) {
		desc.setText(charSequence, bufferType);
		mState = COLLAPSIBLE_STATE_SPREAD;
		requestLayout();
	}

	public void onClick(View v) {
		flag = false;
		requestLayout();
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!flag) {
			flag = true;
			if (desc.getLineCount() <= DEFAULT_MAX_LINE_COUNT) {
				mState = COLLAPSIBLE_STATE_NONE;
				mPackUpBtn.setVisibility(View.GONE);
				//descOp.setVisibility(View.GONE);
				desc.setMaxLines(DEFAULT_MAX_LINE_COUNT + 1);
			} else {
				post(new InnerRunnable());
			}
		}
	}

	class InnerRunnable implements Runnable {
		@Override
		public void run() {
			if (mState == COLLAPSIBLE_STATE_SPREAD) {
				desc.setMaxLines(DEFAULT_MAX_LINE_COUNT);
				mPackUpBtn.setVisibility(View.VISIBLE);
				mPackUpBtn.setImageResource(R.drawable.intro_comment_item_open_pic);
				
			//	descOp.setVisibility(View.VISIBLE);
			//	descOp.setText(spread);
				mState = COLLAPSIBLE_STATE_SHRINKUP;
			} else if (mState == COLLAPSIBLE_STATE_SHRINKUP) {
				desc.setMaxLines(Integer.MAX_VALUE);
				mPackUpBtn.setVisibility(View.VISIBLE);
				mPackUpBtn.setImageResource(R.drawable.intro_comment_item_pack_pic);
				//descOp.setVisibility(View.VISIBLE);
				//descOp.setText(shrinkup);
				mState = COLLAPSIBLE_STATE_SPREAD;
			}else if(mState==COLLAPSIBLE_STATE_NONE){
				desc.setMaxLines(Integer.MAX_VALUE);
			}
		}
	}

}
