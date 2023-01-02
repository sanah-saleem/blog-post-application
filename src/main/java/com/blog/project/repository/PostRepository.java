package com.blog.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blog.project.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	
	@Query("select distinct p from Post p join p.tags t where (p.title like %:parameter%) or"
			+ " (p.author like %:parameter%) or (t.name like %:parameter%)")
	public Page<Post> search(@Param("parameter") String parameter, Pageable pageable);
	
	@Query("select distinct p from Post p where p.author IN :authors")
	public Page<Post> authorFilter(@Param("authors") List<String> authors, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where t.name IN :tagNames")
	public Page<Post> tagFilter(@Param("tagNames") List<String> tagNames, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where (t.name in :tagNames) and (p.author in :authors)")
	public Page<Post> tagAuthorFilter(@Param("authors") List<String> authors, 
			@Param("tagNames") List<String> tags, Pageable pageable);
	
//	start of date query
	
	@Query("select distinct p from Post p where p.publishedAt between :startDate and :endDate")
	public Page<Post> dateFilter(@Param("startDate")Date startDate, @Param("endDate")Date endDate, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where (p.publishedAt between :startDate and :endDate) and (t.name IN :tagNames)")
	public Page<Post> dateTagFilter(@Param("startDate")Date startDate, @Param("endDate") Date endDate, @Param("tagNames")List<String> tags, Pageable pageable);
	
	@Query("select distinct p from Post p where (p.publishedAt between :startDate and :endDate) and (p.author IN :authors)")
	public Page<Post> dateAuthorFilter(@Param("startDate")Date startDate, @Param("endDate") Date endDate,
			@Param("authors")List<String> authors, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where (p.publishedAt between :startDate and :endDate) and (t.name IN :tagNames) and (p.author IN :authors)")
	public Page<Post> dateTagAndAuthorFilter(@Param("startDate")Date startDate, @Param("endDate") Date endDate,
			@Param("authors")List<String> authors, @Param("tagNames")List<String> tags, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where ((p.title like %:parameter%) or (p.author like %:parameter%)" +
			" or (t.name like %:parameter%)) and (t.name IN :tagNames)")
	public Page<Post> searchWithTagFilter(@Param("parameter") String parameter, 
			@Param("tagNames") List<String> tags, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where ((p.title like %:parameter%) or (p.author like %:parameter%) or (t.name like %:parameter%))" +
			" AND (p.author IN :authors)")
	public Page<Post> searchWithAuthorFilter(@Param("parameter") String parameter, 
			@Param("authors") List<String> authors, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where ((p.title like %:parameter%) or (p.author like %:parameter%) or (t.name like %:parameter%))" +
			" AND (t.name IN :tagNames) AND (p.author IN :authors)")
	public Page<Post> searchWithTagAndAuthorFilter(@Param("parameter") String parameter, 
			@Param("tagNames") List<String> tags, @Param("authors") List<String> authors, 
			Pageable pageable);
	
//	start of date query
	@Query("select distinct p from Post p join p.tags t where ((p.title like %:parameter%) or (p.author like %:parameter%)" +
			" or (t.name like %:parameter%)) and (p.publishedAt between :startDate and :endDate)")
	Page<Post> searchwithDateFilter(String parameter, @Param("startDate")Date startDate, @Param("endDate") Date endDate, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where ((p.title like %:parameter%) or (p.author like %:parameter%)" +
			" or (t.name like %:parameter%)) and (p.publishedAt between :startDate and :endDate) and (t.name IN :tagNames)")
	Page<Post> searchWithDateTagFilter(String parameter, @Param("startDate")Date startDate, @Param("endDate") Date endDate, 
			@Param("tagNames")List<String> tags, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where ((p.title like %:parameter%) or (p.author like %:parameter%)" +
			" or (t.name like %:parameter%)) and (p.publishedAt between :startDate and :endDate) and (p.author IN :authors)")
	Page<Post> searchWithDateAuthorFilter(String parameter, @Param("startDate")Date startDate, @Param("endDate") Date endDate, 
			@Param("authors")List<String> authors, Pageable pageable);
	
	@Query("select distinct p from Post p join p.tags t where ((p.title like %:parameter%) or (p.author like %:parameter%)" +
			" or (t.name like %:parameter%)) and (p.publishedAt between :startDate and :endDate) and (t.name IN :tagNames) and (p.author IN :authors)")
	Page<Post> searchWithDateTagAndAuthorFilter(String parameter, @Param("startDate")Date startDate, @Param("endDate") Date endDate,
			@Param("tagNames")List<String> tags, @Param("authors")List<String> authors, Pageable pageable);

	@Query("select distinct p.author from Post p")
	public List<String> findAllAuthor();
	
	@Query("select t.name from Tag t")
	public List<String> findAllTag();
}
