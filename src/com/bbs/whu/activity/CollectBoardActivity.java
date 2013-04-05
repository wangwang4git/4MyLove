package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.CollectBoardAdapter;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.xlistview.XListView;

/**
 * 我的收藏版面界面， 列表形式显示用户收藏的版面， 点击列表项跳转到版面文章列表界面， 长按列表项弹出菜单，菜单项功能待定。
 * 
 * @author wwang
 * 
 */
public class CollectBoardActivity extends Activity {
	// 收藏版块列表
	private XListView mListView;
	// 收藏版块列表适配器
	private CollectBoardAdapter mAdapter;
	
	// 收藏版块列表数据源
	private ArrayList<String> items = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// 取消显示title栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_collect_board);
		MyFontManager.changeFontType(this);//设置当前Activity的字体
		
		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 添加数据
		addData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 收藏版块列表
		mListView = (XListView) findViewById(R.id.collectboard_listview);
		// 禁用“显示更多”
		mListView.setPullLoadEnable(false);
		// 禁用“下拉刷新”
		mListView.setPullRefreshEnable(false);
		// 取消ListView分割线
		// mListView.setDividerHeight(0);
		// 设置点击监听器
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(arg0.getContext(),
						"点击的是 " + items.get((int) arg3), Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new CollectBoardAdapter(this, items,
				R.layout.collect_board_item);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 添加数据
	 */
	private void addData() {
		items.add("部门直通车");
		items.add("研究生之家");
		items.add("缘分的天空");
		items.add("武大特快");
		items.add("工作信息");
		items.add("部门直通车");
		items.add("研究生之家");
		items.add("缘分的天空");
		items.add("武大特快");
		items.add("工作信息");
		items.add("tail");
		mAdapter.notifyDataSetChanged();
	}
}
