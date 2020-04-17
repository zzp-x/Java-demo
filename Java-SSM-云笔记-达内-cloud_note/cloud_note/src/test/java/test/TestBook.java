package test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.BookDao;
import entity.Book;

public class TestBook {
	
	private ApplicationContext ac;
	@Before
	public void init() {
		ac = new ClassPathXmlApplicationContext("conf/spring-mvc.xml");
	}
	
	@Test
	public void test1() {
		BookDao dao = ac.getBean("bookDao", BookDao.class);
		List<Book> books = dao.findByUserId("bfd499d0b95547c6951b47d0adffda20");
		System.out.println(books);
	}
}
