package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class TypeConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(Type.class);
	}

	public Object fromString(String attributeValue) {
		return new Type(attributeValue);
	}

	public String toString(Object obj) {
		return ((Type) obj).getAttributeValue();
	}
}
