package test;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.NoteDao;
import entity.Note;
import util.NoteUtil;

public class TestNoteDao {
	private NoteDao dao;
	
	@Before
	public void init() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("conf/spring-mvc.xml");
		dao = ac.getBean("noteDao", NoteDao.class);
		System.out.println("test " + dao);
	}
	
	@Test
	public void test1() {
		List<Map> maps = dao.findByBookId("2");
		System.out.println(maps);
	}
	
	@Test
	public void test2() {
		Map map = dao.findByBookIdDetail("1");
		System.out.println(map);
	}
	
	@Test
	public void test3() {
		Note note = new Note();
		note.setCn_note_id("1");
		note.setCn_note_title("学习java的第二天");
		note.setCn_note_body("java真是个好东西啊");
		note.setCn_note_last_modify_time(new Date().getTime());
		dao.saveNote(note);
	}
	
	@Test
	public void test4() {
		Note note = new Note();
		note.setCn_note_id(NoteUtil.createId());
		note.setCn_note_title("测试输入啊");
		note.setCn_notebook_id("1");
		note.setCn_note_status_id("1");
		note.setCn_note_body("这个是内容框");
		note.setCn_note_create_time(new Date().getTime());
		dao.saveNewNote(note);
	}
	
	@Test
	public void test5() {
		dao.deleteNote("a4081716de96434581009a02dda1b80e");
	}
	
	@Test
	public void test6() {
		Map map = new HashMap<String, Object>();
		map.put("title", "java");
		map.put("id", "cb7920bf221c4105aa292428af0eba3d");	
		dao.updateNoteByMap(map);
	}
	
	//测试批量删除
	@Test
	public void test7() {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] ids= {"e061ce1b196245d59c1d4197f72dbf5b", "3"};
		map.put("ids", ids);
		map.put("status", 2);
		dao.deleteNotes(map);
	}
}
