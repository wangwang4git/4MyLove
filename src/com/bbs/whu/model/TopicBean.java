package com.bbs.whu.model;

/**
 * ��������Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class TopicBean {
	// �����ð�����
	private String number;
	// ����ȫ�ֱ��
	private String GID;
	// ����
	private String title;
	// ����
	private String author;
	// ����ʱ��
	private String posttime;
	// �ظ���
	private String replyNum;
	// ���»ظ��û����
	private String lastReplyID;
	// ���»ظ��û���
	private String lastReplyAuthor;
	// ���»ظ�ʱ��
	private String lastReplyTime;
	// ��������
	private String flag;

	public TopicBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TopicBean(String number, String gID, String title, String author,
			String posttime, String replyNum, String lastReplyID,
			String lastReplyAuthor, String lastReplyTime, String flag) {
		super();
		this.number = number;
		GID = gID;
		this.title = title;
		this.author = author;
		this.posttime = posttime;
		this.replyNum = replyNum;
		this.lastReplyID = lastReplyID;
		this.lastReplyAuthor = lastReplyAuthor;
		this.lastReplyTime = lastReplyTime;
		this.flag = flag;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getGID() {
		return GID;
	}

	public void setGID(String gID) {
		GID = gID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPosttime() {
		return posttime;
	}

	public void setPosttime(String posttime) {
		this.posttime = posttime;
	}

	public String getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(String replyNum) {
		this.replyNum = replyNum;
	}

	public String getLastReplyID() {
		return lastReplyID;
	}

	public void setLastReplyID(String lastReplyID) {
		this.lastReplyID = lastReplyID;
	}

	public String getLastReplyAuthor() {
		return lastReplyAuthor;
	}

	public void setLastReplyAuthor(String lastReplyAuthor) {
		this.lastReplyAuthor = lastReplyAuthor;
	}

	public String getLastReplyTime() {
		return lastReplyTime;
	}

	public void setLastReplyTime(String lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	@Override
	public String toString() {
		return "TopicBean [number=" + number + ", GID=" + GID + ", title="
				+ title + ", author=" + author + ", posttime=" + posttime
				+ ", replyNum=" + replyNum + ", lastReplyID=" + lastReplyID
				+ ", lastReplyAuthor=" + lastReplyAuthor + ", lastReplyTime="
				+ lastReplyTime + ", flag=" + flag + "]";
	}
}
