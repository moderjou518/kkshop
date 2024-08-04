package com.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_PrepareAccess;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class testReadLog {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws ParseException, FileNotFoundException {

		String logFolder = "C:\\pa\\_logs\\2022_KkshopLog\\";

		ArrayList<String> logAry = new ArrayList<String>();
		logAry.add("kkshop.log.2022-08-11");
		// logAry.add("kkshop.log.2022-08-02");
		// logAry.add("javaLog20220907.txt");

		logAry.forEach((logFile) -> {
			try {
				readLog(logFolder + logFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		});

		/*
		 * HMSSYSLOG(LOG_UUID, LOG_TIME, LOG_LOGIN_ID, LOG_IP, LOG_ACTION,
		 * LOG_FUNC, LOG_RUNTIME) 全系統&指定帳號 每天登入系統人數 SELECT LEFT(LOG_TIME,8)
		 * SYS_DAY, COUNT(*) LOGIN_TIMES FROM HMSSYSLOG WHERE LEFT(LOG_TIME,6) =
		 * '202209' AND LOG_ACTION = 'HBD1LOGIN' 每天登入系統次數(HBD1LOGIN_DO_ACTION)
		 * 每天操作系統點擊幾次(AJAX，不含#) 每天執行那些功能，各幾次(AJAX，不含#) 上次成功登入時間
		 * 
		 * 每天系統操作尖峰時間(每小時使用次數) 每個帳號每天系統操作尖峰時間(每小時使用次數)
		 * 
		 * 每天操作系統總累計執行時間(TOT 1S) 密碼登入失敗次數、上次登入失敗時間
		 * 
		 */

	}

	@SuppressWarnings("resource")
	public static void readLog(String logFile) throws FileNotFoundException {

		File doc = new File(logFile);
		Scanner docObj = new Scanner(doc);

		String lineText = "";
		String[] lineAry = null;

		String logText = "";
		String[] logAry = null;

		String logTime = "";
		String logLoginID = "";
		String logIP = "";
		String logAction = "";
		String logMethod = "";
		String logRunTime = "";
		String logType = "";

		// String row = "";
		JSONArray ary = new JSONArray();
		JSONObject logJo = null;
		StringBuffer sb = new StringBuffer();
		while (docObj.hasNextLine()) {

			lineText = docObj.nextLine();
			
			
			
			lineAry = lineText.trim().split("-");
			logText = lineAry[1].trim();
			logAry = logText.split(",");

			// JAVA LOG
			if (lineText.indexOf("KKSHOP_JAVA_LOG") > 0) {
				
				if (logAry.length == 5) {
					
					System.out.println(logText);
					
					logJo = new JSONObject();
					logJo.put("", "1122334455");
					logJo.put("logType", "KKSHOP_JAVA_LOG");
					logJo.put("logTime", lineText.substring(0, 19).replace("/", "").replace(" ", "-"));
					logJo.put("logLoginID", logAry[0].trim());
					logJo.put("logIP", logAry[1].trim());
					
					logJo.put("logIP", logAry[1].trim());
					logJo.put("logAction ", logAry[2].trim());
					logJo.put("logMethod", logAry[3].trim());
					logJo.put("logRunTime ", logAry[4].trim());
					logJo.put("logType", "KKSHOP_JAVA");
					
					
					sb.append(String.format("%-11s%-18s%-11s%-16s%-20s%-25s%3ss\n", "1122334455", logTime, logLoginID,
							logIP, logAction, logMethod, logRunTime));
					// System.out.println(row);
					// INSERT LOG DB: AJAX ACTION
					// INSERT INTO HMSSYSLOG(LOG_UUID, LOG_COMP, LOG_TIME,
					// LOG_LOGIN_ID, LOG_IP, LOG_ACTION, LOG_FUNC,
					// LOG_DURINGTIME, LOG_TYPE) VALUES(?, ?, ?, ?, ?, ?, ?, ?,
					// ?)

					/**
					 * INSERT LOG DB: AJAX ACTION INSERT INTO
					 * HMSSYSLOG(LOG_UUID, LOG_COMP, LOG_TIME, LOG_LOGIN_ID,
					 * LOG_IP, LOG_ACTION, LOG_FUNC, LOG_DURINGTIME, LOG_TYPE)
					 * VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
					 */
					ArrayList<String> parms = new ArrayList<String>();
					parms.add("1122334455");// comp
					parms.add(logTime); // log_time
					parms.add(logLoginID); // log_login_id
					parms.add(logIP); // log_ip
					parms.add(logAction); // log_action
					parms.add(logMethod); // log_func
					parms.add(logRunTime); // log_duringTime
					parms.add("KKSHOP_JAVA");// log_type
					
					ary.add(parms);

				} else {
					// System.out.println("NO: " + lineText);
				}
			}

			// LOGIN LOG
			if (lineText.indexOf("KKSHOP_LOGIN_LOG") > 0) {
				logType = "KKSHOP_LOGIN_LOG";
				if (lineText.indexOf("Login OK") > 0) {
					// 登入成功, INSERT DB
					// System.out.println(lineText);
				} else {
					// 登入失敗
					// System.out.println(lineText);
				}
			}

			// JSP LOG
			if (lineText.indexOf("KKSHOP_JSP_LOG") > 0) {
				// System.out.println(lineText);
				logType = "KKSHOP_JSP_LOG";
			}

		} // end while

		//System.out.println(sb.toString());

	}

	static void createLog(JSONArray logAry) {

		try {

			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); 
			String connStr = "jdbc:ucanaccess://C:/www/hmsdb/image/HMS0_LOG.mdb;memory=true";
			Connection conn = DriverManager.getConnection(connStr, "Admin", "nocgjldn");
			HMS_PrepareAccess prep = new HMS_PrepareAccess(conn);
			conn.setAutoCommit(false);

			HMS_TBLE dt = prep.queryDataTable("select *　from hms0_misc where misc_kind = ? and misc_code = ? ",
					new Object[] { "ROOT", "WEBACTION" });

			for (int i = 0; i < dt.GetRowsCount(); i++) {
				HMS_TROW dr = dt.getDataRow(i);
				System.out.println("before update- " + dr.GetField("misc_desc"));
			}

			Object[] parms = new Object[] { "test111", "ROOT", "WEBACTION" };
			int c = prep.updateSql("Update hms0_misc set misc_desc = ? Where misc_kind = ? and misc_code = ? ", parms);

			System.out.println("c: " + c);

			dt = prep.queryDataTable("select *　from hms0_misc where misc_kind = ? and misc_code = ? ",
					new Object[] { "ROOT", "WEBACTION" });

			for (int i = 0; i < dt.GetRowsCount(); i++) {
				HMS_TROW dr = dt.getDataRow(i);
				System.out.println(" after update- " + dr.GetField("misc_desc"));
			}

			conn.rollback();

			dt = prep.queryDataTable("select *　from hms0_misc where misc_kind = ? and misc_code = ? ",
					new Object[] { "ROOT", "WEBACTION" });

			for (int i = 0; i < dt.GetRowsCount(); i++) {
				HMS_TROW dr = dt.getDataRow(i);
				System.out.println(" after rollback - " + dr.GetField("misc_desc"));
			}

			prep.closeConnection();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}