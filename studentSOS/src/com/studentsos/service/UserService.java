package com.studentsos.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.studentsos.entity.User;
import com.studentsos.tools.App;
import com.studentsos.tools.HttpUtils;


public class UserService {
	

	App app;
	public UserService(Context context){
		app=(App)context.getApplicationContext();
	}
	/**  登录***
	* code  用户编号
	* password  密码
	* @return 用户对象
	* @throws Exception
	* **/
	public User login(String code,String password)throws Exception
	{
		String url=app.serverUrl+"/?to=login";
		Map<String,String> params=new HashMap<String, String>();
		params.put("code", code);
		params.put("password", password);
		User user=(User)HttpUtils.post(url, params);
		return user;
	}
	public void update()throws Exception
	{
		String url=app.serverUrl+"/?to=updateUser";
		HttpUtils.sendObject(url, app.user);
	}
	public int addbook(String code,int bookid)throws Exception
	{
		String url=app.serverUrl+"/?to=addbook";
		Map<String,String> params=new HashMap<String, String>();
		params.put("code", code);
		params.put("bookid", Integer.toString(bookid));
		int count=(Integer)HttpUtils.post(url, params);
		return count;
	}
	
	public void deletebook(int id)throws Exception
	{
		String url=app.serverUrl+"/?to=deleteubook";
		Map<String,String> params=new HashMap<String, String>();
		params.put("id",Integer.toString(id));
		HttpUtils.post(url, params);
	}
}
