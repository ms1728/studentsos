package com.studentsos.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.studentsos.R;
import com.studentsos.activity.BookInfoActivity;
import com.studentsos.activity.SearchActivity;
import com.studentsos.entity.Books;
import com.studentsos.entity.UserBook;
import com.studentsos.service.UserService;
import com.studentsos.tools.ACache;
import com.studentsos.tools.App;
import com.studentsos.tools.Tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class HomeInFragment extends Fragment {

	@SuppressWarnings("unused")
	private View mParent;
	List<Books> list;
	App app;
	ProgressDialog dialog = null;
	private myAsyncTast tast;
	UserService userervice;
	private int[] count;
	ArrayList<HashMap<String, Object>> lstImageItem;
	@SuppressWarnings("unused")
	private FragmentActivity mActivity;
	AdapterContextMenuInfo menuInfo;
	private GridView nei_gridview;
	private Books booksEnd = null;
	private UserBook userbookEnd = null;
	ACache mCache;
	@SuppressWarnings("unused")
	private TextView mText;
	public ImageView itemImage;
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home_in, container, false);
		app = (App) getActivity().getApplicationContext();
		nei_gridview = (GridView) view.findViewById(R.id.kenei_view);
		if (app.books != null) {
			listShow();
		}

		return view;
	}
	public void listShow() {
		lstImageItem = new ArrayList<HashMap<String, Object>>();
		count = new int[app.books.size()];
		for (int i = 0, coun = 0; i < app.books.size(); i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", app.books.get(i).getPhone_cover());// 添加图像资源的ID
			map.put("itemText", app.books.get(i).getBookname());// 按序号做ItemText
			count[coun++] = app.books.get(i).getBookid();
			lstImageItem.add(map);

		}
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("itemImage", R.drawable.load_more);// 添加图像资源的ID
		lstImageItem.add(map);
		MyAdapter adapter = new MyAdapter(getActivity());
		nei_gridview.setAdapter(adapter); // 添加消息处理
		nei_gridview.setOnItemClickListener(new ItemClickListener());
		ItemOnLongClick1();
	}
	public final class ViewHolder {
		public ImageView itemImage;
		public TextView itemText;
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

				convertView = mInflater.inflate(R.layout.kenei_liset, null);
				holder.itemImage = (ImageView) convertView.findViewById(R.id.nei_ItemImage);
				holder.itemText = (TextView) convertView.findViewById(R.id.nei_ItemText);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}
			if (lstImageItem.get(position).get("itemText") != null) {
				Bitmap bitmap = BitmapFactory.decodeFile((String) (lstImageItem.get(position).get("itemImage")));
				holder.itemImage.setImageBitmap(bitmap);
				holder.itemText.setText(Tools.ToDBC((String) lstImageItem.get(position).get("itemText")));
			} else {
				holder.itemImage.setImageResource(R.drawable.load_more);
				holder.itemText.setText("");
			}
			return convertView;
		}

	}

	class ItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			app = (App) getActivity().getApplicationContext();
			Intent intent = new Intent(getActivity(), BookInfoActivity.class);
			Bundle mBundle = new Bundle();
			if (arg2 == app.books.size()) {
				intent = new Intent(getActivity(), SearchActivity.class);
				getActivity().startActivityForResult(intent, 2);
			} else {
				for (int i = 0; i < app.books.size(); i++) {
					if (app.books.get(i).getBookid() == count[arg2]) {
						mBundle.putSerializable("bookview", app.books.get(i));
						break;
					}
				}
				intent.putExtras(mBundle);
				getActivity().startActivityForResult(intent, 2);
			}
		}

	}
	private void ItemOnLongClick1() {
		// 注：setOnCreateContextMenuListener是与下面onContextItemSelected配套使用的
		nei_gridview.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.add(0, 0, 0, "从书架移除");

			}
		});
	}

	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {
		 menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case 0:
			tast = new myAsyncTast();// 创建AsyncTask
			tast.execute();// 启动AsyncTask
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}
	class myAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(getActivity(), "", "请稍等...", false);// 创建ProgressDialog
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			
				try {
					userervice.deletebook(app.userbook.get(menuInfo.position).getId());
					app.userbook.remove(menuInfo.position);
					app.books.remove(menuInfo.position);
					publishProgress(2);
					return null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			publishProgress(3);
			return null;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			dialog.dismiss();// 关闭ProgressDialog
			bookChange();
			if (values[0] == 2) {
				listShow();
			}else
				Toast.makeText(getActivity().getApplicationContext(), "移除失败！", Toast.LENGTH_SHORT).show();
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		userervice = new UserService(getActivity());
		mParent = getView();
		mCache = ACache.get(getActivity());
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}