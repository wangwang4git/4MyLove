package com.bbs.whu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.MailAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.MailBean;
import com.bbs.whu.model.mail.Mails;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

public class MailListActivity extends Activity implements IXListViewListener,
		OnClickListener {
	// ����
	TextView title;
	// �ʼ��б�
	private XListView mListView;
	// �ʼ��б�����������
	private MailAdapter mAdapter;
	// �ʼ��б�����
	private ArrayList<MailBean> items = new ArrayList<MailBean>();
	// д�Ű�ť
	private Button newMailButton;
	// �����������ݵ�handler
	Handler mHandler;
	// ���ذ�ť
	private ImageView backButton;
	// ˢ�°�ť
	private Button refreshButton;
	// ˢ�¶�̬ͼ
	private ImageView refreshImageView;
	// ˢ�¶���
	private AnimationDrawable refreshAnimationDrawable;

	// ��ǰҳ��
	private int currentList = 1;
	// ��ҳ��
	private int totalList;

	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// ��������
	private int mailBoxType = MAIL_BOX_IN;

	public static final int MAIL_BOX_IN = 1;
	public static final int MAIL_BOX_SEND = 2;
	public static final int MAIL_BOX_DELETE = 3;

	/*����activity��������*/
	public static final int REQUEST_CODE_NEW_MAIL = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// �ޱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mail_list);
		MyFontManager.changeFontType(this);// ���õ�ǰActivity������

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// �����ʼ��б�����
		getMail();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// ע��handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_SUCCESS, "MailListActivity");
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_FAIL, "MailListActivity");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mail_list_new_mail_button:
			// ����д�Ž���
			Intent mIntent = new Intent(this, MailSendActivity.class);
			mIntent.putExtra("head", MyConstants.NEW_MAIL);
			startActivityForResult(mIntent, REQUEST_CODE_NEW_MAIL);
			break;
		case R.id.mail_list_back_icon:
			// �˳�
			onBackPressed();
			break;
		case R.id.mail_list_refresh_button:
			// ˢ��
			onRefresh();
			break;
		case R.id.mail_list_title:
			// ����ѡ������Ի���
			showMailBoxSelectDialog();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// �����л�����������߽��룬�ұ��˳�
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}

	/**
	 * ������Ӧ�����¼�����ΪListView�����ε�Activity��onTouchEvent�¼���������Ҫ��д�˷���
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		return onTouchEvent(event);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_NEW_MAIL
				&& resultCode == Activity.RESULT_OK) {
			// �����ǰ���ڷ����䣬��Ҫˢ��
			if (mailBoxType == MAIL_BOX_SEND)
				onRefresh();
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// ����
		title = (TextView) findViewById(R.id.mail_list_title);
		title.setText("�ռ���");
		title.setOnClickListener(this);
		// �ʼ��б�
		mListView = (XListView) findViewById(R.id.mail_list_listview);
		mListView.setXListViewListener(this);
		// д�Ű�ť
		newMailButton = (Button) findViewById(R.id.mail_list_new_mail_button);
		newMailButton.setOnClickListener(this);
		// ���ذ�ť
		backButton = (ImageView) findViewById(R.id.mail_list_back_icon);
		backButton.setOnClickListener(this);
		// ˢ�°�ť
		refreshButton = (Button) findViewById(R.id.mail_list_refresh_button);
		refreshButton.setOnClickListener(this);
		// ˢ�¶�̬ͼ
		refreshImageView = (ImageView) findViewById(R.id.mail_list_refresh_imageView);
		// ˢ�¶���
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new MailAdapter(this, items, R.layout.mail_item);
		mAdapter.setMailBoxType(mailBoxType);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * ��ʼ��handler
	 */
	private void initHandler() {
		// ��ʼ��handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// ֹͣˢ�¶���
				refreshAnimationDrawable.stop();
				refreshImageView.setVisibility(View.GONE);
				refreshButton.setVisibility(View.VISIBLE);

				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					String res = (String) msg.obj;
					// ���á���ʾ���ࡱ
					// ע�⣬Ĭ�ϵġ���ʾ���ࡱ���������˵���¼�������Ҫ����
					mListView.setPullLoadEnable(true);
					// ��ȡ���ݺ�ֹͣˢ��
					mListView.stopRefresh();
					mListView.stopLoadMore();
					// ˢ���б�
					refreshMailList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "MailListActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "MailListActivity");
	}

	/**
	 * �����ʼ��б�����
	 */
	private void getMail() {
		// ��ʾˢ�¶���
		refreshButton.setVisibility(View.GONE);
		refreshImageView.setVisibility(View.VISIBLE);
		refreshAnimationDrawable.start();

		String boxName = "";
		switch (mailBoxType) {
		case MAIL_BOX_IN:
			boxName = "inbox";
			break;
		case MAIL_BOX_SEND:
			boxName = "sendbox";
			break;
		case MAIL_BOX_DELETE:
			boxName = "deleted";
			break;
		}

		keys.clear();
		values.clear();
		// ���get����
		keys.add("app");
		values.add("mail");
		keys.add("boxname");
		values.add(boxName);
		keys.add("list");
		values.add(Integer.toString(currentList));

		// ��������
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"MailListActivity", true, this);
	}

	/**
	 * ˢ���б�
	 * 
	 * @param res
	 *            ����
	 */
	private void refreshMailList(String res) {
		// XML�����л�
		Mails mails = MyXMLParseUtils.readXml2Mails(res);

		// ����Ϊ��
		if (mails == null) {
			// ���á�����ˢ�¡�
			mListView.setPullRefreshEnable(false);
			// ���á���ʾ���ࡱ
			mListView.setPullLoadEnable(false);
			// ɾ��ָ��Cache�ļ�
			MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
					MyConstants.GET_URL, keys, values));
			// toast����
			Toast.makeText(this, R.string.mail_box_empty_text,
					Toast.LENGTH_SHORT).show();

			return;
		}

		// ��ȡ��ǰҳ��
		currentList = Integer.parseInt(mails.getPage().toString());
		// �����ҳ��
		totalList = Integer.parseInt(mails.getTotalPage().toString());

		// ��������һҳ������á���ʾ���ࡱ
		if (currentList == totalList)
			mListView.setPullLoadEnable(false);

		// ��ȡ�ʼ��б����
		items.addAll(mails.getMails());
		mAdapter.setMailBoxType(mailBoxType);
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
		// ��ǰҳ����һҳ�������´�����
		currentList++;
	}

	/*
	 *  ����ѡ������Ի���
	 */
	private void showMailBoxSelectDialog() {
		// ����ѡ��
		String[] choices = new String[] { "�ռ���", "������", "�ϼ���" };
		// ��������ѡ��Ի���
		new AlertDialog.Builder(this).setTitle("ѡ������")
				.setItems(choices, new DialogInterface.OnClickListener() {
					// ��Ӧ�б�ĵ���¼�
					public void onClick(DialogInterface dialog, int which) {
						// ����
						processLongClick(which);
					}
				}).show();
	}

	/**
	 * ��������ѡ��Ի������¼�
	 * 
	 * @param which
	 *            �Ի����а��µ�ѡ�����
	 */
	private void processLongClick(int which) {
		switch (which) {
		case 0:
			mailBoxType = MAIL_BOX_IN;
			title.setText("�ռ���");
			break;
		case 1:
			mailBoxType = MAIL_BOX_SEND;
			title.setText("������");
			break;
		case 2:
			mailBoxType = MAIL_BOX_DELETE;
			title.setText("�ϼ���");
			break;
		}
		// ���á�����ˢ�¡�
		mListView.setPullRefreshEnable(true);
		// ˢ��
		onRefresh();
	}

	@Override
	public void onRefresh() {
		// ����ǰҳ��Ϊ��ҳ
		currentList = 1;
		// ���ԭ������
		items.clear();
		// ���á���ʾ���ࡱ
		mListView.setPullLoadEnable(false);
		// ��������
		getMail();
		// ��ʾˢ��ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
				Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		mListView.setRefreshTime(timeStr);
	}

	@Override
	public void onLoadMore() {
		// ��������
		getMail();
	}
}
