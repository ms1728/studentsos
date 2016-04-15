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

public class SchoolSearchActivity extends BasicActivity {

	@ViewInject(R.id.header_title)
	private TextView header_title;
	@OnClick(R.id.back_view)
	public void back_viewClick(View v) { 
		onBackPressed();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		ViewUtils.inject(this);
		
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
