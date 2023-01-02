package com.blog.project.controller;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.project.entity.Comment;
import com.blog.project.entity.Post;
import com.blog.project.entity.User;
import com.blog.project.servive.CommentService;
import com.blog.project.servive.PostService;
import com.blog.project.servive.TagService;
import com.blog.project.servive.UserService;

@Controller
public class HomeController {

	@Autowired
	private PostService postService;
	
	@Autowired
	private TagService tagService;
	
	@Autowired
	UserService userService;
	
	@Autowired 
	private CommentService commentService;
	
	@Autowired
	Post post;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("theUser", new User());
		return "register";
	}
	
	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute("theUser") User theUser) {
		userService.saveUser(theUser);
		return "redirect:/doLogin";
	}
	
//	@PostMapping("/doLogin")
//	public String doLogin() {
//		return "Dashboard";
//	}
	
	@GetMapping(value={"/","/Dashboard","doLogin"}) 
	public String viewHomePage( Model model) {
		return getPaginated( 1, "title", "asc", model);
	}
	
	@GetMapping("/addPost")
	public String addPost(Model model, Principal principal) {
		
		post.setAuthor(principal.getName());
		model.addAttribute("post",post);
		return "AddPost";
		
	}
	
	@PostMapping("/savePost")
	public String savepost(@ModelAttribute("post") Post post, @RequestParam("tag") String tagName, Model model) {
		postService.addPost(post, tagName);
		return viewHomePage(model);
	}
	
	@GetMapping("/viewPost/{id}")
	public String viewPost(@PathVariable("id") Integer postId, Model model, Principal principal) {
		String currentUserName = "nil";
		String currentUserRole = "nil";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    currentUserName = authentication.getName();
		    currentUserRole = authentication.getAuthorities().stream().
		    		anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"))?"ADMIN":"USER";
		}
		model.addAttribute("thePost", postService.getPost(postId));
		model.addAttribute("newComment", new Comment());
		model.addAttribute("username", currentUserName);
		model.addAttribute("role", currentUserRole);
		System.out.println(currentUserRole);
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
	public String addComment(@ModelAttribute("theComment") Comment theComment, 
			@RequestParam("postId") Integer postId, Principal principal) {
		theComment.setName(principal.getName());
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
	public String searchPost(@RequestParam(value="search", required=false) String parameter,
			@RequestParam(value="authors", required=false) ArrayList<String> authors, 
			@RequestParam(value="tags", required=false) ArrayList<String> tags, 
			@RequestParam(value="sortDirection", required=false)String sortDirection, 
			@RequestParam(value="sortBy", required=false)String sortBy, 
			@RequestParam(value="startDate", required=false)String startDate,
			@RequestParam(value="endDate", required=false)String endDate, 
			Model model, Principal principal) throws ParseException {
		
		if(sortDirection==null) 
			sortDirection="asc";
		if(sortBy==null)
			sortBy="title";
		System.out.println(startDate);
		
		System.out.println("1");
		return searchPost(parameter, authors, tags, startDate, endDate, 1, sortBy, sortDirection, model, principal);
	}
	
	@GetMapping("/searchPost/{pageNo}")
	public String searchPost(@RequestParam(value="search", required=false)String parameter, 
			@RequestParam(value="authors", required=false)ArrayList<String> authors, 
			@RequestParam(value="tags", required=false)ArrayList<String> tags,
			@RequestParam(value="startDate", required=false)String startDate, 
			@RequestParam(value="endDate", required=false)String endDate,
			@PathVariable(value="pageNo", required=false)int pageNo, 
			@RequestParam(value="sortField", required=false)String sortField, 
			@RequestParam(value="sortDirection", required=false)String sortDirection, 
			Model model, Principal principal) throws ParseException {
		
		System.out.println(pageNo);
		System.out.println(parameter);
		System.out.println(tags);
		System.out.println(authors);
		
		int pageSize = 5;
		
		Page<Post> page = postService.search(parameter, authors, tags, startDate, endDate, pageNo, pageSize, sortField, sortDirection);
		List<Post> listPosts = page.getContent();
		System.out.println(parameter);
		
		System.out.println("4");
		
		model.addAttribute("search", parameter);
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listPosts", listPosts);
		model.addAttribute("type", 2);
		if(principal==null)
			model.addAttribute("username", "nil");
		else
			model.addAttribute("username", principal.getName());
		model.addAttribute("listAuthor", postService.getAllAuthor());
		model.addAttribute("listTag", postService.getAllTags());
		model.addAttribute("listAuthorSelected", authors==null?Collections.<String>emptyList():authors);
		model.addAttribute("listTagSelected", tags==null?Collections.<String>emptyList():tags);
		model.addAttribute("startDate", startDate==null?"":startDate);
		model.addAttribute("endDate", endDate==null?"":endDate);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", sortDirection);
			
		return "Dashboard";
	}
	
	@GetMapping("/page/{pageNo}")
	public String getPaginated( @PathVariable("pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDirection") String sortDirection, 
			Model model) {
		
		int pageSize = 5;
		String currentUserName = "nil";

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
		    currentUserName = authentication.getName();
		}
		
		Page<Post> page = postService.findPage(pageNo, pageSize, sortField, sortDirection);
		List<Post> listPosts = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("listPosts", listPosts);
		model.addAttribute("type", 1);
		model.addAttribute("search", "");
		model.addAttribute("username", currentUserName);
		model.addAttribute("listAuthor", postService.getAllAuthor());
		model.addAttribute("listTag", postService.getAllTags());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", sortDirection);
		
		return "Dashboard";
	}
}
