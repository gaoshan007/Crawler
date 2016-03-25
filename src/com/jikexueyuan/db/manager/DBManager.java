package com.jikexueyuan.db.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

public class DBManager {

	private DBManager() {
		try {
			JAXPConfigurator.configure(DBPool.getDBPool().getPoolPath(), false);
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(String poolName) throws SQLException {
		return DriverManager.getConnection(poolName);
	}
	
	private static class DBManagerDao {
		private static DBManager dbManager = new DBManager();
	}
	
	public static DBManager getDBManager() {
		return DBManagerDao.dbManager;
	}
	
	
	public static void main(String[] args) {
		
	}
	
}
