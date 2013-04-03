package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class userface_imgConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(userface_img.class);
	}

	public Object fromString(String attributeValue) {
		return new userface_img(attributeValue);
	}

	public String toString(Object obj) {
		return ((userface_img) obj).getAttributeValue();
	}
}
