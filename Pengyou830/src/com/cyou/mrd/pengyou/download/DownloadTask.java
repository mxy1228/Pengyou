package com.cyou.mrd.pengyou.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpClient;

public class DownloadTask extends AsyncTask<Void, Long, Long> {

    private static final String TAG = "DownloadTask";
    private CYLog log = CYLog.getInstance();

    private final static int BUFFER_SIZE = 1024 * 8;

    private URL mURL;
    private Context mContext;
    private DownloadItem mItem;
    private DownloadListener mListener;
    private long mTotalSize = 0;// 下载文件的总长度
    private long mHadWriteSize = 0;// 已经在临时文件中写入的长度
    private int mProgressPercent = 0;// 已下载的百分比
    private long mPreviousSize = 0;// 下载之前临时文件已经存储的长度
    private File mFile;
    private File mTempFile;
    private boolean interrupt = false;
    private long mPreviousTime = 0;
    private long mTotalTime = 0;
    private int mSpeed = 0;
    public String mGameCode;// 游戏唯一标识
    private MyHttpConnect myHttpConnect;
    
    private int tempPercent = 0;//用于记录下载的临时百分比
    private long mTempFilesize = 0L;//用于记录下载的临时大小
    private final int  MAX_TEMPFILE_SIZE = 2*100*1024;

    public DownloadTask(DownloadItem item, DownloadListener listener,
            Context context) {
        try {
            this.mContext = context;
            this.mItem = item;
            try {
                if (item.getmURL() != null) {
                    this.mURL = new URL(item.getmURL());
                }else {
                    listener.error(this, DownloadParam.ERROR.URL_ERROR);
                }
            } catch (Exception e) {
                log.e(e);
                listener.error(this, DownloadParam.ERROR.URL_ERROR);
            }
            this.mListener = listener;
            String fileName = item.getKey();
            this.mFile = new File(
                    SharedPreferenceUtil
                            .getAPKPath(CyouApplication.mAppContext),
                    fileName + Config.APK_SUFFIX);
            this.mTempFile = new File(
                    SharedPreferenceUtil
                            .getAPKPath(CyouApplication.mAppContext),
                    fileName + DownloadParam.TEMP_SUFFIX);
            myHttpConnect = MyHttpConnect.getInstance();
        } catch (Exception e) {
            log.e(e);
        }
    }

    public DownloadItem getDownloadItem() {
        return this.mItem;
    }

    public File getFile() {
        return this.mFile;
    }

    @Override
    protected Long doInBackground(Void... params) {
        return download();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        cancel(true);
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Long result) {
        if (result == DownloadParam.ERROR.DOWNLOAD_ERROR) {
            if (this.mListener != null) {
                this.mListener.error(this, DownloadParam.ERROR.DOWNLOAD_ERROR);
            }
        }else if (result == DownloadParam.ERROR.NETWORK_INTERRUPT) {
            if (this.mListener != null) {
                this.mListener.error(this, DownloadParam.ERROR.NETWORK_INTERRUPT);
            }
        }else if (result == DownloadParam.ERROR.FILE_HAD_DOWNLOAD) {
            if (this.mListener != null) {
                this.mListener.finish(this);
            }
        } else if (result == DownloadParam.ERROR.NO_MEMORY) {
            Toast.makeText(mContext, R.string.download_error_no_memory,
                    Toast.LENGTH_SHORT).show();
            if (this.mListener != null) {
                this.mListener.error(this, DownloadParam.ERROR.NO_MEMORY);
            }
        } else if (result == DownloadParam.ERROR.DOWNLOAD_FINISH) {
    		mTempFile.renameTo(mFile);
    		mTempFile.delete();
    		mItem.setPath(mFile.getAbsolutePath());
    		if (this.mListener != null) {
    			// 成功
    			log.d("下载成功：" + mItem.getmName());
    			if (null != mGameCode) {
    				if (!TextUtils.isEmpty(mGameCode)) {
    					BehaviorInfo behaviorInfo = new BehaviorInfo(
    							CYSystemLogUtil.GAME_DOWNLOAD, mGameCode);
    					CYSystemLogUtil.behaviorLog(behaviorInfo);
    					// downloadGame(mGameCode,mItem.getmName());
    				}
    			}
    			this.mListener.finish(this);
    		}
        }
    }

    @Override
    protected synchronized void onPreExecute() {
        log.d("onPreExecute");
        this.mPreviousTime = System.currentTimeMillis();
        if (this.mListener != null) {
            this.mListener.preDownload(this);
        }

    }

    @Override
    protected void onProgressUpdate(Long... values) {
        if (isCancelled()) {
            return;
        }
        mTotalTime = System.currentTimeMillis() - mPreviousTime;
        if (mTotalSize > 0) {
            mProgressPercent = (int) ((values[0] + mPreviousSize) * 100 / mTotalSize);
            mSpeed = (int) (values[0] / mTotalTime);
            if (!interrupt) {
                if (mProgressPercent - tempPercent >= 1 && values[0] - mTempFilesize > MAX_TEMPFILE_SIZE) {
//                log.i("onProgressUpdate mProgressPercent = " + mProgressPercent + "values[0]=" + values[0]);
                    mListener.updateProgress(mItem.getKey(), mProgressPercent, mSpeed);
                    tempPercent = mProgressPercent;
                    mTempFilesize = values[0];
                }
            }
        }
        
    }

