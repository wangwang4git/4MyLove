package com.bbs.whu.model.mail;

public class time {
	private String attributeValue;

	public time() {
		super();
		// TODO Auto-generated constructor stub
	}

	public time(String attributeValue) {
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
