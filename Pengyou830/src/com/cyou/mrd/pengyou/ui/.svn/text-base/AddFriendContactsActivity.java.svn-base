package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ContactFriendAdapter;
import com.cyou.mrd.pengyou.adapter.SearchAdapter;
import com.cyou.mrd.pengyou.entity.ContactFriendItem;
import com.cyou.mrd.pengyou.entity.ContactItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.ActivityManager;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.SearchBar;
import com.cyou.mrd.pengyou.widget.SearchBar.TextAndActionListsner;
import com.loopj.android.http.RequestParams;

public class AddFriendContactsActivity extends CYBaseActivity {

	private CYLog log = CYLog.getInstance();
//	private RelativeLayout mContentLL;
	private ListView mListView;
	private Animation loadingInAnim;
	private Animation loadingOutAnim;
//	private Map<String, ContactFriendItem> map;
	private SearchBar mSearchBar;
	private ListView search_friend;
	private List<ContactFriendItem> allcontacts;// 全部数据
	private List<ContactFriendItem> lists;
	private List<ContactFriendItem> mContactList = new ArrayList<ContactFriendItem>();
	private List<ContactFriendItem> mContactList2 = new ArrayList<ContactFriendItem>();
	private List<ContactFriendItem> filterList = new ArrayList<ContactFriendItem>();
	private List<ContactFriendItem> filterList2 = new ArrayList<ContactFriendItem>();
	private List<ContactFriendItem> isAtt;
	Set<String> set = new HashSet<String>();// 过滤重复号码
	private List<ContactFriendItem> items;// 搜索之后的数据集合
//	private List<String> allnumbers;
	private ContactFriendAdapter mAdapter;
	private SearchAdapter sAdapter;
	private boolean isFromSearch;

	// 通讯录为空
	private LinearLayout ll_contact_epmty;
	private TextView tv_contact_empty;

