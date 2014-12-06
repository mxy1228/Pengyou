package com.cyou.mrd.pengyou.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.log.CYLog;

public class GetContactsActivity extends BaseActivity {

	private CYLog log = CYLog.getInstance();

	private final int REGISTER = 1;// 从RegisterActivity跳转过来
	private final int ADD_FRIEND = 2;// 从AddFriendActivity跳转过来

	private Button mBtn;

	private int mFrom;// 从哪个页面跳转过来的标记

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		try {
			Intent intent = getIntent();
			mFrom = intent.getIntExtra("from", 0);
			setContentView(R.layout.get_contact);
			View headerBar = findViewById(R.id.edit_headerbar);
			ImageButton mBackBtn = (ImageButton) headerBar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			mBackBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			mBackBtn.setVisibility(View.INVISIBLE);
			TextView mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			mHeaderTV.setText(R.string.app_name);
			mBtn = (Button) findViewById(R.id.get_contacts_btn);
			mBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// Intent intent = new
					// Intent(GetContactsActivity.this,UploadContactsService.class);
					// startService(intent);
					Intent enter = new Intent();
					switch (mFrom) {
					case REGISTER:
						enter.setClass(GetContactsActivity.this,
								MainActivity.class);
						enter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(enter);
						finish();
						break;
					case ADD_FRIEND:
						enter.setClass(GetContactsActivity.this,
								AddFriendContactsActivity.class);
						enter.putExtra("search_from", true);
						startActivity(enter);
						finish();
						break;
					default:
						break;
					}
					GetContactsActivity.this.finish();
				}
			});
		} catch (Exception e) {
			log.e(e);
		}
	}

}
