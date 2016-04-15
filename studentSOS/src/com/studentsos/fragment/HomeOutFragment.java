package com.studentsos.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.studentsos.R;
import com.studentsos.entity.Course;
import com.studentsos.service.CourseService;
import com.studentsos.tools.App;
import com.studentsos.tools.CommonUtil;
import com.studentsos.tools.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeOutFragment extends Fragment {

	private View mParent;
	App app;
	View view;
	private FragmentActivity mActivity;
	int random[] = new int[20];
	String zhou[] = { "", "(单)", "(双)" };
	private TextView mText;
	private List<String> color = new ArrayList();
	// 课程页面的button引用，6行5列
	private int[][] lessons = { { R.id.lesson11, R.id.lesson12, R.id.lesson13, R.id.lesson14, R.id.lesson15 },
			{ R.id.lesson21, R.id.lesson22, R.id.lesson23, R.id.lesson24, R.id.lesson25 },
			{ R.id.lesson31, R.id.lesson32, R.id.lesson33, R.id.lesson34, R.id.lesson35 },
			{ R.id.lesson41, R.id.lesson42, R.id.lesson43, R.id.lesson44, R.id.lesson45 },
			{ R.id.lesson51, R.id.lesson52, R.id.lesson53, R.id.lesson54, R.id.lesson55 },
			{ R.id.lesson61, R.id.lesson62, R.id.lesson63, R.id.lesson64, R.id.lesson65 }, };
	// 某节课的背景图,用于随机获取
	private int[] bg = { R.drawable.kb1, R.drawable.kb2, R.drawable.kb3, R.drawable.kb4, R.drawable.kb5, R.drawable.kb6,
			R.drawable.kb7, R.drawable.kb8, R.drawable.kb9, R.drawable.kb10, R.drawable.kb11, R.drawable.kb14,
			R.drawable.kb12, R.drawable.kb13, R.drawable.kb15, R.drawable.kb16, R.drawable.kb17, R.drawable.kb18,
			R.drawable.kb19, R.drawable.kb20 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home_course, container, false);
		app = (App) getActivity().getApplicationContext();
		if (app.course.size() != 0) {
			creatRandom();
			initView();
		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = getActivity();
		mParent = getView();
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		// 这里有逻辑问题，只是简单的显示了下数据，数据并不一定是显示在正确位置
		// 课程可能有重叠
		// 课程可能有1节课的，2节课的，3节课的，因此这里应该改成在自定义View上显示更合理
		List<Course> courses = app.course;// 获得数据库中的课程
		color.add(courses.get(0).getCourseName());
		Course course = null;
		// 循环遍历
		for (int i = 0; i < courses.size(); i++) {
			course = courses.get(i);// 拿到当前课程
			int dayOfWeek = course.getDayOfWeek() - 1;// 转换为lessons数组对应的下标
			if (0<=dayOfWeek&& dayOfWeek< 6) {
				int section = course.getStartSection() / 2;// 转换为lessons数组对应的下标
				Button lesson = (Button) view.findViewById(lessons[section][dayOfWeek]);// 获得该节课的button
				for (int j = 0; j < color.size(); j++) {
					if (course.getCourseName().equals(color.get(j))) {
						lesson.setBackgroundResource(bg[random[j]]);// 设置背景
						j = color.size();
					} else if (j == color.size() - 1) {
						lesson.setBackgroundResource(bg[random[j + 1]]);// 设置背景
						color.add(course.getCourseName());
						j = color.size();
					}

				}
				if ((i + 1) != courses.size() && course.getDayOfWeek() == courses.get(i + 1).getDayOfWeek()
						&& course.getStartSection() == courses.get(i + 1).getStartSection()) {
					lesson.setText(course.getCourseName() + "△" + course.getStartWeek() + "-" + course.getEndWeek()
							+ zhou[course.getEveryWeek()] + "@" + course.getClasssroom() + "\r\n"
							+ courses.get(i + 1).getCourseName() + "△" + courses.get(i + 1).getStartWeek() + "-"
							+ courses.get(i + 1).getEndWeek() + zhou[courses.get(i + 1).getEveryWeek()] + "@"
							+ courses.get(i + 1).getClasssroom());// 设置文本为课程名+“@”+教室
					i++;
				} else
					lesson.setText(course.getCourseName() + "△" + course.getStartWeek() + "-" + course.getEndWeek()
							+ zhou[course.getEveryWeek()] + "@" + course.getClasssroom());// 设置文本为课程名+“@”+教室
			}
		}
	}

	private int[] creatRandom() {
		int number = 20;// 控制随机数产生的范围
		List<Integer> arr = new ArrayList();
		for (int i = 0; i < 20; i++)
			arr.add(i);// 为ArrayList添加元素
		for (int j = 0; j < random.length; j++) {
			int index = (int) (Math.random() * number);// 产生一个随机数作为索引
			random[j] = arr.get(index);
			arr.remove(index);// 移除已经取过的元素
			number--;// 将随机数范围缩小1
		}
		return random;
	}
}