package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum BooksAdapter implements Db.ResultSetAdapter<Books> {
	SINGLETON; // �˳�Ա��ʾ ö�����ö��ֵ����
	public Books convert(ResultSet rs) throws SQLException { // �� �� ResultSet ת��Ϊ
															// Books ����
		Books books=new Books();
		books.setBookid(rs.getInt("bookID"));
		books.setWeb_cover(rs.getString("picPath"));
		books.setBookname(rs.getString("name"));
		books.setDescription(rs.getString("introduction"));
		books.setAuthor(rs.getString("author"));
		books.setPress(rs.getString("publisher"));
		return books;
	}
}