package test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.RelationDao;
import entity.Book;
import entity.User;

public class TestRelationDao {
	
	private RelationDao dao;
	
	@Before
	public void init() {
		String[] conf = {
				"conf/spring-mvc.xml",
				"conf/spring-transaction.xml",
		};
		ApplicationContext ac = new ClassPathXmlApplicationContext(conf);
		dao = ac.getBean("relationDao", RelationDao.class);
	}
	
	//测试关联多个对象
	@Test
	public void test1() {
		User user = dao.findUserAndBooks("bfd499d0b95547c6951b47d0adffda20");
		System.out.println(user);
	}
	
	//测试关联多个对象
	@Test
	public void test2() {
		User user = dao.findUserAndBooks2("bfd499d0b95547c6951b47d0adffda20");
		System.out.println(user);
	}
	
	//测试关联单个对象，一条语句
	@Test
	public void test3() {
		List<Book> books = dao.findBookAndUser();
		System.out.println(books);
	}
	
	//测试关联单个对象，两条语句
	@Test
	public void test4() {
		List<Book> books = dao.findBookAndUser2();
		System.out.println(books);
	}
}
