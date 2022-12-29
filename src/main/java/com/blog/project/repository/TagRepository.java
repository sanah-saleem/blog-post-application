package com.blog.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.project.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer>{
	
	@Query("select t from Tag t where t.name = :tagName")
	public Tag findByName(@Param("tagName")String tagName);

	public List<Tag> findAllByPostsId(int postId);
}
