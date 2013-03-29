package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class modeConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(mode.class);
	}

	public Object fromString(String attributeValue) {
		return new mode(attributeValue);
	}

	public String toString(Object obj) {
		return ((mode) obj).getAttributeValue();
	}
}
