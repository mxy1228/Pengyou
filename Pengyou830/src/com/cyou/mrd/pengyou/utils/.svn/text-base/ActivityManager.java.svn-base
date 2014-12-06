package com.cyou.mrd.pengyou.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;

public class ActivityManager {
	private final Map<String, WeakReference<Activity>> activitiesMap = new HashMap<String, WeakReference<Activity>>();
	private final List<WeakReference<Activity>> activities = new CopyOnWriteArrayList<WeakReference<Activity>>();
	private WeakReference<Activity> currentActivity;

	private static ActivityManager fetionActMag = new ActivityManager();

	private ActivityManager() {

	}

	public static ActivityManager getInstance() {
		return fetionActMag;
	}

	private String getKey(Activity activity) {
		String key = null;
		key = activity.getClass().getSimpleName() + activity.hashCode();
		return key;
	}

	/**
	 * 添加一个页面到栈顶
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		this.currentActivity = new WeakReference<Activity>(activity);
		String key = getKey(activity);
		if (activitiesMap.containsKey(key)) {
			WeakReference<Activity> wr = activities.get(activities.size() - 1);
			// 如果已经存在该页面了，并且不在栈顶则将其从原来的位置移到栈顶
			if (wr.get() == null || wr.get() != activity) {
				removeFromList(activity, activities);
				activities.add(new WeakReference<Activity>(activity));
			}
			return;
		}
		WeakReference<Activity> wr = new WeakReference<Activity>(activity);
		activities.add(wr);
		activitiesMap.put(key, wr);

	}

	/**
	 * 清除掉已经finish的activity
	 * 
	 * @param activity
	 *            被finish的activity
	 */
	public void removeActivity(Activity activity) {
		removeFromList(activity, activities);
		activitiesMap.remove(getKey(activity));
	}

	private void removeFromList(Activity activity,
			List<WeakReference<Activity>> list) {
		int index = 0;
		List<Integer> destoryedActivities = new ArrayList<Integer>();
		for (WeakReference<Activity> wr : list) {
			if (wr.get() != null && wr.get() == activity) {
				destoryedActivities.add(index);
				break;
			} else if (wr.get() == null) {
				destoryedActivities.add(index);
			}
			index++;
		}
		for (Integer i : destoryedActivities) {
			list.remove(i);
		}
	}

	/**
	 * 清除栈中所有的页面，只在退出时调用
	 */
	public void finishAll() {
		for (WeakReference<Activity> wr : activities) {
			if (wr.get() != null && !wr.get().isFinishing()) {
				wr.get().finish();
			}
		}
	}

	/**
	 * 获取当前显示的页面，刚启动的时候有可能是null
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		if (currentActivity == null)
			return null;
		return currentActivity.get();
	}

	public Activity getCurrentActivityToShowDialog() {
		Activity activity = getCurrentActivity();
		if (activity == null)
			return null;
		while (activity.getParent() != null) {
			activity = activity.getParent();
		}
		return activity;
	}

}
