package service;

import java.util.List;

import entity.Book;
import util.NoteResult;

public interface BookService {
	public NoteResult<List<Book>> findByUserId(String userId);
}
