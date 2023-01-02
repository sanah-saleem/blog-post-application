package com.blog.project.servive;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
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
	public Page<Post> search(String parameter, List<String> authors, List<String> tags, String startDateString, 
			String endDateString, int pageNo, int pageSize, String sortField, String sortDirection) throws ParseException  	 {
		
		if(!(startDateString==null||endDateString==null)) {
			if(startDateString.equals(""))
				startDateString=null;
			if(endDateString.equals(""))
				endDateString=null;
			}
		if(tags!=null)
		tags=tags.isEmpty()?null:tags;
		if(authors!=null)
		authors=authors.isEmpty()?null:authors;
		
		Date startDate=null;
		Date endDate=null;
		
		System.out.println("3");
		
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField)
				.ascending() : Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
		
		if(!(startDateString==null || endDateString==null)) {
			String[] dateStart = startDateString.split("-");
			startDate = convertLocalDateTimeToDateUsingTimestamp(LocalDateTime.of(
					Integer.parseInt(dateStart[0]),Integer.parseInt(dateStart[1]),Integer.parseInt(dateStart[2]),0,0,0))	;
			String[] dateEnd = endDateString.split("-");
			endDate = convertLocalDateTimeToDateUsingTimestamp(LocalDateTime.of(
					Integer.parseInt(dateEnd[0]),Integer.parseInt(dateEnd[1]),Integer.parseInt(dateEnd[2]),0,0,0))	;
		}
		if(parameter==null) {
			if(startDate==null && endDate==null) {
				if(authors==null && tags==null) {
					return postRepository.findAll(pageable);
				}
				else if(authors==null) {
					return postRepository.tagFilter(tags, pageable);
				}
				else if(tags==null) {
					return postRepository.authorFilter(authors, pageable);
				}
				else {
					return postRepository.tagAuthorFilter(authors, tags, pageable);
				}
			}
			else {
				if(authors==null && tags==null) {
					return postRepository.dateFilter(startDate, endDate, pageable);
				}
				else if(authors==null) {
					return postRepository.dateTagFilter(startDate, endDate, tags, pageable);
				}
				else if(tags==null) {
					return postRepository.dateAuthorFilter(startDate, endDate, authors, pageable);
				}
				else {
					return postRepository.dateTagAndAuthorFilter(startDate, endDate, authors, tags, pageable);
				}
			}
		}
		else {
			if(startDate==null && endDate==null) {
				if(authors==null && tags==null) {
					System.out.println("only search");
					return postRepository.search(parameter, pageable);
				}
				else if(authors==null) {
					System.out.println("search and tags");
					return postRepository.searchWithTagFilter(parameter, tags, pageable);
				}
				else if(tags==null) {
					System.out.println("search and author");
					return postRepository.searchWithAuthorFilter(parameter, authors, pageable);
				}
				else {
					System.out.println("search author and tags");
					return postRepository.searchWithTagAndAuthorFilter(parameter, tags, authors, pageable);
				}
			}
			else {
				if(authors==null && tags==null) {
					System.out.println("only search");
					return postRepository.searchwithDateFilter(parameter, startDate, endDate, pageable);
				}
				else if(authors==null) {
					System.out.println("search and tags");
					return postRepository.searchWithDateTagFilter(parameter, startDate, endDate, tags, pageable);
				}
				else if(tags==null) {
					System.out.println("search and author");
					return postRepository.searchWithDateAuthorFilter(parameter, startDate, endDate, authors, pageable);
				}
				else {
					System.out.println("search author and tags");
					return postRepository.searchWithDateTagAndAuthorFilter(parameter, startDate, endDate, tags, authors, pageable);
				}
			}
		}
	}

	@Override
	public Page<Post> findPage(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField)
				.ascending() : Sort.by(sortField).descending();
		Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
		return this.postRepository.findAll(pageable);
	}
	@Override
	public List<String> getAllAuthor() {
		return postRepository.findAllAuthor();
	}
	@Override
	public List<String> getAllTags() {
		return postRepository.findAllTag();
	}
	public Date convertLocalDateTimeToDateUsingTimestamp(LocalDateTime dateToConvert) {
		return java.sql.Timestamp.valueOf(dateToConvert);
	}
}
