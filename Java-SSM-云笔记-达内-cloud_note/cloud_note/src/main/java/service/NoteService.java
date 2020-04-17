package service;

import java.util.List;
import java.util.Map;

import util.NoteResult;

public interface NoteService {
	public NoteResult<List<Map>> findByBookId(String bookId); 
	
	public NoteResult<Map> findByBookIdDetail(String noteId);
	
	public NoteResult saveNote(String note_id, String note_title, String note_body);
	
	public NoteResult saveNewNote(String note_book_id, String note_title, String note_body);
	
	public NoteResult deleteNote(String note_id);
	
	public void deleteNotes(String[] ids);
	
	public void deleteNote2(String...ids);
}
