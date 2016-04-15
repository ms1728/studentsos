package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum BookContentAdapter implements Db.ResultSetAdapter<BookContent> {
	SINGLETON; // 此成员表示 枚举类的枚举值对象
	public BookContent convert(ResultSet rs) throws SQLException { // 把 把 ResultSet 转换为
															// Books 对象
		BookContent bookContent=new BookContent();
		bookContent.setBookid(rs.getInt("bookID"));
		bookContent.setId(rs.getInt("bookSectionPathID"));
		bookContent.setChapter(rs.getString("sectionName"));
		bookContent.setAnswer(rs.getString("contentPath"));
		bookContent.setSectionNum(rs.getInt("sectionNum"));
		return bookContent;
	}
}