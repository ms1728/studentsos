package com.studentsos.fragment;

import java.io.File;
import java.util.List;

import com.studentsos.R;
import com.studentsos.activity.BookInfoActivity;
import com.studentsos.activity.LoginActivity;
import com.studentsos.activity.MainActivity;
import com.studentsos.activity.MeAboutActivity;
import com.studentsos.activity.MeFanKuiActivity;
import com.studentsos.activity.MeGongNengActivity;
import com.studentsos.activity.MePersonActivity;
import com.studentsos.entity.Books;
import com.studentsos.entity.UpdateInfo;
import com.studentsos.entity.User;
import com.studentsos.entity.UserBook;
import com.studentsos.service.BooksService;
import com.studentsos.service.DownloadService;
import com.studentsos.service.DownloadService.DownloadBinder;
import com.studentsos.service.FaceImageService;
import com.studentsos.service.UpdateInfoService;

import com.studentsos.service.UserService;
import com.studentsos.tools.ACache;
import com.studentsos.tools.App;
import com.studentsos.tools.FileDownloadThread;
import com.studentsos.tools.Tools;
import com.studentsos.view.SwipeBackLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class MeFragment extends Fragment {

	@SuppressWarnings("unused")
	private View mParent;
	protected SwipeBackLayout layout;
	private boolean isBinded;
	private DownloadBinder binder;
	App app;
	private User user;
	@SuppressWarnings("unused")
	private FragmentActivity mActivity;
	private TextView user_name;
	private TextView version;
	private ImageView touxiang;
	private LinearLayout me_tuichu;
	private LinearLayout me_about;
	private LinearLayout me_gongneng;
	private LinearLayout me_fankui;
	private LinearLayout me_gengxin;
	private Books booksEnd = null;
	private UserBook userbookEnd = null;
	@SuppressWarnings("unused")
	private TextView mText;
	UserService userService; // 用户业务对象
	FaceImageService faceImageService;
	BooksService booksService;
	private boolean userchange = false;
	private boolean bookschange = false;
	private boolean inLoad = false;
	private static final String TAG = MainActivity.class.getSimpleName();
	ACache mCache;
	private ProgressDialog pBar, pTip;
	private UpdateInfo info;
	private String facepath;
	private HomeFragment homeFragment;
	private List<Books> books;
	private List<UserBook> userbook;
	private SharedPreferences sp;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				user_name.setText(app.user.getName().toString());
				facepath = app.user.getTouxiang();
				if (facepath != null) {
					Bitmap bt = BitmapFactory.decodeFile(facepath);// 从Sd中找头像，转换成Bitmap
					Drawable drawable = new BitmapDrawable(getResources(), bt);
					touxiang.setImageDrawable(drawable);
				}
				mCache.put("user", app.user, 7 * ACache.TIME_DAY);
				break;
			case 2:
				bookChange();
				homeFragment = (HomeFragment) getActivity().getSupportFragmentManager()
						.findFragmentById(R.id.fragment_home);
				homeFragment.bookFragment();
				break;

			case 3:
				user_name.setText(app.user.getName().toString());
				facepath = app.user.getTouxiang();
				if (facepath != null) {
					Bitmap bt = BitmapFactory.decodeFile(facepath);// 从Sd中找头像，转换成Bitmap
					Drawable drawable = new BitmapDrawable(getResources(), bt);
					touxiang.setImageDrawable(drawable);
				}
				bookChange();
				mCache.put("user", app.user, 7 * ACache.TIME_DAY);
				homeFragment = (HomeFragment) getActivity().getSupportFragmentManager()
						.findFragmentById(R.id.fragment_home);
				homeFragment.bookFragment();
				break;
			case 4:
				break;
			}

		}

	};
	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示
			if (isNeedUpdate()) {
				showUpdateDialog();
			}
			pTip.dismiss();
		};
	};

	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static MeFragment newInstance(int index) {
		MeFragment f = new MeFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}

	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@SuppressLint("WorldReadableFiles")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		user_name = (TextView) view.findViewById(R.id.user_name);
		touxiang = (ImageView) view.findViewById(R.id.touxiang);
		version = (TextView) view.findViewById(R.id.bianhao);
		me_tuichu = (LinearLayout) view.findViewById(R.id.me_tuichu);
		me_about = (LinearLayout) view.findViewById(R.id.me_about);
		me_gongneng = (LinearLayout) view.findViewById(R.id.me_gongneng);
		me_gengxin = (LinearLayout) view.findViewById(R.id.me_gengxin);
		me_fankui = (LinearLayout) view.findViewById(R.id.me_fankui);
		sp = getActivity().getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
		version.setText("V" + getVersion());
		userService = new UserService(getActivity());
		faceImageService = new FaceImageService(getActivity());
		booksService = new BooksService(getActivity());
		app = (App) getActivity().getApplicationContext();
		mCache = ACache.get(getActivity());
		me_about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MeAboutActivity.class);
				getActivity().startActivity(intent);
			}
		});
		me_gengxin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pTip = ProgressDialog.show(getActivity(), "", "正在检查版本更新..", false);
				// Toast.makeText(getActivity(), "正在检查版本更新..",
				// Toast.LENGTH_SHORT).show();
				// 自动检查有没有新版本 如果有新版本就提示更新
				new Thread() {
					public void run() {
						try {
							UpdateInfoService updateInfoService = new UpdateInfoService(getActivity());
							info = updateInfoService.getUpDateInfo();
							if (pBar != null)
								pBar.dismiss();
							handler1.sendEmptyMessage(0);
						} catch (Exception e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		});
		me_fankui.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MeFanKuiActivity.class);
				getActivity().startActivity(intent);
			}
		});
		touxiang.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				app = (App) getActivity().getApplicationContext();
				if ((app.user) == null) {
					Intent intent = new Intent(getActivity(), LoginActivity.class);
					getActivity().startActivityForResult(intent, 1);
				} else {
					Intent intent = new Intent(getActivity(), MePersonActivity.class);
					getActivity().startActivityForResult(intent, 3);
				}
			}
		});
		if (app.user != null) {
			user_name.setText(app.user.getName());
			String facepath = app.user.getTouxiang();
			if (facepath != null) {
				Bitmap bt = BitmapFactory.decodeFile(facepath);// 从Sd中找头像，转换成Bitmap
				Drawable drawable = new BitmapDrawable(getResources(), bt);
				touxiang.setImageDrawable(drawable);
			}
			me_tuichu.setVisibility(View.VISIBLE);
		}
		if (sp.getString("USER_NAME", "") != null && sp.getString("PASSWORD", "") != null) {
			if (Tools.isNetworkAvailable(getActivity()))
				new Thread(networkTask).start();
			else
				Toast.makeText(getActivity().getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
		}
		return view;
	}

	Runnable networkTask = new Runnable() {

		@Override
		public void run() {
			// 在这里进行 http request.网络请求相关操作
			try {
				String code = sp.getString("USER_NAME", "");
				String password = sp.getString("PASSWORD", "");
				user = userService.login(code, password);
				if (user != null && app.user != null) {
					userbook = booksService.userbook(user.getCode());
					books = booksService.loadbooks(user.getCode());
					if (!app.user.getName().equals(user.getName())
							|| app.user.getTouxiang_numb() != user.getTouxiang_numb()) {
						userchange = true;
						app.user = user;
						if (app.user.getTouxiang_web() != null) {
							String strface = Environment.getExternalStorageDirectory() + "/studentsos/haed/"
									+ app.user.getCode() + "_" + app.user.getTouxiang_web() + ".jpg";
							File f = new File(strface);
							if (!f.exists()) {
								String path = app.loadUrl + "upload/" + app.user.getTouxiang_web();
								faceImageService.loadface(path); // 从服务器下载
							}
							app.user.setTouxiang(strface);
						}
					}
					if (app.userbook.size() != userbook.size()) {
						bookschange = true;
						app.books = books;
						app.userbook = userbook;
						for (int m = 0; m < books.size(); m++) {
							String strPath = Environment.getExternalStorageDirectory() + "/student/bookpicture/"
									+ "keben_" + books.get(m).getBookid() + ".png"; // 手机内存图片路径
							File f = new File(strPath);
							if (!f.exists()) {
								String path = booksService.loadcover(
										app.loadUrl + "upload/" + books.get(m).getWeb_cover(),
										books.get(m).getBookid()); // 获取服务器图片保存到手机
								app.books.get(m).setPhone_cover(path);
							} else
								app.books.get(m).setPhone_cover(strPath);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (userchange) {
				if (bookschange) {
					Message msg = new Message();
					msg.what = 3;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			} else if (bookschange) {
				Message msg = new Message();
				msg.what = 2;
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = 4;
				handler.sendMessage(msg);
			}

		}
	};

	public void bookChange() {
		for (int k = 0; k <= app.books.size(); k++) {
			if (k == app.books.size())
				mCache.put("books" + k, booksEnd);
			else
				mCache.put("books" + k, app.books.get(k));
		}
		for (int t = 0; t <= app.userbook.size(); t++) {
			if (t == app.userbook.size())
				mCache.put("userbook" + t, userbookEnd);
			else
				mCache.put("userbook" + t, app.userbook.get(t));
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mParent = getView();

	}

	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("请升级APP至版本" + info.getVersion());
		builder.setMessage(info.getDescription());
		builder.setCancelable(false);

		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					// 开启更新服务UpdateService
					// 这里为了把update更好模块化，可以传一些updateService依赖的值
					// 如布局ID，资源ID，动态获取的标题,这里以app_name为例
					Intent it = new Intent(getActivity(), DownloadService.class);
					it.putExtra("url", info.getUrl());
					getActivity().startService(it);
					getActivity().bindService(it, conn, Context.BIND_AUTO_CREATE);
				} else {
					Toast.makeText(getActivity(), "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}

		});
		builder.create().show();
	}

	ServiceConnection conn = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			isBinded = false;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			binder = (DownloadBinder) service;
			System.out.println("服务启动!!!");
			// 开始下载
			isBinded = true;
			// binder.addCallback(callback);
			binder.start();

		}
	};

	private boolean isNeedUpdate() {

		String v = info.getVersion(); // 最新版本的版本号
		Log.i("update", v);
		if (v.equals(getVersion())) {
			Toast.makeText(getActivity(),"已是最新版本！", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	// 获取当前版本的版本号
	private String getVersion() {
		try {
			PackageManager packageManager = getActivity().getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (isBinded)
			getActivity().unbindService(conn);
	}
}