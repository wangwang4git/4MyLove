package com.bbs.whu.model;

/**
 * 版面帖子元素结构定义
 * 
 * @author WWang
 * 
 */
public class TopicBean {
	// 所处该版面编号
	private String number;
	// 帖子全局编号
	private String GID;
	// 标题
	private String title;
	// 作者
	private String author;
	// 发表时间
	private String posttime;
	// 回复数
	private String replyNum;
	// 最新回复用户编号
	private String lastReplyID;
	// 最新回复用户名
	private String lastReplyAuthor;
	// 最新回复时间
	private String lastReplyTime;
	
	public TopicBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TopicBean(String number, String gID, String title, String author,
			String posttime, String replyNum, String lastReplyID,
			String lastReplyAuthor, String lastReplyTime) {
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

	@Override
	public String toString() {
		return "Topic [number=" + number + ", GID=" + GID + ", title=" + title
				+ ", author=" + author + ", posttime=" + posttime
				+ ", replyNum=" + replyNum + ", lastReplyID=" + lastReplyID
				+ ", lastReplyAuthor=" + lastReplyAuthor + ", lastReplyTime="
				+ lastReplyTime + "]";
	}
}
