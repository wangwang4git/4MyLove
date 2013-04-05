package com.bbs.whu.model.favor;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class selectConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(select.class);
	}

	public Object fromString(String attributeValue) {
		return new select(attributeValue);
	}

	public String toString(Object obj) {
		return ((select) obj).getAttributeValue();
	}
}
