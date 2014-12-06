package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ContactFriendItem;
import com.cyou.mrd.pengyou.ui.FriendInfoActivity;
import com.cyou.mrd.pengyou.ui.SendSMSActivity;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;

public class SearchAdapter extends BaseAdapter {

	private Activity mActivity;
	private List<ContactFriendItem> searchList;
	private static final int PAY_ATTENTION = 1;

	public SearchAdapter(Activity activity, List<ContactFriendItem> list) {
		this.mActivity = activity;
		this.searchList = list;
	}

	@Override
	public int getCount() {
		return searchList.size();
	}

	@Override
	public Object getItem(int position) {
		return searchList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = View.inflate(mActivity, R.layout.search_result, null);
		LinearLayout item_click = (LinearLayout) view.findViewById(R.id.item_click);
		TextView tv_name = (TextView) view.findViewById(R.id.contact_item_name_tv);
		TextView tv_num = (TextView) view.findViewById(R.id.contact_item_num_tv);
		final Button bt_status = (Button) view.findViewById(R.id.contact_item_letter_ibtn);
		final ContactFriendItem item = searchList.get(position);
		tv_name.setText(item.getName());
		tv_num.setText(item.getPhone());
		if (item.getUid() == null) {
			bt_status.setEnabled(true);
			bt_status.setText(R.string.invite);
			bt_status.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mActivity, SendSMSActivity.class);
					intent.putExtra(Params.SEND_SMS.ITEM, item);
					mActivity.startActivity(intent);
				}
			});
		} else {
			item_click.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 显示用户详情界面
					Intent intent = new Intent(mActivity,
							FriendInfoActivity.class);
					intent.putExtra(Params.FRIEND_INFO.UID,
							Integer.parseInt(item.getUid()));
					intent.putExtra(Params.FRIEND_INFO.NICKNAME,
							item.getNickname());
					intent.putExtra(Params.FRIEND_INFO.GENDER, item.getGender());
					mActivity.startActivity(intent);
				}
			});
			if (!(TextUtils.isEmpty(item.getUid())) && !item.isAttention()) {//如果是未关注
				bt_status.setEnabled(true);
				bt_status.setText(R.string.focus);
				bt_status.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int uid = Integer.parseInt(item.getUid());
						new RelationUtil(mActivity).focus(uid, PAY_ATTENTION,
								null, new ResultListener() {
									@Override
									public void onSuccuss(boolean eachFocused) {
										/*
										 * bt_status
										 * .setBackgroundResource(R.drawable
										 * .had_attention_btn_normal);
										 * bt_status.setText("");
										 */
										bt_status.setBackgroundResource(0);
										bt_status.setEnabled(false);
										bt_status.setText(R.string.had_focus);
										item.setRelation(3);
										searchList.set(position, item);
										notifyDataSetChanged();
									}

									@Override
									public void onFailed() {
										Toast.makeText(mActivity,
												R.string.focus_failed,
												Toast.LENGTH_SHORT).show();
									}
								});
					}
				});
			} else {//如果是已关注
				/*
				 * bt_status
				 * .setBackgroundResource(R.drawable.had_attention_btn_normal);
				 * bt_status.setOnClickListener(null);
				 */
				bt_status.setBackgroundResource(0);
				bt_status.setEnabled(false);
				bt_status.setText(R.string.had_focus);
			}}
		return view;
	}
}