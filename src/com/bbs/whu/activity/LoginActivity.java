package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyHttpClient;
import com.loopj.android.http.PersistentCookieStore;

/**
 * 登陆的Activity，
 * 如果通过用户名密码登陆，进行post请求，获取Cookie，
 * 如果匿名登陆，直接进入主页
 * 
 * @author double
 * 
 */
public class LoginActivity extends Activity implements OnClickListener {
	// 用户名输入框
	private EditText userNameEditText;
	// 密码输入框
	private EditText passwordEditText;
	// 确定按钮
	private Button loginButton;
	// 匿名按钮
	private Button anonymousButton;
	// 接收请求数据的handler
	Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 初始化控件
		init();
		// 初始化handler
		initHandler();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_button:
			// 登陆
			login();
			break;
		case R.id.anonymous_button:
			// 跳转的主页
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			// 关闭登陆页面
			finish();
			break;
		}
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		// 用户名、密码输入框
		userNameEditText = (EditText) findViewById(R.id.user_name_editText);
		passwordEditText = (EditText) findViewById(R.id.password_EditText);
		// 设置用户名、密码初始值
		userNameEditText.setText(MyConstants.MY_USER_NAME);
		passwordEditText.setText(MyConstants.MY_PASSWORD);
		// 确定按钮
		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
		// 匿名按钮
		anonymousButton = (Button) findViewById(R.id.anonymous_button);
		anonymousButton.setOnClickListener(this);
	}

	/**
	 * 初始化handler
	 */
	private void initHandler() {
		// 初始化handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MyConstants.REQUEST_SUCCESS:
					// 跳转的主页
					startActivity(new Intent(LoginActivity.this,
							MainActivity.class));
					// 关闭登陆页面
					finish();
					break;
				case MyConstants.REQUEST_FAIL:
					// 提示失败
					break;
				}
			}
		};
		// 注册handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "LoginActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "LoginActivity");
	}

	/**
	 * 登陆
	 */
	private void login() {
		// 新建CookieStore对象
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		// 将CookieStore设为全局变量
		((MyApplication) getApplicationContext()).setCookieStore(myCookieStore);
		// 添加CookieStore
		MyHttpClient.setCookieStore(myCookieStore);
		// 添加post请求参数
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("id");
		values.add(userNameEditText.getText().toString());
		keys.add("passwd");
		values.add(passwordEditText.getText().toString());
		keys.add("webtype");
		values.add("wforum");
		// post请求
		MyBBSRequest.mPost(MyConstants.LOGIN_URL, keys, values,
				"LoginActivity", this);
	}
}
