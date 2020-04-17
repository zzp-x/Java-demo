package controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import entity.User;
import net.sf.json.JSONObject;
import service.UserService;
import util.NoteResult;

@Controller
@RequestMapping("/user")
public class UserLoginController {
	
	@Resource(name = "userService")
	private UserService service;
	
	@RequestMapping("/login.do")
	@ResponseBody
	public NoteResult<User> login(HttpServletRequest request, HttpServletResponse response) {
//		System.out.println(request.getServletPath());
		String username = request.getParameter("username");
		String password = request.getParameter("password");
//		System.out.println("username = " + username + ", password = " + password);
		NoteResult<User> result = service.check_login(username, password);
		if(result.getStatus() == 0) {
			String user = JSONObject.fromObject(result.getData()).toString();
//			System.out.println(user);
			Cookie cookie = new Cookie("user", user);
			cookie.setMaxAge(60*60);
			cookie.setPath("/");
			response.addCookie(cookie);
		}
//		Integer.parseInt("abc");
		return result;
	}
	
	@RequestMapping("/index.do")
	public ModelAndView index(HttpServletRequest reqeust, HttpServletResponse response) {
		Cookie[] cookies  =reqeust.getCookies();
//		for(Cookie cookie : cookies) {
//			System.out.println(cookie.getName() + " = " + cookie.getValue());
//		}
		response.setContentType("text/html;charset=utf-8;");
		return new ModelAndView("index");
	}
	
	@RequestMapping("/logout.do")
	@ResponseBody
	public NoteResult logout(HttpServletRequest request, HttpServletResponse response) {
		NoteResult result = new NoteResult();
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			result.setStatus(3);
			result.setMsg("未登陆，即将跳转到登陆页面");
		} else {
			boolean type = false;
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("user")) {
					type = true;
					System.out.println("logout user = " + cookie.getValue());
					cookie.setValue(null);
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
					result.setStatus(3);
					result.setMsg("退出成功，即将跳转到登陆页面");
					break;
				}
			}
			if(!type) {
				result.setStatus(3);
				result.setMsg("未登陆，即将跳转到登陆页面");
			}
		}
		return result;
	}
	
	@RequestMapping("/register.do")
	@ResponseBody
	public NoteResult register(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
//		System.out.println("username = " + username + ", password = " + password +", nickname = " + nickname);
		User user = new User();
		user.setCn_user_name(username);
		user.setCn_user_nick(nickname);
		user.setCn_user_password(password);
		NoteResult result = service.save(user);
		return result;
	}
}
