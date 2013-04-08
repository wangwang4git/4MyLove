package com.bbs.whu.model.favor;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class descConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(desc.class);
	}

	public Object fromString(String attributeValue) {
		return new desc(attributeValue);
	}

	public String toString(Object obj) {
		return ((desc) obj).getAttributeValue();
	}
}
