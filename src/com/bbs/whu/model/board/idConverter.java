package com.bbs.whu.model.board;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class idConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(id.class);
	}

	public Object fromString(String attributeValue) {
		return new id(attributeValue);
	}

	public String toString(Object obj) {
		return ((id) obj).getAttributeValue();
	}
}
