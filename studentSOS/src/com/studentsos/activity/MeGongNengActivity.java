package com.studentsos.activity;

import com.studentsos.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MeGongNengActivity extends Activity {

	@ViewInject(R.id.header_title)
	private TextView header_title;
	@OnClick(R.id.back_view)
	public void back_viewClick(View v) { 
	    finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.me_gongneng);
		ViewUtils.inject(this);
		header_title.setText("π¶ƒ‹ΩÈ…‹");
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
