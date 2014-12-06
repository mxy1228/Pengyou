package com.cyou.mrd.pengyou.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.InstallGameFragment;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.InstallGameViewCache;
import com.cyou.mrd.pengyou.widget.ViewToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 游戏 卸载
 * 
 * @author wangkang
 * 
 */
public class InstallGameAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();

	private List<GameItem> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	public static final int SHOW_NUM = 2;
	public InstallGameAdapter(Context context, List<GameItem> data) {
		this.mContext = context;
		this.mData = data;
		if(mContext==null){
			mContext=CyouApplication.mAppContext;
		}
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_default)
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15))
				.resetViewBeforeLoading().build();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public void updateData(List<GameItem> data) {
		mData = data;
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private Map<Integer, View> viewCaches = new HashMap<Integer, View>();

	InstallGameViewCache viewCache = null;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = viewCaches.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.install_game_item, null);
			viewCache = new InstallGameViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (InstallGameViewCache) convertView.getTag();
		}
		viewCaches.put(position, convertView);
		final GameItem item = mData.get(position);
		if (null == item) {
			log.e("this game is null!");
			return convertView;
		}
		viewCache.getTxtGameName().setText(item.getName());
		viewCache.getTxtGamesize().setText(item.getFullsize() + "M");
		CYImageLoader.displayIconImage(item.getIcon(),viewCache.getImgGameIcon(), mOptions);
		viewCache.getBtnDownloadGame().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						installApp(item.getIdentifier());
					}
				});
		return convertView;
	}

	/**
	 * 卸载应用
	 */
	private void installApp(String packageName) {
		try {
			Uri packageURI = Uri.parse("package:" + packageName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,
					packageURI);
			mContext.startActivity(uninstallIntent);
		} catch (Exception e) {
			ViewToast.showToast(mContext, R.string.download_installapp_error, 0);
			log.e(e);
		}
	}
}
