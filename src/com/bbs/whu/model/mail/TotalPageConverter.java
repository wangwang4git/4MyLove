package com.bbs.whu.model.mail;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class TotalPageConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(TotalPage.class);
	}

	public Object fromString(String attributeValue) {
		return new TotalPage(attributeValue);
	}

	public String toString(Object obj) {
		return ((TotalPage) obj).getAttributeValue();
	}
}
