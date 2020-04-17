package service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dao.BookDao;
import entity.Book;
import util.NoteResult;

@Service("bookService")
public class BookServiceImpl implements BookService{

	@Resource(name="bookDao")
	private BookDao dao;
	
	public NoteResult<List<Book>> findByUserId(String userId) {
		NoteResult<List<Book>> result = new NoteResult<List<Book>>(); 
		List<Book> books = dao.findByUserId(userId);
		result.setData(books);
		result.setStatus(0);
		result.setMsg("获取笔记成功");
		return result;
	}

}
