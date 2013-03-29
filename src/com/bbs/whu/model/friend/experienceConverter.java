package com.bbs.whu.model.friend;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class experienceConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(experience.class);
	}

	public Object fromString(String attributeValue) {
		return new experience(attributeValue);
	}

	public String toString(Object obj) {
		return ((experience) obj).getAttributeValue();
	}
}
