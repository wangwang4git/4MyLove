package com.bbs.whu.model;


/**
 * �ʼ�����Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class MailContentBean {
	// �ʼ�����ֵ
	private String num;
	// ������
	private String sender;
	// ���ʼ����
	private String isnew;
	// �ʼ�����
	private String title;
	// �ʼ���С
	private String size;
	// ����ʱ��
	private String time;
	// �ʼ�����
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
