package com.bbs.whu.model;

import com.bbs.whu.model.mail.isnew;
import com.bbs.whu.model.mail.sender;
import com.bbs.whu.model.mail.size;
import com.bbs.whu.model.mail.time;

/**
 * 邮件信息元素结构定义
 * 
 * @author WWang
 * 
 */
public class MailBean {
	// 发信人
	private sender sender;
	// 新邮件标记
	private isnew isnew;
	// 邮件ID
	private size size;
	// 发信时间
	private time time;
	// 邮件标题
	private String title;

	public MailBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MailBean(com.bbs.whu.model.mail.sender sender, com.bbs.whu.model.mail.isnew isnew,
			com.bbs.whu.model.mail.size size, com.bbs.whu.model.mail.time time, String title) {
		super();
		this.sender = sender;
		this.isnew = isnew;
		this.size = size;
		this.time = time;
		this.title = title;
	}

	public sender getSender() {
		return sender;
	}

	public void setSender(sender sender) {
		this.sender = sender;
	}

	public isnew getIsnew() {
		return isnew;
	}

	public void setIsnew(isnew isnew) {
		this.isnew = isnew;
	}

	public size getSize() {
		return size;
	}

	public void setSize(size size) {
		this.size = size;
	}

	public time getTime() {
		return time;
	}

	public void setTime(time time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "MailBean [sender=" + sender + ", isnew=" + isnew + ", size="
				+ size + ", time=" + time + ", title=" + title + "]";
	}
}
