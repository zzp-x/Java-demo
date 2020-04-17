package dao;

import entity.User;

public interface UserDao {
	public User findByName(String username);
	
	public void save(User user);
}
