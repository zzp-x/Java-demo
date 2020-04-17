package controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import entity.Book;
import entity.User;
import net.sf.json.JSONObject;
import service.BookService;
import util.NoteResult;

@Controller
@RequestMapping("/book")
public class BookController {
	
	@Resource(name="bookService")
	private BookService service;
	
	@RequestMapping("/findAll.do")
	@ResponseBody
	public NoteResult<List<Book>> edit(HttpServletRequest reqeust) {
		Cookie[] cookies = reqeust.getCookies();
		NoteResult<List<Book>> result = new NoteResult<List<Book>>();
		boolean type = false;
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("user")) {
				type = true;
				JSONObject jsonObject = JSONObject.fromObject(cookie.getValue());
				User user = (User) jsonObject.toBean(jsonObject, User.class);
				String userId = user.getCn_user_id();
				result = service.findByUserId(userId);
				return result;
			}
		}
		if(!type) {
			result.setStatus(1);
			result.setMsg("Î´µÇÂ¼£¬¼´½«Ìø×ªµ½µÇÂ½Ò³Ãæ");
			return result;
		}
		return null;
	}
}
