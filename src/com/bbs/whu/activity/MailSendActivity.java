package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.FaceGridAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.FaceBean;
import com.bbs.whu.progresshud.ProgressHUDTask;
import com.bbs.whu.utils.MyBBSFacesUtils;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFontManager;

/**
 * 邮件发送
 * 
 * @author WWang
 * 
 */
public class MailSendActivity extends Activity {
	// 发信还是看信
	private int head;
	// 信箱
	private int boxName;
	// 信件编号
	private int read;

	// 标题
	private TextView mActivityTitle;
	// 邮件收件人
	private EditText mAddress;
	// 邮件标题
	private EditText mTitle;
	// 邮件正文
	private EditText mContent;
	// 邮件发送按钮
	private TextView mSendSubmit;
	// 返回按钮
	private ImageView backButton;
	// 接收请求数据的handler
	private Handler mHandler;
	// 等待对话框
	private ProgressHUDTask mProgress;

	// 表情选择器切换按钮
	private ImageButton mBtnFace;
	// 软键盘切换按钮
	private ImageButton mBtnKeyboard;
	// 表情选择器
	private GridView mBBSFacesGridView;
	// 表情选择器适配器
	private FaceGridAdapter mAdapter;
	// BBS表情名称、表情资源ID映射集合
	private ArrayList<FaceBean> BBSFacesList = new ArrayList<FaceBean>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题栏显示
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_mail_send);
		MyFontManager.changeFontType(this);// 设置当前Activity的字体

		// 获取传入的参数
		Intent mIntent = getIntent();
		head = mIntent.getIntExtra("head", -1);

		// 初始化控件
		initView();

		// 初始化适配器
		if (head == MyConstants.NEW_MAIL) {
			initAdapter();
		}
		// 初始化handler
		initHandler();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mActivityTitle = (TextView) findViewById(R.id.mail_send_activity_title);	// 界面标题
		mAddress = (EditText) findViewById(R.id.mail_send_address);	// 收件人
		mTitle = (EditText) findViewById(R.id.mail_send_title);				// 邮件标题
		mContent = (EditText) findViewById(R.id.mail_send_content);	// 邮件内容
		// 发送按钮
		mSendSubmit = (TextView) findViewById(R.id.mail_send_submit);
		// 设置按钮统一监听器
		mSendSubmit.setOnClickListener(mailSendClickListener);
		// 表情按钮
		mBtnFace = (ImageButton) findViewById(R.id.mail_send_face);
		// 键盘按钮
		mBtnKeyboard = (ImageButton) findViewById(R.id.mail_send_keyboard);
		// 返回按钮
		backButton = (ImageView) findViewById(R.id.mail_send_back_icon);
		backButton.setOnClickListener(mailSendClickListener);

		if (head == MyConstants.NEW_MAIL) {
			mActivityTitle.setText("发送信件");
			mSendSubmit.setText("发送");
			// 设置输入框焦点变更统一监听器
			mAddress.setOnFocusChangeListener(mailSendEditFocusListener);
			// 设置输入框焦点变更统一监听器
			mTitle.setOnFocusChangeListener(mailSendEditFocusListener);
			// 设置输入框焦点变更统一监听器
			mContent.setOnFocusChangeListener(mailSendEditFocusListener);
			// 设置按钮统一监听器
			mBtnFace.setOnClickListener(mailSendClickListener);
			mBtnFace.setEnabled(false);
			// 设置按钮统一监听器
			mBtnKeyboard.setOnClickListener(mailSendClickListener);
			mBtnKeyboard.setEnabled(false);

			mBBSFacesGridView = (GridView) findViewById(R.id.mail_send_grid_face);
			// 设置监听器
			mBBSFacesGridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// 插入表情名称
					mContent.append(BBSFacesList.get(arg2).getName());
				}
			});
		} else if (head == MyConstants.READ_MAIL) {
			mActivityTitle.setText("信件");
			mSendSubmit.setText("回复");
			mAddress.setEnabled(false);
			mTitle.setEnabled(false);
			mContent.setEnabled(false);
			mBtnFace.setVisibility(View.GONE);
			mBtnKeyboard.setVisibility(View.GONE);
		}

	}

	/**
	 * 初始化适配器
	 */
	private void initAdapter() {
		// 获取BBS表情映射集合
		BBSFacesList = MyBBSFacesUtils.getBBSFacesList(this);
		// 创建适配器
		mAdapter = new FaceGridAdapter(this, BBSFacesList, R.layout.face_item);
		// 设置适配器
		mBBSFacesGridView.setAdapter(mAdapter);
	}

	/**
	 * 按钮单击统一监听器
	 */
	private OnClickListener mailSendClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mail_send_submit:
				// 发信
				sendNewMail();
				break;
			case R.id.mail_send_face:
				// 表情选择器显示
				facesGridViewShow(true);
				break;
			case R.id.mail_send_keyboard:
				// 表情选择器隐藏
				facesGridViewShow(false);
				break;
			case R.id.mail_send_back_icon:
				onBackPressed();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 输入框焦点变更统一监听器
	 */
	private OnFocusChangeListener mailSendEditFocusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.mail_send_address:
				// 焦点切入切出收件人EditText，都置表情切换按钮、键盘切换按钮处于不能点击状态
				mBtnFace.setEnabled(false);
				mBtnKeyboard.setEnabled(false);
				break;
			case R.id.mail_send_title:
				// 焦点切入切出标题EditText，都置表情切换按钮、键盘切换按钮处于不能点击状态
				mBtnFace.setEnabled(false);
				mBtnKeyboard.setEnabled(false);
				break;
			case R.id.mail_send_content:
				if (hasFocus) {
					// 焦点切入正文EditText，置表情切换按钮、键盘切换按钮处于可以点击状态
					mBtnFace.setEnabled(true);
					mBtnKeyboard.setEnabled(true);
				} else {
					// 取消表情选择器显示
					facesGridViewShow(false);
					// 焦点切出正文EditText，置表情切换按钮、键盘切换按钮处于不能点击状态
					mBtnFace.setEnabled(false);
					mBtnKeyboard.setEnabled(false);
				}
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 表情选择器显示与隐藏切换
	 * 
	 * @param bool
	 *            true，显示；false，隐藏
	 */
	private void facesGridViewShow(Boolean bool) {
		if (true == bool) {
			// 隐藏软键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mContent.getWindowToken(), 0);

			mBtnFace.setVisibility(View.GONE);
			mBtnKeyboard.setVisibility(View.VISIBLE);
			mBBSFacesGridView.setVisibility(View.VISIBLE);
		} else {
			// 显示软键盘
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.showSoftInput(mContent, 0);

			mBtnFace.setVisibility(View.VISIBLE);
			mBtnKeyboard.setVisibility(View.GONE);
			mBBSFacesGridView.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 取消显示等待对话框
				if (mProgress != null) {
					mProgress.dismiss();
					mProgress = null;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					// 返回结果
					String result = (String) msg.obj;
					// 发信失败
					if (result.contains("fail")) {
						Toast.makeText(MailSendActivity.this, "发信失败",
								Toast.LENGTH_SHORT).show();
					} else // 发信成功
					{
						Toast.makeText(MailSendActivity.this, "发信成功",
								Toast.LENGTH_SHORT).show();
						// 退出本Activity
						Intent mIntent = new Intent();
						setResult(RESULT_OK, mIntent);
						finish();
					}
					break;
				case MyConstants.REQUEST_FAIL:
					Toast.makeText(MailSendActivity.this, "发信失败",
							Toast.LENGTH_SHORT).show();
					break;
				}
				return;
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "MailSendActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "MailSendActivity");
	}

	/**
	 * 发信
	 */
	private void sendNewMail() {
		// 添加post请求参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		// destid收件人id , title信件标题, content信件内容,
		// signature使用第几个签名, [backup]是否保存到发件箱
		keys.add("destid");
		values.add(mAddress.getText().toString());
		keys.add("title");
		values.add(mTitle.getText().toString());
		keys.add("content");
		values.add(mContent.getText().toString());
		keys.add("signature");
		values.add("0");
		keys.add("backup");
		values.add("on");

		// post请求
		MyBBSRequest.mPost(MyConstants.POST_NEW_MAIL_URL, keys, values,
				"MailSendActivity", this);
		// 显示等待对话框
		if (null == mProgress) {
			mProgress = new ProgressHUDTask(this);
			mProgress.execute();
		}
	}
}
