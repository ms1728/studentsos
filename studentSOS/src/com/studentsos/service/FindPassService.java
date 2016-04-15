package com.studentsos.service;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import com.studentsos.tools.App;
import com.studentsos.tools.HttpUtils;


public class FindPassService {
	

	App app;
	public FindPassService(Context context){
		app=(App)context.getApplicationContext();
	}
	/**  注册***
	* code  用户编号
	* password  密码
	* @return 用户对象
	* @throws Exception
	* **/
	public int findpassword(String code,String password,String email)throws Exception
	{
		String url=app.serverUrl+"/?to=findpassword";
		Map<String,String> params=new HashMap<String, String>();
		params.put("code", code);
		params.put("password", password);
		params.put("email", email);
		int count=(Integer)HttpUtils.post(url, params);
		return count;
	}
	
}
