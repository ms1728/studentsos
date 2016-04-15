package com.studentsos.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.dominicsayers.isemail.dns.DNSLookupException;
import com.sina.sae.storage.SaeStorage;
import com.studentsos.entity.BookContent;
import com.studentsos.entity.Books;
import com.studentsos.entity.School;
import com.studentsos.entity.User;
import com.studentsos.entity.UserBook;
import com.studentsos.service.BooksService;
import com.studentsos.service.RegisterService;
import com.studentsos.service.SchoolService;
import com.studentsos.service.UserService;
import com.studentsosweb.CheckEmailObj;
import com.studentsosweb.Utils;

public class OnlyServlet extends HttpServlet {
	UserService userService = new UserService();
	BooksService booksService = new BooksService();
	RegisterService registerService = new RegisterService();
	SchoolService schoolService = new SchoolService();
	private static final long serialVersionUID = 1L;
	public static final String DOMAIN = "upload";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		resp.setContentType("multipart/form-data");
		String to = req.getParameter("to");
		if ("login".equals(to))
			login(req, resp);
		else if ("face".equals(to))
			faceimage(req, resp);
		else if ("updateUser".equals(to))
			updateUser(req, resp);
		else if ("books".equals(to))
			loadBooks(req, resp);
		else if ("userbook".equals(to))
			userBook(req, resp);
		else if ("register".equals(to))
			register(req, resp);
		else if ("searchbook".equals(to))
			searchBook(req, resp);
		else if ("addbook".equals(to))
			addUserBook(req, resp);
		else if ("deleteubook".equals(to))
			deleteuBook(req, resp);
		else if ("book_content".equals(to))
			bookContent(req, resp);
		else if ("findpassword".equals(to))
			findPassword(req, resp);
		else if ("schoolmajor".equals(to))
			schoolmajor(req, resp);
		else if ("schoolinfo".equals(to))
			schoolinfo(req, resp);
		else if ("schoolmajortwo".equals(to))
			schoolmajortwo(req, resp);
	}

	void schoolmajortwo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		int majorDetailID = Integer.valueOf(req.getParameter("majorDetailID"));
		List<School> school = schoolService.schoolMajorTwo(majorDetailID);
		sendObject(resp, school);
	}


	void schoolmajor(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String schoolname = req.getParameter("school");
		List<School> school = schoolService.schoolMajor(schoolname);
		sendObject(resp, school);
	}

	void schoolinfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String code = req.getParameter("code");
		String eduSchoolYear = req.getParameter("eduSchoolYear");
		int classNum = Integer.valueOf(req.getParameter("classnum"));
		int majorDetailID = Integer.valueOf(req.getParameter("majorDetailID"));
		registerService
				.updateUser(code, classNum, majorDetailID, eduSchoolYear);
	}

	void bookContent(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String bookid = req.getParameter("bookid");
		List<BookContent> bookContent = booksService.searchContent(Integer
				.parseInt(bookid));
		sendObject(resp, bookContent);
	}

	void deleteuBook(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String id = req.getParameter("id");
		userService.deleteubook(Integer.parseInt(id));
	}

	void addUserBook(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// TODO Auto-generated method stub
		String code = req.getParameter("code");
		String bookid = req.getParameter("bookid");
		int count = userService.addUserBook(code, Integer.parseInt(bookid));
		sendObject(resp, count);
	}

	void searchBook(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String key = req.getParameter("key");
		List<Books> searchbook = booksService.searchBook(key);
		sendObject(resp, searchbook);
	}

	void register(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String code = req.getParameter("code");
		String nicheng = req.getParameter("nicheng");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		/*
		 * try { if(!CheckEmailObj.checkEmail(email)) sendObject(resp, 1); else{
		 */
		int count = registerService.register(code, nicheng, password, email);
		sendObject(resp, count);
		/*
		 * } } catch (DNSLookupException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	void findPassword(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String code = req.getParameter("code");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		int count = userService.findPassword(code, password, email);
		sendObject(resp, count);

	}

	void userBook(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String code = req.getParameter("code");
		List<UserBook> userbook = booksService.userBook(code);
		sendObject(resp, userbook);

	}

	void loadBooks(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String code = req.getParameter("code");
		List<Books> books = booksService.loadBooks(code);
		sendObject(resp, books);

	}

	void login(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String code = req.getParameter("code");
		String password = req.getParameter("password");
		User user = userService.login(code, password);
		sendObject(resp, user);

	}

	/*
	 * @SuppressWarnings("unchecked") void faceimage(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException {
	 * PrintWriter writer = response.getWriter(); String
	 * contentType=request.getContentType();
	 * if(ServletFileUpload.isMultipartContent
	 * (request)){//������������Ƿ�Ϊmultipart������ try { FileItemFactory factory = new
	 * DiskFileItemFactory(); ServletFileUpload upload = new
	 * ServletFileUpload(factory); List<FileItem> items =
	 * upload.parseRequest(request);// �õ����е��ļ� if (items != null && items.size()
	 * > 0) { FileItem item = items.get(0);// �������һ�� String value =
	 * item.getName(); // ȡ�����һ����б�ܡ� int start = value.lastIndexOf("\\"); //
	 * ��ȡ�ϴ��ļ��� �ַ������֡�+1��ȥ����б�ܡ� String filename = value.substring(start + 1);
	 * SaeStorage storage = new SaeStorage(); //ע������ʹ�õ���getFieldName
	 * �������ļ�����ʵ���ƿ��ǵ��ļ����п��ܰ���·�� boolean flag = storage.write(DOMAIN,filename,
	 * item.get());
	 * writer.write(Utils.toData(null,storage.getErrno(),storage.getErrno
	 * ()==0?"success":storage.getErrmsg())); } } catch (FileUploadException e)
	 * {
	 * writer.write(Utils.toData(null,1002,"parse Request file Field"+e.getMessage
	 * ())); } }else{
	 * writer.write(Utils.toData(null,1002,"bad parameter")+contentType
	 * );//���������Ϲ淶����ֵ } writer.close(); }
	 */

	@SuppressWarnings("unchecked")
	void faceimage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		// ��ô����ļ���Ŀ������
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// ��ȡ�ļ��ϴ���Ҫ�����·����upload�ļ�������ڡ�
		String pathroot = request.getSession().getServletContext().getRealPath(
				"/");
		String path[] = pathroot.split("student");
		File file = new File(path[0] + "upload");
		// ����ļ��в������򴴽�
		if (!file.exists() && !file.isDirectory())
			file.mkdir();
		// ������ʱ����ļ��Ĵ洢�ң�����洢�ҿ��Ժ����մ洢�ļ����ļ��в�ͬ����Ϊ���ļ��ܴ�Ļ���ռ�ù����ڴ��������ô洢�ҡ�
		factory.setRepository(file);
		// ���û���Ĵ�С�����ϴ��ļ���������������ʱ���ͷŵ���ʱ�洢�ҡ�
		factory.setSizeThreshold(1024 * 1024);
		// �ϴ��������ࣨ��ˮƽAPI�ϴ�������
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// ���� parseRequest��request������ ����ϴ��ļ� FileItem �ļ���list ��ʵ�ֶ��ļ��ϴ���
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			for (FileItem item : list) {
				// ��ȡ���������֡�
				String name = item.getFieldName();
				// �����ȡ�ı���Ϣ����ͨ���ı���Ϣ����ͨ��ҳ�����ʽ���������ַ�����
				if (item.isFormField()) {
					// ��ȡ�û�����������ַ�����
					String value = item.getString();
					request.setAttribute(name, value);
				}
				// ���������ǷǼ��ַ���������ͼƬ����Ƶ����Ƶ�ȶ������ļ���
				else {
					// ��ȡ·����
					String filename = item.getName();
					String code[] = filename.split("_faceImage");
					request.setAttribute(name, filename);
					// �յ�д�����յ��ļ��С�
					OutputStream out = new FileOutputStream(new File(path[0]
							+ "upload", filename));
					InputStream in = item.getInputStream();

					int length = 0;
					byte[] buf = new byte[1024];
					System.out.println("��ȡ�ļ�����������:" + item.getSize());

					while ((length = in.read(buf)) != -1) {
						out.write(buf, 0, length);
					}
					String olde[] = code[1].split(".jp");
					int count = Integer.parseInt(olde[0]);
					File f = new File(path[0] + "upload/" + code[0]
							+ "_faceImage" + Integer.toString(count - 1)
							+ ".jpg"); // ����Ҫɾ�����ļ�λ��
					if (f.exists())
						f.delete();
					userService.updateFace(filename, code[0]);
					System.out.println(filename + "      " + code[0]);
					in.close();
					out.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void updateUser(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// TODO Auto-generated method stub
		InputStream is = req.getInputStream();
		ObjectInputStream ois = new ObjectInputStream(is);
		try {
			User user = (User) ois.readObject();
			userService.updateUser(user);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		ois.close();
		is.close();
	}

	/**
	 * ��HttpServletResponse��д������
	 * 
	 * @param req
	 *            HttpServletResponse
	 * @param object
	 *            д�������
	 * @throws IOException
	 */
	void sendObject(HttpServletResponse resp, Object object) throws IOException {
		// TODO Auto-generated method stub
		OutputStream os = resp.getOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(object);
		oos.flush();
		oos.close();
	}
}
