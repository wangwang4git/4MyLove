package com.bbs.whu.model;

/**
 * �Ѷ���Ƕ�Ԫ�ؽṹ����
 * 
 * @author WWang
 * 
 */
public class ReadedTagBean {
	// ����Id
	private String id;
	// �Ѷ���ǣ�0��ʾδ����1��ʾ�Ѷ�
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
