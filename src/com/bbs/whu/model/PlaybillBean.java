package com.bbs.whu.model;

/**
 * У԰�����б�Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class PlaybillBean {
	// У԰��������
	private String title;
	// У԰�������ڰ���Ӣ����
	private String board;
	// У԰�������ڰ��ı��
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
