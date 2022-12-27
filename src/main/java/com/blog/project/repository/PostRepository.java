package com.blog.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.blog.project.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
}
