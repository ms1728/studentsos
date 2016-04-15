package com.studentsos.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.studentsos.entity.Books;
import com.studentsos.entity.User;
import com.studentsos.entity.UserBook;
import com.studentsos.service.BooksService;
import com.studentsos.service.ConfigService;
import com.studentsos.service.FaceImageService;
import com.studentsos.service.UserService;
import com.studentsos.tools.ACache;
import com.studentsos.tools.App;
import com.studentsos.view.SwipeBackLayout;
import com.studentsos.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("WorldReadableFiles")
public class LoginActivity extends Activity {

	AlertDialog.Builder dialogBuilder;
	UserService userService; // 用户业务对象
	FaceImageService faceImageService;
	ConfigService configService;
	BooksService booksService;
	private myAsyncTast tast;
	Properties config;
	private long mExitTime;
	private List<Books> books;
	private List<UserBook> userbook;
	private Books booksEnd = null;
	private UserBook userbookEnd = null;
	private String code;
	private String password;
	private User user;
	private SharedPreferences sp;
	@ViewInject(R.id.sjEdt)
	private EditText sjEdt;
	@ViewInject(R.id.header_title)
	private TextView header_title;
	@ViewInject(R.id.passwordEdt)
	private EditText passwordEdt;
	
	private String serverUrl;
	ProgressDialog dialog = null;
	ACache mCache;
	App app;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			finish();
		}
	};

	@OnClick(R.id.zhuce)
	public void zhuceClick(View v) {
		Intent intent = new Intent(LoginActivity.this, RegisteredActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.quhui)
	public void quhuiClick(View v) {
		Intent intent = new Intent(LoginActivity.this, BackPassActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.loginBtn)
	public void loginBtnClick(View v) {
		code = sjEdt.getText().toString();
		password = passwordEdt.getText().toString();
		if (code.length() == 0 || password.length() == 0) {
			showMessageDialog("请输入登录名和密码", R.drawable.warning, null);
			return;
		}

		tast = new myAsyncTast();// 创建AsyncTask
		tast.execute();// 启动AsyncTask
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me_login);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
				.detectNetwork().penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

		ViewUtils.inject(this);
		app = (App) getApplicationContext();
		dialogBuilder =new AlertDialog.Builder(this);
		passwordEdt=(EditText)findViewById(R.id.passwordEdt);
		app.loadUrl = "http://ms1728.oicp.net/";
		app.serverUrl = "http://ms1728.oicp.net/studentSOSweb";
		mCache = ACache.get(this);
		userService = new UserService(this);
		configService = new ConfigService(this);
		faceImageService = new FaceImageService(this);
		booksService = new BooksService(this);
		// 获得实例对象
		sp = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		sjEdt.setText(sp.getString("USER_NAME", ""));
		passwordEdt.setText(sp.getString("PASSWORD", ""));
		try {
			config = configService.read();
			serverUrl = config.getProperty("serverUrl");
		} catch (IOException e) {
			e.printStackTrace();
			showMessageDialog("配置文件读取失败：" + e.getMessage(), R.drawable.not, null);
			config = new Properties();
		}

	}

	class myAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(LoginActivity.this, "", "正在登录，请稍等...", false);// 创建ProgressDialog
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					cancel(true);
				}
			});
		}

		@Override
		protected Void doInBackground(Void... arg0) {


			try {

				user = userService.login(code, password);
				if (isCancelled()) {
					return null;
				}
				if (user == null) {
					publishProgress(1);
					return null;
				}
				userbook = booksService.userbook(user.getCode());
				if (isCancelled()) {
					return null;
				}
				books = booksService.loadbooks(user.getCode());
				if (isCancelled()) {
					return null;
				}
				/*if (user.getMajorDetailID() >= 0)
					syllabus = courseService.loadcourse(user.getMajorDetailID(),user.getEntryYear());*/
				// 记住用户名、密码、
				Editor editor = sp.edit();
				editor.putString("USER_NAME", code);
				editor.putString("PASSWORD", password);
				editor.commit();
				try {
					configService.write(config);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// TODO转向主菜单界面
				Intent intent = new Intent();
				app.user = user;
				app.books = books;
				app.userbook = userbook;
				//app.syllabus = syllabus;
				if (books != null) {
					for (int i = 0; i < books.size(); i++) {
						String strPath = Environment.getExternalStorageDirectory() + "/studentsos/bookpicture/"
								+ "keben_" + books.get(i).getBookid() + ".png";
						File f = new File(strPath);
						if (!f.exists()) {
							String path = booksService.loadcover(app.loadUrl + "upload/" + books.get(i).getWeb_cover(),
									books.get(i).getBookid());
							app.books.get(i).setPhone_cover(path);
						} else
							app.books.get(i).setPhone_cover(strPath);
					}
				}
				if (app.user.getTouxiang_web() != null) {
					String strface = Environment.getExternalStorageDirectory() + "/studentsos/haed/"
							+app.user.getTouxiang_web();
					File f = new File(strface);
					if (!f.exists()) {
						String path = app.loadUrl + "upload/" + app.user.getTouxiang_web();
						faceImageService.loadface(path); // 从服务器下载
					}
					app.user.setTouxiang(strface);
				}

				intent.putExtra("result", app.user.getCode().toString());
				setResult(RESULT_OK, intent);// 设置resultCode，onActivityResult()中能获取到
			} catch (Exception e) {
				e.printStackTrace();
				publishProgress(2);
				return null;
			}
			publishProgress(3);
			return null;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			dialog.dismiss();// 关闭ProgressDialog
			if (values[0] == 1) {
				showMessageDialog("登录名或密码错误", R.drawable.warning, null);
				return;
			}
			if (values[0] == 2) {
				showMessageDialog("登录失败,请检查网络", R.drawable.not, null);
				return;
			}
			cache();
			Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
			LoginActivity.this.startActivity(mainIntent);
			new Thread(new Runnable() {
				@Override
				public void run() {
					SystemClock.sleep(350);
					handler.sendEmptyMessage(0);
				}
			}).start();
		}

	}
	private void cache(){
		mCache.put("user", app.user, 7 * ACache.TIME_DAY);
		//mCache.put("syllabus", app.syllabus);
		for (int i = 0; i <= app.books.size(); i++) {
			if (i == app.books.size())
				mCache.put("books" + i, booksEnd);
			else
				mCache.put("books" + i, app.books.get(i));
		}
		for (int i = 0; i <= app.userbook.size(); i++) {
			if (i == app.userbook.size())
				mCache.put("userbook" + i, userbookEnd);
			else
				mCache.put("userbook" + i, app.userbook.get(i));
		}
	}
	/* 两次返回按钮退出软件 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();

			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	/**显示一个简单的消息对话框
	 * message 消息
	 * iconId  图标ID
	 * 
	 */
	protected void showMessageDialog(String message,int iconId,
		DialogInterface.OnClickListener onClickListener)
	{
		dialogBuilder.setIcon(iconId);   //设置图标ID
		dialogBuilder.setTitle(message);  //设置消息
		dialogBuilder.setPositiveButton("确定", onClickListener);		//确定按钮事件
		dialogBuilder.create().show();   //显示对话框
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
}
