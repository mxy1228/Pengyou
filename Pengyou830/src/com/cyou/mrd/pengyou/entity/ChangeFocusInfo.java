package com.cyou.mrd.pengyou.entity;

import java.util.ArrayList;

public class ChangeFocusInfo {

	public int uid ;
	public int follow;
	private static ChangeFocusInfo changeFocusInfo;
	public static ArrayList<ChangeFocusInfo> changeList =null;
	
	private ChangeFocusInfo(){
		if(ChangeFocusInfo.changeList==null){
			ChangeFocusInfo.changeList = new ArrayList<ChangeFocusInfo>();
		}
	}
	
	public static ChangeFocusInfo getInstance(){
		if(changeFocusInfo==null){
			
			changeFocusInfo  = new ChangeFocusInfo();
		}
		return changeFocusInfo;
	}
}
