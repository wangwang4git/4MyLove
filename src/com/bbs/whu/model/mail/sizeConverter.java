package com.bbs.whu.model.mail;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class sizeConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(isnew.class);
	}

	public Object fromString(String attributeValue) {
		return new isnew(attributeValue);
	}

	public String toString(Object obj) {
		return ((isnew) obj).getAttributeValue();
	}
}
