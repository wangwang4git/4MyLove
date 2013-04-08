package com.bbs.whu.model.mail;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class titleConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(title.class);
	}

	public Object fromString(String attributeValue) {
		return new title(attributeValue);
	}

	public String toString(Object obj) {
		return ((title) obj).getAttributeValue();
	}
}
