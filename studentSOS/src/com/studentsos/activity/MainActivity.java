package com.studentsos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.studentsos.R;
import com.studentsos.entity.Books;
import com.studentsos.entity.Course;
import com.studentsos.entity.User;
import com.studentsos.entity.UserBook;
import com.studentsos.fragment.FragmentIndicator;
import com.studentsos.fragment.FragmentIndicator.OnIndicateListener;
import com.studentsos.fragment.HomeFragment;
import com.studentsos.tools.ACache;
import com.studentsos.tools.App;
import com.studentsos.tools.DataCleanManager;
import com.studentsos.tools.FileDownloadThread;
import com.studentsos.view.MyScrollView;
import com.studentsos.view.MyScrollView.OnScrollListener;
import com.studentsos.view.StickyLayout;
import com.studentsos.view.StickyLayout.OnGiveUpTouchEventListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	AlertDialog.Builder dialogBuilder;
	public static Fragment[] mFragments;
	private long mExitTime;
	@ViewInject(R.id.user_name)
	private TextView user_name;
	@ViewInject(R.id.me_tuichu)
	private LinearLayout me_tuichu;
	@ViewInject(R.id.touxiang)
	private ImageView touxiang;
	@ViewInject(R.id.Searc_lview)
	private ListView searc_lview;
	ACache mCache;

	@OnClick(R.id.me_tuichu)
	public void me_tuichuClick(View v) {
		outLoginDialog("确定退出登录吗?", null);
	}

	@OnClick(R.id.me_huancun)
	public void me_huancunClick(View v) {
		cleanCacheDialog("确定清除缓存吗?", null);
	}

	private App app;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			finish();
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (App) getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mCache = ACache.get(this);
		app.user = (User) mCache.getAsObject("user");
		//app.syllabus = (Syllabus) mCache.getAsObject("syllabus");	
		if (app.user != null) {
			Books books = null;
			UserBook userbook = null;
			Course course=null;
			app.books = new ArrayList();
			app.userbook = new ArrayList();
			app.course = new ArrayList();
			for (int i = 0; i < 1000; i++) {
				books = (Books) mCache.getAsObject("books" + i);
				if (books != null)
					app.books.add(books);
				else
					i = 1000;
			}
			for (int i = 0; i < 1000; i++) {
				userbook = (UserBook) mCache.getAsObject("userbook" + i);
				if (userbook != null)
					app.userbook.add(userbook);
				else
					i = 1000;
			}
			for (int i = 0; i < 1000; i++) {
				course = (Course) mCache.getAsObject(app.user.getCode()+"course" + i);
				if (course != null)
					app.course.add(course);
				else
					i = 1000;
			}
		}
		app.loadUrl = "http://ms1728.oicp.net/";
		app.serverUrl = "http://ms1728.oicp.net/studentSOSweb";
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		setFragmentIndicator(0);
		dialogBuilder = new AlertDialog.Builder(this);
		if(app.course.size()==0){
			loudCourseDialog("是否获取课表",null);
		}
	}

	/**
	 * 初始化fragment
	 */
	private void setFragmentIndicator(int whichIsDefault) {
		mFragments = new Fragment[4];
		mFragments[0] = getSupportFragmentManager().findFragmentById(R.id.fragment_home);
		mFragments[1] = getSupportFragmentManager().findFragmentById(R.id.fragment_search);
		mFragments[2] = getSupportFragmentManager().findFragmentById(R.id.fragment_hot);
		mFragments[3] = getSupportFragmentManager().findFragmentById(R.id.fragment_me);
		getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2])
		.hide(mFragments[3]).show(mFragments[whichIsDefault]).commit();
		FragmentIndicator mIndicator = (FragmentIndicator) findViewById(R.id.indicator);
		FragmentIndicator.setIndicator(whichIsDefault);
		mIndicator.setOnIndicateListener(new OnIndicateListener() {
			@Override
			public void onIndicate(View v, int which) {
				getSupportFragmentManager().beginTransaction().hide(mFragments[0]).hide(mFragments[1])
				.hide(mFragments[2]).hide(mFragments[3]).show(mFragments[which]).commit();

			}
		});
	}

	/* 退出登录对话框 */
	public void outLoginDialog(String message, DialogInterface.OnClickListener onClickListener) {
		dialogBuilder.setTitle(message); // 设置消息
		dialogBuilder.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
				MainActivity.this.startActivityForResult(mainIntent, 1);
				touxiang.setImageResource(R.drawable.me_iv_default);
				user_name.setText(R.string.login_name);
				me_tuichu.setVisibility(View.GONE);
				app = (App) getApplicationContext();
				app.user = null;
				app.userbook = null;
				app.books = null;
				mCache.put("user", app.user);
				HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_home);
				homeFragment.bookFragment();
				new Thread(new Runnable() {
					@Override
					public void run() {
						SystemClock.sleep(1000);
						handler.sendEmptyMessage(0);
					}
				}).start();
			}
		});
		dialogBuilder.setNeutralButton("取消", onClickListener);
		dialogBuilder.create().show(); // 显示对话框
	}

	public void cleanCacheDialog(String message, DialogInterface.OnClickListener onClickListener) {
		dialogBuilder.setTitle(message); // 设置消息
		dialogBuilder.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				File file = new File(Environment.getExternalStorageDirectory() + "/studentsos/");
				DataCleanManager.deleteFiles(file);
				mCache.clear();
			}
		});
		dialogBuilder.setNeutralButton("取消", onClickListener);
		dialogBuilder.create().show(); // 显示对话框
	}
	public void loudCourseDialog(String message, DialogInterface.OnClickListener onClickListener) {
		dialogBuilder.setTitle(message); // 设置消息
		dialogBuilder.setPositiveButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Intent intent=new Intent(MainActivity.this,RegistSchoolActivity.class);
				startActivityForResult(intent,2);
			}
		});
		dialogBuilder.setNeutralButton("取消", onClickListener);
		dialogBuilder.create().show(); // 显示对话框
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

	@SuppressLint("Recycle")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 当otherActivity中返回数据的时候，会响应此方法
		// requestCode和resultCode必须与请求startActivityForResult()和返回setResult()的时候传入的值一致。
		if (requestCode == 1 && resultCode == LoginActivity.RESULT_OK) {
			app = (App) getApplicationContext();
			user_name.setText(app.user.getName().toString());
			String facepath = app.user.getTouxiang();
			if (facepath != null) {
				Bitmap bt = BitmapFactory.decodeFile(facepath);// 从Sd中找头像，转换成Bitmap
				if (bt != null) {
					Drawable drawable = new BitmapDrawable(getResources(), bt);
					touxiang.setImageDrawable(drawable);
				}
			}
			if (app.userbook != null) {
				HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager()
						.findFragmentById(R.id.fragment_home);
				homeFragment.bookFragment();
			}
			me_tuichu.setVisibility(View.VISIBLE);
		}
		if (requestCode == 2) {
			HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_home);
			homeFragment.bookFragment();
		}
		if (requestCode == 3) {
			app = (App) getApplicationContext();
			user_name.setText(app.user.getName().toString());
			String facepath = app.user.getTouxiang();
			if (facepath != null) {
				Bitmap bt = BitmapFactory.decodeFile(facepath);// 从Sd中找头像，转换成Bitmap
				if (bt != null) {
					Drawable drawable = new BitmapDrawable(getResources(), bt);
					touxiang.setImageDrawable(drawable);
				}
			}
			HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_home);
			homeFragment.bookFragment();
		}
	}

	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}

	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}
}