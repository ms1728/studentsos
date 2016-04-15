package com.studentsos.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.studentsos.entity.BookContent;
import com.studentsos.entity.Books;
import com.studentsos.entity.School;
import com.studentsos.entity.UserBook;
import com.studentsos.tools.App;
import com.studentsos.tools.HttpUtils;
import com.studentsos.view.SaveFace;





public class SchoolService {
	App app;
	public SchoolService(Context context){
		app=(App)context.getApplicationContext();
	}
	public List<School> schoolmajor(String school)throws Exception
	{
		String url=app.serverUrl+"/?to=schoolmajor";
		Map<String,String> params=new HashMap<String, String>();
		params.put("school", school);
		@SuppressWarnings("unchecked")
		List<School> schoolmajor=(List<School>)HttpUtils.post(url, params);
		return schoolmajor;
	}
	public List<School> schoolmajorTwo(int majorDetailID)throws Exception
	{
		String url=app.serverUrl+"/?to=schoolmajortwo";
		Map<String,String> params=new HashMap<String, String>();
		params.put("majorDetailID",Integer.toString(majorDetailID));
		@SuppressWarnings("unchecked")
		List<School> schoolmajor=(List<School>)HttpUtils.post(url, params);
		return schoolmajor;
	}
}
