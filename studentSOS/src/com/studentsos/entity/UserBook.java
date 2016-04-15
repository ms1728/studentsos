package com.studentsos.entity;

import java.io.Serializable;


public class UserBook implements Serializable{
	
	private int id;
	private int bookid;  //Ö÷¼ü
	private String code; 

	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public int getBookid() {
		return bookid;
	}



	public void setBookid(int bookid) {
		this.bookid = bookid;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}


	private static final long serialVersionUID = 1L;
	
}
