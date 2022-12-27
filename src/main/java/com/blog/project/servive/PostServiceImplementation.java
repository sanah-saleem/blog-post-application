package com.blog.project.servive;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void addPost(Post post, int id, String tagName) {
		if(id!=0) {
		post.setId(id);
		}
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
	public void updatePost() {
		// TODO Auto-generated method stub

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
}
