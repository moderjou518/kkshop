package com.test;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_PrepareAccess;
import com.hms.util.UTIL_Encrypt_Base64;

import net.sf.json.JSONObject;

public class testTabKey {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		//String tabKey = "eyJMb2dpbk1hcmsiOiJZIiwiTG9naW5Db21wIjoiMTEyMjMzNDQ1NSIsIkxvZ2luSUQiOiIwOTM1MTY5NTgwIiwiTG9naW5Hcm91cCI6IkMiLCAiTG9naW5EYXRlIjoiMjAyMzAxMDgifQ==";
		String tabKey = "eyJMb2dpbk1hcmsiOiJZIiwiTG9naW5Db21wIjoiMTEyMjMzNDQ1NSIsIkxvZ2luSUQiOiIwOTM1MTY5NTgwIiwiTG9naW5Hcm91cCI6IkMiLCAiTG9naW5EYXRlIjoiMjAyNDAyMjAifQ==";
		String jsonInfo = UTIL_Encrypt_Base64.decrypt(tabKey);
		//System.out.println("@@@@@@2jsonInfo: " + jsonInfo);
		JSONObject userJo = JSONObject.fromObject(jsonInfo);
		System.out.println(userJo.toString());
	}
	
	
}
