package com.hms.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;

public class HMS_PrepareExecSQL {

	private String space = "";
	private Connection conn = null;
	/** ??�制arraylist??�大�?,?��??�getRecords?��??��?��?��?��??,?��以用setRowCount?���? */
	private int row_count = 9999;

	/** ??��?��?? PrepareExecSql() */
	public HMS_PrepareExecSQL() {
	}

	/**
	 * ??��?��?? PrepareExecSql(Connection conn) 並�?��?�Connection
	 * 
	 * @param conn
	 */
	public HMS_PrepareExecSQL(Connection conn) {
		this.conn = conn;
	}

	/**
	 * ??�新設�?�getRecords?���???��?��?大�?�數
	 * 
	 * @param row_count
	 *            getRecords?���???��?��?大�?�數
	 */
	public void setRowCount(int row_count) {
		this.row_count = row_count;
	}

	/**
	 * ??��?��?�UTIL_WebConnection
	 * 
	 * @throws Exception
	 */
	public void openConnection() throws Exception {		
		if (this.conn == null) {
			this.conn = UTIL_GlobalConfig.getConnection();			
			if (this.conn == null){
				throw new Exception(String.format("No DB Found"));
			}
		}
	}
	
	public void openConnection(String dbHost) throws Exception {		
		if (this.conn == null) {
			this.conn = UTIL_GlobalConfig.getConnection(dbHost);			
			if (this.conn == null){
				throw new Exception(String.format("No DB Found"));
			}
		}
	}

	/**
	 * ??��?�DB??�?
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() {
		if (this.conn != null) {
			try {			
				this.conn.close();
				this.conn = null;
			} catch (SQLException e) {
				System.out.println("*** closeConnection Error ***");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 設�?�自??�commit
	 * @param cusCommit
	 * @throws SQLException
	 */
	public void setAutoCommit(boolean cusCommit) throws SQLException{
		this.conn.setAutoCommit(cusCommit);	
	}
	
	public void commit(){
		try {			
			this.conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rollBack(){
		try {			
			this.conn.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param sqlstmt
	 * @param parm
	 * @return
	 * @throws SQLException
	 */
	public HMS_TBLE queryDataTable(String sqlstmt, Object[] parm) throws SQLException {

		HMS_TBLE dt = null;
		HMS_TROW dr = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = this.conn.prepareStatement(sqlstmt);
			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			rs = pstmt.executeQuery();
			if (this.conn.isClosed())
				throw new SQLException("Connection is closed.");
			ResultSetMetaData md = rs.getMetaData();
			int column_count = md.getColumnCount();
			Object dataValue = null;

			dt = new HMS_TBLE();
			for (int j = 0; j < this.row_count; j++) {
				if (rs.next()) {
					dr = new HMS_TROW();
					for (int i = 1; i <= column_count; i++) {						
						dataValue = rs.getObject(i);
						if (dataValue != null) {							
							dr.SetCell(md.getColumnName(i).toString(), dataValue.toString());					
						} else {
							dr.SetCell(md.getColumnName(i), "");
						}						
					}
					dt.CreateDataRow(dr);
				} else {
					break;
				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getRecords.rs:" + e.getMessage());
				}
				rs = null;
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getRecords.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}
		return dt;
	}
	
	/**
	 * 
	 * @param sqlstmt
	 * @param parm
	 * @return
	 * @throws SQLException
	 */
	public HMS_TROW queryDataRow(String sqlstmt, Object[] parm) throws SQLException {
		
		HMS_TROW dr = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = this.conn.prepareStatement(sqlstmt);
			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			rs = pstmt.executeQuery();
			if (this.conn.isClosed())
				throw new SQLException("Connection is closed.");
			ResultSetMetaData md = rs.getMetaData();
			int column_count = md.getColumnCount();
			Object dataValue = null;
			 
			for (int j = 0; j < this.row_count; j++) {
				if (rs.next()) {
					dr = new HMS_TROW();
					for (int i = 1; i <= column_count; i++) {						
						dataValue = rs.getObject(i);
						if (dataValue != null) {							
							dr.SetCell(md.getColumnName(i).toString(), dataValue.toString());					
						} else {
							dr.SetCell(md.getColumnName(i), "");
						}						
					}					
					break;
				}
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getRecords.rs:" + e.getMessage());
				}
				rs = null;
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getRecords.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}
		return dr;
	}
	
	public int queryAInt(String sqlstmt, Object[] parm) throws SQLException {
		
		int i = 0;
		String val = "0";		
		val = this.queryAString(sqlstmt, parm);		
		i = Integer.parseInt(val);
		
		return i;
	}
	
	public Double queryADouble(String sqlstmt, Object[] parm) throws SQLException {
		
		double i = 0;
		String val = "0";		
		val = this.queryAString(sqlstmt, parm);		
		i = Double.parseDouble(val);
		
		return i;
	}
	
/**
 * ??��?�到資�?��?��?�然??��?�傳空�?�串
 * @param sqlstmt
 * @param parm
 * @return
 * @throws SQLException
 */
	public String queryAString(String sqlstmt, Object[] parm) throws SQLException {
		
		String val = "";		
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = this.conn.prepareStatement(sqlstmt);
			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			rs = pstmt.executeQuery();
			if (this.conn.isClosed())
				throw new SQLException("Connection is closed.");
			if (rs.next()) {
				val = rs.getString(1);				
			}else{
				val = "";
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getRecords.rs:" + e.getMessage());
				}
				rs = null;
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getRecords.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}
		return val;
	}


	/**
	 * ?��行�?�? sql ??�新�?,?��?��,?��?��等�?��??
	 * 
	 * @param sqlstmt
	 *            �?筆Prepared SQL
	 * @param parm
	 *            Object[]??��?? ex: new Object[]{"ABC",new Integer(1)}
	 * @return int 被異??��?��?��?��?�數.
	 */
	public int executeNonquery(String sqlstmt, Object[] parm) throws SQLException {

		int result = 0;
		PreparedStatement pstmt = null;

		try {
			pstmt = this.conn.prepareStatement(sqlstmt);

			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			result = pstmt.executeUpdate();
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.updateSql.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}

		return result;
	}
}
