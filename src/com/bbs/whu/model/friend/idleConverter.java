package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class idleConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(idle.class);
	}

	public Object fromString(String attributeValue) {
		return new idle(attributeValue);
	}

	public String toString(Object obj) {
		return ((idle) obj).getAttributeValue();
	}
}
