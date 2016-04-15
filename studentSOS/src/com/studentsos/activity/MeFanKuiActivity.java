package com.studentsos.activity;

import com.studentsos.R;
import com.studentsos.adapter.FeedBackAdapter;
import com.studentsos.entity.FeedBack;
import com.studentsos.entity.UserBook;
import com.studentsos.tools.ACache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MeFanKuiActivity extends BasicActivity {

	private FeedBackAdapter feedbackAdapter;
	FeedBack feedback = new FeedBack();
	List<FeedBack> feedbacklist = new ArrayList<FeedBack>();
	int count;
	@ViewInject(R.id.feedback_list)
	private ListView feedListView;
	@ViewInject(R.id.header_title)
	private TextView header_title;
	@ViewInject(R.id.feedback_content_edit)
	private EditText feedback_content_edit;
	ACache mCache;
	@OnClick(R.id.back_view)
	public void back_viewClick(View v) { 
		onBackPressed();
	}
	@OnClick(R.id.feedbackbtn)
	public void feedbackbtnClick(View v) { 
		FeedBack feedback = new FeedBack();
		feedback.mContent=feedback_content_edit.getText().toString();
	    feedback.mDateTime= new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
	    feedbackAdapter.getItems().add(feedback);
	    feedbackAdapter.notifyDataSetChanged();
	    feedListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);//滚动到底部
	    mCache.put("feedback"+count,feedback);
	    feedback_content_edit.setText("");
	    count++;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_fankui);
		ViewUtils.inject(this);
		header_title.setText("意见反馈");
		feedbackAdapter=new FeedBackAdapter(this);
		mCache = ACache.get(this);
		feedListView.setAdapter(feedbackAdapter);
		addListItem();
	}
	@SuppressLint("ResourceAsColor")
	private void addListItem() {
		for(int i=0;i<1000;i++){
			feedback=(FeedBack)mCache.getAsObject("feedback"+i);
			if(feedback!=null)
				feedbackAdapter.getItems().add(feedback);
			else{
				count=i;
				i=1000;
			}
		}
		feedbackAdapter.notifyDataSetChanged();
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
