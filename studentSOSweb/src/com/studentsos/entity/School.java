package com.studentsos.entity;

import java.io.Serializable;

public class School implements Serializable {
	
	private int majorDetailID;
	private String schoolName;
	private String department;
	private String major;
	private int classCount;
	
	public int getMajorDetailID() {
		return majorDetailID;
	}
	public void setMajorDetailID(int majorDetailID) {
		this.majorDetailID = majorDetailID;
	}
	public String getSchoolName() {
		return schoolName;
	}
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public int getClassCount() {
		return classCount;
	}
	public void setClassCount(int classCount) {
		this.classCount = classCount;
	}
	
}
