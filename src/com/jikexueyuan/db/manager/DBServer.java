package com.jikexueyuan.db.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DBServer {
	
	private DBOperation dbOperation;
	
	public DBServer(String poolName) {
		dbOperation = new DBOperation(poolName);
	}
	
	public void close() {
		dbOperation.close();
	}
	
	//数据库“增”方法
	public int insert(String sql) throws SQLException {
		return dbOperation.executeUpdate(sql);
	}
	
	//数据库“增”方法
	public int insert(String tableName, String columns, HashMap<Integer, Object> params) throws ClassNotFoundException, SQLException {
		String sql = insertSql(tableName, columns);
		return dbOperation.executeUpdate(sql, params);
	}
	
	//数据库的删除操作
	public int delete(String sql) throws SQLException {
		return dbOperation.executeUpdate(sql);
	}
	//数据库的删除操作
	public int delete(String tableName, String condition) throws SQLException {
		if(null == tableName) {
			return 0;
		}
		String sql = "delete from " + tableName + " " + condition;
		return dbOperation.executeUpdate(sql);
	}
	
	//数据库更新操作
	public int update(String sql) throws SQLException {
		return dbOperation.executeUpdate(sql);
	}
	
	//数据库更新操作
	public int update(String tableName, String columns, String condition, HashMap<Integer, Object> params) throws ClassNotFoundException, SQLException {
		String sql = updateSql(tableName, columns, condition);
		return dbOperation.executeUpdate(sql, params);
	}
	
	//数据库查询语句
	public ResultSet select(String sql) throws SQLException {
		return dbOperation.executeQuery(sql);
	}
	
	//数据库查询操作
	public ResultSet select(String tableName, String columns, String condition) throws SQLException {
		String sql = "select " + columns + "from" + tableName + " " + condition;
		return dbOperation.executeQuery(sql);
	}
	
	//组装update语句
	private String updateSql(String tableName, String columns, String condition) {
		if(tableName == null || columns == null) {
			return "";
		}
		String[] column = columns.split(",");
		StringBuilder sb = new StringBuilder();
		sb.append("update ");
		sb.append(tableName);
		sb.append(" set ");
		sb.append(column[0]);
		sb.append("=?");
		for (int i = 1; i < column.length; i++) {
			sb.append(", ");
			sb.append(column[i]);
			sb.append("=?");
		}
		sb.append(" ");
		sb.append(condition);
		return sb.toString();
	}
	
	//Sql语句整合函数
	public String insertSql(String tableName, String columns) {
		if(tableName == null || columns == null) {
			return "";
		}
		int n = columns.split(",").length;
		StringBuilder sb = new StringBuilder("");
		sb.append("insert into ");
		sb.append(tableName);
		sb.append(" (");
		sb.append(columns);
		sb.append(") values (?");
		for(int i=1; i < n; i++) {
			sb.append(", ?");
		}
		sb.append(")");
		return sb.toString();
	}
	
	
	

	public static void main(String[] args) {
		
	}

}
