package com.blog.project.servive;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.project.entity.Tag;
import com.blog.project.repository.TagRepository;

@Service
public class TagServiceImplementation implements TagService {

	@Autowired
	TagRepository tagRepository;
	
	@Override
	public List<Tag> getAllTags() {
		return tagRepository.findAll();
	}

	@Override
	public List<Tag> addTag(List<String> tagNames) {
		List<Tag> tags = new ArrayList<Tag>();
		for(String tagName : tagNames) {
			Tag tag = new Tag();
			if(!checkTag(tagName)) {
				tag.setName(tagName);
//				tagRepository.save(tag);
				tags.add(tag);
			}
			else {
				tag = tagRepository.findByName(tagName);
				tags.add(tag);
			}
		}
		return tags;
	}
	
	@Override
	public void deleteTag(Tag tag) {
		
	}

	@Override
	public boolean checkTag(String tagName) {
		List<Tag> listTags = getAllTags();
		boolean check = false;
		for(Tag theTag : listTags) {
			if(theTag.getName().equals(tagName)) {
				check = true;
			}
		}
		System.out.println("check : " + check);
		return check;
	}

	@Override
	public String getAllTags(int postId) {
		List<Tag> listTags = tagRepository.findAllByPostsId(postId);
		System.out.println(listTags);
		String tagNames = "";
		for(Tag tag:listTags) {
		tagNames += tag.getName() + ",";
		}
		return tagNames.substring(0, tagNames.length()-1);
	}

}
