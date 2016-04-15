package com.studentsos.activity;



import com.studentsos.R;
import com.studentsos.entity.User;
import com.studentsos.tools.App;
import com.studentsos.view.SwipeBackLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;

public abstract class BasicActivity extends Activity {
	AlertDialog.Builder dialogBuilder;	
	protected SwipeBackLayout layout;
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		App app= (App)getApplication(); //获取App实例
		@SuppressWarnings("unused")
		User user=app.user;              //获取当前登录用户实例
		layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
				R.layout.base, null);
		layout.attachToActivity(this);
		
		dialogBuilder =new AlertDialog.Builder(this);	
	}
	/**显示一个简单的消息对话框
	 * message 消息
	 * iconId  图标ID
	 * 
	 */
	protected void showMessageDialog(String message,int iconId,
		DialogInterface.OnClickListener onClickListener)
	{
		dialogBuilder.setIcon(iconId);   //设置图标ID
		dialogBuilder.setTitle(message);  //设置消息
		dialogBuilder.setPositiveButton("确定", onClickListener);		//确定按钮事件
		dialogBuilder.create().show();   //显示对话框
	}
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
	}


	// Press the back button in mobile phone
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(0, R.anim.base_slide_right_out);
	}

}
