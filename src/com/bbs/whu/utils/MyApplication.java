package com.bbs.whu.utils;

import android.app.Application;

import com.bbs.whu.imageloader.ExtendedImageDownloader;
import com.loopj.android.http.PersistentCookieStore;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * ��������Ӧ�õ�ȫ�ֱ����洢��ȡ�Ĺ����࣬ȫ�ֱ���ΪCookie�洢��
 * 
 * @author double
 * 
 */
public class MyApplication extends Application {
	// ������λ�û�ȡApplication Context������������
	private static MyApplication instance;

	PersistentCookieStore myCookieStore;

	public PersistentCookieStore getCookieStore() {
		return myCookieStore;
	}

	public void setCookieStore(PersistentCookieStore pcs) {
		myCookieStore = pcs;
	}

	public void clearCookieStore() {
		myCookieStore.clear();
	}

	// �û���
	private String name = "4MyLove";
	// ����
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

	// ������λ�û�ȡApplication Context������������
	public static MyApplication getInstance() {
		return instance;
	}
}