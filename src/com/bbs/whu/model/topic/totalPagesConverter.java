package com.bbs.whu.model.topic;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class totalPagesConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(totalPages.class);
	}

	public Object fromString(String attributeValue) {
		return new totalPages(attributeValue);
	}

	public String toString(Object obj) {
		return ((totalPages) obj).getAttributeValue();
	}
}
