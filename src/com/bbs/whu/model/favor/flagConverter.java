package com.bbs.whu.model.favor;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class flagConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(flag.class);
	}

	public Object fromString(String attributeValue) {
		return new flag(attributeValue);
	}

	public String toString(Object obj) {
		return ((flag) obj).getAttributeValue();
	}
}
