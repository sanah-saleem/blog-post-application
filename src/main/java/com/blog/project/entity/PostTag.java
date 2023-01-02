package com.blog.project.entity;

import javax.persistence.*;

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
