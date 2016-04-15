package com.studentsos.tools;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.studentsos.entity.LinkNode;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;


public class HttpUtils {
	public static final int TIMEOUT = 20000; // 超时时间
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象
	// Host地址
	public static final String HOST = "jw.gzhmt.edu.cn";
	// 基础地址
	public static final String URL_BASE = "http://jw.gzhmt.edu.cn/";
	// 验证码地址
	public static final String URL_CODE = "http://jw.gzhmt.edu.cn/CheckCode.aspx";
	// 登陆地址
	public static final String URL_LOGIN = "http://jw.gzhmt.edu.cn/default2.aspx";
	// 登录成功的首页
	public static String URL_MAIN = "http://jw.gzhmt.edu.cn/xs_main.aspx?xh=XH";
	// 请求地址
	public static String URL_QUERY = "http://jw.gzhmt.edu.cn/QUERY";

	/**
	 * 请求参数
	 */
	public static String Button1 = "";
	public static String hidPdrs = "";
	public static String hidsc = "";
	public static String lbLanguage = "";
	public static String RadioButtonList1 = "学生";
	public static String __VIEWSTATE = "dDwyODE2NTM0OTg7Oz735YJJrHP3fyGfXlshDgkIXLOcmw==";
	//__VIEWSTATE改成页面上对应的值
	public static String TextBox2 = null;
	public static String txtSecretCode = null;
	public static String txtUserName = null;

	// 静态初始化
	static {
		client.setTimeout(10000); // 设置链接超时，如果不设置，默认为10s
		// 设置请求头
		client.addHeader("Host", HOST);
		client.addHeader("Referer", URL_LOGIN);
		client.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
	}

	/**
	 * 提交表单数据
	 * 
	 * @param url
	 *            URl
	 * @param params
	 *            提交参数
	 * @return 返回对象
	 * @throws Exception
	 */

	public static Object post(String url, Map<String, String> params)
			throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpParams p = client.getParams();
		p.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT);
		p.setParameter(CoreConnectionPNames.SO_TIMEOUT, TIMEOUT);
		HttpPost post = new HttpPost(url);
		if (params != null && !params.isEmpty()) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> e : params.entrySet())
				formparams
						.add(new BasicNameValuePair(e.getKey(), e.getValue()));
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					formparams, HTTP.UTF_8);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			post.setEntity(formEntity);
		}
		HttpResponse resp = client.execute(post);
		int status = resp.getStatusLine().getStatusCode();
		if (status != 200)
			throw new Exception("ErrorCode:" + status);
		HttpEntity entity = resp.getEntity();
		if (entity.getContentLength() == 0)
			return null;
		InputStream is = entity.getContent();
		ObjectInputStream ois = new ObjectInputStream(is);
		Object obj = ois.readObject();
		ois.close();
		is.close();
		client.getConnectionManager().shutdown();
		return obj;
	}
	/**

	 * 
	 * @param url
	 *            URl
	 * @param object 发送对象
	 * @throws Exception
	 */
	public static void sendObject(String url,Object object)
			throws Exception {
		HttpURLConnection conn=(HttpURLConnection) new URL(url).openConnection();
		conn.setConnectTimeout(TIMEOUT);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "multipart/form-data");
		OutputStream os=conn.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(object);
		oos.flush();
		oos.close();
		os.close();
		conn.getInputStream().close();
		conn.disconnect();
	}
	/**
	 * get,用一个完整url获取一个string对象
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, AsyncHttpResponseHandler res) {
		client.get(urlString, res);
	}

	/**
	 * get,url里面带参数
	 * 
	 * @param urlString
	 * @param params
	 * @param res
	 */
	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	/**
	 * get,下载数据使用，会返回byte数据
	 * 
	 * @param uString
	 * @param bHandler
	 */
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	/**
	 * post,不带参数
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, AsyncHttpResponseHandler res) {
		client.post(urlString, res);
	}

	/**
	 * post,带参数
	 * 
	 * @param urlString
	 * @param params
	 * @param res
	 */
	public static void post(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	/**
	 * post,返回二进制数据时使用，会返回byte数据
	 * 
	 * @param uString
	 * @param bHandler
	 */
	public static void post(String uString, BinaryHttpResponseHandler bHandler) {
		client.post(uString, bHandler);
	}

	/**
	 * 返回请求客户端
	 * 
	 * @return
	 */
	public static AsyncHttpClient getClient() {
		return client;
	}

	/**
	 * 获得登录时所需的请求参数
	 * 
	 * @return
	 */
	public static RequestParams getLoginRequestParams() {
		// 设置请求参数
		RequestParams params = new RequestParams();
		params.add("__VIEWSTATE", __VIEWSTATE);
		params.add("Button1", Button1);
		params.add("hidPdrs", hidPdrs);
		params.add("hidsc", hidsc);
		params.add("lbLanguage", lbLanguage);
		params.add("RadioButtonList1", RadioButtonList1);
		params.add("TextBox2", TextBox2);
		params.add("txtSecretCode", txtSecretCode);
		params.add("txtUserName", txtUserName);
		return params;
	}

	public interface QueryCallback {
		public String handleResult(byte[] result);
	}

	public static void getQuery(final Context context, List<LinkNode> linkNode,
			final String urlName, final QueryCallback callback) {
		final ProgressDialog dialog = CommonUtil.getProcessDialog(context,
				"正在获取" + urlName);
		dialog.show();
		String link=null;
		for(int i=0;i<linkNode.size();i++){
			if(linkNode.get(i).getTitle().equals(urlName))
				link=linkNode.get(i).getLink();
		}
		 
		if (link != null) {
			HttpUtils.URL_QUERY = HttpUtils.URL_QUERY.replace("QUERY", link);
		} else {
			Toast.makeText(context, "链接出现错误", Toast.LENGTH_SHORT).show();
			return;
		}
		HttpUtils.getClient().addHeader("Referer", HttpUtils.URL_MAIN);
		HttpUtils.getClient().setURLEncodingEnabled(true);
		HttpUtils.get(HttpUtils.URL_QUERY, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (callback != null) {
					callback.handleResult(arg2);
				}
				Toast.makeText(context, urlName + "获取成功！！！", Toast.LENGTH_LONG)
						.show();
				dialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				dialog.dismiss();
				Toast.makeText(context, urlName + "获取失败！！！", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
