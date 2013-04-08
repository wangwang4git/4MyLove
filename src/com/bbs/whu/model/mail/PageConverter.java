package com.bbs.whu.model.mail;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class PageConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(Page.class);
	}

	public Object fromString(String attributeValue) {
		return new Page(attributeValue);
	}

	public String toString(Object obj) {
		return ((Page) obj).getAttributeValue();
	}
}
