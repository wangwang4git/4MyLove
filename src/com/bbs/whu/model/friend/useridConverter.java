package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class useridConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(userid.class);
	}

	public Object fromString(String attributeValue) {
		return new userid(attributeValue);
	}

	public String toString(Object obj) {
		return ((userid) obj).getAttributeValue();
	}
}
