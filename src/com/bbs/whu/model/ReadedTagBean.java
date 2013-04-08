package com.bbs.whu.model;

/**
 * 已读标记对元素结构定义
 * 
 * @author WWang
 * 
 */
public class ReadedTagBean {
	// 帖子Id
	private String id;
	// 已读标记，0表示未读，1表示已读
	private Byte tag;

	public ReadedTagBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReadedTagBean(String id, Byte tag) {
		super();
		this.id = id;
		this.tag = tag;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Byte getTag() {
		return tag;
	}

	public void setTag(Byte tag) {
		this.tag = tag;
	}

	@Override
	public String toString() {
		return "ReadedTagBean [id=" + id + ", tag=" + tag + "]";
	}
}
