package com.bbs.whu.model.friend;

public class idle {
	private String attributeValue;

	public idle() {
		super();
		// TODO Auto-generated constructor stub
	}

	public idle(String attributeValue) {
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
