package com.studentsos.entity;

import java.io.Serializable;



public class User implements Serializable{

	private String touxiang; //Í·Ïñ
	private String touxiang_web; //Í·Ïñ
	private int touxiang_numb;
	private String code;  //±àºÅ 
	private String password;   //ÃÜÂë
	private String name;       //
	private String email; 
	private int majorDetailID;
	private int classNum;
	private int entryYear;

	public String getTouxiang() {
		return touxiang;
	}

	public void setTouxiang(String touxiang) {
		this.touxiang = touxiang;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTouxiang_web() {
		return touxiang_web;
	}

	public void setTouxiang_web(String touxiangWeb) {
		touxiang_web = touxiangWeb;
	}


	public int getTouxiang_numb() {
		return touxiang_numb;
	}

	public void setTouxiang_numb(int touxiangNumb) {
		touxiang_numb = touxiangNumb;
	}

	public int getMajorDetailID() {
		return majorDetailID;
	}

	public void setMajorDetailID(int majorDetailID) {
		this.majorDetailID = majorDetailID;
	}

	public int getClassNum() {
		return classNum;
	}

	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}

	public int getEntryYear() {
		return entryYear;
	}

	public void setEntryYear(int entryYear) {
		this.entryYear = entryYear;
	}

	private static final long serialVersionUID = 1L;
}
