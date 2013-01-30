package com.bbs.whu.progresshud;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class ProgressHUDTask extends AsyncTask<Void, String, Void> implements
		OnCancelListener {
	private Context context;
	private ProgressHUD mProgressHUD;
	private boolean isFinish;

	public ProgressHUDTask(Context context) {
		super();
		this.context = context;
		this.isFinish = false;
	}

	@Override
	protected void onPreExecute() {
		mProgressHUD = ProgressHUD.show(context, "Loading...", true, true, this);
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... params) {
		while (!isFinish) {
			publishProgress("Loading...");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		mProgressHUD.setMessage(values[0]);
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		mProgressHUD.dismiss();
		super.onPostExecute(result);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		this.cancel(true);
		mProgressHUD.dismiss();
	}

	public void dismiss() {
		isFinish = true;
	}
}