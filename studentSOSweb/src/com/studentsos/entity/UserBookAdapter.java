package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum UserBookAdapter implements Db.ResultSetAdapter<UserBook> {
	SINGLETON; // �˳�Ա��ʾ ö�����ö��ֵ����
	public UserBook convert(ResultSet rs) throws SQLException { // �� �� ResultSet ת��Ϊ
															// User ����
		UserBook userbook = new UserBook();
		userbook.setId(rs.getInt("bookCollectionID"));
		userbook.setBookid(rs.getInt("bookID"));
		userbook.setCode(rs.getString("studentID"));
		
		return userbook;
	}
}