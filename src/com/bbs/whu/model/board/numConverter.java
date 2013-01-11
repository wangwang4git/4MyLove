package com.bbs.whu.model.board;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class numConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(num.class);
	}

	public Object fromString(String attributeValue) {
		return new num(attributeValue);
	}

	public String toString(Object obj) {
		return ((num) obj).getAttributeValue();
	}
}
