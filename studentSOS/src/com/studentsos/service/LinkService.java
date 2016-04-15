package com.studentsos.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.studentsos.entity.LinkNode;


/**
 * LinNode表的业务逻辑处理
 * @author lizhangqu
 * @date 2015-2-1
 */
public class LinkService {
	private static volatile LinkService linkService;
	List<LinkNode> linknode;
	private LinkService(){}
	public static LinkService getLinkService() {
		if(linkService==null){
			synchronized (LinkService.class) {
				if(linkService==null)
					linkService=new LinkService();
			}
		}
		
		return linkService;
	}
	/**
	 * 查询所有链接
	 * 
	 * @return
	 */
	public List<LinkNode> findAll() {
		return linknode;
	}
	public String parseMenu(String content) {
		LinkNode linkNode =null;
		linknode=new ArrayList<LinkNode>();
		StringBuilder result = new StringBuilder();
		Document doc = Jsoup.parse(content);
		Elements elements = doc.select("ul.nav a[target=zhuti]");
		for (Element element : elements) {
			result.append(element.html() + "\n" + element.attr("href") + "\n\n");
			linkNode= new LinkNode();
			linkNode.setTitle(element.text());
			linkNode.setLink(element.attr("href"));
			linknode.add(linkNode);
		}
		return result.toString();

	}
	public String isLogin(String content){
		Document doc = Jsoup.parse(content, "UTF-8");
		Elements elements = doc.select("span#xhxm");
		try{
			Element element=elements.get(0);
			return element.text();
		}catch(IndexOutOfBoundsException e){
			//e.printStackTrace();
		}
		return null;
	}
}
