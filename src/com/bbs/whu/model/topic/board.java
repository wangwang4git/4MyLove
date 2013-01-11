package com.bbs.whu.model.topic;

public class board {
	private String attributeValue;

	public board() {
		super();
		// TODO Auto-generated constructor stub
	}

	public board(String attributeValue) {
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
