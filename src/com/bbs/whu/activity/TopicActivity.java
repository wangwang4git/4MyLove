package com.bbs.whu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
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
import com.bbs.whu.adapter.TopicAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.TopicBean;
import com.bbs.whu.model.topic.Topics;
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
	// ���ذ�ť
	private ImageView backButton;
	// ˢ�°�ť
	private Button refreshButton;
	// ˢ�¶�̬ͼ
	private ImageView refreshImageView;
	// ˢ�¶���
	private AnimationDrawable refreshAnimationDrawable;

	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// ������Ӧһһ��Ӧ��������
	private boolean mRequestResponse = false;

	// �������ƶ���ʱ�������
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_topic);
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
		case R.id.topic_back_icon:
			// �˳�
			onBackPressed();
			break;
		case R.id.topic_refresh_button:
			// ˢ��
			onRefresh();
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
		// ���ذ�ť
		backButton = (ImageView) findViewById(R.id.topic_back_icon);
		backButton.setOnClickListener(this);
		// ˢ�°�ť
		refreshButton = (Button) findViewById(R.id.topic_refresh_button);
		refreshButton.setOnClickListener(this);
		// ˢ�¶�̬ͼ
		refreshImageView = (ImageView) findViewById(R.id.topic_refresh_imageView);
		// ˢ�¶���
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
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
		// ��ʾˢ�¶���
		refreshButton.setVisibility(View.GONE);
		refreshImageView.setVisibility(View.VISIBLE);
		refreshAnimationDrawable.start();
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
		mRequestResponse = true;
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
		// ��̳��������ȷ���ݷ��أ���ʾ������ʾ
		if (null == topics) {
			if (mRequestResponse == true) {
				mRequestResponse = false;
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
			}
			return;
		}
		// ��õ�ǰҳ��
		currentPage = Integer.parseInt(topics.getPage().toString());
		// �����ص�ҳ��
		if (currentPage == MyConstants.TOPIC_MAX_PAGES) {
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