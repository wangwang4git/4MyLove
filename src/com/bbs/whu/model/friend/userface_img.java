package com.bbs.whu.model.friend;

public class userface_img {
	private String attributeValue;

	public userface_img() {
		super();
		// TODO Auto-generated constructor stub
	}

	public userface_img(String attributeValue) {
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
