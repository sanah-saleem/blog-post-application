package com.blog.project.servive;

import java.util.List;

import com.blog.project.entity.Post;

public interface PostService {
	public void addPost(Post post, int id, String tagName);
	public void updatePost();
	public Post getPost(int id);
	public List<Post> getAllPosts();
	public void deletePost(int id);
}
