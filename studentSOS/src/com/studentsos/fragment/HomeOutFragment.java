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
	String zhou[] = { "", "(��)", "(˫)" };
	private TextView mText;
	private List<String> color = new ArrayList();
	// �γ�ҳ���button���ã�6��5��
	private int[][] lessons = { { R.id.lesson11, R.id.lesson12, R.id.lesson13, R.id.lesson14, R.id.lesson15 },
			{ R.id.lesson21, R.id.lesson22, R.id.lesson23, R.id.lesson24, R.id.lesson25 },
			{ R.id.lesson31, R.id.lesson32, R.id.lesson33, R.id.lesson34, R.id.lesson35 },
			{ R.id.lesson41, R.id.lesson42, R.id.lesson43, R.id.lesson44, R.id.lesson45 },
			{ R.id.lesson51, R.id.lesson52, R.id.lesson53, R.id.lesson54, R.id.lesson55 },
			{ R.id.lesson61, R.id.lesson62, R.id.lesson63, R.id.lesson64, R.id.lesson65 }, };
	// ĳ�ڿεı���ͼ,���������ȡ
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
	 * ��ʼ����ͼ
	 */
	private void initView() {
		// �������߼����⣬ֻ�Ǽ򵥵���ʾ�������ݣ����ݲ���һ������ʾ����ȷλ��
		// �γ̿������ص�
		// �γ̿�����1�ڿεģ�2�ڿεģ�3�ڿεģ��������Ӧ�øĳ����Զ���View����ʾ������
		List<Course> courses = app.course;// ������ݿ��еĿγ�
		color.add(courses.get(0).getCourseName());
		Course course = null;
		// ѭ������
		for (int i = 0; i < courses.size(); i++) {
			course = courses.get(i);// �õ���ǰ�γ�
			int dayOfWeek = course.getDayOfWeek() - 1;// ת��Ϊlessons�����Ӧ���±�
			if (0<=dayOfWeek&& dayOfWeek< 6) {
				int section = course.getStartSection() / 2;// ת��Ϊlessons�����Ӧ���±�
				Button lesson = (Button) view.findViewById(lessons[section][dayOfWeek]);// ��øýڿε�button
				for (int j = 0; j < color.size(); j++) {
					if (course.getCourseName().equals(color.get(j))) {
						lesson.setBackgroundResource(bg[random[j]]);// ���ñ���
						j = color.size();
					} else if (j == color.size() - 1) {
						lesson.setBackgroundResource(bg[random[j + 1]]);// ���ñ���
						color.add(course.getCourseName());
						j = color.size();
					}

				}
				if ((i + 1) != courses.size() && course.getDayOfWeek() == courses.get(i + 1).getDayOfWeek()
						&& course.getStartSection() == courses.get(i + 1).getStartSection()) {
					lesson.setText(course.getCourseName() + "��" + course.getStartWeek() + "-" + course.getEndWeek()
							+ zhou[course.getEveryWeek()] + "@" + course.getClasssroom() + "\r\n"
							+ courses.get(i + 1).getCourseName() + "��" + courses.get(i + 1).getStartWeek() + "-"
							+ courses.get(i + 1).getEndWeek() + zhou[courses.get(i + 1).getEveryWeek()] + "@"
							+ courses.get(i + 1).getClasssroom());// �����ı�Ϊ�γ���+��@��+����
					i++;
				} else
					lesson.setText(course.getCourseName() + "��" + course.getStartWeek() + "-" + course.getEndWeek()
							+ zhou[course.getEveryWeek()] + "@" + course.getClasssroom());// �����ı�Ϊ�γ���+��@��+����
			}
		}
	}

	private int[] creatRandom() {
		int number = 20;// ��������������ķ�Χ
		List<Integer> arr = new ArrayList();
		for (int i = 0; i < 20; i++)
			arr.add(i);// ΪArrayList���Ԫ��
		for (int j = 0; j < random.length; j++) {
			int index = (int) (Math.random() * number);// ����һ���������Ϊ����
			random[j] = arr.get(index);
			arr.remove(index);// �Ƴ��Ѿ�ȡ����Ԫ��
			number--;// ���������Χ��С1
		}
		return random;
	}
}