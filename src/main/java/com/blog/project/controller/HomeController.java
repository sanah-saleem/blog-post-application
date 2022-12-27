package com.blog.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.project.entity.Comment;
import com.blog.project.entity.Post;
import com.blog.project.servive.CommentService;
import com.blog.project.servive.PostService;
import com.blog.project.servive.TagService;

@Controller
public class HomeController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired 
	private CommentService commentService;
	
	@Autowired
	Post post;
	
	@GetMapping(value={"/","/Dashboard"})
	public String viewHomePage(Model model) {
		model.addAttribute("listPosts", postService.getAllPosts());
		return "Dashboard";
	}
	
	@GetMapping("/addPost")
	public String addPost(Model model) {
		model.addAttribute(post);
		System.out.println("in add post");
		return "AddPost";
	}
	
	@PostMapping("/savePost")
	public String savepost(@ModelAttribute("post") Post post, @RequestParam("id") int id, 
				@RequestParam("tag") String tagName, Model model) {
		postService.addPost(post, id, tagName);
		model.addAttribute("listPosts", postService.getAllPosts());
		return "Dashboard";
	}
	
	@GetMapping("/viewPost/{id}")
	public String viewPost(@PathVariable("id") Integer postId, Model model) {
		System.out.println("in view post");
		System.out.println(postId);
		model.addAttribute("thePost", postService.getPost(postId));
		model.addAttribute("newComment", new Comment());
		System.out.println("hi");
		return "ViewPost";
	}
	
	@GetMapping("/updatePost/{id}")
	public String updatePost(@PathVariable("id") int postId, Model model) {
		model.addAttribute("post",postService.getPost(postId));
		model.addAttribute("tags",tagService.getAllTags(postId));
		return"AddPost";
	}
	
	@GetMapping("/deletePost/{id}")
	public String deletePost(@PathVariable("id") int id) {
		postService.deletePost(id);
		return"redirect:/Dashboard";
	}
	
	@PostMapping("/addComment")
	public String addComment(@ModelAttribute("theComment") Comment theComment, @RequestParam("postId") Integer postId) {
		System.out.println("inside addComment controller");
		commentService.addComment(theComment, postId);
		return "redirect:/viewPost/"+postId;
	}
	
	@GetMapping("/updateComment/{id}")
	public String updateComment(@PathVariable("id") int commentId, @RequestParam("postId") int postId, Model model) {
		Comment theComment = commentService.getComment(commentId);
		commentService.deleteComment(theComment, postId);
		model.addAttribute("thePost", postService.getPost(postId));
		model.addAttribute("newComment", theComment);
		return "ViewPost";
	}
	
	@GetMapping("/deleteComment/{id}")
	public String deleteComment(@PathVariable("id") int commentId, @RequestParam("postId") int postId) {
		Comment theComment = commentService.getComment(commentId);
		commentService.deleteComment(theComment, postId);
		return "redirect:/viewPost/"+postId;
	}
	
	@GetMapping("/searchPost")
	public String searchPost(@RequestParam("search") String parameter, @RequestParam("category") String category, Model model) {
		System.out.println(category+":"+parameter);
		System.out.println(postService.search(category, parameter));
		model.addAttribute("listPosts", postService.search(category, parameter));
		return "Dashboard";
	}
}
