package com.bbs.whu.model.friend;

public class Type {
	private String attributeValue;

	public Type() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Type(String attributeValue) {
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
