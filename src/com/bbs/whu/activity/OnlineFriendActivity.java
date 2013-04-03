package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
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

import com.bbs.whu.R;
import com.bbs.whu.adapter.OnlineFriendAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.FriendBean;
import com.bbs.whu.model.FriendsAllBean;
import com.bbs.whu.model.FriendsOnlineBean;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

/**
 * �ҵĺ��ѽ��棬 �б���ʽ��ʾ�û��ĺ��ѣ� ����б����޲����� �����б�����˵���ѡ����ú��ѷ��ͼ�ʱ��Ϣ�����ʼ���
 * 
 * @author wwang
 * 
 */
public class OnlineFriendActivity extends Activity implements OnClickListener,
		IXListViewListener {
	// �ҵĺ����б�
	private XListView mListView;
	// �ҵĺ����б�������
	private OnlineFriendAdapter mAdapter;
	// ȫ����������Դ
	private ArrayList<FriendsAllBean> allFriends = new ArrayList<FriendsAllBean>();
	// ���ߺ�������Դ
	private ArrayList<FriendsOnlineBean> onlineFriends = new ArrayList<FriendsOnlineBean>();
	// �ҵĺ����б�����Դ
	private ArrayList<FriendBean> items = new ArrayList<FriendBean>();
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
	// �Ƿ�������ߺ�������
	private boolean isRequestOnlineFriends = false;
	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// �������ƶ���ʱ�������
	float x_temp1 = 0, y_temp1 = 0, x_temp2, y_temp2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ȡ����ʾtitle��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_online_friend);
		MyFontManager.changeFontType(this);// ���õ�ǰActivity������

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ��ȡ�����б�
		getFriends();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.online_friend_back_icon:
			onBackPressed();
			break;
		case R.id.online_friend_refresh_button:
			// ˢ��
			onRefresh();
			break;
		}
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
		// �ҵĺ����б�
		mListView = (XListView) findViewById(R.id.online_friend_listview);
		mListView.setXListViewListener(this);
		// ��Ϊ���Ѳ��漰��ҳ������ȡ�������ظ��ࡱ�Ĺ���
		mListView.setPullLoadEnable(false);
		// ���ذ�ť
		backButton = (ImageView) findViewById(R.id.online_friend_back_icon);
		backButton.setOnClickListener(this);
		// ˢ�°�ť
		refreshButton = (Button) findViewById(R.id.online_friend_refresh_button);
		refreshButton.setOnClickListener(this);
		// ˢ�¶�̬ͼ
		refreshImageView = (ImageView) findViewById(R.id.online_friend_refresh_imageView);
		// ˢ�¶���
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new OnlineFriendAdapter(this, items,
				R.layout.online_friend_item);
		mListView.setAdapter(mAdapter);
	}

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
					// ��ȡ���ݺ�ֹͣˢ��
					mListView.stopRefresh();

					if (!isRequestOnlineFriends) {
						allFriends.clear();
						allFriends.addAll(MyXMLParseUtils.readXml2FriendsAll(
								res).getFriends());
						// �������ߺ���
						isRequestOnlineFriends = true;
						getFriends();
					} else {
						isRequestOnlineFriends = false;
						onlineFriends.clear();
						onlineFriends.addAll(MyXMLParseUtils
								.readXml2FriendsOnline(res).getFriends());
						// ˢ���б�
						refreshList();
					}
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "OnlineFriendActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "OnlineFriendActivity");
	}

	/**
	 * ��ȡ�����б�
	 */
	private void getFriends() {
		// ��ʾˢ�¶���
		refreshButton.setVisibility(View.GONE);
		refreshImageView.setVisibility(View.VISIBLE);
		refreshAnimationDrawable.start();

		keys.clear();
		values.clear();
		// ���get����
		keys.add("app");
		values.add("friend");
		keys.add("list");
		if (isRequestOnlineFriends)
			values.add("online");
		else
			values.add("all");

		// ��������
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"OnlineFriendActivity", true, this);
	}

	/**
	 * ˢ�º����б�
	 */
	private void refreshList() {
		items.clear();
		// ������ߺ���
		for (FriendsOnlineBean onlineFriend : onlineFriends) {
			items.add(new FriendBean(onlineFriend.getUserid().toString(), true));
		}

		// ��Ӳ����ߺ���
		boolean isOnline;
		for (FriendsAllBean allFriend : allFriends) {
			isOnline = false;
			for (FriendsOnlineBean onlineFriend : onlineFriends) {
				if (allFriend.getID().toString()
						.equals(onlineFriend.getUserid().toString())) {
					isOnline = true;
					break;
				}
			}
			if (!isOnline)
				items.add(new FriendBean(allFriend.getID().toString(), false));
		}
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// ��ȡ�����б�
		getFriends();
	}

	@Override
	public void onLoadMore() {
	}
}
