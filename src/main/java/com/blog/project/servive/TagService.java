package com.blog.project.servive;

import java.util.List;

import com.blog.project.entity.Tag;

public interface TagService {
	
	public List<Tag> getAllTags();
	
	public List<Tag> addTag(List<String> tagName);
	
	public void deleteTag(Tag tag);
	
	public boolean checkTag(String tagName);
	
	public String getAllTags(int postId);
	
}
