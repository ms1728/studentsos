package com.studentsos.activity;

import com.studentsos.R;
import com.studentsos.activity.RegisteredActivity.myAsyncTast;
import com.studentsos.entity.Course;
import com.studentsos.entity.LinkNode;
import com.studentsos.entity.School;
import com.studentsos.service.ConfigService;
import com.studentsos.service.CourseService;
import com.studentsos.service.LinkService;
import com.studentsos.service.RegisterService;
import com.studentsos.tools.ACache;
import com.studentsos.tools.App;
import com.studentsos.tools.CommonUtil;
import com.studentsos.tools.DataCleanManager;
import com.studentsos.tools.HttpUtils;
import com.studentsos.tools.HttpUtils.QueryCallback;
import com.studentsos.tools.LinkUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable.Factory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegistSchoolActivity extends BasicActivity {
	RegisterService registerService;
	private CourseService courseService;
	private LinkService linkService;
	List<LinkNode> linkNode;
	//School school = new School();
	private PersistentCookieStore cookie;
	private String code;
	private Course courseEnd=null;
	private EditText secrectCode;
	private TextView coedchange;
	boolean isdialog = false;
	ProgressDialog dialog = null;
	View dialogView;
	ImageView imgView;
	ACache mCache;
	App app;
	@ViewInject(R.id.Spinner_school)
	private TextView Spinner_school;
	@ViewInject(R.id.jiaowu_codeEdt)
	private EditText jiaowu_codeEdt;
	@ViewInject(R.id.jiaowu_passEdt)
	private EditText jiaowu_passEdt;
	@ViewInject(R.id.header_title)
	private TextView header_title;

	@OnClick(R.id.back_view)
	public void back_viewClick(View v) {
		onBackPressed();
	}
	@OnClick(R.id.relat_school)
	public void relat_schoolClick(View v) {
		Intent intent= new Intent(RegistSchoolActivity.this,SchoolSearchActivity.class);
		startActivity(intent);
	}
	@OnClick(R.id.spinner_saveBt)
	public void spinner_saveBtClick(View v) {

		getCode();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registe_school);
		ViewUtils.inject(this);
		header_title.setText("ѧУ��Ϣ");
		app = (App) getApplicationContext();
		mCache = ACache.get(this);
		Intent intent = this.getIntent();
		code = intent.getStringExtra("code");
		registerService = new RegisterService(this);
		initCookie(this);// cookie��ʼ��
		initValue();
		Spinner_school.setText("���ݺ���ѧԺ");
	}
	/**
	 * ��ʼ������
	 */
	private void initValue() {
		linkService = LinkService.getLinkService();
		courseService = CourseService.getCourseService();
	}
	/**
	 * ��ʼ��Cookie
	 */
	private void initCookie(Context context) {
		// ����������ǰ��ʼ��
		cookie = new PersistentCookieStore(context);
		HttpUtils.getClient().setCookieStore(cookie);
	}


	public void codeDialog(String message, DialogInterface.OnClickListener onClickListener) {
		dialogBuilder.setTitle(message); // ������Ϣ
		dialogBuilder.setView(dialogView);
		dialogBuilder.setPositiveButton("ȷ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				isdialog = false;
				login();
			}
		});
		dialogBuilder.setNeutralButton("ȡ��", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialogBuilder.setView(null);
				isdialog = false;
			}
		});
		dialogBuilder.create().show(); // ��ʾ�Ի���
		isdialog = true;
	}

	/**
	 * ��¼
	 */
	private void login() {
		HttpUtils.txtUserName = jiaowu_codeEdt.getText().toString().trim();
		HttpUtils.TextBox2 = jiaowu_passEdt.getText().toString().trim();

		HttpUtils.txtSecretCode = secrectCode.getText().toString().trim();
		if (TextUtils.isEmpty(HttpUtils.txtUserName) || TextUtils.isEmpty(HttpUtils.TextBox2)) {
			Toast.makeText(getApplicationContext(), "�˺Ż������벻��Ϊ��!", Toast.LENGTH_SHORT).show();
			return;
		}
		final ProgressDialog dialog = CommonUtil.getProcessDialog(RegistSchoolActivity.this, "���ڵ�¼��֤�У�����");
		dialog.show();
		RequestParams params = HttpUtils.getLoginRequestParams();// ����������
		HttpUtils.URL_MAIN = HttpUtils.URL_MAIN.replace("XH", HttpUtils.txtUserName);// ��������ַ
		HttpUtils.getClient().setURLEncodingEnabled(true);
		HttpUtils.post(HttpUtils.URL_LOGIN, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				try {
					String resultContent = new String(arg2, "gb2312");
					if (linkService.isLogin(resultContent) != null) {
						String ret = linkService.parseMenu(resultContent);
						Log.d("zafu", "login success:"+ret);
						 linkNode= linkService.findAll();
						 jump2Kb(false);
					} else {
						Toast.makeText(getApplicationContext(), "�˺Ż���������󣡣���", Toast.LENGTH_SHORT).show();
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} finally {
					dialog.dismiss();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				Toast.makeText(getApplicationContext(), "��¼ʧ�ܣ�������", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
			}
		});
	}
	/**
	 * ����Ի���ؼ�
	 */
	private void dialogEntity(){
		if (!isdialog) {
			LayoutInflater factorys = LayoutInflater.from(RegistSchoolActivity.this);
			dialogView = factorys.inflate(R.layout.regist_code, null);
		}
		imgView = (ImageView) dialogView.findViewById(R.id.code_image);
		secrectCode = (EditText) dialogView.findViewById(R.id.yanzhenma);
		coedchange = (TextView) dialogView.findViewById(R.id.codechange);
		
		coedchange.setOnClickListener(new TextView.OnClickListener() {
			public void onClick(View v) {
				getCode();
			}
		});
	}
	/**
	 * �����֤��
	 */
	private void getCode() {
		final ProgressDialog dialog = CommonUtil.getProcessDialog(RegistSchoolActivity.this, "���ڻ�ȡ��֤��");
		if (!isdialog) {
			dialog.show();
		}
		HttpUtils.get(HttpUtils.URL_CODE, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				InputStream is = new ByteArrayInputStream(arg2);
				Bitmap decodeStream = BitmapFactory.decodeStream(is);
				Bitmap decode = Bitmap.createScaledBitmap(decodeStream, 250, 100, false);
				dialogEntity();
				imgView.setImageBitmap(decode);
				if (!isdialog) {
					dialog.dismiss();
					codeDialog("��������֤��", null);
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

				Toast.makeText(getApplicationContext(), "��֤���ȡʧ�ܣ�����", Toast.LENGTH_SHORT).show();
				dialog.dismiss();

			}
		});
	}
	/**
	 * �����α�ҳ��
	 */
	private void jump2Kb(boolean flag) {
				
		HttpUtils.getQuery(RegistSchoolActivity.this,linkNode,LinkUtil.XSKBCX, new QueryCallback() {
					@Override
					public String handleResult(byte[] result) {
							String ret=null;
							try {
								ret = courseService.parseCourse(new String(result,"gb2312"));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
							app.course=courseService.findAll();
							for (int i = 0; i <= app.course.size(); i++) {
								if (i == app.course.size())
									mCache.put(app.user.getCode()+"course" + i, courseEnd);
								else
									mCache.put(app.user.getCode()+"course" + i, app.course.get(i));
							}
							onBackPressed();
							return ret;
					}
				});
	}

}
