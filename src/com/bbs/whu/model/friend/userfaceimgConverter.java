package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class userfaceimgConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(userfaceimg.class);
	}

	public Object fromString(String attributeValue) {
		return new userfaceimg(attributeValue);
	}

	public String toString(Object obj) {
		return ((userfaceimg) obj).getAttributeValue();
	}
}
