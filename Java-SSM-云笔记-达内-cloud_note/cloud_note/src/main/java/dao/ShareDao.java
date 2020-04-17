package dao;

import java.util.List;
import java.util.Map;

import entity.Share;

public interface ShareDao {
	
	public void insert(Share share);
	
	public List<Share> search(Map<String, Object> map);
}
