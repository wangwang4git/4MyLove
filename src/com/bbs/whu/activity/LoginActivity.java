package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.UserPasswordBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyEncryptionDecryptionUtils;
import com.bbs.whu.utils.MyFileUtils;
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
	
	// 用户名、密码对列表
	private List<UserPasswordBean> userPasswords;
	// 如果删除了userPasswords中的第i项，相应的弹出对话框询问是否删除该项对应的缓存文件夹，删除方法如下：
	// 删除/whubbs/data/cache/username/文件夹
	// MyFileUtils.delFolder(MyFileUtils.getSdcardDataCacheDir(userPasswords.get(i).getName);
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取出Activity的title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		// 初始化控件
		init();
		// 初始化handler
		initHandler();
		// 登录前操作
		loginBefore();
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
			// 登陆后操作
			loginAfter();
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
					// 跳转的主页
					startActivity(new Intent(LoginActivity.this,
							MainActivity.class));
					// 登陆后操作
					loginAfter();
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
		// 将用户名密码设为全局变量
		((MyApplication) getApplicationContext()).setName(userNameEditText.getText().toString());
		((MyApplication) getApplicationContext()).setPassword(passwordEditText.getText().toString());
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
		//  添加支持多次登录参数
		keys.add("kick_multi");
		values.add("1");
		// post请求
		MyBBSRequest.mPost(MyConstants.LOGIN_URL, keys, values,
				"LoginActivity", this);
	}
	
	/**
	 * 登录后操作，包括用户名、密码列表序列化
	 */
	private void loginAfter() {
		// 遍历当前用户名、密码是否存在于列表中
		String loginName = ((MyApplication) getApplicationContext()).getName();
		String loginPassword = ((MyApplication) getApplicationContext())
				.getPassword();
		Boolean isExist = false;
		for (int i = 0; i < userPasswords.size(); ++i) {
			if (userPasswords.get(i).getName().equals(loginName)
					&& userPasswords.get(i).getPassword().equals(loginPassword)) {
				isExist = true;
				break;
			}
		}
		// 不存在，就添加
		if (!isExist) {
			UserPasswordBean bean = new UserPasswordBean();
			bean.setName(loginName);
			bean.setPassword(loginPassword);
			userPasswords.add(bean);
		}
		// 用户名，密码加密
		try {
			// 采用默认密钥
			MyEncryptionDecryptionUtils des = new MyEncryptionDecryptionUtils();
			for (int i = 0; i < userPasswords.size(); ++i) {
				String name = des.encrypt(userPasswords.get(i).getName());
				userPasswords.get(i).setName(name);
				String password = des.encrypt(userPasswords.get(i)
						.getPassword());
				userPasswords.get(i).setPassword(password);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 序列化到用户名、密码json文件
		MyBBSCache.setUserPasswordList(userPasswords,
				MyFileUtils.USERPASSWORDNAME);
	}

	/**
	 * 登录前操作，包括反序列化用户名、密码json文件
	 */
	private void loginBefore() {
		// 先读取用户名、密码json文件，再反序列化
		userPasswords = MyBBSCache
				.getUserPasswordList(MyFileUtils.USERPASSWORDNAME);
		// 如果userPasswords为空，当前还不存在用户名、密码json文件
		if (null == userPasswords) {
			userPasswords = new ArrayList<UserPasswordBean>();
		} else {
			// 用户名，密码解密
			try {
				// 采用默认密钥
				MyEncryptionDecryptionUtils des = new MyEncryptionDecryptionUtils();
				for (int i = 0; i < userPasswords.size(); ++i) {
					String name = des.decrypt(userPasswords.get(i).getName());
					userPasswords.get(i).setName(name);
					String password = des.decrypt(userPasswords.get(i)
							.getPassword());
					userPasswords.get(i).setPassword(password);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
