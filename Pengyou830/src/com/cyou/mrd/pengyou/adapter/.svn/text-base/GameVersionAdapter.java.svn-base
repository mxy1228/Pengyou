package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.entity.GameVersion;
import com.cyou.mrd.pengyou.entity.GameVersionData;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GameVersionViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GameVersionAdapter extends BaseAdapter implements OnClickListener{

    private CYLog log = CYLog.getInstance();
    
    private Activity mContext;
    private List<GameVersion> mData;
    private DisplayImageOptions mCaptrueOption;
    private DisplayImageOptions mAvatarOption;
    private DisplayImageOptions mIconOption;
    private LayoutInflater mInflater;
    private Handler mHandler;
    private boolean mSupporting = false;
    
    public GameVersionAdapter(Activity context,List<GameVersion> data,Handler handler){
        this.mContext = context;
        this.mData = data;
        this.mCaptrueOption = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .showImageForEmptyUri(R.drawable.capture_default)
        .showImageOnFail(R.drawable.capture_default)
        .showStubImage(R.drawable.capture_default)
        .build();
        this.mAvatarOption = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .showImageForEmptyUri(R.drawable.avatar_defaul)
        .showImageOnFail(R.drawable.avatar_defaul)
        .showStubImage(R.drawable.avatar_defaul)
        .build();
        this.mIconOption = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .showImageForEmptyUri(R.drawable.icon_default)
        .showImageOnFail(R.drawable.icon_default)
        .showStubImage(R.drawable.icon_default)
        .displayer(new RoundedBitmapDisplayer(Config.ROUND))
        .build();
        this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mHandler = handler;
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
        GameVersionViewCache viewCache = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.gameversion_item, null);
            viewCache = new GameVersionViewCache(convertView);
            convertView.setTag(viewCache);
        }else{
            viewCache = (GameVersionViewCache)convertView.getTag();
        }
        GameVersion item = mData.get(position);
        viewCache.getVersionNameTV().setText(item.getVersionName());
        StringBuilder sb = new StringBuilder();
        for (GameVersionData i : item.getmVersions()) {
            sb.append(Util.getGameVersion(i.getDataFrom(), i.getDataSize(), i.getGameUrl()));
        }
        item.setGameVersionInfo(sb);
        viewCache.getGameVersionInfo().setText(
                Html.fromHtml(item.getGameVersionInfo().toString()));
        Log.d("ccx", "2222222222222222222+"+sb.toString());
        viewCache.getVersionNameTV().setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        
    }
    
}
