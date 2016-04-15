package com.studentsosweb;



import com.dominicsayers.isemail.*;
import com.dominicsayers.isemail.dns.DNSLookupException;

public class CheckEmailObj {
	public static boolean checkEmail(String email) throws DNSLookupException {
		  if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
		   return false;
		  }
		  IsEMailResult result = IsEMail.is_email_verbose(email, true);
		  switch (result.getState()) {
		  case OK:
		   return true;
		  default:
		   return false;
		  }
		 }
	/*public static boolean checkEmail(String email) {
		System.out.println(email);
		if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
			System.err.println("Format error");
			return false;
		}

		String log = "";
		String host = "";
		String hostName = email.split("@")[1];
		Record[] result = null;
		SMTPClient client = new SMTPClient();

		try {
			  
			// 查找MX记录
			Lookup lookup = new Lookup(hostName, Type.MX);
			lookup.run();
			if (lookup.getResult() != Lookup.SUCCESSFUL) {
				log += "找不到MX记录\n";
				return false;
			} else {
				result = lookup.getAnswers();
			}

			// 连接到邮箱服务器
			for (int i = 0; i < result.length; i++) {
				host = result[i].getAdditionalName().toString();
				client.connect(host);
				if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
					client.disconnect();
					continue;
				} else {
					log += "MX record about " + hostName + " exists.\n";
					log += "Connection succeeded to " + host + "\n";
					break;
				}
			}
			log += client.getReplyString();

			// HELO cyou-inc.com
			client.login("qq.com");
			log += ">HELO qq.com\n";
			log += "=" + client.getReplyString();

			// MAIL FROM: <zhaojinglun@cyou-inc.com>
			client.setSender("172837016@qq.com");
			log += ">MAIL FROM: <172837016@qq.com>\n";
			log += "=" + client.getReplyString();

			// RCPT TO: <$email>
			client.addRecipient(email);
			log += ">RCPT TO: <" + email + ">\n";
			log += "=" + client.getReplyString();

			if (250 == client.getReplyCode()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				client.disconnect();
			} catch (IOException e) {
			}
			// 打印日志
			System.err.println(log);
		}
		return false;
	}*/

	/*public static void main(String[] args) {
		try {
			System.err.println("Outcome: "
					+ CheckEmailObj.checkEmail1("qq172837016@163.com"));
		} catch (DNSLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}