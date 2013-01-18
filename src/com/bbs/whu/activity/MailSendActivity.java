package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
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
import android.widget.TextView;

import com.bbs.whu.R;
import com.bbs.whu.adapter.FaceGridAdapter;
import com.bbs.whu.model.FaceBean;
import com.bbs.whu.utils.MyBBSFacesUtils;

/**
 * 邮件发送
 * 
 * @author WWang
 * 
 */
public class MailSendActivity extends Activity {
	// 收件人
	private EditText mAddressee;
	// 标题
	private EditText mTitle;
	// 邮件正文
	private EditText mContent;
	// 邮件发送按钮
	private TextView mSendSubmit;
	
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

		// 初始化控件
		initView();
		// 初始化适配器
		initAdapter();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		mAddressee = (EditText) findViewById(R.id.mail_send_addressee);
		// 设置输入框焦点变更统一监听器
		mAddressee.setOnFocusChangeListener(mailSendEditFocusListener);

		mTitle = (EditText) findViewById(R.id.mail_send_title);
		// 设置输入框焦点变更统一监听器
		mTitle.setOnFocusChangeListener(mailSendEditFocusListener);

		mContent = (EditText) findViewById(R.id.mail_send_content);
		// 设置输入框焦点变更统一监听器
		mContent.setOnFocusChangeListener(mailSendEditFocusListener);

		mSendSubmit = (TextView) findViewById(R.id.mail_send_submit);
		// 设置按钮统一监听器
		mSendSubmit.setOnClickListener(mailSendClickListener);

		mBtnFace = (ImageButton) findViewById(R.id.mail_send_face);
		// 设置按钮统一监听器
		mBtnFace.setOnClickListener(mailSendClickListener);
		mBtnFace.setEnabled(false);

		mBtnKeyboard = (ImageButton) findViewById(R.id.mail_send_keyboard);
		// 设置按钮统一监听器
		mBtnKeyboard.setOnClickListener(mailSendClickListener);
		mBtnKeyboard.setEnabled(false);

		mBBSFacesGridView = (GridView) findViewById(R.id.mail_send_grid_face);
		// 设置监听器
		mBBSFacesGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 插入表情名称
				mContent.append(BBSFacesList.get(arg2).getName());
			}
		});
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
				break;
			case R.id.mail_send_face:
				// 表情选择器显示
				facesGridViewShow(true);
				break;
			case R.id.mail_send_keyboard:
				// 表情选择器隐藏
				facesGridViewShow(false);
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
			case R.id.mail_send_addressee:
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
}
