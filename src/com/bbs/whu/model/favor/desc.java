package com.bbs.whu.model.favor;

public class desc {
	private String attributeValue;

	public desc() {
		super();
		// TODO Auto-generated constructor stub
	}

	public desc(String attributeValue) {
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
