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
	public static final int TIMEOUT = 20000; // ��ʱʱ��
	private static AsyncHttpClient client = new AsyncHttpClient(); // ʵ��������
	// Host��ַ
	public static final String HOST = "jw.gzhmt.edu.cn";
	// ������ַ
	public static final String URL_BASE = "http://jw.gzhmt.edu.cn/";
	// ��֤���ַ
	public static final String URL_CODE = "http://jw.gzhmt.edu.cn/CheckCode.aspx";
	// ��½��ַ
	public static final String URL_LOGIN = "http://jw.gzhmt.edu.cn/default2.aspx";
	// ��¼�ɹ�����ҳ
	public static String URL_MAIN = "http://jw.gzhmt.edu.cn/xs_main.aspx?xh=XH";
	// �����ַ
	public static String URL_QUERY = "http://jw.gzhmt.edu.cn/QUERY";

	/**
	 * �������
	 */
	public static String Button1 = "";
	public static String hidPdrs = "";
	public static String hidsc = "";
	public static String lbLanguage = "";
	public static String RadioButtonList1 = "ѧ��";
	public static String __VIEWSTATE = "dDwyODE2NTM0OTg7Oz735YJJrHP3fyGfXlshDgkIXLOcmw==";
	//__VIEWSTATE�ĳ�ҳ���϶�Ӧ��ֵ
	public static String TextBox2 = null;
	public static String txtSecretCode = null;
	public static String txtUserName = null;

	// ��̬��ʼ��
	static {
		client.setTimeout(10000); // �������ӳ�ʱ����������ã�Ĭ��Ϊ10s
		// ��������ͷ
		client.addHeader("Host", HOST);
		client.addHeader("Referer", URL_LOGIN);
		client.addHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko");
	}

	/**
	 * �ύ������
	 * 
	 * @param url
	 *            URl
	 * @param params
	 *            �ύ����
	 * @return ���ض���
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
	 * @param object ���Ͷ���
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
	 * get,��һ������url��ȡһ��string����
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void get(String urlString, AsyncHttpResponseHandler res) {
		client.get(urlString, res);
	}

	/**
	 * get,url���������
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
	 * get,��������ʹ�ã��᷵��byte����
	 * 
	 * @param uString
	 * @param bHandler
	 */
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	/**
	 * post,��������
	 * 
	 * @param urlString
	 * @param res
	 */
	public static void post(String urlString, AsyncHttpResponseHandler res) {
		client.post(urlString, res);
	}

	/**
	 * post,������
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
	 * post,���ض���������ʱʹ�ã��᷵��byte����
	 * 
	 * @param uString
	 * @param bHandler
	 */
	public static void post(String uString, BinaryHttpResponseHandler bHandler) {
		client.post(uString, bHandler);
	}

	/**
	 * ��������ͻ���
	 * 
	 * @return
	 */
	public static AsyncHttpClient getClient() {
		return client;
	}

	/**
	 * ��õ�¼ʱ������������
	 * 
	 * @return
	 */
	public static RequestParams getLoginRequestParams() {
		// �����������
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
				"���ڻ�ȡ" + urlName);
		dialog.show();
		String link=null;
		for(int i=0;i<linkNode.size();i++){
			if(linkNode.get(i).getTitle().equals(urlName))
				link=linkNode.get(i).getLink();
		}
		 
		if (link != null) {
			HttpUtils.URL_QUERY = HttpUtils.URL_QUERY.replace("QUERY", link);
		} else {
			Toast.makeText(context, "���ӳ��ִ���", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(context, urlName + "��ȡ�ɹ�������", Toast.LENGTH_LONG)
						.show();
				dialog.dismiss();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				dialog.dismiss();
				Toast.makeText(context, urlName + "��ȡʧ�ܣ�����", Toast.LENGTH_SHORT)
						.show();
			}
		});
	}
}
