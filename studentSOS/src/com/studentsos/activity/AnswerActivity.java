package com.studentsos.activity;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;

import com.studentsos.R;
import com.studentsos.activity.LoginActivity.myAsyncTast;
import com.studentsos.entity.BookContent;
import com.studentsos.service.BooksService;
import com.studentsos.tools.App;
import com.studentsos.tools.Tools;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AnswerActivity extends Activity {

	private BooksService booksService;
	Bitmap mCurPageBitmap, mNextPageBitmap;
	Canvas mCurPageCanvas, mNextPageCanvas;
	private myAsyncTast tast;
	WebView webView;
	WebSettings webSettings;
	DisplayMetrics dm;
	// private String docPath
	// =Environment.getExternalStorageDirectory()+"/studentsos/answer/";
	private String docName;
	private String savePath = Environment.getExternalStorageDirectory() + "/studentsos/answer/";
	// ProgressDialog dialog =null;
	@SuppressWarnings("unused")
	private BookContent bookContent;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.book_answer);
		ViewUtils.inject(this);
		booksService = new BooksService(this);
		bookContent = (BookContent) getIntent().getSerializableExtra("bookcontent");
		docName = bookContent.getAnswer();
		String name = docName.substring(0, docName.indexOf("."));
		try {
			if (!(new File(savePath + name).exists()))
				new File(savePath + name).mkdirs();
			if (!(new File(savePath + name + ".html").exists()))
				convert2Html(savePath + docName, savePath + name + ".html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// WebView加载显示本地html文件
		webView = (WebView) this.findViewById(R.id.webView);
		webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webView.loadUrl("file://" + savePath + name + ".html");
		webView.getSettings().setUseWideViewPort(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	}

	/**
	 * word文档转成html格式
	 */
	public void convert2Html(String fileName, String outPutFile)
			throws TransformerException, IOException, ParserConfigurationException {
		HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));
		WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
				DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

		// 设置图片路径
		wordToHtmlConverter.setPicturesManager(new PicturesManager() {
			public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
					float heightInches) {
				String name = docName.substring(0, docName.indexOf("."));
				return name + "/" + suggestedName;
			}
		});

		// 保存图片
		List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
		if (pics != null) {
			for (int i = 0; i < pics.size(); i++) {
				Picture pic = (Picture) pics.get(i);
				System.out.println(pic.suggestFullFileName());
				try {
					String name = docName.substring(0, docName.indexOf("."));
					pic.writeImageContent(new FileOutputStream(savePath + name + "/" + pic.suggestFullFileName()));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		wordToHtmlConverter.processDocument(wordDocument);
		Document htmlDocument = wordToHtmlConverter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.transform(domSource, streamResult);
		out.close();
		// 保存html文件
		Tools.writeFile(new String(out.toByteArray()), outPutFile);
	}


}
