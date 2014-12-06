package com.cyou.mrd.pengyou.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.content.DialogInterface.OnKeyListener;

import com.cyou.mrd.pengyou.R;

public class WaitingDialog extends ProgressDialog implements OnKeyListener{
	
	private Context mContext;
	private View mView;

	public WaitingDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
	}

	
	public WaitingDialog(Context context) {
		super(context);
		this.mContext = context;
	}
	
	protected WaitingDialog(Context context, boolean cancelable,OnCancelListener cancelListener) {
		super(context);
		this.mContext = context;
	}

	public void init(){
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	}
	
	@Override
	public void show() {
		super.show();
		if(mView == null){
			LayoutInflater inflater = LayoutInflater.from(mContext);
			mView = inflater.inflate(R.layout.waiting_dialog, null);
			init();
		}
		setContentView(mView);
	}


	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.dismiss();
			if(mContext != null){
				((Activity)mContext).finish();
			}
		}
		return false;
	}

	public void show(String msg) {
		super.show();
		if(mView == null){
			LayoutInflater inflater = LayoutInflater.from(mContext);
			mView = inflater.inflate(R.layout.waiting_dialog, null);
			((TextView)mView.findViewById(R.id.waiting_dialog_tv)).setText(msg);
			init();
		}
		setContentView(mView);
	}
}
