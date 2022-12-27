package com.blog.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.project.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

	List<Comment> findAllByPostId(int postId);

}
