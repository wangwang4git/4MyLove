package com.bbs.whu.model.board;

import com.bbs.whu.model.bulletin.num;

/**
 * �������Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class Board {
	// �����
	private num num;
	// �������
	private name name;
	// ���Ӣ������
	private id id;

	public Board() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Board(num num, name name, id id) {
		super();
		this.num = num;
		this.name = name;
		this.id = id;
	}

	public num getNum() {
		return num;
	}

	public void setNum(num num) {
		this.num = num;
	}

	public name getName() {
		return name;
	}

	public void setName(name name) {
		this.name = name;
	}

	public id getId() {
		return id;
	}

	public void setId(id id) {
		this.id = id;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.num.toString() + "/" + this.name.toString() + "/"
				+ this.id.toString();
	}
}