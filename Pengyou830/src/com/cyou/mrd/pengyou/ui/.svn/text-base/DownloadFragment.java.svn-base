package com.cyou.mrd.pengyou.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.DownloadAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DBHelper;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.CountService;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.DownloadItemViewCache;

public class DownloadFragment extends BaseFragment {

    private CYLog log = CYLog.getInstance();

    private ListView mListView;
    private AlertDialog mDownloadingDialog;// 下载中的游戏弹出框 显示 进入详细,暂停,删除,返回
    private AlertDialog mPauseDialog;// 暂停弹出框 显示 进入详细,开始,删除,返回
    private AlertDialog mWaitingDialog;// 等待中的游戏弹出框 显示 优先下载,暂停,删除,返回
    private AlertDialog mDoneDialog;// 下载完成未安装的游戏弹出框 显示 安装,清理完成,进入详细,返回
    private AlertDialog mInstallDailog;// 下载完成已安装 显示 启动 ,清理完成,进入详细,,删除,返回
    private LinearLayout mEmptyLL;

    private DownloadAdapter mAdapter;
    private ArrayList<DownloadItem> mData;
    private boolean mRegistedProgress = false;
    private ProgressReceiver mReceiver;
    private DownloadCountReceiver mDownloadCountReceiver;
    private DownloadItem mTempDonloadItem;
    private DownloadDao mDao;
    private int mIngCount = 0;// 还未下载完成的任务数量
    private int mWaitingCount = 0;// 正在等待
    private int mPauseCount = 0;// 正在暂停
    Activity mActivity;

    public DownloadFragment() {
        super();
    }

