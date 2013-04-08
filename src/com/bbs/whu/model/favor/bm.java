package com.bbs.whu.model.favor;

public class bm {
	private String attributeValue;

	public bm() {
		super();
		// TODO Auto-generated constructor stub
	}

	public bm(String attributeValue) {
		super();
		this.attributeValue = attributeValue;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.attributeValue;
	}
}
