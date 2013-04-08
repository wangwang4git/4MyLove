package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.FaceGridAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.FaceBean;
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyBBSFacesUtils;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;

/**
 * �ʼ�����
 * 
 * @author WWang
 * 
 */
public class MailSendActivity extends Activity implements OnClickListener,
		OnFocusChangeListener {
	// ������
	private String sender;
	// �ʼ�����
	private String title;

	// �ʼ��ռ���
	private EditText mAddress;
	// �ʼ�����
	private EditText mTitle;
	// �ʼ�����
	private EditText mContent;
	// �ʼ����Ͱ�ť
	private TextView mSendSubmit;
	// ���ذ�ť
	private ImageView backButton;
	// �����������ݵ�handler
	private Handler mHandler;
	// �ȴ��Ի���
	private ProgressHUDTask mProgress;

	// ����ѡ�����л���ť
	private ImageButton mBtnFace;
	// ������л���ť
	private ImageButton mBtnKeyboard;
	// ����ѡ����
	private GridView mBBSFacesGridView;
	// ����ѡ����������
	private FaceGridAdapter mAdapter;
	// BBS�������ơ�������ԴIDӳ�伯��
	private ArrayList<FaceBean> BBSFacesList = new ArrayList<FaceBean>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ����������ʾ
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mail_send);
		MyFontManager.changeFontType(this);// ���õ�ǰActivity������

		// ��ȡ����Ĳ���
		Intent mIntent = getIntent();
		sender = mIntent.getStringExtra("sender");
		title = mIntent.getStringExtra("title");

		// ��ʼ���ؼ�
		initView();

		// ��ʼ��handler
		initHandler();

		// ��ʼ��������
		initAdapter();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// ע��handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_SUCCESS, "MailSendActivity");
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_FAIL, "MailSendActivity");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mail_send_submit:
			// ����
			sendNewMail();
			break;
		case R.id.mail_send_face:
			// ����ѡ������ʾ
			facesGridViewShow(true);
			break;
		case R.id.mail_send_keyboard:
			// ����ѡ��������
			facesGridViewShow(false);
			break;
		case R.id.mail_send_back_icon:
			onBackPressed();
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.mail_send_address:
			// ���������г��ռ���EditText�����ñ����л���ť�������л���ť���ڲ��ܵ��״̬
			mBtnFace.setEnabled(false);
			mBtnKeyboard.setEnabled(false);
			break;
		case R.id.mail_send_title:
			// ���������г�����EditText�����ñ����л���ť�������л���ť���ڲ��ܵ��״̬
			mBtnFace.setEnabled(false);
			mBtnKeyboard.setEnabled(false);
			break;
		case R.id.mail_send_content:
			if (hasFocus) {
				// ������������EditText���ñ����л���ť�������л���ť���ڿ��Ե��״̬
				mBtnFace.setEnabled(true);
				mBtnKeyboard.setEnabled(true);
			} else {
				// ȡ������ѡ������ʾ
				facesGridViewShow(false);
				// �����г�����EditText���ñ����л���ť�������л���ť���ڲ��ܵ��״̬
				mBtnFace.setEnabled(false);
				mBtnKeyboard.setEnabled(false);
			}
			break;
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// �ʼ��ռ���
		mAddress = (EditText) findViewById(R.id.mail_send_address);
		mAddress.setText(sender);
		mAddress.setOnFocusChangeListener(this);
		// �ʼ�����
		mTitle = (EditText) findViewById(R.id.mail_send_title);
		if (title != null)
			mTitle.setText("Re��" + title);
		mTitle.setOnFocusChangeListener(this);
		// �ʼ�����
		mContent = (EditText) findViewById(R.id.mail_send_content);
		mContent.setOnFocusChangeListener(this);
		// ���Ͱ�ť
		mSendSubmit = (TextView) findViewById(R.id.mail_send_submit);
		mSendSubmit.setOnClickListener(this);
		// ���鰴ť
		mBtnFace = (ImageButton) findViewById(R.id.mail_send_face);
		mBtnFace.setOnClickListener(this);
		mBtnFace.setEnabled(false);
		// ���̰�ť
		mBtnKeyboard = (ImageButton) findViewById(R.id.mail_send_keyboard);
		mBtnKeyboard.setOnClickListener(this);
		mBtnKeyboard.setEnabled(false);
		// ���ذ�ť
		backButton = (ImageView) findViewById(R.id.mail_send_back_icon);
		backButton.setOnClickListener(this);

		mBBSFacesGridView = (GridView) findViewById(R.id.mail_send_grid_face);
		// ���ü�����
		mBBSFacesGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// �����������
				mContent.append(BBSFacesList.get(arg2).getName());
			}
		});

	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ��ȡBBS����ӳ�伯��
		BBSFacesList = MyBBSFacesUtils.getBBSFacesList(this);
		// ����������
		mAdapter = new FaceGridAdapter(this, BBSFacesList, R.layout.face_item);
		// ����������
		mBBSFacesGridView.setAdapter(mAdapter);
	}

	/**
	 * ����ѡ������ʾ�������л�
	 * 
	 * @param bool
	 *            true����ʾ��false������
	 */
	private void facesGridViewShow(Boolean bool) {
		if (true == bool) {
			// ���������
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mContent.getWindowToken(), 0);

			mBtnFace.setVisibility(View.GONE);
			mBtnKeyboard.setVisibility(View.VISIBLE);
			mBBSFacesGridView.setVisibility(View.VISIBLE);
		} else {
			// ��ʾ�����
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.showSoftInput(mContent, 0);

			mBtnFace.setVisibility(View.VISIBLE);
			mBtnKeyboard.setVisibility(View.GONE);
			mBBSFacesGridView.setVisibility(View.GONE);
		}
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
					// ����ʧ��
					if (result.contains("fail")) {
						Toast.makeText(MailSendActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();
					} else // ���ųɹ�
					{
						Toast.makeText(MailSendActivity.this, "���ųɹ�",
								Toast.LENGTH_SHORT).show();
						// �˳���Activity
						Intent mIntent = new Intent();
						setResult(RESULT_OK, mIntent);
						finish();
					}
					break;
				case MyConstants.REQUEST_FAIL:
					Toast.makeText(MailSendActivity.this, "����ʧ��",
							Toast.LENGTH_SHORT).show();
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "MailSendActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "MailSendActivity");
	}

	/**
	 * ����
	 */
	private void sendNewMail() {
		// ���post�������
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		// destid�ռ���id , title�ż�����, content�ż�����,
		// signatureʹ�õڼ���ǩ��, [backup]�Ƿ񱣴浽������
		keys.add("destid");
		values.add(mAddress.getText().toString());
		keys.add("title");
		values.add(mTitle.getText().toString());
		keys.add("content");
		values.add(mContent.getText().toString());
		keys.add("signature");
		values.add("0");
		keys.add("backup");
		values.add("on");

		// post����
		MyBBSRequest.mPost(MyConstants.POST_NEW_MAIL_URL, keys, values,
				"MailSendActivity", this);
		// ��ʾ�ȴ��Ի���
		if (null == mProgress) {
			mProgress = new ProgressHUDTask(this);
			mProgress.execute();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mBBSFacesGridView.isShown()) {
			// ���񷵻ؼ�
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				mBBSFacesGridView.setVisibility(View.GONE);
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}
