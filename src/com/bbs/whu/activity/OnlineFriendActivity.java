package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

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
	// �������Ѱ�ť
	private ImageView addButton;
	// ˢ�°�ť
	private Button refreshButton;
	// ˢ�¶�̬ͼ
	private ImageView refreshImageView;
	// ˢ�¶���
	private AnimationDrawable refreshAnimationDrawable;
	// ������
	private static int requestCode = 0;
	private static final int SERVER_REQUEST_ALL_FRIEND = 1;
	private static final int SERVER_REQUEST_ONLINE_FRIEND = 2;
	private static final int SERVER_REQUEST_ADD_FRIEND = 3;
	public static final int SERVER_REQUEST_DELETE_FRIEND = 4;

	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// ͼƬ�첽����������
	private ImageLoader imageLoader = ImageLoader.getInstance();
	// ����ڴ滺����÷���
	// imageLoader.clearMemoryCache();
	// ����ļ�������÷���
	// imageLoader.clearDiscCache();
	// ͼƬ�첽���ػ������ñ���
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// ȡ����ʾtitle��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_online_friend);
		MyFontManager.changeFontType(this);// ���õ�ǰActivity������

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.person_head_portrait)
				.showImageForEmptyUri(R.drawable.person_head_portrait)
				.cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(5)).build();

		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ��ȡ�����б�
		requestCode = SERVER_REQUEST_ALL_FRIEND;
		getFriends();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ע��handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_SUCCESS, "OnlineFriendActivity");
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_FAIL, "OnlineFriendActivity");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.online_friend_back_icon:
			onBackPressed();
			break;
		case R.id.online_friend_add_icon:
			// ��ʾ�������ѶԻ���
			showAddFriendDialog();
			break;
		case R.id.online_friend_refresh_button:
			// ˢ��
			onRefresh();
			break;
		}
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
		// �������Ѱ�ť
		addButton = (ImageView) findViewById(R.id.online_friend_add_icon);
		addButton.setOnClickListener(this);
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
				R.layout.online_friend_item, imageLoader, options);
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
					switch (requestCode) {
					case SERVER_REQUEST_ALL_FRIEND:
						if (res == null) {
							// ��������㣬��ֹ��������
							requestCode = 0;
							Toast.makeText(OnlineFriendActivity.this,
									"��ȡ��Ϣʧ�ܣ�", Toast.LENGTH_SHORT).show();
						} else {
							// �������
							allFriends.clear();
							allFriends.addAll(MyXMLParseUtils
									.readXml2FriendsAll(res).getFriends());
							// �������ߺ���
							requestCode = SERVER_REQUEST_ONLINE_FRIEND;
							getFriends();
						}
						break;

					case SERVER_REQUEST_ONLINE_FRIEND:
						// ��������㣬��ֹ��������
						requestCode = 0;
						if (res == null)
							Toast.makeText(OnlineFriendActivity.this,
									"��ȡ��Ϣʧ�ܣ�", Toast.LENGTH_SHORT).show();
						else {
							// �������
							onlineFriends.clear();
							onlineFriends.addAll(MyXMLParseUtils
									.readXml2FriendsOnline(res).getFriends());
							// ˢ���б�
							refreshList();
						}
						break;

					case SERVER_REQUEST_DELETE_FRIEND:
						// ��������㣬��ֹ��������
						requestCode = 0;
						// ˢ���б�
						onRefresh();
						break;
					case SERVER_REQUEST_ADD_FRIEND:
						// ��������㣬��ֹ��������
						requestCode = 0;
						// ˢ���б�
						onRefresh();
						// ��ʾ�û�
						Toast.makeText(OnlineFriendActivity.this, res,
								Toast.LENGTH_SHORT).show();

					}
				case MyConstants.REQUEST_FAIL:
					break;
				}
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
		if (requestCode == SERVER_REQUEST_ALL_FRIEND)
			values.add("all");
		else if (requestCode == SERVER_REQUEST_ONLINE_FRIEND)
			values.add("online");

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
			if (onlineFriend.getUserid() != null)
				items.add(new FriendBean(onlineFriend.getUserid().toString(),
						onlineFriend.getUserfaceimg().toString(), true));
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
			if (!isOnline) {
				items.add(new FriendBean(allFriend.getID().toString(),
						allFriend.getUserfaceimg().toString(), false));
			}
		}
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * ��ʾ�������ѶԻ���
	 */
	private void showAddFriendDialog() {
		View dialog = getLayoutInflater().inflate(R.layout.add_friend_dialog,
				(ViewGroup) findViewById(R.id.add_friend_dialog));
		final EditText addFriendName = (EditText) dialog
				.findViewById(R.id.add_friend_name_editText);
		// �����ظ��Ի���
		new AlertDialog.Builder(this)
				.setTitle("��������")
				.setView(dialog)
				.setPositiveButton("ȷ��",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = addFriendName.getText()
										.toString();

								keys.clear();
								values.clear();
								// ���get����
								keys.add("app");
								values.add("friend");
								keys.add("add");
								values.add(name);

								// ����������
								requestCode = SERVER_REQUEST_ADD_FRIEND;
								// ��������
								MyBBSRequest.mGet(MyConstants.GET_URL, keys,
										values, "OnlineFriendActivity", true,
										OnlineFriendActivity.this);

							}
						}).setNegativeButton("ȡ��", null).show();
	}

	public static void setRequestCode(int requestCode) {
		OnlineFriendActivity.requestCode = requestCode;
	}

	@Override
	public void onRefresh() {
		requestCode = SERVER_REQUEST_ALL_FRIEND;
		// ��ȡ�����б�
		getFriends();
	}

	@Override
	public void onLoadMore() {
	}
}
