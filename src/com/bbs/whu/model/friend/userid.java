package com.bbs.whu.model.friend;

public class userid {
	private String attributeValue;

	public userid() {
		super();
		// TODO Auto-generated constructor stub
	}

	public userid(String attributeValue) {
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
