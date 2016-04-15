package com.studentsosweb;

import java.sql.*;
import java.util.*;

import com.sina.sae.util.SaeUserInfo;


public class Db {
	public static final String URL = "jdbc:mysql://localhost:3306/studentsos?useUnicode=true&characterEncoding=gb2312";
																				// �����ݿ�����,thin
																				// ���ݿͻ������ӷ�ʽ
	public static final String NAME ="root"; // ���ݿ��û��˺�
	public static final String PASSWORD = "123456";
	public static final String driver="com.mysql.jdbc.Driver";
	/*public static final String URL = "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_studentsos"; // �Ʒ�����
	// �����ݿ�����,thin
	// ���ݿͻ������ӷ�ʽ
	public static final String NAME = SaeUserInfo.getAccessKey(); // ���ݿ��û��˺�
	public static final String PASSWORD =SaeUserInfo.getSecretKey();
	public static final String driver = "com.mysql.jdbc.Driver";*/
	private Db() { // ����ʵ����
	}

	/**
	 * ��ȡ Connection �ķ��� ��<strong>Connection �ú���Ҫ�ر� ��</strong>
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
			} // ���� oracle ���ݿ�����
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return DriverManager.getConnection(URL, NAME, PASSWORD);
	}

	/**
	 * ִ�в�ѯ �ķ��� �� ���ض������
	 * 
	 * @param conn
	 *            Connection
	 * @param adapter
	 *            �� �� ResultSet ת��Ϊ List
	 * @param sql
	 *            * SQL ���
	 * @param params
	 *            SQL ������ֵ
	 * @return List ���
	 */
	public static <T> List<T> query(Connection conn,
			ResultSetAdapter<T> adapter, String sql, Object... params)
			throws SQLException { // Object... params ������ʽ JDK1.5 ֧��, ��ʾ�������
									// �ļ���, ������ȷ��
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
					ps.setObject(i++, p); // Ϊ Ϊ ps �е� SQL ������ò���ֵ
			rs = ps.executeQuery();
			while (rs.next())
				list.add(adapter.convert(rs)); // ResultSetAdapter<T>�� ��
												// ResultSet ת��Ϊ T ���͵Ķ���
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return list;
	}

	/**
	 * ִ�в�ѯ �� ����һ������ �ķ���
	 * 
	 * @param conn
	 *            Connection
	 * @param adapter
	 *            �� �� ResultSet ת��Ϊ List
	 * @param sql
	 *            SQL ���
	 * @param params
	 *            SQL ������ֵ
	 * @return ���
	 */
	public static <T> T get(Connection conn, ResultSetAdapter<T> adapter,
			String sql, Object... params) throws SQLException {
		List<T> list = query(conn, adapter, sql, params);
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	/**
	 * ִ�и��� �ķ���
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL ���(update,insert,delete)
	 * @param params
	 *            SQL ������ֵ
	 * @return ��Ӱ�������
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
	 * ִ�и��� �� ����ȡ���ɵ�ֵ �ķ���
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL ���(update,insert,delete)
	 * @param keyColumns
	 *            �ؼ��ֶ��к�����
	 * @param params
	 *            SQL ������ֵ
	 * @return �ṹΪ[ ��Ӱ������� �� ���ɵ�ֵ 1 �� ���ɵ�ֵ 2 �� ���ɵ�ֵ 3...]
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
			r.add(ps.executeUpdate()); // ��ִ�� SQL ������Ӱ�� ���������� r (List ����) ��.
			rs = ps.getGeneratedKeys(); // ����ִ�� SQL ���(insert ) ���������Ĺؼ��ֶ�ֵ
			// ����: ��������, ���� ��ִ�в��붩���� insert into order ������������ id( ���� id, �����ݿ�
			// �Զ�����ֵ���ֶ�) ֵ����
			while (rs.next())
				for (int k : keyColumns)
					r.add(rs.getObject(k)); // �ѹؼ��ֶ�ֵ���� r (List �� ��) ��
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
		}
		return r;
	}

	/**
	 * ִ���������� �ķ���
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL ���
	 * @param params
	 *            SQL ������ֵ
	 * @return ��Ӱ�������
	 */
	public static int[] updateBatch(Connection conn, String sql,
			Object[]... params) throws SQLException {
		PreparedStatement ps = null; // Object[]... params ��ʾ�������ļ���
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
				ps.addBatch(); // ��һ������ֵ���� pp �ӵ� ps �е�һ�� SQL ��� ��
			} // �� �� ������ֵ���� params �ӵ� ps �еĶ�� SQL ��� ��
			return ps.executeBatch(); // ִ�� ���( ����)SQL ���
			// ����: ��������, ����ִ�в���һ�������Ķ���������ϸ��¼�Ķ�� insert SQL ���
		} finally {
			if (ps != null)
				ps.close();
		}
	}

	/**
	 * �� �� ResultSet �е�һ����¼ת��Ϊ{T} ���� �Ľӿ�
	 * 
	 * @param <T>
	 *            ��Ҫת ���ɵ�����
	 */
	public static interface ResultSetAdapter<T> {
		/*
		 * �� �� ResultSet �е�һ����¼ת��Ϊ{T} ���� �ķ��� ��aRowInResultSet �� next() �����ᱻ���� ��
		 * ��Ҫ�ٴε��� ��
		 * 
		 * @param aRowInResultSet ��Ҫת���� ResultSet
		 * 
		 * @return T ���͵Ķ���
		 */
		T convert(ResultSet aRowInResultSet) throws SQLException;
	}
} // class Db
