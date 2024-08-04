package com.evergreen_hotels.bmg.whms1.util;

import java.io.Serializable;

public class RowColumn implements Serializable {
	private static final long serialVersionUID = -4550508632415388241L;
	private int type;
	private String name;

	public RowColumn(String name, int type) {
		this.name = name;
		this.type = type;
	}

	int getType() {
		return type;
	}

	String getName() {
		return name;
	}

	String getMethodName() {
		return titleTextConversion(name);
	}

	String getAttributeName()
	{
		return firstCharToLowerCase(titleTextConversion(name));
	}

	private static String titleTextConversion(String text)
	{
		String[] words = text.split("(?<=_)|(?=_)");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			if (word.length() > 0) {
				sb.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString().replaceAll("_", "");
	}

	private static String firstCharToLowerCase(String str) {
		if ((str != null) && (str != "")) {
			str = str.substring(0, 1).toLowerCase() + str.substring(1);
		} else {
			str = "";
		}
		return str;
	}

	public static void main(String[] args) {
		System.out.println(firstCharToLowerCase(titleTextConversion("CUSTOMER_ID")));
	}
}