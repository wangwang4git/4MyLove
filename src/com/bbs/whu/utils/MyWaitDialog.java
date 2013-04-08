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
	public static int CREATED = 1;// 1Ϊ����
	public static int SHOWING = 2;// 2Ϊ��ʾ
	public static int CANCELLED = 3;// 3Ϊȡ��
	private Dialog mDialog;// �ȴ�����
	private ProgressDialog mProgressDialog;// �ȴ��Ի���
	private int mWaitMethod;// �ȴ���ʽ��1Ϊ�ȴ�������2Ϊ�ȴ��Ի���
	private String mTitle;// �ȴ��Ի���ı���
	private String mContent;// �ȴ��Ի��������
	private Context mContext;// ��ʾ�ȴ��Ի���͵ȴ�������Activity
	public int mStatus;// ��ǰ�ȴ��Ի��򣨶�������״̬��1Ϊ������2Ϊ��ʾ��3Ϊȡ��

	/**
	 * ���õȴ���ʽΪ�ȴ��Ի���
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
		// �����ȴ��Ի���ķ��ؼ�
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
	 * ���õȴ���ʽΪ�ȴ�����
	 */
	public MyWaitDialog(Context context) {
		mStatus = CREATED;
		mWaitMethod = 1;
		mContext = context;

		// �����ȴ������ķ��ؼ�
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
	 * ִ�������ҵ����ȴ��Ի���(����)
	 * 
	 * @param context
	 * @param progressDialog
	 */
	public void show() {
		if (mWaitMethod == 1) {
			mStatus = SHOWING;
			mDialog.show();
			// ע��˴�Ҫ����show֮�� ����ᱨ�쳣
			mDialog.setContentView(R.layout.loading_process_dialog_anim);
		} else if (mWaitMethod == 2) {
			mStatus = SHOWING;
			mProgressDialog.show();
		}
	}

	/**
	 * ȡ���ȴ�
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
	 *      ����onKeyDownʵ�ְ����ؼ�ȡ���ȴ�����
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