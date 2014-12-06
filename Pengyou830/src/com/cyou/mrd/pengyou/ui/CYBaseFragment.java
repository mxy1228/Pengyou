package com.cyou.mrd.pengyou.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.cyou.mrd.pengyou.log.CYLog;

public abstract class CYBaseFragment extends Fragment {

	protected CYLog log;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log = CYLog.getInstance();
	}
	
	protected abstract void initEvent();
	
	protected abstract void initData();
	
	protected void showShortToast(int res){
		Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
	}
	
	protected void showShortToast(String str){
		Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
	}
	
	protected void showLongToast(int res){
		Toast.makeText(getActivity(), res, Toast.LENGTH_LONG).show();
	}
	
	protected void showLongToast(String str){
		Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
	}
	
}
