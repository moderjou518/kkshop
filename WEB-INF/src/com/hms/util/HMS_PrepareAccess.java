package com.hms.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hms.entity.HMS_TBLE;
import com.hms.entity.HMS_TROW;

public class HMS_PrepareAccess {

	private String space = "";
	private Connection conn = null;
	/** 限制arraylist的大小,避免getRecords產生過多資料,可以用setRowCount改變 */
	private int row_count = 9999;

	/** 初始化 PrepareExecSql() */
	public HMS_PrepareAccess() {
	}

	/**
	 * 初始化 PrepareExecSql(Connection conn) 並指定Connection
	 * 
	 * @param conn
	 */
	public HMS_PrepareAccess(Connection conn) {
		this.conn = conn;
	}

	/**
	 * 重新設定getRecords可讀取的最大筆數
	 * 
	 * @param row_count
	 *            getRecords可讀取的最大筆數
	 */
	public void setRowCount(int row_count) {
		this.row_count = row_count;
	}

	/**
	 * 初始化UTIL_WebConnection
	 * 
	 * @throws Exception
	 */
	public void openConnection(String connStr) throws Exception {
		if (this.conn == null) {
			this.conn = DriverManager.getConnection( connStr, "Admin", "nocgjldn" );	
		}		
	}
	
	public void openConnection(String connStr, String userID, String pwd) throws Exception {
		if (this.conn == null) {
			this.conn = DriverManager.getConnection( connStr , userID, pwd);	
		}		
	}	

	/**
	 * 關閉DB連線
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
	 * 使用preparedSql來取得多筆資料的所有欄位,defualt最大資料筆數為9999
	 * 
	 * @param sqlstmt
	 *            Prepared SQL
	 * @param parm
	 *            Object[]型態 ex: new Object[]{"ABC",new Integer(1)}
	 * @param record_index
	 *            指向第record_index筆資料並開始讀取其後的資料
	 * @return ArrayList 每筆record為String[]型態,ArrayList是record的集合
	 */
	public ArrayList getRecords(String sqlstmt, Object[] parm, int record_index) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList list = new ArrayList();

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
			String[] result = null;
			Object data_value = null;

			if (record_index > 1) {
				if (record_index > 1024) {
					rs.setFetchSize(1024);
				}
				for (int i = 1; i < record_index; i++) {
					rs.next();
				}
			}

