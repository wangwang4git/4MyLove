package com.bbs.whu.model.topic;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class boardConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(board.class);
	}

	public Object fromString(String attributeValue) {
		return new board(attributeValue);
	}

	public String toString(Object obj) {
		return ((board) obj).getAttributeValue();
	}
}
