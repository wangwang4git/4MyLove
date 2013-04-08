package com.bbs.whu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFileUtils;
import com.bbs.whu.utils.MyFontManager;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MoreActivity extends Activity implements OnClickListener {
	private ViewGroup mNewGuid = null;
	private ViewGroup mCleanCache = null;
	private ViewGroup mCheckUpdate = null;
	private ViewGroup mAbout = null;
	private ViewGroup mLogout = null;
	// ͼƬ�첽����������
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		MyFontManager.changeFontType(this);// ���õ�ǰActivity������

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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.more_list_guide:
			// ��ת�����ֵ���ҳ
			startActivity(new Intent(this, BeginnerNavigationActivity.class));
			break;
		case R.id.more_list_clean_cache:
			cleanCache();
			break;
		case R.id.more_list_about:
			// ��ת�������ڡ�ҳ��
			startActivity(new Intent(this, AboutActivity.class));
			break;
		case R.id.more_list_check_update:
			checkUpdate();
			break;
		case R.id.more_list_logout:
			logout();
			break;
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// ���ֵ���
		mNewGuid = (ViewGroup) findViewById(R.id.more_list_guide);
		mNewGuid.setOnClickListener(this);
		// �������
		mCleanCache = (ViewGroup) findViewById(R.id.more_list_clean_cache);
		mCleanCache.setOnClickListener(this);
		mCheckUpdate = (ViewGroup) findViewById(R.id.more_list_check_update);
		mCheckUpdate.setOnClickListener(this);
		// ����
		mAbout = (ViewGroup) findViewById(R.id.more_list_about);
		mAbout.setOnClickListener(this);
		// ע��
		mLogout = (ViewGroup) findViewById(R.id.more_list_logout);
		mLogout.setOnClickListener(this);
		// �����û�û��ע������
		if (MyApplication.getInstance().getName().equals("4MyLove"))
			mLogout.setVisibility(View.GONE);
	}

	private void cleanCache() {
		// ����Ѷ�����
		MyApplication.getInstance().clearReadedTag();
		// ��ձ����ļ�����
		// ɾ��/whubbs/data/cache/username/�ļ���
		MyFileUtils.delFolder(MyFileUtils
				.getSdcardDataCacheDir(((MyApplication) getApplication())
						.getName()));
		// ���ͼƬ�������ڴ滺��
		imageLoader.clearMemoryCache();
		// ���ͼƬ�������ļ�����
		imageLoader.clearDiscCache();
		Toast.makeText(this, "�������", Toast.LENGTH_SHORT).show();
	}

	private void checkUpdate() {
		UpdateManager updateManager = new UpdateManager(this);
		updateManager.checkUpdate();
	}

	private void logout() {
		AlertDialog.Builder builder = new Builder(MoreActivity.this);
		builder.setMessage("ȷ��Ҫע����?");
		builder.setTitle("��ʾ");
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �˳���½
				MyBBSRequest.mGet(MyConstants.LOG_OUT_URL, "MainActivity");
				// ����Cookie
				MyApplication.getInstance().clearCookieStore();
				// �Ի����˳�
				dialog.dismiss();
				// ��ת����½����
				Intent mIntent = new Intent(MoreActivity.this,
						LoginActivity.class);
				startActivity(mIntent);
			}
		});
		builder.setNegativeButton("ȡ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// �Ի����˳�
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
}
