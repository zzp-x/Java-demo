package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import dao.NoteDao;
import dao.ShareDao;
import entity.Share;
import util.NoteResult;
import util.NoteUtil;

@Service("shareService")
public class ShareServiceImpl implements ShareService{

	@Resource(name="shareDao")
	private ShareDao dao;
	
	@Resource(name="noteDao")
	private NoteDao noteDao;
	
	public NoteResult insert(String cn_note_id) {
		NoteResult result = new NoteResult();
		Map map = noteDao.findByBookIdDetail(cn_note_id);
		
		System.out.println(cn_note_id + "-------" + map);
		String cn_share_title = (String) map.get("cn_note_title"); 
		String cn_share_body = (String) map.get("cn_note_body"); 
		
		Share share = new Share();
		share.setCn_share_id(NoteUtil.createId());
		share.setCn_note_id(cn_note_id);
		share.setCn_share_title(cn_share_title);
		share.setCn_share_body(cn_share_body);
		dao.insert(share);
		result.setStatus(0);
		result.setMsg("分享成功");
		return result;
	}

	public NoteResult search(String value, String page) {
		NoteResult<List<Share>> result = new NoteResult<List<Share>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", "%" + value + "%");
		Integer begin = (Integer.parseInt(page) - 1) * 3;
		map.put("begin", begin);
		List<Share> shares = dao.search(map);
		if(shares.size() == 0) {
			result.setStatus(1);
			result.setMsg("无数据");
			return result;
		}
		result.setStatus(0);
		result.setMsg("查询成功");
		result.setData(shares);
		return result;
	}

}
