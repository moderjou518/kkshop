package com.evergreen_hotels.bmg.whms1.util;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class GenericDao
{
	//boolean standalone;
	Connection conn;

	/*
	public GenericDao() {
		standalone = false;
	}
	*/

	/*
	public GenericDao(boolean standalone) {
		//this.standalone = standalone;
	}
	*/
	
	public void setConnection(Connection conn) throws SQLException {
		this.conn = conn;
	}

	private Connection getConnection() throws SQLException {
		return this.conn;
	}

	protected PrepareExecuteSql getPrepareExecuteSql() throws SQLException {
		return new PrepareExecuteSql(this.conn);
	}

	protected void setAutoCommit(boolean autoCommit) throws SQLException {
		getConnection().setAutoCommit(autoCommit);
	}

	protected void commit() throws SQLException {
		Connection conn = getConnection();
		if (!conn.getAutoCommit()) {
			conn.commit();
		}
	}

	protected void rollback() throws SQLException {
		Connection conn = getConnection();
		if (!conn.getAutoCommit()) {
			conn.rollback();
		}
	}

	protected void closeConnection() throws SQLException {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (SQLException localSQLException) {
			}
		}
	}

}