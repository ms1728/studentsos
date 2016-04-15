package com.studentsos.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.studentsosweb.Db;
import com.studentsos.*;
public enum SchoolAdapter implements Db.ResultSetAdapter<School> {
	SINGLETON; // 此成员表示 枚举类的枚举值对象
	public School convert(ResultSet rs) throws SQLException { // 把 把 ResultSet 转换为
															// User 对象
		School school = new School();
		school.setSchoolName(rs.getString("schoolName"));
		school.setDepartment(rs.getString("departmentName"));
		school.setMajorDetailID(rs.getInt("majorDetailID"));
		school.setMajor(rs.getString("majorName"));
		school.setClassCount(rs.getInt("classCount"));
		return school;
	}
}