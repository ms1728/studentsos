package com.studentsos.activity;

import com.studentsos.R;
import com.studentsos.activity.RegisteredActivity.myAsyncTast;
import com.studentsos.service.FindPassService;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BackPassActivity extends BasicActivity {

	FindPassService findPassService;
	@ViewInject(R.id.header_title)
	private TextView header_title;
	private myAsyncTast tast;
	private String code;
	private String repassword;
	private String password;
	private String email;
	@ViewInject(R.id.back_codeEdt)
	private EditText back_codeEdt;
	@ViewInject(R.id.new_passEdt)
	private EditText new_passEdt;
	@ViewInject(R.id.re_passEdt)
	private EditText re_passEdt;
	@ViewInject(R.id.back_emEdt)
	private EditText back_email;
	ProgressDialog dialog = null;
	@OnClick(R.id.back_view)
	public void back_viewClick(View v) { 
		onBackPressed();
	}
	@OnClick(R.id.backyesbtn)
	public void backyesbtnClick(View v) {
		code = back_codeEdt.getText().toString();
		repassword = re_passEdt.getText().toString();
		password = new_passEdt.getText().toString();
		email = back_email.getText().toString();
		if(!email.matches("\\w+@\\w+\\.\\w+")){
			showMessageDialog("邮箱格式错误", R.drawable.warning, null);
			return;
		}
		if (code.length() == 0) {
			showMessageDialog("请输入用户名", R.drawable.warning, null);
			return;
		}
		if (repassword.length() == 0) {
			showMessageDialog("请输入确认密码", R.drawable.warning, null);
			return;
		}
		if (password.length() == 0) {
			showMessageDialog("请输入新密码", R.drawable.warning, null);
			return;
		}
		if (email.length() == 0) {
			showMessageDialog("请输入邮箱", R.drawable.warning, null);
			return;
		}
		if(!repassword.equals(password))
		{
			showMessageDialog("两次输入密码不相同", R.drawable.warning, null);
			return;
		}
		tast = new myAsyncTast();// 创建AsyncTask
		tast.execute();// 启动AsyncTask

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_pass);
		ViewUtils.inject(this);
		header_title.setText("取回密码");
		findPassService=new FindPassService(this);
	}
	class myAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(BackPassActivity.this,"",
					"正在取回密码，请稍等...", false);// 创建ProgressDialog
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			try {
				int count = findPassService.findpassword(code,
						password, email);
				switch (count) {
				case 0:
					publishProgress(0);
					return null;
				case 1:
					publishProgress(1);
					return null;
				}
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
			 if(values[0]==1){
				 showMessageDialog("用户名与邮箱不匹配", R.drawable.not, null);
				 return;
	            }
			 if(values[0]==0){
				 Toast.makeText(BackPassActivity.this, "取回成功", Toast.LENGTH_SHORT).show();
				 BackPassActivity.this.finish();
				 return;
	            }
			 if(values[0]==2){
				 showMessageDialog("找回失败", R.drawable.not,
							null);
				 return;
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
