package com.bbs.whu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MoreActivity extends Activity implements OnClickListener {
	private ViewGroup mCleanCache = null;
	private ViewGroup mAbout = null;

	// 图片异步下载下载器
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
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
		case R.id.more_list_clean_cache:
			// 清空本地文件缓存
			// 删除/whubbs/data/cache/username/文件夹
			MyFileUtils.delFolder(MyFileUtils
					.getSdcardDataCacheDir(((MyApplication) getApplication())
							.getName()));
			// 清空图片下载器内存缓存
			imageLoader.clearMemoryCache();
			// 清空图片下载器文件缓存
			imageLoader.clearDiscCache();
			Toast.makeText(
					getApplicationContext(),
					((TextView) ((ViewGroup) view)
							.findViewById(R.id.more_list_clean_cache_textview))
							.getText().toString(), Toast.LENGTH_SHORT).show();
			break;
		case R.id.more_list_about:
			// 跳转到“关于”页面
			startActivity(new Intent(this, AboutActivity.class));
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 清除缓存
		mCleanCache = (ViewGroup) findViewById(R.id.more_list_clean_cache);
		mCleanCache.setOnClickListener(this);

		// 关于
		mAbout = (ViewGroup) findViewById(R.id.more_list_about);
		mAbout.setOnClickListener(this);
	}
}
