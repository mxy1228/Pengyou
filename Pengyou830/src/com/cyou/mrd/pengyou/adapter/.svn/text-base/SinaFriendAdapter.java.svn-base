package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.SinaFriendItem;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.AddFriendSinaActivity;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.viewcache.SinaFriendViewCache;

public class SinaFriendAdapter extends BaseAdapter implements SectionIndexer,
		OnScrollListener {

	private CYLog log = CYLog.getInstance();
	private static final int PAY_ATTENTION = 1;
	private static final int CANCEL_ATTENTION = 0;
	private Context mContext;
	private List<SinaFriendItem> mData;
	private LayoutInflater mInflater;
	private String[] mSection;
	private MyHttpConnect mConn;
	AddFriendSinaActivity addConFriendAct;

	public SinaFriendAdapter(Context context, List<SinaFriendItem> data,
			AddFriendSinaActivity maddConFriendAct) {
		this.mContext = context;
		this.mData = data;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		addConFriendAct = maddConFriendAct;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public void updateListData(List<SinaFriendItem> data) {
		mData = data;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	SinaFriendViewCache contactviewcache;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.sina_friend_item, null);
			contactviewcache = new SinaFriendViewCache(convertView);
			convertView.setTag(contactviewcache);
		} else {
			contactviewcache = (SinaFriendViewCache) convertView.getTag();
		}
		final SinaFriendItem item = mData.get(position);
		if (item != null) {
			contactviewcache.getTxtPyouNickName().setText(item.getNickname());
			contactviewcache.getTxtTeleName().setText(item.getSnsnicknm());
			if (item.isAttention()) {// 若已经关注
				/*contactviewcache.getmInviteIBtn().setBackgroundResource(
						R.drawable.had_attention_btn_normal);
				contactviewcache.getmInviteIBtn().setOnClickListener(null);
				contactviewcache.getmInviteIBtn().setText("");*/
				contactviewcache.getmInviteIBtn().setBackgroundResource(0);
				contactviewcache.getmInviteIBtn().setEnabled(false);
				contactviewcache.getmInviteIBtn().setText(R.string.had_focus);
			} else {
				contactviewcache.getmInviteIBtn().setBackgroundResource(
						R.drawable.focus_btn_xbg);
				contactviewcache.getmInviteIBtn().setEnabled(true);
				contactviewcache.getmInviteIBtn().setText(R.string.focus);
				contactviewcache.getmInviteIBtn().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								int postion = mData.indexOf(item);
								int uid = 0;
								try {
									uid = Integer.parseInt(item.getUid());
									focus(PAY_ATTENTION, uid, item, postion,
											contactviewcache.getmInviteIBtn());
								} catch (Exception e) {
									log.e(e);
									Toast.makeText(mContext, mContext.getString(R.string.add_sina_frd_error), 1).show();
								}
							}
						});
			}
		} else {
			log.e("info is null");
		}
		return convertView;
	}

	private void focus(int isfocus, int mUID, final SinaFriendItem item,
			final int postion, final Button imgButton) {
		if (0 == mUID) {
			log.e("mUID is null");
			Toast.makeText(mContext, mContext.getString(R.string.add_sina_frd_error), 1).show();
			return;
		}
		new RelationUtil(mContext).focus(mUID, isfocus,
				RelationUtil.SINA_WEIBO, new ResultListener() {

					@Override
					public void onSuccuss(boolean eachFocused) {
						Toast.makeText(mContext, R.string.focus_success,
								Toast.LENGTH_SHORT).show();
						/*imgButton.setText("");
						imgButton
						.setBackgroundResource(R.drawable.had_attention_btn_normal);*/
						imgButton.setBackgroundResource(0);
						imgButton.setEnabled(false);
						imgButton.setText(R.string.had_focus);
						item.setAttention(true);
						mData.set(postion, item);
						notifyDataSetChanged();
					}

					@Override
					public void onFailed() {
						Toast.makeText(mContext, R.string.focus_failed,
								Toast.LENGTH_SHORT).show();
					}

				});

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
