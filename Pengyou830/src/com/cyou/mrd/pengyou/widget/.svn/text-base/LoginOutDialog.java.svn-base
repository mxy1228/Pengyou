package com.cyou.mrd.pengyou.widget;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.utils.Util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;

public class LoginOutDialog extends Builder implements OnKeyListener{

	private Context mContext;
	
	public LoginOutDialog(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	private void init(){
		this.setTitle(R.string.login_out_title);
		this.setMessage(R.string.login_out_hint);
		this.setCancelable(false);
		this.setPositiveButton(R.string.relogin, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				loginOut();
			}
		});
		this.setOnKeyListener(this);
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			loginOut();
		}
		return false;
	}

	private void loginOut(){
		Util.loginOut(true,mContext);
	}
}
