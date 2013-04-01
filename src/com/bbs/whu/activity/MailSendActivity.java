package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class MailSendActivity extends Activity {
	// ���Ż��ǿ���
	private int head;
	// ����
	private int boxName;
	// �ż����
	private int read;

	// ����
	private TextView mActivityTitle;
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
		head = mIntent.getIntExtra("head", -1);

		// ��ʼ���ؼ�
		initView();

		// ��ʼ��������
		if (head == MyConstants.NEW_MAIL) {
			initAdapter();
		}
		// ��ʼ��handler
		initHandler();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		mActivityTitle = (TextView) findViewById(R.id.mail_send_activity_title);	// �������
		mAddress = (EditText) findViewById(R.id.mail_send_address);	// �ռ���
		mTitle = (EditText) findViewById(R.id.mail_send_title);				// �ʼ�����
		mContent = (EditText) findViewById(R.id.mail_send_content);	// �ʼ�����
		// ���Ͱ�ť
		mSendSubmit = (TextView) findViewById(R.id.mail_send_submit);
		// ���ð�ťͳһ������
		mSendSubmit.setOnClickListener(mailSendClickListener);
		// ���鰴ť
		mBtnFace = (ImageButton) findViewById(R.id.mail_send_face);
		// ���̰�ť
		mBtnKeyboard = (ImageButton) findViewById(R.id.mail_send_keyboard);
		// ���ذ�ť
		backButton = (ImageView) findViewById(R.id.mail_send_back_icon);
		backButton.setOnClickListener(mailSendClickListener);

		if (head == MyConstants.NEW_MAIL) {
			mActivityTitle.setText("�����ż�");
			mSendSubmit.setText("����");
			// ��������򽹵���ͳһ������
			mAddress.setOnFocusChangeListener(mailSendEditFocusListener);
			// ��������򽹵���ͳһ������
			mTitle.setOnFocusChangeListener(mailSendEditFocusListener);
			// ��������򽹵���ͳһ������
			mContent.setOnFocusChangeListener(mailSendEditFocusListener);
			// ���ð�ťͳһ������
			mBtnFace.setOnClickListener(mailSendClickListener);
			mBtnFace.setEnabled(false);
			// ���ð�ťͳһ������
			mBtnKeyboard.setOnClickListener(mailSendClickListener);
			mBtnKeyboard.setEnabled(false);

			mBBSFacesGridView = (GridView) findViewById(R.id.mail_send_grid_face);
			// ���ü�����
			mBBSFacesGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// �����������
					mContent.append(BBSFacesList.get(arg2).getName());
				}
			});
		} else if (head == MyConstants.READ_MAIL) {
			mActivityTitle.setText("�ż�");
			mSendSubmit.setText("�ظ�");
			mAddress.setEnabled(false);
			mTitle.setEnabled(false);
			mContent.setEnabled(false);
			mBtnFace.setVisibility(View.GONE);
			mBtnKeyboard.setVisibility(View.GONE);
		}

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
	 * ��ť����ͳһ������
	 */
	private OnClickListener mailSendClickListener = new OnClickListener() {
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
			default:
				break;
			}
		}
	};

	/**
	 * ����򽹵���ͳһ������
	 */
	private OnFocusChangeListener mailSendEditFocusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
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
			default:
				break;
			}
		}
	};

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
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
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
}
