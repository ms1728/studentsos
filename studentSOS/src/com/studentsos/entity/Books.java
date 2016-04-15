package com.studentsos.entity;

import java.io.Serializable;



public class Books implements Serializable{
	private int bookid;  //主键
	private String web_cover; //封面
	private String phone_cover; 
	private String bookname;       //书名
	private String description;   //说明
	private String author;
	private String press; //出版社
	
	
	public Books(int bookid, String web_cover, String phone_cover,
			String bookname, String description, String author, String press) {
		super();
		this.bookid = bookid;
		this.web_cover = web_cover;
		this.phone_cover = phone_cover;
		this.bookname = bookname;
		this.description = description;
		this.author = author;
		this.press = press;
	}


	public Books() {
		
	}
	
	
	public int getBookid() {
		return bookid;
	}


	public void setBookid(int bookid) {
		this.bookid = bookid;
	}


	public String getWeb_cover() {
		return web_cover;
	}


	public void setWeb_cover(String web_cover) {
		this.web_cover = web_cover;
	}


	public String getPhone_cover() {
		return phone_cover;
	}


	public void setPhone_cover(String phone_cover) {
		this.phone_cover = phone_cover;
	}


	public String getBookname() {
		return bookname;
	}


	public void setBookname(String bookname) {
		this.bookname = bookname;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getPress() {
		return press;
	}


	public void setPress(String press) {
		this.press = press;
	}


	private static final long serialVersionUID = 1L;
	
}
