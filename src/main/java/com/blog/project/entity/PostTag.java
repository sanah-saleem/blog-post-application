package com.blog.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="post_tag")
public class PostTag {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	private int id;
	
	@Column(name="post_id")
	private int postId;
	
	@Column(name="tag_id")
	private int tagId;
}