	private AsyncQueryHandler mQueryHandler;
	private String[] mProjection = {
			ContactsContract.CommonDataKinds.Phone._ID,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
			ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
	private Uri mUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private MyHttpConnect mConn;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_friend_contacts);
		ActivityManager.getInstance().addActivity(this);
		isFromSearch = getIntent().getBooleanExtra("search_from", false);
		initView();
		initData();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		try {
//			mContentLL = (RelativeLayout) findViewById(R.id.add_friend_contacts_content_ll);
			ll_contact_epmty = (LinearLayout) findViewById(R.id.ll_contact_listview);
			tv_contact_empty = (TextView) findViewById(R.id.tv_empty);
			View headerBar = findViewById(R.id.edit_headerbar);
			mSearchBar = (SearchBar) findViewById(R.id.search_from_contact_searchbar);

			ImageButton mBackBtn = (ImageButton) headerBar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			search_friend = (ListView) findViewById(R.id.search_frienf_lv);
			mBackBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			if(isFromSearch)
				//隐藏返回按钮,但仍然保留返回键占的区域,headerTV好处于居中位置
			    mBackBtn.setVisibility(View.INVISIBLE);
			TextView mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			mHeaderTV.setText(R.string.phone_contact);
			Button mOkBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			mOkBtn.setText(R.string.btn_finish);
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mOkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});

			if(isFromSearch)
			   mOkBtn.setVisibility(View.VISIBLE);
			else
			   mOkBtn.setVisibility(View.INVISIBLE);

			mListView = (ListView) findViewById(R.id.add_friend_contacts_lv);
			mListView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					View v = AddFriendContactsActivity.this.getCurrentFocus();
					if (v != null) {
						IBinder binder = v.getWindowToken();
						if (binder != null) {
							inputMethodManager.hideSoftInputFromWindow(binder,
									InputMethodManager.HIDE_NOT_ALWAYS);
						}
					}
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
				}
			});
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		try {
			allcontacts = new ArrayList<ContactFriendItem>();
			lists = new ArrayList<ContactFriendItem>();
			items = new ArrayList<ContactFriendItem>();
			isAtt = new ArrayList<ContactFriendItem>();
//			allnumbers = new ArrayList<String>();
			this.mConn = MyHttpConnect.getInstance();
			this.loadingInAnim = AnimationUtils.loadAnimation(this,
					R.anim.fading_in);
			this.loadingInAnim.setDuration(600);
			this.loadingOutAnim = AnimationUtils.loadAnimation(this,
					R.anim.fading_out);
			this.loadingOutAnim.setDuration(600);
			getContacts();
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	protected void onDestroy() {
		mAdapter = null;
		super.onDestroy();
	}

	private void getContacts() {
		if (mQueryHandler == null) {
			mQueryHandler = new MyAsyncQueryHandler(getContentResolver());
		}
		mQueryHandler.startQuery(0, null, mUri, mProjection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
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
						item.num = cursor.getString(2) != null ? cursor.getString(2).replace(" ", "") : cursor.getString(2);
						item.sortKey = cursor.getString(3);

						if (set.add(item.num) && !isBindPhoneNumber(item.num)) {
							filterList.add(new ContactFriendItem(item.num,
									item.name, null, null, null, 1, 0));
						}

						if (i != cursor.getCount() - 1) {
							sb.append("{\"name\":\"" + item.name
									+ "\",\"phone\":\"" + item.num + "\"},");
						} else {
							sb.append("{\"name\":\"" + item.name
									+ "\",\"phone\":\"" + item.num + "\"}");
						}
						if (item.name == null && item.num == null) {
							mSearchBar.setVisibility(View.GONE);
							ll_contact_epmty.setVisibility(View.GONE);
							tv_contact_empty.setText(R.string.contants_is_empty);
							tv_contact_empty.setVisibility(View.VISIBLE);
							return;
						}
						i++;
					}
					set.clear();

					for (int j = 0; j < filterList.size(); j++) {
						Log.d("luochuang3", "alldate:"
								+ filterList.get(j).getPhone());
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
					loadContactsList(contactStr);
				} else {
					mSearchBar.setVisibility(View.GONE);
					ll_contact_epmty.setVisibility(View.GONE);
					tv_contact_empty.setText(R.string.contants_is_empty);
					tv_contact_empty.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				finish();
				log.e(e);
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
		}
	}

	private void selectData(List<ContactFriendItem> lists, String key) {
		if(lists != null && !lists.isEmpty()) {
			Iterator<ContactFriendItem> it = lists.iterator();
			while(it.hasNext()){
				ContactFriendItem item = it.next();
				if(item.getName() != null && item.getName().contains(key) || item.getPhone() != null && item.getPhone().contains(key)){
					items.add(item);
				}
			}
		}
	}

//	private void filterDate(List<ContactFriendItem> items) {
//		for (int j = 0; j < items.size(); j++) {
//			// 3已经关注 4 互相关注
//			if (items.get(j).getRelation() == 3
//					|| items.get(j).getRelation() == 4) {
//				isAtt.add(items.get(j));
//				items.remove(j);
//			}
//		}
//	}
//
//	public void filterSameDate(List<ContactFriendItem> items,
//			List<ContactFriendItem> allDate) {
//		for (int i = 0; i < items.size(); i++) {
//			allnumbers.add(items.get(i).getPhone());
//		}
//		for (int i = 0; i < allDate.size(); i++) {
//			if (allnumbers.contains(allDate.get(i).getPhone())) {
//				allDate.remove(i);
//			}
//		}
//	}
//
//	public void filterServer(List<ContactFriendItem> items) {
//		set.clear();
//		for (int i = 0; i < items.size(); i++) {
//			if (!set.add(items.get(i).getPhone())) {
//				items.remove(i);
//			}
//		}
//	}
	private boolean isBindPhoneNumber(String num) {
		String bindNum = UserInfoUtil.getPhoneNumber();
		bindNum = bindNum == null ? bindNum : bindNum.replace(" ", "");
		if (num != null && bindNum != null) {
			if (num.equals(bindNum)) {
				return true;
			}
		}
		return false;
	}

	private void loadContactsList(final String contactStr) {
		RequestParams params = new RequestParams();
		params.put("contacts", contactStr);

		mConn.post2Json(
				HttpContants.NET.UPLOAD_CONTACTS,
				params,
				new JSONAsyncHttpResponseHandler<ContactFriendItem>(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						ContactFriendItem.class) {
					@Override
					public void onSuccessForList(List list) {
						mContactList = list;
						/* TEST CODE
						if(list == null
								|| list.size() == 0){
							for(int i=0; i < filterList.size();i++){
								if(i == 5){
									filterList.get(i).setUid("111");
									filterList.get(i).setNickname("555555");
									mContactList.add(filterList.get(i));
									
								}
								if(i == 8){
									filterList.get(i).setUid("101");
									filterList.get(i).setNickname("88888");
									mContactList.add(filterList.get(i));
									
								}
		                        if(i== 6){
		                        	filterList.get(i).setUid("222");
									filterList.get(i).setNickname("666666");
									mContactList.add(filterList.get(i));
								}
								if(i== 7){
									filterList.get(i).setUid("333");
									filterList.get(i).setNickname("777777");
									filterList.get(i).setRelation(2);
									mContactList.add(filterList.get(i));
								}
								
								if(i== 9){
									filterList.get(i).setUid("444");
									filterList.get(i).setNickname("9999999999");
									filterList.get(i).setRelation(2);
									mContactList.add(filterList.get(i));
								}
							}
							
							
						}*/
				 //LIST 过滤---------------------------------
						if (mContactList != null && mContactList.size() > 0) {
							for (int k = 0; k < mContactList.size(); k++) {
								Log.d("luochuang3", "already:"
										+ mContactList.get(k)
												.getPhone());
							}
							for(int i= 0; i < filterList.size(); i++){
								filterList2.add(filterList.get(i));
							}

							for(int m=0; m<filterList2.size(); m++){
								for(int n=0; n< mContactList.size();n++){
									if(filterList2.get(m).getPhone().equals(mContactList.get(n).getPhone())){
										filterList.remove(filterList2.get(m));
									}
								}
							}

							for(int i= 0; i < mContactList.size(); i++){
								mContactList2.add(mContactList.get(i));
							}
							for(int m=0; m<mContactList2.size(); m++){
								if(mContactList2.get(m).isAttention()){
									mContactList.remove(mContactList2.get(m));
									isAtt.add(mContactList2.get(m));

								}
							}
							filterList2.clear();
							mContactList2.clear();

					//过滤结束-----------------------------------------------------
						if (mContactList.size() > 0
									&& !mContactList.isEmpty()) {
								allcontacts.addAll(mContactList);
						}

						}
						if (filterList.size() > 0) {
							allcontacts
									.addAll(filterList);
						}

						if (isAtt.size() > 0) {
							allcontacts
									.addAll(isAtt);
						}

						if (mAdapter == null) {
							mAdapter = new ContactFriendAdapter(
									AddFriendContactsActivity.this,
									allcontacts);
						}
						mListView.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
						lists.addAll(mContactList);
						lists.addAll(filterList);
						lists.addAll(isAtt);
						mSearchBar
								.setTextAndActionListener(new TextAndActionListsner() {

									@Override
									public void onText() {

									}

									@Override
									public void onEmpty() {
										mListView
												.setVisibility(View.VISIBLE);
										search_friend
												.setVisibility(View.GONE);

									}

									@Override
									public void onAction(String key) {
										items.clear();
										mListView
												.setVisibility(View.GONE);
										search_friend
												.setVisibility(View.VISIBLE);
										// 搜索数据
										selectData(lists, key);
										if(items.isEmpty()) {
											tv_contact_empty.setText(R.string.search_is_empty);
											tv_contact_empty.setVisibility(View.VISIBLE);
										} else {
											tv_contact_empty.setVisibility(View.GONE);
										}
										sAdapter = new SearchAdapter(
												AddFriendContactsActivity.this,
												items);
										search_friend
												.setAdapter(sAdapter);
									}
								});
						super.onSuccessForList(list);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								AddFriendContactsActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onFailure(Throwable error) {
						super.onFailure(error);
						 showNetErrorDialog(AddFriendContactsActivity.this,new ReConnectListener() {								
								@Override
								public void onReconnect() {
									loadContactsList(contactStr);
								}
							});
					}
				});
	}

}