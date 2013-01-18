package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
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
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.adapter.FaceGridAdapter;
import com.bbs.whu.model.FaceBean;
import com.bbs.whu.utils.MyBBSFacesUtils;

/**
 * �ʼ�����
 * 
 * @author WWang
 * 
 */
public class MailSendActivity extends Activity {
	// �ռ���
	private EditText mAddressee;
	// ����
	private EditText mTitle;
	// �ʼ�����
	private EditText mContent;
	// �ʼ����Ͱ�ť
	private TextView mSendSubmit;
	
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

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		mAddressee = (EditText) findViewById(R.id.mail_send_addressee);
		// ��������򽹵���ͳһ������
		mAddressee.setOnFocusChangeListener(mailSendEditFocusListener);

		mTitle = (EditText) findViewById(R.id.mail_send_title);
		// ��������򽹵���ͳһ������
		mTitle.setOnFocusChangeListener(mailSendEditFocusListener);

		mContent = (EditText) findViewById(R.id.mail_send_content);
		// ��������򽹵���ͳһ������
		mContent.setOnFocusChangeListener(mailSendEditFocusListener);

		mSendSubmit = (TextView) findViewById(R.id.mail_send_submit);
		// ���ð�ťͳһ������
		mSendSubmit.setOnClickListener(mailSendClickListener);

		mBtnFace = (ImageButton) findViewById(R.id.mail_send_face);
		// ���ð�ťͳһ������
		mBtnFace.setOnClickListener(mailSendClickListener);
		mBtnFace.setEnabled(false);

		mBtnKeyboard = (ImageButton) findViewById(R.id.mail_send_keyboard);
		// ���ð�ťͳһ������
		mBtnKeyboard.setOnClickListener(mailSendClickListener);
		mBtnKeyboard.setEnabled(false);

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
	 * ��ť����ͳһ������
	 */
	private OnClickListener mailSendClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mail_send_submit:
				break;
			case R.id.mail_send_face:
				// ����ѡ������ʾ
				facesGridViewShow(true);
				break;
			case R.id.mail_send_keyboard:
				// ����ѡ��������
				facesGridViewShow(false);
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
			case R.id.mail_send_addressee:
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
}
