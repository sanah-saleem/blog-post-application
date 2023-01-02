package com.blog.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.project.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	User findByUsername(String username);
	
}
