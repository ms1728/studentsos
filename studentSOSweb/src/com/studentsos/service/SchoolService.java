package com.studentsos.service;

import java.*;
import java.sql.*;
import java.util.List;

import com.studentsos.entity.BooksAdapter;
import com.studentsos.entity.School;
import com.studentsos.entity.SchoolAdapter;
import com.studentsos.entity.User;
import com.studentsos.entity.UserAdapter;
import com.studentsosweb.CheckEmailObj;
import com.studentsosweb.Db;



public class SchoolService {
	/**
	 * 
	 * 
	
	 */
	public List<School> schoolMajor(String school) {

		String sql1 = "select * from majordetail_vw where schoolName=?";	
		Connection conn = null;
		try {
			conn = Db.getConnection();
			return Db.query(conn, SchoolAdapter.SINGLETON,sql1,school);
			
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
	public List<School> schoolMajorTwo(int majorDetailID) {

		String sql1 = "select * from majordetail_vw where majorDetailID=?";	
		Connection conn = null;
		try {
			conn = Db.getConnection();
			return schoolMajor(Db.query(conn, SchoolAdapter.SINGLETON,sql1,majorDetailID).get(0).getSchoolName());
			
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
}
