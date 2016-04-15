package com.studentsos.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.studentsos.R;
import com.studentsos.entity.School;
import com.studentsos.fragment.HomeFragment;
import com.studentsos.service.ConfigService;
import com.studentsos.service.FaceImageService;
import com.studentsos.service.SchoolService;
import com.studentsos.service.UserService;
import com.studentsos.tools.ACache;
import com.studentsos.tools.App;
import com.studentsos.tools.Tools;
import com.studentsos.view.SaveFace;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("NewApi")
public class MePersonActivity extends BasicActivity {

	private static String TAG;
	private String[] items = new String[] { "选择本地图片", "拍照" };
	/* 头像名称 */
	private static final String IMAGE_FILE_NAME = "faceImage";
	private static final String IMAGE_JPG = ".jpg";
	private String code;
	private String count;
	ProgressDialog dialog = null;
	// private Syllabus syllabus;
	private myAsyncTast tast;
	FaceImageService faceImageService;
	SchoolService schoolService;
	UserService userService;
	ACache mCache;
	// 保存图片本地路径
	private static final String IMGPATH =Environment.getExternalStorageDirectory() + "/studentsos/haed";

	//private static final String TMP_IMAGE_FILE_NAME = "tmp_faceImage.jpeg";

	// 常量定义
	public static final int TAKE_A_PICTURE = 10;
	public static final int SELECT_A_PICTURE = 20;
	public static final int SET_PICTURE = 30;
	public static final int SET_ALBUM_PICTURE_KITKAT = 40;
	public static final int SELECET_A_PICTURE_AFTER_KIKAT = 50;
	private String mAlbumPicturePath = null;

	// 版本比较：是否是4.4及以上版本
	final boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	private List<String> depart, major, classCount;
	private ArrayAdapter<String> adapter, adapter1;
	private static final String[] year = { "2010", "2011", "2012", "2013", "2014", "2015" };
	School school = new School();
	School nowSchool = new School();
	private String eduSchoolYear;
	private int classnum;
	private Bitmap head;// 头像Bitmap
	private App app;
	private File facepath;
	@ViewInject(R.id.nichengEdt)
	private EditText nichengEdt;
	@ViewInject(R.id.person_touxiang)
	private ImageView person_touxiang;
	@ViewInject(R.id.header_title)
	private TextView header_title;
	@ViewInject(R.id.Spinner_school)
	private TextView Spinner_school;
	@ViewInject(R.id.Spinner_departTe)
	private TextView Spinner_departTe;
	@ViewInject(R.id.Spinner_majorTe)
	private TextView Spinner_majorTe;
	@ViewInject(R.id.Spinner_yearTe)
	private TextView Spinner_yearTe;
	@ViewInject(R.id.Spinner_classTe)
	private TextView Spinner_classTe;
	@ViewInject(R.id.Spinner_depart)
	private Spinner Spinner_depart;
	@ViewInject(R.id.Spinner_major)
	private Spinner Spinner_major;
	@ViewInject(R.id.Spinner_year)
	private Spinner Spinner_year;
	@ViewInject(R.id.Spinner_classcount)
	private Spinner Spinner_classcount;
	@ViewInject(R.id.linear_info)
	private LinearLayout linear_info;
	@ViewInject(R.id.relat_depart)
	private RelativeLayout relat_depart;
	@ViewInject(R.id.relat_major)
	private RelativeLayout relat_major;
	@ViewInject(R.id.relat_year)
	private RelativeLayout relat_year;
	@ViewInject(R.id.relat_class)
	private RelativeLayout relat_class;

	@OnClick(R.id.back_view)
	public void back_viewClick(View v) {
		onBackPressed();
	}

	@OnClick(R.id.relat_school)
	public void relat_schoolClick(View v) {
		Intent intent = new Intent(MePersonActivity.this, SchoolSearchActivity.class);
		startActivityForResult(intent, 1);
	}

	@OnClick(R.id.person_touxiang)
	public void person_touxiangClick(View v) {
		showDialog();
	}

