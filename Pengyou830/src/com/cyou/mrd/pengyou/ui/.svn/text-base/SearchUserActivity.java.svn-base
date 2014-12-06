package com.cyou.mrd.pengyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;

/**
 * 搜索用户
 * 
 * @author wangkang
 * 
 */
public class SearchUserActivity extends BaseActivity {

	private ImageButton mBackBtn;
	TextView emptyView;
	private EditText et_search;
	private long lastTime;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.search_user);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		initView();
		// try{
		// //自动弹出软键盘
		// new Timer().schedule(new TimerTask() {
		// public void run(){
		// ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
		// .toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
		// }
		// }, 500L);
		// }catch (Exception e) {
		// log.e(e);
		// }
	}

	private void initView() {
		//emptyView = (TextView) findViewById(R.id.txt_empty);
		//emptyView.setText(getString(R.string.user_search_nodata_hint));
		//emptyView.setVisibility(View.GONE);
		// emptyView.setImageGone();
		View headerBar = findViewById(R.id.edit_headerbar);
		this.mBackBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView mHeaderTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		mHeaderTV.setText(R.string.search_user_title);
		et_search = (EditText) findViewById(R.id.et_user_searchbar);
		// this.mSearchBar = (SearchBar) findViewById(R.id.user_searchbar);
		// mSearchBar.getEditText().requestFocus();
		// et_search.requestFocus();

		et_search.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				long nowTime = System.currentTimeMillis();
				if (nowTime - lastTime < 1000) {
					lastTime = nowTime;
					return false;
				}
				lastTime = nowTime;
				if (actionId == EditorInfo.IME_ACTION_SEARCH
						|| actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
					String key = et_search.getText().toString();
					if (TextUtils.isEmpty(key.trim())) {
						Toast.makeText(SearchUserActivity.this,
								R.string.text_not_empty, Toast.LENGTH_SHORT)
								.show();
						return false;
					} else {
						// search(key, true);
						Intent intent = new Intent(SearchUserActivity.this,
								SearchUserResultActivity.class);
						intent.putExtra("key", key);
						startActivity(intent);
						et_search.setText("");
					}

				}
				return false;
			}
		});
	}
}
