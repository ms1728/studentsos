package com.studentsos.service;

import java.io.File;

import java.io.InputStream;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.studentsos.tools.App;
import com.studentsos.tools.UploadUtil;
import com.studentsos.view.SaveFace;






public class FaceImageService {
	App app;
	Bitmap bitmap;
	public FaceImageService(Context context){
		app=(App)context.getApplicationContext();
		
	}
	
	public void sendface(File tempFile)throws Exception{
		String url=app.serverUrl+"/?to=face";
		UploadUtil.uploadFile(tempFile, url);
		
	}
	public void loadface(String path)throws Exception{
		DefaultHttpClient httpclient = new DefaultHttpClient(); 
        HttpGet httpget = new HttpGet(path);  
        try {  
            HttpResponse resp = httpclient.execute(httpget);  
            //判断是否正确执行  
            if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {  
                //将返回内容转换为bitmap  
                HttpEntity entity = resp.getEntity();  
                InputStream in = entity.getContent();  
                bitmap = BitmapFactory.decodeStream(in);  
                String headpath=SaveFace.saveFace(bitmap, app.user.getCode()+"_", app.user.getTouxiang_numb()); 
                app.user.setTouxiang(headpath);
            }  
        } catch (Exception e) {  
            e.printStackTrace();   
        } finally {  
            httpclient.getConnectionManager().shutdown();  
        }  
	}
}
