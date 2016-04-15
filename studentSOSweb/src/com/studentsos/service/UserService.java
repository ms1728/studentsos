package com.studentsos.service;

import java.*;
import java.sql.*;
import java.util.Date;
import java.util.List;

import com.studentsos.entity.User;
import com.studentsos.entity.UserAdapter;
import com.studentsos.entity.UserBook;
import com.studentsos.entity.UserBookAdapter;
import com.studentsosweb.Db;



public class UserService {
	/**
	 * 登录的方法
	 * 
	 * @param code
	 *            用户编号
	 * @param password
	 *            密码
	 * @return 登录成功后，返回User实例，否则返回null
	 */
	public User login(String code, String password) {
		String sql = "select * from studentdetail_vw where studentID =? and password =?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			return Db.get(conn, UserAdapter.SINGLETON, sql, code, password);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	User user;
	public int findPassword(String code,String password, String email) {
		String sql = "select * from student where studentID = ? and email = ?";
		String sql1 = "update student set password=? where studentID =?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			user=new User();
			user=Db.get(conn, UserAdapter.SINGLETON, sql, code,email);
			if(user!=null){
				Db.update(conn,sql1,password,user.getCode());
				return 0;
			}else
				return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return 0;
	}
	public void updateUser(User user) {
		String sql1 = "update student set studentName=?,changeHeadTimes=? ,majorDetailID=?,entryYear=?,classNum=? where studentID =?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			Db.update(conn,sql1, user.getName(), user.getTouxiang_numb(),user.getMajorDetailID(),user.getEntryYear(),user.getClassNum(),user.getCode());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	public int addUserBook(String code,int bookid) {
		String sql1 = "select * from bookCollection where bookID=? and studentID=?";
		String sql2 = "insert into bookCollection (bookID,studentID) values(?,?)";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			UserBook id=new UserBook();
			List<Object> l = Db.updateAndGetId(conn, sql2, new int[] { 1 },bookid,code);	//返回获取id
			return Integer.parseInt(String.valueOf(l.get(1)));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return 0;
	}
	public void updateFace(String web_face,String code) {
		String sql1 = "update student set headPath=?  where studentID =?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			Db.update(conn,sql1,web_face,code);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	public void deleteubook(int id) {
		String sql1 = "delete from bookCollection where bookCollectionID=?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			Db.update(conn,sql1,id);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}
