package com.studentsos.service;

import java.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.studentsos.entity.BookContent;
import com.studentsos.entity.BookContentAdapter;
import com.studentsos.entity.Books;
import com.studentsos.entity.BooksAdapter;
import com.studentsos.entity.UserAdapter;
import com.studentsos.entity.UserBook;
import com.studentsos.entity.UserBookAdapter;
import com.studentsosweb.Db;

public class BooksService {
	/**
	 * 
	 * @param code
	 *            用户编号
	 * 
	 * @return 返回Books实例，否则返回null
	 */
	@SuppressWarnings("null")
	public List<Books> loadBooks(String code) {
		String sql = "select * from bookCollection where studentID=?";
		String sql1 = "select * from book where bookID=?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			List<UserBook> userbook = Db.query(conn, UserBookAdapter.SINGLETON,
					sql, code);
			List<Books> books = new ArrayList();
			for (int i = 0; i < userbook.size(); i++) {
				books.add(Db.get(conn, BooksAdapter.SINGLETON, sql1, userbook
						.get(i).getBookid()));
			}
			return books;
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
		return new ArrayList<Books>(0);
	}

	public List<Books> searchBook(String key) {
		String sql = "select * from book WHERE name like ? or author like ? or publisher like ? or introduction like ?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			return Db.query(conn, BooksAdapter.SINGLETON,sql,"%"+key+"%","%"+key+"%","%"+key+"%","%"+key+"%");

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

	public List<UserBook> userBook(String code) {
		String sql = "select * from bookCollection where studentID=?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			return Db.query(conn, UserBookAdapter.SINGLETON, sql, code);

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

	public List<BookContent> searchContent(int bookid) {
		String sql = "select * from booksectionpath where bookID=?";
		Connection conn = null;
		try {
			conn = Db.getConnection();
			return Db.query(conn, BookContentAdapter.SINGLETON, sql,bookid);
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
