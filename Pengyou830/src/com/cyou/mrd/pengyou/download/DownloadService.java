package com.cyou.mrd.pengyou.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.Util;

public class DownloadService extends Service {

    private CYLog log = CYLog.getInstance();
    private DownloadManager mManager;

    public DownloadService() {
        this.mManager = DownloadManager.getInstance(this);
        this.mManager.startManage();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        log.d("onBind");
        return null;
    }
    

    @Override
    public void onStart(Intent intent, int startId) {
        if (intent != null) {
            int state = intent.getIntExtra(DownloadParam.STATE, -1);
            DownloadItem item = intent.getParcelableExtra(DownloadParam.DOWNLOAD_ITEM);
            if (item != null && !TextUtils.isEmpty(item.getmURL())) {
                switch (state) {
                case DownloadParam.TASK.START_SERVICE:
                    if (!mManager.isRunning()) {
                        mManager.startManage();
                    }
                    mManager.reStartDownloading();
                    break;
                case DownloadParam.TASK.ADD:
                    if (item.getmPackageName() != null && item.getmPackageName().equals(this.getPackageName())) {
                        mManager.add(item);
                    }else {
                        if (Util.is3GNetWork()) {
                            if (Config.isAskConfirmNetChange) {
                                Util.show3GNetDialog(getApplicationContext(),mManager,item);
                            }else {
                                mManager.add(item);
                            }
                        }else {
                            mManager.add(item);
                        }
                        
                    }
                    break;
                case DownloadParam.TASK.CONTINUE:
                    mManager.cont(item);
                    break;
                case DownloadParam.TASK.DELETE:
                    mManager.delete(item);
                    break;
                case DownloadParam.TASK.PAUSE:
                    mManager.pause(item);
                    break;
                case DownloadParam.TASK.PAUSE_ALL:
                    mManager.pauseAll();
                    break;
                case DownloadParam.TASK.CONTINUE_ALL:// 继续所有
                    mManager.contAll();
                    break;
                case DownloadParam.TASK.DELETE_ALL:// 清空所有--仅清空下载完成和安装完成的
                    mManager.deleteAllDone();
                    break;
                case DownloadParam.TASK.DOWNLOAD_FIRST:// 优先下载
                    mManager.downloadFirst(item);
                    break;
                default:
                    break;
                }
            } else {
                log.e("the url is empty");
                if (state == DownloadParam.TASK.PAUSE_ALL) {
                    mManager.pauseAll();
                }
                if (state == DownloadParam.TASK.CONTINUE_ALL) {
                    mManager.contAll();
                }
                if (state == DownloadParam.TASK.DELETE_ALL) {
                    mManager.deleteAllDone();
                }
                if (state == DownloadParam.TASK.START_SERVICE) {
                    if (!mManager.isRunning()) {
                        mManager.startManage();
                    }
                    mManager.reStartDownloading();
                }
            }
        }
        
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        log.d("DownloadService:onDestroy");
        super.onDestroy();
    }

}
