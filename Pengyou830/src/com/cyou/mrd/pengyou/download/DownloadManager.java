package com.cyou.mrd.pengyou.download;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.DownloadActivity;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.RequestParams;

/**
 * 下载管理- 根据packageName_versionName 进行区分唯一下载
 * 
 * @author wangkang
 * 
 */
public class DownloadManager {

    private CYLog log = CYLog.getInstance();
    public static final int TASK_MAX_COUNT = 3;// 最大任务并发量

    // 等待下载任务队列
    private Queue<DownloadTask> mPendingQueue;
    // 正在下载任务队列
    private List<DownloadTask> mDownloadingList;
    // 暂停队列
    private Queue<DownloadTask> mPauseQueue;
    private NotificationManager mNotificationManager;
    private Context mContext;
    private DownloadDao mDao;
    private Timer mTimer;

    private boolean isRunning = false;
    private boolean hasTask = false;
    private boolean isPause = false;// 是否处于暂停状态 若为true 则Timer不执行
    
    private final int  DOWNLOADING_STATUS = 1;
    private final int  ALL_PAUSE_STATUS = 1;
    private final int  UPDATE_STATUS = 1;
    
    public static final int DOWNLOAD_NOTIFICATION = 555;
    
    private static DownloadManager mManager;
    private DownloadManager(Context context) {
        this.mPendingQueue = new LinkedList<DownloadTask>();
        this.mDownloadingList = new ArrayList<DownloadTask>();
        this.mPauseQueue = new LinkedList<DownloadTask>();
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) CyouApplication.mAppContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        this.mDao = DownloadDao.getInstance(mContext);
        updateAllState();
    }
    
    public static DownloadManager getInstance (Context context) {
        if (mManager == null) {
            mManager = new DownloadManager(context);
        }
        return mManager;
    }

    /**
     * 初始化各队列的数据
     */
    public void updateAllState() {
        this.mPendingQueue = new LinkedList<DownloadTask>();
        this.mDownloadingList = new ArrayList<DownloadTask>();
        this.mPauseQueue = new LinkedList<DownloadTask>();
        List<DownloadItem> list = mDao.getDownloadItems();
        if (list != null && list.size() > 0) {
            for (DownloadItem item : list) {
                log.d("名称:" + item.getmName() + "__下载地址:" + item.getmURL());
                switch (item.getmState()) {
                case DownloadParam.C_STATE.DOWNLOADING:// 下载中
                    try {
                        mDownloadingList.add(item2task(item));
                    } catch (Exception e) {
                        log.e(e);
                        continue;
                    }
                    break;
                case DownloadParam.C_STATE.PAUSE:// 下载暂停
                    try {
                        log.d("添加到暂停队列中:" + item.getmName());
                        mPauseQueue.add(item2task(item));
                    } catch (Exception e) {
                        log.e(e);
                        continue;
                    }
                    break;
                case DownloadParam.C_STATE.WAITING:// 等待中
                    try {
//                        sendTaskStatusBroadcast(item,DownloadParam.TASK.WAITING_PAUSE);
                        mPendingQueue.add(item2task(item));
                    } catch (Exception e) {
                        log.e(e);
                        continue;
                    }
                    break;

                }
            }
        }
    }

    public void sendTaskStatusBroadcast (DownloadItem item,int status) {
        Intent progressIntent = new Intent(DownloadParam.UPDATE_PROGRESS_ACTION);
        switch (status) {
        case DownloadParam.TASK.ADD:
            sendDownloadTaskCountBroadcast(getTaskList().size(),DownloadParam.TASK.ADD,item);
//            Toast.makeText(mContext,mContext.getString(R.string.begin_download_gamename),Toast.LENGTH_SHORT).show();
//            Util.showToast(mContext, mContext.getString(R.string.begin_download_gamename), Toast.LENGTH_SHORT);
            break;
        case DownloadParam.TASK.HAD_CONTAIN_TASK:
            sendDownloadTaskCountBroadcast(getTaskList().size(),DownloadParam.TASK.HAD_CONTAIN_TASK,null);
            Toast.makeText(mContext,mContext.getString(R.string.had_contain_download_task),Toast.LENGTH_SHORT).show();
        case DownloadParam.TASK.DELETE:
            sendDownloadTaskCountBroadcast(getTaskList().size(),DownloadParam.TASK.DELETE,null);
            if (item != null) {//
                progressIntent.putExtra(DownloadParam.GAMESTRING,item.getKey());
                progressIntent.putExtra(DownloadParam.GAMECODE, item.getGameCode());
                progressIntent.putExtra(DownloadParam.PACKAGE_NAME, item.getmPackageName());
                progressIntent.putExtra(DownloadParam.STATE,DownloadParam.TASK.DELETE);
                mContext.sendBroadcast(progressIntent);
            } else {// 删除所有
                progressIntent.putExtra(DownloadParam.STATE,
                        DownloadParam.TASK.DELETE_ALL);
                mContext.sendBroadcast(progressIntent);
            }
            break;
        case DownloadParam.TASK.DONE:
            sendDownloadTaskCountBroadcast(getTaskList().size(),DownloadParam.TASK.DONE,null);
            progressIntent.putExtra(DownloadParam.STATE, DownloadParam.TASK.DONE);
            progressIntent.putExtra(DownloadParam.PACKAGE_NAME, item.getmPackageName());
            mContext.sendBroadcast(progressIntent);
            break;
        case DownloadParam.TASK.PAUSE:// 暂停
            if (item != null) {//
                log.d("暂停" + item.getmName());
                progressIntent.putExtra(DownloadParam.GAMESTRING,
                        item.getKey());
                progressIntent.putExtra(DownloadParam.GAMECODE, item.getGameCode());
                progressIntent.putExtra(DownloadParam.STATE,
                        DownloadParam.TASK.PAUSE);
                progressIntent.putExtra(DownloadParam.PERCENT,
                        item.getmPercent());
                mContext.sendBroadcast(progressIntent);
            } else {// 暂停所有
                progressIntent.putExtra(DownloadParam.STATE,
                        DownloadParam.TASK.PAUSE_ALL);
                mContext.sendBroadcast(progressIntent);
            }
            break;
        case DownloadParam.TASK.CONTINUE:// 下载
            if (item != null) {//
                log.d("下载" + item.getmName());
                progressIntent.putExtra(DownloadParam.GAMESTRING,
                        item.getKey());
                progressIntent.putExtra(DownloadParam.GAMECODE, item.getGameCode());
                progressIntent.putExtra(DownloadParam.STATE,
                        DownloadParam.TASK.CONTINUE);
                progressIntent.putExtra(DownloadParam.PACKAGE_NAME, 
                        item.getmPackageName());
                mContext.sendBroadcast(progressIntent);
            } else {// 
                progressIntent.putExtra(DownloadParam.STATE,
                        DownloadParam.TASK.CONTINUE_ALL);
                mContext.sendBroadcast(progressIntent);
            }
            break;
        case DownloadParam.TASK.WAITING_PAUSE:// 等待
            if (item != null) {//
                log.d("等待" + item.getmName());
                progressIntent.putExtra(DownloadParam.GAMESTRING,
                        item.getKey());
                progressIntent.putExtra(DownloadParam.GAMECODE, item.getGameCode());
                progressIntent.putExtra(DownloadParam.STATE,
                        DownloadParam.TASK.WAITING_PAUSE);
                mContext.sendBroadcast(progressIntent);
            } 
            
//            progressIntent.putExtra(DownloadParam.STATE,
//                    DownloadParam.C_STATE.WAITING);
//            mContext.sendBroadcast(progressIntent);
            break;
        default:
            Toast.makeText(mContext,mContext.getString(R.string.download_exception),Toast.LENGTH_SHORT).show();
            break;
        }
    }
    
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-13下午4:56:02
     *
     */
    private void showDownloadNotification () {
        int num = mDownloadingList.size() + mPendingQueue.size();
        int pausenum = mPauseQueue.size();
        if (num > 0) {
            StringBuffer content = new StringBuffer();
            if (mDownloadingList != null && mDownloadingList.size() > 0) {
                for (DownloadTask downloadTask : mDownloadingList) {
                    if (downloadTask.getDownloadItem() != null) {
                        content.append(downloadTask.getDownloadItem().getmName());
                        content.append("、");
                    }
                }
                
            }
            if (mPendingQueue != null && mPendingQueue.size() > 0) {
                for (DownloadTask downloadTask : mPendingQueue) {
                    if (downloadTask.getDownloadItem() != null) {
                        content.append(downloadTask.getDownloadItem().getmName());
                        content.append("、");
                    }
                }
            }
            showNotification(mContext.getResources().getString(R.string.notification_downloading_text, num), content.substring(0, content.length()-1));
        }else if (pausenum > 0){
        	showPauseNotification();
        }else{
        	cancelNotification();
        }
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-13下午4:59:03
     *
     */
    private void showPauseNotification () {
        int pausenum = mPauseQueue.size();
        int downloadingNum = mDownloadingList.size() + mPendingQueue.size();
        if (pausenum > 0 && downloadingNum == 0) {
            StringBuffer content = new StringBuffer();
            if (mPauseQueue != null && mPauseQueue.size() > 0) {
                for (DownloadTask downloadTask : mPauseQueue) {
                    if (downloadTask.getDownloadItem() != null) {
                        content.append(downloadTask.getDownloadItem().getmName());
                        content.append("、");
                    }
                }
            }
            showNotification(mContext.getResources().getString(R.string.notification_all_pause_text, pausenum), content.substring(0, content.length()-1));
        }
    }
    
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-13下午4:55:34
     *
     * @param title
     * @param content
     */
    private void showNotification (String title,String content) {
        Intent intent = new Intent(mContext, DownloadActivity.class);
        PendingIntent pending = PendingIntent.getActivity(mContext, 0,intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification(R.drawable.icon_small, title,System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        RemoteViews view = new RemoteViews(mContext.getPackageName(),R.layout.notification_download);
        view.setTextViewText(R.id.notification_download_main_tv,title);
        view.setTextViewText(R.id.notification_download_sub_tv, content);
//        view.setTextViewText(R.id.notification_download_time, Util.getDate(System.currentTimeMillis()/1000));
        notification.contentView = view;
        if (pending != null) {
            notification.contentIntent = pending;
        }
        mNotificationManager.notify(DOWNLOAD_NOTIFICATION,notification);
    }
    
    
    private void cancelNotification () {
        int pausenum = mPauseQueue.size();
        int pendingNum = mPendingQueue.size();
        int downloadingNum = mDownloadingList.size();
        if (pausenum == 0 && pendingNum == 0 && downloadingNum == 0) {
            mNotificationManager.cancel(DOWNLOAD_NOTIFICATION);
        }
    }
    
    private void sendDownloadTaskCountBroadcast (int count,int status,DownloadItem item) {
        Intent downloadIntent = new Intent(Contants.ACTION.DOWNLOADING_COUNT);
        downloadIntent.putExtra(DownloadParam.STATE, status);
        downloadIntent.putExtra(DownloadParam.DOWNLOAD_COUNT, count);
        if(item != null){
            downloadIntent.putExtra(DownloadParam.DOWNLOAD_ITEM, item);
        }
        mContext.sendBroadcast(downloadIntent);
        
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public boolean startManage() {
        reStartDownloading();
        log.i("start manager thread ");
        mTimer = new Timer();
        this.isRunning = true;
        // 从DB中查出未完成的任务
        mTimer.schedule(new Heart(), 0, 1000);
        return this.isRunning;
    }

    public void stopManage() {
        this.isRunning = false;
        log.i("DownloadManager:stop");
        mTimer.cancel();
    }

    /**
     * Test
     */
//    private void printCurrentQueue() {
//        log.v("正在下载的有：");
//        for (DownloadTask task : mDownloadingList) {
//            log.i(task.getDownloadItem().getmName());
//        }
//        log.v("等待下载的有：");
//        Iterator<DownloadTask> pendingIt = mPendingQueue.iterator();
//        while (pendingIt.hasNext()) {
//            DownloadTask task = (DownloadTask) pendingIt.next();
//            log.i(task.getDownloadItem().getmName());
//        }
//        log.v("被暂停的有：" + mPauseQueue.size());
//        Iterator<DownloadTask> pauseIt = mPauseQueue.iterator();
//        while (pauseIt.hasNext()) {
//            DownloadTask task = (DownloadTask) pauseIt.next();
//            log.i(task.getDownloadItem().getmName());
//        }
//    }

    /**
     * 
     * tuozhonghua_zk
     * 2013-11-11上午10:41:01
     *
     * @param item
     */
    public synchronized void pause(DownloadItem item) {
        DownloadItem itemFromDB = mDao.getDowloadItem(item.getmPackageName(),
                item.getVersionName());
        if (itemFromDB != null) {
            itemFromDB.setmState(DownloadParam.C_STATE.PAUSE);
            mDao.updateItem(itemFromDB);
            this.sendTaskStatusBroadcast(itemFromDB, DownloadParam.TASK.PAUSE);
            mPauseQueue.add(item2task(itemFromDB));
            // 先从正在下载中寻找
            Iterator it = mDownloadingList.iterator();
            while (it.hasNext()) {
                DownloadTask task = (DownloadTask) it.next();
                if (task.getDownloadItem().getKey().equals(item.getKey())) {
                    task.pauseTask();
                    it.remove();
                    log.d(item.getmName() + " pased");
                    hasTask();
//                    printCurrentQueue();
                    //显示通知栏暂停信息
                    showDownloadNotification();
                    return;
                }
            }
            // 若找不到，然后从等待队列里寻找
            Iterator pendingIt = mPendingQueue.iterator();
            while (pendingIt.hasNext()) {
                DownloadTask task = (DownloadTask) pendingIt.next();
                if (task.getDownloadItem().getKey().equals(item.getKey())) {
                    task.pauseTask();
                    pendingIt.remove();
                    log.d(item.getmName() + " pased");
                    hasTask();
//                    printCurrentQueue();
                    //显示通知栏暂停信息
                    showDownloadNotification();
                    return;
                }
            }

//            printCurrentQueue();
        } else {
            log.e("DB中没找到" + item.getmName());
        }
        hasTask();
    }

    /**
     * 暂停所有下载任务并关闭Manager
     */
    public void pauseAll() {
        isPause = true;
        try {
            log.d("pauseAll");
            Iterator<DownloadTask> downloadIterator = mDownloadingList
                    .iterator();
            while (downloadIterator.hasNext()) {
                DownloadTask task = (DownloadTask) downloadIterator.next();
                task.getDownloadItem().setmState(DownloadParam.C_STATE.PAUSE);
                mDao.updateItem(task.getDownloadItem());
                task.pauseTask();
                log.d("下载中暂停:" + task.getDownloadItem().getmName());
                downloadIterator.remove();
                mPauseQueue.add(task);
            }
            Iterator<DownloadTask> pendingIterator = mPendingQueue.iterator();
            while (pendingIterator.hasNext()) {
                DownloadTask task = (DownloadTask) pendingIterator.next();
                task.getDownloadItem().setmState(DownloadParam.C_STATE.PAUSE);
                mDao.updateItem(task.getDownloadItem());
                task.pauseTask();
                log.d("下载中暂停:" + task.getDownloadItem().getmName());
                pendingIterator.remove();
                mPauseQueue.add(task);
            }
          //显示通知栏暂停信息
            showDownloadNotification();
            this.sendTaskStatusBroadcast(null, DownloadParam.TASK.PAUSE);
//            printCurrentQueue();
            isPause = false;
        } catch (Exception e) {
            log.e(e);
        }
        hasTask();
    }

    /**
     * 将该url对应的下载任务加入到下载队列中
     * 
     * @param url
     */
    public void add(DownloadItem item) {
        // 如果mDownloadingCache没有该url的任务则可以Add
        if (!Util.isDownloadUrl(item.getmURL())
                || TextUtils.isEmpty(item.getVersionName())
                || TextUtils.isEmpty(item.getmPackageName())) {
            Toast.makeText(mContext, R.string.download_exception,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //如果是升级，先删除之前的升级记录
        if (item.getmPackageName() != null && item.getmPackageName().equals(mContext.getPackageName())) {
            delete(item);
        }
        if (item != null && !mDao.isHasInfos(item)) {
            log.d("mDownloadingCache中没有" + item.getmName());
            item.setmState(DownloadParam.C_STATE.WAITING);// 状态为等待中
            DownloadTask task = item2task(item);
            mPendingQueue.add(task);
//            this.sendTaskStatusBroadcast(item, DownloadParam.TASK.WAITING_PAUSE);
            task.getDownloadItem().setmState(DownloadParam.C_STATE.WAITING);
            if (!mDao.isHasInfos(task.getDownloadItem())) {
                // 下载之前将url存储到DB中
                mDao.save(task.getDownloadItem());
            } else {
                mDao.updateItem(item);
            }
            //显示通知栏信息
            showDownloadNotification();
            this.sendTaskStatusBroadcast(item, DownloadParam.TASK.ADD);
            downloadStatics(item,"start");
        } else {
            log.d("mDownloadingCache有" + item.getmName());
            this.sendTaskStatusBroadcast(item, DownloadParam.TASK.HAD_CONTAIN_TASK);
        }
        if (hasTask() && !isRunning()) {
            this.startManage();
        }
    }
    /**
     * 直接加入到暂停队列
     * tuozhonghua_zk
     * 2013-11-19上午11:45:43
     *
     * @param item
     */
    public void addPause(DownloadItem item) {
        // 如果mDownloadingCache没有该url的任务则可以Add
        if (!Util.isDownloadUrl(item.getmURL())
                || TextUtils.isEmpty(item.getVersionName())
                || TextUtils.isEmpty(item.getmPackageName())) {
            Toast.makeText(mContext, R.string.download_exception,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        //如果是升级，先删除之前的升级记录
        if (item.getmPackageName() != null && item.getmPackageName().equals(mContext.getPackageName())) {
            delete(item);
        }
        if (item != null && !mDao.isHasInfos(item)) {
            log.d("mDownloadingCache中没有" + item.getmName());
            item.setmState(DownloadParam.C_STATE.PAUSE);// 状态为等待中
            DownloadTask task = item2task(item);
            task.getDownloadItem().setmState(DownloadParam.C_STATE.PAUSE);
            mPauseQueue.add(task);
            if (!mDao.isHasInfos(task.getDownloadItem())) {
                // 下载之前将url存储到DB中
                mDao.save(task.getDownloadItem());
            } else {
                mDao.updateItem(item);
            }
            this.sendTaskStatusBroadcast(item, DownloadParam.TASK.ADD);
//            downloadStatics(item,"start");
        } else {
            log.d("mDownloadingCache有" + item.getmName());
            this.sendTaskStatusBroadcast(item, DownloadParam.TASK.HAD_CONTAIN_TASK);
        }
        if (hasTask() && !isRunning()) {
            this.startManage();
        }
    }
    /**
     * 下载统计
     * tuozhonghua_zk
     * 2013-9-27下午4:03:54
     *
     * @param item
     * @param type 类型 start 开始下载 end 下载结束
     */
    private void downloadStatics (final DownloadItem item,final String type) {
        RequestParams params = new RequestParams();
        params.put("gamecode", item.getGameCode());
        params.put("gamename", item.getmName());
        params.put("type", type);
        MyHttpConnect.getInstance().post2Json(HttpContants.NET.DOWNLOAD_STATICS, params, new JSONAsyncHttpResponseHandler(){
            @Override
            public void onFailure(Throwable error, String content) {
                log.e("downloadStatics fail:gamecode=" + item.getGameCode() + "gamename=" + item.getmName()+ "type="+ type + "response=" + content);
            }
            
            @Override
            public void onSuccess(int statusCode,JSONObject response) {
                if (response == null) {
                    log.e("downloadStatics fail:gamecode=" + item.getGameCode() + "gamename=" + item.getmName()+ "type="+ type );
                    return;
                }
                log.i(" downloadStatics userinfo result is:"+ response.toString());
                RootPojo rootPojo = JsonUtils.fromJson(response.toString(), RootPojo.class);
                if (null == rootPojo) {
                    log.e("downloadStatics fail:gamecode=" + item.getGameCode() + "gamename=" + item.getmName()+ "type="+ type );
                    return;
                }
                try {
                    String successful = rootPojo.getSuccessful();
                    if (String.valueOf(Contants.SEND_DYNAMIC_STATUS.SUCCESS).equals(successful)) {
                        log.i("downloadStatics success:gamecode=" + item.getGameCode() + "gamename=" + item.getmName()+ "type="+ type );
                        return;
                    }else {
                        log.e("downloadStatics fail:gamecode=" + item.getGameCode() + "gamename=" + item.getmName()+ "type="+ type );
                    }
                } catch (Exception e) {
                    log.e("downloadStatics fail:gamecode=" + item.getGameCode() + "gamename=" + item.getmName()+ "type="+ type +"/n"+ e );
                    return;
                }
            }
        });
    }

    /**
     * 继续下载该item对应的任务 Heart会轮询执行pendingQueue中任务
     * 
     * @param url
     */
    public synchronized void cont(DownloadItem item) {
        DownloadItem itemFromDB = mDao.getDowloadItem(item.getmPackageName(),
                item.getVersionName());
        if (itemFromDB != null) {
            if (itemFromDB.getmState() == DownloadParam.C_STATE.PAUSE) {
                itemFromDB.setmState(DownloadParam.C_STATE.WAITING);
                mDao.updateItem(itemFromDB);
                Iterator<DownloadTask> it = mPauseQueue.iterator();
                while (it.hasNext()) {
                    DownloadTask t = (DownloadTask) it.next();
                    if (t.getDownloadItem().getKey().equals(item.getKey())) {
                        it.remove();
                        break;
                    }
                }
                DownloadTask task = item2task(itemFromDB);
                mPendingQueue.add(task);
//                printCurrentQueue();
                showDownloadNotification();
                if (mDownloadingList.size() >= TASK_MAX_COUNT) {
                    this.sendTaskStatusBroadcast(item, DownloadParam.TASK.WAITING_PAUSE);
                }
            } else {
                log.e(item.getmName() + " state = " + itemFromDB.getmState());
            }
        } else {
            log.e(item.getmName() + " 没从DB中找到");
        }
        if (hasTask() && !isRunning()) {
            this.startManage();
        }
    }

    /**
     * 开始全部
     * 
     * @param url
     */
    public synchronized void contAll() {
        updateAllState();
        Iterator<DownloadTask> it = mPauseQueue.iterator();
        while (it.hasNext()) {
            DownloadTask t = (DownloadTask) it.next();
            it.remove();
            t.getDownloadItem().setmState(DownloadParam.C_STATE.WAITING);
            mDao.updateItem(t.getDownloadItem());
            mPendingQueue.add(t);
        }
        showDownloadNotification();
        //由于tast启动时会发广播，所以此处不用发广播了
        if (mDownloadingList.size() >= TASK_MAX_COUNT) {
            this.sendTaskStatusBroadcast(null, DownloadParam.TASK.CONTINUE);
            
        }
        if (hasTask() && !isRunning()) {
            this.startManage();
        }
//        printCurrentQueue();
    }

    /**
     * 若状态为正在下载的没有下载,则执行下载操作
     */
    public void reStartDownloading() {
        updateAllState();
//        printCurrentQueue();
        for (DownloadTask task : mDownloadingList) {
            // if (task.isInterrupt()) {// 若暂停 则重新开启
            try {
                task.execute();
            } catch (Exception e) {
                log.e(e);
            }
            // }
        }
    }

    /**
     * 优先下载
     * 
     * @param url
     */
    public synchronized void downloadFirst(DownloadItem item) {
        if (item != null) {
            if (item.getmState() == DownloadParam.C_STATE.WAITING) {
                try {
                    int size = mDownloadingList.size();// 移除最后一个
                    if (size == TASK_MAX_COUNT) {
                        DownloadTask lastTask = mDownloadingList.get(size - 1);
                        try {
                            lastTask.pauseTask();
                            mDownloadingList.remove(lastTask);
                            lastTask.getDownloadItem().setmState(DownloadParam.C_STATE.WAITING);
                            mDao.updateItem(lastTask.getDownloadItem());
                            mPendingQueue.add(lastTask);
                        } catch (Exception e) {
                            log.e("downloadFirst"+ e);
                        }
                    }
                    DownloadTask task = item2task(item);// 增加到下载队列
                    for (DownloadTask downloadTask : mPendingQueue) {
                        if (downloadTask.getDownloadItem().getKey().equals(item.getKey())) {
                            mPendingQueue.remove(downloadTask);
                            item.setmState(DownloadParam.C_STATE.DOWNLOADING);
                            mDao.updateItem(item);
                            mDownloadingList.add(task);
                            this.sendTaskStatusBroadcast(item, DownloadParam.TASK.WAITING_PAUSE);
                            try {
                                task.execute();
                            } catch (Exception e) {
                                log.e(e);
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    log.e(e);
                }
            } else {
                log.e(item.getmName() + " state = " + item.getmState());
            }
        } else {
            log.e(item.getmName() + " 没从DB中找到");
        }
        hasTask();
    }

    /**
     * 删除已经下载完成或者安装的
     * 
     * @param url
     */
    public void deleteAllDone() {
        //下载完成的，删除文件后，发广播给游戏库更新状态
//        List<DownloadItem> list = mDao.getDownloadItemsByState(DownloadParam.C_STATE.DONE);
//        if (list != null && list.size() > 0) {
//            for (DownloadItem item : list) {
//                this.sendTaskStatusBroadcast(item, DownloadParam.TASK.DELETE);
//            }
//        }
        // 删除DB中该任务的下载记录item
        mDao.deleteAllDone();
        deleteAllDoneFile();
        this.sendTaskStatusBroadcast(null, DownloadParam.TASK.DELETE);
    }

    /**
     * 删除该url对应的下载任务
     * 
     * @param url
     */
    public void delete(DownloadItem item) {
        // 删除DB中该任务的下载记录
        mDao.delete(item);
        deleteFile(item);
        // 正在下载中
        Iterator ingIt = mDownloadingList.iterator();
        while (ingIt.hasNext()) {
            DownloadTask task = (DownloadTask) ingIt.next();
            if (task.getDownloadItem().getKey().equals(item.getKey())) {
                ingIt.remove();
                task.deleteTask();
                log.d(item.getmName() + " deleted");
                hasTask();
//                printCurrentQueue();
                //显示通知栏信息
                showDownloadNotification();
                return;
            }
        }
        // 等待中的
        Iterator pendingIt = mPendingQueue.iterator();
        while (pendingIt.hasNext()) {
            DownloadTask task = (DownloadTask) pendingIt.next();
            if (task.getDownloadItem().getKey().equals(item.getKey())) {
                pendingIt.remove();
//                task.pauseTask();
                task.deleteTask();
                log.d(item.getmName() + " deleted");
                hasTask();
//                printCurrentQueue();
                //显示通知栏信息
                showDownloadNotification();
                return;
            }
        }
        // 等待暂停的游戏
        Iterator pauseIt = mPauseQueue.iterator();
        while (pauseIt.hasNext()) {
            DownloadTask task = (DownloadTask) pauseIt.next();
            if (task.getDownloadItem().getKey().equals(item.getKey())) {
                pauseIt.remove();
//                task.pauseTask();
                task.deleteTask();
                hasTask();
                log.d(item.getmName() + " deleted");
//                printCurrentQueue();
                //显示通知栏信息
                showDownloadNotification();
                return;
            }
        }
      this.sendTaskStatusBroadcast(item, DownloadParam.TASK.DELETE);
        hasTask();
//        printCurrentQueue();
    }

    // /**
    // * 通过包名和版本号对下载文件进行命名
    // *
    // * @param item
    // * @return
    // */
    // public String getGameFileName(DownloadItem item) {
    // if (null == item) {
    // return "";
    // }
    // return item.getmPackageName() + "_" + item.getVersionName();
    // }

    /**
     * 删除该DownloadItem对应的已下载文件或临时文件
     * 
     * @param item
     */
    private void deleteFile(DownloadItem item) {
        try {
            String fileName = item.getKey();
            File tempFile = new File(SharedPreferenceUtil.getAPKPath(mContext),
                    fileName + DownloadParam.TEMP_SUFFIX);
            if (tempFile.exists()) {
                if (tempFile.delete()) {
                    log.d(item.getmName() + " 删除临时文件成功");
                }
                return;
            } else {
                File file = new File(SharedPreferenceUtil.getAPKPath(mContext),
                        fileName + Config.APK_SUFFIX);
                if (file.exists()) {
                    if (file.delete()) {
                        log.d(item.getmName() + " 删除文件成功");
                    }
                    return;
                }
            }
        } catch (Exception e) {
            log.e(e);
        }
    }

    /**
     * 删除该DownloadItem对应的已下载文件或临时文件
     * 
     * @param item
     */
    private void deleteAllDoneFile() {
        try {
            File tempFilePath = new File(
                    SharedPreferenceUtil.getAPKPath(mContext));
            if (tempFilePath.exists()) {
                for (File tempFile : tempFilePath.listFiles()) {
                    if (tempFile.getName().contains(Config.APK_SUFFIX)) {
                        tempFile.delete();
                    }
                }
                return;
            }
        } catch (Exception e) {
            log.e(e);
        }
    }
    
    private boolean hasTask () {
        if (this.mDownloadingList != null && this.mDownloadingList.size() > 0) {
            hasTask = true;
        }else if (this.mPendingQueue != null && this.mPendingQueue.size() > 0) {
            hasTask = true;
        }else {
            hasTask = false;
        }
        return hasTask;
    }
    
    /**
     * 生成该url对应的DownloadTask
     * 
     * @param url
     */
    private DownloadTask item2task(final DownloadItem item) {
        DownloadListener listener = new DownloadListener() {

            @Override
            public void updateProgress(String gameString, int percent, int speed) {
                Intent intent = new Intent(DownloadParam.UPDATE_PROGRESS_ACTION);
                intent.putExtra(DownloadParam.GAMESTRING, gameString);
                intent.putExtra(DownloadParam.GAMECODE, item.getGameCode());
                intent.putExtra(DownloadParam.PERCENT, percent);
                intent.putExtra(DownloadParam.STATE, item.getmState());
                intent.putExtra(DownloadParam.SPEED, speed);
                mContext.sendBroadcast(intent);
                log.i("DownloadListener updateProgress DownloadParam.PERCENT = " + percent);
                
            }

            @Override
            public void finish(DownloadTask task) {
                log.d("finish:" + task.getDownloadItem().getmName());
                int index = 0;
                Iterator<DownloadTask> it = mDownloadingList.iterator();
                while (it.hasNext()) {
                    DownloadTask d = (DownloadTask) it.next();
                    if (d.getDownloadItem().getKey()
                            .equals(task.getDownloadItem().getKey())) {
                        it.remove();
                        break;
                    }
                }
                hasTask();
                showDownloadNotification();
                cancelNotification();
                // 发送进度100%广播
                sendTaskStatusBroadcast(task.getDownloadItem(), DownloadParam.TASK.DONE);
                log.d("finish:index = " + index);
                item.setmState(DownloadParam.C_STATE.DONE);
                mDao.updateItem(item);
                Util.install(task.getDownloadItem(), mContext);
//                Intent i = new Intent(Intent.ACTION_VIEW);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.setDataAndType(Uri.fromFile(task.getFile()),
//                        "application/vnd.android.package-archive");
//                mContext.startActivity(i);
                if (task != null) {
                    downloadStatics(item,"end");
                }
            }

            @Override
            public void preDownload(DownloadTask task) {

            }

            @Override
            public void error(DownloadTask task, int error) {
                try {

//                    int index = mDownloadingList.indexOf(task);
//                    log.d("Current Index : " + index);
//                    if (error != DownloadParam.ERROR.DOWNLOAD_ERROR && error != DownloadParam.ERROR.NETWORK_INTERRUPT ) {
//                        if (-1 != index) {
//                            pause(task.getDownloadItem());
//                            mDownloadingList.remove(task);
//                        }
//                    } else {
//                        if (-1 != index) {
//                            task.getDownloadItem().setmState(
//                                    DownloadParam.C_STATE.PAUSE);
//                            mDao.updateItem(task.getDownloadItem());
//                            mDownloadingList.remove(task);
//                        }
//                    }
//                    String str = task.getDownloadItem().getmName();
                    PendingIntent pending = null;
                    switch (error) {
                    case DownloadParam.ERROR.DOWNLOAD_ERROR:
                        pause(item);
                        Toast.makeText(mContext,R.string.download_exception,Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(mContext,DownloadActivity.class);
//                        pending = PendingIntent.getActivity(mContext, 0,intent, PendingIntent.FLAG_ONE_SHOT);
                        break;
                    case DownloadParam.ERROR.FILE_HAD_DOWNLOAD:
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.fromFile(task.getFile()),"application/vnd.android.package-archive");
                        pending = PendingIntent.getActivity(mContext, 0, i,PendingIntent.FLAG_UPDATE_CURRENT);
                        Toast.makeText(mContext,R.string.download_error_had_downloaded,Toast.LENGTH_SHORT).show();
                        break;
                    case DownloadParam.ERROR.NO_MEMORY:
                        Toast.makeText(mContext,R.string.download_error_no_memory,Toast.LENGTH_SHORT).show();
                        pause(item);
//                        intent = new Intent(mContext, DownloadActivity.class);
//                        pending = PendingIntent.getActivity(mContext, 0,intent, PendingIntent.FLAG_ONE_SHOT);
                        break;
                    case DownloadParam.ERROR.NETWORK_INTERRUPT:
                        Toast.makeText(mContext,R.string.error_txt,Toast.LENGTH_SHORT).show();
//                        intent = new Intent(mContext, DownloadActivity.class);
//                        pending = PendingIntent.getActivity(mContext, 0,intent, PendingIntent.FLAG_ONE_SHOT);
                        pause(item);
//                        pauseAll();
                       break;
                    case DownloadParam.ERROR.URL_ERROR:// 下载地址错误
                        Toast.makeText(mContext, R.string.download_url_error,Toast.LENGTH_SHORT).show();
//                        intent = new Intent(mContext, DownloadActivity.class);
//                        pending = PendingIntent.getActivity(mContext, 0,intent, PendingIntent.FLAG_ONE_SHOT);
                        break;
                    }
                    
//                    Notification notification = new Notification(R.drawable.icon_small, str,System.currentTimeMillis());
//                    // 放到“正在运行”栏目中
//                    notification.flags = Notification.FLAG_AUTO_CANCEL;
//                    RemoteViews view = new RemoteViews(mContext.getPackageName(),R.layout.notification_download);
//                    view.setTextViewText(R.id.notification_download_main_tv,mContext.getString(R.string.pengyou_download_manager));
////                    view.setTextViewText(R.id.notification_download_time, Util.getDate(System.currentTimeMillis()/1000));
//                    view.setTextViewText(R.id.notification_download_sub_tv, str);
//                    // 制定自定义视图
//                    notification.contentView = view;
//                    if (null != pending) {
//                        notification.contentIntent = pending;
//                    }
//                    mNotificationManager.notify(mDownloadingList.indexOf(task),notification);
                    
                } catch (Exception e) {
                    log.e(e);
                }
            }

            @Override
            public void updateItemInDB(DownloadItem item) {
                mDao.updateItem(item);
            }

            @Override
            public void deleteTask(DownloadItem item) {
                sendTaskStatusBroadcast(item, DownloadParam.TASK.DELETE);
//                Intent intent = new Intent(DownloadParam.UPDATE_PROGRESS_ACTION);
//                intent.putExtra(DownloadParam.GAMESTRING, item.getKey());
//                intent.putExtra(DownloadParam.PERCENT, 0);
//                intent.putExtra(DownloadParam.STATE,
//                        DownloadParam.C_STATE.DELETE);
//                mContext.sendBroadcast(intent);
            }
        };
        DownloadTask task = null;
        try {
            if (item != null) {
                task = new DownloadTask(item, listener, mContext);
            }
        } catch (Exception e) {
            log.e(e);
        }
        return task;
    }

    /**
     * 获取所有任务的List 包括下载中暂停 和等待中暂停
     * 
     * @return
     */
    private List<DownloadItem> getTaskList() {
        List<DownloadItem> list = new ArrayList<DownloadItem>();
        for (DownloadTask task : mDownloadingList) {
            list.add(task.getDownloadItem());
        }
        for (DownloadTask task : mPendingQueue) {
            list.add(task.getDownloadItem());
        }
        for (DownloadTask task : mPauseQueue) {
            list.add(task.getDownloadItem());
        }
        return list;
    }

    private class Heart extends TimerTask {
        @Override
        public void run() {
            if (isPause == true) {
                return;
            }
            if (!mPendingQueue.isEmpty()) {
                if (mDownloadingList.size() < TASK_MAX_COUNT) {
                    log.i("mDownloadingList.size = " + mDownloadingList.size());
                    DownloadTask task = mPendingQueue.poll();
                    log.d("find a pending task");
                    mPendingQueue.remove(task);
                    task.getDownloadItem().setmState(
                            DownloadParam.C_STATE.DOWNLOADING);
                    mDao.updateItem(task.getDownloadItem());
                    if (task != null) {
                        try {
                            task.execute();
                            mDownloadingList.add(task);
                            log.i("task.execute()");
                            sendTaskStatusBroadcast(task.getDownloadItem(), DownloadParam.TASK.CONTINUE);
                        } catch (Exception e) {
                            DownloadItem item = task.getDownloadItem();
                            task.deleteTask();
                            task = null;
//                            task = item2task(item);
//                            try {
//                                task.execute();
//                                mDownloadingList.add(task);
//                                sendTaskStatusBroadcast(task.getDownloadItem(), DownloadParam.TASK.CONTINUE);
//                            } catch (Exception e2) {
//                                log.e(e2);
//                            }
                            log.e("mDownloadingList has contain the same task"+e);
                        }
                    }
                } else if (mDownloadingList != null
                        && mDownloadingList.size() > TASK_MAX_COUNT) {
                    Iterator<DownloadTask> downloadIterator = mDownloadingList
                            .iterator();
                    int temp = 1;
                    while (downloadIterator.hasNext()) {
                        if (temp > TASK_MAX_COUNT) {
                            DownloadTask task = (DownloadTask) downloadIterator
                                    .next();
                            task.getDownloadItem().setmState(
                                    DownloadParam.C_STATE.WAITING);
                            mDao.updateItem(task.getDownloadItem());
                            task.pauseTask();
                            log.d("下载中暂停:" + task.getDownloadItem().getmName());
                            downloadIterator.remove();
                            mPendingQueue.add(task);
                            log.i("task.pauseTask");
                            sendTaskStatusBroadcast(task.getDownloadItem(), DownloadParam.TASK.CONTINUE);
                        }
                        temp++;
                    }
                    return;
                }
            } else {
                if (!hasTask) {
                    log.i("mDownloadingList.size = " + mDownloadingList.size());
                    if (mDownloadingList.isEmpty()) {
                        isRunning = false;
                        stopManage();
                    }
                }
            }
        }

    }
}
