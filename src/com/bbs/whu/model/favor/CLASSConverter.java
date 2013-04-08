package com.bbs.whu.model.favor;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class CLASSConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(CLASS.class);
	}

	public Object fromString(String attributeValue) {
		return new CLASS(attributeValue);
	}

	public String toString(Object obj) {
		return ((CLASS) obj).getAttributeValue();
	}
}
