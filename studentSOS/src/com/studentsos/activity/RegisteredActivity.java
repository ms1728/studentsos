package com.studentsos.activity;

import com.studentsos.R;
import com.studentsos.service.RegisterService;
import com.studentsos.service.SchoolService;
import com.studentsos.tools.App;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisteredActivity extends BasicActivity {

	RegisterService registerService;
	SchoolService schoolService;
	App app;
	private myAsyncTast tast;
	private String code;
	private String nicheng;
	private String password;
	private String email;
	private String school;
	@ViewInject(R.id.header_title)
	private TextView header_title;
	@ViewInject(R.id.codeEdt)
	private EditText codeEdt;
	@ViewInject(R.id.regis_nichengEdt)
	private EditText regis_nichengEdt;
	@ViewInject(R.id.regis_passEdt)
	private EditText regis_passEdt;
	@ViewInject(R.id.regis_emEdt)
	private EditText regis_emEdt;
	ProgressDialog dialog = null;

	@OnClick(R.id.back_view)
	public void back_viewClick(View v) {
		onBackPressed();
	}

	@OnClick(R.id.regisyesbtn)
	public void regisyesbtnClick(View v) {
		code = codeEdt.getText().toString();
		nicheng = regis_nichengEdt.getText().toString();
		password = regis_passEdt.getText().toString();
		email = regis_emEdt.getText().toString();
		//school = regis_schoolEdt.getText().toString();
		if (!email.matches("\\w+@\\w+\\.\\w+")) {
			showMessageDialog("�����ʽ����", R.drawable.warning, null);
			return;
		}
		if (code.length() == 0) {
			showMessageDialog("�������û���", R.drawable.warning, null);
			return;
		}
		/*if (school.length() == 0) {
			showMessageDialog("������ѧУ", R.drawable.warning, null);
			return;
		}*/
		if (nicheng.length() == 0) {
			showMessageDialog("�������ǳ�", R.drawable.warning, null);
			return;
		}
		if (password.length() == 0) {
			showMessageDialog("����������", R.drawable.warning, null);
			return;
		}
		if (email.length() == 0) {
			showMessageDialog("����������", R.drawable.warning, null);
			return;
		}
		tast = new myAsyncTast();// ����AsyncTask
		tast.execute();// ����AsyncTask

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registered);
		ViewUtils.inject(this);
		header_title.setText("ע��");
		registerService = new RegisterService(this);
		schoolService = new SchoolService(this);
		app = (App) getApplicationContext();

	}

	class myAsyncTast extends AsyncTask<Void, Integer, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = ProgressDialog.show(RegisteredActivity.this, "", "����ע�ᣬ���Ե�...", false);// ����ProgressDialog
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			try {
				int count = registerService.register(code, nicheng, password, email);
				switch (count) {
				case 0:
					break;
				case 1:
					publishProgress(1);
					return null;
				case 2:

					publishProgress(2);
					return null;
				case 3:

					publishProgress(3);
					return null;
				case 4:

					publishProgress(4);
					return null;
				case 5:
					publishProgress(5);
					return null;
				case 6:
					//app.schoolMajor = schoolService.schoolmajor(school);
					publishProgress(5);
					return null;
				}
			} catch (Exception e) {
				e.printStackTrace();
				publishProgress(7);
				return null;

			}
			publishProgress(8);
			return null;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			dialog.dismiss();// �ر�ProgressDialog
			if (values[0] == 1) {
				Toast.makeText(getApplicationContext(), "���䲻����", Toast.LENGTH_SHORT).show();
				return;
			}
			if (values[0] == 2) {
				Toast.makeText(getApplicationContext(), "�û����ѱ�ʹ��", Toast.LENGTH_SHORT).show();
				return;
			}
			if (values[0] == 3) {
				Toast.makeText(getApplicationContext(), "�ǳ��ѱ�ʹ��", Toast.LENGTH_SHORT).show();
				return;
			}
			if (values[0] == 4) {
				Toast.makeText(getApplicationContext(), "�����ѱ�ʹ��", Toast.LENGTH_SHORT).show();
				return;
			}
			if (values[0] == 5) {
				Toast.makeText(getApplicationContext(), "ע��ɹ���", Toast.LENGTH_SHORT).show();
				RegisteredActivity.this.finish();
				return;
			}
			if (values[0] == 6) {
				Intent intent = new Intent(RegisteredActivity.this, RegistSchoolActivity.class);
				intent.putExtra("code", code);
				startActivity(intent);
				RegisteredActivity.this.finish();
				return;
			}
			if (values[0] == 7) {
				Toast.makeText(getApplicationContext(), "ע��ʧ��", Toast.LENGTH_SHORT).show();

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
