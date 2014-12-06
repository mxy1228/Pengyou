package com.cyou.mrd.pengyou.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
/**
 * 意见反馈成功页
 * @author 
 *
 */
public class FeedBackSubActivity extends BaseActivity implements
		OnClickListener {

	private ImageButton ib_feed_back;
	private TextView tv_title;
	private Button bt_right;
	private TextView mFeedBackTV;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.feed_back_sub);
		View headerView = this.findViewById(R.id.sub_feed_back_sub);
		ib_feed_back = (ImageButton) headerView
				.findViewById(R.id.sub_header_bar_left_ibtn);
		bt_right = (Button) headerView
				.findViewById(R.id.sub_header_bar_right_ibtn);
		bt_right.setVisibility(View.INVISIBLE);
		tv_title = (TextView) findViewById(R.id.sub_header_bar_tv);
		this.mFeedBackTV = (TextView)findViewById(R.id.feed_back_sub_tv);
		SharedPreferences sp = getSharedPreferences(Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
		String feedback_finish = sp.getString(Params.SP_PARAMS.FEED_BACK_FINISH, null);
		if(feedback_finish != null && !TextUtils.isEmpty(feedback_finish)){
			this.mFeedBackTV.setText(feedback_finish);
		}
		this.tv_title.setText(R.string.feed);
		ib_feed_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
//			Intent intent = new Intent(FeedBackSubActivity.this,
//					MainActivity.class);
//			startActivity(intent);
			FeedBackSubActivity.this.finish();
			break;
		}
	}
}
