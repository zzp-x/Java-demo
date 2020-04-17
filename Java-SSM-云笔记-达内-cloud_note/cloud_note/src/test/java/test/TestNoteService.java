package test;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import service.NoteService;
import util.NoteResult;

public class TestNoteService {
	
	private NoteService service;
	
	@Before
	public void init() {
		String[] conf = {
				"conf/spring-mvc.xml",
				"conf/spring-aop.xml",
				"conf/spring-transaction.xml"
		};
		ApplicationContext ac = new ClassPathXmlApplicationContext(conf);
		service = ac.getBean("noteService", NoteService.class);
	}
	
	@Test
	public void test1() {
		NoteResult<List<Map>> result = service.findByBookId("10");
		System.out.println(result);
	}
	
	@Test
	public void test2() {
		NoteResult<Map> result = service.findByBookIdDetail("7");
		System.out.println(result);
	}
	
	@Test
	public void test3() {
		NoteResult result = service.saveNote("3", "学习java的第三天", "网络编程也是要学的");
		System.out.println(result);
	}
	
	@Test
	public void test4() {
		NoteResult result = service.saveNewNote("1", "真是麻烦", "python可是我的最爱啊");
		System.out.println(result);
	}
	
	//测试事务回滚
	@Test
	public void test5(){
		service.deleteNote2("2", "8","12");
	}
}
