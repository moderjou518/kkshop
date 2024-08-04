package com.hms.util;
/**
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

/*
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
*/

import org.apache.log4j.Logger;

import com.hms.entity.Actions;
import com.hms.entity.Actions.Action;
import com.hms.entity.HMS_TROW;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UTIL_GlobalConfig {

	private static final String A = null;
	public static Logger logger = Logger.getLogger("KKSHOP_JAVA_LOG");

	public static String getConfigFile() throws ClassNotFoundException {
		return "/home/kkshop/kkshop.tw/WEB-INF/resource/config_pa.properties";
	}

	public static String getDvpConfigFile() throws ClassNotFoundException {
		return "/home/kkshop/kkshop.tw/WEB-INF/resource/dvp/dvpUserInfo.properties";
	}

	public static String getActionFile() {
		return "/home/kkshop/kkshop.tw/WEB-INF/resource/actions_pa.json";
		//return "/home/kkshop/kkshop.tw/WEB-INF/resource/actions_pa.xml";
	}

	public static String getDBFile(String systemID) {
		String conn = "jdbc:ucanaccess:///home/kkshop/kkshop.tw/WEB-INF/resource/" + systemID + ".mdb;memory=true";
		return conn;
	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		return UTIL_GlobalConfig.getConnection("HMS");
	}

	public static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {

		// logger.info("UTIL_GlobalConfig.getConnection(dbName)");

		String driverStr = "";
		String connStr = "";
		String uid = "";
		String pwd = "";
		Properties prop = new Properties();

		try {
			// logger.info("load properties: " +
			// UTIL_GlobalConfig.getConfigFile());
			// prop.load(UTIL_GlobalConfig.class.getClassLoader().getResourceAsStream(UTIL_GlobalConfig.getConfigFile()));
			InputStream is = new FileInputStream(UTIL_GlobalConfig.getConfigFile());
			prop.load(is);
			connStr = prop.getProperty(dbName + "_DB");
			driverStr = prop.getProperty(dbName + "_DB_DRIVER");
			uid = prop.getProperty(dbName + "_DB_USERID");
			pwd = prop.getProperty(dbName + "_DB_PASSWD");
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.toString());
		}
		;

		Connection conn = null;
		try {
			// logger.info("classForName: " + driverStr);
			Class.forName(driverStr);
			if (null != uid) {
				conn = DriverManager.getConnection(connStr, uid, pwd);
			} else {
				conn = DriverManager.getConnection(connStr);
			}
		} catch (ClassNotFoundException e) {
			logger.error(e.toString());
			logger.error("driverStr: " + driverStr);
			logger.error("conStr: " + connStr);
			throw e;
		}

		return conn;

	}

	public static Connection getTestConnection() throws SQLException, ClassNotFoundException {

		// logger.info("UTIL_GlobalConfig.getConnection(dbName)");

		String driverStr = "";
		String connStr = "";
		String uid = "";
		String pwd = "";
		// Properties prop = new Properties();
		// logger.info("load properties: " +
		// UTIL_GlobalConfig.getConfigFile());
		// prop.load(UTIL_GlobalConfig.class.getClassLoader().getResourceAsStream(UTIL_GlobalConfig.getConfigFile()));
		// InputStream is = new
		// FileInputStream(UTIL_GlobalConfig.getConfigFile());
		// prop.load(is);
		// connStr =
		// "jdbc:ucanaccess:///home/kkshop/kkshop.tw/WEB-INF/resource/HBD.mdb;memory=true";V

		connStr = UTIL_GlobalConfig.getDBFile("HBD");
		driverStr = "net.ucanaccess.jdbc.UcanaccessDriver";
		uid = "";
		pwd = "";

		Connection conn = null;
		try {
			// logger.info("classForName: " + driverStr);
			Class.forName(driverStr);
			conn = DriverManager.getConnection(connStr, uid, pwd);
		} catch (ClassNotFoundException e) {
			logger.error(e.toString());
			logger.error("driverStr: " + driverStr);
			logger.error("conStr: " + connStr);
			throw e;
		}

		return conn;

	}

	// 載入系統資料
	public static final void preLoad() throws Exception {

		try {
			logger.info("UTIL_GlobalConfig.preLoad()");
			UTIL_GlobalConfig.loadWebactions();
		} catch (Exception e) {
			logger.error(e.toString());
			// throw e;
		} finally {

		}
	}

	// 從 action.xml 把入action 清單到記憶體
	@SuppressWarnings("unchecked")
	public static final void loadWebactions() throws FileNotFoundException {

		logger.info("UTIL_GlobalConfig.preLoad()->loadWebactions");

		
		/* XML 版
		 * 
		 *
		JAXBContext jc = JAXBContext.newInstance(Actions.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		InputStream is = new FileInputStream(UTIL_GlobalConfig.getActionFile());
		Actions actions = (Actions) unmarshaller.unmarshal(is);

		// logger.info("marshall");
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(actions, System.out);
		*/
		
		
		/**
		 * 讀action json from txt
		 */
		
		//FileReader fr = new FileReader("/home/kkshop/kkshop.tw/WEB-INF/resource/actions_pa.json");
		FileReader fr = new FileReader(UTIL_GlobalConfig.getActionFile());		
		BufferedReader br = new BufferedReader(fr);
		StringBuffer actSb = new StringBuffer();		
		try {
			while (br.ready()) {
				actSb.append(br.readLine());
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// actionn string
		JSONArray actionArys = JSONArray.fromObject(actSb.toString());
		
		actionArys.forEach(obj->{
			JSONObject jo = (JSONObject) obj;
			Action act = new Action();
			act.setActionID(jo.getString("actionID"));
			act.setActionName(jo.getString("actionName"));
			act.setPageName(jo.getString("actionPage"));
			act.setClassName(jo.getString("actionClass"));
			act.setDesc(jo.getString("actionDesc"));
			HMS_Util.getOne().setAction(act.getActionID(), act);
		});

		/* xml 版
		List<Action> acts = ((Actions) actions).getAction();
		StringBuffer sb = new StringBuffer();
		JSONArray actionAry = new JSONArray();
		for (int i = 0; i < acts.size(); i++) {
			Action act = (Action) acts.get(i);
			String keyID = act.getActionID();
			//HMS_Util.getOne().setAction(keyID, act);
			
			
						
			JSONObject joj = new JSONObject();			
			joj.put("actionID", act.getActionID());
			joj.put("actionName", act.getActionName());
			joj.put("actionPage", act.getPageName());
			joj.put("actionClass", act.getClassName());
			joj.put("actionDesc", act.getDesc());			
			actionAry.add(joj);
			
			System.out.println(i + "--" + joj.toString());
		}
		*/
		//System.out.println(actionAry.toString());
		
		Action act = new Action();
		act.setActionID("404notFound");
		act.setClassName("com.hms.test");
		act.setPageName("/_errorpages/404.jsp");
		HMS_Util.getOne().setAction("404notFound", act);
		
		

	}

	/**
	 * 判斷dvp檔案在不在

	 */
	public static boolean existsDvpFile() throws ClassNotFoundException {
		String dvpFilePath = UTIL_GlobalConfig.getDvpConfigFile();
		logger.info(" uu dvpFilePath: " + dvpFilePath);
		boolean result = false;
		File file = new File(dvpFilePath);
		result = file.exists();
		return result;
	}

	public static String getDvpConfig(String keyID) throws ClassNotFoundException {

		String val = "";
		Properties prop = new Properties();
		try {
			// InputStream is =
			// UTIL_GlobalConfig.class.getClassLoader().getResourceAsStream(UTIL_GlobalConfig.getDvpConfigFile());
			InputStream is = new FileInputStream(UTIL_GlobalConfig.getDvpConfigFile());
			prop.load(is);
			val = prop.getProperty(keyID);
		} catch (IOException e) {
			val = "";
			e.printStackTrace();
		}
		;

		return val;
	}

	public static String getAuthPgmID(String comp, String role) {
		
		//logger.info(String.format("comp/role:%s/%s", comp, role));

		//String AuthPgmID = "";
		StringBuffer AuthPgmID = new StringBuffer();

		// 依群祖給予程式權限
		if (null!=role && "C".equals(role)) {
			AuthPgmID.append("HBD1W010,HBD1W010_ACTION,");
			AuthPgmID.append("HBD1W020,HBD1W020_ACTION,");
			AuthPgmID.append("HBD1W030,HBD1W030_ACTION,");
		}

		// 現場出貨員工
		if (null!=role && "E".equals(role)) {
			AuthPgmID.append("HBD1W310,HBD1W310_ACTION,");
			AuthPgmID.append("HBD1W320,HBD1W320_ACTION,");
			AuthPgmID.append("HBD1W330,HBD1W330_ACTION,");
			AuthPgmID.append("HBD1W510,HBD1W510_ACTION,");
			// 20221031 呆呆呂小姐: 現場可以重新開放預訂單
			AuthPgmID.append("HBD1W620,HBD1W620_ACTION,");
		}

		// 會計
		if (null!=role && "F".equals(role)) {
			AuthPgmID.append("HBD1W510,HBD1W510_ACTION,");
		}

		// 管理者
		if (null!=role && "A".equals(role)) {			
			AuthPgmID.append("HBD1W510,HBD1W510_ACTION,");
			AuthPgmID.append("HBD1W610,HBD1W610_ACTION,");
			AuthPgmID.append("HBD1W620,HBD1W620_ACTION,");
			AuthPgmID.append("HBD1W630,HBD1W630_ACTION,");
			AuthPgmID.append("HBD1W640,HBD1W640_ACTION,");
			AuthPgmID.append("HBD1W640,HBD1W640_ACTION,");
			AuthPgmID.append("HBD1W660,HBD1W660_ACTION,");
			AuthPgmID.append("HBD1W661,HBD1W661_ACTION,");
			// 20230227: 新增年度報表662
			AuthPgmID.append("HBD1W662,HBD1W662_ACTION,");
			AuthPgmID.append("HBD1W670,HBD1W670_ACTION,");
			AuthPgmID.append("HBD1W680,HBD1W680_ACTION,");
			AuthPgmID.append("HBD1W690,HBD1W690_ACTION,");
			
			// 管理者也有現場出貨的權限
			AuthPgmID.append("HBD1W310,HBD1W310_ACTION,");
			AuthPgmID.append("HBD1W320,HBD1W320_ACTION,");
			AuthPgmID.append("HBD1W330,HBD1W330_ACTION,");
			
		}

		return AuthPgmID.toString();
	}

	/**
	 * 若有 DVP FILE的話，就優先讀取DVP CONFIG 從dvp file得到使用者資訊
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static UserInfo getDvpUserInfo() throws ClassNotFoundException {

		UserInfo ui = new UserInfo();

		
//		ui.setCompCode(UTIL_GlobalConfig.getDvpConfig("COMP_CODE"));
//		ui.setLoginID(UTIL_GlobalConfig.getDvpConfig("LOGIN_ID"));
//		ui.setLoginRole(UTIL_GlobalConfig.getDvpConfig("LOGIN_ROLE"));

		return ui;

	}

	public static String getWebActionUrl(String webactionID) throws ClassNotFoundException {

		logger.info("UTIL_GlobalConfig.getWebActioinUrl(" + webactionID + ")");

		Properties prop = new Properties();
		try {
			prop.load(UTIL_GlobalConfig.class.getClassLoader().getResourceAsStream(UTIL_GlobalConfig.getConfigFile()));
		} catch (IOException e) {
			logger.error(e.toString());
			e.printStackTrace();
		}
		;

		String systemDomain = prop.getProperty("SYSTEM_DOMAIN");
		String systemController = prop.getProperty("SYSTEM_CONTROLLER");
		String url = systemDomain + systemController + "?webactionID=" + webactionID;

		return url;
	}

	public static String getWebActionUrl(String webActionID, String method) throws ClassNotFoundException {

		// logger.info("doDelete:#");
		logger.info("UTIL_GlobalConfig.getWebActionUrl()->actioMethod: " + method);
		String url = getWebActionUrl(webActionID) + "&actionMethod=" + method;
		return url;

	}

	public static HMS_TROW getWebAction(String webactionID) {

		HMS_TROW tr = HMS_Util.getOne().getDRB("WEBACTION_" + webactionID);

		if (null == tr) {
			// logger.error( "WEBACTION NOT FOUND[" + webactionID + "]" );
		}

		return tr;
	}

	public static Action getAction(String webactionID) {

		Action act = HMS_Util.getOne().getAction(webactionID);

		if (null == act) {
			// logger.error( "WEBACTION NOT FOUND[" + webactionID + "]" );
		}

		return act;
	}

	public static void postIssue11(String title, Exception ex) {

		// refresh config(webaction)
		StringBuffer sb = new StringBuffer();
		sb.append("   Insert into HMP0LOG(HMP0_LOG_UUID, HMP0_LOG_TIME, HMP0_LOG_MSG) ");
		sb.append("    Values(?, ?, ?)		");

		HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
		try {
			dbt01.openConnection();
			Object[] parms = new Object[] { UTIL_String.getNowString("yyyyMMddHHmmsss"),
					UTIL_String.getNowString("yyyyMMddHHmm"), "Error: " + title + "-" + ex.toString() };
			dbt01.executeNonquery(sb.toString(), parms);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.toString());
		} finally {
			// 找不到db時要怎麼將log顯示出來
			// dbt01.closeConnection();
		}

	}

	public static void postLog111(int logLevel, String msg) {

		// refresh config(webaction)
		StringBuffer sb = new StringBuffer();
		sb.append("   Insert into HMP0LOG(HMP0_LOG_UUID, HMP0_LOG_TIME, HMP0_LOG_MSG) ");
		sb.append("    Values(UUID(), SYSDATE(), ?)		");

		if (logLevel < 1) {
			// HMS_PrepareExecSQL dbt01 = new HMS_PrepareExecSQL();
			try {
				// dbt01.openConnection();
				// dbt01.executeNonquery(sb.toString(), new Object[]{"Info: " +
				// msg});
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.toString());
			} finally {
				// dbt01.closeConnection();
			}
		}

	}

	public static void postLog(String msg) {

		// refresh config(webaction)
		StringBuffer sb = new StringBuffer();
		sb.append("   Insert into HMP0LOG(HMP0_LOG_UUID, HMP0_LOG_TIME, HMP0_LOG_MSG) ");
		sb.append("    Values(UUID(), SYSDATE(), ?)		");
		logger.info(msg);

	}

	public static String getConfig(String keyID) throws SQLException, ClassNotFoundException {

		String val = "";
		Properties prop = new Properties();
		try {
			prop.load(UTIL_GlobalConfig.class.getClassLoader().getResourceAsStream(UTIL_GlobalConfig.getConfigFile()));
			val = prop.getProperty(keyID);
		} catch (IOException e) {
			val = "";
			e.printStackTrace();
		}
		;

		return val;
	}

}
