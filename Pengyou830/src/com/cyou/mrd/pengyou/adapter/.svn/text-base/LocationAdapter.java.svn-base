package com.cyou.mrd.pengyou.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.log.CYLog;

public class LocationAdapter extends BaseExpandableListAdapter {

	private CYLog log = CYLog.getInstance();

	private String[] parents = null;
	private String[][] childs = null;
	private LayoutInflater mInflater;
	private Context mContext;
	private ViewChildVHolder viewChildVHolder = null;
	private ViewGroupHolder viewGroupHolder = null;
	
	public LocationAdapter(String[] parent, String[][] childs, Context context) {
		this.parents = parent;
		this.childs = childs;
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.childs[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			viewChildVHolder = new ViewChildVHolder();
			convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.location_child, null);
			viewChildVHolder.textview = (TextView) convertView
					.findViewById(R.id.location_child_tv);
			convertView.setTag(viewChildVHolder);
		} else {
			viewChildVHolder = (ViewChildVHolder) convertView.getTag();
		}

		viewChildVHolder.textview
				.setText(this.childs[groupPosition][childPosition]);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this.childs[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.parents[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return this.parents.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		

		if (convertView == null) {
			convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.location_parent, null);
			viewGroupHolder = new ViewGroupHolder();
			viewGroupHolder.textview = (TextView) convertView
					.findViewById(R.id.location_parent_tv);
			convertView.setTag(viewGroupHolder);

		} else {
			viewGroupHolder = (ViewGroupHolder) convertView.getTag();
		}
		viewGroupHolder.textview.setText(this.parents[groupPosition]);
		return convertView;
	}

	class ViewGroupHolder {
		TextView textview;
	}

	class ViewChildVHolder {
		TextView textview;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
