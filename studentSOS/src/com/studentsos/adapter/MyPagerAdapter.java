package com.studentsos.adapter;

import com.studentsos.fragment.HomeInFragment;
import com.studentsos.fragment.HomeOutFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {
	FragmentManager fm;
	/**
	 * 课内界面的Fragment
	 */
	private HomeInFragment homeInFragment;

	/**
	 * 发现界面的Fragment
	 */
	private HomeOutFragment homeOutFragment;
	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	private final String[] titles = { "书架", "课表" };

	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			if (homeInFragment == null) {
				homeInFragment = new HomeInFragment();
			}
			return homeInFragment;
		case 1:
			if (homeOutFragment == null) {
				homeOutFragment = new HomeOutFragment();
			}
			return homeOutFragment;
		default:
			return null;
		}
	}
	
}
