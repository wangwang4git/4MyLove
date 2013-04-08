package com.bbs.whu.model.board;

public class id {
	private String attributeValue;

	public id() {
		super();
		// TODO Auto-generated constructor stub
	}

	public id(String attributeValue) {
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
