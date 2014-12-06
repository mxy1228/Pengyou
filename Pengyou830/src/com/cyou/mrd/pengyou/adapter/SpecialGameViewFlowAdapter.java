package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.SeminarBean;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.GameSpecialDetailActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 分类顶部滑动视图对应Adapter
 * 
 * @author wangkang
 * 
 */
public class SpecialGameViewFlowAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	private Context mContext;
	private LayoutInflater mInflater;
	private List<SeminarBean> classifyGameLst;
	private DisplayImageOptions mOptions;
	int width;
	int height;
	private TextView txtView;

	public SpecialGameViewFlowAdapter(Context context,
			List<SeminarBean> mClassifyGameLst, int mHeight,
			TextView txtSeminarInfo) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		classifyGameLst = mClassifyGameLst;
		this.mOptions = new DisplayImageOptions.Builder().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading()
				.showImageForEmptyUri(R.drawable.gamestore_sp_bg)
				.showImageOnFail(R.drawable.gamestore_sp_bg)
				.showStubImage(R.drawable.gamestore_sp_bg)
				.build();
		width = Util.getScreenWidthSize((Activity) mContext);
		height = mHeight;
		txtView = txtSeminarInfo;
	}

	/**
	 * 更新数据
	 * 
	 * @param mClassifyGameLst
	 */
	public void updateListView(List<SeminarBean> mClassifyGameLst) {
		if (null == mClassifyGameLst) {
			return;
		}
		classifyGameLst = mClassifyGameLst;
	}

	public int getCount() {
		if (null == classifyGameLst) {
			return 0;
		}
		return Integer.MAX_VALUE;
	}

	public Object getItem(int i) {
		if (null == classifyGameLst || classifyGameLst.size() <= i) {
			return null;
		}
		return classifyGameLst.get(i);
	}

	public long getItemId(int i) {
		return (long) i;
	}

	public View getView(int i, View view, ViewGroup viewgroup) {
		try {
			if (view == null) {
				view = mInflater.inflate(R.layout.game_special_viewflow_item,
						null);
			}
			if (classifyGameLst == null || classifyGameLst.size() == 0) {
				return view;
			}
			int temp = i % classifyGameLst.size();
			final SeminarBean gameItem = classifyGameLst.get(temp);
			ImageView imageview = (ImageView) view.findViewById(R.id.imgView);
			if (!TextUtils.isEmpty(gameItem.getDesc())) {
				txtView.setText(gameItem.getDesc());
			}
			imageview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					BehaviorInfo behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_ADS_ID,
							CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_ADS_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					// 跳转至专题详细
					Intent mIntent = new Intent();
					log.d("专题ID：" + gameItem.getId());
					mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_ID,
							gameItem.getId());
					mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_NAME,
							gameItem.getName());
					mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_DATE,
							gameItem.getTopicdate());
					mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_IMAGE,
							gameItem.getPicture());
					mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_DESC,
							gameItem.getDesc());
					mIntent.setClass(mContext, GameSpecialDetailActivity.class);
					mContext.startActivity(mIntent);
				}
			});
			if (!TextUtils.isEmpty(gameItem.getPicture())) {
				CYImageLoader.displayGameImg(gameItem.getPicture(), imageview,
						mOptions);
				// CYImageLoader.displayOtherImage(gameItem.getPicture(),
				// imageview, mOptions);
				// imageview.setLayoutParams(new LayoutParams(width, height));
				view.setLayoutParams(new LayoutParams(width, height));
			}
			return view;
		} catch (Exception e) {
			log.e(e);
		}
		return null;
	}

}
