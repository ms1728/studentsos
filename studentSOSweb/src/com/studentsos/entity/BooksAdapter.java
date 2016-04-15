package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum BooksAdapter implements Db.ResultSetAdapter<Books> {
	SINGLETON; // 此成员表示 枚举类的枚举值对象
	public Books convert(ResultSet rs) throws SQLException { // 把 把 ResultSet 转换为
															// Books 对象
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