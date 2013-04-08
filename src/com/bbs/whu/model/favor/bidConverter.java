package com.bbs.whu.model.favor;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class bidConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(bid.class);
	}

	public Object fromString(String attributeValue) {
		return new bid(attributeValue);
	}

	public String toString(Object obj) {
		return ((bid) obj).getAttributeValue();
	}
}
