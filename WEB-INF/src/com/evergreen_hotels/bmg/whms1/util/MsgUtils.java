package com.evergreen_hotels.bmg.whms1.util;

import net.sf.json.JSON;

public class MsgUtils
{
	public static String succeed(String msg)
	{
		Msg rm = new Msg(true, msg);
		return rm.getJSONString();
	}

	public static String fail(String msg)
	{
		Msg rm = new Msg(false, msg);
		return rm.getJSONString();
	}

	public static String fail(String msg, String issueId)
	{
		Msg rm = new Msg(false, msg);
		if (issueId != null) {
			rm.addProperty("issueId", issueId);
		}
		return rm.getJSONString();
	}

	public static String toString(boolean success, String msg, JSON data)
	{
		Msg rm = new Msg(success, msg, data);
		return rm.getJSONString();
	}
}