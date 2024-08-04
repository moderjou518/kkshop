package com.evergreen_hotels.bmg.whms1.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
//import oracle.sql.ROWID;

import com.hms.util.UTIL_GlobalConfig;

public class PrepareExecuteSql
{
	private String space;
	private Connection conn;
	private int max_rows;
	
	/**
	 * add by mj
	 * @param dbHost
	 * @throws Exception
	 */
	public PrepareExecuteSql() throws Exception{
		this.space = "";
		this.max_rows = 9999;		
	}

	public PrepareExecuteSql(Connection connection)
	{
		this.space = "";
		this.max_rows = 9999;
		this.conn = connection;
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
	
	public Connection getConnection(){
		return this.conn;
	}
	
	public void setConnection(Connection conn){
		this.conn = conn;
	}

	public void setMaxRows(int max_rows) {
		this.max_rows = max_rows;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public RowDataList getRowDataList(String sqlstmt, Object[] parm, int start_idx, int max_rows)
			throws SQLException
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			if (this.conn.isClosed()) {
				throw new SQLException(getClass().getName() + ": Connection is closed.");
			}
			pstmt = this.conn.prepareStatement(sqlstmt, 1003, 1007);

			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}

			rs = pstmt.executeQuery();

			setFetchSize(rs, start_idx);

			if (start_idx > 1) {
				setFetchSize(rs, start_idx);

				for (int i = 1; i < start_idx; i++) {
					rs.next();
				}
			}
			return fetchResultSet2List(rs, max_rows);
		} finally {
			JDBCUtil.safelyClose(rs);
			JDBCUtil.safelyClose(pstmt);
		}
	}

	/*
	 * public RowDataList getRowDataList(String sqlstmt, Object[] parm, int
	 * start_idx) throws SQLException { return getRowDataList(sqlstmt, parm,
	 * start_idx, max_rows); }
	 */

	public RowDataList getRowDataList(String sqlstmt, Object[] parm)
			throws SQLException
	{
		return getRowDataList(sqlstmt, parm, 1, max_rows);
	}

	/*
	 * public RowDataList getRowDataList(String sqlstmt) throws SQLException {
	 * return getRowDataList(sqlstmt, null, 1, max_rows); }
	 */

	public List<RowData> getRowData(String sqlstmt, Object[] parm, int start_idx, int max_rows)
			throws SQLException
	{
		return getRowDataList(sqlstmt, parm, start_idx, max_rows).toRowDataList();
	}

	/*
	 * public List<RowData> getRowData(String sqlstmt, Object[] parm, int
	 * start_idx) throws SQLException { return getRowDataList(sqlstmt, parm,
	 * start_idx).toRowDataList(); }
	 */

	public List<RowData> getRowData(String sqlstmt, Object[] parm)
			throws SQLException
	{
		return getRowData(sqlstmt, parm, 1, max_rows);
	}

	/*
	 * public List<RowData> getRowData(String sqlstmt) throws SQLException {
	 * return getRowData(sqlstmt, null, 1, max_rows); }
	 */

	public RowData getARowData(String sqlstmt, Object[] parm, int start_idx)
			throws SQLException
	{
		List<RowData> list = getRowData(sqlstmt, parm, start_idx, 1);
		if (list.size() > 0) {
			return (RowData) list.get(0);
		}
		return null;
	}

	public RowData getARowData(String sqlstmt, Object[] parm)
			throws SQLException
	{
		return getARowData(sqlstmt, parm, 1);
	}

	/*
	 * public RowData getARowData(String sqlstmt) throws SQLException { return
	 * getARowData(sqlstmt, null, 1); }
	 */

	public String[] getARecord(String sqlstmt, Object[] parm, int start_idx)
			throws SQLException
	{
		RowData data = getARowData(sqlstmt, parm, 1);
		if (data == null) {
			return null;
		}
		return data.toStringArray();
	}

	public String[] getARecord(String sqlstmt, Object[] parm) throws SQLException
	{
		return getARecord(sqlstmt, parm, 1);
	}

	/*
	 * public String[] getARecord(String sqlstmt) throws SQLException { return
	 * getARecord(sqlstmt, null); }
	 */

	public int getAInt(String sqlstmt, Object[] parm)
			throws SQLException
	{
		String[] strArr = getARecord(sqlstmt, parm);
		if (strArr != null) {
			return Integer.parseInt(strArr[0]);
		}
		return 0;
	}

	/*
	 * public int getAInt(String sqlstmt) throws SQLException { String[] strArr
	 * = getARecord(sqlstmt); if (strArr != null) { return
	 * Integer.parseInt(strArr[0]); } return 0; }
	 */

	public double getADouble(String sqlstmt, Object[] parm)
			throws SQLException
	{
		String[] strArr = getARecord(sqlstmt, parm);
		if (strArr != null) {
			return Double.parseDouble(strArr[0]);
		}
		return 0.0D;
	}

	/*
	 * public double getADouble(String sqlstmt) throws SQLException { String[]
	 * strArr = getARecord(sqlstmt); if (strArr != null) { return
	 * Double.parseDouble(strArr[0]); } return 0.0D; }
	 */

	public String getAString(String sqlstmt, Object[] parm) throws SQLException
	{
		String[] record = getARecord(sqlstmt, parm, 1);
		if (record == null) {
			return null;
		}
		return record[0];
	}

	/*
	 * public String getAString(String sqlstmt) throws SQLException { String[]
	 * record = getARecord(sqlstmt); if (record == null) { return null; } return
	 * record[0]; }
	 */

	public boolean execute(String sqlstmt) throws SQLException
	{
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sqlstmt);
			return pstmt.execute(sqlstmt);
		} finally {
			JDBCUtil.safelyClose(pstmt);
		}
	}

	/*
	 * public int updateSql(String sqlstmt) throws SQLException { return
	 * updateSql(sqlstmt, null); }
	 */

	public int updateSql(String sqlstmt, Object[] parm) throws SQLException
	{
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sqlstmt);
			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}
			return pstmt.executeUpdate();
		} finally {
			JDBCUtil.safelyClose(pstmt);
		}
	}

	/*
	 * public int[] updateSqls(ArrayList<String> sqlstmts) throws SQLException {
	 * return updateSqls(sqlstmts, null, true); }
	 */

	public int[] updateSqls(ArrayList<String> sqlstmts, ArrayList<Object[]> parms)
			throws SQLException
	{
		return updateSqls(sqlstmts, parms, true);
	}

	public int[] updateSqls(ArrayList<String> sqlstmts, ArrayList<Object[]> parms, boolean enableZero)
			throws SQLException
	{
		return updateSqls(sqlstmts, parms, enableZero, false);
	}

	public int[] updateSqls(ArrayList<String> sqlstmts, ArrayList<Object[]> parms, boolean enableZero, boolean isAutoCommit)
			throws SQLException
	{
		int i = 0;
		PreparedStatement pstmt = null;
		int[] result = new int[sqlstmts.size()];
		try
		{
			if (!isAutoCommit) {
				conn.setAutoCommit(false);
			}
			for (i = 0; i < result.length; i++) {
				String sqlstmt = (String) sqlstmts.get(i);

				if (sqlstmt != null) {
					pstmt = conn.prepareStatement(sqlstmt);

					if (parms != null) {
						Object[] parm = (Object[]) parms.get(i);

						for (int j = 0; j < parm.length; j++) {
							pstmt.setObject(j + 1, parm[j]);
						}
					}

					pstmt.executeUpdate();
					pstmt.close();
					pstmt = null;

					if ((!enableZero) && (result[i] == 0)) {
						throw new SQLException("SQL Stmt Update No Record!", "", 999);
					}
				}
			}
			if (!isAutoCommit) {
				conn.commit();
				conn.setAutoCommit(true);
			}
			return result;
		} catch (SQLException sqle) {
			try {
				if (!isAutoCommit) {
					conn.rollback();
					conn.setAutoCommit(true);
				}
			} catch (SQLException localSQLException1) {
			}
			throw new SQLException(sqle.getMessage() + " at STMT:" + i, sqle.getSQLState(), sqle.getErrorCode());
		} finally {
			JDBCUtil.safelyClose(pstmt);
		}
	}

	public CallableStatement createCallableStatement(String sqlstmt)
			throws SQLException
	{
		return conn.prepareCall(sqlstmt);
	}

	public List<RowData> callProcedureQuery(String sqlstmt, Object[] parm, int start_idx)
			throws SQLException
	{
		CallableStatement cstmt = null;
		ResultSet rs = null;
		try {
			cstmt = conn.prepareCall(sqlstmt);
			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					cstmt.setObject(i + 1, parm[i]);
				}
			}
			rs = cstmt.executeQuery();

			setFetchSize(rs, start_idx);

			if (start_idx > 1) {
				for (int i = 1; i < start_idx; i++) {
					rs.next();
				}
			}
			return fetchResultSet2List(rs, max_rows).toRowDataList();
		} finally {
			JDBCUtil.safelyClose(rs);
			JDBCUtil.safelyClose(cstmt);
		}
	}

	private RowDataList fetchResultSet2List(ResultSet rs, int max_rows) throws SQLException {
		
		RowDataContainer container = new RowDataContainer();
		container.setMaxRows(max_rows);
		container.setSpace(space);
		container.fetchData(rs);
		return container.getRowDataList();
	}

	private void setFetchSize(ResultSet rs, int start_idx) throws SQLException{
		if (start_idx > 512) {
			rs.setFetchSize(512);
		}
	}

	public ArrayList<String[]> getRecords(String sqlstmt) throws SQLException {
		return getRecords(sqlstmt, null, 0, false);
	}

	public ArrayList<String[]> getRecords(String sqlstmt, Object[] parm)
			throws SQLException
	{
		return getRecords(sqlstmt, parm, 0, false);
	}

	public ArrayList<String[]> getRecords(String sqlstmt, Object[] parm, int record_index)
			throws SQLException
	{
		return getRecords(sqlstmt, parm, record_index, false);
	}

	public ArrayList<String[]> getRecords(String sqlstmt, Object[] parm, int record_index, boolean is_rowid_mode)
			throws SQLException
	{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<String[]> list = new ArrayList();
		try
		{
			pstmt = conn.prepareStatement(sqlstmt);

			if (parm != null) {
				for (int i = 0; i < parm.length; i++) {
					pstmt.setObject(i + 1, parm[i]);
				}
			}

			rs = pstmt.executeQuery();
			if (conn.isClosed())
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

			for (int j = 0; j < max_rows; j++) {
				if (!rs.next())
					break;
				result = new String[column_count];

				for (int i = 1; i <= column_count; i++) {
					data_value = rs.getObject(i);

					if (data_value != null) {
						int type = md.getColumnType(i);
						if (type == 2005) {
							result[(i - 1)] = rs.getString(i);
						} else {
							result[(i - 1)] = data_value.toString();
						}
					} else {
						result[(i - 1)] = space;
					}

					/* mark by mj
					if ((i == column_count) && (is_rowid_mode)) {
						result[(i - 1)] = ((ROWID) rs.getObject(i)).stringValue();
					}
					*/
				}

				list.add(result);
			}
			return list;
		} finally {
			JDBCUtil.safelyClose(rs);
			JDBCUtil.safelyClose(pstmt);
		}
	}

	public RowDataList getRowDataList(String sql, Map<String, Object> parm, int start_idx, int max_rows)
			throws SQLException
	{
		NamedParameterStatement npstmt = null;
		ResultSet rs = null;
		try {
			if (conn.isClosed()) {
				throw new SQLException(getClass().getName() + ": Connection is closed.");
			}
			if (sql.indexOf("?") > 0) {
				throw new SQLException("SQL statement can't contain the '?' char.");
			}
			npstmt = new NamedParameterStatement(conn, sql);
			if (parm != null) {
				Iterator<String> it = parm.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					if (key == null) {
						npstmt.setString(key, null);
					} else {
						npstmt.setObject(key, parm.get(key));
					}
				}
			}
			rs = npstmt.executeQuery();

			setFetchSize(rs, start_idx);

			if (start_idx > 1) {
				setFetchSize(rs, start_idx);

				for (int i = 1; i < start_idx; i++) {
					rs.next();
				}
			}
			return fetchResultSet2List(rs, max_rows);
		} finally {
			JDBCUtil.safelyClose(rs);
			JDBCUtil.safelyClose(npstmt);
		}
	}

	public RowDataList getRowDataList(String sqlstmt, Map<String, Object> parm, int start_idx)
			throws SQLException
	{
		return getRowDataList(sqlstmt, parm, start_idx, max_rows);
	}

	public RowDataList getRowDataList(String sqlstmt, Map<String, Object> parm)
			throws SQLException
	{
		return getRowDataList(sqlstmt, parm, 1, max_rows);
	}

	public List<RowData> getRowData(String sqlstmt, Map<String, Object> parm, int start_idx, int max_rows)
			throws SQLException
	{
		return getRowDataList(sqlstmt, parm, start_idx, max_rows).toRowDataList();
	}

	public List<RowData> getRowData(String sqlstmt, Map<String, Object> parm, int start_idx)
			throws SQLException
	{
		return getRowDataList(sqlstmt, parm, start_idx).toRowDataList();
	}

	public List<RowData> getRowData(String sqlstmt, Map<String, Object> parm)
			throws SQLException
	{
		return getRowData(sqlstmt, parm, 1, max_rows);
	}

	public RowData getARowData(String sqlstmt, Map<String, Object> parm, int start_idx)
			throws SQLException
	{
		List<RowData> list = getRowData(sqlstmt, parm, start_idx, 1);
		if (list.size() > 0) {
			return (RowData) list.get(0);
		}
		return null;
	}

	public RowData getARowData(String sqlstmt, Map<String, Object> parm)
			throws SQLException
	{
		return getARowData(sqlstmt, parm, 1);
	}

	public String[] getARecord(String sqlstmt, Map<String, Object> parm, int start_idx)
			throws SQLException
	{
		RowData data = getARowData(sqlstmt, parm, 1);
		if (data == null) {
			return null;
		}
		return data.toStringArray();
	}

	public String[] getARecord(String sqlstmt, Map<String, Object> parm) throws SQLException
	{
		return getARecord(sqlstmt, parm, 1);
	}

	public int getAInt(String sqlstmt, Map<String, Object> parm)
			throws SQLException
	{
		String[] strArr = getARecord(sqlstmt, parm);
		if (strArr != null) {
			return Integer.parseInt(strArr[0]);
		}
		return 0;
	}

	public double getADouble(String sqlstmt, Map<String, Object> parm)
			throws SQLException
	{
		String[] strArr = getARecord(sqlstmt, parm);
		if (strArr != null) {
			return Double.parseDouble(strArr[0]);
		}
		return 0.0D;
	}

	public String getAString(String sqlstmt, Map<String, Object> parm) throws SQLException
	{
		String[] record = getARecord(sqlstmt, parm, 1);
		if (record == null) {
			return null;
		}
		return record[0];
	}

	public int updateSql(String sqlstmt, Map<String, Object> parm)
			throws SQLException
	{
		NamedParameterStatement npstmt = null;
		try {
			if (conn.isClosed()) {
				throw new SQLException(getClass().getName() + ": Connection is closed.");
			}
			if (sqlstmt.indexOf("?") > 0) {
				throw new SQLException("SQL statement can't contain the '?' char.");
			}
			npstmt = new NamedParameterStatement(conn, sqlstmt);
			if (parm != null) {
				Iterator<String> it = parm.keySet().iterator();
				while (it.hasNext()) {
					String key = (String) it.next();
					if (key == null) {
						npstmt.setString(key, null);
					} else {
						npstmt.setObject(key, parm.get(key));
					}
				}
			}
			return npstmt.executeUpdate();
		} finally {
			JDBCUtil.safelyClose(npstmt);
		}
	}

	public int[] updateSqls(List<String> sqlstmts, List<Map<String, Object>> parms)
			throws SQLException
	{
		return updateSqls(sqlstmts, parms, true);
	}

	public int[] updateSqls(List<String> sqlstmts, List<Map<String, Object>> parms, boolean enableZero)
			throws SQLException
	{
		return updateSqls(sqlstmts, parms, enableZero, false);
	}

	public int[] updateSqls(List<String> sqlstmts, List<Map<String, Object>> parms, boolean enableZero, boolean isAutoCommit)
			throws SQLException
	{
		int i = 0;
		NamedParameterStatement npstmt = null;
		int[] result = new int[sqlstmts.size()];
		try
		{
			if (!isAutoCommit) {
				conn.setAutoCommit(false);
			}
			for (i = 0; i < result.length; i++) {
				String sqlstmt = (String) sqlstmts.get(i);

				if (sqlstmt != null) {
					npstmt = new NamedParameterStatement(conn, sqlstmt);

					if (parms != null) {
						Map<String, Object> parm = (Map) parms.get(i);
						if (parm != null) {
							Iterator<String> it = parm.keySet().iterator();
							while (it.hasNext()) {
								String key = (String) it.next();
								if (key == null) {
									npstmt.setString(key, null);
								} else {
									npstmt.setObject(key, parm.get(key));
								}
							}
						}
					}
					npstmt.executeUpdate();
					npstmt.close();
					npstmt = null;

					if ((!enableZero) && (result[i] == 0)) {
						throw new SQLException("SQL Stmt Update No Record!", "", 999);
					}
				}
			}
			if (!isAutoCommit) {
				conn.commit();
				conn.setAutoCommit(true);
			}
			return result;
		} catch (SQLException sqle) {
			try {
				if (!isAutoCommit) {
					conn.rollback();
					conn.setAutoCommit(true);
				}
			} catch (SQLException localSQLException1) {
			}
			throw new SQLException(sqle.getMessage() + " at STMT:" + i, sqle.getSQLState(), sqle.getErrorCode());
		} finally {
			JDBCUtil.safelyClose(npstmt);
		}
	}
}