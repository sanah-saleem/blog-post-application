package com.blog.project.servive;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.blog.project.entity.Post;
import com.blog.project.entity.Tag;
import com.blog.project.repository.PostRepository;

@Service
public class PostServiceImplementation implements PostService {

	@Autowired
	PostRepository postRepository;
	
	@Autowired
	TagService tagService;
	
	@Override
	public void addPost(Post post, String tagName) {
//		if(post.getId() == 0) {
//			LocalDateTime now = LocalDateTime.now();
//	        Timestamp timestamp = Timestamp.valueOf(now);
//	        post.setPublishedAt(timestamp);
//	        post.setCreatedAt(timestamp);
//		}
		if(post.getContent().length()>=200) {
			post.setExcerpt(post.getContent().substring(0,200));
		}
		else {
			post.setExcerpt(post.getContent());
		}
		List<String> tagNames = Arrays.asList(tagName.split(","));
		List<Tag> tags = tagService.addTag(tagNames);
		post.setTags(tags);
		postRepository.save(post);
	}

	@Override
	public Post getPost(int id) {
		return postRepository.findById(id).get();
	}

	@Override
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	@Override
	public void deletePost(int id) {
		postRepository.delete(getPost(id));
	}

	@Override
	public List<Post> search(String category, String parameter) {
		if(category.equals("tag"))
			return postRepository.searchByTag(parameter);
		if(category.equals("title"))
			return postRepository.searchByTitle(parameter);
		if(category.equals("author"))
			return postRepository.searchByAuthor(parameter);
		return null;
	}

	@Override
	public Page<Post> findPage(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField)
				.ascending() : Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
		return this.postRepository.findAll(pageable);
	}
}
