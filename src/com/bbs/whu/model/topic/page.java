package com.bbs.whu.model.topic;

public class page {
	private String attributeValue;

	public page() {
		super();
		// TODO Auto-generated constructor stub
	}

	public page(String attributeValue) {
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
