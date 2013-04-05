package com.bbs.whu.model.favor;

public class flag {
	private String attributeValue;

	public flag() {
		super();
		// TODO Auto-generated constructor stub
	}

	public flag(String attributeValue) {
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
