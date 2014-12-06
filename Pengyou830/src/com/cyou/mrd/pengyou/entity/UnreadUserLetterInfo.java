package com.cyou.mrd.pengyou.entity;

/**
 * 所有未读私信的统计
 * @author xumengyang
 *
 */
public class UnreadUserLetterInfo {

	private int mTotalUnreadLetter;//所有未读私信数
	private int mTotalUnreadUser;//所有含有未读私信的用户数
	public int getmTotalUnreadLetter() {
		return mTotalUnreadLetter;
	}
	public void setmTotalUnreadLetter(int mTotalUnreadLetter) {
		this.mTotalUnreadLetter = mTotalUnreadLetter;
	}
	public int getmTotalUnreadUser() {
		return mTotalUnreadUser;
	}
	public void setmTotalUnreadUser(int mTotalUnreadUser) {
		this.mTotalUnreadUser = mTotalUnreadUser;
	}
	
	
}
