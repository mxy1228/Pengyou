package com.cyou.mrd.pengyou.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.Context;

import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.entity.ConversationItem;
import com.cyou.mrd.pengyou.entity.MyMessageItem;
import com.cyou.mrd.pengyou.log.CYLog;

public class MessageManager {
	private CYLog log = CYLog.getInstance();
	private static final int TIME = 3 * 1000;

	private Map<Integer,Queue<MyMessageItem>> mMessages;
	private static MessageManager mManager;
	private ReadWriteLock mLock;
	private Lock mWriteLock;
	private Lock mReadLock;
	private Timer mTimer;
	private LetterDao mDao;
	private Context mContext;
	private WriteDBListener mWriteDBListener;
	
	public static MessageManager getInstance(Context context){
		if(mManager == null){
			mManager = new MessageManager(context);
		}
		return mManager;
	}
	
	private MessageManager(Context context){
		mMessages = new HashMap<Integer, Queue<MyMessageItem>>();
		mLock = new ReentrantReadWriteLock();
		mWriteLock = mLock.writeLock();
		mReadLock = mLock.readLock();
		mTimer = new Timer();
		mTimer.schedule(new WriteHeart(), 0, TIME);
		mDao = new LetterDao(context);
	}
	
	/**
	 * 接受到消息后调用
	 * 将MyMessageItem加入到缓存中并等待写入DB
	 */
	public void receiveMessage(MyMessageItem item){
		if(mMessages == null){
			return;
		}
		mWriteLock.lock();
		try {
			log.d("收到"+item.getTauid()+":"+item.getNickname()+":"+item.getContent()+"的私信");
			if(mMessages.containsKey(item.getTauid())){
				Queue<MyMessageItem> queue = mMessages.get(item.getTauid());
				if(queue == null){
					log.e(item.getTauid()+"消息队列为空");
					return;
				}
				queue.add(item);
			}else{
				Queue<MyMessageItem> queue = new LinkedList<MyMessageItem>();
				queue.add(item);
				mMessages.put(item.getTauid(), queue);
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			mWriteLock.unlock();
		}
	}
	
	/**
	 * 轮询将mMessages中的消息写入到DB
	 * 轮询频率为10s
	 * @author xumengyang
	 *
	 */
	private class WriteHeart extends TimerTask{
		@Override
		public void run() {
			mReadLock.lock();
			try {
				if(mMessages.isEmpty()){
					return;
				}else{
					Iterator it = mMessages.entrySet().iterator();
					while(it.hasNext()){
						Map.Entry<Integer, Queue<MyMessageItem>> entry = (Map.Entry<Integer, Queue<MyMessageItem>>)it.next();
						Queue<MyMessageItem> queue = entry.getValue();
						if(queue == null || queue.isEmpty()){
							log.e("queue is null or empty");
							continue;
						}
						MyMessageItem item = queue.element();
						int count = mDao.insert(queue);
						it.remove();
						if(mWriteDBListener != null){
							ConversationItem conver = new ConversationItem();
							conver.setAvatar(item.getAvatar());
							conver.setLastLetter(item.getContent());
							conver.setNickname(item.getNickname());
							conver.setTime(System.currentTimeMillis());
							conver.setUid(item.getFrom());
							mWriteDBListener.insertDBComplete(count, conver);
						}
					}
				}
			} catch (Exception e) {
				log.e(e);
			}finally{
				mReadLock.unlock();
			}
			
		}
	}
	
	public interface WriteDBListener{
		public void insertDBComplete(int count,ConversationItem item);
	}
	
	public void setWriteDBListener(WriteDBListener listener){
		this.mWriteDBListener = listener;
	}
}