			for (int j = 0; j < this.row_count; j++) {
				if (rs.next()) {
					result = new String[column_count];
					for (int i = 1; i <= column_count; i++) {
						data_value = rs.getObject(i);

						if (data_value != null) {
							result[i - 1] = data_value.toString();
						} else {
							result[i - 1] = this.space;
						}
					}
				} else {
					break;
				}

				list.add(result);
			}
			return list;
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
	}

	/**
	 * 使用preparedSql來取得多筆資料的所有欄位,defualt最大資料筆數為9999
	 * 
	 * @param sqlstmt
	 *            Prepared SQL
	 * @param parm
	 *            Object[]型態 ex: new Object[]{"ABC",new Integer(1)}
	 * @return ArrayList 每筆record為String[]型態,ArrayList是record的集合
	 */
	public ArrayList getRecords(String sqlstmt, Object[] parm) throws SQLException {
		return getRecords(sqlstmt, parm, 0);
	}

	public ArrayList getRecords(String sqlstmt) throws SQLException {
		return getRecords(sqlstmt, null, 0);
	}

	/**
	 * 使用preparedSql來取得第一筆資料的所有欄位
	 * 
	 * @param sqlstmt
	 *            ...Prepared SQL ..
	 * @param parm
	 *            Object[]型態 ex: new Object[]{"ABC",new Integer(1)}
	 * @return String[] 所有欄位值都轉成字串型態
	 */
	public String[] getARecord(String sqlstmt, Object[] parm) throws SQLException {
		return getARecord(sqlstmt, parm, 1);
	}

	/**
	 * 使用preparedSql來取得第record_index筆資料的所有欄位
	 * 
	 * @param sqlstmt
	 *            Prepared SQL
	 * @param parm
	 *            Object[]型態 ex: new Object[]{"ABC",new Integer(1)}
	 * @param record_index
	 *            指向第record_index筆資料並讀取其資料
	 * @return String[] 所有欄位值都轉成字串型態
	 */
	public String[] getARecord(String sqlstmt, Object[] parm, int record_index) throws SQLException {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String[] result = null;

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
			Object data_value = null;

			if (record_index > 1) {
				if (record_index > 99) {
					rs.setFetchSize(record_index);
				}
				for (int i = 1; i < record_index; i++) {
					rs.next();
				}
			}

			if (rs.next()) {
				result = new String[column_count];

				for (int i = 1; i <= column_count; i++) {
					data_value = rs.getObject(i);

					if (data_value != null) {
						result[i - 1] = data_value.toString();
					} else {
						result[i - 1] = this.space;
					}
				}
			}
			return result;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getARecord.rs:" + e.getMessage());
				}
				rs = null;
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getARecord.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}
	}

	/**
	 * 使用preparedSql來取得第一筆資料第一個欄位的int型態
	 * 
	 * @param sqlstmt
	 *            Prepared SQL
	 * @param parm
	 *            Object[]型態 ex: new Object[]{"ABC",new Integer(1)}
	 * @return int 回傳一整數
	 */
	public int getAInt(String sqlstmt, Object[] parm) throws SQLException {

		int result = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = conn.prepareStatement(sqlstmt);

			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}

			rs = pstmt.executeQuery();
			if (this.conn.isClosed())
				throw new SQLException("Connection is closed.");

			if (rs.next()) {
				result = rs.getInt(1);
			}

			return result;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getAInt.pstmt:" + e.getMessage());
				}
				rs = null;
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getAInt.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}
	}

	/**
	 * 使用preparedSql來取得第一筆資料第一個欄位的double型態
	 * 
	 * @param sqlstmt
	 *            ...Prepared SQL ..
	 * @param parm
	 *            Object[]型態 ex: new Object[]{"ABC",new Integer(1)}
	 * @return double 回傳一倍精度浮點數
	 */
	public double getADouble(String sqlstmt, Object[] parm) throws SQLException {

		double result = 0;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sqlstmt);

			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}

			rs = pstmt.executeQuery();
			if (this.conn.isClosed())
				throw new SQLException("Connection is closed.");

			if (rs.next()) {
				result = rs.getDouble(1);
			}
			return result;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getADouble.rs:" + e.getMessage());
				}
				rs = null;
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getADouble.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}
	}

	/**
	 * 使用preparedSql來取得第一筆資料第一個欄位的String型態
	 * 
	 * @param sqlstmt
	 *            PreparedSQL
	 * @param parm
	 *            Object[]型態 ex: new Object[]{"ABC",new Integer(1)}
	 * @return String 回傳一字串
	 */
	public String getAString(String sqlstmt, Object[] parm) throws SQLException {

		String result = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try {
			pstmt = conn.prepareStatement(sqlstmt);

			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}

			rs = pstmt.executeQuery();
			if (this.conn.isClosed())
				throw new SQLException("Connection is closed.");

			if (rs.next()) {
				Object tmp = rs.getObject(1);

				if (tmp != null) {
					result = tmp.toString();
				} else {
					result = space;
				}
			}

			return result;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getAString.rs:" + e.getMessage());
				}
				rs = null;
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getAString.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}
	}

	/**
	 * 執行一筆 sql 的新增,刪除,更新等動作
	 * 
	 * @param sqlstmt
	 *            一筆Prepared SQL
	 * @param parm
	 *            Object[]型態 ex: new Object[]{"ABC",new Integer(1)}
	 * @return int 被異動的資料筆數.
	 */
	public int updateSql(String sqlstmt, Object[] parm) throws SQLException {

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

	/**
	 * 
	 * @param sqlstmt
	 * @return
	 * @throws SQLException
	 */
	public String[] getColumns(String sqlstmt) throws SQLException {
		return this.getColumns(sqlstmt, null);

	}

	/**
	 * 取得資料集的欄位名稱陣列
	 * 
	 * @param sqlstmt
	 * @param parm
	 * @param record_index
	 * @return
	 * @throws SQLException
	 */
	public String[] getColumns(String sqlstmt, Object[] parm) throws SQLException {

		ResultSet rs = null;
		String[] result = null;
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

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCount = rsmd.getColumnCount();
			result = new String[columnCount];

			for (int i = 1; i < columnCount + 1; i++) {
				String name = rsmd.getColumnName(i);
				result[i - 1] = name;
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getColumns.rs:" + e.getMessage());
				}
				rs = null;
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("EC_PrepareExecSql.getColumn.pstmt:" + e.getMessage());
				}
				pstmt = null;
			}
		}

		return result;
	}

}
