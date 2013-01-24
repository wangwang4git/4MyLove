package com.bbs.whu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.TopicAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.model.topic.Topics;
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
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

public class TopicActivity extends Activity implements IXListViewListener,
		OnClickListener {
	/* ���Ӱ��Ӣ��������һ��Activity���룬���������б����� */
	private String board;// ���Ӣ������
	private String name;// �������
	/* ���ӵ�ҳ�����ڼ������ݣ�web�����ݷ�ҳ���� */
	int currentPage = 1;// ��ǰҳ��
	// �Ƿ�ǿ�ƴ������ȡ����
	boolean isForcingWebGet = false;
	// ����
	TextView title;
	// �����б�
	private XListView mListView;
	// �����б�������
	private TopicAdapter mAdapter;
	// �����б�����
	private ArrayList<TopicBean> items = new ArrayList<TopicBean>();
	// ���ӷ���ť
	private Button publishButton;
	// �����������ݵ�handler
	Handler mHandler;

	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();
	
	// �ȴ��Ի���
	private ProgressHUDTask mProgress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_topic);
		// ��ʾ�ȴ��Ի���
		mProgress = new ProgressHUDTask(this);
		mProgress.execute();
		// ��ȡ����Ĳ���
		board = getIntent().getStringExtra("board");
		name = getIntent().getStringExtra("name");
		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ���������б�����
		getTopic();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.topic_publish_button:
			// ��������
			publishBulletin();
			break;
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// ����
		title = (TextView) findViewById(R.id.topic_title);
		title.setText(name);
		// �����б�
		mListView = (XListView) findViewById(R.id.topic_listview);
		mListView.setXListViewListener(this);
		// ����ť
		publishButton = (Button) findViewById(R.id.topic_publish_button);
		publishButton.setOnClickListener(this);
		// ����������û�������ʾ����ť
		if (MyApplication.getInstance().getName().equals("4MyLove"))
			publishButton.setVisibility(View.GONE);
		else
			publishButton.setVisibility(View.VISIBLE);
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
				// ȡ����ʾ�ȴ��Ի���
				if (mProgress != null) {
					mProgress.dismiss();
					mProgress = null;
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
		keys.clear();
		values.clear();
		// ���get����
		keys.add("app");
		values.add("topics");
		keys.add("board");
		values.add(board);
		keys.add("page");
		values.add(Integer.toString(currentPage));

		// ��������
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "TopicActivity",
				isForcingWebGet, this);
		// ��ʾ�ȴ��Ի���
		if (null == mProgress) {
			mProgress = new ProgressHUDTask(this);
			mProgress.execute();
		}
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
		// ��̳��������ȷ���ݷ��أ���ʾ������ʾ
		if (null == topics) {
			// ���á�����ˢ�¡�
			mListView.setPullRefreshEnable(false);
			// ���á���ʾ���ࡱ
			mListView.setPullLoadEnable(false);
			// ɾ��ָ��Cache�ļ�
			MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
					MyConstants.GET_URL, keys, values));
			// toast����
			Toast.makeText(this, R.string.bbs_exception_text,
					Toast.LENGTH_SHORT).show();
			return;
		}
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

	/**
	 * ��������
	 */
	private void publishBulletin() {
		// ��ת�����ӻظ�����
		Intent intent = new Intent(this, BulletinReplyActivity.class);

		// ��Ӳ���
		intent.putExtra("head", MyConstants.NEW_BULLETIN);
		intent.putExtra("board", board);
		intent.putExtra("id", "0");

		// ����Activity��������һ��intend����
		this.startActivity(intent);
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