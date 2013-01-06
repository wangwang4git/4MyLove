package com.bbs.whu.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * HTTP异步请求的工具类，提供post请求和get请求，注意，采用了static的请求对象（AsyncHttpClient）,
 * 这样整个程序中同一时间只有一个HTTP请求
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

	public static void setCookieStore(PersistentCookieStore mCookieStore) {
		client.setCookieStore(mCookieStore);
	}

}
