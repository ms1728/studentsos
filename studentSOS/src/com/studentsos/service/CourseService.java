package com.studentsos.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.studentsos.entity.Course;


/**
 * Course���ҵ���߼�����
 * 
 * @author lizhangqu
 * @date 2015-2-1
 */
public class CourseService {
	private static volatile CourseService courseService;
	List<Course> courses=new ArrayList<Course>();;
	private CourseService(){}
	public static CourseService getCourseService() {
		if(courseService==null){
			synchronized (CourseService.class) {
				if(courseService==null)
					courseService=new CourseService();
			}
		}
		return courseService;
	}


	/**
	 * ��ѯ���пγ�
	 * 
	 * @return
	 */
	public List<Course> findAll() {
		return courses;
	}

	/**
	 * ������ҳ���ؽ�������γ̲�����
	 * 
	 * @param content
	 * @return
	 */
	public String parseCourse(String content) {
		StringBuilder result = new StringBuilder();
		Document doc = Jsoup.parse(content);
		
		Elements semesters = doc.select("option[selected=selected]");
		String[] years=semesters.get(0).text().split("-");
		int startYear=Integer.parseInt(years[0]);
		int endYear=Integer.parseInt(years[1]);
		int semester=Integer.parseInt(semesters.get(1).text());
		
		
		
		Elements elements = doc.select("table#Table1");
		Element element = elements.get(0).child(0);
		//�Ƴ�һЩ��������
		
		element.child(0).remove();
		element.child(0).remove();
		element.child(0).child(0).remove();
		element.child(4).child(0).remove();
		element.child(8).child(0).remove();
		int rowNum = element.childNodeSize();
		int[][] map = new int[12][7];
		for (int i = 0; i < rowNum - 1; i++) {
			Element row = element.child(i);
			int columnNum = row.childNodeSize() - 2;
			for (int j = 1; j < columnNum; j++) {
				Element column = row.child(j);
				int week = fillMap(column, map, i);
				//���map����ȡ�ܼ����ڼ������ڼ���
				//���ã��ֲ����ܻ�ȡ��Щ���ݵĸ�ʽ
				if (column.hasAttr("rowspan")) {
					try {
						System.out.println("��"+ week+ " ��"+ (i + 1)+ "��-��"+ (i + Integer.parseInt(column.attr("rowspan"))) + "��");
						splitCourse(column.html(), startYear,endYear,semester,week, i + 1,i + Integer.parseInt(column.attr("rowspan")));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return result.toString();
	}

	
	
	/**
	 * ���ݴ������Ŀγ̸�ʽת��Ϊ��Ӧ��ʵ���ಢ����
	 * @param sub
	 * @param startYear
	 * @param endYear
	 * @param semester
	 * @param week
	 * @param startSection
	 * @param endSection
	 * @return
	 */
	private Course storeCourseByResult(String sub,int startYear,int endYear,int semester, int week,
			int startSection, int endSection) {
		// �ܶ���1,2��{��4-16��} 		��,1,2,4,16,null
		// {��2-10��|3��/��} 			null,null,null,2,10,3��/��
		// �ܶ���1,2��{��4-16��|˫��} 	��,1,2,4,16,˫��
		// �ܶ���1��{��4-16��} 			��,1,null,4,16,null
		// �ܶ���1��{��4-16��|˫��} 		��,1,null,4,16,˫��
		// str��ʽ���ϣ�����ֻ�Ǽ򵥿���ÿ���ζ�ֻ�����ڿΣ�ʵ���������ں��Ľڣ�ģʽ��Ҫ�Ķ�������ƥ��ģʽ�������޸�

		String reg = "��?(.)?��?(\\d{1,2})?,?(\\d{1,2})?��?\\{��(\\d{1,2})-(\\d{1,2})��\\|?((.*��))?\\}";

		String splitPattern = "<br />";
		String[] temp = sub.split(splitPattern);
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(temp[2]);
		matcher.matches();
		Course course = new Course();
		//�γ̿�ʼѧ��
		course.setStartYear(startYear);
		//�γ̽���ѧ��
		course.setEndYear(endYear);
		//�γ�ѧ��
		course.setSemester(semester);
		
		//�γ���
		course.setCourseName(temp[0]);
		//�γ�ʱ�䣬�����ֶ�
		course.setCourseTime(temp[2]);
		//��ʦ
		course.setTeacher(temp[3]);
		
		try {
			// �������Խ�磬��û�н���
			course.setClasssroom(temp[4]);
		} catch (ArrayIndexOutOfBoundsException e) {
			course.setClasssroom("�޽���");
		}
		//�ܼ�������Ϊ�գ���ʱʹ�ô�������ֵ
		if (null != matcher.group(1)){
			course.setDayOfWeek(getDayOfWeek(matcher.group(1)));
		}else{
			course.setDayOfWeek(getDayOfWeek(week+""));
		}
		//�γ̿�ʼ����������Ϊ�գ���ʱʹ�ô�������ֵ
		if (null != matcher.group(2)){
			course.setStartSection(Integer.parseInt(matcher.group(2)));
		}else{
			course.setStartSection(startSection);
		}
		
		//�γ̽���ʱ�Ľ���������Ϊ�գ���ʱʹ�ô�������ֵ
		if (null != matcher.group(3)){
			course.setEndSection(Integer.parseInt(matcher.group(3)));
		}else{
			course.setEndSection(endSection);
		}
		
		//��ʼ��
		course.setStartWeek(Integer.parseInt(matcher.group(4)));
		//������
		course.setEndWeek(Integer.parseInt(matcher.group(5)));
		//��˫��
		String t = matcher.group(6);
		setEveryWeekByChinese(t, course);

		courses.add(course);
		return course;
	}
	

	
	
	/**
	 * ��ȡ�γ̸�ʽ�����ܰ�����ڿ�
	 * @param str
	 * @param startYear
	 * @param endYear
	 * @param semester
	 * @param week
	 * @param startSection
	 * @param endSection
	 * @return
	 */
	private int splitCourse(String str, int startYear,int endYear,int semester,int week, int startSection,
			int endSection) {
		String pattern = "<br /><br />";

		String[] split = str.split(pattern);
		if (split.length > 1) {// �������һ�ڿ�
			for (int i = 0; i < split.length; i++) {
				if (!(split[i].startsWith("<br />") && split[i].endsWith("<br />"))) {
					storeCourseByResult(split[i], startYear,endYear,semester,week, startSection,
							endSection);// ���浥�ڿ�
				} else {
					// <br />�Ļ���������γ̣�<br />���յ�10��{��17-17��}<br />���ΰ<br />
					// ���ϸ�ʽ�����⴦�����ָ�ʽ��û�н�ʦ������²��������������պ�<br />���ɴ���
					int brLength = "<br />".length();
					String substring = split[i].substring(brLength,
							split[i].length() - brLength);
					storeCourseByResult(substring, startYear,endYear,semester,week, startSection,
							endSection);// ���浥�ڿ�
				}
			}
			return split.length;
		} else {
			storeCourseByResult(str, startYear,endYear,semester,week, startSection, endSection);// ����
			return 1;
		}
	}
	
	/**
	 * ���map����ȡ�ܼ����ڼ��ڿ����ڼ��ڿ�
	 * @param childColumn
	 * @param map
	 * @param i
	 * @return �ܼ�
	 */
	public static int fillMap(Element childColumn, int map[][], int i) {
		//�����������������������֮���Ƿ����ܼ���Ҳ�������з��ֵģ����Ǿ�������ȡ�ˣ�������˫�ر��ϣ���Ϊ��Щ�����޷���������ƥ����ܼ��ڼ��ڵ��ڼ���
		boolean hasAttr = childColumn.hasAttr("rowspan");
		int week = 0;
		if (hasAttr) {
			for (int t = 0; t < map[0].length; t++) {
				if (map[i][t] == 0) {
					int r = Integer.parseInt(childColumn.attr("rowspan"));
					for (int l = 0; l < r; l++) {
						map[i + l][t] = 1;
					}
					week = t + 1;
					break;
				}
			}

		} else {
			if (childColumn.childNodes().size() > 1) {
				childColumn.attr("rowspan", "1");
			}
			for (int t = 0; t < map[0].length; t++) {
				if (map[i][t] == 0) {
					map[i][t] = 1;
					week = t + 1;
					break;
				}
			}
		}
		return week;
	}
	/**
	 * ���õ�˫��
	 * @param week
	 * @param course
	 */
	public void setEveryWeekByChinese(String week, Course course) {
		// 1�����ܣ�2����˫��
		if (week != null) {
			if (week.equals("����"))
				course.setEveryWeek(1);
			else if (week.equals("˫��"))
				course.setEveryWeek(2);
		}
		// Ĭ��ֵΪ0������ÿ��
	}
	
	/**������������һ�����������ģ��壬�����գ�ת��Ϊ��Ӧ�İ���������
	 * @param day 
	 * @return int
	 */
	public int getDayOfWeek(String day) {
		if (day.equals("һ"))
			return 1;
		else if (day.equals("��"))
			return 2;
		else if (day.equals("��"))
			return 3;
		else if (day.equals("��"))
			return 4;
		else if (day.equals("��"))
			return 5;
		else if (day.equals("��"))
			return 6;
		else if (day.equals("��"))
			return 7;
		else
			return 0;
	}
}
