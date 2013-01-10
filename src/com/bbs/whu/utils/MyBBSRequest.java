package com.bbs.whu.utils;

import java.util.ArrayList;

import android.content.Context;

import com.bbs.whu.handler.MessageHandlerManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * 进行get，post请求的工具类，包括多个重载的方法
 * 
 * @author double
 * 
 */
public class MyBBSRequest {
	/**
	 * 无参数的get数据请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param activityName
	 *            消息发往的activity
	 */
	public static void mGet(String url, final String activityName) {
		// get请求
		MyHttpClient.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				// 发送成功内容
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_SUCCESS, (Object) response,
						activityName);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// 发送失败消息
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_FAIL, activityName);
			}
		});
	}

	/**
	 * 带参数的get数据请求，如果有缓存，直接读缓存，没有的话，进行异步get访问，获取数据
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param keys
	 *            get请求时键（key）
	 * @param values
	 *            get请求时值（value）
	 * @param activityName
	 *            消息发往的activity
	 * @param isForcingWebGet
	 *            是否强制从网络获取数据
	 * @param context
	 *            应用上下文
	 */
	public static void mGet(String url, ArrayList<String> keys,
			ArrayList<String> values, final String activityName,
			boolean isForcingWebGet, Context context) {
		// 如果非强制从网络获取，先尝试读缓存
		if (!isForcingWebGet) {
			// 获取缓存文件名
			String cacheFileName = getCacheFileName(url, keys, values);
			// 尝试读取缓存
			String cacheContentString = MyBBSCache.getUrlCache(cacheFileName);
			// 文件已缓存，则读缓存
			if (cacheContentString != null) {
				// 发送缓存内容
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_SUCCESS,
						(Object) cacheContentString, activityName);
				return;
			}
		}
		/*异步get访问，获取数据*/
		// 检测网络状态
		if (checkNetwork(context)) {
			// 异步的get请求
			get(url, keys, values, activityName, context);
		} else {
			MyNetworkUtils.setNetworkState(context);
		}
	}

	/**
	 * 异步的get请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param keys
	 *            get请求时键（key）
	 * @param values
	 *            get请求时值（value）
	 * @param activityName
	 *            消息发往的activity
	 * @param context
	 *            应用上下文
	 */
	private static void get(String url, ArrayList<String> keys,
			ArrayList<String> values, final String activityName, Context context) {
		// 获取缓存文件名
		final String cacheFileName = getCacheFileName(url, keys, values);
		// 获取Cookie
		PersistentCookieStore cookieStore = getCookieStore(context);
		MyHttpClient.setCookieStore(cookieStore);
		// 添加get请求参数
		RequestParams params = new RequestParams();
		for (int i = 0; i < keys.size(); ++i) {
			params.put(keys.get(i), values.get(i));
		}
		// get请求
		MyHttpClient.get(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				// 写缓存
				MyBBSCache.setUrlCache(response, cacheFileName);
				// 发送内容
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_SUCCESS, response, activityName);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// 发送失败消息
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_FAIL, activityName);
			}
		});
	}

	/**
	 * 检测网络状态后进行异步的post请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param keys
	 *            get请求时键（key）
	 * @param values
	 *            get请求时值（value）
	 * @param activityName
	 *            消息发往的activity
	 * @param context
	 *            应用上下文
	 */
	public static void mPost(String url, ArrayList<String> keys,
			ArrayList<String> values, final String activityName, Context context) {
		// 检测网络状态
		if (checkNetwork(context)) {
			// 异步的get请求
			post(url, keys, values, activityName, context);
		} else {
			MyNetworkUtils.setNetworkState(context);
		}
	}

	/**
	 * 异步的post请求
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param keys
	 *            get请求时键（key）
	 * @param values
	 *            get请求时值（value）
	 * @param activityName
	 *            消息发往的activity
	 * @param context
	 *            应用上下文
	 */
	private static void post(String url, ArrayList<String> keys,
			ArrayList<String> values, final String activityName, Context context) {
		// 添加post请求参数
		RequestParams params = new RequestParams();
		for (int i = 0; i < keys.size(); ++i) {
			// try {
			// params.put(new String(keys.get(i).getBytes("GBK"), "utf-8"),
			// new String(values.get(i).getBytes("GBK"), "utf-8"));
			//
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// }
			params.put(keys.get(i), values.get(i));
		}
		// post请求
		MyHttpClient.post(url, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) {
				// 发送成功内容
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_SUCCESS, activityName);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// 发送失败消息
				MessageHandlerManager.getInstance().sendMessage(
						MyConstants.REQUEST_FAIL, activityName);
			}
		});
	}

	/**
	 * 构造缓存文件名
	 * 
	 * @param url
	 *            请求的URL地址
	 * @param keys
	 *            get请求时键（key）
	 * @param values
	 *            get请求时值（value）
	 * @return 缓存文件名，格式为：URL+get参数+Cookie+用户名
	 */
	private static String getCacheFileName(String url, ArrayList<String> keys,
			ArrayList<String> values) {
		// 缓存文件名中的参数部分
		StringBuilder paramsString = new StringBuilder();
		/* 构造 get请求的参数String */
		for (int i = 0; i < keys.size(); ++i) {
			paramsString.append(keys.get(i));
			paramsString.append(values.get(i));
		}
		// 返回构造的缓存文件名
		return url + paramsString.toString() + MyConstants.MY_USER_NAME;
	}

	/**
	 * 获取整个应用中的Cookie存储器全局变量
	 * 
	 * @param context
	 *            应用上下文
	 * @return
	 */
	private static PersistentCookieStore getCookieStore(Context context) {
		return ((MyApplication) context.getApplicationContext())
				.getCookieStore();
	}

	/**
	 * 检测网络状态
	 * 
	 * @param context
	 *            应用上下文
	 * @return
	 *         有网络连接，返回true，否则返回false
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
