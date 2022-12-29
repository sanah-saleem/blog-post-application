package com.blog.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	public String viewHomePage( Model model) {
		return viewPaginated( 1, "title", "asc", model);
	}
	
	@GetMapping("/addPost")
	public String addPost(Model model) {
		model.addAttribute(post);
		return "AddPost";
	}
	
	@PostMapping("/savePost")
	public String savepost(@ModelAttribute("post") Post post, @RequestParam("tag") String tagName, Model model) {
		postService.addPost(post, tagName);
		model.addAttribute("listPosts", postService.getAllPosts());
		return "Dashboard";
	}
	
	@GetMapping("/viewPost/{id}")
	public String viewPost(@PathVariable("id") Integer postId, Model model) {
		model.addAttribute("thePost", postService.getPost(postId));
		model.addAttribute("newComment", new Comment());
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
		model.addAttribute("listPosts", postService.search(category, parameter));
		return printPosts(model);
	}
	
	@GetMapping("/page/{pageNo}")
	public String viewPaginated( @PathVariable("pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDirection") String sortDirection, 
			Model model) {
		int pageSize = 5;
		Page<Post> page = postService.findPage(pageNo, pageSize, sortField, sortDirection);
		List<Post> listPosts = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listPosts", listPosts);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("reverseSortDirection", sortDirection.equals("asc")? "desc" : "asc");
		return printPosts(model);
	}
	
	public String printPosts(Model model) {
		return "Dashboard";
	}
//	public String viewPaginated(List<Post> postList, int pageNo, Model model) {
//		int pageSize = 5;
////		Page<Post> page = postService.findPage(pageNo, pageSize);
//		List<Post> listPosts = postService.getPosts(postList, pageNo, pageSize);
//		int reminder = postList.size()%pageSize;
//		int totalPages = postList.size()/pageSize;
//		if(reminder>0) {
//			totalPages += 1;
//		}
//		System.out.println(totalPages);
//		model.addAttribute("currentPage", pageNo);
//		model.addAttribute("totalPages", totalPages);
//		model.addAttribute("mainListPosts", postList);
//		model.addAttribute("listPosts", listPosts);
//		return "Dashboard";
//	}
}
