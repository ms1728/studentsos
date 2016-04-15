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
	 * (request)){//检查输入请求是否为multipart表单数据 try { FileItemFactory factory = new
	 * DiskFileItemFactory(); ServletFileUpload upload = new
	 * ServletFileUpload(factory); List<FileItem> items =
	 * upload.parseRequest(request);// 得到所有的文件 if (items != null && items.size()
	 * > 0) { FileItem item = items.get(0);// 仅处理第一个 String value =
	 * item.getName(); // 取到最后一个反斜杠。 int start = value.lastIndexOf("\\"); //
	 * 截取上传文件的 字符串名字。+1是去掉反斜杠。 String filename = value.substring(start + 1);
	 * SaeStorage storage = new SaeStorage(); //注意这里使用的是getFieldName
	 * 而不是文件的真实名称考虑到文件名中可能包含路径 boolean flag = storage.write(DOMAIN,filename,
	 * item.get());
	 * writer.write(Utils.toData(null,storage.getErrno(),storage.getErrno
	 * ()==0?"success":storage.getErrmsg())); } } catch (FileUploadException e)
	 * {
	 * writer.write(Utils.toData(null,1002,"parse Request file Field"+e.getMessage
	 * ())); } }else{
	 * writer.write(Utils.toData(null,1002,"bad parameter")+contentType
	 * );//参数不符合规范返回值 } writer.close(); }
	 */

	@SuppressWarnings("unchecked")
	void faceimage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		// 获得磁盘文件条目工厂。
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 获取文件上传需要保存的路径，upload文件夹需存在。
		String pathroot = request.getSession().getServletContext().getRealPath(
				"/");
		String path[] = pathroot.split("student");
		File file = new File(path[0] + "upload");
		// 如果文件夹不存在则创建
		if (!file.exists() && !file.isDirectory())
			file.mkdir();
		// 设置暂时存放文件的存储室，这个存储室可以和最终存储文件的文件夹不同。因为当文件很大的话会占用过多内存所以设置存储室。
		factory.setRepository(file);
		// 设置缓存的大小，当上传文件的容量超过缓存时，就放到暂时存储室。
		factory.setSizeThreshold(1024 * 1024);
		// 上传处理工具类（高水平API上传处理？）
		ServletFileUpload upload = new ServletFileUpload(factory);

		try {
			// 调用 parseRequest（request）方法 获得上传文件 FileItem 的集合list 可实现多文件上传。
			List<FileItem> list = (List<FileItem>) upload.parseRequest(request);
			for (FileItem item : list) {
				// 获取表单属性名字。
				String name = item.getFieldName();
				// 如果获取的表单信息是普通的文本信息。即通过页面表单形式传递来的字符串。
				if (item.isFormField()) {
					// 获取用户具体输入的字符串，
					String value = item.getString();
					request.setAttribute(name, value);
				}
				// 如果传入的是非简单字符串，而是图片，音频，视频等二进制文件。
				else {
					// 获取路径名
					String filename = item.getName();
					String code[] = filename.split("_faceImage");
					request.setAttribute(name, filename);
					// 收到写到接收的文件中。
					OutputStream out = new FileOutputStream(new File(path[0]
							+ "upload", filename));
					InputStream in = item.getInputStream();

					int length = 0;
					byte[] buf = new byte[1024];
					System.out.println("获取文件总量的容量:" + item.getSize());

					while ((length = in.read(buf)) != -1) {
						out.write(buf, 0, length);
					}
					String olde[] = code[1].split(".jp");
					int count = Integer.parseInt(olde[0]);
					File f = new File(path[0] + "upload/" + code[0]
							+ "_faceImage" + Integer.toString(count - 1)
							+ ".jpg"); // 输入要删除的文件位置
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
	 * 向HttpServletResponse中写入数据
	 * 
	 * @param req
	 *            HttpServletResponse
	 * @param object
	 *            写入的数据
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
