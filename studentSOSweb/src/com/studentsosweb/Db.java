package com.studentsosweb;

import java.sql.*;
import java.util.*;

import com.sina.sae.util.SaeUserInfo;


public class Db {
	public static final String URL = "jdbc:mysql://localhost:3306/studentsos?useUnicode=true&characterEncoding=gb2312";
																				// 是数据库名称,thin
																				// 是瘦客户端连接方式
	public static final String NAME ="root"; // 数据库用户账号
	public static final String PASSWORD = "123456";
	public static final String driver="com.mysql.jdbc.Driver";
	/*public static final String URL = "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_studentsos"; // 云服务器
	// 是数据库名称,thin
	// 是瘦客户端连接方式
	public static final String NAME = SaeUserInfo.getAccessKey(); // 数据库用户账号
	public static final String PASSWORD =SaeUserInfo.getSecretKey();
	public static final String driver = "com.mysql.jdbc.Driver";*/
	private Db() { // 不可实例化
	}

	/**
	 * 获取 Connection 的方法 ，<strong>Connection 用后需要关闭 。</strong>
	 * 
	 * @return Connection
	 */
	public static Connection getConnection() throws SQLException {
		try {
			try {
				Class.forName(driver).newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 加载 oracle 数据库驱动
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return DriverManager.getConnection(URL, NAME, PASSWORD);
	}

	/**
	 * 执行查询 的方法 ， 返回多个对象
	 * 
	 * @param conn
	 *            Connection
	 * @param adapter
	 *            将 将 ResultSet 转化为 List
	 * @param sql
	 *            * SQL 语句
	 * @param params
	 *            SQL 语句参数值
	 * @return List 结果
	 */
	public static <T> List<T> query(Connection conn,
			ResultSetAdapter<T> adapter, String sql, Object... params)
			throws SQLException { // Object... params 参数格式 JDK1.5 支持, 表示多个参数
									// 的集合, 个数不确定
		List<T> list = new ArrayList<T>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		int i = 1;
		try {
			ps = conn.prepareStatement(sql);
			for (Object p : params)
				if (p instanceof java.util.Date)
					ps.setTimestamp(i++, new java.sql.Timestamp(
							((java.util.Date) p).getTime()));
				else
					ps.setObject(i++, p); // 为 为 ps 中的 SQL 语句设置参数值
			rs = ps.executeQuery();
			while (rs.next())
				list.add(adapter.convert(rs)); // ResultSetAdapter<T>把 把
												// ResultSet 转换为 T 类型的对象
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return list;
	}

	/**
	 * 执行查询 ， 返回一个对象 的方法
	 * 
	 * @param conn
	 *            Connection
	 * @param adapter
	 *            将 将 ResultSet 转化为 List
	 * @param sql
	 *            SQL 语句
	 * @param params
	 *            SQL 语句参数值
	 * @return 结果
	 */
	public static <T> T get(Connection conn, ResultSetAdapter<T> adapter,
			String sql, Object... params) throws SQLException {
		List<T> list = query(conn, adapter, sql, params);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * 执行更新 的方法
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL 语句(update,insert,delete)
	 * @param params
	 *            SQL 语句参数值
	 * @return 受影响的行数
	 */
	public static int update(Connection conn, String sql, Object... params)
			throws SQLException {
		PreparedStatement ps = null;
		int i = 1;
		try {
			ps = conn.prepareStatement(sql);
			for (Object p : params)
				if (p instanceof java.util.Date)
					ps.setTimestamp(i++, new java.sql.Timestamp(
							((java.util.Date) p).getTime()));
				else
					ps.setObject(i++, p);
			return ps.executeUpdate();
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	/**
	 * 执行更新 ， 并获取生成的值 的方法
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL 语句(update,insert,delete)
	 * @param keyColumns
	 *            关键字段列号数组
	 * @param params
	 *            SQL 语句参数值
	 * @return 结构为[ 受影响的行数 ， 生成的值 1 ， 生成的值 2 ， 生成的值 3...]
	 */
	public static List<Object> updateAndGetId(Connection conn, String sql,
			int[] keyColumns, Object... params) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Object> r = new ArrayList<Object>();
		int i = 1;
		try {
			ps = conn.prepareStatement(sql, keyColumns);
			/*
			 * Creates a default PreparedStatement object capable of returning
			 * the auto-generated keys designated by the given array. This array
			 * contains the indexes of the columns in the target table that
			 * contain the auto-generated keys.
			 */
			for (Object p : params)
				if (p instanceof java.util.Date)
					ps.setTimestamp(i++, new java.sql.Timestamp(
							((java.util.Date) p).getTime()));
				else
					ps.setObject(i++, p);
			r.add(ps.executeUpdate()); // 把执行 SQL 语句后受影响 的行数加入 r (List 类型) 中.
			rs = ps.getGeneratedKeys(); // 返回执行 SQL 语句(insert ) 后所产生的关键字段值
			// 例如: 本案例中, 用于 把执行插入订单的 insert into order 语句后所产生的 id( 订单 id, 是数据库
			// 自动产生值的字段) 值返回
			while (rs.next())
				for (int k : keyColumns)
					r.add(rs.getObject(k)); // 把关键字段值加入 r (List 类 型) 中
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return r;
	}

	/**
	 * 执行批量更新 的方法
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL 语句
	 * @param params
	 *            SQL 语句参数值
	 * @return 受影响的行数
	 */
	public static int[] updateBatch(Connection conn, String sql,
			Object[]... params) throws SQLException {
		PreparedStatement ps = null; // Object[]... params 表示多个数组的集合
		try {
			ps = conn.prepareStatement(sql);
			for (Object[] pp : params) {
				int i = 1;
				for (Object p : pp)
					if (p instanceof java.util.Date)
						ps.setObject(i++, new java.sql.Date(
								((java.util.Date) p).getTime()));
					else
						ps.setObject(i++, p);
				ps.addBatch(); // 把一个参数值数组 pp 加到 ps 中的一个 SQL 语句 中
			} // 把 多 个参数值数组 params 加到 ps 中的多个 SQL 语句 中
			return ps.executeBatch(); // 执行 多个( 批量)SQL 语句
			// 例如: 本案例中, 用于执行插入一个订单的多条订单明细记录的多个 insert SQL 语句
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	/**
	 * 将 将 ResultSet 中的一条记录转化为{T} 类型 的接口
	 * 
	 * @param <T>
	 *            需要转 化成的类型
	 */
	public static interface ResultSetAdapter<T> {
		/*
		 * 将 将 ResultSet 中的一条记录转化为{T} 类型 的方法 。aRowInResultSet 的 next() 方法会被调用 ，
		 * 不要再次调用 。
		 * 
		 * @param aRowInResultSet 需要转化的 ResultSet
		 * 
		 * @return T 类型的对象
		 */
		T convert(ResultSet aRowInResultSet) throws SQLException;
	}
} // class Db
