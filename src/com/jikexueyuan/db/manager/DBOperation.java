package com.jikexueyuan.db.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBOperation {
	
	private String poolName;
	private Connection con = null;
	
	public DBOperation(String poolName) {
		this.poolName = poolName;
	}
	
	public void close() {
		if(this.con != null) {
			try {
				this.con.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void open() throws SQLException {
		close();
		this.con = DBManager.getDBManager().getConnection(this.poolName);
	}

	private PreparedStatement setPres(String sql, HashMap<Integer, Object> params) throws SQLException, ClassNotFoundException {
		if(null == params || params.size() < 1) {
			return null;
		}
		PreparedStatement pres = this.con.prepareStatement(sql);
		for(int i=1; i <= params.size(); i++) {
			if(params.get(i) == null) {
				pres.setString(i, "");
			} else if(params.get(i).getClass() == Class.forName("java.lang.String")) {
			pres.setString(i, params.get(i).toString());
			} else if(params.get(i).getClass() == Class.forName("java.lang.Integer")) {
				pres.setInt(i, (Integer) params.get(i));
			} else if (params.get(i).getClass() == Class.forName("java.lang.Long")) {
				pres.setLong(i, (Long) params.get(i));
			} else if (params.get(i).getClass() == Class.forName("java.lang.Double")) {
				pres.setDouble(i, (Double) params.get(i));
			} else if (params.get(i).getClass() == Class.forName("java.lang.Flaot")) {
				pres.setFloat(i, (Float) params.get(i));
			} else if (params.get(i).getClass() == Class.forName("java.lang.Boolean")) {
				pres.setBoolean(i, (Boolean) params.get(i));
			} else if (params.get(i).getClass() == Class.forName("java.sql.Date")) {
				pres.setDate(i, java.sql.Date.valueOf(params.get(i).toString()));
			} else {
				return null;
			}
		}
		return pres;
	}
	
	public int executeUpdate(String sql) throws SQLException {
		this.open();
		Statement state = this.con.createStatement();
		return state.executeUpdate(sql);
	}
	
	public int executeUpdate(String sql, HashMap<Integer, Object> params) throws ClassNotFoundException, SQLException {
		this.open();
		PreparedStatement pres = setPres(sql, params);
		if(null == pres) {
			return 0;
		}
		return pres.executeUpdate();
	}
	
	public ResultSet executeQuery(String sql) throws SQLException {
		this.open();
		Statement state = this.con.createStatement();
		return state.executeQuery(sql);
	}
	
	public ResultSet executeQuery(String sql, HashMap<Integer, Object> params) throws ClassNotFoundException, SQLException {
		this.open();
		PreparedStatement pres = setPres(sql, params);
		if(null == pres) {
			return null;
		}
		return pres.executeQuery();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}

}
