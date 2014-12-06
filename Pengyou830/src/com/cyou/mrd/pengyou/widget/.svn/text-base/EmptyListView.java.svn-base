package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;

public class EmptyListView extends LinearLayout {

	private Context mContext;

	public EmptyListView(Context context) {
		super(context);
		this.mContext = context;
		initView();

	}

	public EmptyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.waiting_dialog, this);
	}

}
