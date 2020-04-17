package test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import entity.Share;
import service.ShareService;
import util.NoteResult;

public class TestShareService {
	
	private ShareService service;
	
	@Before
	public void init() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("conf/spring-mvc.xml");
		service = ac.getBean("shareService", ShareService.class);
	}
	
	@Test
	public void test1() {
		NoteResult<List<Share>> result = service.search("a", "1");
		System.out.println(result);
	}
}
