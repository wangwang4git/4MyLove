package com.bbs.whu.model.attr;

public class name {
	private String attributeValue;

	public name() {
		super();
		// TODO Auto-generated constructor stub
	}

	public name(String attributeValue) {
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
