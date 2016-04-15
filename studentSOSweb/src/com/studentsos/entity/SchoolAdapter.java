package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum SchoolAdapter implements Db.ResultSetAdapter<School> {
	SINGLETON; // �˳�Ա��ʾ ö�����ö��ֵ����
	public School convert(ResultSet rs) throws SQLException { // �� �� ResultSet ת��Ϊ
															// User ����
		School school = new School();
		school.setSchoolName(rs.getString("schoolName"));
		school.setDepartment(rs.getString("departmentName"));
		school.setMajorDetailID(rs.getInt("majorDetailID"));
		school.setMajor(rs.getString("majorName"));
		school.setClassCount(rs.getInt("classCount"));
		return school;
	}
}