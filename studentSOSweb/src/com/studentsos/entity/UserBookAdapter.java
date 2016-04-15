package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum UserBookAdapter implements Db.ResultSetAdapter<UserBook> {
	SINGLETON; // 此成员表示 枚举类的枚举值对象
	public UserBook convert(ResultSet rs) throws SQLException { // 把 把 ResultSet 转换为
															// User 对象
		UserBook userbook = new UserBook();
		userbook.setId(rs.getInt("bookCollectionID"));
		userbook.setBookid(rs.getInt("bookID"));
		userbook.setCode(rs.getString("studentID"));
		
		return userbook;
	}
}