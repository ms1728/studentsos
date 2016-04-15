package com.studentsos.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import com.studentsos.tools.App;
import com.studentsos.tools.HttpUtils;


public class RegisterService {
	

	App app;
	public RegisterService(Context context){
		app=(App)context.getApplicationContext();
	}
	/**  注册***
	* code  用户编号
	* password  密码
	* @return 用户对象
	* @throws Exception
	* **/
	public int register(String code,String nicheng,String password,String email)throws Exception
	{
		String url=app.serverUrl+"/?to=register";
		Map<String,String> params=new HashMap<String, String>();
		params.put("code", code);
		params.put("nicheng", nicheng);
		params.put("password", password);
		params.put("email", email);
		int count=(Integer)HttpUtils.post(url, params);
		return count;
	}
	public void schoolInfo(String code,int majorDetailID,String eduSchoolYear,int classnum)throws Exception
	{
		String url=app.serverUrl+"/?to=schoolinfo";
		Map<String,String> params=new HashMap<String, String>();
		params.put("code", code);
		params.put("majorDetailID",Integer.toString(majorDetailID));
		params.put("eduSchoolYear", eduSchoolYear);
		params.put("classnum", Integer.toString(classnum));
		HttpUtils.post(url, params);
	}
}
