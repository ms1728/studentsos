package com.studentsos.service;

import java.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.studentsos.entity.School;
import com.studentsos.entity.SchoolAdapter;
import com.studentsos.entity.User;
import com.studentsos.entity.UserAdapter;
import com.studentsosweb.CheckEmailObj;
import com.studentsosweb.Db;



public class RegisterService {
	/**
	 * 
	 * 
	 * @param code
	 *            用户编号
	 * @param password
	 *            密码
	 * @return 注册成功后，返回1实例，否则返回0
	 */
	User user;
	School schools;
	public int register(String code, String nicheng, String password, String email) {
		String sql1 = "select * from majordetail_vw where schoolName= ?";
		String sql2 = "insert into student (password,studentName,studentID,email,headPath,changeHeadTimes,majorDetailID) values(?,?,?,?,?,?,?)";
		String sql3 = "select * from student where studentID= ?";
		String sql4 = "select * from student where studentName=?";
		String sql5 = "select * from student where email=?";
		
		Connection conn = null;
		try {
			conn = Db.getConnection();
			user=new User();
			user=Db.get(conn,UserAdapter.SINGLETON,sql3,code);
			if(user!=null)
				return 2;
			user=Db.get(conn,UserAdapter.SINGLETON,sql4,nicheng);
			if(user!=null)
				return 3;
			user=Db.get(conn,UserAdapter.SINGLETON,sql5,email);
			if(user!=null)
				return 4;
			int count=Db.update(conn,sql2,password,nicheng,code,email,null,0,1);
					return 5;

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
	public void updateUser(String code,int classNum,int majorDetailID,String eduSchoolYear) {
		String sql1 = "update student set classNum=?,majorDetailID=?,entryYear=?  where studentID =?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			Db.update(conn,sql1,classNum,majorDetailID,eduSchoolYear,code);
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
