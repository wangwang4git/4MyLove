package com.bbs.whu.model.mail;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class senderConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(sender.class);
	}

	public Object fromString(String attributeValue) {
		return new sender(attributeValue);
	}

	public String toString(Object obj) {
		return ((sender) obj).getAttributeValue();
	}
}