	@OnClick(R.id.personyesbtn)
	public void personyesbtnClick(View v) {
		if (Tools.isNetworkAvailable(this)) {
			tast = new myAsyncTast();// 创建AsyncTask
			tast.execute();// 启动AsyncTask
		} else
			Toast.makeText(getApplicationContext(), "当前没有可用网络！", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_person);
		ViewUtils.inject(this);
		header_title.setText("个人信息");
		faceImageService = new FaceImageService(this);
		userService = new UserService(this);
		schoolService = new SchoolService(this);
		mCache = ACache.get(this);
		app = (App) getApplicationContext();
		File imagepath = new File(IMGPATH);
		if (!imagepath.exists()) {
			Log.i("zou", "imagepath.mkdir()");
			imagepath.mkdir();
		}
		nichengEdt.setText(app.user.getName().toString());
		code = app.user.getCode().toString() + "_";
		count = Integer.toString(app.user.getTouxiang_numb() + 1);
		String facepath = app.user.getTouxiang();
		if (facepath != null) {
			Bitmap bt = BitmapFactory.decodeFile(facepath);// 从Sd中找头像，转换成Bitmap
			Drawable drawable = new BitmapDrawable(getResources(), bt);
			person_touxiang.setImageDrawable(drawable);
		}
	}

	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this).setTitle("设置头像").setItems(items, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					if (mIsKitKat) {
						selectImageUriAfterKikat();
					} else {
						cropImageUri();
					}
					break;
				case 1:

					Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// 判断存储卡是否可以用，可用进行存储
					if (Tools.hasSdcard()) {

						intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
								Uri.fromFile(new File(IMGPATH,
										code + IMAGE_FILE_NAME + count + IMAGE_JPG)));
					}
					startActivityForResult(intentFromCapture, TAKE_A_PICTURE);
					Log.i("zou", "TAKE_A_PICTURE");
					break;
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_CANCELED) {
			switch (requestCode) {
			case SELECT_A_PICTURE:
				if (resultCode == RESULT_OK && null != data) {
					// Log.i("zou", "4.4以下的");
					head = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, code + IMAGE_FILE_NAME + count + IMAGE_JPG)));
					person_touxiang.setImageBitmap(head);
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "取消头像设置", Toast.LENGTH_SHORT).show();
				}
				break;
			case SELECET_A_PICTURE_AFTER_KIKAT:
				if (resultCode == RESULT_OK && null != data) {
					// Log.i("zou", "4.4以上的");
					mAlbumPicturePath = getPath(getApplicationContext(), data.getData());
					cropImageUriAfterKikat(Uri.fromFile(new File(mAlbumPicturePath)));
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "取消头像设置", Toast.LENGTH_SHORT).show();
				}
				break;
			case SET_ALBUM_PICTURE_KITKAT:
				Log.i("zou", "4.4以上上的 RESULT_OK");

				head = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, code + IMAGE_FILE_NAME + count + IMAGE_JPG)));
				person_touxiang.setImageBitmap(head);

				// Log.i("zou", "4.4以上上的 RESULT_OK");
				// Bitmap bitmap = data.getParcelableExtra("data");
				// mAcountHeadIcon.setImageBitmap(bitmap);
				break;
			case TAKE_A_PICTURE:
				Log.i("zou", "TAKE_A_PICTURE-resultCode:" + resultCode);
				if (resultCode == RESULT_OK) {
					cameraCropImageUri(Uri.fromFile(new File(IMGPATH, code + IMAGE_FILE_NAME + count + IMAGE_JPG)));
				} else {
					Toast.makeText(this, "取消头像设置", Toast.LENGTH_SHORT).show();
				}
				break;
			case SET_PICTURE:
				// 拍照的设置头像 不考虑版本
				// Log.i("zou", "SET_PICTURE-resultCode:" + resultCode);
				if (resultCode == RESULT_OK && null != data) {
					head = decodeUriAsBitmap(Uri.fromFile(new File(IMGPATH, code + IMAGE_FILE_NAME + count + IMAGE_JPG)));
					person_touxiang.setImageBitmap(head);
				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(this, "取消头像设置", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "设置头像失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}

	class myAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(MePersonActivity.this, "", "请稍等...", false);// 创建ProgressDialog
		}

		@SuppressWarnings("static-access")
		@Override
		protected Void doInBackground(Void... arg0) {

			if (head != null) {

				SaveFace saveface = new SaveFace();
				String path = saveface.saveFace(head, app.user.getCode().toString() + "_",
						app.user.getTouxiang_numb() + 1);
				facepath = new File(path);
				app.user.setTouxiang_numb(app.user.getTouxiang_numb() + 1);
				try {
					faceImageService.sendface(facepath);
				} catch (Exception e) {
					e.printStackTrace();
					publishProgress(1);
					return null;
				}
				app.user.setTouxiang(path);
			}
			app.user.setName(nichengEdt.getText().toString());
			mCache.put("user", app.user, 7 * ACache.TIME_DAY);
			try {
				userService.update();
			} catch (Exception e) {
				e.printStackTrace();
				publishProgress(1);
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
				showMessageDialog("保存失败，请检查网络", R.drawable.not, null);
				return;
			}
			if (values[0] == 3) {
				// Intent intent = new Intent();
				// intent.putExtra("result", app.user.getName().toString());
				// setResult(RESULT_OK, intent);
				finish();
			}
		}

	}

	/**
	 * <br>
	 * 功能简述:裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	private void cropImageUri() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMGPATH, code + IMAGE_FILE_NAME + count + IMAGE_JPG)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, SELECT_A_PICTURE);
	}

	/**
	 * <br>
	 * 功能简述:4.4以上裁剪图片方法实现---------------------- 相册 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void selectImageUriAfterKikat() {
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(intent, SELECET_A_PICTURE_AFTER_KIKAT);
	}

	/**
	 * <br>
	 * 功能简述:裁剪图片方法实现----------------------相机 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 */
	private void cameraCropImageUri(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		// if (mIsKitKat) {
		// intent.putExtra("return-data", true);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// } else {
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// }
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, SET_PICTURE);
	}

	/**
	 * <br>
	 * 功能简述: 4.4及以上改动版裁剪图片方法实现 --------------------相机 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 */
	private void cropImageUriAfterKikat(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/jpeg");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 640);
		intent.putExtra("outputY", 640);
		intent.putExtra("scale", true);
		// intent.putExtra("return-data", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(IMGPATH, code + IMAGE_FILE_NAME + count + IMAGE_JPG)));
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, SET_ALBUM_PICTURE_KITKAT);
	}

	/**
	 * <br>
	 * 功能简述: <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param uri
	 * @return
	 */
	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * <br>
	 * 功能简述:4.4及以上获取图片的方法 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param context
	 * @param uri
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {

			// Return the remote address
			if (isGooglePhotosUri(uri))
				return uri.getLastPathSegment();

			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

}
