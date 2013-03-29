package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class usernameConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(username.class);
	}

	public Object fromString(String attributeValue) {
		return new username(attributeValue);
	}

	public String toString(Object obj) {
		return ((username) obj).getAttributeValue();
	}
}
