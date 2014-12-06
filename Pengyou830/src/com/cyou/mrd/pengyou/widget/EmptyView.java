package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

/**
 * 列表空数据时加载视图
 * 
 * @author wangkang
 * 
 */
public class EmptyView extends RelativeLayout {
	private Context mContext;
	private View rootView;
	private TextView txtNodata;
	private ImageView imageView;

	public EmptyView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public EmptyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		rootView = LayoutInflater.from(mContext).inflate(
				R.layout.empty_view_layout, this);
		txtNodata = (TextView) rootView.findViewById(R.id.txt_empty);
		imageView = (ImageView) rootView.findViewById(R.id.img_nodata);
	}

	/**
	 * 设置无数据时的文字和图片提示
	 * 
	 * @param text
	 * @param drawable
	 */
	public void setText(String text) {
		if (!TextUtils.isEmpty(text)) {
			txtNodata.setText(text);
		}
	}

	/**
	 * 隐藏
	 */
	public void setImageGone() {
		imageView.setVisibility(View.GONE);
	}

	/**
	 * 设置默认图片
	 * 
	 * @param resId
	 */
	public void setImageResource(int resId) {
		if (resId != 0) {
			imageView.setImageResource(resId);
		}
	}
}
