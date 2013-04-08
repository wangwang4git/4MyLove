package com.bbs.whu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bbs.whu.R;
import com.bbs.whu.adapter.AdvancedAutoCompleteAdapter;
import com.bbs.whu.adapter.LoginOptionsAdapter;
import com.bbs.whu.handler.MessageHandlerManager;
import com.bbs.whu.model.UserPasswordBean;
import com.bbs.whu.utils.MyApplication;
import com.bbs.whu.utils.MyBBSCache;
import com.bbs.whu.utils.MyBBSRequest;
import com.bbs.whu.utils.MyConstants;
import com.bbs.whu.utils.MyFileUtils;
import com.bbs.whu.utils.MyFontManager;
import com.bbs.whu.utils.MyHttpClient;
import com.bbs.whu.utils.MyWaitDialog;
import com.loopj.android.http.PersistentCookieStore;

/**
 * ��½��Activity�� ���ͨ���û��������½������post���󣬻�ȡCookie�� ���������½��ֱ�ӽ�����ҳ
 * 
 * @author double
 * 
 */

public class LoginActivity extends Activity implements OnClickListener {
	// �û��������
	private AutoCompleteTextView userNameEditText;
	private AdvancedAutoCompleteAdapter mAdapter;
	// ���������
	private EditText passwordEditText;
	// ȷ����ť
	private Button loginButton;
	// ������ť
	private Button anonymousButton;
	// ��ס����checkbox
	private CheckBox rememberUserCheckBox;
	// ��ס�����־
	private boolean rememberUserFlag = true;
	// �����������ݵ�handler
	Handler mHandler;

	// ��¼ʱ�ĵȴ��Ի���
	private MyWaitDialog loginWaitDialog;
	// PopupWindow����
	private PopupWindow selectPopupWindow = null;
	// �Զ���Adapter
	private LoginOptionsAdapter optionsAdapter = null;
	// ������ѡ������Դ
	private ArrayList<String> datas = new ArrayList<String>();
	// �������������
	private LinearLayout parent;
	// ���������������ȣ�Ҳ����Ϊ������Ŀ��
	private int pwidth;
	// ������ͷͼƬ���
	private ImageView selectImg;
	// չʾ��������ѡ���ListView
	private ListView listView = null;
	// �Ƿ��ʼ����ɱ�־
	private boolean flag = false;

	// �û�����������б�
	private ArrayList<UserPasswordBean> userPasswords;
	// ɾ���˺Ż����ļ���ȷ�϶Ի���
	private AlertDialog.Builder deleteConfirmDlg;
	// ѡ�е�Ҫɾ������
	private int delIndex;

	// ��¼Ӧ���Ƿ�Ϊ��һ��ʹ��
	private SharedPreferences firstUseSP;
	private SharedPreferences.Editor firstUseEditor;
	private String firstUseSPKey = "isUsed";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ȡ��Activity��title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		MyFontManager.changeFontType(this);// ���õ�ǰActivity������

		// �ж��Ƿ�Ϊ��һ��ʹ��
		firstUseSP = this.getSharedPreferences("firstUse", MODE_WORLD_READABLE);

		// ���Ϊ��һ��ʹ��������SharedPreferences��������ת�����ֵ���ҳ
		if (!firstUseSP.contains(firstUseSPKey)) {
			firstUseEditor = firstUseSP.edit();
			firstUseEditor.putInt(firstUseSPKey, 1);// 1Ϊʹ�ù�
			firstUseEditor.commit(); // �ύ

			// ��ת�����ֵ���ҳ
			Intent intent = new Intent(LoginActivity.this,
					BeginnerNavigationActivity.class);
			LoginActivity.this.startActivity(intent);
		}

		// ��¼ǰ����
		loginBefore();
		// ��ʼ���ؼ�
		init();
		// ��ʼ��handler
		initHandler();

