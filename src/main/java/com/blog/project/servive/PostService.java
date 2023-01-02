package com.blog.project.servive;

import java.text.ParseException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.blog.project.entity.Post;

public interface PostService {
	
	public void addPost(Post post, String tagName);
	
	public Post getPost(int id);
	
	public List<Post> getAllPosts();
	
	public void deletePost(int id);
	
	public Page<Post> search(String parameter, List<String> authors, List<String> titles, String startDate, 
			String endDate ,int pageNo, int pageSize, String sortField, String sortDirection) throws ParseException;
	Page<Post> findPage(int pageNo, int pageSize, String sortField, String sortDirection);
	
	List<String> getAllAuthor();
	
	List<String> getAllTags();
	
}
