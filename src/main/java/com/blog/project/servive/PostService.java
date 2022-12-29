package com.blog.project.servive;

import java.util.List;

import org.springframework.data.domain.Page;

import com.blog.project.entity.Post;

public interface PostService {
	public void addPost(Post post, String tagName);
	public Post getPost(int id);
	public List<Post> getAllPosts();
	public void deletePost(int id);
	public List<Post> search(String parameter, String category);
	Page<Post> findPage(int pageNo, int pageSize, String sortField, String sortDirection);
}
