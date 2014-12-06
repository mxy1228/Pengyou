package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.SeminarBean;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.GameSpecialDetailActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GameSpecialViewCache;
import com.cyou.mrd.pengyou.widget.GameGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 
 * @author tuozhonghua_zk
 *
 */
public class GameSpecialTopicAdapter extends BaseAdapter {

    private CYLog log = CYLog.getInstance();

    private List<SeminarBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private DisplayImageOptions mOptions;
    public static final int SHOW_NUM = 2;
    private float density = 1.5f;

    @SuppressWarnings("all")
    public GameSpecialTopicAdapter(Context context, List<SeminarBean> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.game_special_defaul_bg)
                .showImageForEmptyUri(R.drawable.game_special_defaul_bg)
                .showImageOnFail(R.drawable.game_special_defaul_bg)
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new RoundedBitmapDisplayer(15))
                .resetViewBeforeLoading().build();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        GameSpecialViewCache viewCache = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.game_special_item, null);
            viewCache = new GameSpecialViewCache(convertView);
            convertView.setTag(viewCache);
        } else {
            viewCache = (GameSpecialViewCache) convertView.getTag();
        }
        final SeminarBean item = mData.get(position);
        if (null == item) {
            return convertView;
        }
        viewCache.getTxtGameSpecialName().setText(item.getName());
        viewCache.getTxtGameSpecialTime().setText(Util.getDateForH(item.getTopicdate()));
//        viewCache.getImgGameSpecialPicture().setImageDrawable(mContext.getResources().getDrawable(R.drawable.game_specail_xioguo));
        log.i("item.getPicture()=" + item.getPicture());
        CYImageLoader.displayGameImg(item.getPicture(),viewCache.getImgGameSpecialPicture(), mOptions);
        int height = (int)(Util.getScreenWidth((Activity)mContext)/700.0f*204);
        viewCache.getImgGameSpecialPicture().setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));

        GameSpecialGridAdapter adapter = new GameSpecialGridAdapter(mContext, item.getTopicgms());
        int allWidth = (int) ((mContext.getResources().getDimensionPixelSize(R.dimen.game_specail_icon_width) + 15) * item.getTopicgms().size());
        int itemWidth = (int) (mContext.getResources().getDimensionPixelSize(R.dimen.game_specail_icon_width));
        GameGridView gridView = viewCache.getGameSpecialIconView();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(allWidth, LinearLayout.LayoutParams.WRAP_CONTENT);  
        gridView.setLayoutParams(params);
        gridView.setHorizontalSpacing(15);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setColumnWidth(itemWidth);
        gridView.setNumColumns(item.getTopicgms().size());
        
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                Intent mIntent = new Intent();
                mIntent.setClass(mContext, GameSpecialDetailActivity.class);
                mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_ID, item.getId());
                mContext.startActivity(mIntent);
            }
        });
        
        return convertView;
    }

}
