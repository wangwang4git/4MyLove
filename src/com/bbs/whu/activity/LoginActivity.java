package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;

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
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.UserPasswordBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFileUtils;
import com.bbs.whu.utils.MyHttpClient;
import com.loopj.android.http.PersistentCookieStore;

/**
 * ��½��Activity�� ���ͨ���û��������½������post���󣬻�ȡCookie�� ���������½��ֱ�ӽ�����ҳ
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
	// �ȴ��Ի���
	private ProgressDialog mProgressDialog;
	// ȡ���ȴ��Ի���ʱ��ʶ�Ƿ��¼
	private boolean isLogin;
	// �û�����������б�
	private List<UserPasswordBean> userPasswords;

	// ���ɾ����userPasswords�еĵ�i���Ӧ�ĵ����Ի���ѯ���Ƿ�ɾ�������Ӧ�Ļ����ļ��У�ɾ���������£�
	// ɾ��/whubbs/data/cache/username/�ļ���
	// MyFileUtils.delFolder(MyFileUtils.getSdcardDataCacheDir(userPasswords.get(i).getName);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ��Activity��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		// ��ʼ���ؼ�
		init();
		// ��ʼ��handler
		initHandler();
		// ��¼ǰ����
		loginBefore();

		// �趨mProgressDialog������
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("��ʾ");
		mProgressDialog.setMessage("���ڵ�¼...");
		mProgressDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Log.d("Login", "ProgressDialog OnKeyDown called");
					if (mProgressDialog != null) {
						isLogin = false;
						mProgressDialog.dismiss();
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
			// ��¼ʱ�ĵȴ��Ի���
			mProgressDialog.show();
			// ��½
			login();
			break;
		case R.id.anonymous_button:
			// ��ת����ҳ
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			// ��½�����
			loginAfter();
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
		passwordEditText = (EditText) findViewById(R.id.password_editText);
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
					if (isLogin) {

						// ���CookieΪ�գ�˵���û����������
						List<Cookie> cookies = MyApplication.getInstance()
								.getCookieStore().getCookies();
						if (cookies.size() == 0) {
							// �رյȴ��Ի���
							mProgressDialog.dismiss();
							// �����û�
							Toast.makeText(LoginActivity.this, "�û������������",
									Toast.LENGTH_SHORT).show();
							return;
						}

						// ��ת����ҳ
						startActivity(new Intent(LoginActivity.this,
								MainActivity.class));
						// �رյȴ��Ի���
						mProgressDialog.dismiss();
						// ��½�����
						loginAfter();
						// �رյ�½ҳ��
						finish();
					} else {
						// ��¼�ɹ�����ȡ���˵ȴ��Ի������͵ǳ�����
						MyBBSRequest.mGet(MyConstants.LOG_OUT_URL,
								"LoginActivity");
					}
					break;
				case MyConstants.REQUEST_FAIL:
					// �رյȴ��Ի���
					mProgressDialog.dismiss();
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
		// ���û���������Ϊȫ�ֱ���
		((MyApplication) getApplicationContext()).setName(userNameEditText
				.getText().toString());
		((MyApplication) getApplicationContext()).setPassword(passwordEditText
				.getText().toString());
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
		// ���֧�ֶ�ε�¼����
		keys.add("kick_multi");
		values.add("1");
		// post����
		MyBBSRequest.mPost(MyConstants.LOGIN_URL, keys, values,
				"LoginActivity", this);
	}

	/**
	 * ��¼������������û����������б����л�
	 */
	private void loginAfter() {
		// ������ǰ�û����������Ƿ�������б���
		// ���������������������Ҳ������
		String loginName = ((MyApplication) getApplicationContext()).getName();
		String loginPassword = ((MyApplication) getApplicationContext())
				.getPassword();
		if (!loginName.equals(MyFileUtils.USERPASSWORDNAME)) {
			Boolean isExist = false;
			for (int i = 0; i < userPasswords.size(); ++i) {
				// ����û�����ͬ�������
				// ����û�����ͬ�����벻ͬ�����������
				if (userPasswords.get(i).getName().equals(loginName)) {
					if (!userPasswords.get(i).getPassword()
							.equals(loginPassword)) {
						// ��������
						userPasswords.get(i).setPassword(loginPassword);
					}
					isExist = true;
					break;
				}
			}
			// �����ڣ������
			if (!isExist) {
				UserPasswordBean bean = new UserPasswordBean();
				bean.setName(loginName);
				bean.setPassword(loginPassword);
				userPasswords.add(bean);
			}
		}

		// ���л����û���������json�ļ�
		MyBBSCache.setUserPasswordList(userPasswords,
				MyFileUtils.USERPASSWORDNAME);
	}

	/**
	 * ��¼ǰ���������������л��û���������json�ļ�
	 */
	private void loginBefore() {
		// �ȶ�ȡ�û���������json�ļ����ٷ����л�
		userPasswords = MyBBSCache
				.getUserPasswordList(MyFileUtils.USERPASSWORDNAME);
		// ���userPasswordsΪ�գ���ǰ���������û���������json�ļ�
		if (null == userPasswords) {
			userPasswords = new ArrayList<UserPasswordBean>();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("Login", "Activity OnKeyDown called");
		}
		return super.onKeyDown(keyCode, event);
	}
}
