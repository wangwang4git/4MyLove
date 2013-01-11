package com.bbs.whu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.utils.MyFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MoreActivity extends Activity {
	private ViewGroup mCleanCache = null;

	// 图片异步下载下载器
	private ImageLoader imageLoader = ImageLoader.getInstance();

	// 清空内存缓存调用方法
	// imageLoader.clearMemoryCache();
	// 清空文件缓存调用方法
	// imageLoader.clearDiscCache();

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

	/**
	 * 初始化控件
	 */
	private void initView() {
		mCleanCache = (ViewGroup) findViewById(R.id.more_list_clean_cache);
		mCleanCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 清空本地文件缓存
				// 删除/whubbs/data/cache根文件夹/whubbs，需要删除返回路径中的/data/cache
				MyFileUtils.delFolder(MyFileUtils.getSdcardDataCacheDir().replace("/data/cache", ""));
				// 清空图片下载器内存缓存
				imageLoader.clearMemoryCache();
				// 清空图片下载器文件缓存
				imageLoader.clearDiscCache();
				Toast.makeText(
						getApplicationContext(),
						"您点击的是："
								+ ((TextView) ((ViewGroup) v)
										.findViewById(R.id.more_list_clean_cache_textview))
										.getText().toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
