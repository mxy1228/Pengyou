package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.SettingAdapter;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.PYUpdate;
import com.cyou.mrd.pengyou.entity.SettingItem;
import com.cyou.mrd.pengyou.entity.base.PYUpdateBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.service.CheckUpdateService;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SettingActivity extends CYBaseActivity implements OnClickListener,OnItemClickListener{

	private ImageButton mBackIBtn;
	private ListView mListView;
	
	private List<SettingItem> mData;
	private SettingAdapter mAdapter;
    private AlertDialog mSharedDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		initView();
		initEvent();
		initData();
	}
	
	@Override
	protected void initView() {
		this.mBackIBtn = (ImageButton)findViewById(R.id.setting_back_ibtn);
		this.mListView = (ListView)findViewById(R.id.setting_lv);
	}

	@Override
	protected void initEvent() {
		this.mBackIBtn.setOnClickListener(this);
		this.mListView.setOnItemClickListener(this);
	}	

	@Override
	protected void initData() {
		this.mData = new ArrayList<SettingItem>();
		String[] arrays = getResources().getStringArray(R.array.setting);
		for(int i=0;i<arrays.length;i++){
			String[] args = arrays[i].split(";");
			SettingItem item = new SettingItem();
			item.setName(args[0]);
			item.setTag(Integer.valueOf(args[1]));
			item.setType(Integer.valueOf(args[2]));
			try {
				if(i == arrays.length-1){
					String version = getApplication().getPackageManager()
							.getPackageInfo(getApplicationInfo().packageName, 0).versionName;
					if (PYVersion.DEBUG) {
						item.setText(getString(
								R.string.current_version, version) + "-debug");
					} else {
						item.setText(getString(
								R.string.current_version, version));
					}
				}
			} catch (Exception e) {
				log.e(e);
			}
			mData.add(item);
		}
		this.mAdapter = new SettingAdapter(this, mData);
		this.mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_back_ibtn:
			SettingActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg2) {
//		case 0:
//			if (mSharedDialog == null) {
//				showShareAppSelector();
//			}
//			mSharedDialog.show();
//			break;
		case 1:
			//通知
			Toast.makeText(SettingActivity.this, R.string.checking_update,
					Toast.LENGTH_LONG).show();
			checkUpdate();
			break;
		default:
			break;
		}
	}

	public void showShareAppSelector() {
		try {
			mSharedDialog = new AlertDialog.Builder(this).setItems(
					new CharSequence[] {getString(R.string.share_app_sina),
							getString(R.string.share_app_others)},
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								isValidateSinaToken();
								break;
							case 1:
								Util.shareToChooseApps(SettingActivity.this, false);
								break;
							default:
								break;
							}
						}
					}).create();
			mSharedDialog.setTitle(getString(R.string.share));
			mSharedDialog.setCanceledOnTouchOutside(true);
			mSharedDialog.show();
		} catch (Exception e) {
			log.e(e);
		}
	}
    
	private void isValidateSinaToken() {
		Intent intent = new Intent(SettingActivity.this, ShareAppToSinaActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 检查更新
	 */
	private void checkUpdate() {
		RequestParams params = new RequestParams();
		params.put("type", CyouApplication.getChannel());
		MyHttpConnect.getInstance().post(HttpContants.NET.CHECK_UPDATE, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("check pengyou update result = " + content);
						try {
							PYUpdateBase base = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.readValue(content, new TypeReference<PYUpdateBase>() {
									});
							if (base != null) {
								PYUpdate mUpdateInfo = base.getData();
								PackageManager pm = getPackageManager();
								PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
								if (mUpdateInfo.getApkversion() > pi.versionCode) {
									Intent intent = new Intent(SettingActivity.this, CheckUpdateService.class);
									intent.putExtra(Params.PENGYOU_UPDATE.TYPE, CheckUpdateService.CHECK);
									startService(intent);
								}else{
									Toast.makeText(SettingActivity.this, R.string.no_new_version, Toast.LENGTH_SHORT).show();
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(SettingActivity.this);
						dialog.create().show();
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						showShortToast(R.string.check_update_net_error);
					}
					});
	}
}
