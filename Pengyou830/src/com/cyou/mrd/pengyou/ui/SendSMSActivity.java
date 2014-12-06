package com.cyou.mrd.pengyou.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ContactFriendItem;
import com.cyou.mrd.pengyou.entity.ContactItem;
import com.cyou.mrd.pengyou.log.CYLog;

public class SendSMSActivity extends BaseActivity implements OnClickListener {

	private CYLog log = CYLog.getInstance();

	private EditText mET;
	private Button mSendBtn;
	private TextView mNameTV;
	private ImageButton mBackBtn;
	private TextView mTitleTV;

	private ContactItem mItem;
	private ContactFriendItem items;

	private String number;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.send_message);
		initView();
		initData();
	}

	private void initView() {
		this.mET = (EditText) findViewById(R.id.send_message_et);
		this.mSendBtn = (Button) findViewById(R.id.send_message_btn);
		this.mSendBtn.setOnClickListener(this);
		this.mNameTV = (TextView) findViewById(R.id.send_message_name_tv);
		View headerbar = findViewById(R.id.send_msg_header_bar);
		this.mBackBtn = (ImageButton) headerbar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mBackBtn.setOnClickListener(this);
		this.mTitleTV = (TextView) headerbar
				.findViewById(R.id.sub_header_bar_tv);
		String content = getResources().getString(R.string.sms_content,
				PYVersion.IP.APK_URL_PY);
		mET.setText(content);
		this.mSendBtn.setText(R.string.send);
	}

	private void initData() {
		Intent intent = getIntent();

		if (intent.getSerializableExtra(Params.SEND_SMS.ITEM) instanceof ContactItem) {
			mItem = (ContactItem) intent
					.getSerializableExtra(Params.SEND_SMS.ITEM);
			this.mNameTV.setText(mItem.name);
			this.mTitleTV.setText(mItem.name);
			number = mItem.num;
		} else {
			items = (ContactFriendItem) intent
					.getSerializableExtra(Params.SEND_SMS.ITEM);
			this.mNameTV.setText(items.getName());
			this.mTitleTV.setText(items.getName());
			number = items.getPhone();
		}

		if (mItem == null) {
			log.e("item is null");
			return;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			SendSMSActivity.this.finish();
			break;
		case R.id.send_message_btn:
			String content = mET.getText().toString();
			if (content != null && content.trim().length() != 0) {
				try {
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					if (tm.getSimState() != TelephonyManager.SIM_STATE_READY) {
						Toast.makeText(SendSMSActivity.this,
								R.string.sim_error, Toast.LENGTH_SHORT).show();
						return;
					}
					SmsManager manager = SmsManager.getDefault();
					List<String> smsDivs = manager.divideMessage(content);
					for (String str : smsDivs) {
						manager.sendTextMessage(number, null, str, null,
								null);
					}
					Toast.makeText(SendSMSActivity.this,
							R.string.send_sms_success, Toast.LENGTH_SHORT)
							.show();
					SendSMSActivity.this.finish();
				} catch (Exception e) {
					Toast.makeText(SendSMSActivity.this,
							R.string.send_sms_failed, Toast.LENGTH_SHORT)
							.show();
					log.e(e);
				}
			} else {
				Toast.makeText(SendSMSActivity.this,
						R.string.please_input_sms_content, Toast.LENGTH_SHORT)
						.show();
			}
			break;
		default:
			break;
		}
	}
}
