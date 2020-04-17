package test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.EmpDao;
import entity.Emp;

public class TestEmpDao {
	
	private EmpDao dao;
	
	@Before
	public void init() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("conf/spring-mvc.xml");
		dao = ac.getBean("empDao", EmpDao.class);
	}
	
	@Test
	public void test1() {
		Emp emp = new Emp();
		emp.setAge(22);
		emp.setName("索隆");
		System.out.println("插入之前：" + emp);
		dao.insert(emp);
		System.out.println("插入之后：" + emp);
	}
	
}
