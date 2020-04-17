package service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.NoteDao;
import entity.Note;
import util.NoteResult;
import util.NoteUtil;

@Service("noteService")
public class NoteServiceImpl implements NoteService{

	@Resource(name="noteDao")
	private NoteDao dao;
	
	public NoteResult<List<Map>> findByBookId(String bookId) {
		NoteResult<List<Map>> result = new NoteResult<List<Map>>();
		List<Map> maps = dao.findByBookId(bookId);
		System.out.println(maps);
		if(maps.size() != 0) {
			result.setStatus(0);
			result.setMsg("获取笔记成功");
			result.setData(maps);
			return result;
		}
		result.setStatus(1);
		result.setMsg("无数据");
		return result;
	}

	public NoteResult<Map> findByBookIdDetail(String noteId) {
		NoteResult<Map> result = new NoteResult<Map>();
		Map map = dao.findByBookIdDetail(noteId);
		if(map != null) {
			result.setStatus(0);
			result.setMsg("获取数据成功");
			result.setData(map);		
			return result;
		}
		result.setStatus(1);
		result.setMsg("无数据");
		return result;
	}

	public NoteResult saveNote(String note_id, String note_title, String note_body) {
		NoteResult result = new NoteResult();
		Note note = new Note();
		note.setCn_note_id(note_id);
		note.setCn_note_title(note_title);
		note.setCn_note_body(note_body);
		note.setCn_note_last_modify_time(new Date().getTime());
		dao.saveNote(note);
		result.setStatus(0);
		result.setMsg("保存成功");
		return result;
	}

	public NoteResult saveNewNote(String note_book_id, String note_title, String note_body) {
		Note note = new Note();
		note.setCn_note_id(NoteUtil.createId());
		note.setCn_note_title(note_title);
		note.setCn_notebook_id(note_book_id);
		note.setCn_note_status_id("1");
		note.setCn_note_body(note_body);
		note.setCn_note_create_time(new Date().getTime());
		dao.saveNewNote(note);
		NoteResult result = new NoteResult();
		result.setStatus(0);
		result.setMsg("新建成功");
		return result;
	}

	public NoteResult deleteNote(String note_id) {
		dao.deleteNote(note_id);
		NoteResult result = new NoteResult();
		result.setStatus(0);
		result.setMsg("删除成功");
		return result;
	}

	@Transactional
	public void deleteNotes(String[] ids) {
		
		for(String id : ids) {
			int n = dao.deleteNote(id);
			System.out.println("删除的 id = " + id + "返回值 n = " + n);
		}
		
	}
	
	@Transactional
	public void deleteNote2(String...ids) {
		//String... 就是String[]
		for(String id : ids) {
			int n = dao.deleteNote2(id);
			System.out.println(n);
			if(n != 1) {
				//抛出异常触发，事务的回滚
				throw new RuntimeException("删除失败");
			}
		}
	}
	
}
