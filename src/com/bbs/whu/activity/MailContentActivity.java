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
	// ����
	private int box;
	// �ż����
	private int read;
	// ������
	private String sender;
	// �ʼ�����
	private String title;

	// �ʼ�����
	private TextView mTitle;
	// �ʼ�������
	private TextView mSender;
	// �ʼ�����ʱ��
	private TextView mTime;
	// �ʼ�����
	private TextView mText;
	// �ʼ��ظ���ť
	private Button mReplyButton;
	// ���ذ�ť
	private ImageView backButton;
	// �����������ݵ�handler
	private Handler mHandler;
	// �ȴ��Ի���
	private ProgressHUDTask mProgress;

	// �������ƶ���ʱ�������
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ����������ʾ
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mail_content);
		MyFontManager.changeFontType(this);// ���õ�ǰActivity������

		// ��ȡ����Ĳ���
		Intent mIntent = getIntent();
		box = mIntent.getIntExtra("boxname", -1);
		read = mIntent.getIntExtra("read", -1);

		// ��ʼ���ؼ�
		initView();

		// ��ʼ��handler
		initHandler();

		// ��ȡ�ż�����
		getMail();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// ע��handler
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
			// ��ת�뷢�Ž���
			Intent mIntent = new Intent(this, MailSendActivity.class);
			mIntent.putExtra("sender", sender);
			mIntent.putExtra("title", title);
			startActivity(mIntent);
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ��õ�ǰ����
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
			// �һ�
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
	 * ��ʼ���ؼ�
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
	 * ��ʼ��handler
	 */
	private void initHandler() {
		// ��ʼ��handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// ȡ����ʾ�ȴ��Ի���
				if (mProgress != null) {
					mProgress.dismiss();
					mProgress = null;
				}

				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					// ���ؽ��
					String result = (String) msg.obj;
					// ��������
					MailContentBean mMailContentBean = MyXMLParseUtils
							.readXml2MailContentBean(result);
					sender = mMailContentBean.getSender();
					title = mMailContentBean.getTitle();
					// ��ʾ�ż�����
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
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "MailContentActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "MailContentActivity");
	}

	/**
	 * ��ȡ�ż�����
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
		// ���get�������
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("mail");
		keys.add("boxname");
		values.add(boxName);
		keys.add("read");
		values.add(Integer.toString(read));
		// ��������
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"MailContentActivity", true, this);
		// ��ʾ�ȴ��Ի���
		mProgress.execute();
	}

}
