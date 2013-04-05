package com.bbs.whu.model.favor;

public class select {
	private String attributeValue;

	public select() {
		super();
		// TODO Auto-generated constructor stub
	}

	public select(String attributeValue) {
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
