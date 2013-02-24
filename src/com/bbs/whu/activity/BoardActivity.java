package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.BoardAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.BoardBean;
import com.bbs.whu.model.board.Board;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyXMLParseUtils;

public class BoardActivity extends Activity {
	private ExpandableListView mExpandableListView;
	private BoardAdapter mAdapter;
	// һ���б�����
	private List<BoardBean> groups = new ArrayList<BoardBean>();
	// �����б�����
	private List<List<Board>> childs = new ArrayList<List<Board>>();
	// �����������ݵ�handler
	Handler mHandler;

	// get����
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// ����ƥ������Դ
	private List<String> mSearchBoardsList = new ArrayList<String>();
	// ����ƥ��������
	private ArrayAdapter<String> mSearchAdapter;
	// ��������
	private LinearLayout mSearchLinearLayout;
	// ���������
	private AutoCompleteTextView mSearchEditText;
	// ����ȷ����ť
	private ImageView mSearchConfirmButton;
	// ��������������ʾ��ť
	private Button mShowSearch;
	// ˢ�°�ť
	private Button refreshButton;
	// ˢ�¶�̬ͼ
	private ImageView refreshImageView;
	// ˢ�¶���
	private AnimationDrawable refreshAnimationDrawable;

	// ������Ӧһһ��Ӧ��������
	private boolean mRequestResponse = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_board);
		// ��ʼ���ؼ�
		initView();
		// ��ʼ��������
		initAdapter();
		// ��ʼ��handler
		initHandler();
		// ��ȡ�б�����
		getBoardList(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ֻ�в��񷵻ؼ���������false��������MainActivity�в��񷵻ؼ�
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// ����������������ʾ��������
			if (mSearchLinearLayout.getVisibility() == View.VISIBLE) {
				searchLayoutShow(false);
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void initView() {
		mExpandableListView = (ExpandableListView) findViewById(R.id.board_elist);
		mExpandableListView.setGroupIndicator(null);

		// ��ȡAutoCompleteTextView���������
		mSearchEditText = (AutoCompleteTextView) findViewById(R.id.board_search_autocompletetextview);
		// ������ƥ������Դ
		mSearchAdapter = new ArrayAdapter<String>(this,
				R.layout.board_search_match_item, mSearchBoardsList);
		// ��AutoCompleteTextView����ָ��ArrayAdapter����
		mSearchEditText.setAdapter(mSearchAdapter);

		// ��ȡȷ����ť
		mSearchConfirmButton = (ImageView) findViewById(R.id.board_search_confirm);
		// ��ȡ��������
		mSearchLinearLayout = (LinearLayout) findViewById(R.id.board_search_linearlayout);
		// ��ȡ��������������ʾ��ť
		mShowSearch = (Button) findViewById(R.id.board_search_show_linearlayout_button);

		mSearchConfirmButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchLayoutShow(false);
				searchTheBoard(mSearchEditText.getText().toString());
			}
		});

		mShowSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchLayoutShow(true);
			}
		});

		// ˢ�°�ť
		refreshButton = (Button) findViewById(R.id.board_refresh_button);
		refreshButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ˢ��
				getBoardList(true);
			}
		});
		// ˢ�¶�̬ͼ
		refreshImageView = (ImageView) findViewById(R.id.board_refresh_imageView);
		// ˢ�¶���
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
	}

	/**
	 * ��ʼ��������
	 */
	private void initAdapter() {
		// ����������
		mAdapter = new BoardAdapter(this, groups, childs);
		mExpandableListView.setAdapter(mAdapter);
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
					// ˢ���б�
					refreshBoardList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "BoardActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "BoardActivity");
	}

	/**
	 * ���������б�����
	 * 
	 * @param isForcingWebGet
	 *            �Ƿ�ǿ�ƴ������ȡ����
	 */
	private void getBoardList(boolean isForcingWebGet) {
		// ��ʾˢ�¶���
		refreshButton.setVisibility(View.GONE);
		refreshImageView.setVisibility(View.VISIBLE);
		refreshAnimationDrawable.start();
		keys.clear();
		values.clear();
		// ���get����
		keys.add("app");
		values.add("boards");
		// ��������
		mRequestResponse = true;
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values, "BoardActivity",
				isForcingWebGet, this);
	}

	/**
	 * ˢ���б�
	 * 
	 * @param res
	 *            ����
	 */
	private void refreshBoardList(String res) {
		groups.clear();
		childs.clear();

		List<BoardBean> boardBeanList = MyXMLParseUtils.readXml2BoardList(res);
		// ��̳��������ȷ���ݷ��أ���ʾ������ʾ
		if (null == boardBeanList) {
			if (mRequestResponse == true) {
				mRequestResponse = false;
				// ɾ��ָ��Cache�ļ�
				MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
						MyConstants.GET_URL, keys, values));
				// toast����
				Toast.makeText(this, R.string.bbs_exception_text,
						Toast.LENGTH_SHORT).show();
			}
			return;
		}

		// ���һ���б�����
		groups.addAll(boardBeanList);
		// ��Ӷ����б�����
		for (int i = 0; i < boardBeanList.size(); ++i) {
			childs.add(boardBeanList.get(i).getBoards());
		}
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();

		mSearchBoardsList.clear();
		// ��������ƥ������Դ
		for (int i = 0; i < boardBeanList.size(); ++i) {
			for (int j = 0; j < boardBeanList.get(i).getBoards().size(); ++j) {
				mSearchBoardsList.add(boardBeanList.get(i).getBoards().get(j)
						.getName().getAttributeValue());
				mSearchBoardsList.add(boardBeanList.get(i).getBoards().get(j)
						.getId().getAttributeValue());
			}
		}
		mSearchAdapter.notifyDataSetChanged();
	}

	/**
	 * ����������ʾ�������л�
	 * 
	 * @param bool
	 *            true����ʾ��false������
	 */
	void searchLayoutShow(boolean bool) {
		if (true == bool) {
			// ������������
			mSearchEditText.setText("");
			// ��ʾ
			mSearchLinearLayout.setVisibility(View.VISIBLE);
			mShowSearch.setVisibility(View.INVISIBLE);
		} else {
			// ���������
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mSearchEditText.getWindowToken(), 0);
			mSearchLinearLayout.setVisibility(View.INVISIBLE);
			mShowSearch.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * ��������İ�����������н�����ת
	 * 
	 * @param keyword
	 *            ����İ����
	 */
	void searchTheBoard(String keyword) {
		// ��������
		for (int i = 0; i < childs.size(); ++i) {
			for (int j = 0; j < childs.get(i).size(); ++j) {
				// ��ѯ�ð��������
				if (keyword.equals(childs.get(i).get(j).getName()
						.getAttributeValue())
						|| keyword.equals(childs.get(i).get(j).getId()
								.getAttributeValue())) {
					// ��ת�������б����
					Intent mIntent = new Intent(this, TopicActivity.class);
					// ��Ӳ��� app=topics&board=PieFriends&page=1
					mIntent.putExtra("board", childs.get(i).get(j).getId()
							.getAttributeValue());
					mIntent.putExtra("name", childs.get(i).get(j).getName()
							.getAttributeValue());
					this.startActivity(mIntent);
					return;
				}
			}
		}
		// ��ѯ�ð����������
		Toast.makeText(this, "���治����", Toast.LENGTH_SHORT).show();
		return;
	}
}
