package com.bbs.whu.model;

/**
 * 帖子详情列表元素结构定义
 * 
 * @author WWang
 * 
 */
public class BulletinBean {
	// 楼层
	private String floor;
	// 内容
	private String content;
	// id
	private String id;
	// 发信人名称
	private String author;
	// 发信人头像url
	private String userfaceImg;
	// 信区（板块）
	private String board;
	// 标题
	private String title;
	// 发站信
	private String site;
	// 发帖（回复）时间
	private String time;
	// 帖子头部
	private String head;
	// 帖子主体
	private String body;
	// 帖子回复
	private String reply;
	// 帖子正文
	private String text;
	// 签名档
	private String sign;
	// 来源
	private String from;

	public BulletinBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BulletinBean(String floor, String content, String id, String author,
			String userfaceImg, String board, String title, String site,
			String time, String head, String body, String reply, String text,
			String sign, String from) {
		super();
		this.floor = floor;
		this.content = content;
		this.id = id;
		this.author = author;
		this.userfaceImg = userfaceImg;
		this.board = board;
		this.title = title;
		this.site = site;
		this.time = time;
		this.head = head;
		this.body = body;
		this.reply = reply;
		this.text = text;
		this.sign = sign;
		this.from = from;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getUserfaceImg() {
		return userfaceImg;
	}

	public void setUserfaceImg(String userfaceImg) {
		this.userfaceImg = userfaceImg;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String toString() {
		return "BulletinBean [floor=" + floor + ", content=" + content
				+ ", id=" + id + ", author=" + author + ", userfaceImg="
				+ userfaceImg + ", board=" + board + ", title=" + title
				+ ", site=" + site + ", time=" + time + ", head=" + head
				+ ", body=" + body + ", reply=" + reply + ", text=" + text
				+ ", sign=" + sign + ", from=" + from + "]";
	}
}