package com.bbs.whu.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.view.KeyEvent;

import com.bbs.whu.R;

public class MyWaitDialog extends Activity {

	private static final String TAG = "WaitDialog";// Print Tag in LogCat
	public static int CREATED = 1;// 1为创建
	public static int SHOWING = 2;// 2为显示
	public static int CANCELLED = 3;// 3为取消
	private Dialog mDialog;// 等待动画
	private ProgressDialog mProgressDialog;// 等待对话框
	private int mWaitMethod;// 等待方式：1为等待动画，2为等待对话框
	private String mTitle;// 等待对话框的标题
	private String mContent;// 等待对话框的内容
	private Context mContext;// 显示等待对话框和等待动画的Activity
	public int mStatus;// 当前等待对话框（动画）的状态：1为创建，2为显示，3为取消

	/**
	 * 设置等待方式为等待对话框
	 */
	public MyWaitDialog(Context context, String title, String content) {
		mStatus = CREATED;
		mWaitMethod = 2;
		mContext = context;
		mTitle = title;
		mContent = content;

		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(mTitle);
		mProgressDialog.setMessage(mContent);
		// 监听等待对话框的返回键
		mProgressDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Log.d(TAG, "ProgressDialog OnKeyDown called");
					if (mProgressDialog != null) {
						mProgressDialog.cancel();
						mStatus = CANCELLED;
						return true;
					}
				}
				return false;
			}
		});

	}

	/**
	 * 设置等待方式为等待动画
	 */
	public MyWaitDialog(Context context) {
		mStatus = CREATED;
		mWaitMethod = 1;
		mContext = context;

		// 监听等待动画的返回键
		OnKeyListener keyListener = new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Log.d(TAG, "mDialog OnKeyDown called");
					if (mDialog != null) {
						mDialog.cancel();
						mStatus = CANCELLED;
						return true;
					}
				}
				return false;
			}
		};
		mDialog = new AlertDialog.Builder(mContext).create();
		mDialog.setOnKeyListener(keyListener);
	}

	/**
	 * 执行任务并且弹出等待对话框(动画)
	 * 
	 * @param context
	 * @param progressDialog
	 */
	public void show() {
		if (mWaitMethod == 1) {
			mStatus = SHOWING;
			mDialog.show();
			// 注意此处要放在show之后 否则会报异常
			mDialog.setContentView(R.layout.loading_process_dialog_anim);
		} else if (mWaitMethod == 2) {
			mStatus = SHOWING;
			mProgressDialog.show();
		}
	}

	/**
	 * 取消等待
	 */
	public void cancel() {
		Log.d(TAG, "onCancelled called !");

		switch (mWaitMethod) {
		case 1:
			mDialog.dismiss();
			break;
		case 2:
			mProgressDialog.dismiss();
			break;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 *      重载onKeyDown实现按返回键取消等待功能
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("Login", "ProgressDialog OnKeyDown called");

			if (mProgressDialog != null && mWaitMethod == 2) {
				mProgressDialog.dismiss();
				mStatus = CANCELLED;
				return true;
			} else if (mDialog != null && mWaitMethod == 1) {
				mDialog.dismiss();
				mStatus = CANCELLED;
				return true;
			}
		}
		return false;
	}
}