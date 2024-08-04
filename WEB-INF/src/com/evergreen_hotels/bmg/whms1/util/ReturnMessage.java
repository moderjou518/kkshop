package com.evergreen_hotels.bmg.whms1.util;

import net.sf.json.JSONObject;

public class ReturnMessage {
	public static final String SUCCESS = "success";
	public static final String MESSAGE = "msg";
	private JSONObject jobj;

	public ReturnMessage(boolean success) {
		jobj = new JSONObject();
		jobj.put("success", Boolean.valueOf(success));
	}

	public ReturnMessage(String message) {
		jobj = JSONObject.fromObject(message);
	}

	public ReturnMessage(boolean success, String message) {
		this(success);
		setMessage(message);
	}

	public boolean isSuccess() {
		return jobj.getBoolean("success");
	}

	protected void setSuccess(boolean success) {
		getJSONObject().put("success", Boolean.valueOf(success));
	}

	protected void setMessage(String message) {
		getJSONObject().put("msg", message);
	}

	public String getMessage() {
		return (String) getJSONObject().get("msg");
	}

	public void addProperty(String key, Object object) {
		getJSONObject().put(key, object);
	}

	public Object getProperty(String key) {
		return getJSONObject().get(key);
	}

	public String getPropertyAsString(String key) {
		return getJSONObject().get(key).toString();
	}

	protected JSONObject getJSONObject() {
		return jobj;
	}

	public String getJSONString() {
		return getJSONObject().toString();
	}

	public String toString() {
		return getJSONString();
	}
}