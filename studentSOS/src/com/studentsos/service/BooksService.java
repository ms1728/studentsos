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
import com.studentsos.entity.UserBook;
import com.studentsos.tools.App;
import com.studentsos.tools.HttpUtils;
import com.studentsos.view.SaveFace;





public class BooksService {
	App app;
	Bitmap bitmap;
	public BooksService(Context context){
		app=(App)context.getApplicationContext();
	}
	public List<UserBook> userbook(String code)throws Exception
	{
		String url=app.serverUrl+"/?to=userbook";
		Map<String,String> params=new HashMap<String, String>();
		params.put("code", code);
		@SuppressWarnings("unchecked")
		List<UserBook> userbook=(List<UserBook>)HttpUtils.post(url, params);
		return userbook;
	}
	public List<Books> searchbook(String key)throws Exception
	{
		String url=app.serverUrl+"/?to=searchbook";
		Map<String,String> params=new HashMap<String, String>();
		params.put("key", key);
		@SuppressWarnings("unchecked")
		List<Books> searcbook=(List<Books>)HttpUtils.post(url, params);
		return searcbook;
	}
	public List<Books> loadbooks(String code)throws Exception
	{
		String url=app.serverUrl+"/?to=books";
		Map<String,String> params=new HashMap<String, String>();
		params.put("code", code);
		@SuppressWarnings("unchecked")
		List<Books> books=(List<Books>)HttpUtils.post(url, params);
		return books;
	}
	public String loadcover(String path,int book_id)throws Exception{
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
                String headpath=SaveFace.saveBook(bitmap,book_id);
                return headpath;
            }  
        } catch (Exception e) {  
            e.printStackTrace();   
        } finally {  
            httpclient.getConnectionManager().shutdown();  
        }
		return null;  
	}
	public List<BookContent> bookcontent(int bookid)throws Exception{
		String url=app.serverUrl+"/?to=book_content";
		Map<String,String> params=new HashMap<String, String>();
		params.put("bookid",Integer.toString(bookid));
		@SuppressWarnings("unchecked")
		List<BookContent> bookContent=(List<BookContent>)HttpUtils.post(url, params);
		return bookContent; 
	}
	public void loadanswer(String webpath,String path) throws Exception{
		DefaultHttpClient httpclient = new DefaultHttpClient(); 
        HttpGet httpget = new HttpGet(webpath);  
        try {  
            HttpResponse resp = httpclient.execute(httpget);  
            //判断是否正确执行  
            if (HttpStatus.SC_OK == resp.getStatusLine().getStatusCode()) {  
                HttpEntity entity = resp.getEntity();  
                InputStream is = entity.getContent();  
                File file = new File(path);
                FileOutputStream fos = new FileOutputStream(file);//对应文件建立输出流
                byte[] buffer = new byte[10*1024];//新建缓存  用来存储 从网络读取数据 再写入文件
                int len = 0;
                while((len=is.read(buffer)) != -1){//当没有读到最后的时候 
                	fos.write(buffer, 0, len);//将缓存中的存储的文件流秀娥问file文件
                }
                fos.flush();//将缓存中的写入file
                fos.close();
                is.close();//将输入流 输出流关闭
            }  
        } catch (Exception e) {  
            e.printStackTrace();   
        } finally {  
            httpclient.getConnectionManager().shutdown();  
        }
		
	}
}
