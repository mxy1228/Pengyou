package com.cyou.mrd.pengyou.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ContactItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.SendSMSActivity;
import com.cyou.mrd.pengyou.viewcache.ContactViewCache;
import com.cyou.mrd.pengyou.widget.ContactActionBar;
import com.cyou.mrd.pengyou.widget.PullTitleListView;
import com.cyou.mrd.pengyou.widget.SearchBar;

public class ContactsAdapter extends BaseAdapter implements SectionIndexer,
		OnScrollListener {

	private CYLog log = CYLog.getInstance();

	private Activity mContext;
	private List<ContactItem> mData;
	private Map<String, Integer> mUserLetter = new HashMap<String, Integer>();
	private LayoutInflater mInflater;
	private String[] mSection;
	private SearchBar mSearchBar;

	public ContactsAdapter(Activity context, List<ContactItem> data,
			ContactActionBar bar, SearchBar searchBar) {
		this.mContext = context;
		this.mData = data;
		for (ContactItem item : mData) {
			String key = getAlpha(item.sortKey);
			if (!mUserLetter.containsKey(key)) {
				mUserLetter.put(key, mData.indexOf(item));
			}
		}
		Set<String> sectionLetters = mUserLetter.keySet();
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);
		Collections.sort(sectionList);
		mSection = new String[sectionList.size()];
		sectionList.toArray(mSection);
		bar.setUserLetters(mUserLetter);
		bar.setLetter(mSection);
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mSearchBar = searchBar;
		if(mUserLetter.isEmpty()){
			bar.setVisibility(View.GONE);
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ContactViewCache contactviewcache;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_item, null);
			contactviewcache = new ContactViewCache(convertView);
			convertView.setTag(contactviewcache);
		} else {
			contactviewcache = (ContactViewCache) convertView.getTag();
		}
		final ContactItem item = (ContactItem) mData.get(position);
		if (item != null) {
			String currentStr = getAlpha(item.sortKey);
			String previewStr = (position - 1) >= 0 ? getAlpha(mData
					.get(position - 1).sortKey) : " ";
			if (!currentStr.equals(previewStr)) {
				contactviewcache.getmTopTV().setVisibility(View.VISIBLE);
				contactviewcache.getmTopTV().setText(currentStr);
			} else {
				contactviewcache.getmTopTV().setVisibility(View.GONE);
			}
			contactviewcache.getmNameTV().setText(item.name);
			contactviewcache.getmNumTV().setText(item.num);
			contactviewcache.getmInviteIBtn().setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							BehaviorInfo behaviorInfo = new BehaviorInfo(
									CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_TELE_ADDFRD_ID,
									CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_TELE_ADDFRD_NAME);
							CYSystemLogUtil.behaviorLog(behaviorInfo);
							Intent intent = new Intent(mContext,
									SendSMSActivity.class);
							intent.putExtra(Params.SEND_SMS.ITEM, item);
							mContext.startActivity(intent);
						}
					});
		} else {
			log.e("info is null");
		}
		return convertView;
	}

	private String getAlpha(String str) {
		if (str == null) {
			return "#";
		}
		if (str.trim().length() == 0) {
			return "#";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(); // ??д???
		} else {
			return "#";
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (view instanceof PullTitleListView) {
			((PullTitleListView) view).titleLayout(firstVisibleItem);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		InputMethodManager inputMethodManager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View v = mContext.getCurrentFocus();
		if (v != null) {
			IBinder binder = v.getWindowToken();
			if (binder != null) {
				inputMethodManager.hideSoftInputFromWindow(binder,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		if (mSearchBar.getEditText().isFocused()) {
			mSearchBar.getEditText().clearFocus();
		}
	}

	@Override
	public int getPositionForSection(int section) {
		String s = mSection[section];
		int position = 0;
		for (ContactItem item : mData) {
			if (getAlpha(item.sortKey).equals(s)) {
				position = mData.indexOf(item);
				break;
			}
		}
		return position;
	}

	@Override
	public int getSectionForPosition(int position) {
		if(position >=0 && position <= mData.size()-1){
			ContactItem item = mData.get(position);
			String letter = getAlpha(item.sortKey);
			for (int i = 0; i < mSection.length; i++) {
				if (letter.equals(mSection[i])) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		return mSection;
	}

	public int getTitleState(int position) {
		if (position < 0 || getCount() == 0) {
			return 0;
		}
		int section = getSectionForPosition(position);
		if (section > mSection.length) {
			return 0;
		}
		int nextSectionPosition = getSectionForPosition(position + 1);
		if (nextSectionPosition != -1 && section == nextSectionPosition - 1) {
			return 2;
		}
		return 1;
	}

	public void setTitleText(View header, int firstVisiblePosition) {
		String letter = getAlpha(mData.get(firstVisiblePosition).sortKey);
		TextView sectionHeader = (TextView) header;
		sectionHeader.setText(String.valueOf(letter));
	}

	public void changeData(List<ContactItem> data) {
		this.mData = data;
		this.notifyDataSetChanged();
	}
}