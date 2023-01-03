package com.blog.project.api;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.project.entity.Comment;
import com.blog.project.entity.Post;
import com.blog.project.entity.SearchAndFilter;
import com.blog.project.servive.CommentService;
import com.blog.project.servive.PostService;
import com.blog.project.servive.UserService;

@RestController
public class BlogController {
	
	@Autowired
	PostService postService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CommentService commentService;

	@GetMapping("/api/getBlogs")
	public ResponseEntity<List<Post>> getBlogs() {
		return new ResponseEntity<List<Post>>(postService.getAllPosts(), HttpStatus.OK);
	} 
	
	@GetMapping("/api/getBlog/{id}")
	public ResponseEntity<Post> getBlog(@PathVariable("id") int postId) {
		return new ResponseEntity<Post>(postService.getPost(postId), HttpStatus.OK);
	}
	
	@PostMapping("/api/addBlog")
	public ResponseEntity<HttpStatus> createBlog(@RequestBody Post post, 
			@RequestParam("tags") String tags, Principal principal) {
			post.setAuthor(principal.getName());
			postService.addPost(post, tags); 
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/deleteBlog/{id}")
	public ResponseEntity<HttpStatus> deleteBlog(@PathVariable("id") int postId, 
			@RequestHeader String token, Principal principal) {
		if(principal.getName().equals(postService.getPost(postId).getAuthor())
				|| userService.findByUsername(principal.getName()).equals("ROLE_ADMIN")) {
			if(postService.getPost(postId)!=null) {
			postService.deletePost(postId);
			return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@PutMapping("/api/updateBlog/{id}")
	public ResponseEntity<HttpStatus> updateBlog(@PathVariable("id") int postId, 
			@RequestBody Post post, @RequestParam("tags") String tags, Principal principal) {
		if(principal.getName().equals(postService.getPost(postId).getAuthor())
				|| userService.findByUsername(principal.getName()).equals("ROLE_ADMIN")) {
			if(postService.getPost(postId)!=null) {
				if(userService.findByUsername(principal.getName()).equals("ROLE_ADMIN")) {
					post.setAuthor(postService.getPost(postId).getAuthor());
				}
				else {
					post.setAuthor(principal.getName());
				}
				post.setId(postId);
				postService.addPost(post, tags);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@PostMapping("/api/addComment/{id}")
	public ResponseEntity<HttpStatus> addComment(@PathVariable("id") int postId, 
			@RequestBody Comment comment, Principal principal) {
		if(postService.getPost(postId)!=null) {
			comment.setName(principal.getName());
			commentService.addComment(comment, postId);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/api/deleteComment/{postId}/{commentId}")
	public ResponseEntity<HttpStatus> deleteComment(@PathVariable("postId") int postId, 
			@PathVariable("commentId") int commentId, Principal principal) {
		if(principal.getName().equals(commentService.getComment(commentId).getName())
				|| userService.findByUsername(principal.getName()).equals("ROLE_ADMIN")) {
			if(postService.getPost(postId)!=null&&commentService.getComment(commentId)!=null) {
				commentService.deleteComment(commentId, postId);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@PutMapping("/api/updateComment/{postId}/{commentId}")
	public ResponseEntity<HttpStatus> updateComment(@PathVariable("postId") int postId, 
			@PathVariable("commentId") int commentId, @RequestBody Comment comment, Principal principal)  {
		if(principal.getName().equals(commentService.getComment(commentId).getName())
				|| userService.findByUsername(principal.getName()).equals("ROLE_ADMIN")) {
				String name = principal.getName();
			if(postService.getPost(postId)!=null&&commentService.getComment(commentId)!=null) {
				if(userService.findByUsername(name).equals("ROLE_ADMIN")) {
					name=commentService.getComment(commentId).getName();
				}
				commentService.deleteComment(commentService.getComment(commentId), postId);
				comment.setId(commentId);
				comment.setName(name);
				commentService.addComment(comment, postId);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@GetMapping("/api/search/{pageNo}")
	public ResponseEntity<List<Post>> search(@PathVariable("pageNo") int pageNo, 
			@RequestBody SearchAndFilter searchAndFilter ) throws ParseException {
		
		List<Post> listPost = postService.search(searchAndFilter.getParameter(), searchAndFilter.getAuthors(), searchAndFilter.getTags(), 
				searchAndFilter.getStartDate(), searchAndFilter.getEndDate(), pageNo, 5, 
				searchAndFilter.getSortField(), searchAndFilter.getSortDirection()).getContent();
		return new ResponseEntity<List<Post>>(listPost, HttpStatus.OK);
	}
	
}
