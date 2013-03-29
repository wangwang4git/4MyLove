package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class userfromConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(userfrom.class);
	}

	public Object fromString(String attributeValue) {
		return new userfrom(attributeValue);
	}

	public String toString(Object obj) {
		return ((userfrom) obj).getAttributeValue();
	}
}
