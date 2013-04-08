package com.bbs.whu.model.favor;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class usersConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(users.class);
	}

	public Object fromString(String attributeValue) {
		return new users(attributeValue);
	}

	public String toString(Object obj) {
		return ((users) obj).getAttributeValue();
	}
}
