package dao;

import java.util.List;

import entity.Book;
import entity.User;

public interface RelationDao {
	//关联多个对象查询
	public User findUserAndBooks(String UserId);
	
	public User findUserAndBooks2(String UserId);
	
	//关联单个对象
	public List<Book> findBookAndUser();
	
	public List<Book> findBookAndUser2();
}
