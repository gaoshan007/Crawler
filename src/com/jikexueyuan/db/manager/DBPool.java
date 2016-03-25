/**
 * 
 */
package com.jikexueyuan.db.manager;

import com.jikexueyuan.util.ClassUtil;


public class DBPool {

	private String poolPath;
	
	private DBPool() {
	
	}
	
	private static class DBPoolDao {
		private static DBPool dbPool = new DBPool();
	}
	
	public static DBPool getDBPool() {
		return DBPoolDao.dbPool;
	}
	
	public String getPoolPath() {
		if(poolPath == null) {
			poolPath = ClassUtil.getClassPath(DBPool.class) + "proxool.xml";
		}
		return poolPath;
	}

	public void setPoolPath(String poolPath) {
		this.poolPath = poolPath;
	}


	public static void main(String[] args) {
		
		// TODO Auto-generated method stub

	}

}
