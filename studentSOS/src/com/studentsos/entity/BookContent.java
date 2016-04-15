package com.studentsos.entity;

import java.io.Serializable;




public class BookContent implements Serializable{
	private int id;  //Ö÷¼ü
	private int bookid;
	private int sectionNum;
	private String chapter;   
	private String answer;       
	
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


	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public int getSectionNum() {
		return sectionNum;
	}

	public void setSectionNum(int sectionNum) {
		this.sectionNum = sectionNum;
	}

	private static final long serialVersionUID = 1L;
}
