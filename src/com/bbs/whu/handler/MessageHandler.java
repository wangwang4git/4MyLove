package com.bbs.whu.handler;

import android.os.Handler;
import android.os.Message;

public class MessageHandler {
	private Handler handler; // 处理消息的句柄
	private int what; // 消息ID
	private String className; // 处理消息的类名称

	MessageHandler(Handler handler, int what, String name) {
		this.handler = handler;
		this.what = what;
		this.className = name;
	}

	@Override
	protected void finalize() {
		clear();
	}

	/** 值相等判断 */
	public boolean valueEqual(Handler handler, int what, String name) {
		if (this.handler.equals(handler) && this.what == what
				&& this.className.equals(name))
			return true;
		return false;
	}

	/** 值相等判断 */
	public boolean valueEqual(MessageHandler messageHandler) {
		return valueEqual(messageHandler.handler, messageHandler.what,
				messageHandler.className);
	}

	/** 通知handler，其关心的事情发生，需要相应的界面处理 */
	void sendMessage(int arg1, int arg2, Object obj) {
		if (handler == null)
			return;
		Message message = Message.obtain();
		message.what = what;
		message.obj = obj;
		message.arg1 = arg1;
		message.arg2 = arg2;
		handler.sendMessage(message);
	}

	/** 清除 */
	void clear() {
		handler = null;
		className = null;
	}

	/** 获取消息ID */
	int getMessageWhat() {
		return what;
	}

	/** 获取处理消息的类名 */
	String getClassName() {
		return className == null ? "" : className;
	}

}
