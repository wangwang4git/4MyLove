package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.MailContentBean;
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyXMLParseUtils;

public class MailContentActivity extends Activity implements OnClickListener {
	// 信箱
	private int box;
	// 信件编号
	private int read;
	// 发件人
	private String sender;
	// 邮件标题
	private String title;

	// 邮件标题
	private TextView mTitle;
	// 邮件发件人
	private TextView mSender;
	// 邮件发件时间
	private TextView mTime;
	// 邮件内容
	private TextView mText;
	// 邮件回复按钮
	private Button mReplyButton;
	// 返回按钮
	private ImageView backButton;
	// 接收请求数据的handler
	private Handler mHandler;
	// 等待对话框
	private ProgressHUDTask mProgress;

	// 进行手势动作时候的坐标
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题栏显示
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mail_content);
		MyFontManager.changeFontType(this);// 设置当前Activity的字体

		// 获取传入的参数
		Intent mIntent = getIntent();
		box = mIntent.getIntExtra("boxname", -1);
		read = mIntent.getIntExtra("read", -1);

		// 初始化控件
		initView();

		// 初始化handler
		initHandler();

		// 获取信件数据
		getMail();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_SUCCESS, "MailContentActivity");
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_FAIL, "MailContentActivity");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mail_content_back_icon:
			onBackPressed();
			break;
		case R.id.mail_content_submit:
			// 跳转入发信界面
			Intent mIntent = new Intent(this, MailSendActivity.class);
			mIntent.putExtra("sender", sender);
			mIntent.putExtra("title", title);
			startActivity(mIntent);
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获得当前坐标
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x_temp1 = x;
			y_temp1 = y;
			break;

		case MotionEvent.ACTION_UP: {
			x_temp2 = x;
			y_temp2 = y;
			// 右滑
			if (x_temp1 != 0 && x_temp2 - x_temp1 > MyConstants.MIN_GAP_X
					&& Math.abs(y_temp2 - y_temp1) < MyConstants.MAX_GAP_Y) {
				onBackPressed();
			}
		}
			break;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		return onTouchEvent(event);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mTitle = (TextView) findViewById(R.id.mail_content_title);
		mSender = (TextView) findViewById(R.id.mail_content_sender);
		mTime = (TextView) findViewById(R.id.mail_content_time);
		mText = (TextView) findViewById(R.id.mail_content_text);
		mReplyButton = (Button) findViewById(R.id.mail_content_submit);
		mReplyButton.setOnClickListener(this);
		backButton = (ImageView) findViewById(R.id.mail_content_back_icon);
		backButton.setOnClickListener(this);
		mProgress = new ProgressHUDTask(this);
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 取消显示等待对话框
				if (mProgress != null) {
					mProgress.dismiss();
					mProgress = null;
				}

				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					// 返回结果
					String result = (String) msg.obj;
					// 解析数据
					MailContentBean mMailContentBean = MyXMLParseUtils
							.readXml2MailContentBean(result);
					sender = mMailContentBean.getSender();
					title = mMailContentBean.getTitle();
					// 显示信件内容
					mSender.setText(sender);
					mTitle.setText(title);
					mTime.setText(mMailContentBean.getTime());
					mText.setText(mMailContentBean.getText());
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "MailContentActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "MailContentActivity");
	}

	/**
	 * 获取信件数据
	 */
	private void getMail() {
		String boxName = null;
		switch (box) {
		case MailListActivity.MAIL_BOX_IN:
			boxName = "inbox";
			break;
		case MailListActivity.MAIL_BOX_SEND:
			boxName = "sendbox";
			break;
		case MailListActivity.MAIL_BOX_DELETE:
			boxName = "deleted";
			break;
		}
		// 添加get请求参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("mail");
		keys.add("boxname");
		values.add(boxName);
		keys.add("read");
		values.add(Integer.toString(read));
		// 请求数据
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"MailContentActivity", true, this);
		// 显示等待对话框
		mProgress.execute();
	}

}
