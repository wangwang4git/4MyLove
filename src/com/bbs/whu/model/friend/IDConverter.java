package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class IDConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(ID.class);
	}

	public Object fromString(String attributeValue) {
		return new ID(attributeValue);
	}

	public String toString(Object obj) {
		return ((ID) obj).getAttributeValue();
	}
}
