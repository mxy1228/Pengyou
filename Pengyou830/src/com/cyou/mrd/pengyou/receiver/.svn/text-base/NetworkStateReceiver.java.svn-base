package com.cyou.mrd.pengyou.receiver;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadManager;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.CoreService;
import com.cyou.mrd.pengyou.utils.Util;

public class NetworkStateReceiver extends BroadcastReceiver {

    private CYLog log = CYLog.getInstance();
    private static int lastType = -1;
    
    @Override
    public void onReceive(Context arg0, Intent arg1) {
        log.i("NetworkStateReceiver:onReceive");
        ConnectivityManager manager = (ConnectivityManager)arg0.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infos = manager.getAllNetworkInfo();
        if(infos != null){
            for(int i=0;i<infos.length;i++){
                if(infos[i].getState() == NetworkInfo.State.CONNECTED){
                    Intent intent = new Intent(arg0, CoreService.class);
                    intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.JOIN);
                    arg0.startService(intent);
                }
            }
        }
        
        State state = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        State mobileState = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info == null || !manager.getBackgroundDataSetting()) {
            log.i("Network is disconnect");
        }else {
            int netType = info.getType();
            if (netType != lastType) {
            	// 判断是否正在使用3G网络 
                if (State.CONNECTED != state && State.CONNECTED ==mobileState) {
                    DownloadDao downloadDao = DownloadDao.getInstance(arg0);
                    List<DownloadItem> downloadingList = new ArrayList<DownloadItem>();
                    downloadingList.addAll(downloadDao.getDownloadItemsByState(DownloadParam.C_STATE.DOWNLOADING));
//                    downloadingList.addAll(downloadDao.getDownloadItemsByState(DownloadParam.C_STATE.WAITING));
//                    downloadingList.addAll(downloadDao.getDownloadItemsByState(DownloadParam.C_STATE.PAUSE));
                    if (downloadingList.size() > 0) {
                        DownloadManager mManager = DownloadManager.getInstance(arg0);
                        mManager.startManage();
                        Config.isAskConfirmNetChange = true;
                        mManager.pauseAll();
                        Util.showNetChangeDialog(arg0,mManager);
                    }
                }
                lastType = netType;
            }
        }
    }

}
