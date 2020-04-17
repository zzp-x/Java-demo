package test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.UserDao;
import entity.User;
import service.UserService;
import util.NoteResult;

public class TestCase {
	@Test
	public void test1() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("conf/spring-mvc.xml");
		UserDao dao = ac.getBean("userDao", UserDao.class);
		User user = dao.findByName("libai");
		System.out.println(user);
		
	}
	
	@Test
	public void test2() {
		String[] conf = {
				"conf/spring-mvc.xml",
				"conf/spring-transaction.xml"
		};
		ApplicationContext ac = new ClassPathXmlApplicationContext(conf);
		UserService service = ac.getBean("userService", UserService.class);
		System.out.println(service.getClass().getName());
		NoteResult<User> result = service.check_login("aaa", "123");
		System.out.println(result);
	}
}