    /**
     * 删除任务
     */
    public void deleteTask() {
        this.cancel(true);
        this.interrupt = true;
        if (mListener != null) {
            mListener.deleteTask(mItem);
        }
    }
    /**
     * 暂停任务
     */
    public void pauseTask() {
        this.cancel(true);
        this.interrupt = true;
    }

    private class TempFile extends RandomAccessFile {

        public TempFile(File file, String mode) throws FileNotFoundException {
            super(file, mode);
        }

        @Override
        public void write(byte[] buffer, int byteOffset, int byteCount)
                throws IOException {
            super.write(buffer, byteOffset, byteCount);
            mHadWriteSize += byteCount;
            publishProgress(mHadWriteSize);
        }
    }

    private long download() {
        if (!isCancelled()) {
//            AndroidHttpClient client = null;
            HttpClient client = null;
            InputStream is = null;
            BufferedInputStream bis = null;
            RandomAccessFile random = null;
            try {
                mItem.setmState(DownloadParam.C_STATE.DOWNLOADING);
//                client = AndroidHttpClient.newInstance("DownloadTask");
                //解决在特殊网络环境无法下载问题
                AsyncHttpClient l = new AsyncHttpClient();
                client = l.getHttpClient();
                l.setTimeout(100 * 1000);
//                if (!Util.isDownloadUrl(mItem.getmURL())) {
//                    return DownloadParam.ERROR.DOWNLOAD_ERROR;
//                }
                HttpGet get = new HttpGet(mItem.getmURL());
                HttpResponse response = client.execute(get);
                this.mTotalSize = response.getEntity().getContentLength();
                if (this.mTotalSize <= 0) {
                    return DownloadParam.ERROR.DOWNLOAD_ERROR;
                }
                log.d(mItem.getmName() + " mTotalSize = " + mTotalSize);
                this.mItem.setmTotalSize(mTotalSize);
                if (mTempFile.exists()) {
                    log.d(mItem.getmName()
                            + " tempFile exist and the length = "
                            + mTempFile.length());
                    get.addHeader("Range", "bytes=" + mTempFile.length() + "-");
                    mPreviousSize = mTempFile.length();
                    log.d("mPreviousSize = " + mTempFile.length());
//                    client.close();
//                    client = AndroidHttpClient.newInstance("DownloadTask");
                    l = new AsyncHttpClient();
                    client = l.getHttpClient();
                    response = client.execute(get);
                } else if (this.mFile.exists()
                        && this.mFile.length() == this.mTotalSize) {
                    log.d(mItem.getmName() + " had been download complete");
                    this.mItem.setPath(mFile.getAbsolutePath());
//                    if (this.mListener != null) {
//                        this.mListener.updateItemInDB(mItem);
//                    }
                    // 该url的文件已经被下载
                    return DownloadParam.ERROR.FILE_HAD_DOWNLOAD;
                }
                this.mItem.setPath(mTempFile.getAbsolutePath());
                if (this.mListener != null) {
                    this.mListener.updateItemInDB(mItem);
                }
                if (DownloadUtil.getAvailableStorage() < (mTotalSize-this.mTempFile.length())) {
                    return DownloadParam.ERROR.NO_MEMORY;
                } else {
                    log.d(mItem.getmName() + " start download");
                    random = new TempFile(mTempFile, "rw");
                    publishProgress(Long.valueOf(0));
                    is = response.getEntity().getContent();
                    byte[] buffer = new byte[BUFFER_SIZE];
                    bis = new BufferedInputStream(is);
                    random.seek(random.length());
                    int count = 0;
                    int i = 0;
                    while (!interrupt) {
                        i = bis.read(buffer, 0, BUFFER_SIZE);
                        if (i == -1) {
                            if (count == 0) {
                                interrupt = true;
                            }else {
                            	Long tempFielSize = mTempFile.length();
                            	if (tempFielSize == this.mTotalSize) {
                            		return DownloadParam.ERROR.DOWNLOAD_FINISH;
                            	}else {
                            		return DownloadParam.ERROR.NETWORK_INTERRUPT;
                            	}
                            }
                            break;
                        }
                        random.write(buffer, 0, i);
                        count += i;
                    }
//                    client.close();
                    get.abort();
                    bis.close();
                    is.close();
                    random.close();
                    return count;
                }
            }catch (SocketTimeoutException e) {
            	log.e(mItem.getmURL()+e);
                return DownloadParam.ERROR.NETWORK_INTERRUPT;
//                if (!isNetWorkAvailable()) {
//                }
            }catch (Exception e) {
                log.e(e);
            } finally {
                if (client != null) {
//                    client.close();
                }
                client = null;
                try {
                    if (random != null) {
                        random.close();
                    }
                } catch (Exception e2) {
                    log.e(e2);
                }
                try {
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    log.e(e);
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    log.e(e);
                }
            }
        } else {
//            if (!isNetWorkAvailable()) {
//                return DownloadParam.ERROR.NETWORK_INTERRUPT;
//            }
        }
        return DownloadParam.ERROR.DOWNLOAD_ERROR;
    }

    /**
     * 判断网络是否可用
     * 
     * @return
     */
    private boolean isNetWorkAvailable() {
        ConnectivityManager connectivitymanager = (ConnectivityManager) CyouApplication.mAppContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivitymanager == null) {
            log.i("network is bad");
            return false;
        }
        NetworkInfo info = connectivitymanager.getActiveNetworkInfo();
        if (info == null || !info.isAvailable()) {
            log.i("network is bad");
            return false;
        }
        return true;
    }

    public boolean isInterrupt() {
        return interrupt;
    }

    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

}
