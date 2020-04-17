package service;

import entity.User;
import util.NoteResult;

public interface UserService {
	public NoteResult<User> check_login(String username, String password);
	
	public NoteResult save(User user);
}
