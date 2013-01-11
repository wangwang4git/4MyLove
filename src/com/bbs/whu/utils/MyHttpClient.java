package com.bbs.whu.utils;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * HTTP�첽����Ĺ����࣬�ṩpost�����get����ע�⣬������static���������AsyncHttpClient��,
 * ��������������ͬһʱ��ֻ��һ��HTTP����
 * 
 * @author double
 * 
 */
public class MyHttpClient {
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static void post(Context context, String url, HttpEntity httpEntity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		client.post(context, url, httpEntity, contentType, responseHandler);
	}

	public static void setCookieStore(PersistentCookieStore mCookieStore) {
		client.setCookieStore(mCookieStore);
	}

}
