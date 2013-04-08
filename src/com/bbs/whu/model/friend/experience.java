package com.bbs.whu.model.friend;

public class experience {
	private String attributeValue;

	public experience() {
		super();
		// TODO Auto-generated constructor stub
	}

	public experience(String attributeValue) {
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
