package com.bbs.whu.model.bulletin;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class totalConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(total.class);
	}

	public Object fromString(String attributeValue) {
		return new total(attributeValue);
	}

	public String toString(Object name) {
		return ((total) name).getAttributeValue();
	}
}
