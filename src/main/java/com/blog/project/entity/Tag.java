package com.blog.project.entity;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="tag")
@Component
public class Tag {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@CreationTimestamp
	@Column(name="created_at", updatable=false)
	private Timestamp createdAt;
	
	@UpdateTimestamp
	@Column(name="updated_at")
	private Timestamp updatedAt;
	
	
	@ManyToMany(targetEntity=Post.class, fetch=FetchType.LAZY, cascade = 
		{CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.PERSIST,
			CascadeType.REFRESH}, mappedBy = "tags")
	
	List<Post> posts;

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Timestamp getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}


	public Timestamp getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}


	public List<Post> getPosts() {
		return posts;
	}

	
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
}
