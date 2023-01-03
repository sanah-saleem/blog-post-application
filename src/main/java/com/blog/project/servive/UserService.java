package com.blog.project.servive;

import com.blog.project.entity.User;

public interface UserService {
	
	public void saveUser(User user);
	
	public User findByUsername(String name);
	
}
