package com.bbs.whu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.update.UpdateManager;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyFileUtils;
import com.bbs.whu.utils.MyFontManager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MoreActivity extends Activity implements OnClickListener {
	private ViewGroup mNewGuid = null;
	private ViewGroup mCleanCache = null;
	private ViewGroup mCheckUpdate = null;
	private ViewGroup mAbout = null;
	// 图片异步下载下载器
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		MyFontManager.changeFontType(this);//设置当前Activity的字体
		
		// 初始化控件
		initView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 只有捕获返回键，并返回false，才能在MainActivity中捕获返回键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.more_list_guide:
			// 跳转至新手导航页
			startActivity(new Intent(this, BeginnerNavigationActivity.class));
			break;
		case R.id.more_list_clean_cache:
			cleanCache();
			break;
		case R.id.more_list_about:
			// 跳转到“关于”页面
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.more_list_check_update:
			checkUpdate();
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 新手导航
		mNewGuid = (ViewGroup) findViewById(R.id.more_list_guide);
		mNewGuid.setOnClickListener(this);
		// 清除缓存
		mCleanCache = (ViewGroup) findViewById(R.id.more_list_clean_cache);
		mCleanCache.setOnClickListener(this);
		mCheckUpdate = (ViewGroup) findViewById(R.id.more_list_check_update);
		mCheckUpdate.setOnClickListener(this);
		// 关于
		mAbout = (ViewGroup) findViewById(R.id.more_list_about);
		mAbout.setOnClickListener(this);
	}

	private void cleanCache() {
		// 清空已读缓存
		MyApplication.getInstance().clearReadedTag();
		// 清空本地文件缓存
		// 删除/whubbs/data/cache/username/文件夹
		MyFileUtils.delFolder(MyFileUtils
				.getSdcardDataCacheDir(((MyApplication) getApplication())
						.getName()));
		// 清空图片下载器内存缓存
		imageLoader.clearMemoryCache();
		// 清空图片下载器文件缓存
		imageLoader.clearDiscCache();
		Toast.makeText(this, "清除缓存", Toast.LENGTH_SHORT).show();
	}

	private void checkUpdate() {
		UpdateManager updateManager = new UpdateManager(this);
		updateManager.checkUpdate();
	}
}
