package com.bbs.whu.model;

/**
 * 校园海报列表元素结构定义
 * 
 * @author WWang
 * 
 */
public class PlaybillBean {
	// 校园海报标题
	private String title;
	// 校园海报所在版块的英文名
	private String board;
	// 校园海报所在版块的编号
	private String groupid;

	public PlaybillBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlaybillBean(String title, String board, String groupid) {
		super();
		this.title = title;
		this.board = board;
		this.groupid = groupid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBoard() {
		return board;
	}

	public void setBoard(String board) {
		this.board = board;
	}

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	@Override
	public String toString() {
		return "PlaybillBean [title=" + title + ", board=" + board
				+ ", groupid=" + groupid + "]";
	}

}
