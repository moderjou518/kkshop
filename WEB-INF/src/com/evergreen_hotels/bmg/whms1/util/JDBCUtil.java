package com.evergreen_hotels.bmg.whms1.util;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
//import oracle.jdbc.pool.OracleDataSource;

public class JDBCUtil
{

	/*
	public static Connection getConnection() throws SQLException
	{
		return DataSourceLocator.getInstance().getDataSource().getConnection();
	}
	*/

	public static void safelyClose(Connection conn)
	{
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException localSQLException) {
			}
		}
	}

	public static void safelyClose(PreparedStatement pstmt)
	{
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException localSQLException) {
			}
		}
	}

	public static void safelyClose(ResultSet rs)
	{
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException localSQLException) {
			}
		}
	}

	public static void safelyClose(NamedParameterStatement npstmt)
	{
		if (npstmt != null) {
			try {
				npstmt.close();
			} catch (SQLException localSQLException) {
			}
		}
	}

}