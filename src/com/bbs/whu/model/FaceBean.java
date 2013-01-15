package com.bbs.whu.model;

/**
 * 表情元素结构定义
 * 
 * @author WWang
 * 
 */
public class FaceBean {
	// 表情名称，例如[em01]
	private String name;
	// 表情资源ID，例如R.drawable.em01
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
