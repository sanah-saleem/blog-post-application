package com.blog.project.servive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.blog.project.entity.User;
import com.blog.project.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public void saveUser(User user) {
		if(!userRepository.findAll().contains(user)) {
			String password = user.getPassword();
			user.setPassword(this.bCryptPasswordEncoder.encode(password));
			userRepository.save(user);
		}
	}

	@Override
	public User findByUsername(String name) {
		return userRepository.findByUsername(name);
	}

}
