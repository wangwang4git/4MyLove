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

import com.bbs.whu.R;
import com.bbs.whu.adapter.MailAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.MailBean;
import com.bbs.whu.model.mail.Mails;
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

	// �������ƶ���ʱ�������
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	// ��������
	private int mailBoxType = MAIL_BOX_IN;

	private static final int MAIL_BOX_IN = 1;
	private static final int MAIL_BOX_SEND = 2;
	private static final int MAIL_BOX_DELETE = 3;

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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mail_list_new_mail_button:
			// ����д�Ž���
			startActivity(new Intent(this, MailSendActivity.class));
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

	/**
	 * ������Ӧ�����¼�����ΪListView�����ε�Activity��onTouchEvent�¼���������Ҫ��д�˷���
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		return onTouchEvent(event);
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
		// ��ȡ��ǰҳ��
		currentList = Integer.parseInt(mails.getPage().toString());
		// �����ҳ��
		totalList = Integer.parseInt(mails.getTotalPage().toString());

		// ��������һҳ������á���ʾ���ࡱ
		if (currentList == totalList)
			mListView.setPullLoadEnable(false);

		// ��ȡ�ʼ��б����
		items.addAll(mails.getMails());
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
			break;
		case 1:
			mailBoxType = MAIL_BOX_SEND;
			break;
		case 2:
			mailBoxType = MAIL_BOX_DELETE;
			break;
		}
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
