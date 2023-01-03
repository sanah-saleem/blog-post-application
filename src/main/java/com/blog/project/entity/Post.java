package com.blog.project.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="post")
@Component
public class Post {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY )
	@Column(name = "id", insertable=false, updatable=false)
	private int id;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "excerpt")
	private String excerpt;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "author")
	private String author;
	
	@CreationTimestamp
	@Column(name = "published_at", updatable=false)
	private Timestamp publishedAt;
	
	@Column(name = "is_published")
	private boolean isPublished = false;
	
	@CreationTimestamp
	@Column(name = "created_at", updatable=false)
	private Timestamp createdAt;
	
	@UpdateTimestamp
	@Column(name = "updated_at")
	private Timestamp updatedAt;
	
	@ManyToMany(targetEntity=Tag.class, fetch=FetchType.EAGER, cascade = 
		{CascadeType.DETACH,
			CascadeType.MERGE,
			CascadeType.PERSIST,
			CascadeType.REFRESH})
	@JoinTable(
			name="post_tag",
			joinColumns=@JoinColumn(name="post_id"),
			inverseJoinColumns=@JoinColumn(name="tag_id"))
	List<Tag> tags;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	List<Comment> comments;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExcerpt() {
		return excerpt;
	}

	public void setExcerpt(String excerpt) {
		this.excerpt = excerpt;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Timestamp getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Timestamp dateTime) {
		this.publishedAt = dateTime;
	}

	public boolean isPublished() {
		return isPublished;
	}

	public void setPublished(boolean isPublished) {
		this.isPublished = isPublished;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp dateTime) {
		this.createdAt = dateTime;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp dateTime) {
		this.updatedAt = dateTime;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + ", excerpt=" + excerpt + ", content=" + content + ", author="
				+ author + ", publishedAt=" + publishedAt + ", isPublished=" + isPublished + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", tags=" + tags + ", comments=" + comments + "]";
	}
	
	public void addComment(Comment comment) {
		if(comments == null) {
			comments = new ArrayList<>();
		}
		comments.add(comment);
	}
	
}
