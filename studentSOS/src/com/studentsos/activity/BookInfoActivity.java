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
	/** ��ʾ���ؽ���TextView */
	// private TextView mMessageView;
	/** ��ʾ���ؽ���ProgressBar */
	// private ProgressBar mProgressbar;
	App app;

	@OnClick(R.id.tv_loadmore)
	public void tv_loadmoreClick(View v) {
		if (!isClickable) {
			book_description.setMaxLines(book_description.getLineCount());
			tv_loadmore.setText("����");
			isClickable = true;
		} else {
			book_description.setMaxLines(MAX_LINES);
			tv_loadmore.setText("�鿴����");
			isClickable = false;
		}
	}

	@OnClick(R.id.back_view)
	public void back_viewClick(View v) {
		onBackPressed();
	}

	/**
	 * ʹ��Handler����UI������Ϣ
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
			Toast.makeText(BookInfoActivity.this, "������ɣ�", Toast.LENGTH_LONG).show();
			// }
			// mMessageView.setText("���ؽ���:" + progress + " %");

		}
	};

	@OnClick(R.id.classinbtn)
	public void classinbtnClick(View v) {
		if (!cbtn) {
			userbook = new UserBook();
			userbook.setCode(app.user.getCode());
			userbook.setBookid(books.getBookid());
			tast = new myAsyncTast();// ����AsyncTask
			tast.execute();// ����AsyncTask
		} else {
			tast = new myAsyncTast();// ����AsyncTask
			tast.execute();// ����AsyncTask
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_info);
		ViewUtils.inject(this);
		userervice = new UserService(this);
		booksService = new BooksService(this);
		header_title.setText("�α�����");
		app = (App) getApplicationContext();
		mCache = ACache.get(this);
		scro.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		books = (Books) getIntent().getSerializableExtra("bookview");
		book_view = (ImageView) findViewById(R.id.book_view);
		Bitmap bitmap = BitmapFactory.decodeFile(books.getPhone_cover());
		book_view.setImageBitmap(bitmap);
		book_name.setText(Tools.ToDBC(books.getBookname()));
		book_author.setText(Tools.ToDBC("���ߣ�" + books.getAuthor()));
		book_press.setText(Tools.ToDBC("�����磺" + books.getPress()));
		book_description.setMaxLines(MAX_LINES);
		if (app.user != null) {

			for (int i = 0; i < app.userbook.size(); i++) {
				if (books.getBookid() == app.userbook.get(i).getBookid()) {
					cbtn = true;
					bookid = app.userbook.get(i).getId();
					count = i;
					classinbtn.setText("������Ƴ�");
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
		scro.smoothScrollTo(0, 20);// ����������
		if (Tools.isNetworkAvailable(this)) {
			ctast = new contentAsyncTast();// ����AsyncTask
			ctast.execute();// ����AsyncTask
		} else {
			Toast.makeText(getApplicationContext(), "��ǰû�п������磡", Toast.LENGTH_LONG).show();
			classoutbtn.setClickable(false);
			classinbtn.setClickable(false);
		}
	}

	class contentAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = ProgressDialog.show(BookInfoActivity.this, "", "���Ե�...", false);// ����ProgressDialog

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
			dialog.dismiss();// �ر�ProgressDialog
			if (values[0] == 1) {
				listShow();
				scro.smoothScrollTo(0, 20);// ����������
			}

		}

	}

	/**
	 * ��ʾ�½��б�
	 */
	public void listShow() {
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < app.bookContent.size(); i++) {
			for (int j = 0; j < app.bookContent.size(); j++) {
				if (app.bookContent.get(j).getSectionNum() == (i + 1)) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("itemText", app.bookContent.get(j).getChapter());// �������ItemText
					lstImageItem.add(map);
					j=app.bookContent.size();
				}
			}
		}
		// ������������ImageItem <====> ��̬�����Ԫ�أ�����һһ��Ӧ
		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem, R.layout.mulu_liset_item,
				new String[] { "itemText" }, new int[] { R.id.zhangjie });
		// ��Ӳ�����ʾ
		mulu_lview.setAdapter(saImageItems);
		// �����Ϣ����
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
			atast = new answerAsyncTast();// ����AsyncTask
			atast.execute();// ����AsyncTask
		}

	}

	class myAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(BookInfoActivity.this, "", "���Ե�...", false);// ����ProgressDialog
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
			dialog.dismiss();// �ر�ProgressDialog
			bookChange();
			if (values[0] == 1) {
				cbtn = true;
				count = app.userbook.size() - 1;
				classinbtn.setText("������Ƴ�");
			}
			if (values[0] == 2) {
				cbtn = false;
				classinbtn.setText("�������");
			}
		}

	}

	/**
	 * �����û��鱾����
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
	 * �ӷ��������ش�
	 */
	class answerAsyncTast extends AsyncTask<Void, Integer, Void> {

		private String downloadUrl;// �������ӵ�ַ
		private int threadNum;// �������߳���
		private String filePath;// �����ļ�·����ַ
		private int blockSize;// ÿһ���̵߳�������

		public void downloadTask() {
			this.downloadUrl = app.loadUrl + "upload/answer/" + bookContent.getAnswer();
			this.threadNum = 5;
			this.filePath = Environment.getExternalStorageDirectory() + "/studentsos/answer/" + bookContent.getAnswer();
			FileDownloadThread[] threads = new FileDownloadThread[threadNum];
			try {
				URL url = new URL(downloadUrl);
				Log.d(TAG, "download file http path:" + downloadUrl);
				URLConnection conn = url.openConnection();
				// ��ȡ�����ļ��ܴ�С
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					System.out.println("��ȡ�ļ�ʧ��");
					return;
				}
				// ����ProgressBar���ĳ���Ϊ�ļ�Size
				// mProgressbar.setMax(fileSize);

				// ����ÿ���߳����ص����ݳ���
				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum : fileSize / threadNum + 1;

				Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");

				File file = new File(filePath);
				for (int i = 0; i < threads.length; i++) {
					// �����̣߳��ֱ�����ÿ���߳���Ҫ���صĲ���
					threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				boolean isfinished = false;
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;
					// ��ǰ�����߳���������
					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}
					// ֪ͨhandlerȥ������ͼ���
					Thread.sleep(1000);// ��Ϣ1����ٶ�ȡ���ؽ���
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
			dialog = ProgressDialog.show(BookInfoActivity.this, "", "���Ե�...", false);// ����ProgressDialog
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
						} // �ӷ���������
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
					} // �ӷ���������
				}

			}

			publishProgress(3);
			return null;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			dialog.dismiss();// �ر�ProgressDialog
			intent.putExtras(mBundle);
			startActivity(intent);
		}

	}

	/**
	 * ����׼����������ȡSD��·���������߳�
	 */
	private void doDownload() {
		// ��ȡSD��·��
		String path = Environment.getExternalStorageDirectory() + "/amosdownload/";
		File file = new File(path);
		// ���SD��Ŀ¼�����ڴ���
		if (!file.exists()) {
			file.mkdir();
		}
		// ����progressBar��ʼ��
		// mProgressbar.setProgress(0);

		// ����������Ȱ�URL���ļ�����д������ʵ��Щ������ͨ��HttpHeader��ȡ��
		String downloadUrl = "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk";
		String fileName = "baidu_16785426.apk";
		int threadNum = 5;
		String filepath = path + fileName;
		Log.d(TAG, "download file  path:" + filepath);
		downloadTask task = new downloadTask();
		task.start();
	}

	/**
	 * ���߳��ļ�����
	 * 
	 * @author yangxiaolong @2014-8-7
	 */
	class downloadTask extends Thread {
		private String downloadUrl;// �������ӵ�ַ
		private int threadNum;// �������߳���
		private String filePath;// �����ļ�·����ַ
		private int blockSize;// ÿһ���̵߳�������

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
				// ��ȡ�����ļ��ܴ�С
				int fileSize = conn.getContentLength();
				if (fileSize <= 0) {
					System.out.println("��ȡ�ļ�ʧ��");
					return;
				}
				// ����ProgressBar���ĳ���Ϊ�ļ�Size
				// mProgressbar.setMax(fileSize);

				// ����ÿ���߳����ص����ݳ���
				blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum : fileSize / threadNum + 1;

				Log.d(TAG, "fileSize:" + fileSize + "  blockSize:");

				File file = new File(filePath);
				for (int i = 0; i < threads.length; i++) {
					// �����̣߳��ֱ�����ÿ���߳���Ҫ���صĲ���
					threads[i] = new FileDownloadThread(url, file, blockSize, (i + 1));
					threads[i].setName("Thread:" + i);
					threads[i].start();
				}

				boolean isfinished = false;
				int downloadedAllSize = 0;
				while (!isfinished) {
					isfinished = true;
					// ��ǰ�����߳���������
					downloadedAllSize = 0;
					for (int i = 0; i < threads.length; i++) {
						downloadedAllSize += threads[i].getDownloadLength();
						if (!threads[i].isCompleted()) {
							isfinished = false;
						}
					}
					// ֪ͨhandlerȥ������ͼ���
					Message msg = new Message();
					msg.getData().putInt("size", downloadedAllSize);
					mHandler.sendMessage(msg);
					// Log.d(TAG, "current downloadSize:" + downloadedAllSize);
					Thread.sleep(1000);// ��Ϣ1����ٶ�ȡ���ؽ���
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
