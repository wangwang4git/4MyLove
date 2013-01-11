package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;

public class BulletinReplyActivity extends Activity implements OnClickListener {
	private String originBoard;// ԭ�����
	private String originId;// ԭ��id��
	private String originTitle;// ԭ������
	private String originAuthor;// ԭ������
	private String originContent;// ԭ������

	// �ظ�����
	TextView replyTitle;
	// �ظ�����
	TextView replyContent;
	// ����ť
	Button publishButton;
	// �����������ݵ�handler
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bulletin_reply);

		// ��ȡ�����������ݣ����ص�������
		Intent postInfoIntent = getIntent();
		originBoard = postInfoIntent.getStringExtra("board");// ���
		originId = postInfoIntent.getStringExtra("id");// id��
		originTitle = postInfoIntent.getStringExtra("title");// ����
		originAuthor = postInfoIntent.getStringExtra("author");// ����
		originContent = postInfoIntent.getStringExtra("content");// ����

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��handler
		initHandler();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bulletin_reply_publish_button:
			// �ظ�����
			replyPost();
			break;
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// ��ʼ�����Ⲣ���ó�ʼֵ
		replyTitle = (TextView) findViewById(R.id.bulletin_reply_title);
		replyTitle.setText(titleFormat(originTitle));
		// ��ʼ�����ݲ����ó�ʼֵ
		replyContent = (TextView) findViewById(R.id.bulletin_reply_content);
		replyContent.setText(contentFormat(originAuthor, originContent));
		// ��ʼ����������ť
		publishButton = (Button) findViewById(R.id.bulletin_reply_publish_button);
		publishButton.setOnClickListener(this);
	}

	/**
	 * ��ʼ��handler
	 */
	private void initHandler() {
		// ��ʼ��handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					// ���۳ɹ�
					Toast.makeText(BulletinReplyActivity.this, "���۳ɹ���",
							Toast.LENGTH_SHORT).show();
					// �˳���Activity
					finish();
					break;
				case MyConstants.REQUEST_FAIL:
					// ����ʧ��
					Toast.makeText(BulletinReplyActivity.this, "����ʧ�ܣ�",
							Toast.LENGTH_SHORT).show();
					// �˳���Activity
					finish();
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "BulletinReplyActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "BulletinReplyActivity");
	}

	/**
	 * �ظ����ӵı����ʽ
	 * 
	 * @param originTitle
	 *            ԭ������
	 * @return
	 */
	private String titleFormat(String originTitle) {
		return "res:" + originTitle;
	}

	/**
	 * �ظ����ӵ����ݸ�ʽ:
	 * �� �� xioumu �Ĵ������ᵽ: �� : PPT �� PPTX�ļ����ϴ���������
	 * 
	 * @param originAuthor
	 *            ԭ������
	 * @param originContent
	 *            ԭ������
	 * @return
	 */
	private String contentFormat(String originAuthor, String originContent) {
		// ����
		return "\n����" + originAuthor + "�Ĵ������ᵽ: �� : " + originContent;
	}

	/**
	 * ��������
	 */
	private void replyPost() {
		// ���post�������
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("board");
		values.add(originBoard);
		keys.add("reID");
		values.add(originId);
		keys.add("font");
		values.add("");
		keys.add("subject");
		values.add(replyTitle.getText().toString());
		keys.add("Content");
		values.add(replyContent.getText().toString());
		keys.add("signature");
		// if (msignature.equals("--"))
		values.add("0");
		// else
		// values.add(msignature);

		// post����
		MyBBSRequest.mPost(MyConstants.POST_BULLETIN_REPLY_URL, keys, values,
				"BulletinReplyActivity", this);

	}
}
