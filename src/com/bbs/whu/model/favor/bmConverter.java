package com.bbs.whu.model.favor;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class bmConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(bm.class);
	}

	public Object fromString(String attributeValue) {
		return new bm(attributeValue);
	}

	public String toString(Object obj) {
		return ((bm) obj).getAttributeValue();
	}
}
