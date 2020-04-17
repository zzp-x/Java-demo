package dao;

import java.util.List;
import java.util.Map;

import entity.Note;

public interface NoteDao {
	public List<Map> findByBookId(String notebook_id);
	
	public Map findByBookIdDetail(String note_id);
	
	public void saveNote(Note note);
	
	public void updateNoteByMap(Map<String, Object> map);
	
	public void saveNewNote(Note note);
	
	public int deleteNote(String note_id);
	
	/*
	 * map 中需要添加参数
	 * map = {ids:[id1,id2,id3....], status:2}
	 * ids 代表被删除笔记的ID列表
	 * status 代表被删除笔记的属性状态
	 * */
	public void deleteNotes(Map<String, Object> map);
	
	public int deleteNote2(String id);
}
