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
 * ��½��Activity��
 * ���ͨ���û��������½������post���󣬻�ȡCookie��
 * ���������½��ֱ�ӽ�����ҳ
 * 
 * @author double
 * 
 */
public class LoginActivity extends Activity implements OnClickListener {
	// �û��������
	private EditText userNameEditText;
	// ���������
	private EditText passwordEditText;
	// ȷ����ť
	private Button loginButton;
	// ������ť
	private Button anonymousButton;
	// �����������ݵ�handler
	Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// ��ʼ���ؼ�
		init();
		// ��ʼ��handler
		initHandler();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_button:
			// ��½
			login();
			break;
		case R.id.anonymous_button:
			// ��ת����ҳ
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			// �رյ�½ҳ��
			finish();
			break;
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
		// �û��������������
		userNameEditText = (EditText) findViewById(R.id.user_name_editText);
		passwordEditText = (EditText) findViewById(R.id.password_EditText);
		// �����û����������ʼֵ
		userNameEditText.setText(MyConstants.MY_USER_NAME);
		passwordEditText.setText(MyConstants.MY_PASSWORD);
		// ȷ����ť
		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
		// ������ť
		anonymousButton = (Button) findViewById(R.id.anonymous_button);
		anonymousButton.setOnClickListener(this);
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
					// ��ת����ҳ
					startActivity(new Intent(LoginActivity.this,
							MainActivity.class));
					// �رյ�½ҳ��
					finish();
					break;
				case MyConstants.REQUEST_FAIL:
					// ��ʾʧ��
					break;
				}
			}
		};
		// ע��handler
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_SUCCESS, "LoginActivity");
		MessageHandlerManager.getInstance().register(mHandler,
				MyConstants.REQUEST_FAIL, "LoginActivity");
	}

	/**
	 * ��½
	 */
	private void login() {
		// �½�CookieStore����
		PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
		// ��CookieStore��Ϊȫ�ֱ���
		((MyApplication) getApplicationContext()).setCookieStore(myCookieStore);
		// ���CookieStore
		MyHttpClient.setCookieStore(myCookieStore);
		// ���post�������
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("id");
		values.add(userNameEditText.getText().toString());
		keys.add("passwd");
		values.add(passwordEditText.getText().toString());
		keys.add("webtype");
		values.add("wforum");
		// �˲����������û���ε�¼
		keys.add("kick_multi");
		values.add("1");
		// post����
		MyBBSRequest.mPost(MyConstants.LOGIN_URL, keys, values,
				"LoginActivity", this);
	}
}
