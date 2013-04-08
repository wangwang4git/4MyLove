package com.bbs.whu.model.mail;

import com.bbs.whu.model.attr.num;

public class MailContent {
	// 邮件索引值
	private num num;
	// 发信人
	private sender sender;
	// 新邮件标记
	private isnew isnew;
	// 邮件标题
	private title title;
	// 邮件大小
	private size size;
	// 发信时间
	private time time;
	// 邮件正文
	private String content;
	
	public MailContent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MailContent(com.bbs.whu.model.attr.num num,
			com.bbs.whu.model.mail.sender sender,
			com.bbs.whu.model.mail.isnew isnew,
			com.bbs.whu.model.mail.title title,
			com.bbs.whu.model.mail.size size, com.bbs.whu.model.mail.time time,
			String content) {
		super();
		this.num = num;
		this.sender = sender;
		this.isnew = isnew;
		this.title = title;
		this.size = size;
		this.time = time;
		this.content = content;
	}

	public num getNum() {
		return num;
	}

	public void setNum(num num) {
		this.num = num;
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

	public title getTitle() {
		return title;
	}

	public void setTitle(title title) {
		this.title = title;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "MailContent [num=" + num + ", sender=" + sender + ", isnew="
				+ isnew + ", title=" + title + ", size=" + size + ", time="
				+ time + ", content=" + content + "]";
	}
}
