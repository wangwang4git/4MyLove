package com.bbs.whu.model;

/**
 * ����Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class FaceBean {
	// �������ƣ�����[em01]
	private String name;
	// ������ԴID������R.drawable.em01
	private int id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "FaceBean [name=" + name + ", id=" + id + "]";
	}
}
