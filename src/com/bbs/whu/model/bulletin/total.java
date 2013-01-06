package com.bbs.whu.model.bulletin;

public class total {
	private String attributeValue;

	public total() {
		super();
		// TODO Auto-generated constructor stub
	}

	public total(String attributeValue) {
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
