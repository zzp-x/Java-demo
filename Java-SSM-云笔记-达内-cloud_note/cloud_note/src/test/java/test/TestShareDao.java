package test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.ShareDao;
import entity.Share;
import util.NoteUtil;

public class TestShareDao {
	
	private ShareDao dao;
	
	@Before
	public void init() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("conf/spring-mvc.xml");
		dao = ac.getBean("shareDao", ShareDao.class);
	}
	
	@Test
	public void test1() {
		Share share = new Share();
		share.setCn_note_id("2");
		share.setCn_share_id(NoteUtil.createId());
		share.setCn_share_title("∑÷œÌ");
		share.setCn_share_body("ƒ⁄»›");
		dao.insert(share);
	}
	
	@Test
	public void test2() {
//		List<Share> shares = dao.search("%¡À%");
//		System.out.println(shares);
	}
	
	@Test
	public void test3() {
//		AAA aaa = new AAA();
//		aaa.setValue("%%");
//		aaa.setBegin(0);
//		List<Share> shares = dao.search(aaa);
//		System.out.println(shares);
	}
}
