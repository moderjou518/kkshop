package com.evergreen_hotels.bmg.whms1.util;

import net.sf.json.JSON;

public class Msg extends ReturnMessage
{
	public Msg(boolean success)
	{
		super(success);
	}

	public Msg(boolean success, String msg) {
		super(success, msg);
	}

	public Msg(boolean success, String msg, JSON data) {
		super(success, msg);
		addProperty("data", data);
	}
}