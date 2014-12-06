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
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.AdvertBean;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.GameCircleActivity;
import com.cyou.mrd.pengyou.ui.GameCircleDetailActivity;
import com.cyou.mrd.pengyou.ui.GameGiftDetailActivity;
import com.cyou.mrd.pengyou.ui.GameSpecialDetailActivity;
import com.cyou.mrd.pengyou.ui.GameStoreWebActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 分类顶部滑动视图对应Adapter
 * 
 * @author wangkang
 * 
 */
public class RecomGameViewFlowAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	private Context mContext;
	private LayoutInflater mInflater;
	private List<AdvertBean> classifyGameLst;
	private DisplayImageOptions mOptions;
	int width;
	int height;
	private TextView txtDesc;

	public RecomGameViewFlowAdapter(Context context,
			List<AdvertBean> mClassifyGameLst, int mHeight, TextView mTxtDesc) {
		mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		classifyGameLst = mClassifyGameLst;
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.gamestore_recommend_defaul_bg)
				.showImageForEmptyUri(R.drawable.gamestore_recommend_defaul_bg)
				.showImageOnFail(R.drawable.gamestore_recommend_defaul_bg)
				.cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).resetViewBeforeLoading()
				.build();
		width = Util.getScreenWidthSize((Activity) mContext);
		height = mHeight;
		txtDesc = mTxtDesc;
	}

	/**
	 * 更新数据
	 * 
	 * @param mClassifyGameLst
	 */
	public void updateListView(List<AdvertBean> mClassifyGameLst) {
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
				view = mInflater.inflate(R.layout.top_recom_game_viewflow_item,
						null);
			}
			if (classifyGameLst == null || classifyGameLst.size() == 0) {
				return view;
			}
			int temp = i % classifyGameLst.size();
			final AdvertBean gameItem = classifyGameLst.get(temp);
			ImageView imageview = (ImageView) view.findViewById(R.id.imgView);
			if (null != txtDesc) {
				txtDesc.setText(gameItem.getDesc());
			}
			imageview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					BehaviorInfo behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_ADS_ID,
							CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_ADS_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					// 跳转至游戏详细
					Intent intent = new Intent();
					log.d("game value is:" + gameItem.getUri());
					String type = Util.getAdsType(gameItem.getUri());
					if (TextUtils.isEmpty(type)) {
						Toast.makeText(
								mContext,
								mContext.getText(R.string.gamestore_error_params),
								0).show();
						return;
					}
					String value = "";
					value = Util.getGameCodeByUrl(gameItem.getUri(), type);
					if (Contants.SCROLL_ADS.GAME.equals(type)) {// 游戏详细
						String gameName = gameItem.getName();
						intent.putExtra(Params.INTRO.GAME_CODE, value);
						intent.setClass(mContext,
								GameCircleDetailActivity.class);
						intent.putExtra(Params.INTRO.GAME_NAME, gameName);
					} else if (Contants.SCROLL_ADS.TOPIC.equals(type)) {// 专题
						// 跳转至专题详细
						intent.putExtra(Params.GAME_SPECIAL.SPECIAL_ID, value);
						intent.setClass(mContext,
								GameSpecialDetailActivity.class);
					} else if (Contants.SCROLL_ADS.CIRCLE.equals(type)) {// 游戏圈
						intent.putExtra(Params.Dynamic.GAME_CIRCLE_ID, value);
						if (Util.isInstallByread(gameItem.getIdentifier())) {
							intent.putExtra(Params.INTRO.GAME_ISINSTALLED, true);
						} else {
							intent.putExtra(Params.INTRO.GAME_ISINSTALLED,
									false);
						}
						intent.setClass(mContext, GameCircleActivity.class);
						intent.putExtra(Params.INTRO.GAME_CODE,
								gameItem.getGamecode());
					    intent.putExtra(Params.INTRO.GAME_PKGE,gameItem.getIdentifier());
					} else if (Contants.SCROLL_ADS.WEB.equals(type)) {// HTML
						intent.putExtra(Params.GAMESTORE_HTML, value);
						intent.putExtra(Params.GAMESTORE_HTML_NAME,
								gameItem.getName());
						intent.setClass(mContext, GameStoreWebActivity.class);
					} else if (Contants.SCROLL_ADS.GIFT.equals(type)) {// 礼包
						// 跳转至礼包详细
						intent.putExtra(Params.DYNAMIC_DETAIL.GAMEGIFTID, value);
						intent.setClass(mContext, GameGiftDetailActivity.class);
					} else if (Contants.SCROLL_ADS.GLOBALACT.equals(type)) {// 广场
						if(null!=CyouApplication.getMainActiv()){
							CyouApplication.getMainActiv().intent2GRelationCircle();
						}
						return;
					} else {
						Toast.makeText(
								mContext,
								mContext.getText(R.string.gamestore_error_params),
								0).show();
						return;
					}
					intent.putExtra(Params.INTRO.GAME_CIRCLE, false);
					mContext.startActivity(intent);
				}
			});
			if (!TextUtils.isEmpty(gameItem.getImg())) {
				CYImageLoader.displayGameImg(gameItem.getImg(), imageview,
						mOptions);
				imageview.setLayoutParams(new LayoutParams(width, height));
				view.setLayoutParams(new LayoutParams(width, height));
			}
			return view;
		} catch (Exception e) {
			log.e(e);
		}
		return null;
	}
}
