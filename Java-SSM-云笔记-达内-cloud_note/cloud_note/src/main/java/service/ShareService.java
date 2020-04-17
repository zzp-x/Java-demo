package service;

import util.NoteResult;

public interface ShareService {
	public NoteResult insert(String cn_note_id);
	
	public NoteResult search(String value, String page);
}
