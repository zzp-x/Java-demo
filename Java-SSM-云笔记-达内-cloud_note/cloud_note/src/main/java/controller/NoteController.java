package controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.NoteService;
import util.NoteResult;

@Controller
@RequestMapping("/note")
public class NoteController {
	@Resource(name="noteService")
	private NoteService service;
	
	@RequestMapping("/findByNoteId.do")
	@ResponseBody
	public NoteResult<List<Map>> findByNoteId(HttpServletRequest request) {
		String notebook_id = request.getParameter("notebook_id");
		NoteResult<List<Map>> result = service.findByBookId(notebook_id);
//		System.out.println(result);
		return result;
	}
	
	
	@RequestMapping("/findByNoteIdDetail.do")
	@ResponseBody
	public NoteResult<Map> findByNoteIdDetail(HttpServletRequest request) {
		String note_id = request.getParameter("note_id");
		NoteResult<Map> result = service.findByBookIdDetail(note_id);
//		System.out.println(result);
		return result;
	}
	
	@RequestMapping("/saveNote.do")
	@ResponseBody
	public NoteResult<Object> saveNote(HttpServletRequest request){
		String note_id = request.getParameter("note_id");
		String note_title = request.getParameter("note_title");
		String note_body = request.getParameter("note_body");
//		System.out.println(note_id + ", " + note_title + ", " + note_body);
		NoteResult<Object> result = service.saveNote(note_id, note_title, note_body);
		return result;
	}
	
	
	@RequestMapping("/saveNewNote.do")
	@ResponseBody
	public NoteResult<Object> saveNewNote(HttpServletRequest request){
		String notebook_id = request.getParameter("notebook_id");
		String note_title = request.getParameter("note_title");
		String note_body = request.getParameter("note_body");
//		System.out.println(notebook_id + ", " + note_title + ", " + note_body);
		NoteResult<Object> result = service.saveNewNote(notebook_id,note_title, note_body);
		return result;
	}
	
	@RequestMapping("/deleteNote.do")
	@ResponseBody
	public NoteResult<Object> deleteNote(HttpServletRequest request){
		String note_id = request.getParameter("note_id");
		System.out.println(note_id);
		NoteResult<Object> result = service.deleteNote(note_id);
		return result;
	}
	
}
