package com.bbs.whu.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
 * 登陆的Activity， 如果通过用户名密码登陆，进行post请求，获取Cookie， 如果匿名登陆，直接进入主页
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
	// 等待对话框
	private ProgressDialog mProgressDialog;
	// 取消等待对话框时标识是否登录
	private boolean isLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取出Activity的title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		// 初始化控件
		init();
		// 初始化handler
		initHandler();
		
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("提示");
		mProgressDialog.setMessage("正在登录..."); 
		mProgressDialog.setOnKeyListener(new OnKeyListener() { 
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Log.d("Login", "ProgressDialog OnKeyDown called");
					if (mProgressDialog != null) {
						isLogin = false;
						mProgressDialog.cancel();
						return true;
					}
				} 
				return false;
			}
		});  
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_button:
			isLogin = true;
			
			// 登录时的等待对话框
			mProgressDialog.show(); 
			
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
		passwordEditText = (EditText) findViewById(R.id.password_editText);
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
					if (isLogin) {
						// 跳转的主页
						startActivity(new Intent(LoginActivity.this,
								MainActivity.class));
						// 关闭等待对话框
						mProgressDialog.dismiss();
						// 关闭登陆页面
						finish();
					} else {
						// 取消了等待对话框，则取消登录
					}
					break;
				case MyConstants.REQUEST_FAIL:
					// 关闭等待对话框
					mProgressDialog.dismiss();
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
		// 将用户名密码设为全局变量
		((MyApplication) getApplicationContext()).setName(userNameEditText
				.getText().toString());
		((MyApplication) getApplicationContext()).setPassword(passwordEditText
				.getText().toString());
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
		// 添加支持多次登录参数
		keys.add("kick_multi");
		values.add("1");
		// post请求
		MyBBSRequest.mPost(MyConstants.LOGIN_URL, keys, values,
				"LoginActivity", this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("Login", "Activity OnKeyDown called");
		}
		return super.onKeyDown(keyCode, event);
	}
}
