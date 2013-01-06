package com.bbs.whu.utils;

import android.app.Application;
import com.loopj.android.http.PersistentCookieStore;

/**
 * 进行整个应用的全局变量存储获取的工具类，全局变量为Cookie存储器
 * 
 * @author double
 * 
 */
public class MyApplication extends Application {
	PersistentCookieStore myCookieStore;

	public PersistentCookieStore getCookieStore() {
		return myCookieStore;
	}

	public void setCookieStore(PersistentCookieStore pcs) {
		myCookieStore = pcs;
	}
}