		// �趨loginWaitDialog������
		// loginWaitDialog = new WaitDialog(LoginActivity.this, "��ʾ",
		// "���ڵ�¼...");
		loginWaitDialog = new MyWaitDialog(LoginActivity.this, "��¼",
				"���ڵ�¼���԰�����......");
	}

	@Override
	public void onStart() {
		super.onStart();
		if (MyApplication.getInstance().isExit())
			finish();
		// ����û���������
		userNameEditText.setText("");
		passwordEditText.setText("");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ע��handler
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_SUCCESS, "LoginActivity");
		MessageHandlerManager.getInstance().unregister(
				MyConstants.REQUEST_FAIL, "LoginActivity");
		// ��������
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.login_button:
			// ��¼ʱ�ĵȴ��Ի���
			loginWaitDialog.show();
			// ��½
			login();
			break;
		case R.id.anonymous_button:
			// ��ת����ҳ
			startActivity(new Intent(LoginActivity.this, MainActivity.class));
			// ��½�����
			loginAfter();
			// �رյ�½ҳ��
			// finish();
			break;
		}
	}

	/**
	 * ��ʼ���ؼ�
	 */
	private void init() {
		// �û��������������
		userNameEditText = (AutoCompleteTextView) findViewById(R.id.user_name_editText);
		passwordEditText = (EditText) findViewById(R.id.password_editText);
		// �����û����������ʼֵ
		// userNameEditText.setText(MyConstants.MY_USER_NAME);
		// passwordEditText.setText(MyConstants.MY_PASSWORD);

		// ����userNameEditText������
		mAdapter = new AdvancedAutoCompleteAdapter(this, userPasswords, 10);
		userNameEditText.setAdapter(mAdapter);

		// ����userNameEditText������
		userNameEditText.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				passwordEditText.setText(userPasswords.get(arg2).getPassword());
			}
		});

		// ȷ����ť
		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(this);
		// ������ť
		anonymousButton = (Button) findViewById(R.id.anonymous_button);
		anonymousButton.setOnClickListener(this);

		// ��ס����checkbox
		rememberUserCheckBox = (CheckBox) findViewById(R.id.remember_user_checkBox);
		rememberUserCheckBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							// ѡ��
							rememberUserFlag = true;
						} else {
							// δѡ��
							rememberUserFlag = false;
						}
					}
				});

		// ɾ���˺Ż����ļ���ȷ�϶Ի���
		deleteConfirmDlg = new Builder(LoginActivity.this);
		deleteConfirmDlg.setMessage("ȷ��ɾ�����˻��Ļ����ļ���");
		deleteConfirmDlg.setTitle("��ʾ");
		deleteConfirmDlg.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						datas.remove(delIndex);

						// ɾ�������ļ�
						MyFileUtils.delFolder(MyFileUtils
								.getSdcardDataCacheDir(userPasswords.get(
										delIndex).getName()));

						// ɾ��userPasswords�е��˻�
						userPasswords.remove(delIndex);

						// ���tempRefreshJson
						ArrayList<UserPasswordBean> tempRefreshJson = new ArrayList<UserPasswordBean>();
						for (UserPasswordBean tempAdd : userPasswords) {
							UserPasswordBean tempUser = new UserPasswordBean();
							tempUser.setName(tempAdd.getName());
							tempUser.setPassword(tempAdd.getPassword());
							tempRefreshJson.add(tempUser);
						}

						// ���л����û���������json�ļ�
						MyBBSCache.setUserPasswordList(tempRefreshJson,
								MyFileUtils.USERPASSWORDNAME);

						// ˢ�������б�
						optionsAdapter.notifyDataSetChanged();

						dialog.dismiss();
					}
				});

		deleteConfirmDlg.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	}

	/**
	 * ��ʼ��handler
	 */
	private void initHandler() {
		// ��ʼ��handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle data = msg.getData();
				switch (msg.what) {
				case MyConstants.LOGIN_SELECT_USER:
					// ѡ���������������ʧ
					int selIndex = data.getInt("selIndex");
					userNameEditText.setText(datas.get(selIndex));
					passwordEditText.setText(userPasswords.get(selIndex)
							.getPassword());
					dismiss();
					break;
				case MyConstants.LOGIN_DELETE_USER:
					// �Ƴ�����������
					delIndex = data.getInt("delIndex");

					// ����ɾ���˺Ż����ļ���ȷ�϶Ի���
					deleteConfirmDlg.create().show();
					break;
				case MyConstants.REQUEST_SUCCESS:
					if (loginWaitDialog.mStatus == MyWaitDialog.SHOWING) {

						// ���CookieΪ�գ�˵���û����������
						List<Cookie> cookies = MyApplication.getInstance()
								.getCookieStore().getCookies();
						if (cookies.size() == 0) {
							// �رյȴ��Ի���
							loginWaitDialog.cancel();
							// �����û�
							Toast.makeText(LoginActivity.this, "�û������������",
									Toast.LENGTH_SHORT).show();
							return;
						}

						// ��¼�ɹ��󣬽��û���������Ϊȫ�ֱ���
						((MyApplication) getApplicationContext())
								.setName(userNameEditText.getText().toString());
						((MyApplication) getApplicationContext())
								.setPassword(passwordEditText.getText()
										.toString());

						// ��ת����ҳ
						startActivity(new Intent(LoginActivity.this,
								MainActivity.class));
						// �رյȴ��Ի���
						loginWaitDialog.cancel();
						// ��½�����
						loginAfter();
						// �رյ�½ҳ��
						// finish();
					} else if (loginWaitDialog.mStatus == MyWaitDialog.CANCELLED) {
						// ��¼�ɹ�����ȡ���˵ȴ��Ի������͵ǳ�����
						MyBBSRequest.mGet(MyConstants.LOG_OUT_URL,
								"MainActivity");// ����ΪMainActivity��Ϊ�˲������˳�����Ӧ�¼�
					}
					break;
				case MyConstants.REQUEST_FAIL:
					// �رյȴ��Ի���
					loginWaitDialog.cancel();
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
		// ���֧�ֶ�ε�¼����
		keys.add("kick_multi");
		values.add("1");
		// post����
		MyBBSRequest.mPost(MyConstants.LOGIN_URL, keys, values,
				"LoginActivity", this);
		// ���õ�¼URL 2.0�ӿڵ�¼
		// keys.add("app");
		// values.add("login");
		// keys.add("id");
		// values.add(userNameEditText.getText().toString());
		// keys.add("passwd");
		// values.add(passwordEditText.getText().toString());
		// // post����
		// MyBBSRequest.mPost(MyConstants.LOGIN_URL_2, keys, values,
		// "LoginActivity", this);
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
		// ���δѡ�м�ס���룬�򲻱�������
		if (!rememberUserFlag) {
			for (int i = 0; i < userPasswords.size(); ++i) {
				if (userPasswords.get(i).getName().equals(loginName)) {
					userPasswords.get(i).setPassword("");
					break;
				}
			}
		}
		MyBBSCache.setUserPasswordList(userPasswords,
				MyFileUtils.USERPASSWORDNAME);

		// ��ȡ�Ѷ����json�ļ����������л�
		HashMap<String, Byte> readedTagMap = MyBBSCache
				.getReadedTagMap(MyFileUtils.READEDTAGNAME);
		// ���readedTagMapΪ�գ���ǰ���������Ѷ����json�ļ�
		if (null == readedTagMap) {
			readedTagMap = new HashMap<String, Byte>();
		}
		// ����ȫ������
		((MyApplication) getApplicationContext()).setReadedTag(readedTagMap);
	}

	/**
	 * ��¼ǰ���������������л��û���������json�ļ����Ѷ����json�ļ�
	 */
	private void loginBefore() {
		// ��ȡ�û���������json�ļ����������л�
		userPasswords = (ArrayList<UserPasswordBean>) MyBBSCache
				.getUserPasswordList(MyFileUtils.USERPASSWORDNAME);
		// ���userPasswordsΪ�գ���ǰ���������û���������json�ļ�
		if (null == userPasswords) {
			userPasswords = new ArrayList<UserPasswordBean>();
		}
	}

	/**
	 * û����onCreate�����е���initWedget()��������onWindowFocusChanged�����е��ã�
	 * ����ΪinitWedget()����Ҫ��ȡPopupWindow���������������������ȣ���onCreate���������޷���ȡ���ÿ�ȵ�
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		while (!flag) {
			initWedget();
			flag = true;
		}

	}

	/**
	 * ��ʼ�������б�ؼ�
	 */
	private void initWedget() {
		// ��ʼ���������
		parent = (LinearLayout) findViewById(R.id.login_user_parent);
		userNameEditText = (AutoCompleteTextView) findViewById(R.id.user_name_editText);
		passwordEditText = (EditText) findViewById(R.id.password_editText);
		selectImg = (ImageView) findViewById(R.id.btn__multiple_user_select);

		// ��ȡ������������������
		int width = parent.getWidth();
		pwidth = width;

		// ���õ��������ͷͼƬ�¼����������PopupWindow����������
		selectImg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					// ��ʾPopupWindow����
					popupWindwShowing();
				}
			}
		});

		// ��ʼ��PopupWindow
		initPopuWindow();
	}

	/**
	 * ��ʼ�����LoginOptionsAdapter����List����
	 */
	private void initDatas() {
		// ���datas
		datas.clear();

		for (int i = 0; i < userPasswords.size(); i++) {
			UserPasswordBean temp = userPasswords.get(i);
			datas.add(temp.getName());
		}
	}

	/**
	 * ��ʼ��PopupWindow
	 */
	private void initPopuWindow() {

		initDatas();

		// PopupWindow���������򲼾�
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.login_options, null);
		listView = (ListView) loginwindow.findViewById(R.id.user_list);

		// �����Զ���Adapter
		optionsAdapter = new LoginOptionsAdapter(this, mHandler, datas);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);

		selectPopupWindow.setOutsideTouchable(true);

		// ��һ����Ϊ��ʵ�ֵ���PopupWindow�󣬵������Ļ�������ּ�Back��ʱPopupWindow����ʧ��
		// û����һ����Ч�����ܳ�������������Ӱ�챳��
		// ���������������ޣ���������ԭ�򣬻������֡�֪����ָ��һ��
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * ��ʾPopupWindow����
	 * 
	 * @param popupwindow
	 */
	public void popupWindwShowing() {
		// ��selectPopupWindow��Ϊparent����������ʾ����ָ��selectPopupWindow��Y����������ƫ��3pix��
		// ����Ϊ�˷�ֹ���������ı���֮�������϶��Ӱ���������
		// ���Ƿ�������϶����������϶�Ĵ�С�����ܻ���ݻ��͡�Androidϵͳ�汾��ͬ����ɣ���̫�����
		selectPopupWindow.showAsDropDown(parent, 0, -3);
	}

	/**
	 * PopupWindow��ʧ
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d("Login", "Activity OnKeyDown called");
		}
		return super.onKeyDown(keyCode, event);
	}
}
