package org.taptwo.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.adapter.DynamicCommentAdapter;
import com.cyou.mrd.pengyou.log.CYLog;

public class DynamicCommentListView extends LinearLayout {

	private CYLog log = CYLog.getInstance();
	
	private DynamicCommentAdapter mAdapter;
	
	public DynamicCommentListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public DynamicCommentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setAdapter(DynamicCommentAdapter adapter){
		if(adapter == null){
			log.e("adapter is null");
			return;
		}
		this.mAdapter = adapter;
		initView();
	}
	
	public void notifyDataSetChanged(){
		initView();
	}
	
	private void initView(){
		int count = mAdapter.getCount();
		for(int i=0;i<count;i++){
			View v = mAdapter.getView(i, null, null);
			addView(v);
		}
	}
	
}
