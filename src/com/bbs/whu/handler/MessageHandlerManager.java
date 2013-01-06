package com.bbs.whu.handler;

import android.os.Handler;
import android.util.Log;

public class MessageHandlerManager {
	// 单例模式
	private volatile static MessageHandlerManager messageHandlerManagerInstance = null;
	private MessageHandlerList messageHandlerList = null;

	/** 获取单例 */
	public static MessageHandlerManager getInstance() {
		// 检查实例,如是不存在就进入同步代码区
		if (messageHandlerManagerInstance == null) {
			// 对其进行锁,防止两个线程同时进入同步代码区
			synchronized (MessageHandlerManager.class) {
				// 必须双重检查
				if (messageHandlerManagerInstance == null) {
					messageHandlerManagerInstance = new MessageHandlerManager();
				}
			}
		}
		return messageHandlerManagerInstance;
	}

	/** 私有构造函数 */
	private MessageHandlerManager() {
		// 创建RegistrantList的单键实例
		if (messageHandlerList == null) {
			// 对其进行锁,防止两个线程同时进入同步代码区
			synchronized (MessageHandlerManager.class) {
				// 必须双重检查
				if (messageHandlerList == null) {
					messageHandlerList = new MessageHandlerList();
				}
			}
		}
	}

	public void printNum() {
		messageHandlerList.printNum();
	}

	// 注册，在注册时，最后提供类名
	public void register(Handler h, int what, String className) {
		messageHandlerList.add(h, what, className);
	}

	// 注销已what为消息标识的Registrant对象
	public void unregister(int what) {
		messageHandlerList.remove(what);
	}

	// 注销已what为消息标识，同时类名为class_name的Registrant对象
	public void unregister(int what, String className) {
		messageHandlerList.remove(what, className);
	}

	// 注销所有handler
	public void unregisterAll() {
		messageHandlerList.clear();
	}

	// 消息的组成分为what（消息标识），附带内容arg1，arg2，obj

	// 通知以What为消息标识的消息发生
	public void sendMessage(int what) {
		messageHandlerList.sendMessage(what, 0, 0, null, "");
	}

	// 通知以What为消息标识的消息发生，同时附带有arg1内容
	public void sendMessage(int what, int arg1) {
		messageHandlerList.sendMessage(what, arg1, 0, null, "");
	}

	// 通知以What为消息标识的消息发生，同时附带有obj内容
	public void sendMessage(int what, Object obj) {
		Log.i("what", Integer.toString(what));
		messageHandlerList.sendMessage(what, 0, 0, obj, "");
	}

	// 通知以What为消息标识的消息发生，同时附带有arg1，arg2内容
	public void sendMessage(int what, int arg1, int arg2) {
		messageHandlerList.sendMessage(what, arg1, arg2, null, "");
	}

	// 通知以What为消息标识的消息发生，同时附带有arg1，arg2，obj内容
	public void sendMessage(int what, int arg1, int arg2, int obj) {
		messageHandlerList.sendMessage(what, arg1, arg2, obj, "");
	}

	// 通知以What为消息标识的消息发生，这个消息只由类名为class_name的类处理
	public void sendMessage(int what, String class_name) {
		messageHandlerList.sendMessage(what, 0, 0, null, class_name);
	}

	// 通知以What为消息标识的消息发生，同时附带有arg1内容，这个消息只由类名为class_name的类处理
	public void sendMessage(int what, int arg1, String class_name) {
		messageHandlerList.sendMessage(what, arg1, 0, null, class_name);
	}

	// 通知以What为消息标识的消息发生，同时附带有obj内容，这个消息只由类名为class_name的类处理
	public void sendMessage(int what, Object obj, String class_name) {
		messageHandlerList.sendMessage(what, 0, 0, obj, class_name);
	}

	// 通知以What为消息标识的消息发生，同时附带有arg1，arg2内容，这个消息只由类名为class_name的类处理
	public void sendMessage(int what, int arg1, int arg2, String class_name) {
		messageHandlerList.sendMessage(what, arg1, arg2, null, class_name);
	}

	// 通知以What为消息标识的消息发生，同时附带有arg1，arg2，obj内容，这个消息只由类名为class_name的类处理
	public void sendMessage(int what, int arg1, int arg2, int obj,
			String class_name) {
		messageHandlerList.sendMessage(what, arg1, arg2, obj, class_name);
	}
}
