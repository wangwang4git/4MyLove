package com.bbs.whu.model.topic;

public class totalPages {
	private String attributeValue;

	public totalPages() {
		super();
		// TODO Auto-generated constructor stub
	}

	public totalPages(String attributeValue) {
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
