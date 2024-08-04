package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;
import com.hms.util.HMS_PrepareAccess;

public class testAccess {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub		
		test1();
		

	}
	
	static void test1(){
		// Connection conn=DriverManager.getConnection("jdbc:ucanaccess://<mdb or accdb file path>",user, password);
				// for example:
				try {
					
					System.out.println("SYSENV: " + System.getenv("APPDATA"));
					
					Class.forName("net.ucanaccess.jdbc.UcanaccessDriver"); /* often not required for Java 6 and later (JDBC 4.x) */
					String connStr = "jdbc:ucanaccess://C:/www/hmsdb/image/HMS0_MISC.mdb;memory=true";
					Connection conn = DriverManager.getConnection(connStr, "Admin", "nocgjldn");
					HMS_PrepareAccess prep = new HMS_PrepareAccess(conn);
					
					//prep.openConnection(connStr, userID, pwd)
					
					conn.setAutoCommit(false);
					
					
					HMS_TBLE dt = prep.queryDataTable("select *　from hms0_misc where misc_kind = ? and misc_code = ? ", new Object[]{"ROOT", "WEBACTION"});
					
					for(int i = 0; i < dt.GetRowsCount();i++){
						HMS_TROW dr = dt.getDataRow(i);
						System.out.println("before update- " + dr.GetField("misc_desc"));
					}
					
					Object[] parms = new Object[]{"test111", "ROOT", "WEBACTION"};
					int c = prep.updateSql("Update hms0_misc set misc_desc = ? Where misc_kind = ? and misc_code = ? ", parms);
					
					System.out.println("c: " + c);
					
					dt = prep.queryDataTable("select *　from hms0_misc where misc_kind = ? and misc_code = ? ", new Object[]{"ROOT", "WEBACTION"});
					
					for(int i = 0; i < dt.GetRowsCount();i++){
						HMS_TROW dr = dt.getDataRow(i);
						System.out.println(" after update- " + dr.GetField("misc_desc"));
					}
					
					conn.rollback();
					
					dt = prep.queryDataTable("select *　from hms0_misc where misc_kind = ? and misc_code = ? ", new Object[]{"ROOT", "WEBACTION"});
					
					for(int i = 0; i < dt.GetRowsCount();i++){
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
	
	static void test2() throws SQLException{
		Connection conn = null;
		try {			
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			//System.out.println(String.format("!!%s:%s, %s/%s", dbCode, connStr, uid, pwd));
			//conn = DriverManager.getConnection(connStr, uid, pwd);	
			String connStr = "jdbc:ucanaccess://C:/home/hms/hms.tw/WEB-INF/resource/HMS0_MISC.mdb;memory=true";
			conn = DriverManager.getConnection(connStr);
			System.out.println(conn);
		} catch( ClassNotFoundException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
