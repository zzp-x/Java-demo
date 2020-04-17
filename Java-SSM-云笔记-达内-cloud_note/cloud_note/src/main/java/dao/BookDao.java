package dao;

import java.util.List;

import entity.Book;

public interface BookDao {
	public List<Book> findByUserId(String userId);
}