    private View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        log.d("DownloadFragment  onResume");
        registReceiver();
        // checkInstalled();
        try {
            DownloadActivity downloadActivity = (DownloadActivity) mActivity;
            if (null != downloadActivity) {
                downloadActivity.setDownloadCount();
            }
        } catch (Exception e) {
            log.e(e);
        }
        initData();
        super.onResume();
    }

    @Override
    public void onStart() {
//        initData();
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mActivity = getActivity();
        contentView = inflater.from(mActivity).inflate(R.layout.download, null);
        this.mDao = DownloadDao.getInstance(mContext);;
        initView();
        return contentView;
    }

    @Override
    public void onDestroyView() {
        if (mRegistedProgress && mReceiver != null && mDownloadCountReceiver != null) {
            mActivity.unregisterReceiver(mReceiver);
            mActivity.unregisterReceiver(mDownloadCountReceiver);
            mRegistedProgress = false;
        }
        super.onDestroyView();
    }

    private void dismissDialog () {
        if (mDownloadingDialog != null && mDownloadingDialog.isShowing()) {
            mDownloadingDialog.dismiss();
        }
        if (mPauseDialog != null && mPauseDialog.isShowing()) {
            mPauseDialog.dismiss();
        }
        if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
            mWaitingDialog.dismiss();
        }
        if (mDoneDialog != null && mDoneDialog.isShowing()) {
            mDoneDialog.dismiss();
        }
        if (mInstallDailog != null && mInstallDailog.isShowing()) {
            mInstallDailog.dismiss();
        }
    }
    public void initData() {
        
        if (this.mData == null) {
            this.mData = new ArrayList<DownloadItem>();
        }
        if (mDao == null) {
            mDao = DownloadDao.getInstance(mContext);;
        }
        if (mAdapter == null && mActivity != null) {
            mAdapter = new DownloadAdapter(mActivity,DownloadFragment.this, mData, mIngCount,mWaitingCount, mPauseCount);
        }
        if (mListView != null && mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }
        if (mAdapter != null) {
            initDownloadData();
        }
    }
    
    private void initDownloadData() {
        List<DownloadItem> list = mDao.getDownloadItems();
        List<DownloadItem> ingList = new ArrayList<DownloadItem>();// 上次正在下载中
        List<DownloadItem> pauseList = new ArrayList<DownloadItem>();// 暂停
        List<DownloadItem> waittingList = new ArrayList<DownloadItem>();// 等待队列的
        List<DownloadItem> doneList = new ArrayList<DownloadItem>();// 下载未安装的
        List<DownloadItem> installedList = new ArrayList<DownloadItem>();// 下载已安装的
        
        this.mData.clear();
        
        if (list != null) {
            if (list.size() == 0) {
                mEmptyLL.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.GONE);
            } else {
                for (DownloadItem item : list) {
                    
                    if (Util.isInstallByread(item.getmPackageName()) // 判断已安装并且版本名称相同
                            && item.getVersionName().equals(Util.getAppVersionName(item.getmPackageName()))) {// 如果已安装
                        item.setmState(DownloadParam.C_STATE.INSTALLED);
                        mDao.updateItem(item);
                        installedList.add(item);
                        continue;
                    }
                    // 其他类型
                    switch (item.getmState()) {
                    case DownloadParam.C_STATE.PAUSE:// 正在暂停状态
                        pauseList.add(item);
                        break;
                    case DownloadParam.C_STATE.WAITING:// 正在等待
                        waittingList.add(item);
                        break;
                    case DownloadParam.C_STATE.DOWNLOADING:// 正在下载
                        ingList.add(item);
                        break;
                    case DownloadParam.C_STATE.DONE:// 下载完成
                        if (Util.isInstallByread(item.getmPackageName())
                                && Util.getAppVersionName(item.getmPackageName()).equals(item.getVersionName())) {// 若同一版本的已经安装过
                            item.setmState(DownloadParam.C_STATE.INSTALLED);
                            mDao.updateItem(item);
                            installedList.add(item);
                            // 删除对应记录及文件
                            Intent intent = new Intent(mActivity,DownloadService.class);
                            intent.putExtra(DownloadParam.STATE,DownloadParam.TASK.DELETE);
                            intent.putExtra(DownloadParam.DOWNLOAD_ITEM, item);
                            mActivity.startService(intent);
                        } else {
                            File file = new File(item.getPath());
                            if (file != null) {
                                if (file.length() == item.getmTotalSize()) {
                                    doneList.add(item);
                                }
                            }
                        }
                        break;
                    case DownloadParam.C_STATE.INSTALLED:// 已经下载安装
                        if (Util.isInstallByread(item.getmPackageName()) // 判断已安装并且版本名称相同
                                && item.getVersionName().equals(Util.getAppVersionName(item.getmPackageName()))) {// 如果已安装
                            installedList.add(item);
                        } else {
                            // 删除对应记录及文件
                            Intent intent = new Intent(mActivity,DownloadService.class);
                            intent.putExtra(DownloadParam.STATE,DownloadParam.TASK.DELETE);
                            intent.putExtra(DownloadParam.DOWNLOAD_ITEM, item);
                            mActivity.startService(intent);
                        }
                        break;
                    }
                    
                }
                mIngCount = ingList.size();
                mWaitingCount = waittingList.size();
                mPauseCount = pauseList.size();
                mData.addAll(ingList);
                mData.addAll(waittingList);
                mData.addAll(pauseList);
                mData.addAll(doneList);
                log.d("下载完成：" + doneList.size());
                mData.addAll(installedList);
                log.d("安装完成：" + installedList.size());
                mEmptyLL.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                
                mAdapter.updateCount(mIngCount, mWaitingCount, mPauseCount);
                mAdapter.notifyDataSetChanged();
                log.d("下载总数:" + mData.size());
                if (mData.isEmpty()) {
                    mListView.setVisibility(View.GONE);
                    mEmptyLL.setVisibility(View.VISIBLE);
                }
            }
        }
        
    }

    private void initView() {
        this.mListView = (ListView) contentView.findViewById(R.id.download_lv);
        this.mEmptyLL = (LinearLayout) contentView.findViewById(R.id.download_empty_ll);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                DownloadItem item = mData.get(position);
                mTempDonloadItem = item;
                // 其他类型
                switch (item.getmState()) {
                case DownloadParam.C_STATE.PAUSE:// 正在下载暂停状态
                    initDownloadingPauseDialog(mTempDonloadItem);
                    mPauseDialog.show();
                    break;
                case DownloadParam.C_STATE.WAITING:// 正在等待
                    initWaitingDialog(mTempDonloadItem);
                    mWaitingDialog.show();
                    break;
                case DownloadParam.C_STATE.DOWNLOADING:// 正在下载
                    initDownloadingDialog(mTempDonloadItem);
                    mDownloadingDialog.show();
                    break;
                case DownloadParam.C_STATE.DONE:// 下载完成
                    if (Util.isInstallByread(item.getmPackageName()) // 判断已安装并且版本名称相同
                            && item.getVersionName().equals(Util.getAppVersionName(item.getmPackageName()))) {
                        initInstalledDialog(mTempDonloadItem);
                        mInstallDailog.show();
                    } else {
                        initDoneDialog(mTempDonloadItem);
                        mDoneDialog.show();
                    }
                    break;

                case DownloadParam.C_STATE.INSTALLED:// 已经下载安装
                    initInstalledDialog(mTempDonloadItem);
                    mInstallDailog.show();
                    break;
                }
            }
        });
    }

    public void registReceiver() {
        // 注册下载进度监听器
        if (mReceiver == null) {
            mReceiver = new ProgressReceiver();
        }
        if (mDownloadCountReceiver == null){
        	mDownloadCountReceiver = new DownloadCountReceiver();
        }
        if (!mRegistedProgress && mActivity != null) {
            mActivity.registerReceiver(mReceiver, new IntentFilter(DownloadParam.UPDATE_PROGRESS_ACTION));
            mActivity.registerReceiver(mDownloadCountReceiver, new IntentFilter(Contants.ACTION.DOWNLOADING_COUNT));
            mRegistedProgress = true;
        }
    }



    private void initDownloadingDialog(DownloadItem item) {
        if (item != null && item.getmPackageName() != null && item.getmPackageName().equals(mActivity.getPackageName())) {
            mDownloadingDialog = new AlertDialog.Builder(mActivity).setItems(
                    new CharSequence[] { 
                            getString(R.string.download_btn_pause),
                            getString(R.string.download_btn_delete),
                            getString(R.string.download_btn_back) },
                            new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                                pause();
                                break;
                            case 1:
                                deleteGame();
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                            }
                            dialog.dismiss();
                        }
                    }).create();
        }else {
            mDownloadingDialog = new AlertDialog.Builder(mActivity).setItems(
                    new CharSequence[] { getString(R.string.download_btn_detail),
                            getString(R.string.download_btn_pause),
                            getString(R.string.download_btn_delete),
                            getString(R.string.download_btn_back) },
                            new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                                insertIntro();
                                break;
                            case 1:
                                pause();
                                break;
                            case 2:
                                deleteGame();
                                break;
                            case 3:
                                dialog.dismiss();
                                break;
                            }
                            dialog.dismiss();
                        }
                    }).create();
        }
        mDownloadingDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 暂停正在下载
     */
    private void initDownloadingPauseDialog(DownloadItem item) {
       if (item != null && item.getmPackageName() != null && item.getmPackageName().equals(mActivity.getPackageName())) {
          mPauseDialog = new AlertDialog.Builder(mActivity).setItems(
                new CharSequence[] {
                      getString(R.string.download_btn_continue),
                      getString(R.string.download_btn_delete),
                      getString(R.string.download_btn_back) },
                      new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                      switch (which) {
                      case 0:
                         continueGame();
                         break;
                      case 1:
                         deleteGame();
                         break;
                      case 2:
                         dialog.dismiss();
                         break;
                      }
                      dialog.dismiss();
                   }
                }).create();
       }else {
          mPauseDialog = new AlertDialog.Builder(mActivity).setItems(
                new CharSequence[] { getString(R.string.download_btn_detail),
                      getString(R.string.download_btn_continue),
                      getString(R.string.download_btn_delete),
                      getString(R.string.download_btn_back) },
                      new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                      switch (which) {
                      case 0:
                         insertIntro();
                         break;
                      case 1:
                         continueGame();
                         break;
                      case 2:
                         deleteGame();
                         break;
                      case 3:
                         dialog.dismiss();
                         break;
                      }
                      dialog.dismiss();
                   }
                }).create();
       }
        mPauseDialog.setCanceledOnTouchOutside(true);
    }

    private void initWaitingDialog(DownloadItem item) {
        if (item != null && item.getmPackageName() != null && item.getmPackageName().equals(mActivity.getPackageName())) {
            mWaitingDialog = new AlertDialog.Builder(mActivity).setItems(
                    new CharSequence[] {
                            getString(R.string.download_btn_down_first),
                            getString(R.string.download_btn_pause),
                            getString(R.string.download_btn_delete),
                            getString(R.string.download_btn_back) },
                            new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                                downloadFirst();
                                break;
                            case 1:
                                pause();
                                break;
                            case 2:
                                deleteGame();
                                break;
                            case 3:
                                dialog.dismiss();
                                break;
                            }
                            dialog.dismiss();
                        }
                    }).create();
        }else {
            mWaitingDialog = new AlertDialog.Builder(mActivity).setItems(
                    new CharSequence[] {
                            getString(R.string.download_btn_down_first),
                            getString(R.string.download_btn_detail),
                            getString(R.string.download_btn_pause),
                            getString(R.string.download_btn_delete),
                            getString(R.string.download_btn_back) },
                            new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                                downloadFirst();
                                break;
                            case 1:
                                insertIntro();
                                break;
                            case 2:
                                pause();
                                break;
                            case 3:
                                deleteGame();
                                break;
                            case 4:
                                dialog.dismiss();
                                break;
                            }
                            dialog.dismiss();
                        }
                    }).create();
            
        }
        mWaitingDialog.setCanceledOnTouchOutside(true);
    }

    private void initDoneDialog(final DownloadItem item) {
        if (item != null && item.getmPackageName() != null && item.getmPackageName().equals(mActivity.getPackageName())) {
            mDoneDialog = new AlertDialog.Builder(mActivity).setItems(
                    new CharSequence[] { getString(R.string.download_btn_install),
                            getString(R.string.download_btn_clear),
                            getString(R.string.download_btn_back) },
                            new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                            	Util.install(item, mContext);
//                                install(item.getmPackageName());
                                break;
                            case 1:
                                deleteGame();
                                break;
                            case 2:
                                dialog.dismiss();
                                break;
                            }
                            dialog.dismiss();
                        }
                    }).create();

        }else {
            mDoneDialog = new AlertDialog.Builder(mActivity).setItems(
                    new CharSequence[] { getString(R.string.download_btn_install),
                            getString(R.string.download_btn_clear),
                            getString(R.string.download_btn_detail),
                            getString(R.string.download_btn_back) },
                            new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                            	Util.install(item, mContext);
//                                install(item.getmPackageName());
                                break;
                            case 1:
                                deleteGame();
                                break;
                            case 2:
                                insertIntro();
                                break;
                            case 3:
                                dialog.dismiss();
                                break;
                            }
                            dialog.dismiss();
                        }
                    }).create();
            
        }
        mDoneDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 已经安装
     */
    private void initInstalledDialog(DownloadItem item) {
        if (item != null && item.getmPackageName() != null && item.getmPackageName().equals(mActivity.getPackageName())) {
            mInstallDailog = new AlertDialog.Builder(mActivity).setItems(
                    new CharSequence[] {
                            getString(R.string.download_btn_clear),
                            getString(R.string.download_btn_back) },
                            new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                                deleteGame();
                                break;
                            case 1:
                                dialog.dismiss();
                                break;
                            }
                            dialog.dismiss();
                        }
                    }).create();
        }else {
            mInstallDailog = new AlertDialog.Builder(mActivity).setItems(
                    new CharSequence[] { getString(R.string.download_btn_start),
                            getString(R.string.download_btn_clear),
                            getString(R.string.download_btn_detail),
                            getString(R.string.download_btn_back) },
                            new DialogInterface.OnClickListener() {
                        
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                            case 0:
                                play();
                                break;
                            case 1:
                                deleteGame();
                                break;
                            case 2:
                                insertIntro();
                                break;
                            case 3:
                                dialog.dismiss();
                                break;
                            }
                            dialog.dismiss();
                        }
                    }).create();
            
        }
        mInstallDailog.setCanceledOnTouchOutside(true);
    }

    private void pause() {
        Intent intent = new Intent(mContext, DownloadService.class);
        mTempDonloadItem.setmState(DownloadParam.C_STATE.PAUSE);
        intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.PAUSE);
        intent.putExtra(DownloadParam.DOWNLOAD_ITEM, mTempDonloadItem);
        mContext.startService(intent);
        try {
            DownloadActivity downloadActivity = (DownloadActivity) mActivity;
            if (null != downloadActivity) {
                downloadActivity.setDownloadCount();
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    public void pauseAll() {
        Intent intent = new Intent(mContext, DownloadService.class);
        intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.PAUSE_ALL);
        mContext.startService(intent);
        try {
            DownloadActivity downloadActivity = (DownloadActivity) mActivity;
            if (null != downloadActivity) {
                downloadActivity.setDownloadCount();
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    /**
     * 优先下载
     */
    private void downloadFirst() {
        if (Util.isNetWorkAvailable()) {
            Intent intent = new Intent(mContext, DownloadService.class);
            intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.DOWNLOAD_FIRST);
            intent.putExtra(DownloadParam.DOWNLOAD_ITEM, mTempDonloadItem);
            mContext.startService(intent);
        }else{
            Util.showToast(mContext, getResources().getString(R.string.download_error_network_error), Toast.LENGTH_SHORT);
        }
//        initData();
    }

    private void install(final String pkg) {
        File file = new File(mTempDonloadItem.getPath());
        if (file.exists()) {
            if (pkg != null && !"".equals(pkg)) {
            PackageInfo packageInfo = Util.getAppPackageInfo(pkg);
                if (packageInfo != null) {
                    Signature sig1[] = packageInfo.signatures;
                    Signature sig2[] = Util.getSignature(mTempDonloadItem.getPath());
                    Boolean isSameSignature = Util.IsSignaturesSame(sig1, sig2);
                    log.i("isSameSignature=" + isSameSignature);
                    if (isSameSignature) {
                        this.installApk(file);
                    }else {
                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage(getResources().getString(R.string.update_prompt_content, mTempDonloadItem.getmName()))
                        .setTitle(getResources().getString(R.string.update_prompt))
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Util.unInstallApp(mContext, pkg);
                            }})
                            .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                }else {
                    this.installApk(file);
                }
            }
        } else {
            showToastMessage(getResources().getString(R.string.download_install_file_noexit), 1);
            deleteGame();
        }
        
    }
    
    private void installApk (File file) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    private void continueGame() {
        if (Util.isNetWorkAvailable()) {
            Intent intent = new Intent(mContext, DownloadService.class);
            mTempDonloadItem.setmState(DownloadParam.C_STATE.DOWNLOADING);
            intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.CONTINUE);
            intent.putExtra(DownloadParam.DOWNLOAD_ITEM, mTempDonloadItem);
            mContext.startService(intent);
//            try {
//                DownloadActivity downloadActivity = (DownloadActivity) mActivity;
//                if (null != downloadActivity) {
//                    downloadActivity.setDownloadCount();
//                }
//            } catch (Exception e) {
//                log.e(e);
//            }
        }else{
            Util.showToast(mContext, getResources().getString(R.string.download_error_network_error), Toast.LENGTH_SHORT);
        }
        // initData();
    }

    
    public void continueAll() {
        if (Util.isNetWorkAvailable()) {
            Intent intent = new Intent(mContext, DownloadService.class);
            intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.CONTINUE_ALL);
            mContext.startService(intent);
//            try {
//                DownloadActivity downloadActivity = (DownloadActivity) mActivity;
//                if (null != downloadActivity) {
//                    downloadActivity.setDownloadCount();
//                }
//            } catch (Exception e) {
//                log.e(e);
//            }
        }else{
            Util.showToast(mContext, getResources().getString(R.string.download_error_network_error), Toast.LENGTH_SHORT);
        }
    }

    private void play() {
        if (Util.isInstallByread(mTempDonloadItem.getmPackageName())) {// 若已安装
            // 直接运行
            PackageManager manager = mContext.getPackageManager();
            Intent iStart = manager.getLaunchIntentForPackage(mTempDonloadItem.getmPackageName());
            mContext.startActivity(iStart);
            ContentValues values = new ContentValues();
            values.put(DBHelper.MYGAME.PACKAGE_NAME, mTempDonloadItem.getmPackageName());
            mContext.getContentResolver().update(Uri.parse(MyGameProvider.URI), values, null, null);
			//启动统计Service
			Intent jifen_intent = new Intent(mContext, CountService.class);
			jifen_intent.putExtra("identifier",
					mTempDonloadItem.getmPackageName());
			mContext.startService(jifen_intent);
        } else {
            showToastMessage(getResources().getString(R.string.download_installapp_error), 1);

        }
    }

    /**
     * 删除游戏
     */
    private void deleteGame() {
        try {
            Intent intent = new Intent(mActivity, DownloadService.class);
            intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.DELETE);
            intent.putExtra(DownloadParam.DOWNLOAD_ITEM, mTempDonloadItem);
            mActivity.startService(intent);
//            int index = mData.indexOf(mTempDonloadItem);
//            if (index > -1) {
//                mData.remove(index);
//                mAdapter.notifyDataSetChanged();
//            }
        } catch (Exception e) {
            e.fillInStackTrace();
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除游戏
     */
    public void deleteAll() {
        try {
            Intent intent = new Intent(mContext, DownloadService.class);
            intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.DELETE_ALL);
            mContext.startService(intent);
            if (null != mData) {
                Iterator ingIt = mData.iterator();
                while (ingIt.hasNext()) {
                    DownloadItem item = (DownloadItem) ingIt.next();
                    if (item.getmState() == DownloadParam.C_STATE.DONE
                            || item.getmState() == DownloadParam.C_STATE.INSTALLED) {
                        ingIt.remove();
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            mAdapter.notifyDataSetChanged();
        }

    }

    private void insertIntro() {
        Intent mIntent = new Intent();
        mIntent.setClass(mActivity, GameCircleDetailActivity.class);
        log.d("gamecode is :" + mTempDonloadItem.getGameCode());
        mIntent.putExtra(Params.INTRO.GAME_CODE, mTempDonloadItem.getGameCode());
        mIntent.putExtra(Params.INTRO.GAME_NAME, mTempDonloadItem.getmName());
        mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
      //  mIntent.putExtra(Params.INTRO.GAME_DISPFLAG, true);
        startActivity(mIntent);
    }

    /**
     * 下载进度接收器
     * 
     * @author xumengyang
     * 
     */
    private class ProgressReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int state = intent.getIntExtra(DownloadParam.STATE, 0);
                String gameString = intent.getStringExtra(DownloadParam.GAMESTRING);
                if (state >= 0) {// 进度条广播
                    log.d("有新的操作:" + state + "__" + gameString);
                    dismissDialog();
                    initData();
                } else {
                    int percent = intent.getIntExtra(DownloadParam.PERCENT, 0);
                    int speed = intent.getIntExtra(DownloadParam.SPEED, 0);
                    if (percent < 0) {
                    	percent = 0;
                    }else if (percent > 100) {
                    	percent = 100;
                    }
                    if (mData != null) {
                        for (DownloadItem item : mData) {
                            if (item.getKey().equals(gameString)) {
                                item.setmPercent(percent);
                                item.setmSpeed(speed);
                                updateListViewItem(item);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
    private class DownloadCountReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        	try {
                DownloadActivity downloadActivity = (DownloadActivity) mActivity;
                if (null != downloadActivity) {
                    downloadActivity.setDownloadCount();
                }
            } catch (Exception e) {
                log.e(e);
            }
        }
    }

    /**
     * 
     * tuozhonghua_zk
     * 2013-9-25上午11:11:03
     *
     * @param item
     * TODO
     * @return
     */
    private void updateListViewItem(DownloadItem item) {
        if (mData != null && !mData.isEmpty()) {
            int firstVisiblePosition = mListView.getFirstVisiblePosition();
            int index = mData.indexOf(item);
            View view = mListView.getChildAt(index - firstVisiblePosition);
            if (view != null) {
                if (item.getmPercent() != 100) {
                    DownloadItemViewCache viewCache = (DownloadItemViewCache) view.getTag();
                    switch (item.getmState()) {
                    case DownloadParam.C_STATE.DOWNLOADING:
                        viewCache.getmSpeedTV().setText(mContext.getString(R.string.speed,item.getmSpeed()));
                        if (item.getmPackageName().equals(mContext.getPackageName())) {
                            viewCache.getmSizeTV().setText("");
                        }else{
                            viewCache.getmSizeTV().setText(mContext.getString(R.string.download_game_size,Util.getGameSize(item.getmSize())));
                        }
                        viewCache.getmPB().setProgress(item.getmPercent());
                        viewCache.getmPercentTV().setText(String.valueOf(item.getmPercent()) + "%");
                        break;
                    case DownloadParam.C_STATE.PAUSE:// 暂停
                        viewCache.getmSpeedTV().setText(mContext.getString(R.string.download_state_pause));
                        break;
                    case DownloadParam.C_STATE.WAITING:
                        viewCache.getmSpeedTV().setText(mContext.getString(R.string.download_state_waiting));
                        break;
                    // case DownloadParam.C_STATE.WAITINGPAUSE:
                    // viewCache
                    // .getmSpeedTV()
                    // .setText(
                    // mContext.getString(R.string.download_state_waitingpause));
                    // break;
                    case DownloadParam.C_STATE.DONE:
                        viewCache.getmSpeedTV().setVisibility(View.GONE);
                        viewCache.getTxtState().setText(mContext.getString(R.string.download_state_done));
                        viewCache.getmPB().setVisibility(View.GONE);
                        viewCache.getTxtState().setVisibility(View.VISIBLE);
                        viewCache.getmPercentTV().setVisibility(View.GONE);
                        break;
                    case DownloadParam.C_STATE.INSTALLED:
                        viewCache.getmSpeedTV().setVisibility(View.GONE);
                        viewCache.getmPercentTV().setVisibility(View.GONE);
                        viewCache.getmPB().setVisibility(View.GONE);
                        viewCache.getTxtState().setVisibility(View.VISIBLE);
                        viewCache.getTxtState().setText(mContext.getString(R.string.download_state_installed));
                        break;
                    }
                } else {
                    mListView.getAdapter().getView(index, view, mListView);
                }
            }
        }
    }
}
