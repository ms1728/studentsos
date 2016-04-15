package com.studentsos.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class SaveFace {
	
	public static String saveFace(Bitmap b, String code,int face_numb) {  
		
	String oldecount=Integer.toString(face_numb-1);
	String count=Integer.toString(face_numb);
	String strPath = "/studentsos/haed/"+code+ "faceImage"+count+".jpg";
	if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
		File sdCardDir = Environment.getExternalStorageDirectory();
		FileOutputStream fos = null;
		try{
			File file = new File(sdCardDir, strPath);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			fos = new FileOutputStream(file);
			b.compress(CompressFormat.JPEG, 100, fos);
			File f = new File(sdCardDir,"/studentsos/haed/"+code+ "faceImage"+oldecount+".jpg"); // 输入要删除的文件位置
            if(f.exists())
            f.delete();
			fos.flush();
		}catch(Exception e){
			Log.e("Save test", "save bitmap error : " + e);
		}finally{
			try{
				fos.close();
			}catch(IOException e){
				Log.e("Save test", "finally error : " + e);
			}
		}
	}
	return Environment.getExternalStorageDirectory()+strPath;
	}
	public static String saveBook(Bitmap b,int book_id) {  
		
		String strPath = "/studentsos/bookpicture/"+"keben_"+book_id+".png";
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			File sdCardDir = Environment.getExternalStorageDirectory();
			FileOutputStream fos = null;
			try{
				File file = new File(sdCardDir, strPath);
				if(!file.getParentFile().exists()){
					file.getParentFile().mkdirs();
				}
				fos = new FileOutputStream(file);
				b.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
			}catch(Exception e){
				Log.e("Save test", "save bitmap error : " + e);
			}finally{
				try{
					fos.close();
				}catch(IOException e){
					Log.e("Save test", "finally error : " + e);
				}
			}
		}
		return Environment.getExternalStorageDirectory()+strPath;
		}
}
