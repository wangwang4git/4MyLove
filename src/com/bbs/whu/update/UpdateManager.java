package com.bbs.whu.update;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.bbs.whu.utils.MyConstants;

/**
 * �汾����ģ�鹤����
 * 
 * @author WWang
 * 
 */

public class UpdateManager {
	// ��������
	final static int UPDATA_CLIENT = 8000;
	final static int NOT_UPDATA_CLIENT = 8001;
	final static int GET_UNDATAINFO_ERROR = 8002;
	final static int DOWN_ERROR = 8003;

	private UpdataInfo mInfo;
	private Context mContext;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case UPDATA_CLIENT:
				// �Ի���֪ͨ�û���������
				showUpdataDialog();
				break;
			case NOT_UPDATA_CLIENT:
				// �ͻ��˲���Ҫ����
				Toast.makeText(mContext, "��ǰ�汾�������°汾", Toast.LENGTH_SHORT)
						.show();
				break;
			case GET_UNDATAINFO_ERROR:
				// ��������ʱ
				Toast.makeText(mContext, "��ȡ������������Ϣʧ��", Toast.LENGTH_SHORT)
						.show();
				break;
			case DOWN_ERROR:
				// ����apkʧ��
				Toast.makeText(mContext, "�����°汾ʧ��", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public UpdateManager(Context context) {
		super();
		this.mContext = context;
	}

	// �ⲿ�ӿ�����Activity����
	public void checkUpdate() {
		Thread thread = new Thread(new CheckVersionTask());
		thread.start();
	}

	/*
	 * ��ȡ��ǰ����İ汾��
	 */
	private int getVersionName() throws Exception {
		// ��ȡpackagemanager��ʵ��
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()���㵱ǰ��İ�����0�����ǻ�ȡ�汾��Ϣ
		PackageInfo packInfo = packageManager.getPackageInfo(
				mContext.getPackageName(), 0);
		return packInfo.versionCode;
	}

	/*
	 * 
	 * �����Ի���֪ͨ�û����³���
	 * 
	 * �����Ի���Ĳ��裺 1.����alertDialog��builder. 2.Ҫ��builder��������, �Ի��������,��ʽ,��ť
	 * 3.ͨ��builder ����һ���Ի��� 4.�Ի���show()����
	 */
	private void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(mContext);
		builer.setTitle("�汾����");
		builer.setMessage(mInfo.getDescription());
		// ����ȡ����ťʱ���е�¼
		builer.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		// ����ȷ����ťʱ�ӷ����������� �µ�apk Ȼ��װ
		builer.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downLoadApk();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	/*
	 * �ӷ�����������APK
	 */
	private void downLoadApk() {
		final ProgressDialog pd; // �������Ի���
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("�������ظ���");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(
							mInfo.getUrl(), pd);
					sleep(500);
					installApk(file);
					pd.dismiss(); // �������������Ի���
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					mHandler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	// ��װapk
	private void installApk(File file) {
		Intent intent = new Intent();
		// ִ�ж���
		intent.setAction(Intent.ACTION_VIEW);
		// ִ�е���������
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	private class CheckVersionTask implements Runnable {
		public void run() {
			try {
				// ����Դ�ļ���ȡ������ ��ַ
				String path = MyConstants.UPDATE_CLIENT_URL;
				// ��װ��url�Ķ���
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(5000);
				InputStream is = conn.getInputStream();
				mInfo = UpdataInfoParser.getUpdataInfo(is);
				if (mInfo.getVersion() == getVersionName()) {
					Message msg = new Message();
					msg.what = NOT_UPDATA_CLIENT;
					mHandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = UPDATA_CLIENT;
					mHandler.sendMessage(msg);
				}
			} catch (Exception e) {
				// ������
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				mHandler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}
}
