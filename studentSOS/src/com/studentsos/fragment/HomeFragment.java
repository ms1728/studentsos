package com.studentsos.fragment;




import com.studentsos.R;
import com.studentsos.adapter.MyPagerAdapter;
import com.studentsos.tools.PagerSlidingTabStrip;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.SimpleAdapter;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	SimpleAdapter adapter;
	@SuppressWarnings("unused")
	private View mParent;
	View view;
	ViewPager pager;
	@SuppressWarnings("unused")
	private FragmentActivity mActivity;
	private PagerSlidingTabStrip tabs;
	private DisplayMetrics dm = new DisplayMetrics();
	@SuppressWarnings("unused")
	private TextView mText;

	/**
	 * Create a new instance of DetailsFragment, initialized to show the text at
	 * 'index'.
	 */
	public static HomeFragment newInstance(int index) {
		HomeFragment f = new HomeFragment();

		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		f.setArguments(args);

		return f;
	}
	public int getShownIndex() {
		return getArguments().getInt("index", 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home, container, false);
		bookFragment();

		return view;
	}
	public void bookFragment() {
		dm = getResources().getDisplayMetrics();
		pager = (ViewPager) view.findViewById(R.id.pager);
		tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
		pager.setOverScrollMode(android.view.View.OVER_SCROLL_NEVER);
		pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
		setTabsValue();
		tabs.setViewPager(pager);
	}
	private void setTabsValue() {
		// 设置Tab是自动填充满屏幕的
		tabs.setShouldExpand(true);
		// 设置Tab的分割线是透明的
		tabs.setDividerColor(Color.TRANSPARENT);
		// 设置Tab底部线的高度
		tabs.setUnderlineHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 1, dm));
		// 设置Tab Indicator的高度
		tabs.setIndicatorHeight((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, dm));
		tabs.setTextColor(Color.parseColor("#FFFFFF"));
		// 设置Tab标题文字的大小
		tabs.setTextSize((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 14, dm));
		// 设置Tab Indicator的颜色
		tabs.setIndicatorColor(Color.parseColor("#b1bc39"));
		tabs.setBackgroundColor(Color.parseColor("#0099cc"));
		// 设置选中Tab文字的颜色 (这是我自定义的一个方法)
		// tabs.setSelectedTextColor(Color.parseColor("#45c01a"));
		// 取消点击Tab时的背景色
		tabs.setTabBackground(0);
	}



	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();

		mParent = getView();

	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

}