package com.bbs.whu.utils;

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
	PersistentCookieStore myCookieStore;

	public PersistentCookieStore getCookieStore() {
		return myCookieStore;
	}

	public void setCookieStore(PersistentCookieStore pcs) {
		myCookieStore = pcs;
	}

	// 用户名
	private String name;
	// 密码
	private String password;

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

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

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
}