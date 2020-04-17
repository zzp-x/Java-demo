package controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import entity.Share;
import service.ShareService;
import util.NoteResult;

@RequestMapping("/share")
@Controller
public class ShareContoller {
	
	@Resource(name="shareService")
	private ShareService service;
	
	@ResponseBody
	@RequestMapping("/insert.do")
	public NoteResult insert(HttpServletRequest request) {
		String cn_note_id = request.getParameter("note_id");

		NoteResult result = service.insert(cn_note_id);
		return result;
	}

	@ResponseBody
	@RequestMapping("/search.do")
	public NoteResult<List<Share>> search(HttpServletRequest request){
		String value = request.getParameter("value");
		String page = request.getParameter("page");
		NoteResult<List<Share>> result = service.search(value, page);
		return result;
	}

}
