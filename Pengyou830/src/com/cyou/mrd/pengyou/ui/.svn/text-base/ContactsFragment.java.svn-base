package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ContactsAdapter;
import com.cyou.mrd.pengyou.entity.ContactItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.widget.ContactActionBar;
import com.cyou.mrd.pengyou.widget.PullTitleListView;
import com.cyou.mrd.pengyou.widget.SearchBar;
import com.cyou.mrd.pengyou.widget.SearchBar.TextAndActionListsner;

public class ContactsFragment extends BaseFragment {

	private CYLog log = CYLog.getInstance();

	private PullTitleListView mListView;
	private ContactActionBar mBar;
	private TextView mLetterTV;
	private View mView;
	private TextView mListTitleTV;
	private SearchBar mSearchBar;
	private LinearLayout mContentLL;
	private TextView mEmptyView;

	private Activity mActivity;
	private List<ContactItem> mData;
	private AsyncQueryHandler mQueryHandler;
	private String[] mProjection = {
			ContactsContract.CommonDataKinds.Phone._ID,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
			ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
	private Uri mUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private Map<String, ContactItem> mContactMap = new HashMap<String, ContactItem>();
	private ContactsAdapter mAdapter;
	private Animation loadingInAnim;
	private Animation loadingOutAnim;
	private int mY;
	private Animation mSearchOutAnim;
	private Animation mSearchInAnim;

	public ContactsFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_TELE_ID,
				CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_TELE_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mActivity = getActivity();
		this.loadingInAnim = AnimationUtils.loadAnimation(mActivity,
				R.anim.fading_in);
		this.loadingInAnim.setDuration(600);
		this.loadingOutAnim = AnimationUtils.loadAnimation(mActivity,
				R.anim.fading_out);
		this.loadingOutAnim.setDuration(600);
		int searchBarHeight = getResources().getDimensionPixelOffset(
				R.dimen.search_bar_height);
		this.mSearchInAnim = new TranslateAnimation(0, 0, -searchBarHeight, 0);
		this.mSearchInAnim.setDuration(300);
		this.mSearchInAnim.setFillAfter(true);
		this.mSearchInAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				mSearchBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		this.mSearchOutAnim = new TranslateAnimation(0, 0, 0, -searchBarHeight);
		this.mSearchOutAnim.setDuration(300);
		this.mSearchOutAnim.setFillAfter(true);
		this.mSearchOutAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mSearchBar.setVisibility(View.GONE);
			}
		});
		mView = inflater.inflate(R.layout.contacts, null);
		this.mListView = (PullTitleListView) mView
				.findViewById(R.id.contacts_lv);
		this.mEmptyView = (TextView)mView.findViewById(R.id.contacts_empty);
		this.mContentLL = (LinearLayout)mView.findViewById(R.id.contacts_content_ll);
		this.mBar = (ContactActionBar) mView.findViewById(R.id.contacts_ab);
		this.mLetterTV = (TextView) mView.findViewById(R.id.contacts_dialog_tv);
		this.mListTitleTV = (TextView) mView
				.findViewById(R.id.contacts_title_tv);
		this.mSearchBar = (SearchBar) mView
				.findViewById(R.id.contants_search_bar);
		this.mSearchBar.getEditText().clearFocus();
		this.mSearchBar.getEditText().setOnFocusChangeListener(
				new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							loadingOutAnim.setFillAfter(true);
							mBar.startAnimation(loadingOutAnim);
						} else {
							loadingInAnim.setFillAfter(true);
							mBar.startAnimation(loadingInAnim);
						}
					}
				});
		this.mSearchBar.setTextAndActionListener(new TextAndActionListsner() {

			@Override
			public void onText() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onEmpty() {
				if (mAdapter != null) {
					mAdapter.changeData(mData);
				}
			}

			@Override
			public void onAction(String key) {

				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_TELE_SEARCH_ID,
						CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_TELE_SEARCH_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				Cursor c = null;
				try {
					hideImm();
					if (TextUtils.isDigitsOnly(key)) {
						c = mActivity.getContentResolver().query(
								mUri,
								mProjection,
								ContactsContract.CommonDataKinds.Phone.DATA1
										+ " LIKE " + "'%" + key + "%'", null,
								"sort_key COLLATE LOCALIZED asc");
					} else {
						c = mActivity.getContentResolver().query(mUri,
								mProjection,
								"sort_key LIKE " + "'%" + key + "%'", null,
								"sort_key COLLATE LOCALIZED asc");
					}
					Set<String> set = new HashSet<String>();
					List<ContactItem> list = new ArrayList<ContactItem>();
					while (c.moveToNext()) {
						ContactItem item = new ContactItem();
						item.name = c.getString(1);
						item.num = c.getString(2);
						item.sortKey = c.getString(3);
						if (set.add(item.name)) {
							list.add(item);
						}
					}
					if (mAdapter != null) {
						mAdapter.changeData(list);
					}
				} catch (Exception e) {
					log.e(e);
				} finally {
					if (c != null) {
						c.close();
					}
				}
			}
		});
		WindowManager manager = mActivity.getWindowManager();
		Display display = manager.getDefaultDisplay();
		this.mListView.setTitleView(mListTitleTV, mActivity.getResources()
				.getDimensionPixelSize(R.dimen.contact_title), display
				.getWidth());
		return mView;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mData == null) {
			mData = new ArrayList<ContactItem>();
		}
		getContacts();
	}

	private void getContacts() {
		if (mQueryHandler == null) {
			mQueryHandler = new MyAsyncQueryHandler(
					mActivity.getContentResolver());
		}
		mQueryHandler.startQuery(0, null, mUri, mProjection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}
	
	public void pause(){
		hideImm();
	}
	
	/**
	 * 隐藏软键盘
	 */
	private void hideImm(){
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View v = getActivity().getCurrentFocus();
		if (v != null) {
			IBinder binder = v.getWindowToken();
			if (binder != null) {
				inputMethodManager.hideSoftInputFromWindow(binder,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
	
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			try {
				if (cursor != null && cursor.getCount() > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append("[");
					int i = 0;
					while (cursor.moveToNext()) {
						ContactItem item = new ContactItem();
						item.name = cursor.getString(1);
						item.num = cursor.getString(2);
						item.sortKey = cursor.getString(3);
						if (!mContactMap.containsKey(item.num)) {
							mContactMap.put(item.num, item);
							mData.add(item);
						}
						if (i != cursor.getCount() - 1) {
							sb.append("{\"nm\":\"" + item.name
									+ "\",\"phone\":[\"" + item.num + "\"]},");
						} else {
							sb.append("{\"nm\":\"" + item.name
									+ "\",\"phone\":[\"" + item.num + "\"]}");
						}
						i++;
					}
					sb.append("]");
					String contactStr = sb.toString();
					if (contactStr.length() > 1) {
						contactStr = contactStr.substring(0,
								contactStr.length() - 1)
								+ "]";
					} else {
						contactStr = contactStr.substring(0,
								contactStr.length())
								+ "]";
					}
					mBar.init(mView);
					mBar.setListView(mListView);
					mAdapter = new ContactsAdapter(mActivity, mData, mBar,
							mSearchBar);
					mListView.setAdapter(mAdapter);
					mListView.setOnScrollListener(mAdapter);
					if(mData.isEmpty()){
						mBar.setVisibility(View.GONE);
						mEmptyView.setVisibility(View.VISIBLE);
						mContentLL.setVisibility(View.GONE);
					}else{
						mEmptyView.setVisibility(View.GONE);
						mContentLL.setVisibility(View.VISIBLE);
					}
				}else{
					mEmptyView.setVisibility(View.VISIBLE);
					mContentLL.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				log.e(e);
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}

	}

}
