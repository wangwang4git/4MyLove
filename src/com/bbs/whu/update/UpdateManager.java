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
 * 版本更新模块工具类
 * 
 * @author WWang
 * 
 */

public class UpdateManager {
	// 常量定义
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
				// 对话框通知用户升级程序
				showUpdataDialog();
				break;
			case NOT_UPDATA_CLIENT:
				// 客户端不需要更新
				Toast.makeText(mContext, "当前版本已是最新版本", Toast.LENGTH_SHORT)
						.show();
				break;
			case GET_UNDATAINFO_ERROR:
				// 服务器超时
				Toast.makeText(mContext, "获取服务器更新信息失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case DOWN_ERROR:
				// 下载apk失败
				Toast.makeText(mContext, "下载新版本失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public UpdateManager(Context context) {
		super();
		this.mContext = context;
	}

	// 外部接口让主Activity调用
	public void checkUpdate() {
		Thread thread = new Thread(new CheckVersionTask());
		thread.start();
	}

	/*
	 * 获取当前程序的版本号
	 */
	private int getVersionName() throws Exception {
		// 获取packagemanager的实例
		PackageManager packageManager = mContext.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = packageManager.getPackageInfo(
				mContext.getPackageName(), 0);
		return packInfo.versionCode;
	}

	/*
	 * 
	 * 弹出对话框通知用户更新程序
	 * 
	 * 弹出对话框的步骤： 1.创建alertDialog的builder. 2.要给builder设置属性, 对话框的内容,样式,按钮
	 * 3.通过builder 创建一个对话框 4.对话框show()出来
	 */
	private void showUpdataDialog() {
		AlertDialog.Builder builer = new Builder(mContext);
		builer.setTitle("版本升级");
		builer.setMessage(mInfo.getDescription());
		// 当点取消按钮时进行登录
		builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		// 当点确定按钮时从服务器上下载 新的apk 然后安装
		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				downLoadApk();
			}
		});
		AlertDialog dialog = builer.create();
		dialog.show();
	}

	/*
	 * 从服务器中下载APK
	 */
	private void downLoadApk() {
		final ProgressDialog pd; // 进度条对话框
		pd = new ProgressDialog(mContext);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("正在下载更新");
		pd.show();
		new Thread() {
			@Override
			public void run() {
				try {
					File file = DownLoadManager.getFileFromServer(
							mInfo.getUrl(), pd);
					sleep(500);
					installApk(file);
					pd.dismiss(); // 结束掉进度条对话框
				} catch (Exception e) {
					Message msg = new Message();
					msg.what = DOWN_ERROR;
					mHandler.sendMessage(msg);
					e.printStackTrace();
				}
			}
		}.start();
	}

	// 安装apk
	private void installApk(File file) {
		Intent intent = new Intent();
		// 执行动作
		intent.setAction(Intent.ACTION_VIEW);
		// 执行的数据类型
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}

	private class CheckVersionTask implements Runnable {
		public void run() {
			try {
				// 从资源文件获取服务器 地址
				String path = MyConstants.UPDATE_CLIENT_URL;
				// 包装成url的对象
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
				// 待处理
				Message msg = new Message();
				msg.what = GET_UNDATAINFO_ERROR;
				mHandler.sendMessage(msg);
				e.printStackTrace();
			}
		}
	}
}
