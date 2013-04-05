package com.bbs.whu.model.favor;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public class articlesConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type.equals(articles.class);
	}

	public Object fromString(String attributeValue) {
		return new articles(attributeValue);
	}

	public String toString(Object obj) {
		return ((articles) obj).getAttributeValue();
	}
}
