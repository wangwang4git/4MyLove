package com.bbs.whu.model.topic;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class pageConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(page.class);
	}

	public Object fromString(String attributeValue) {
		return new page(attributeValue);
	}

	public String toString(Object obj) {
		return ((page) obj).getAttributeValue();
	}
}
