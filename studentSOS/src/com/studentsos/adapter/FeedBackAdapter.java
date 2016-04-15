package com.studentsos.adapter;

import java.util.ArrayList;

import com.studentsos.R;
import com.studentsos.entity.FeedBack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedBackAdapter extends BaseAdapter {

	private ArrayList<FeedBack> mItems = null;

	private Context mContext;

	public FeedBackAdapter(Context context) {
		super();
		mContext = context;
		mItems = new ArrayList<FeedBack>();
	}
	
	public ArrayList<FeedBack> getItems() {
		return mItems;
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		FeedBack item = (FeedBack) getItem(position);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.feedlist_item, null);
			holder = new ViewHolder();
			holder.contentText = (TextView) convertView
					.findViewById(R.id.content_text);
			holder.timeText = (TextView) convertView
					.findViewById(R.id.time_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.contentText.setText(item.mContent);
		holder.timeText.setText(item.mDateTime);

		return convertView;
	}

	private class ViewHolder {
		TextView contentText;
		TextView timeText;
	}

}
