package test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.UserDao;
import entity.User;
import service.UserService;
import util.NoteResult;
import util.NoteUtil;

public class TestUserService {
	private UserService service;
	private ApplicationContext ac;
	@Before
	public void init() {
		ac = new ClassPathXmlApplicationContext("conf/spring-mvc.xml");
		service = ac.getBean("userService", UserService.class);
	}
	
	@Test
	public void test1() {
		NoteResult<User> result = service.check_login("sdfs", "dsf");
		System.out.println(result);
	}
	
	@Test
	public void test2() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println(NoteUtil.EncoderByMd5("123456"));
	}
	
	@Test
	public void test3() {
		UserDao dao = ac.getBean("userDao", UserDao.class);
		User user = new User();
		user.setCn_user_name("aa");
		user.setCn_user_password("123");
		user.setCn_user_token("token");
		user.setCn_user_nick("nick");
		
		dao.save(user);
	}
	
	@Test
	public void test4() {
		String id = NoteUtil.createId();
		System.out.println(id);
	}
	
	@Test
	public void test5() {
		User user = new User();
		user.setCn_user_name("libai2");
		user.setCn_user_password("123446");
		user.setCn_user_nick("nick");
		
		NoteResult result = service.save(user);
		System.out.println(result);
	}
	
	//用例-4：预期结果：注册成功
	@Test
	public void test6() {
		User user = new User();
		user.setCn_user_name("苍老师2");
		user.setCn_user_nick("cang");
		user.setCn_user_password("200");
		NoteResult result = service.save(user);
		System.out.println(result.getStatus());
		System.out.println(result.getMsg());
		
	}
	
}
