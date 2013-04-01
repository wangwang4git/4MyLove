package com.bbs.whu.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.bbs.whu.handler.MessageHandlerManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * ����get��post����Ĺ����࣬����������صķ���
 * 
 * @author double
 * 
 */
public class MyBBSRequest {
	/**
	 * �޲�����get��������
	 * 
	 * @param url
	 *            �����URL��ַ
	 * @param activityName
	 *            ��Ϣ������activity
	 */
	public static void mGet(String url, final String activityName) {
		// get����
		MyHttpClient.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				// ���ͳɹ�����
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_SUCCESS, (Object) response,
						activityName);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// ����ʧ����Ϣ
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_FAIL, activityName);
			}
		});
	}

	/**
	 * ��������get������������л��棬ֱ�Ӷ����棬û�еĻ��������첽get���ʣ���ȡ����
	 * 
	 * @param url
	 *            �����URL��ַ
	 * @param keys
	 *            get����ʱ����key��
	 * @param values
	 *            get����ʱֵ��value��
	 * @param activityName
	 *            ��Ϣ������activity
	 * @param isForcingWebGet
	 *            �Ƿ�ǿ�ƴ������ȡ����
	 * @param context
	 *            Ӧ��������
	 */
	public static void mGet(String url, ArrayList<String> keys,
			ArrayList<String> values, final String activityName,
			boolean isForcingWebGet, Context context) {
		// �����ǿ�ƴ������ȡ���ȳ��Զ�����
		if (!isForcingWebGet) {
			// ��ȡ�����ļ���
			String cacheFileName = MyBBSCache.getCacheFileName(url, keys,
					values);
			// ���Զ�ȡ����
			String cacheContentString = MyBBSCache.getUrlCache(cacheFileName);
			// �ļ��ѻ��棬�������
			if (cacheContentString != null) {
				// ���ͻ�������
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_SUCCESS,
						(Object) cacheContentString, activityName);
				return;
			}
		}
		/*�첽get���ʣ���ȡ����*/
		// �������״̬
		if (checkNetwork(context)) {
			// �첽��get����
			get(url, keys, values, activityName, context);
		} else {
			MyNetworkUtils.setNetworkState(context);
		}
	}

	/**
	 * �첽��get����
	 * 
	 * @param url
	 *            �����URL��ַ
	 * @param keys
	 *            get����ʱ����key��
	 * @param values
	 *            get����ʱֵ��value��
	 * @param activityName
	 *            ��Ϣ������activity
	 * @param context
	 *            Ӧ��������
	 */
	private static void get(String url, ArrayList<String> keys,
			ArrayList<String> values, final String activityName, Context context) {
		// ��ȡ�����ļ���
		final String cacheFileName = MyBBSCache.getCacheFileName(url, keys,
				values);
		// ��ȡCookie
		PersistentCookieStore cookieStore = getCookieStore(context);
		MyHttpClient.setCookieStore(cookieStore);
		// ���get�������
		RequestParams params = new RequestParams();
		for (int i = 0; i < keys.size(); ++i) {
			params.put(keys.get(i), values.get(i));
		}
		// get����
		MyHttpClient.get(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				// д����
				MyBBSCache.setUrlCache(response, cacheFileName);
				// ��������
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_SUCCESS, response, activityName);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// ����ʧ����Ϣ
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_FAIL, activityName);
			}
		});
	}

	/**
	 * �������״̬������첽��post����
	 * 
	 * @param url
	 *            �����URL��ַ
	 * @param keys
	 *            get����ʱ����key��
	 * @param values
	 *            get����ʱֵ��value��
	 * @param activityName
	 *            ��Ϣ������activity
	 * @param context
	 *            Ӧ��������
	 */
	public static void mPost(String url, ArrayList<String> keys,
			ArrayList<String> values, final String activityName, Context context) {
		// �������״̬
		if (checkNetwork(context)) {
			// �첽��post����
			post(url, keys, values, activityName, context);
		} else {
			MyNetworkUtils.setNetworkState(context);
		}
	}

	/**
	 * �첽��post����
	 * 
	 * @param url
	 *            �����URL��ַ
	 * @param keys
	 *            get����ʱ����key��
	 * @param values
	 *            get����ʱֵ��value��
	 * @param activityName
	 *            ��Ϣ������activity
	 * @param context
	 *            Ӧ��������
	 */
	private static void post(String url, ArrayList<String> keys,
			ArrayList<String> values, final String activityName, Context context) {
		// ���post�������
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < keys.size(); ++i) {
			params.add(new BasicNameValuePair(keys.get(i), values.get(i)));
		}
		try {
			// ����ͨ��HttpEntity����ת�룬����post��ȥ�������ǡ�UTF-8������
			HttpEntity mHttpEntity = new UrlEncodedFormEntity(params, "GBK");
			// ��web�����в���
			String contentType = "application/x-www-form-urlencoded";
			// post����
			MyHttpClient.post(context, url, mHttpEntity, contentType,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String response) {
							// ���ͳɹ�����
							MessageHandlerManager.getInstance().sendMessage(
									MyConstants.REQUEST_SUCCESS, response,
									activityName);
						}

						@Override
						public void onFailure(Throwable error, String content) {
							// ����ʧ����Ϣ
							MessageHandlerManager.getInstance().sendMessage(
									MyConstants.REQUEST_FAIL, activityName);
						}
					});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ȡ����Ӧ���е�Cookie�洢��ȫ�ֱ���
	 * 
	 * @param context
	 *            Ӧ��������
	 * @return
	 */
	private static PersistentCookieStore getCookieStore(Context context) {
		return ((MyApplication) context.getApplicationContext())
				.getCookieStore();
	}

	/**
	 * �������״̬
	 * 
	 * @param context
	 *            Ӧ��������
	 * @return
	 *         ���������ӣ�����true�����򷵻�false
	 */
	private static boolean checkNetwork(Context context) {
		switch (MyNetworkUtils.getNetworkState(context)) {
		case MyNetworkUtils.NETWORN_NONE:
			return false;
		case MyNetworkUtils.NETWORN_WIFI:
			return true;
		case MyNetworkUtils.NETWORN_MOBILE:
			return true;
		default:
			return false;
		}
	}
}// end of class MyBBSRequest
