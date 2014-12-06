package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.GameCircleDetailActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.RecommendedGameViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 
 * @author tuozhonghua_zk
 *
 */
public class RecommendedGameAdapter extends BaseAdapter {

    private CYLog log = CYLog.getInstance();

    private List<GameItem> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private DisplayImageOptions mOptions;
    
    private int index1 = 0;
    private int index2 = 0;

    @SuppressWarnings("all")
    public RecommendedGameAdapter(Context context, List<GameItem> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOptions = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.recommended_game_default)
//                .showImageForEmptyUri(R.drawable.recommended_game_default)
//                .showImageOnFail(R.drawable.recommended_game_default)
                .cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new RoundedBitmapDisplayer(15))
                .resetViewBeforeLoading().build();
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mData == null && mData.size() > 0) {
            count = 0;
        }else {
            count = (int)(Math.ceil(mData.size()/2.0));
            log.i("count="+count);
        }
        return count;
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
        RecommendedGameViewCache viewCache = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.recommended_game_item, null);
            viewCache = new RecommendedGameViewCache(convertView);
            convertView.setTag(viewCache);
        } else {
            viewCache = (RecommendedGameViewCache) convertView.getTag();
        }
        index1 = 2*position;
        if (index1 < mData.size()) {
            log.i("index1 = " + index1 +"mData.size="+ mData.size());
            final GameItem item1 = mData.get(index1);
            if (null == item1) {
                return convertView;
            }
            viewCache.getTxtGameNameFirst().setText(item1.getName());
            String userplay1 = item1.getUsrplay();
            if (TextUtils.isEmpty(userplay1)) {
                userplay1 = "0";
            }
            viewCache.getRatingBarFirst().setRating(item1.getStar());
            viewCache.getTxtPlayerCountFirst().setText(item1.getRecdesc());
            CYImageLoader.displayGameImg(item1.getRecpic(),viewCache.getImgGamePictureFirst(), mOptions);
            
//            viewCache.getTxtPlayerCountFirst().setText(mContext.getString(R.string.rank_game_world,userplay1,
//                    Util.getGameSize(item1.getFullsize())));
//            viewCache.getImgGamePictureFirst().setImageResource(R.drawable.recommended_game_picture_xiaoguo);
            int height = (int)(Util.getScreenWidth((Activity)mContext)/720.0f*124 + 1);
            viewCache.getImgGamePictureFirst().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
            viewCache.getRecommenedGameLayoutFirst().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent();
                    mIntent.setClass(mContext, GameCircleDetailActivity.class);
                    mIntent.putExtra(Params.INTRO.GAME_CODE, item1.getGamecode());
                    mIntent.putExtra(Params.INTRO.GAME_NAME, item1.getName());
                    mContext.startActivity(mIntent);
                    //用户行为统计
                    BehaviorInfo behaviorInfo = new BehaviorInfo(
                            CYSystemLogUtil.GAMESTORE.BTN_NICERECOMMEND_MOREGOODGAME_GAMEDETAIL_ID,
                            CYSystemLogUtil.GAMESTORE.BTN_NICERECOMMEND_MOREGOODGAME_GAMEDETAIL_NAME);
                    CYSystemLogUtil.behaviorLog(behaviorInfo);
                }
            });
            index2 = 2*position+1;
            if (index2 < mData.size()) {
                log.i("index2 = " + index2 +"mData.size2="+ mData.size());
                final GameItem item2 = mData.get(index2);
                if (null == item2) {
                    return convertView;
                }
                viewCache.getRecommenedGameLayoutSecond().setVisibility(View.VISIBLE);
                viewCache.getTxtGameNameSecond().setText(item2.getName());
                String userplay2 = item2.getUsrplay();
                if (TextUtils.isEmpty(userplay2)) {
                    userplay2 = "0";
                }
                viewCache.getRatingBarSecond().setRating(item2.getStar());
                viewCache.getTxtPlayerCountSecond().setText(item2.getRecdesc());
                CYImageLoader.displayGameImg(item2.getRecpic(),viewCache.getImgGamePictureSecond(), mOptions);
                
//                viewCache.getTxtPlayerCountSecond().setText(mContext.getString(R.string.rank_game_world,userplay2,Util.getGameSize(item2.getFullsize())));
//                
//                viewCache.getImgGamePictureSecond().setImageResource(R.drawable.recommended_game_picture_xiaoguo);
                viewCache.getImgGamePictureSecond().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
                viewCache.getRecommenedGameLayoutSecond().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent();
                        mIntent.setClass(mContext, GameCircleDetailActivity.class);
                        mIntent.putExtra(Params.INTRO.GAME_CODE, item2.getGamecode());
                        mIntent.putExtra(Params.INTRO.GAME_NAME, item2.getName());
                        mContext.startActivity(mIntent);
                        
                      //用户行为统计
                        BehaviorInfo behaviorInfo = new BehaviorInfo(
                                CYSystemLogUtil.GAMESTORE.BTN_NICERECOMMEND_MOREGOODGAME_GAMEDETAIL_ID,
                                CYSystemLogUtil.GAMESTORE.BTN_NICERECOMMEND_MOREGOODGAME_GAMEDETAIL_NAME);
                        CYSystemLogUtil.behaviorLog(behaviorInfo);
                    }
                });
            }else {
                viewCache.getRecommenedGameLayoutSecond().setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
    }

}
