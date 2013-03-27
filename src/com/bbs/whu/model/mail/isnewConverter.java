package com.bbs.whu.model.mail;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class isnewConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(size.class);
	}

	public Object fromString(String attributeValue) {
		return new size(attributeValue);
	}

	public String toString(Object obj) {
		return ((size) obj).getAttributeValue();
	}
}
