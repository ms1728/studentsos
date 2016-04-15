package com.studentsos.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.studentsos.R;
import com.studentsos.entity.Books;
import com.studentsos.fragment.HomeFragment;
import com.studentsos.service.BooksService;
import com.studentsos.tools.App;
import com.studentsos.tools.Tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends BasicActivity {

	@ViewInject(R.id.etSearch)
	private EditText etSearch;
	@ViewInject(R.id.ivDeleteText)
	private ImageView ivDeleteText;
	@ViewInject(R.id.Searc_lview)
	private ListView searc_lview;
	@ViewInject(R.id.louding_Te)
	private TextView louding;
	private List<Books> sbooks;
	InputMethodManager imm;
	App app;
	BooksService booksService;
	private int[] count;
	private int MID;
	ArrayList<HashMap<String, Object>> lstImageItem;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (sbooks.size() == 0) {
					louding.setText("不存在相关书籍");
				} else {
					louding.setVisibility(View.GONE);
					listShow();
				}
				break;
			}

		}

	};

	@OnClick(R.id.back_view)
	public void back_viewClick(View v) {
		onBackPressed();
	}

	@OnClick(R.id.ivDeleteText)
	public void ivDeleteTextClick(View v) {
		etSearch.setText("");
	}

	@OnClick(R.id.btnSearch)
	public void btnSearchClick(View v) {

		imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0); // 隐藏软键盘，当单击搜索输入框时弹出
		louding.setText("加载中...");
		louding.setVisibility(View.VISIBLE);
		searc_lview.setVisibility(View.GONE);
		new Thread(networkTask).start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		ViewUtils.inject(this);
		booksService = new BooksService(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		etSearch.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ivDeleteText.setVisibility(View.GONE);
				} else {
					ivDeleteText.setVisibility(View.VISIBLE);
				}
			}
		});
		listShow();
	}

	Runnable networkTask = new Runnable() {

		@Override
		public void run() {
			// TODO
			// 在这里进行 http request.网络请求相关操作
			try {
				sbooks = booksService.searchbook(etSearch.getText().toString());
				if (sbooks != null) {
					app = (App) getApplicationContext();
					for (int i = 0; i < sbooks.size(); i++) {
						String strPath = Environment.getExternalStorageDirectory() + "/studentsos/bookpicture/"
								+ "keben_" + sbooks.get(i).getBookid() + ".png";
						File f = new File(strPath);
						if (!f.exists()) {
							String path = booksService.loadcover(app.loadUrl + "upload/" + sbooks.get(i).getWeb_cover(),
									sbooks.get(i).getBookid());
							sbooks.get(i).setPhone_cover(path);
						} else
							sbooks.get(i).setPhone_cover(strPath);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}
	};

	public void listShow() {
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		if (sbooks != null) {
			count = new int[sbooks.size()];
			for (int i = 0, coun = 0; i < sbooks.size(); i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("itemImage", sbooks.get(i).getPhone_cover());// 添加图像资源的ID
				map.put("itemText", sbooks.get(i).getBookname());// 按序号做ItemText
				map.put("sear_author", sbooks.get(i).getAuthor());
				map.put("sear_press", sbooks.get(i).getPress());
				count[coun++] = sbooks.get(i).getBookid();
				lstImageItem.add(map);
			}
		}
		MyAdapter adapter = new MyAdapter(this);
		// 添加并且显示
		searc_lview.setVisibility(View.VISIBLE);
		searc_lview.setAdapter(adapter);
		// 添加消息处理
		// Tools.setListViewHeightBasedOnChildren(searc_lview);
		searc_lview.setOnItemClickListener(new ItemClickListener());

	}

	public final class ViewHolder {
		public ImageView itemImage;
		public TextView itemText;
		public TextView sear_author;
		public TextView sear_press;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lstImageItem.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();

				convertView = mInflater.inflate(R.layout.searc_liset_item, null);
				holder.itemImage = (ImageView) convertView.findViewById(R.id.sear_ItemImage);
				holder.itemText = (TextView) convertView.findViewById(R.id.sear_ItemText);
				holder.sear_author = (TextView) convertView.findViewById(R.id.sear_author);
				holder.sear_press = (TextView) convertView.findViewById(R.id.sear_press);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			Bitmap bitmap = BitmapFactory.decodeFile((String) (lstImageItem.get(position).get("itemImage")));
			holder.itemImage.setImageBitmap(bitmap);
			holder.itemText.setText(Tools.ToDBC((String) lstImageItem.get(position).get("itemText")));
			holder.sear_author.setText(Tools.ToDBC("作者：" + (String) lstImageItem.get(position).get("sear_author")));
			holder.sear_press.setText(Tools.ToDBC("出版社：" + (String) lstImageItem.get(position).get("sear_press")));
			return convertView;
		}

	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (sbooks != null) {
				Intent intent = new Intent(SearchActivity.this, BookInfoActivity.class);
				Bundle mBundle = new Bundle();
				for (int i = 0; i < sbooks.size(); i++) {
					if (sbooks.get(i).getBookid() == count[arg2]) {
						mBundle.putSerializable("bookview", sbooks.get(i));
						break;
					}
				}
				intent.putExtras(mBundle);
				startActivity(intent);
			}
		}

	}

}
