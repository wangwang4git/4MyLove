package com.bbs.whu.model.bulletin;

public class num {
	private String attributeValue;

	public num() {
		super();
		// TODO Auto-generated constructor stub
	}

	public num(String attributeValue) {
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
