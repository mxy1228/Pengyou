package com.cyou.mrd.pengyou.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.DownloadFragment;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.DownloadItemViewCache;
import com.cyou.mrd.pengyou.widget.MessageBox;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class DownloadAdapter extends BaseAdapter {

    private CYLog log = CYLog.getInstance();
    private DownloadFragment downloadFragment;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<DownloadItem> mData;
    private ListView mListView;
    private DownloadDao mDao;
    private int mIngCount = 0;
    private DisplayImageOptions mOptions;
    private int mWaitingCount;
    private int mPauseCount;

    public DownloadAdapter(Context context, DownloadFragment mDownloadFrag,
            List<DownloadItem> data, int ingCount,
            int waitingCount, int pauseCount) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mDao = DownloadDao.getInstance(mContext);;
        this.mIngCount = ingCount;
        this.mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.icon_default)
                .showImageForEmptyUri(R.drawable.icon_default)
                .showImageOnFail(R.drawable.icon_default).cacheInMemory(true)
                .cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(15)).build();
        mWaitingCount = waitingCount;
        mPauseCount = pauseCount;
        downloadFragment = mDownloadFrag;
        // log.d("current downloading size is:" + ingCount);
        // log.d("current waitting size is:" + waitingCount);
    }

    public void updateCount(int ingCount, int waitingCount, int pauseCount) {
        mWaitingCount = waitingCount;
        mPauseCount = pauseCount;
        this.mIngCount = ingCount;
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

    private Map<Integer, View> viewCaches = new HashMap<Integer, View>();
    DownloadItemViewCache viewCache = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = viewCaches.get(position);
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.download_item, null);
            viewCache = new DownloadItemViewCache(convertView);
            convertView.setTag(viewCache);
        } else {
            viewCache = (DownloadItemViewCache) convertView.getTag();
        }
        viewCaches.put(position, convertView);
        final Button btnUpdateAll = viewCache.getBtnUpdateAll();
        // TextView mTopTV= viewCache.getmTopTV();;
        // TextView mNameTV= viewCache.getmNameTV();
        TextView mSizeTV = viewCache.getmSizeTV();
        TextView mPercentTV = viewCache.getmPercentTV();
        TextView mSpeedTV = viewCache.getmSpeedTV();
        TextView txtState = viewCache.getTxtState();
        DownloadItem item = mData.get(position);
        log.d("当前任务状态:" + item.getmState());
        // if (mIngCount != 0) {
        if (position == 0
                && item.getmState() == DownloadParam.C_STATE.DOWNLOADING) {// 正在下载顶部条
            viewCache.getmTopLL().setVisibility(View.VISIBLE);
            viewCache.getmTopTV().setText(mContext.getString(R.string.downloading, mIngCount));
            btnUpdateAll.setText(mContext.getString(R.string.allpause_game_txt));// 显示暂停
            btnUpdateAll.setBackgroundResource(R.drawable.all_pause_btn_xbg);
            btnUpdateAll.setOnClickListener(new OnClickListener() {// 全部暂停

                        @Override
                        public void onClick(View v) {
                            pauseAll();
                            // btnUpdateAll.setText(mContext
                            // .getString(R.string.allcontinue_game_txt));//
                            // 显示全部开始

                        }
                    });
        } else if (position == mIngCount
                && item.getmState() == DownloadParam.C_STATE.WAITING) {// 正在等待
            viewCache.getmTopLL().setVisibility(View.VISIBLE);
            viewCache.getmTopTV().setText(mContext.getString(R.string.waitting, mWaitingCount));
            // item.setmState(DownloadParam.C_STATE.DONE);
            mSpeedTV.setVisibility(View.VISIBLE);
            viewCache.getBtnUpdateAll().setVisibility(View.GONE);
            // viewCache.getmPB().setProgress(100);
        } else if (position == mWaitingCount + mIngCount
                && item.getmState() == DownloadParam.C_STATE.PAUSE) {// 正在暂停
            viewCache.getmTopLL().setVisibility(View.VISIBLE);
            viewCache.getmTopTV().setText(mContext.getString(R.string.download_pause_title,mPauseCount));
            // item.setmState(DownloadParam.C_STATE.DONE);
            mSpeedTV.setVisibility(View.VISIBLE);
            btnUpdateAll.setVisibility(View.VISIBLE);
            btnUpdateAll.setText(mContext.getString(R.string.allcontinue_game_txt));
            btnUpdateAll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    continueAll();
                }
            });
            btnUpdateAll.setBackgroundResource(R.drawable.all_start_btn_xbg);
        } else if (position == mWaitingCount + mIngCount + mPauseCount
                && (item.getmState() == DownloadParam.C_STATE.DONE || item
                        .getmState() == DownloadParam.C_STATE.INSTALLED)) {// 已下载
            log.d("done game top  state is:" + item.getmState());
            viewCache.getmTopLL().setVisibility(View.VISIBLE);
            viewCache.getmTopTV().setText(mContext.getString(R.string.download_done_title,
                        mData.size() - mPauseCount - mIngCount- mWaitingCount));
            btnUpdateAll.setVisibility(View.VISIBLE);
            btnUpdateAll.setText(mContext.getString(R.string.allclear_game_txt));
            btnUpdateAll.setBackgroundResource(R.drawable.all_clear_btn_xbg);
            btnUpdateAll.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                	AlertDialog dialog = new AlertDialog.Builder(mContext)
                	.setMessage(mContext.getResources().getString(R.string.download_clear_all))
                    .setPositiveButton(mContext.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        	clearAll();
                        }})
                    .setNegativeButton(mContext.getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            });
        } else {
            viewCache.getmTopLL().setVisibility(View.GONE);
        }
        if (item != null) {
            if (item.getmPackageName().equals(mContext.getPackageName())) {
                mSizeTV.setText("");
                viewCache.getmLogoIV().setImageResource(R.drawable.icon);
            }else{
                CYImageLoader.displayIconImage(item.getmLogoURL(),viewCache.getmLogoIV(), mOptions);
                mSizeTV.setText(mContext.getString(R.string.download_game_size,Util.getGameSize(item.getmSize())));
            }
            if (!TextUtils.isEmpty(item.getmName())) {
                viewCache.getmNameTV().setText(item.getmName());
            }
            viewCache.getmPB().setProgress(item.getmPercent());
            mPercentTV.setText(String.valueOf(item.getmPercent()) + "%");
            mSpeedTV.setTag(item.getmURL());
            // if (item.getmPercent() == 100) {
            // mSpeedTV.setVisibility(View.GONE);
            // if (Util.isInstallByread(item.getmPackageName())) {
            // item.setmState(DownloadParam.C_STATE.INSTALLED);
            // } else {
            // item.setmState(DownloadParam.C_STATE.DONE);
            // }
            //
            // }
            log.d("状态:" + item.getmState());
            switch (item.getmState()) {
            case DownloadParam.C_STATE.DOWNLOADING:
            	mPercentTV.setVisibility(View.VISIBLE);
                mSpeedTV.setVisibility(View.VISIBLE);
                txtState.setVisibility(View.GONE);
                viewCache.getmPB().setVisibility(View.VISIBLE);
                mSpeedTV.setText(mContext.getString(R.string.speed,item.getmSpeed()));
                break;
            case DownloadParam.C_STATE.PAUSE:// 暂停
            	mPercentTV.setVisibility(View.VISIBLE);
                mSpeedTV.setVisibility(View.VISIBLE);
                txtState.setVisibility(View.GONE);
                viewCache.getmPB().setVisibility(View.VISIBLE);
                mSpeedTV.setText(mContext.getString(R.string.download_state_pause));
                break;
            case DownloadParam.C_STATE.WAITING:
            	mPercentTV.setVisibility(View.VISIBLE);
                mSpeedTV.setVisibility(View.VISIBLE);
                txtState.setVisibility(View.GONE);
                viewCache.getmPB().setVisibility(View.VISIBLE);
                mSpeedTV.setText(mContext.getString(R.string.download_state_waiting));
                break;
            case DownloadParam.C_STATE.DONE:
                mSpeedTV.setVisibility(View.GONE);
                txtState.setText(mContext.getString(R.string.download_state_done));
                viewCache.getmPB().setVisibility(View.GONE);
                txtState.setVisibility(View.VISIBLE);
                mPercentTV.setVisibility(View.GONE);
                break;
            case DownloadParam.C_STATE.INSTALLED:
                mSpeedTV.setVisibility(View.GONE);
                mPercentTV.setVisibility(View.GONE);
                viewCache.getmPB().setVisibility(View.GONE);
                txtState.setVisibility(View.VISIBLE);
                txtState.setText(mContext.getString(R.string.download_state_installed));
                break;
            }
        }
        return convertView;
    }

    // 全部暂停
    private void pauseAll() {
        downloadFragment.pauseAll();
    }
    
    // 全部开始
    private void continueAll() {
        if (!NetUtil.isNetworkAvailable()) {
            Toast.makeText(mContext,mContext.getString(R.string.download_error_network_error),0).show();
            return;
        }
        downloadFragment.continueAll();
    }

    /**
     * 清空所有
     */
    private void clearAll() {
        downloadFragment.deleteAll();
    }

    public void notifyDataSetChanged(int ingCount) {
        this.mIngCount = ingCount;
        this.notifyDataSetChanged();
    }
}
