package com.blog.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.project.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	
	@Query("select distinct p from Post p join p.tags t where t.name like %:parameter%")
	public List<Post> searchByTag(@Param("parameter")String parameter);
	
	@Query("select p from Post p where p.author like %:parameter%")
	public List<Post> searchByAuthor(@Param("parameter")String parameter);
	
	@Query("select p from Post p where p.title like %:parameter%")
	public List<Post> searchByTitle(@Param("parameter")String parameter);
}
