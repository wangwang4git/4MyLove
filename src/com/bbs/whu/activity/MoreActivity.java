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
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MoreActivity extends Activity {
	private ViewGroup mCleanCache = null;

	// ͼƬ�첽����������
	private ImageLoader imageLoader = ImageLoader.getInstance();

	// ����ڴ滺����÷���
	// imageLoader.clearMemoryCache();
	// ����ļ�������÷���
	// imageLoader.clearDiscCache();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		// ��ʼ���ؼ�
		initView();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ֻ�в��񷵻ؼ���������false��������MainActivity�в��񷵻ؼ�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return false;
		}
		return false;
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		mCleanCache = (ViewGroup) findViewById(R.id.more_list_clean_cache);
		mCleanCache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ��ձ����ļ�����
				// ɾ��/whubbs/data/cache/username/�ļ���
				MyFileUtils.delFolder(MyFileUtils
						.getSdcardDataCacheDir(((MyApplication) getApplication())
								.getName()));
				// ���ͼƬ�������ڴ滺��
				imageLoader.clearMemoryCache();
				// ���ͼƬ�������ļ�����
				imageLoader.clearDiscCache();
				Toast.makeText(
						getApplicationContext(),
						"��������ǣ�"
								+ ((TextView) ((ViewGroup) v)
										.findViewById(R.id.more_list_clean_cache_textview))
										.getText().toString(),
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
