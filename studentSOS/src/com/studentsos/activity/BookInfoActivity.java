package com.studentsos.activity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.studentsos.R;

import com.studentsos.entity.BookContent;
import com.studentsos.entity.Books;
import com.studentsos.entity.UserBook;
import com.studentsos.service.BooksService;
import com.studentsos.service.UserService;
import com.studentsos.tools.ACache;
import com.studentsos.tools.App;
import com.studentsos.tools.FileDownloadThread;
import com.studentsos.tools.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BookInfoActivity extends BasicActivity {

	@ViewInject(R.id.header_title)
	private TextView header_title;
	@ViewInject(R.id.scro)
	private ScrollView scro;
	@ViewInject(R.id.book_name)
	private TextView book_name;
	@ViewInject(R.id.book_author)
	private TextView book_author;
	@ViewInject(R.id.book_press)
	private TextView book_press;
	@ViewInject(R.id.classinbtn)
	private Button classinbtn;
	@ViewInject(R.id.classoutbtn)
	private Button classoutbtn;
	@ViewInject(R.id.book_description)
	private TextView book_description;
	@ViewInject(R.id.tv_loadmore)
	private TextView tv_loadmore;
	@ViewInject(R.id.mulu_lview)
	private ListView mulu_lview;
	@ViewInject(R.id.book_view)
	private ImageView book_view;
	private final int MAX_LINES = 5;
	private boolean isClickable = false;
	private Books booksEnd = null;
	private UserBook userbookEnd = null;
	private BookContent booksContentEnd = null;
	ArrayList<HashMap<String, Object>> lstImageItem;
	UserService userervice;
	BooksService booksService;
	ProgressDialog dialog = null;
	ACache mCache;
	private myAsyncTast tast;
	private contentAsyncTast ctast;
	private answerAsyncTast atast;
	private BookContent bookContent;
	private List<BookContent> webBookContent;
	private UserBook userbook;
	private Books books;
	private Intent intent;
	private Bundle mBundle;
	DisplayMetrics dm;
	private int bookid;
	private int count;
	private int[] count1;
	private boolean cbtn = false;
	private static final String TAG = BookInfoActivity.class.getSimpleName();
	/** 显示下载进度TextView */
	// private TextView mMessageView;
	/** 显示下载进度ProgressBar */
	// private ProgressBar mProgressbar;
	App app;

	@OnClick(R.id.tv_loadmore)
	public void tv_loadmoreClick(View v) {
		if (!isClickable) {
			book_description.setMaxLines(book_description.getLineCount());
			tv_loadmore.setText("收起");
			isClickable = true;
		} else {
			book_description.setMaxLines(MAX_LINES);
			tv_loadmore.setText("查看更多");
			isClickable = false;
		}
	}

	@OnClick(R.id.back_view)
	public void back_viewClick(View v) {
		onBackPressed();
	}

	/**
	 * 使用Handler更新UI界面信息
	 */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			/*
			 * mProgressbar.setProgress(msg.getData().getInt("size"));
			 * 
			 * float temp = (float) mProgressbar.getProgress() / (float)
			 * mProgressbar.getMax();
			 */

			// int progress = (int) (temp * 100);
			// if (progress == 100) {
			Toast.makeText(BookInfoActivity.this, "下载完成！", Toast.LENGTH_LONG).show();
			// }
			// mMessageView.setText("下载进度:" + progress + " %");

		}
	};

	@OnClick(R.id.classinbtn)
	public void classinbtnClick(View v) {
		if (!cbtn) {
			userbook = new UserBook();
			userbook.setCode(app.user.getCode());
			userbook.setBookid(books.getBookid());
			tast = new myAsyncTast();// 创建AsyncTask
			tast.execute();// 启动AsyncTask
		} else {
			tast = new myAsyncTast();// 创建AsyncTask
			tast.execute();// 启动AsyncTask
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_info);
		ViewUtils.inject(this);
		userervice = new UserService(this);
		booksService = new BooksService(this);
		header_title.setText("课本介绍");
		app = (App) getApplicationContext();
		mCache = ACache.get(this);
		scro.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		books = (Books) getIntent().getSerializableExtra("bookview");
		book_view = (ImageView) findViewById(R.id.book_view);
		Bitmap bitmap = BitmapFactory.decodeFile(books.getPhone_cover());
		book_view.setImageBitmap(bitmap);
		book_name.setText(Tools.ToDBC(books.getBookname()));
		book_author.setText(Tools.ToDBC("作者：" + books.getAuthor()));
		book_press.setText(Tools.ToDBC("出版社：" + books.getPress()));
		book_description.setMaxLines(MAX_LINES);
		if (app.user != null) {

			for (int i = 0; i < app.userbook.size(); i++) {
				if (books.getBookid() == app.userbook.get(i).getBookid()) {
					cbtn = true;
					bookid = app.userbook.get(i).getId();
					count = i;
					classinbtn.setText("从书架移除");
				}
			}

		} else {
			classinbtn.setBackgroundResource(R.drawable.shape2);
			classinbtn.setTextColor(Color.parseColor("#999999"));
			classinbtn.setClickable(false);
		}
		app.bookContent = new ArrayList();
		BookContent bookc = null;
		for (int i = 0; i < 100; i++) {
			if (mCache.getAsObject(books.getBookid() + books.getBookname() + i) != null) {
				bookc = (BookContent) mCache.getAsObject(books.getBookid() + books.getBookname() + i);
				app.bookContent.add(bookc);
			} else
				i = 100;
		}
		listShow();
		scro.smoothScrollTo(0, 20);// 滚动到顶部
		if (Tools.isNetworkAvailable(this)) {
			ctast = new contentAsyncTast();// 创建AsyncTask
			ctast.execute();// 启动AsyncTask
		} else {
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
			classoutbtn.setClickable(false);
			classinbtn.setClickable(false);
		}
	}

	class contentAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(BookInfoActivity.this, "", "请稍等...", false);// 创建ProgressDialog

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				webBookContent = booksService.bookcontent(books.getBookid());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//if (webBookContent != null && webBookContent.size() != app.bookContent.size()) {
				app.bookContent = webBookContent;
				/*for (int i = 0; i <= webBookContent.size(); i++) {
					if (i == webBookContent.size())
						mCache.put(books.getBookid() + books.getBookname() + i, booksContentEnd);
					else
						mCache.put(books.getBookid() + books.getBookname() + i, webBookContent.get(i));
				}*/
				publishProgress(1);
				return null;
	
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			dialog.dismiss();// 关闭ProgressDialog
			if (values[0] == 1) {
				listShow();
				scro.smoothScrollTo(0, 20);// 滚动到顶部
			}

		}

	}

	/**
	 * 显示章节列表
	 */
	public void listShow() {
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < app.bookContent.size(); i++) {
			for (int j = 0; j < app.bookContent.size(); j++) {
				if (app.bookContent.get(j).getSectionNum() == (i + 1)) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("itemText", app.bookContent.get(j).getChapter());// 按序号做ItemText
					lstImageItem.add(map);
					j=app.bookContent.size();
				}
			}
		}
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem, R.layout.mulu_liset_item,
				new String[] { "itemText" }, new int[] { R.id.zhangjie });
		// 添加并且显示
		mulu_lview.setAdapter(saImageItems);
		// 添加消息处理
		Tools.setListViewHeightBasedOnChildren(mulu_lview);
		mulu_lview.setOnItemClickListener(new ItemClickListener());

	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			intent = new Intent(BookInfoActivity.this, AnswerActivity.class);
			mBundle = new Bundle();
			for (int i = 0; i < app.bookContent.size(); i++) {
				if (app.bookContent.get(i).getSectionNum() == (arg2+1)) {
					mBundle.putSerializable("bookcontent", app.bookContent.get(i));
					bookContent = app.bookContent.get(i);
					break;
				}
			}
			atast = new answerAsyncTast();// 创建AsyncTask
			atast.execute();// 启动AsyncTask
		}

	}

	class myAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(BookInfoActivity.this, "", "请稍等...", false);// 创建ProgressDialog
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			if (!cbtn) {
				try {
					bookid = userervice.addbook(userbook.getCode(), userbook.getBookid());
					app.books.add(books);
					userbook.setId(bookid);
					app.userbook.add(userbook);
					publishProgress(1);
					return null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					userervice.deletebook(bookid);
					if (isCancelled()) {
						return null;
					}
					app.userbook.remove(count);
					app.books.remove(count);
					publishProgress(2);
					return null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			publishProgress(3);
			return null;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			dialog.dismiss();// 关闭ProgressDialog
			bookChange();
			if (values[0] == 1) {
				cbtn = true;
				count = app.userbook.size() - 1;
				classinbtn.setText("从书架移除");
			}
			if (values[0] == 2) {
				cbtn = false;
				classinbtn.setText("加入书架");
			}
		}

	}

	/**
	 * 更新用户书本缓存
	 */
	public void bookChange() {
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

	/**
	 * 
	 * 从服务器下载答案
	 */
	class answerAsyncTast extends AsyncTask<Void, Integer, Void> {

		private String downloadUrl;// 下载链接地址
		private int threadNum;// 开启的线程数
		private String filePath;// 保存文件路径地址
		private int blockSize;// 每一个线程的下载量

		public void downloadTask() {
			this.downloadUrl = app.loadUrl + "upload/answer/" + bookContent.getAnswer();
			this.threadNum = 5;
			this.filePath = Environment.getExternalStorageDirectory() + "/studentsos/answer/" + bookContent.getAnswer();
			FileDownloadThread[] threads = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(downloadUrl);
				Log.d(TAG, "download file http path:" + downloadUrl);
				URLConnection conn = url.openConnection();
				// 读取下载文件总大小
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					System.out.println("读取文件失败");
					return;
				}
				// 设置ProgressBar最大的长度为文件Size
				// mProgressbar.setMax(fileSize);

				// 计算每条线程下载的数据长度
				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum : fileSize / threadNum + 1;

				Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");

				File file = new File(filePath);
				for (int i = 0; i < threads.length; i++) {
					// 启动线程，分别下载每个线程需要下载的部分
					threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				boolean isfinished = false;
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;
					// 当前所有线程下载总量
					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}
					// 通知handler去更新视图组件
					Thread.sleep(1000);// 休息1秒后再读取下载进度
				}
				Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(BookInfoActivity.this, "", "请稍等...", false);// 创建ProgressDialog
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

			App app = (App) getApplicationContext();
			if (bookContent.getAnswer() != null) {
				String path = Environment.getExternalStorageDirectory() + "/studentsos/answer/";
				File f = new File(path);
				if (f.exists()) {
					File f1 = new File(path + bookContent.getAnswer());
					if (!f1.exists()) {
						String webpath = app.loadUrl + "upload/answer/" + bookContent.getAnswer();
						try {
							downloadTask();
							if (isCancelled()) {
								return null;
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} // 从服务器下载
					}
				} else {
					f.mkdirs();
					String webpath = app.loadUrl + "upload/answer/" + bookContent.getAnswer();
					try {
						downloadTask();
						if (isCancelled()) {
							return null;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // 从服务器下载
				}

			}

			publishProgress(3);
			return null;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			dialog.dismiss();// 关闭ProgressDialog
			intent.putExtras(mBundle);
			startActivity(intent);
		}

	}

	/**
	 * 下载准备工作，获取SD卡路径、开启线程
	 */
	private void doDownload() {
		// 获取SD卡路径
		String path = Environment.getExternalStorageDirectory() + "/amosdownload/";
		File file = new File(path);
		// 如果SD卡目录不存在创建
		if (!file.exists()) {
			file.mkdir();
		}
		// 设置progressBar初始化
		// mProgressbar.setProgress(0);

		// 简单起见，我先把URL和文件名称写死，其实这些都可以通过HttpHeader获取到
		String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";
		String fileName = "baidu_16785426.apk";
		int threadNum = 5;
		String filepath = path + fileName;
		Log.d(TAG, "download file  path:" + filepath);
		downloadTask task = new downloadTask();
		task.start();
	}

	/**
	 * 多线程文件下载
	 * 
	 * @author yangxiaolong @2014-8-7
	 */
	class downloadTask extends Thread {
		private String downloadUrl;// 下载链接地址
		private int threadNum;// 开启的线程数
		private String filePath;// 保存文件路径地址
		private int blockSize;// 每一个线程的下载量

		public downloadTask() {
			this.downloadUrl = app.loadUrl + "upload/answer/" + bookContent.getAnswer();
			this.threadNum = 5;
			this.filePath = Environment.getExternalStorageDirectory() + "/studentsos/answer/" + bookContent.getAnswer();
		}

		@Override
		public void run() {

			FileDownloadThread[] threads = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(downloadUrl);
				Log.d(TAG, "download file http path:" + downloadUrl);
				URLConnection conn = url.openConnection();
				// 读取下载文件总大小
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					System.out.println("读取文件失败");
					return;
				}
				// 设置ProgressBar最大的长度为文件Size
				// mProgressbar.setMax(fileSize);

				// 计算每条线程下载的数据长度
				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum : fileSize / threadNum + 1;

				Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");

				File file = new File(filePath);
				for (int i = 0; i < threads.length; i++) {
					// 启动线程，分别下载每个线程需要下载的部分
					threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				boolean isfinished = false;
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;
					// 当前所有线程下载总量
					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}
					// 通知handler去更新视图组件
					Message msg = new Message();
					msg.getData().putInt("size", downloadedAllSize);
					mHandler.sendMessage(msg);
					// Log.d(TAG, "current downloadSize:" + downloadedAllSize);
					Thread.sleep(1000);// 休息1秒后再读取下载进度
				}
				Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

}
