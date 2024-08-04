package com.hms.util.hms2;

import net.sf.json.JSON;

public class Msg extends ReturnMessage {
	public Msg(boolean success) {
		super(success);
	}

	public Msg(boolean success, String msg) {
		super(success, msg);
	}

	public Msg(boolean success, String msg, JSON data) {
		super(success, msg);
		addProperty("data", data);
	}
	
	public Msg(boolean success, String msg, String jsonString){
		super(success, msg);
		addProperty("data", jsonString);
	}
}