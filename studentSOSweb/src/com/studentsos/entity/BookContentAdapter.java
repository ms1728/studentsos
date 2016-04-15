package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum BookContentAdapter implements Db.ResultSetAdapter<BookContent> {
	SINGLETON; // �˳�Ա��ʾ ö�����ö��ֵ����
	public BookContent convert(ResultSet rs) throws SQLException { // �� �� ResultSet ת��Ϊ
															// Books ����
		BookContent bookContent=new BookContent();
		bookContent.setBookid(rs.getInt("bookID"));
		bookContent.setId(rs.getInt("bookSectionPathID"));
		bookContent.setChapter(rs.getString("sectionName"));
		bookContent.setAnswer(rs.getString("contentPath"));
		bookContent.setSectionNum(rs.getInt("sectionNum"));
		return bookContent;
	}
}