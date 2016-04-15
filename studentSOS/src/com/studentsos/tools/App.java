package com.studentsos.tools;


import java.io.IOException;
import java.util.List;

import com.studentsos.entity.BookContent;
import com.studentsos.entity.Books;
import com.studentsos.entity.Course;
import com.studentsos.entity.School;
import com.studentsos.entity.User;
import com.studentsos.entity.UserBook;
import com.studentsos.service.ConfigService;

import android.app.Application;


public class App extends Application {

	/**
	 * 保存当前登陆者
	 */
	public User user;
	public List<Books> books;
	public List<BookContent> bookContent;
	public List<UserBook> userbook;
	public List<School> schoolMajor;
	public List<Course> course;
	public String serverUrl;
	public String loadUrl;
	private boolean isDownload;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isDownload = false;
		ConfigService configService=new ConfigService(this);
		try{
			serverUrl=configService.read().getProperty("serverUrl");
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}
	
}
