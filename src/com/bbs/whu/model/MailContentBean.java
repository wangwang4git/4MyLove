package com.bbs.whu.model;


/**
 * 邮件详情元素结构定义
 * 
 * @author WWang
 * 
 */
public class MailContentBean {
	// 邮件索引值
	private String num;
	// 发信人
	private String sender;
	// 新邮件标记
	private String isnew;
	// 邮件标题
	private String title;
	// 邮件大小
	private String size;
	// 发信时间
	private String time;
	// 邮件正文
	private String text;

	public MailContentBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MailContentBean(String num, String sender, String isnew,
			String title, String size, String time, String text) {
		super();
		this.num = num;
		this.sender = sender;
		this.isnew = isnew;
		this.title = title;
		this.size = size;
		this.time = time;
		this.text = text;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getIsnew() {
		return isnew;
	}

	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "MailContentBean [num=" + num + ", sender=" + sender
				+ ", isnew=" + isnew + ", title=" + title + ", size=" + size
				+ ", time=" + time + ", text=" + text + "]";
	}
}
