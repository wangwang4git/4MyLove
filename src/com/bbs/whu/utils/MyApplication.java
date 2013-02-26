package com.bbs.whu.utils;

import java.util.HashMap;

import android.app.Application;

import com.bbs.whu.imageloader.ExtendedImageDownloader;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 进行整个应用的全局变量存储获取的工具类，全局变量为Cookie存储器
 * 
 * @author double
 * 
 */
public class MyApplication extends Application {
	// 在任意位置获取Application Context，包括工具类
	private static MyApplication instance;

	PersistentCookieStore myCookieStore;
	
	public PersistentCookieStore getCookieStore() {
		return myCookieStore;
	}

	public void setCookieStore(PersistentCookieStore pcs) {
		myCookieStore = pcs;
	}

	public void clearCookieStore() {
		if (myCookieStore != null) {
			myCookieStore.clear();
		}
	}

	// 用户名
	private String name = "4MyLove";
	// 密码
	private String password = "4MyLove";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 已读标记HashMap
	private HashMap<String, Byte> readedTagMap;

	public HashMap<String, Byte> getReadedTag() {
		return readedTagMap;
	}

	public void setReadedTag(HashMap<String, Byte> readedTagMap) {
		this.readedTagMap = readedTagMap;
	}
	
	public void clearReadedTag() {
		if (this.readedTagMap != null) {
			this.readedTagMap.clear();
		}
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(2 * 1024 * 1024)
				// 2 Mb
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.imageDownloader(
						new ExtendedImageDownloader(getApplicationContext()))
				.tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging() // Not
																				// necessary
																				// in
																				// common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	// 在任意位置获取Application Context，包括工具类
	public static MyApplication getInstance() {
		return instance;
	}
}