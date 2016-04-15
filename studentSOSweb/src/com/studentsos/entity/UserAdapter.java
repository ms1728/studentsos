package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum UserAdapter implements Db.ResultSetAdapter<User> {
	SINGLETON; // 此成员表示 枚举类的枚举值对象
	public User convert(ResultSet rs) throws SQLException { // 把 把 ResultSet 转换为
															// User 对象
		User user = new User();
		user.setTouxiang_web(rs.getString("headPath"));
		user.setTouxiang_numb(rs.getInt("changeHeadTimes"));
		user.setCode(rs.getString("studentID"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("studentName"));
		user.setEmail(rs.getString("email"));
		user.setMajorDetailID(rs.getInt("majorDetailID"));
		user.setClassNum(rs.getInt("classNum"));
		user.setEntryYear(rs.getInt("entryYear"));
		return user;
	}
}