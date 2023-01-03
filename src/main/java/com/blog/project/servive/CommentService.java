package com.blog.project.servive;

import java.util.List;

import com.blog.project.entity.Comment;

public interface CommentService {
	
	public void addComment(Comment theComment, int postId);
	
	public void deleteComment(int commentId, int postId);
	
	public void deleteComment(Comment theComment, int postId);
	
	public List<Comment> getComments(int postId);
	
	public Comment getComment(int commentId);
}
