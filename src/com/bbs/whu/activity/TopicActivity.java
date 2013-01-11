package com.bbs.whu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bbs.whu.R;
import com.bbs.whu.adapter.TopicAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.model.topic.Topics;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * �����еİ�������б����
 * 
 * @author ljp
 * 
 */

public class TopicActivity extends Activity implements IXListViewListener {
	/* ���Ӱ��Ӣ��������һ��Activity���룬���������б����� */
	private String board;// ���Ӣ������
	/* ���ӵ�ҳ�����ڼ������ݣ�web�����ݷ�ҳ���� */
	int currentPage = 1;// ��ǰҳ��
	// �Ƿ�ǿ�ƴ������ȡ����
	boolean isForcingWebGet = false;
	// �����б�
	private XListView mListView;
	// �����б�������
	private TopicAdapter mAdapter;
	// �����б�����
	private ArrayList<TopicBean> items = new ArrayList<TopicBean>();
	// �����������ݵ�handler
	Handler mHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top_ten);
		// ��ȡ����Ĳ���
		board = getIntent().getStringExtra("board");
		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ���������б�����
		getTopic();
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// �����б�
		mListView = (XListView) findViewById(R.id.topten_listview);
		mListView.setXListViewListener(this);
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new TopicAdapter(this, items, R.layout.topic_item, board);
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
					refreshBulletinList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "TopicActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "TopicActivity");
	}

	/**
	 * ���������б�����
	 * 
	 */

	private void getTopic() {
		// ���get����
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("topics");
		keys.add("board");
		values.add(board);
		keys.add("page");
		values.add(Integer.toString(currentPage));

		// ��������
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "TopicActivity",
				isForcingWebGet, this);
	}

	/**
	 * ˢ���б�
	 * 
	 * @param res
	 *            ����
	 */
	private void refreshBulletinList(String res) {
		// XML�����л�
		Topics topics = MyXMLParseUtils.readXml2Topics(res);
		// ��õ�ǰҳ��
		currentPage = Integer.parseInt(topics.getPage().toString());
		// ������10ҳ
		if (currentPage == 10) {
			// ���á���ʾ���ࡱ
			mListView.setPullLoadEnable(false);
		}
		// ��ȡ�����б����
		items.addAll(topics.getTopics());
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
		// ��ǰҳ����һҳ�������´�����
		currentPage++;
	}

	@Override
	public void onRefresh() {
		// ����ǰҳ��Ϊ��ҳ
		currentPage = 1;
		// ���ԭ������
		items.clear();
		// ǿ�ƴ��������������б������
		isForcingWebGet = true;
		// ���á���ʾ���ࡱ
		mListView.setPullLoadEnable(false);
		// ��������
		getTopic();
		// ��ʾˢ��ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
				Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		mListView.setRefreshTime(timeStr);
	}

	@Override
	public void onLoadMore() {
		// ��������
		getTopic();
	}
}