package com.bbs.whu.handler;

import java.util.ArrayList;
import android.os.Handler;

public class MessageHandlerList {
	private ArrayList<MessageHandler> messageHandlerList = new ArrayList<MessageHandler>();

	public synchronized void printNum() {

	}

	/** 对应MessageHandler是否已添加 */
	protected synchronized boolean exist(Handler handler, int what, String name) {
		for (int i = 0; i < messageHandlerList.size(); i++) {
			if (messageHandlerList.get(i).valueEqual(handler, what, name))
				return true;
		}
		return false;
	}

	/** 对应MessageHandler是否已添加 */
	protected synchronized boolean exist(MessageHandler messageHandler) {
		for (int i = 0; i < messageHandlerList.size(); i++) {
			if (messageHandlerList.get(i).valueEqual(messageHandler))
				return true;
		}
		return false;
	}

	/** 添加新成员 */
	public synchronized void add(Handler handler, int what, String className) {
		if (!exist(handler, what, className))
			messageHandlerList
					.add(new MessageHandler(handler, what, className));
	}

	/** 根据what删除成员 */
	public synchronized void remove(int what) {
		remove(what, "");
	}

	/** 根据what和类名删除成员 */
	public synchronized void remove(int what, String className) {
		for (int i = 0; i < messageHandlerList.size(); i++) {
			MessageHandler messageHandler = messageHandlerList.get(i);
			if (messageHandler.getMessageWhat() == what
					&& ((className.equals("") || className == null) ? true
							: messageHandler.getClassName().equals(className))) {
				messageHandler.clear();
				messageHandlerList.remove(i);
			}
		}
	}

	/** 删除所有成员 */
	public synchronized void clear() {
		while (!messageHandlerList.isEmpty()) {
			messageHandlerList.get(0).clear();
			messageHandlerList.remove(0);
		}
	}

	/** 根据类名className和消息标志what，通知相应的类处理消息，若className为空，则只匹配what */
	public synchronized void sendMessage(int what, int arg1, int arg2,
			Object obj, String className) {
		for (int i = 0; i < messageHandlerList.size(); i++) {
			MessageHandler messageHandler = messageHandlerList.get(i);
			if (messageHandler.getMessageWhat() == what
					&& ((className.equals("") || className == null) ? true
							: messageHandler.getClassName().equals(className))) {
				messageHandler.sendMessage(arg1, arg2, obj);
			}
		}
	}
}
