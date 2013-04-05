package com.bbs.whu.model.favor;

public class articles {
	private String attributeValue;

	public articles() {
		super();
		// TODO Auto-generated constructor stub
	}

	public articles(String attributeValue) {
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
