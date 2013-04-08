package com.bbs.whu.model.mail;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class timeConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(time.class);
	}

	public Object fromString(String attributeValue) {
		return new time(attributeValue);
	}

	public String toString(Object obj) {
		return ((time) obj).getAttributeValue();
	}
}
