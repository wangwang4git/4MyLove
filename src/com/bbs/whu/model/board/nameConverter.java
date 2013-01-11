package com.bbs.whu.model.board;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class nameConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(name.class);
	}

	public Object fromString(String attributeValue) {
		return new name(attributeValue);
	}

	public String toString(Object obj) {
		return ((name) obj).getAttributeValue();
	}
}
