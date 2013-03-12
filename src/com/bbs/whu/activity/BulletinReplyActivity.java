package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;
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

public class BulletinReplyActivity extends Activity {
	private int head;// �����ӻظ������ǡ����ӷ���
	private String originBoard;// ԭ�����
	private String originId;// ԭ��id��
	private String originTitle;// ԭ������
	private String originAuthor;// ԭ������
	private String originContent;// ԭ������

	// �������
	TextView headTextView;
	// �ظ�����
	EditText replyTitle;
	// �ظ�����
	EditText replyContent;
	// ����ť
	Button publishButton;
	// �����������ݵ�handler
	Handler mHandler;

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
	
	// �ȴ��Ի���
	private ProgressHUDTask mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ����������ʾ
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bulletin_reply);
		MyFontManager.changeFontType(this);//���õ�ǰActivity������
		
		// ��ȡ�����������ݣ����ص�������
		Intent postInfoIntent = getIntent();
		head = postInfoIntent.getIntExtra("head", -1);
		originBoard = postInfoIntent.getStringExtra("board");// ���
		originId = postInfoIntent.getStringExtra("id");// id��
		originTitle = postInfoIntent.getStringExtra("title");// ����
		originAuthor = postInfoIntent.getStringExtra("author");// ����
		originContent = postInfoIntent.getStringExtra("content");// ����

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// ���ӽ������
		headTextView = (TextView) findViewById(R.id.bulletin_reply_head);
		if (head == MyConstants.NEW_BULLETIN)
			headTextView.setText("��������");
		else if (head == MyConstants.BULLETIN_REPLY)
			headTextView.setText("���ӻظ�");

		// ��ʼ�����Ⲣ���ó�ʼֵ
		replyTitle = (EditText) findViewById(R.id.bulletin_reply_title);
		replyTitle.setText(titleFormat(originTitle));
		// ��������򽹵���ͳһ������
		replyTitle.setOnFocusChangeListener(bulletinReplyEditFocusListener);

		// ��ʼ�����ݲ����ó�ʼֵ
		replyContent = (EditText) findViewById(R.id.bulletin_reply_content);

		// ��ִ����ӵ��ı�ʹ��ָ����ɫ
		SpannableStringBuilder builder = new SpannableStringBuilder();
		builder.append(contentFormat(originAuthor, originContent));
		builder.setSpan(new ForegroundColorSpan(Color.BLUE), 0,
				contentFormat(originAuthor, originContent).length(),
				Spanned.SPAN_COMPOSING);
		replyContent.setText(builder, BufferType.EDITABLE);
		// ��������򽹵���ͳһ������
		replyContent.setOnFocusChangeListener(bulletinReplyEditFocusListener);

		// ��ʼ����������ť
		publishButton = (Button) findViewById(R.id.bulletin_reply_publish_button);
		// ���ð�ťͳһ������
		publishButton.setOnClickListener(bulletinReplyClickListener);

		mBtnFace = (ImageButton) findViewById(R.id.bulletin_reply_bottom_face);
		// ���ð�ťͳһ������
		mBtnFace.setOnClickListener(bulletinReplyClickListener);
		mBtnFace.setEnabled(false);

		mBtnKeyboard = (ImageButton) findViewById(R.id.bulletin_reply_bottom_keyboard);
		// ���ð�ťͳһ������
		mBtnKeyboard.setOnClickListener(bulletinReplyClickListener);
		mBtnKeyboard.setEnabled(false);

		mBBSFacesGridView = (GridView) findViewById(R.id.bulletin_reply_grid_face);
		// ���ü�����
		mBBSFacesGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// �����������
				insertText(replyContent, BBSFacesList.get(arg2).getName());
			}
		});
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
				String toastString;
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					if (head == MyConstants.NEW_BULLETIN)
						toastString = "����ɹ���";
					else
						toastString = "���۳ɹ���";
					Toast.makeText(BulletinReplyActivity.this, toastString,
							Toast.LENGTH_SHORT).show();
					// �˳���Activity
					finish();
					break;
				case MyConstants.REQUEST_FAIL:
					if (head == MyConstants.NEW_BULLETIN)
						toastString = "����ʧ�ܣ�";
					else
						toastString = "����ʧ�ܣ�";
					Toast.makeText(BulletinReplyActivity.this, toastString,
							Toast.LENGTH_SHORT).show();
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
		// ��������
		if (head == MyConstants.NEW_BULLETIN)
			return "";
		// ��ʽ��ԭ������
		if (!originTitle.startsWith("Re: ")) {
			originTitle = "Re: " + originTitle;
		}
		return originTitle;
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
		// ��������
		if (head == MyConstants.NEW_BULLETIN)
			return "";
		// ��ʽ��ԭ������
		// ���ԭ������̫��������ɾ��
		String content = originContent.length() > 20 ? originContent.substring(
				0, 20) + "......" : originContent;
		content = content.replaceAll("\n\n", "\n");
		content = content.replaceAll("\n", "\n: ");
		content += "\n:  ����Android�ͻ���.........";
		// ����
		return "\n�� ��" + originAuthor + "�Ĵ������ᵽ�� ��\n: " + content;
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
		// ��ʾ�ȴ��Ի���
		if (null == mProgress) {
			mProgress = new ProgressHUDTask(this);
			mProgress.execute();
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
	private OnClickListener bulletinReplyClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.bulletin_reply_publish_button:
				// �ظ�����
				replyPost();
				break;
			case R.id.bulletin_reply_bottom_face:
				// ����ѡ������ʾ
				facesGridViewShow(true);
				break;
			case R.id.bulletin_reply_bottom_keyboard:
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
	private OnFocusChangeListener bulletinReplyEditFocusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bulletin_reply_title:
				// ���������г�����EditText�����ñ����л���ť�������л���ť���ڲ��ܵ��״̬
				mBtnFace.setEnabled(false);
				mBtnKeyboard.setEnabled(false);
				break;
			case R.id.bulletin_reply_content:
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
			imm.hideSoftInputFromWindow(replyContent.getWindowToken(), 0);

			mBtnFace.setVisibility(View.GONE);
			mBtnKeyboard.setVisibility(View.VISIBLE);
			mBBSFacesGridView.setVisibility(View.VISIBLE);
		} else {
			// ��ʾ�����
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.showSoftInput(replyContent, 0);

			mBtnFace.setVisibility(View.VISIBLE);
			mBtnKeyboard.setVisibility(View.GONE);
			mBBSFacesGridView.setVisibility(View.GONE);
		}
	}

	/**
	 * ��ȡEditText������ڵ�λ��
	 */
	private int getEditTextCursorIndex(EditText mEditText) {
		return mEditText.getSelectionStart();
	}

	/**
	 * ��EditTextָ�����λ�ò����ַ���
	 */
	private void insertText(EditText mEditText, String mText) {
		mEditText.getText().insert(getEditTextCursorIndex(mEditText), mText);
	}
}
