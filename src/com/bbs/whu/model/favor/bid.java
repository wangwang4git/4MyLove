package com.bbs.whu.model.favor;

public class bid {
	private String attributeValue;

	public bid() {
		super();
		// TODO Auto-generated constructor stub
	}

	public bid(String attributeValue) {
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
