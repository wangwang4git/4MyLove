package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ExpandableListView;

import com.bbs.whu.R;
import com.bbs.whu.adapter.BoardAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.BoardBean;
import com.bbs.whu.model.board.Board;
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
		// ���get����
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("app");
		values.add("boards");
		// ��������
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

		// ���һ���б�����
		groups.addAll(boardBeanList);
		// ��Ӷ����б�����
		for (int i = 0; i < boardBeanList.size(); ++i) {
			childs.add(boardBeanList.get(i).getBoards());
		}
		// ˢ��ListView
		mAdapter.notifyDataSetChanged();
	}
}
