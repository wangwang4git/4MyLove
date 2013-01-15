package com.bbs.whu.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.bbs.whu.R;
import com.bbs.whu.adapter.BulletinAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.BulletinBean;
import com.bbs.whu.model.bulletin.Page;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyRegexParseUtils;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * ����������棬 ��ͨ�������ˢ�¡���ˢ�����ӣ� ��ͨ���������ʾ���ࡱ���ظ��࣬��ҳ����
 * 
 * @author double
 * 
 */
public class BulletinActivity extends Activity implements IXListViewListener {
	/* ���Ӱ��Ӣ����������ID����һ��Activity���룬���������������� */
	// ���Ӱ��Ӣ����
	String board;
	// ����ID
	String groupid;
	/* ���ӵ�ҳ�����ڼ������ݣ�web�����ݷ�ҳ���� */
	// ���ӵ�ǰҳ��
	int currentPage = 1;
	// ������ҳ��
	int totalPage;
	// �Ƿ�ǿ�ƴ������ȡ����
	boolean isForcingWebGet = false;
	// ���ӻظ��б�������
	private BulletinAdapter mAdapter;
	// ���ӻظ��б�
	private ArrayList<BulletinBean> items = new ArrayList<BulletinBean>();
	// �����������ݵ�handler
	Handler mHandler;
	// �ظ��б�
	private XListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bulletin);
		// ��ȡ����Ĳ���
		board = getIntent().getStringExtra("board");
		groupid = getIntent().getStringExtra("groupid");
		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ��������
		getBulletin();

		// ���÷�������޸�ListView FastScrollerĬ�ϻ���
		// �μ�http://www.eoeandroid.com/forum.php?mod=viewthread&tid=176342ԭ��
		// try {
		// Field field = AbsListView.class.getDeclaredField("mFastScroller");
		// field.setAccessible(true);
		// Object obj = field.get(mListView);
		// field = field.getType().getDeclaredField("mThumbDrawable");
		// field.setAccessible(true);
		// Drawable drawable = (Drawable) field.get(obj);
		// drawable = getResources().getDrawable(R.drawable.ic_launcher);
		// field.set(obj, drawable);
		// // Toast.makeText(this, field.getType().getName(), 1000).show();
		// } catch (Exception e) {
		// throw new RuntimeException(e);
		// }
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		// �ظ��б�
		mListView = (XListView) findViewById(R.id.bulletin_listview);
		mListView.setXListViewListener(this);
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new BulletinAdapter(this, items,
				R.layout.bulletin_comment_item);
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
					// ˢ�����ӽ���
					refreshBulletin(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "BulletinActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "BulletinActivity");
	}

	/**
	 * ����������������
	 */
	private void getBulletin() {
		// ���get����
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("read");
		keys.add("board");
		values.add(board);
		keys.add("GID");
		values.add(groupid);
		keys.add("page");
		values.add(Integer.toString(currentPage));
		// ��������
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"BulletinActivity", isForcingWebGet, this);
	}

	/**
	 * ˢ�����ӽ���
	 * 
	 * @param res
	 *            ����
	 */
	private void refreshBulletin(String res) {
		// XML�����л�
		Page page = MyXMLParseUtils.readXml2Page(res);
		// ��õ�ǰҳ��
		currentPage = Integer.parseInt(page.getNum().getAttributeValue());
		// ���ҳ����
		totalPage = Integer.parseInt(page.getTotal().getAttributeValue());

		// ��������һҳ������á���ʾ���ࡱ
		if (currentPage == totalPage)
			mListView.setPullLoadEnable(false);

		// ��ȡ�������ݲ����
		items.addAll(MyRegexParseUtils.getContentList(page));
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
		// ��ǰҳ����һҳ�������´�����
		currentPage++;
	}

	@Override
	public void onRefresh() {
		// ����ǰҳ��Ϊ��ҳ
		currentPage = 1;
		// �������
		items.clear();
		// ����ǿ�ƴ���������������������
		isForcingWebGet = true;
		// ���á���ʾ���ࡱ
		mListView.setPullLoadEnable(false);
		// ��������
		getBulletin();
		// ��ʾˢ��ʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
				Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		mListView.setRefreshTime(timeStr);
	}

	@Override
	public void onLoadMore() {
		// ��������
		getBulletin();
	}
}
