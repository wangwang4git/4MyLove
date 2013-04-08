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
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.MailAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.MailBean;
import com.bbs.whu.model.mail.Mails;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyXMLParseUtils;
import com.bbs.whu.xlistview.XListView;
import com.bbs.whu.xlistview.XListView.IXListViewListener;

public class MailListActivity extends Activity implements IXListViewListener,
		OnClickListener {
	// 标题
	TextView title;
	// 邮件列表
	private XListView mListView;
	// 邮件列表数据适配器
	private MailAdapter mAdapter;
	// 邮件列表数据
	private ArrayList<MailBean> items = new ArrayList<MailBean>();
	// 写信按钮
	private Button newMailButton;
	// 接收请求数据的handler
	Handler mHandler;
	// 返回按钮
	private ImageView backButton;
	// 刷新按钮
	private Button refreshButton;
	// 刷新动态图
	private ImageView refreshImageView;
	// 刷新动作
	private AnimationDrawable refreshAnimationDrawable;

	// 当前页数
	private int currentList = 1;
	// 总页数
	private int totalList;

	// get参数
	ArrayList<String> keys = new ArrayList<String>();
	ArrayList<String> values = new ArrayList<String>();

	// 邮箱类型
	private int mailBoxType = MAIL_BOX_IN;

	public static final int MAIL_BOX_IN = 1;
	public static final int MAIL_BOX_SEND = 2;
	public static final int MAIL_BOX_DELETE = 3;

	/*打开新activity的请求码*/
	public static final int REQUEST_CODE_NEW_MAIL = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mail_list);
		MyFontManager.changeFontType(this);// 设置当前Activity的字体

		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
		// 初始化handler
		initHandler();
		// 请求邮件列表数据
		getMail();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_SUCCESS, "MailListActivity");
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_FAIL, "MailListActivity");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mail_list_new_mail_button:
			// 进入写信界面
			Intent mIntent = new Intent(this, MailSendActivity.class);
			mIntent.putExtra("head", MyConstants.NEW_MAIL);
			startActivityForResult(mIntent, REQUEST_CODE_NEW_MAIL);
			break;
		case R.id.mail_list_back_icon:
			// 退出
			onBackPressed();
			break;
		case R.id.mail_list_refresh_button:
			// 刷新
			onRefresh();
			break;
		case R.id.mail_list_title:
			// 弹出选择信箱对话框
			showMailBoxSelectDialog();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// 设置切换动画，从左边进入，右边退出
		overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	}

	/**
	 * 最先响应触屏事件，因为ListView会屏蔽掉Activity的onTouchEvent事件，所以需要重写此方法
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		super.dispatchTouchEvent(event);
		return onTouchEvent(event);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_NEW_MAIL
				&& resultCode == Activity.RESULT_OK) {
			// 如果当前处于发件箱，需要刷新
			if (mailBoxType == MAIL_BOX_SEND)
				onRefresh();
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		// 标题
		title = (TextView) findViewById(R.id.mail_list_title);
		title.setText("收件箱");
		title.setOnClickListener(this);
		// 邮件列表
		mListView = (XListView) findViewById(R.id.mail_list_listview);
		mListView.setXListViewListener(this);
		// 写信按钮
		newMailButton = (Button) findViewById(R.id.mail_list_new_mail_button);
		newMailButton.setOnClickListener(this);
		// 返回按钮
		backButton = (ImageView) findViewById(R.id.mail_list_back_icon);
		backButton.setOnClickListener(this);
		// 刷新按钮
		refreshButton = (Button) findViewById(R.id.mail_list_refresh_button);
		refreshButton.setOnClickListener(this);
		// 刷新动态图
		refreshImageView = (ImageView) findViewById(R.id.mail_list_refresh_imageView);
		// 刷新动作
		refreshAnimationDrawable = (AnimationDrawable) refreshImageView
				.getBackground();
	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 创建适配器
		mAdapter = new MailAdapter(this, items, R.layout.mail_item);
		mAdapter.setMailBoxType(mailBoxType);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 停止刷新动画
				refreshAnimationDrawable.stop();
				refreshImageView.setVisibility(View.GONE);
				refreshButton.setVisibility(View.VISIBLE);

				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					String res = (String) msg.obj;
					// 启用“显示更多”
					// 注意，默认的“显示更多”功能屏蔽了点击事件，所以要启用
					mListView.setPullLoadEnable(true);
					// 获取数据后停止刷新
					mListView.stopRefresh();
					mListView.stopLoadMore();
					// 刷新列表
					refreshMailList(res);
					break;
				case MyConstants.REQUEST_FAIL:
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "MailListActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "MailListActivity");
	}

	/**
	 * 请求邮件列表数据
	 */
	private void getMail() {
		// 显示刷新动画
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
		// 添加get参数
		keys.add("app");
		values.add("mail");
		keys.add("boxname");
		values.add(boxName);
		keys.add("list");
		values.add(Integer.toString(currentList));

		// 请求数据
		MyBBSRequest.mGet(MyConstants.GET_URL, keys, values,
				"MailListActivity", true, this);
	}

	/**
	 * 刷新列表
	 * 
	 * @param res
	 *            数据
	 */
	private void refreshMailList(String res) {
		// XML反序列化
		Mails mails = MyXMLParseUtils.readXml2Mails(res);

		// 邮箱为空
		if (mails == null) {
			// 禁用“下拉刷新”
			mListView.setPullRefreshEnable(false);
			// 禁用“显示更多”
			mListView.setPullLoadEnable(false);
			// 删除指定Cache文件
			MyBBSCache.delCacheFile(MyBBSCache.getCacheFileName(
					MyConstants.GET_URL, keys, values));
			// toast提醒
			Toast.makeText(this, R.string.mail_box_empty_text,
					Toast.LENGTH_SHORT).show();

			return;
		}

		// 获取当前页号
		currentList = Integer.parseInt(mails.getPage().toString());
		// 获得总页数
		totalList = Integer.parseInt(mails.getTotalPage().toString());

		// 如果是最后一页，则禁用“显示更多”
		if (currentList == totalList)
			mListView.setPullLoadEnable(false);

		// 获取邮件列表并添加
		items.addAll(mails.getMails());
		mAdapter.setMailBoxType(mailBoxType);
		// 刷新ListView
		mAdapter.notifyDataSetChanged();
		// 当前页增加一页，便于下次申请
		currentList++;
	}

	/*
	 *  弹出选择信箱对话框
	 */
	private void showMailBoxSelectDialog() {
		// 信箱选择
		String[] choices = new String[] { "收件箱", "发件箱", "废件箱" };
		// 弹出信箱选择对话框
		new AlertDialog.Builder(this).setTitle("选择信箱")
				.setItems(choices, new DialogInterface.OnClickListener() {
					// 响应列表的点击事件
					public void onClick(DialogInterface dialog, int which) {
						// 处理
						processLongClick(which);
					}
				}).show();
	}

	/**
	 * 处理信箱选择对话框点击事件
	 * 
	 * @param which
	 *            对话框中按下的选项序号
	 */
	private void processLongClick(int which) {
		switch (which) {
		case 0:
			mailBoxType = MAIL_BOX_IN;
			title.setText("收件箱");
			break;
		case 1:
			mailBoxType = MAIL_BOX_SEND;
			title.setText("发件箱");
			break;
		case 2:
			mailBoxType = MAIL_BOX_DELETE;
			title.setText("废件箱");
			break;
		}
		// 启用“下拉刷新”
		mListView.setPullRefreshEnable(true);
		// 刷新
		onRefresh();
	}

	@Override
	public void onRefresh() {
		// 将当前页设为首页
		currentList = 1;
		// 清空原有数据
		items.clear();
		// 禁用“显示更多”
		mListView.setPullLoadEnable(false);
		// 请求数据
		getMail();
		// 显示刷新时间
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm",
				Locale.SIMPLIFIED_CHINESE);
		String timeStr = sdf.format(new Date());
		mListView.setRefreshTime(timeStr);
	}

	@Override
	public void onLoadMore() {
		// 请求数据
		getMail();
	}
}
